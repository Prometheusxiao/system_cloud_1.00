package com.system.function.controller;

import com.system.bean.AddressBasisInfo;
import com.system.function.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @CreateTime: 2019-09-10 22:38
 * @Description:
 * @Author: WH
 */
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/address/{superCode}")
    public List<AddressBasisInfo> queryAddressBySuperCode(@PathVariable("superCode")String superCode){
        return addressService.queryAddressBySuperCode(superCode);
    }

}
