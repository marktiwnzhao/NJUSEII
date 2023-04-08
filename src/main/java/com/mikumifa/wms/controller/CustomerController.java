package com.mikumifa.wms.controller;

import com.mikumifa.wms.entity.CustomerVo;
import com.mikumifa.wms.entity.dto.Customer;
import com.mikumifa.wms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:30
 **/

@Controller
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestParam String email, @RequestParam String password) {
        boolean success = customerService.login(email, password);
        return ResponseEntity.ok(success);
    }

    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody Customer customer, @RequestParam String code) {
        boolean success = customerService.signIn(customer, code);
        return ResponseEntity.ok(success);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerVo> getCustomerInfo(@PathVariable Integer customerId) {
        CustomerVo customerVo = customerService.getInfoById(customerId);
        if (customerVo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerVo);
    }
}
