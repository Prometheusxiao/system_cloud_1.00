package com.system.basis.dao;

import com.system.basis.bean.AddressBasisInfo;

import java.util.List;

/**
 * @CreateTime: 2019-09-11 09:15
 * @Description:
 * @Author: WH
 */
public interface AddressBasisInfoMapper {
    int deleteByPrimaryKey(String code);

    int insert(AddressBasisInfo record);

    int insertSelective(AddressBasisInfo record);

    AddressBasisInfo selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(AddressBasisInfo record);

    int updateByPrimaryKey(AddressBasisInfo record);

    List<AddressBasisInfo> selectXian();
}
