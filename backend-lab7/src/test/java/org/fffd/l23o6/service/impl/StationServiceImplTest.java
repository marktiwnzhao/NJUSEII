package org.fffd.l23o6.service.impl;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.fffd.l23o6.service.StationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StationServiceImplTest {
    @Autowired
    StationService service;
    @Autowired
    StationDao stationDao;
    @BeforeEach
    void initStation() {
        service.addStation("1");
        service.addStation("2");
        service.addStation("3");
        service.addStation("4");
        service.addStation("5");
        service.addStation("6");
    }
    @AfterEach
    void delAllStation() {
        stationDao.deleteAll();
    }
    @Test
        //没有重名, 添加并查找, 查找和删除
    void stationTest_1() {

        List<StationVO> stationVOS = service.listStations();
        List<String> stationNames = new ArrayList<String>();
        for (StationVO station : stationVOS) {
            stationNames.add(station.getName());
        }
        Collections.sort(stationNames);
        assertEquals(stationNames, Arrays.asList("1", "2", "3", "4", "5", "6"));
        StationVO stationVO = stationVOS.get(0);
        Long id = stationVO.getId();
        String name = stationVO.getName();
        service.delStation(id);
        List<String> names = service.listStations().stream().map(StationVO::getName).toList();
        assertFalse(names.contains(name));

    }

    @Test
        //有重名,添加并查找,删除不存在的
    void stationTest_2() {
        try {
            service.addStation("6");
        } catch (BizException e) {
            assertEquals(e.getMessage(), "同名站点已存在");
        }
        List<StationVO> stationVOS = service.listStations();
        List<String> stationNames = new ArrayList<String>();
        for (StationVO station : stationVOS) {
            stationNames.add(station.getName());
        }
        Collections.sort(stationNames);
        assertEquals(stationNames, Arrays.asList("1", "2", "3", "4", "5", "6"));
        StationVO stationVO = stationVOS.get(0);
        Long id = stationVO.getId();
        String name = stationVO.getName();
        try {
            service.delStation(id+10);

        } catch (Exception e) {
           e.getMessage();
        }
        assertEquals(stationNames, Arrays.asList("1", "2", "3", "4", "5", "6"));
    }
}