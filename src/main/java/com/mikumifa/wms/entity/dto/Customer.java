package com.mikumifa.wms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: se2-work
 * @description: Customer
 * @author: Xie
 * @create: 2023-04-07 16:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    private Integer customerId;
    private String name;
    private String email;
    private String idCardInfo;
    private Integer creditScore;
    private String password;
}
