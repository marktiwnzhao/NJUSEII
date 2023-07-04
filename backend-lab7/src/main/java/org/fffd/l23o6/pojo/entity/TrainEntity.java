package org.fffd.l23o6.pojo.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.fffd.l23o6.pojo.enum_.TrainType;
import org.hibernate.annotations.*;

import io.hypersistence.utils.hibernate.type.array.BooleanArrayType;

import java.util.Date;
import java.util.List;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TrainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long routeId;

    @NotNull
    @Type(BooleanArrayType.class)
    @Column(name = "seats", columnDefinition = "boolean[][]" )
    //出发点到结束点的座位的使用情况
    private boolean[][] seats;

    @NotNull
    private TrainType trainType;

    @NotNull
    private String date;

    @NotNull
    private List<Date> arrivalTimes;

    @NotNull
    private List<Date> departureTimes;

    @NotNull
    private List<String> extraInfos;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
