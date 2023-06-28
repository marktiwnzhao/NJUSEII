package org.fffd.l23o6.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.OrderDao;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.fffd.l23o6.exception.BizError;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.fffd.l23o6.service.OrderService;
import org.fffd.l23o6.util.strategy.payment.PaymentStrategy;
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
        OrderEntity order = OrderEntity.builder().trainId(trainId).userId(userId).seat(seat)
                .status(OrderStatus.PENDING_PAYMENT).arrivalStationId(toStationId).departureStationId(fromStationId)
                .build();
        train.setUpdatedAt(null);// force it to update
        trainDao.save(train);
        orderDao.save(order);
        return order.getId();
    }

    public List<OrderVO> listOrders(String username) {
        Long userId = userDao.findByUsername(username).getId();
        List<OrderEntity> orders = orderDao.findByUserId(userId);
        orders.sort((o1,o2)-> o2.getId().compareTo(o1.getId()));
        return orders.stream().map(order -> {
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
                .build();
    }

    public void cancelOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        // TODO: refund user's money and credits if needed

        double refundAmount = calculateAmount(order);
        Long userId = order.getUserId();
//        refundUser(userId, refundAmount);


        order.setStatus(OrderStatus.CANCELLED);
        orderDao.save(order);
    }

    private double calculateAmount(OrderEntity order) {
        TrainEntity trainEntity = trainDao.findById(order.getTrainId()).get();
        Long mileagePoints = userDao.findById(order.getUserId()).get().getMileagePoints();
        List<Long> stationIds = routeDao.findById(trainEntity.getRouteId()).get().getStationIds();
        double basePrice = calculateRawPaymentByStationIds(stationIds, trainEntity.getTrainType());
        double v = calculatePaymentByPoints(mileagePoints, basePrice);
        return v;

    }

    public void payOrder(Long id) {
        OrderEntity order = orderDao.findById(id).get();

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BizException(BizError.ILLEAGAL_ORDER_STATUS);
        }

        // TODO: use payment strategy to pay!
//        double amountToPay = calculateAmountToPay(order);
//        Long userId = order.getUserId();
//        processPayment(userId, amountToPay);
        // TODO: update user's credits, so that user can get discount next time


        order.setStatus(OrderStatus.COMPLETED);
        orderDao.save(order);
    }
    private  final double[][] POINTS_DISCOUNT_TABLE = {
            {1000, 0.1},
            {2000, 0.15},
            {7000, 0.2},
            {40000, 0.25},
            {Double.POSITIVE_INFINITY, 0.3} // 用无穷大来表示50000以上的积分
    };
    /**
     * @Description:  通过积分和basePrice来计算价格
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

            if (remainingPoints <= pointsRange) {
                discount += remainingPoints * discountRate;
                break;
            } else {
                discount += pointsRange * discountRate;
                remainingPoints -= pointsRange;
            }
        }

        return basePrice - discount;
    }
    /**
     * @Description: 普通过一个站20块, 高铁过一站40
     * @Param: [stationIds]
     * @return: double
     * @Date: 2023/6/28
     */
    public double calculateRawPaymentByStationIds(List<Long> stationIds, TrainType type){
        if(TrainType.HIGH_SPEED==type){
            return stationIds.size()*40;
        } else if(TrainType.NORMAL_SPEED==type){
            return  stationIds.size()*20;
        }
        return 0;
    }
}
