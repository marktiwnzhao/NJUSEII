package org.fffd.l23o6.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.payment.AliStrategy;
import org.fffd.l23o6.util.strategy.payment.PaymentStrategy;
import org.fffd.l23o6.util.strategy.payment.WeChatStrategy;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final TrainDao trainDao;
    private final RouteDao routeDao;
    PaymentStrategy paymentStrategy = new WeChatStrategy();

    public Long createOrder(String username, Long trainId, Long fromStationId, Long toStationId, String seatType,
                            Long seatNumber) {
        Long userId = userDao.findByUsername(username).getId();
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        int startStationIndex = route.getStationIds().indexOf(fromStationId);
        int endStationIndex = route.getStationIds().indexOf(toStationId);
        String seat = null;
        switch (train.getTrainType()) {
            case HIGH_SPEED:
                seat = GSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType), train.getSeats());
                break;
            case NORMAL_SPEED:
                seat = KSeriesSeatStrategy.INSTANCE.allocSeat(startStationIndex, endStationIndex,
                        KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType), train.getSeats());
                break;
        }
        if (seat == null) {
            throw new BizException(BizError.OUT_OF_SEAT);
        }
        UserEntity user = userDao.findById(userId).get();
        Long rawPoint = user.getMileagePoints();
        Double rawMoney = null;
        TrainType trainType = train.getTrainType();
        switch (trainType) {
            case HIGH_SPEED:
                rawMoney= calculateRawPaymentByTrainInfo(train,fromStationId, toStationId,GSeriesSeatStrategy.GSeriesSeatType.fromString(seatType).getText() );
                break;
            case NORMAL_SPEED:
                rawMoney= calculateRawPaymentByTrainInfo(train, fromStationId, toStationId,  KSeriesSeatStrategy.KSeriesSeatType.fromString(seatType).getText() );
                break;
        }
        double money = calculatePaymentByPoints(rawPoint, rawMoney);
        long usedPoint=calculateUsedPoints(rawPoint, rawMoney);
        long point =moneyToPoint(rawMoney);
        OrderEntity order = OrderEntity.builder().trainId(trainId).userId(userId).seat(seat)
                .status(OrderStatus.PENDING_PAYMENT).arrivalStationId(toStationId).departureStationId(fromStationId)
                .rawMoney(rawMoney)
                .point(point)
                .rawPoint(rawPoint)
                .money(money)
                .usedPoint(usedPoint)
                .build();
        train.setUpdatedAt(null);// force it to update
        trainDao.save(train);
        orderDao.save(order);
        return order.getId();
    }
    public Long moneyToPoint(double money){
        return  (long)money*10;
    }

    public List<OrderVO> listOrders(String username) {
        Long userId = userDao.findByUsername(username).getId();
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1, o2) -> o2.getId().compareTo(o1.getId()));
        return orders.stream().filter(order -> {
            TrainEntity train = trainDao.findById(order.getTrainId()).orElse(null);
            if(train==null) return false;
            RouteEntity route = routeDao.findById(train.getRouteId()).orElse(null);
            return route != null;
        }).map(order -> {
            TrainEntity train = trainDao.findById(order.getTrainId()).get();
            RouteEntity route = routeDao.findById(train.getRouteId()).get();
            int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
            int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
            return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                    .seat(order.getSeat()).status(order.getStatus().getText())
                    .createdAt(order.getCreatedAt())
                    .startStationId(order.getDepartureStationId())
                    .endStationId(order.getArrivalStationId())
                    .departureTime(train.getDepartureTimes().get(startIndex))
                    .arrivalTime(train.getArrivalTimes().get(endIndex))
                    .build();
        }).collect(Collectors.toList());
    }

    public OrderVO getOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();
        TrainEntity train = trainDao.findById(order.getTrainId()).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();


        int startIndex = route.getStationIds().indexOf(order.getDepartureStationId());
        int endIndex = route.getStationIds().indexOf(order.getArrivalStationId());
        return OrderVO.builder().id(order.getId()).trainId(order.getTrainId())
                .seat(order.getSeat()).status(order.getStatus().getText())
                .createdAt(order.getCreatedAt())
                .startStationId(order.getDepartureStationId())
                .endStationId(order.getArrivalStationId())
                .departureTime(train.getDepartureTimes().get(startIndex))
                .arrivalTime(train.getArrivalTimes().get(endIndex))
                .rawMoney(order.getRawMoney())
                .point(order.getPoint())
                .rawPoint(order.getRawPoint())
                .money(order.getMoney())
                .usedPoint(order.getUsedPoint())
                .build();
    }

    /**
     * @Description: 取消订单, 当这个订单是PAID的时候, 首先 去计算他的basePrice,, 通过basePrice 去退积分, 然后通过paymentStrategy去退款,其中会设置订单的状态
     * @Param: [id]
     * @return: void
     * @Date: 2023/6/29
     */
    public void cancelOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        if (order.getStatus() == OrderStatus.PAID||order.getStatus() == OrderStatus.PENDING_PAYMENT) {
            TrainEntity trainEntity = trainDao.findById(order.getTrainId()).get();
            TrainType trainType = trainEntity.getTrainType();
            UserEntity userEntity = userDao.findById(order.getUserId()).get();
            RouteEntity routeEntity = routeDao.findById(trainEntity.getRouteId()).get();
            List<Long> stationIds = routeEntity.getStationIds();
            int start = stationIds.indexOf(order.getDepartureStationId());
            int end = stationIds.indexOf(order.getArrivalStationId());
            switch (trainType) {
                case HIGH_SPEED:
                    trainEntity.setSeats(GSeriesSeatStrategy.INSTANCE.refundSeat(start, end,order.getSeat(),trainEntity.getSeats()));
                    break;
                case NORMAL_SPEED:
                    trainEntity.setSeats(KSeriesSeatStrategy.INSTANCE.refundSeat(start, end,order.getSeat(),trainEntity.getSeats()));
                    break;
            }
            trainEntity.setUpdatedAt(null);// force it to update
            trainDao.saveAndFlush(trainEntity);
            //只有在支付情况下,才会退款和退积分
            if(order.getStatus() == OrderStatus.PAID){
                userEntity.setMileagePoints(order.getRawPoint());
                if(paymentStrategy.refund(order)) {
                    userDao.save(userEntity);
                    order.setStatus(OrderStatus.CANCELLED);
                }
            } else {
                order.setStatus(OrderStatus.CANCELLED);
            }

        }

        orderDao.save(order);
    }

    public void setPaymentStrategy(int strategy) {
        if (strategy == 1) {
            paymentStrategy = new WeChatStrategy();
        } else {
            paymentStrategy = new AliStrategy();
        }
    }
    /**
     * @Description: 根据订单, 计算要花费的钱, 并且保存订单的信息和用户的信息, 首先分配作为, 计算basePrice, 计算打折后的价格, 计算支付完成后得到的积分, 保存订单, 保存积分
     * @Param: id
     * @return: double
     * @Date: 2023/6/29
     */
    public void payOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }
        UserEntity userEntity = userDao.findById(order.getUserId()).get();
        userEntity.setMileagePoints(userEntity.getMileagePoints()- order.getUsedPoint()+order.getPoint());
        if(paymentStrategy.pay(order)) {
            userDao.save(userEntity);
            order.setStatus(OrderStatus.PAID);
            orderDao.save(order);
        }
    }

    private final double[][] POINTS_DISCOUNT_TABLE = {
            {1000, 0.1},
            {3000, 0.15},
            {10000, 0.2},
            {50000, 0.25},
            {Double.POSITIVE_INFINITY, 0.3} // 用无穷大来表示50000以上的积分
    };

    /**
     * @Description: 通过积分和basePrice来计算价格
     * @Param: [mileagePoints, basePrice]
     * @return: double
     * @Date: 2023/6/28
     */
    public double calculatePaymentByPoints(Long mileagePoints, double basePrice) {
        double discount = 0;
        long remainingPoints = mileagePoints;
        for (double[] pointsDiscount : POINTS_DISCOUNT_TABLE) {
            long pointsRange = (long) pointsDiscount[0];
            double discountRate = pointsDiscount[1] / 100; // 折扣率，转换为小数
            //大于了就可以减去
            if (remainingPoints >= pointsRange) {
                discount += basePrice * discountRate;
            } else {
                break;
            }
        }
        return basePrice - discount;
    }
    public Long calculateUsedPoints(Long mileagePoints, double basePrice) {
        long usedPoints=0;

        for (double[] pointsDiscount : POINTS_DISCOUNT_TABLE) {
            long pointsRange = (long) pointsDiscount[0];

            if (mileagePoints >= pointsRange) {
                usedPoints=pointsRange;
            } else {
                break;

            }
        }
        return usedPoints;
    }

    /**
     * @Description:
     * @Param: [stationIds]
     * @return: double
     * @Date: 2023/6/28
     */
    public double calculateRawPaymentByTrainInfo(TrainEntity entity, Long start, Long end, String type) {

        TrainVO trainVO = TrainMapper.toTrainVO(entity, routeDao, start, end);

        List<TicketInfo> ticketInfo = trainVO.getTicketInfo();
        for (var t : ticketInfo) {
            if (Objects.equals(t.getType(), type)) {
                return t.getPrice();
            }
        }
        return 0;
    }
}
