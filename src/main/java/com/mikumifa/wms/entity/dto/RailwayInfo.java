package com.mikumifa.wms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


/**
 * @program: se2-work
 * @description: RailwayInfo
 * @author: Xie
 * @create: 2023-04-07 16:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RailwayInfo {
    private Integer routeId;
    private String origin;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;
}
