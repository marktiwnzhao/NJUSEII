package com.mikumifa.wms.service.impl;

import com.mikumifa.wms.dao.mapper.CustomerMapper;
import com.mikumifa.wms.dao.mapper.OrderInfoMapper;
import com.mikumifa.wms.dao.mapper.RailwayInfoMapper;
import com.mikumifa.wms.dao.mapper.TicketInfoMapper;
import com.mikumifa.wms.entity.CustomerVo;
import com.mikumifa.wms.entity.OrderInfoVo;
import com.mikumifa.wms.entity.TicketInfoVo;
import com.mikumifa.wms.entity.dto.Customer;
import com.mikumifa.wms.entity.dto.OrderInfo;
import com.mikumifa.wms.entity.dto.RailwayInfo;
import com.mikumifa.wms.entity.dto.TicketInfo;
import com.mikumifa.wms.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 23:25
 **/
@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    @Autowired
    TicketInfoMapper ticketInfoMapper;
    @Autowired
    RailwayInfoMapper railwayInfoMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Override
    public List<TicketInfoVo> getByInfo(String origin, String destination, Date departureTime,Integer selled) {

        List<TicketInfo> ticketInfos = ticketInfoMapper.selectByInfo(origin, destination, departureTime,0 );
        List<TicketInfoVo> voList = new ArrayList<>();
        for (TicketInfo ticketInfo : ticketInfos) {
            RailwayInfo railwayInfo = railwayInfoMapper.selectByPrimaryKey(ticketInfo.getRouteId());
            voList.add(new TicketInfoVo(ticketInfo, railwayInfo));
        }
        LOGGER.info(String.format("查询:%s", voList));
        return voList;


    }

    /**
     * @Description:
     * @Param: [ticketId]
     * @return: com.mikumifa.wms.entity.TicketInfoVo
     * @Date: 2023/4/8
     */
    @Override
    public TicketInfoVo getById(Integer ticketId) {
        TicketInfo ticketInfo = ticketInfoMapper.selectByPrimaryKey(ticketId);
        RailwayInfo railwayInfo = railwayInfoMapper.selectByPrimaryKey(ticketInfo.getRouteId());
        if (railwayInfo == null) {
            LOGGER.info(String.format("ticketId:%s查询失败", ticketId));
            return null;
        } else {
            TicketInfoVo ticketInfoVo = new TicketInfoVo(ticketInfo, railwayInfo);
            LOGGER.info(String.format("查询:%s", ticketInfoVo));
            return ticketInfoVo;
        }

    }

    /**
     * @Description:
     * @Param: [ticketId]
     * @return: com.mikumifa.wms.entity.OrderInfoVo  返回订单(未付款)
     * @Date: 2023/4/8
     */
    @Override
    public OrderInfoVo buyById(Integer ticketId, Integer customerId) {
        TicketInfoVo ticketInfoVo = getById(ticketId);
        if (ticketInfoVo.getSelled()==1) {
            LOGGER.info(String.format("该票已经被购买,ticketId:%d,customerId:%d", ticketId, customerId));
            return null;
        }


        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        CustomerVo customerVo = new CustomerVo(customer);
        OrderInfo orderInfo = new OrderInfo(null, customerVo.getCustomerId(), ticketInfoVo.getTicketId(), 0);
        int num = orderInfoMapper.insert(orderInfo);
        if (num == 0) {
            LOGGER.info(String.format("生成订单失败,ticketId:%d,customerId:%d", ticketId, customerId));
            return null;
        }
        TicketInfo ticketInfo = ticketInfoMapper.selectByPrimaryKey(ticketId);
        if (ticketInfo == null) {
            LOGGER.info(String.format("生成订单失败,ticketId:%d,customerId:%d", ticketId, customerId));
            return null;
        }
        ticketInfo.setSelled(1);
        int num2 = ticketInfoMapper.updateByPrimaryKey(ticketInfo);
        if (num2 == 0) {
            LOGGER.info(String.format("生成订单失败,ticketId:%d,customerId:%d", ticketId, customerId));
            return null;
        }

        LOGGER.info(String.format("生成订单成功,orderId:%d,customerId:%d,ticketId:%d", orderInfo.getOrderId(), customerVo.getCustomerId(), ticketInfoVo.getTicketId()));
        return new OrderInfoVo(orderInfo, customerVo, ticketInfoVo);
    }
}
