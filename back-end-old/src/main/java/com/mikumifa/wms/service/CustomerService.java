package com.mikumifa.wms.service;

import com.mikumifa.wms.entity.CustomerVo;
import com.mikumifa.wms.entity.dto.Customer;

public interface CustomerService {
    public boolean login(String email, String password);
    public boolean signIn(Customer customer,String code);
    public CustomerVo getInfoById(Integer customerId);
}
