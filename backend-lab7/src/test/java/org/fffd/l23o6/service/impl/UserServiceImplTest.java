package org.fffd.l23o6.service.impl;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {
    @Autowired
    UserServiceImpl service;
    @Autowired
    UserDao userDao;

    @BeforeEach
    void initStation() {
        service.register("user1", "123456", "user1", "323322200410155010", "15851181899", "身份证");
        service.register("user2", "123456", "user2", "323322200410155010", "15851181899", "身份证");
        service.register("user3", "123456", "user3", "323322200410155010", "15851181899", "身份证");
        service.register("user4", "123456", "user4", "323322200410155010", "15851181899", "身份证");
    }

    @AfterEach
    void delAllStation() {
        userDao.deleteAll();
    }

    @Test
        //登录测试
    void test_1() {

        assertNotNull(service.findByUserName("user1"));
        assertNull(service.findByUserName("user5"));
        assertThrows(BizException.class, () -> {
            service.login("user1", "123457");
        });
        assertDoesNotThrow(() -> {
            service.login("user1", "123456");
        });


    }

    @Test
        //修改用户info的测试
    void test_2() {
        service.editInfo("user1", "123", "123", "123", "123");
        UserEntity user1 = service.findByUserName("user1");
        assertEquals("123", user1.getName());
        assertEquals("123", user1.getPhone());
        assertEquals("123", user1.getIdn());
        assertEquals("123", user1.getType());

    }

}