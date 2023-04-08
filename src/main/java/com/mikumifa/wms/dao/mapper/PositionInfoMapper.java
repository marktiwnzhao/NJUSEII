package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.PositionInfo;
import org.springframework.stereotype.Repository;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:38
 **/
@Repository
public interface PositionInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(PositionInfo record);

    int insertSelective(PositionInfo record);

    PositionInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PositionInfo record);

    int updateByPrimaryKey(PositionInfo record);
}
