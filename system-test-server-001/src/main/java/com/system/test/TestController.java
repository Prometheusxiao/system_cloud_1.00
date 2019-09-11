package com.system.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @CreateTime: 2019-09-11 22:40
 * @Description:
 * @Author: WH
 */
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("test/{superCode}")
    public String queryAddressBySuperCode(@PathVariable("superCode")String superCode){
        return "haha哈哈哈="+superCode;
    }

}
