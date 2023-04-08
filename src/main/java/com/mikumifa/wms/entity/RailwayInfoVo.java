package com.mikumifa.wms.entity;

import com.mikumifa.wms.entity.dto.RailwayInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.sql.Date;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-08 00:52
 **/
@Data
public class RailwayInfoVo {
    private Integer routeId;
    private String origin;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;
    public RailwayInfoVo(RailwayInfo po){
        BeanUtils.copyProperties(po,this);
    }
}
