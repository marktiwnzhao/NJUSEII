package com.mikumifa.wms.service.impl;

import com.mikumifa.wms.dao.mapper.CustomerMapper;
import com.mikumifa.wms.entity.CustomerVo;
import com.mikumifa.wms.entity.dto.Customer;
import com.mikumifa.wms.service.CodeService;
import com.mikumifa.wms.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 22:40
 **/
@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CodeService codeService;

    @Override
    public boolean login(String email, String password) {
        int result = customerMapper.verifyPassword(email, password);
        if (result == 1) {
            LOGGER.info(String.format("用户%s登录成功", email));
            return true;
        } else {
            LOGGER.info(String.format("用户%s登录失败", email));
            return false;
        }
    }

    @Override
    public boolean signIn(Customer customer, String code) {
        boolean verifyCode = codeService.verifyCode(customer.getEmail(), code);
        if (verifyCode) {
            //TODO
            try{
                int insert = customerMapper.insert(customer);
                if (insert == 0) {
                    throw new Exception();
                }
            }

            catch (Exception e){
                LOGGER.info(String.format("创建失败:%s重复", customer.getEmail()));
                return false;
            }
            LOGGER.info(String.format("创建成功:%s", customer));
            return true;

        }
        LOGGER.info(String.format("code错误:%s", code));
        return false;
    }

    @Override
    public CustomerVo getInfoById(Integer customerId) {

        Customer customer = customerMapper.selectByPrimaryKey(customerId);
        if (customer == null) {
            LOGGER.info(String.format("没找到customerId:%s", customerId));
            return null;

        } else {
            LOGGER.info(String.format("找到customer:%s", customer));
            return new CustomerVo(customer);
        }

    }
}
