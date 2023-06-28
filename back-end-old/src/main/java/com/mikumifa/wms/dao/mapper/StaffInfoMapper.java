package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.StaffInfo;
import org.springframework.stereotype.Repository;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:39
 **/
@Repository
public interface StaffInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StaffInfo record);

    int insertSelective(StaffInfo record);

    StaffInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StaffInfo record);

    int updateByPrimaryKey(StaffInfo record);
}
