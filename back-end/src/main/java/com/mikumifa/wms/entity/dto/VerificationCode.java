package com.mikumifa.wms.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @program: se2-work
 * @description: VerificationCode
 * @author: Xie
 * @create: 2023-04-07 16:28
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationCode {
    private Integer id;
    private String email;
    private String code;
    private Date expireTime;
    private Date createTime;
}
