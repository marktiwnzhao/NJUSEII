package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.OrderInfo;
import com.mikumifa.wms.entity.dto.TicketInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:37
 **/
@Repository
public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(OrderInfo record);
    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer id);
    /**
    * @Description: 参数可选,不填就是搜索全部
    * @Param: [orderStatus]
    * @return: java.util.List<com.mikumifa.wms.entity.dto.TicketInfo>
    * @Date: 2023/4/8
    */
    List<OrderInfo> selectBySelective(Integer customerId, Integer ticketId,Integer orderStatus);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

}
