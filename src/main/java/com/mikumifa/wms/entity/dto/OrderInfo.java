package com.mikumifa.wms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {
    private Integer orderId;
    private Integer customerId;
    private Integer ticketId;
    private Integer orderStatus;
}
