package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.TicketInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:38
 **/
@Repository
public interface TicketInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TicketInfo record);

    int insertSelective(TicketInfo record);

    TicketInfo selectByPrimaryKey(Integer id);

    List<TicketInfo> selectByInfo(String origin, String destination, Date departureTime,Integer selled);

    int updateByPrimaryKey(TicketInfo record);
}
