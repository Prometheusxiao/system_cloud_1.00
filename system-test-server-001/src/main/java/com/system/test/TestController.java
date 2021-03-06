package com.system.test;

import com.system.bean.AddressBasisInfo;
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
    private static final String REST_URL_PREFIX = "http://ADDRESS-SERVER";

    @GetMapping("test/{superCode}")
    public List<AddressBasisInfo> queryAddressBySuperCode(@PathVariable("superCode")String superCode){
        return restTemplate.getForObject(REST_URL_PREFIX+"/address/"+superCode,List.class);
    }

}
