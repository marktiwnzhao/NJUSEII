package com.mikumifa.wms.service;

import com.mikumifa.wms.entity.OrderInfoVo;
import com.mikumifa.wms.entity.TicketInfoVo;

import java.util.Date;
import java.util.List;

public interface TicketService {
    public List<TicketInfoVo> getByInfo(String from, String to, Date departureTime,Integer selled);
    public TicketInfoVo getById(Integer ticketId);
    public OrderInfoVo buyById(Integer ticketId,Integer customerId);
}
