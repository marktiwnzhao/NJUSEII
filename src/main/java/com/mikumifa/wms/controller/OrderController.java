package com.mikumifa.wms.controller;

import com.mikumifa.wms.entity.OrderInfoVo;
import com.mikumifa.wms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:30
 **/
@Controller
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderInfoVo> getOrderById(@PathVariable Integer orderId) {
        OrderInfoVo orderInfo = orderService.getById(orderId);
        if (orderInfo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderInfo);
    }

    @PostMapping("/{orderId}/buy")
    public ResponseEntity<Boolean> buyOrderById(@PathVariable Integer orderId) {
        boolean success = orderService.buyById(orderId);
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(success);
    }

    @GetMapping
    public ResponseEntity<List<OrderInfoVo>> getOrdersByInfo(
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer ticketId,
            @RequestParam(required = false) Integer orderStatus) {
        List<OrderInfoVo> orderInfoList = orderService.getByInfo(customerId, ticketId, orderStatus);
        if (orderInfoList == null || orderInfoList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderInfoList);
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<Boolean> refundOrderById(@PathVariable Integer orderId) {
        boolean success = orderService.refundById(orderId);
        if (!success) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(success);
    }
}
