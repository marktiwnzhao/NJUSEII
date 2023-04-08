package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.RailwayInfo;
import org.springframework.stereotype.Repository;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:38
 **/
@Repository
public interface RailwayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RailwayInfo record);

    int insertSelective(RailwayInfo record);

    RailwayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RailwayInfo record);

    int updateByPrimaryKey(RailwayInfo record);
}
