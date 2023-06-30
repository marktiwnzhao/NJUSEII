package org.fffd.l23o6.mapper;

import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.vo.order.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "status", source = "entity.status.text")
    @Mapping(target = "startStationId", source = "entity.departureStationId")
    @Mapping(target = "endStationId", source = "entity.arrivalStationId")
    @Mapping(target = "money", constant = "0.0")
    @Mapping(target = "point", constant = "0L")
    @Mapping(target = "rawMoney", constant = "0.0")
    @Mapping(target = "rawPoint", constant = "0L")
    @Mapping(target = "usedPoint", constant = "0L")
    OrderVO toOrderVO(OrderEntity entity);
}