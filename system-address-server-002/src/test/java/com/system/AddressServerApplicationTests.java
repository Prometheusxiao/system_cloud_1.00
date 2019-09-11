package com.system;

import com.system.bean.AddressBasisInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tools.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressServerApplicationTests {

    @Test
    public void contextLoads() {
        AddressBasisInfo addressBasisInfo = new AddressBasisInfo();
        System.out.println(JsonUtils.objectToJson(addressBasisInfo));
    }

}
