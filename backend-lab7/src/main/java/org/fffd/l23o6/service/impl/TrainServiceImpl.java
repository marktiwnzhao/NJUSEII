package org.fffd.l23o6.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    // 根据列车ID获取列车详细信息
    @Override
    public TrainDetailVO getTrain(Long trainId) {
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        return TrainDetailVO.builder()
                .id(trainId)
                .date(train.getDate())
                .name(train.getName())
                .stationIds(route.getStationIds())
                .arrivalTimes(train.getArrivalTimes())
                .departureTimes(train.getDepartureTimes())
                .extraInfos(train.getExtraInfos())
                .build();
    }

    /**
     * 获取符合条件的列车列表
     *
     * @param startStationId  起始站点ID
     * @param endStationId    目的站点ID
     * @param date            列车日期
     * @return 符合条件的列车列表
     */
    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        // 首先，获取所有包含起始站点和目的站点的路线
        // 然后，获取当天具有所需路线的所有列车
        List<RouteEntity> all = routeDao.findAll();

        List<Long> routes = all.stream()
                .filter(new Predicate<RouteEntity>() {
                    @Override
                    public boolean test(RouteEntity t) {
                        List<Long> stationIds = t.getStationIds();
                        if (stationIds == null)
                            return false;
                        int start = stationIds.indexOf(startStationId);
                        int end = stationIds.indexOf(endStationId);
                        return start != -1 && end != -1 && start < end;
                    }
                })
                .map(new Function<RouteEntity, Long>() {
                    @Override
                    public Long apply(RouteEntity routeEntity) {
                        return routeEntity.getId();
                    }
                })
                .toList();
        List<TrainEntity> trains = trainDao.findAll();
        return trains.stream()
                .filter(new Predicate<TrainEntity>() {
                    @Override
                    public boolean test(TrainEntity trainEntity) {
                        if (routes.contains(trainEntity.getRouteId())) {
                            Long routeId = trainEntity.getRouteId();
                            RouteEntity routeEntity = routeDao.findById(routeId).get();
                            int start = routeEntity.getStationIds().indexOf(startStationId);
                            // 开始站的出发时间
                            List<Date> departureTimes = trainEntity.getDepartureTimes();
                            Date departureDate = departureTimes.get(start);
                            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                if (fmt.format(departureDate).equals(fmt.format(fmt.parse(date))))
                                    return true;
                                else
                                    return false;
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        } else
                            return false;
                    }
                })
                .map(trainEntity -> TrainMapper.toTrainVO(trainEntity, routeDao, startStationId, endStationId))
                .toList();
    }


    // 获取所有列车列表（管理员用）
    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream()
                .map(TrainMapper::toAdminTrainVO)
                .collect(Collectors.toList());
    }

    // 添加列车
    /**
     * 添加列车
     *
     * @param name            列车名称
     * @param routeId         路线ID
     * @param type            列车类型
     * @param date            列车日期
     * @param arrivalTimes    到达时间列表
     * @param departureTimes  出发时间列表
     */
    @Override
    public void addTrain(String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                         List<Date> departureTimes) {
        // 创建列车实体对象
        TrainEntity entity = TrainEntity.builder()
                .name(name)
                .routeId(routeId)
                .trainType(type)
                .date(date)
                .arrivalTimes(arrivalTimes)
                .departureTimes(departureTimes)
                .build();

        // 获取路线信息
        RouteEntity route = routeDao.findById(routeId).get();

        // 检查到达时间和出发时间列表的长度是否与路线站点数量一致
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }

        // 设置额外信息列表，初始化为每个站点的预计正点时间
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));

        // 根据列车类型设置座位信息
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }

        // 保存列车实体对象
        trainDao.save(entity);
    }


    /**
     * 修改列车信息
     *
     * @param id              列车ID
     * @param name            列车名称
     * @param routeId         路线ID
     * @param type            列车类型
     * @param date            列车日期
     * @param arrivalTimes    到达时间列表
     * @param departureTimes  出发时间列表
     */
    @Override
    public void changeTrain(Long id, String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                            List<Date> departureTimes) {
        // 查找现有的TrainEntity
        TrainEntity entity = trainDao.findById(id).get();

        // 更新TrainEntity字段
        entity.setName(name);
        entity.setRouteId(routeId);
        entity.setTrainType(type);
        entity.setDate(date);
        entity.setArrivalTimes(arrivalTimes);
        entity.setDepartureTimes(departureTimes);

        // 获取路线信息
        RouteEntity route = routeDao.findById(routeId).get();

        // 检查到达时间和出发时间列表的长度是否与路线站点数量一致
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }

        // 设置额外信息列表，初始化为每个站点的预计正点时间
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));

        // 根据列车类型设置座位信息
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }

        // 保存更新后的TrainEntity
        trainDao.save(entity);
    }


    //删除列车
    @Override
    public void deleteTrain(Long id) {
        trainDao.deleteById(id);
    }
}
