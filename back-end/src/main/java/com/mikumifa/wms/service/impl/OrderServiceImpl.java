package com.mikumifa.wms.service.impl;

import com.mikumifa.wms.dao.mapper.OrderInfoMapper;
import com.mikumifa.wms.entity.CustomerVo;
import com.mikumifa.wms.entity.OrderInfoVo;
import com.mikumifa.wms.entity.TicketInfoVo;
import com.mikumifa.wms.entity.dto.OrderInfo;
import com.mikumifa.wms.service.CustomerService;
import com.mikumifa.wms.service.OrderService;
import com.mikumifa.wms.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-08 11:37
 **/
@Service
public class OrderServiceImpl<CustomService> implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CustomerService customService;
    @Autowired
    TicketService ticketService;

    @Override
    public OrderInfoVo getById(Integer orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo == null) {
            LOGGER.warn(String.format("orderId:%s查询失败", orderId));
            return null;
        } else {
            TicketInfoVo ticketInfoVo = ticketService.getById(orderInfo.getTicketId());
            CustomerVo customerVo = customService.getInfoById(orderInfo.getTicketId());
            if (customerVo == null || ticketInfoVo == null) {
                LOGGER.warn(String.format("orderId:%s查询失败", orderId));
                return null;
            } else {
                OrderInfoVo orderInfoVo = new OrderInfoVo(orderInfo, customerVo, ticketInfoVo);
                LOGGER.info(String.format("查询成功:%s", orderInfoVo));
                return orderInfoVo;
            }
        }
    }

    @Override
    public boolean buyById(Integer orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo.getOrderStatus() == 0) {
            orderInfo.setOrderStatus(1);
            int num = orderInfoMapper.updateByPrimaryKey(orderInfo);
            if(num==0){
                LOGGER.info(String.format("购买失败:%d", orderId));
            }
            return true;
        } else {
            return false;

        }
    }

    @Override
    public List<OrderInfoVo> getByInfo(Integer customerId, Integer ticketId, Integer orderStatus) {
        List<OrderInfo> orderInfos = orderInfoMapper.selectBySelective(customerId, ticketId, orderStatus);
        List<OrderInfoVo> voList = new ArrayList<>();
        for (OrderInfo orderInfo : orderInfos) {
            TicketInfoVo ticketInfoVo = ticketService.getById(orderInfo.getTicketId());
            CustomerVo customerVo = customService.getInfoById(orderInfo.getTicketId());
            if (customerVo == null || ticketInfoVo == null) {
                LOGGER.warn(String.format("orderId:%d,错误", orderInfo.getOrderId()));
            }
            voList.add(new OrderInfoVo(orderInfo, customerVo, ticketInfoVo));
        }
        LOGGER.info(String.format("查询:%s", voList));
        return voList;

    }

    @Override
    public boolean refundById(Integer orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo.getOrderStatus() == 1) {
            TicketInfoVo ticketInfoVo = ticketService.getById(orderInfo.getTicketId());
            CustomerVo customerVo = customService.getInfoById(orderInfo.getTicketId());
            if (customerVo == null || ticketInfoVo == null) {
                LOGGER.warn(String.format("orderId:%d,错误", orderInfo.getOrderId()));
            }
            if (ticketInfoVo.getRailwayInfo() == null) {
                LOGGER.warn(String.format("orderId:%d,错误", orderInfo.getOrderId()));
                return false;
            }
            Date arrivalTime = ticketInfoVo.getRailwayInfo().getArrivalTime();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = new Date(System.currentTimeMillis());
            if (arrivalTime.compareTo(nowDate) <= 0) {
                LOGGER.info(String.format("退款超时orderId:%d", orderId));
                return false;
            }
            orderInfo.setOrderStatus(3);
            LOGGER.info(String.format("退款成功orderId:%d", orderId));
            orderInfoMapper.updateByPrimaryKey(orderInfo);
            return true;
        } else {
            return false;

        }
    }
}
