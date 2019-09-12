package com.system.function.task;

import com.system.basis.dao.AddressBasisInfoMapper;
import com.system.basis.tools.AddressRequest;
import com.system.bean.AddressBasisInfo;
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
import java.util.Random;
import java.util.logging.Handler;

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

    @Scheduled(cron = "0 58 9 * * * ")
    public void getAddress() throws InterruptedException {
        System.out.println("获取国家统计局地址编码 - 开始");
        String year = "2018";
        String basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year+"/";
        Map<String, String> firstPage = AddressRequest.getCookie(basicUrl+ "index.html");
        String cookie = firstPage.get("cookie");
        String html = firstPage.get("html");
        List<AddressBasisInfo> data = new ArrayList<>();
        //获取省级数据
        List<AddressBasisInfo> sheng = sheng(data,year, html);
        //获取市级数据
        for (AddressBasisInfo addressBasisInfoSheng : sheng) {
            String code = addressBasisInfoSheng.getCode();
            String url = basicUrl + addressBasisInfoSheng.getAddressUrl();
            System.out.println("shi = " + url);
            html = AddressRequest.getHtml(cookie, url);
            List<AddressBasisInfo> shi = shi(data,year, addressBasisInfoSheng.getCode(), html);
            getZt();
            for (AddressBasisInfo addressBasisInfoShengShi : shi) {
                url = basicUrl + addressBasisInfoShengShi.getAddressUrl();
                System.out.println("xian = " + url);
                html = AddressRequest.getHtml(cookie, url);
                List<AddressBasisInfo> xian = xian(data, year, addressBasisInfoShengShi.getCode(), html);
                getZt();
                for (AddressBasisInfo addressBasisInfoXian : xian) {
                    String address = addressBasisInfoXian.getAddressUrl();
                    if(StringUtils.isNotBlank(address)){
                        url = basicUrl+code+"/"+address;
                        System.out.println("zhenjie = " + url);
                        html = AddressRequest.getHtml(cookie, url);
                        List<AddressBasisInfo> zhenjie = jieZhen(data, year, addressBasisInfoXian.getCode(), html);
                        if(zhenjie!=null&&zhenjie.size()>0){
                            for (AddressBasisInfo addressBasisInfoZhenjie : zhenjie) {
                                saveAddressBasisInfo(addressBasisInfoZhenjie);
                            }
                        }
                        getZt();
                    } else {
                        System.err.println("地址空 ： "+addressBasisInfoXian.getAddressName());
                    }
                    saveAddressBasisInfo(addressBasisInfoXian);
                }
                saveAddressBasisInfo(addressBasisInfoShengShi);
            }
            saveAddressBasisInfo(addressBasisInfoSheng);
        }
        System.out.println("获取国家统计局地址编码 - 结束");
    }

    public void saveAddressBasisInfo(AddressBasisInfo addressBasisInfo){
        AddressBasisInfo old = addressBasisInfoMapper.selectByPrimaryKey(addressBasisInfo.getCode());
        if(old == null){
            addressBasisInfoMapper.insert(addressBasisInfo);
        } else {
            addressBasisInfoMapper.updateByPrimaryKey(addressBasisInfo);
        }
    }

    public List<AddressBasisInfo> sheng(List<AddressBasisInfo> data ,String year,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(html)) {
            Document parse = Jsoup.parse(html);
            Elements trs = parse.getElementsByClass("provincetr");
            for (Element tr : trs) {
                Elements as = tr.getElementsByTag("a");
                for (Element a : as) {
                    String href = a.attr("href");
                    if (href.contains(".html")) {
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
        }
        return addressBasisInfos;
    }

    public List<AddressBasisInfo> shi(List<AddressBasisInfo> data,String year,String superCode,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(html)) {
            Document parse = Jsoup.parse(html);
            Elements citytrs = parse.getElementsByClass("citytr");
            for (Element citytr : citytrs) {
                Elements as = citytr.getElementsByTag("a");
                Element a0 = as.get(0);
                String text = a0.text();
                String href = a0.attr("href");
                Element a1 = as.get(1);
                String name = a1.text();
                AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                addressBaseInfo.setAddressUrl(href);
                addressBaseInfo.setCode(text);
                addressBaseInfo.setSuperCode(superCode);
                addressBaseInfo.setAddressName(name);
                addressBaseInfo.setAddressVersion(year);
                addressBaseInfo.setStatus("1");
                addressBasisInfos.add(addressBaseInfo);
                data.add(addressBaseInfo);
            }
        }
        return addressBasisInfos;
    }

    public List<AddressBasisInfo> xian(List<AddressBasisInfo> data,String year,String superCode,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(html)) {
            Document parse = Jsoup.parse(html);
            Elements countytrs = parse.getElementsByClass("countytr");
            for (Element countytr : countytrs) {
                Elements as = countytr.getElementsByTag("a");
                if (as != null && as.size() > 0) {
                    Element a0 = as.get(0);
                    String text = a0.text();
                    String href = a0.attr("href");
                    Element a1 = as.get(1);
                    String name = a1.text();
                    AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                    addressBaseInfo.setAddressUrl(href);
                    addressBaseInfo.setCode(text);
                    addressBaseInfo.setSuperCode(superCode);
                    addressBaseInfo.setAddressName(name);
                    addressBaseInfo.setAddressVersion(year);
                    addressBaseInfo.setStatus("1");
                    addressBasisInfos.add(addressBaseInfo);
                    data.add(addressBaseInfo);
                } else {
                    Elements tds = countytr.getElementsByTag("td");
                    String code = tds.get(0).text();
                    String name = tds.get(1).text();
                    AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                    addressBaseInfo.setAddressUrl(null);
                    addressBaseInfo.setCode(code);
                    addressBaseInfo.setSuperCode(superCode);
                    addressBaseInfo.setAddressName(name);
                    addressBaseInfo.setAddressVersion(year);
                    addressBaseInfo.setStatus("1");
                    addressBasisInfos.add(addressBaseInfo);
                    data.add(addressBaseInfo);
                }

            }
        }
        return addressBasisInfos;
    }

    public List<AddressBasisInfo> jieZhen(List<AddressBasisInfo> data,String year,String superCode,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(html)){
            Document parse = Jsoup.parse(html);
            Elements towntrs = parse.getElementsByClass("towntr");
            for (Element towntr : towntrs) {
                Elements as = towntr.getElementsByTag("a");
                if(as!=null&&as.size()>0){
                    Element a0 = as.get(0);
                    String text = a0.text();
                    String href = a0.attr("href");
                    Element a1 = as.get(1);
                    String name = a1.text();
                    AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                    addressBaseInfo.setAddressUrl(href);
                    addressBaseInfo.setCode(text);
                    addressBaseInfo.setSuperCode(superCode);
                    addressBaseInfo.setAddressName(name);
                    addressBaseInfo.setAddressVersion(year);
                    addressBaseInfo.setStatus("1");
                    addressBasisInfos.add(addressBaseInfo);
                    data.add(addressBaseInfo);
                } else {
                    Elements tds = towntr.getElementsByTag("td");
                    String code = tds.get(0).text();
                    String name = tds.get(1).text();
                    AddressBasisInfo addressBaseInfo = new AddressBasisInfo();
                    addressBaseInfo.setAddressUrl(null);
                    addressBaseInfo.setCode(code);
                    addressBaseInfo.setSuperCode(superCode);
                    addressBaseInfo.setAddressName(name);
                    addressBaseInfo.setAddressVersion(year);
                    addressBaseInfo.setStatus("1");
                    addressBasisInfos.add(addressBaseInfo);
                    data.add(addressBaseInfo);
                }

            }
        }
        return addressBasisInfos;
    }

    public void getZt(){
        try {
            Random r = new Random();
            long val = r.nextInt(1500)*1l;
            Thread.sleep(val);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
