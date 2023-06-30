package org.fffd.l23o6.pojo.vo.order;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderVO {
    private Long id;
    private Long trainId;
    private Long startStationId;
    private Long endStationId;
    private Date departureTime;
    private Date arrivalTime;
    private String status;
    private Date createdAt;
    private String seat;
    private Double money; // 支付费用
    private Long point; //订单会获得的point
    private Double rawMoney; //原来的价格
    private Long rawPoint; //原来的积分
    private Long usedPoint; //原来的积分

}
