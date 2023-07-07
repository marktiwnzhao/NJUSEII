package org.fffd.l23o6.service.impl;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class OrderServiceImplTest {
    @Autowired
    OrderServiceImpl orderService;
    @Test
    public void testCalculatePaymentByPoints() {
        // Test case 1: Mileage points = 1000, Base price = 100.0
        double payment1 = orderService.calculatePaymentByPoints(1000L, 100.0);
        Assertions.assertEquals(99.9, payment1, 0.001);

        // Test case 2: Mileage points = 2000, Base price = 100.0
        double payment2 = orderService.calculatePaymentByPoints(2000L, 100.0);
        Assertions.assertEquals(99.9, payment2, 0.001);

        // Test case 3: Mileage points = 5000, Base price = 100.0
        double payment3 = orderService.calculatePaymentByPoints(5000L, 100.0);
        Assertions.assertEquals(99.75, payment3, 0.001);

        // Test case 4: Mileage points = 15000, Base price = 100.0
        double payment4 = orderService.calculatePaymentByPoints(15000L, 100.0);
        Assertions.assertEquals(99.55, payment4, 0.001);

        // Test case 5: Mileage points = 60000, Base price = 100.0
        double payment5 = orderService.calculatePaymentByPoints(60000L, 100.0);
        Assertions.assertEquals(99.3, payment5, 0.001);
    }
    @Test
    public void testCalculateUsedPoints() {
        // Test case 1: Mileage points = 1000, Base price = 100.0
        Long usedPoints1 = orderService.calculateUsedPoints(1000L);
        Assertions.assertEquals(1000L, usedPoints1);

        // Test case 2: Mileage points = 2000, Base price = 100.0
        Long usedPoints2 = orderService.calculateUsedPoints(2000L);
        Assertions.assertEquals(1000L, usedPoints2);

        // Test case 3: Mileage points = 5000, Base price = 100.0
        Long usedPoints3 = orderService.calculateUsedPoints(5000L);
        Assertions.assertEquals(3000L, usedPoints3);

        // Test case 4: Mileage points = 15000, Base price = 100.0
        Long usedPoints4 = orderService.calculateUsedPoints(15000L);
        Assertions.assertEquals(10000L, usedPoints4);

        // Test case 5: Mileage points = 60000, Base price = 100.0
        Long usedPoints5 = orderService.calculateUsedPoints(60000L);
        Assertions.assertEquals(50000L, usedPoints5);
    }
}