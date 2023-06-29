package org.fffd.l23o6.util.strategy.train;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KSeriesSeatStrategyTest {
    @Test
    void test1(){
        KSeriesSeatStrategy.INSTANCE.allocSeat(2,3, KSeriesSeatStrategy.KSeriesSeatType.HARD_SEAT,new boolean[4][60]);
    }



}