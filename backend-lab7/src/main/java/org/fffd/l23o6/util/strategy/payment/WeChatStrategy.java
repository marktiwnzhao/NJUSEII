package org.fffd.l23o6.util.strategy.payment;

import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.fffd.l23o6.pojo.enum_.OrderStatus;

/**
 * @program: l23o6
 * @description:
 * @author: Xie
 * @create: 2023-06-28 16:35
 **/

public class WeChatStrategy extends PaymentStrategy {

    @Override
    public boolean pay(OrderEntity order) {

        return true;
    }

    @Override
    public boolean refund(OrderEntity order) {

        return true;
    }
}
