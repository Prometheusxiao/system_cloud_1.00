package com.system.function.service.impl;

import com.system.basis.dao.AddressBasisInfoMapper;
import com.system.bean.AddressBasisInfo;
import com.system.function.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @CreateTime: 2019-09-10 22:39
 * @Description:
 * @Author: WH
 */
@Transactional
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressBasisInfoMapper addressBasisInfoMapper;

    @Override
    public List<AddressBasisInfo> queryAddressBySuperCode(String superCode) {
        if(StringUtils.isBlank(superCode)){
            superCode = "2018";
        }
        return addressBasisInfoMapper.selectBySuperCode(superCode);
    }
}
