package org.fffd.l23o6.util.strategy.payment;

import lombok.RequiredArgsConstructor;
import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.dao.UserDao;
import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.springframework.stereotype.Service;

/**
 * @program: l23o6
 * @description:
 * @author: Xie
 * @create: 2023-06-28 16:35
 **/

public class WeChatStrategy extends  PaymentStrategy{

    @Override
    double pay(OrderEntity order) {
        return 0;
    }
}
