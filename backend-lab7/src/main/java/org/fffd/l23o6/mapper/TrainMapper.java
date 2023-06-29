package org.fffd.l23o6.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: l23o6
 * @description:
 * @author: Xie
 * @create: 2023-05-27 13:24
 **/
public interface TrainMapper  {
    static TrainVO toTrainVO(TrainEntity trainEntity, RouteDao routeDao,Long startStationId, Long endStationId) {



        TrainVO trainVO = new TrainVO();
        trainVO.setName(trainEntity.getName());
        trainVO.setId(trainEntity.getId());


        Long routeId = trainEntity.getRouteId();
        RouteEntity route = routeDao.findById(routeId).get();
        List<Long> stationIds = route.getStationIds();
        //开始的站, 结束的站
        int start = stationIds.indexOf(startStationId);
        int end = stationIds.indexOf(endStationId);


        //结束的到达时间
        trainVO.setArrivalTime(trainEntity.getArrivalTimes().get(end));
        //开始站的出发时间
        trainVO.setDepartureTime(trainEntity.getDepartureTimes().get(start));
        trainVO.setTrainType(trainEntity.getTrainType().getText());

        trainVO.setStartStationId(startStationId);
        trainVO.setEndStationId(endStationId);
        //下面要得到每个作为有多少的人,
        List<TicketInfo> tickets =new ArrayList<>();
        @NotNull boolean[][] seats = trainEntity.getSeats();

        if(trainEntity.getTrainType()== TrainType.HIGH_SPEED){
            Map<GSeriesSeatStrategy.GSeriesSeatType, Integer> gSeriesSeatTypeIntegerMap = GSeriesSeatStrategy.INSTANCE.getLeftSeatCount(start, end, seats);

            for (var type : GSeriesSeatStrategy.GSeriesSeatType.values()) {
                String text = type.getText();
                if(gSeriesSeatTypeIntegerMap.get(type)==null)
                    continue;
                tickets.add(new TicketInfo(text,gSeriesSeatTypeIntegerMap.get(type),GSeriesSeatStrategy.INSTANCE.price(type,end-start+1).intValue()));
            }
        } else if (trainEntity.getTrainType()==TrainType.NORMAL_SPEED) {
            Map<KSeriesSeatStrategy.KSeriesSeatType, Integer> kSeriesSeatTypeIntegerMap = KSeriesSeatStrategy.INSTANCE.getLeftSeatCount(start, end, seats);

            for (var type : KSeriesSeatStrategy.KSeriesSeatType.values()) {
                String text = type.getText();
                if(kSeriesSeatTypeIntegerMap.get(type)==null)
                    continue;
                tickets.add(new TicketInfo(text,kSeriesSeatTypeIntegerMap.get(type),KSeriesSeatStrategy.INSTANCE.price(type,end-start+1).intValue()));
            }
        }
        trainVO.setTicketInfo(tickets);
        return trainVO;
    }

    static  TrainDetailVO toTrainDetailVO(TrainEntity trainEntity,RouteDao routeDao) {
        RouteEntity routeEntity = routeDao.findById(trainEntity.getRouteId()).get();
        return new TrainDetailVO(
                trainEntity.getId(),
                trainEntity.getName(),
                trainEntity.getTrainType().getText(),
                routeEntity.getStationIds(),
                trainEntity.getDate(),
                trainEntity.getDepartureTimes(),
                trainEntity.getArrivalTimes(),
                trainEntity.getExtraInfos()
        );
    }

    static AdminTrainVO toAdminTrainVO(TrainEntity trainEntity) {
        return new AdminTrainVO(
                trainEntity.getId(),
                trainEntity.getName(),
                trainEntity.getTrainType().getText(),
                trainEntity.getRouteId(),
                trainEntity.getDate(),
                trainEntity.getDepartureTimes(),
                trainEntity.getArrivalTimes(),
                trainEntity.getExtraInfos()
        );
    }
}
