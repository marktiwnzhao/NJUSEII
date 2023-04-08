package com.mikumifa.wms.entity;

import com.mikumifa.wms.entity.dto.Customer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:54
 **/
@Data
public class CustomerVo {
    private Integer customerId;
    private String name;
    private String email;
    private String idCardInfo;
    private Integer creditScore;
    public  CustomerVo(Customer po){
        BeanUtils.copyProperties(po, this);


    }
}
