package com.mikumifa.wms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @program: se2-work
 * @description: TicketInfo
 * @author: Xie
 * @create: 2023-04-07 16:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketInfo {
    private Integer ticketId;
    private Integer routeId;
    private BigDecimal price;
    private Integer seatClass;
    private Integer seatNo;
    private Integer selled;
}
