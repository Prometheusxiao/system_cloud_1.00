package com.system.basis.main.controller;

import com.system.basis.main.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @CreateTime: 2019-09-10 22:38
 * @Description:
 * @Author: WH
 */
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("getTest")
    public String getTest(){
        return "HAHAhaha哈哈";
    }
}
