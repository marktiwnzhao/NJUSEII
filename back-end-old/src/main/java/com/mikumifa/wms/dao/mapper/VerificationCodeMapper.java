package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.VerificationCode;
import org.springframework.stereotype.Repository;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:38
 **/
@Repository
public interface VerificationCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VerificationCode record);

    int insertSelective(VerificationCode record);

    VerificationCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VerificationCode record);

    int updateByPrimaryKey(VerificationCode record);
}
