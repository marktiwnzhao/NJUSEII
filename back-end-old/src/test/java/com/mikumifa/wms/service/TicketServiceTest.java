package com.mikumifa.wms.service;

import com.mikumifa.wms.entity.TicketInfoVo;
import com.mikumifa.wms.entity.dto.TicketInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class TicketServiceTest {
    @Autowired
    TicketService ticketService;
    @Test
    void getByInfo() {
        List<TicketInfoVo> ticketInfoVos = ticketService.getByInfo("南京", "上海", null,0);
        ticketInfoVos.forEach(System.out::println);
    }

    @Test
    void getById() {
    }

    @Test
    void buyById() {
    }
}