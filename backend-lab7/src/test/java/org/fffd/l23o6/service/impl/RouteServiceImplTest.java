package org.fffd.l23o6.service.impl;

import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.RouteService;
import org.fffd.l23o6.service.StationService;
import org.fffd.l23o6.service.TrainService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@SpringBootTest
class RouteServiceImplTest {
    @Autowired
    TrainService trainService;
    @Autowired
    TrainDao trainDao;
    @Autowired
    StationService stationService;
    @Autowired
    RouteService routeService;

    @BeforeEach
    void initStation() {
        stationService.addStation("1");
        stationService.addStation("2");
        stationService.addStation("3");
        stationService.addStation("4");
        stationService.addStation("5");
        stationService.addStation("6");
        routeService.addRoute("r1", Arrays.asList(1L, 2L, 3L, 4L, 5L));
        routeService.addRoute("r2", Arrays.asList(1L, 2L, 3L, 4L, 5L));

    }

    @AfterEach
    void delAllStation() {
        trainDao.deleteAll();
    }

    @Test
    void test_1() {
        RouteVO route = routeService.getRoute(1L);
        assertEquals(route.getStationIds(), Arrays.asList(1, 2, 3, 4, 5));
        routeService.delRoute(1L);
        routeService.editRoute(2L,"r3",Arrays.asList(6L, 2L, 3L, 4L, 5L));
        assertEquals(routeService.getRoute(2L).getStationIds(), Arrays.asList(6, 2, 3, 4, 5));


    }


}