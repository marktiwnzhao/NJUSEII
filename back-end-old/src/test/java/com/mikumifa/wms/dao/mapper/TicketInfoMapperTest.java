package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.TicketInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class TicketInfoMapperTest {
    @Autowired
    TicketInfoMapper ticketInfoMapper;
    @Test
    void deleteByPrimaryKey() {
    }

    @Test
    void insert() {
    }

    @Test
    void insertSelective() {
    }

    @Test
    void selectByPrimaryKey() {
    }

    @Test
    void selectByInfo() {
        List<TicketInfo> ticketInfos = ticketInfoMapper.selectByInfo("南京", "上海", null,0 );
        ticketInfos.forEach(System.out::println);

    }

    @Test
    void updateByPrimaryKey() {
    }
}