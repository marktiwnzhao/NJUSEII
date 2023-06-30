package org.fffd.l23o6.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.fffd.l23o6.pojo.enum_.OrderStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long trainId;

    @NotNull
    private Long departureStationId;

    @NotNull
    private Long arrivalStationId;

    @NotNull
    private OrderStatus status;
    @NotNull
    private String seat;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;
    @NotNull
    private Double money; // 支付费用
    @NotNull
    private Long point; //订单会获得的point
    @NotNull
    private Double rawMoney; //原来的价格
    @NotNull
    private Long rawPoint; //原来的积分
    @NotNull
    private Long usedPoint; //原来的积分
}
