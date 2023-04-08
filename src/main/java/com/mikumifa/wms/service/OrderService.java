package com.mikumifa.wms.service;

import com.mikumifa.wms.entity.OrderInfoVo;

import java.util.List;

public interface OrderService {
    public OrderInfoVo getById(Integer orderId);
    public boolean buyById(Integer orderId);
    public List<OrderInfoVo> getByInfo(Integer customerId, Integer ticketId, Integer orderStatus);
    public boolean refundById(Integer orderId);

}
