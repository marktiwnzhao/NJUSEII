package com.mikumifa.wms.dao.mapper;

import com.mikumifa.wms.entity.dto.Customer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: se2-work
 * @description:
 * @author: Xie
 * @create: 2023-04-07 16:37
 **/
@Repository
public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Customer record);

    int insertSelective(Customer record);

    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Customer record);

    int updateByPrimaryKey(Customer record);

    int verifyPassword(String email, String password);
}
