package com.mikumifa.wms.entity;

import com.mikumifa.wms.entity.dto.Customer;
import com.mikumifa.wms.entity.dto.OrderInfo;
import com.mikumifa.wms.entity.dto.TicketInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 23:17
 **/
@Data
public class OrderInfoVo {
    private Integer orderId;

    private CustomerVo customerVo;

    private TicketInfoVo ticketInfoVo;
    private String orderStatus;
    private static HashMap<Integer, String> statusMap = new HashMap<Integer, String>() {
        {
            put(0, "未支付");
            put(1, "已支付");
            put(2, "已过期");
            put(3, "已退款");

        }


    };
    public OrderInfoVo(OrderInfo po, CustomerVo customerVo, TicketInfoVo ticketInfoVo) {
        BeanUtils.copyProperties(po, this);
        this.customerVo = customerVo;
        this.ticketInfoVo = ticketInfoVo;
        this.orderStatus = statusMap.get(po.getOrderStatus());
    }
}
