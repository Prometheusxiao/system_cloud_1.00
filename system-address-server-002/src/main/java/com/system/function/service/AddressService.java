package com.system.function.service;



import com.system.bean.AddressBasisInfo;

import java.util.List;

public interface AddressService {
    List<AddressBasisInfo> queryAddressBySuperCode(String superCode);
}
