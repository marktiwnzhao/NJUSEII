package org.fffd.l23o6.util.strategy.payment;

import org.fffd.l23o6.pojo.entity.OrderEntity;

public abstract class PaymentStrategy {

    // TODO: implement this by adding necessary methods and implement specified strategy
    public  abstract double  pay(OrderEntity order,double v);
    public  abstract double  refund(OrderEntity order);
}
