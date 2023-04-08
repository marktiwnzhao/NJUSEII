package com.mikumifa.wms.controller;

import com.mikumifa.wms.entity.OrderInfoVo;
import com.mikumifa.wms.entity.TicketInfoVo;
import com.mikumifa.wms.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:30
 **/
@Controller
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketInfoVo>> getTicketsByInfo(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date departureTime,
            @RequestParam(required = false) Integer selled) {
        List<TicketInfoVo> ticketInfoList = ticketService.getByInfo(from, to, departureTime, selled);
        if (ticketInfoList == null || ticketInfoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketInfoList);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketInfoVo> getTicketById(@PathVariable Integer ticketId) {
        TicketInfoVo ticketInfo = ticketService.getById(ticketId);
        if (ticketInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ticketInfo);
    }

    @PostMapping("/{ticketId}/buy")
    public ResponseEntity<OrderInfoVo> buyTicketById(@PathVariable Integer ticketId, @RequestParam Integer customerId) {
        OrderInfoVo orderInfo = ticketService.buyById(ticketId, customerId);
        if (orderInfo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(orderInfo);
    }
}
