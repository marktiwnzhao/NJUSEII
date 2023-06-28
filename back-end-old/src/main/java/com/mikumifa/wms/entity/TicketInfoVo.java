package com.mikumifa.wms.entity;

import com.mikumifa.wms.dao.mapper.RailwayInfoMapper;
import com.mikumifa.wms.entity.dto.RailwayInfo;
import com.mikumifa.wms.entity.dto.TicketInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 23:01
 **/
@Data
public class TicketInfoVo {

    private Integer ticketId;
    private RailwayInfo railwayInfo;
    private BigDecimal price;
    private Integer seatClass;
    private Integer seatNo;
    private Integer selled;
    public TicketInfoVo(TicketInfo po,RailwayInfo railwayInfo) {
        BeanUtils.copyProperties(po,this);
        this.railwayInfo=railwayInfo;
    }
}
