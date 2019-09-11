package com.system.basis.tools;

import com.system.basis.bean.AddressBasisInfo;
import com.system.basis.dao.AddressBasisInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @CreateTime: 2019-09-11 15:06
 * @Description:
 * @Author: WH
 */
public class AddressThreed implements Callable<Boolean> {

    private AddressBasisInfoMapper addressBasisInfoMapper;

    private AddressBasisInfo addressBasisInfo;

    public AddressThreed(AddressBasisInfoMapper addressBasisInfoMapper, AddressBasisInfo addressBasisInfo) {
        this.addressBasisInfoMapper = addressBasisInfoMapper;
        this.addressBasisInfo = addressBasisInfo;
    }

    @Override
    public Boolean call() throws Exception {
        List<AddressBasisInfo> data = new ArrayList<>();
        String year = "2018";

        String basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year+"/";
        Map<String, String> firstPage = AddressRequest.getCookie(basicUrl+ "index.html");
        String cookie = firstPage.get("cookie");

        String code = addressBasisInfo.getCode();
        String url = basicUrl + addressBasisInfo.getAddressUrl();
        String html = AddressRequest.getHtml(cookie, url);

        List<AddressBasisInfo> shi = shi(data,year, code, html);
        getZt();
        for (AddressBasisInfo addressBasisInfoShengShi : shi) {
            url = basicUrl + addressBasisInfoShengShi.getAddressUrl();
            System.out.println("xian = " + url);
            html = AddressRequest.getHtml(cookie, url);
            List<AddressBasisInfo> xian = xian(data, year, addressBasisInfoShengShi.getCode(), html);
            getZt();
            for (AddressBasisInfo addressBasisInfo : xian) {
                String address = addressBasisInfo.getAddressUrl();
                if(StringUtils.isNotBlank(address)){
                    url = basicUrl+code+"/"+address;
                    System.out.println("zhenjie = " + url);
                    html = AddressRequest.getHtml(cookie, url);
                    List<AddressBasisInfo> zhenjie = jieZhen(data, year, addressBasisInfo.getCode(), html);
                    System.out.println(JsonUtils.objectToJson(zhenjie));
                    getZt();
                } else {
                    System.err.println("地址空 ： "+addressBasisInfo.getAddressName());
                }
            }
        }
        for (AddressBasisInfo datum : data) {
            AddressBasisInfo tag = addressBasisInfoMapper.selectByPrimaryKey(datum.getCode());
            if(tag == null){
                addressBasisInfoMapper.insert(datum);
            } else {
                addressBasisInfoMapper.updateByPrimaryKey(datum);
            }
        }
        return null;
    }

    public List<AddressBasisInfo> shi(List<AddressBasisInfo> data, String year, String superCode, String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
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
        return addressBasisInfos;
    }

    public List<AddressBasisInfo> xian(List<AddressBasisInfo> data,String year,String superCode,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
        Document parse = Jsoup.parse(html);
        Elements countytrs = parse.getElementsByClass("countytr");
        for (Element countytr : countytrs) {
            Elements as = countytr.getElementsByTag("a");
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
        return addressBasisInfos;
    }

    public List<AddressBasisInfo> jieZhen(List<AddressBasisInfo> data,String year,String superCode,String html){
        List<AddressBasisInfo> addressBasisInfos = new ArrayList<>();
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
