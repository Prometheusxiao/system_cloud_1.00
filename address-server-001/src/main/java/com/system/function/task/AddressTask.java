package com.system.function.task;

import com.system.basis.bean.AddressBasisInfo;
import com.system.basis.dao.AddressBasisInfoMapper;
import com.system.basis.tools.AddressRequest;
import com.system.basis.tools.AddressThreed;
import com.system.basis.tools.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @CreateTime: 2019-09-11 09:26
 * @Description:
 * @Author: WH
 */
@Component
@Configurable
@EnableScheduling
public class AddressTask {


    @Autowired
    private AddressBasisInfoMapper addressBasisInfoMapper;

    @Scheduled(cron = "0 24 15 * * * ")
    public void getAddress(){
        System.out.println("getAddress运行");
        ExecutorService es = Executors.newFixedThreadPool(50);
        String year = "2018";
        String basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year+"/";
        Map<String, String> firstPage = AddressRequest.getCookie(basicUrl+ "index.html");
        String cookie = firstPage.get("cookie");
        String html = firstPage.get("html");
        List<AddressBasisInfo> sheng = sheng(new ArrayList<>(),year, html);
        for (AddressBasisInfo addressBasisInfo : sheng) {
            System.out.println(addressBasisInfo.toString() +" 开始");
            es.submit(new AddressThreed(addressBasisInfoMapper,addressBasisInfo));
            System.out.println(addressBasisInfo.toString() +" 结束");
        }
        System.out.println("getAddress结束");
    }


    public List<AddressBasisInfo> sheng(List<AddressBasisInfo> data ,String year,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        Document parse = Jsoup.parse(html);
        Elements trs = parse.getElementsByClass("provincetr");
        for (Element tr : trs) {
            Elements as = tr.getElementsByTag("a");
            for (Element a : as) {
                String href = a.attr("href");
                if(href.contains(".html")){
                    AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                    addressBaseInfo.setAddressUrl(href);
                    String code = href.replace(".html", "");
                    String text = a.text();
                    addressBaseInfo.setSuperCode(year);
                    addressBaseInfo.setCode(code);
                    addressBaseInfo.setAddressName(text);
                    addressBaseInfo.setAddressVersion(year);
                    addressBaseInfo.setStatus("1");
                    addressBasisInfos.add(addressBaseInfo);
                    data.add(addressBaseInfo);
                }
            }
        }
        return addressBasisInfos;
    }
}
