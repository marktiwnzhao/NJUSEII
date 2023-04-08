package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.OrderInfo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class OrderInfoMapperTest {
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Test
    void deleteByPrimaryKey() {
        int v = orderInfoMapper.deleteByPrimaryKey(1);
        System.out.println(v);
    }

    @Test
    void insert() {
    }

    @Test
    void insertSelective() {
    }

    @Test
    void selectByPrimaryKey() {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(11);
        System.out.println(orderInfo);

    }

    @Test
    void updateByPrimaryKeySelective() {
    }

    @Test
    void updateByPrimaryKey() {
    }
}