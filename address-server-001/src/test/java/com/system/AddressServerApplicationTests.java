package com.system;

import com.google.gson.JsonArray;
import com.system.basis.bean.AddressBasisInfo;
import com.system.basis.dao.AddressBasisInfoMapper;
import com.system.basis.tools.AddressRequest;
import com.system.basis.tools.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressServerApplicationTests {

    @Autowired
    private AddressBasisInfoMapper addressBasisInfoMapper;

    @Test
    public void contextLoads() {
        AddressBasisInfo addressBasisInfo = new AddressBasisInfo();
        addressBasisInfo.setCode("1111");
        addressBasisInfoMapper.insert(addressBasisInfo);
    }


    @Test
    public void dasjidasjdsaio(){
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
        }
        for (AddressBasisInfo datum : data) {
            AddressBasisInfo addressBasisInfo = addressBasisInfoMapper.selectByPrimaryKey(datum.getCode());
            if(addressBasisInfo == null){
                addressBasisInfoMapper.insert(datum);
            } else {
                addressBasisInfoMapper.updateByPrimaryKey(datum);
            }
        }
    }

    @Test
    public void sjdiasdjasoi(){
        String year = "2018";
        String basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year+"/";
        Map<String, String> firstPage = AddressRequest.getCookie(basicUrl+ "index.html");
        String cookie = firstPage.get("cookie");
        String html = firstPage.get("html");
        System.out.println("cookie = "+ cookie);
        System.out.println("html = "+ html);
        List<AddressBasisInfo> data = new ArrayList<>();
        List<AddressBasisInfo> addressBasisInfos = addressBasisInfoMapper.selectXian();
        for (AddressBasisInfo addressBasisInfo : addressBasisInfos) {
            String addressUrl = addressBasisInfo.getAddressUrl();
            String code = addressBasisInfo.getCode().substring(0,2);
            String url = basicUrl+code+"/"+addressUrl;
            html = AddressRequest.getHtml(cookie, url );
            System.out.println(url);
            System.out.println(html);
            List<AddressBasisInfo> zhenjie = jieZhen(data, year, addressBasisInfo.getCode(), html);
            for (AddressBasisInfo basisInfo : zhenjie) {
                AddressBasisInfo tag = addressBasisInfoMapper.selectByPrimaryKey(basisInfo.getCode());
                if(tag == null){
                    addressBasisInfoMapper.insert(basisInfo);
                } else {
                    addressBasisInfoMapper.updateByPrimaryKey(basisInfo);
                }
            }
            getZt();
        }
    }

    @Test
    public void djsaidsajoas(){
        List<AddressBasisInfo> addressBasisInfos = addressBasisInfoMapper.selectXian();
        String year = "2018";
        String basicUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/" + year+"/";
        Map<String, String> firstPage = AddressRequest.getCookie(basicUrl+ "index.html");
        String cookie = firstPage.get("cookie");
        List<AddressBasisInfo> data = new ArrayList<>();
        for (AddressBasisInfo addressBasisInfo : addressBasisInfos) {
            String addressUrl = addressBasisInfo.getAddressUrl();
            String code = addressBasisInfo.getCode().substring(0,2);
            String url = basicUrl+code+"/"+addressUrl;
            String html = AddressRequest.getHtml(cookie, url );
            List<AddressBasisInfo> zhenjie = jieZhen(data, year, addressBasisInfo.getCode(), html);
            System.out.println(url);
            System.out.println(html);
            System.out.println("-----------------------------------------");
        }
        for (AddressBasisInfo datum : data) {
            AddressBasisInfo tag = addressBasisInfoMapper.selectByPrimaryKey(datum.getCode());
            if(tag == null){
                addressBasisInfoMapper.insert(datum);
            } else {
                addressBasisInfoMapper.updateByPrimaryKey(datum);
            }
        }
        System.out.println(JsonUtils.objectToJson(data));
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

    public List<AddressBasisInfo> shi(List<AddressBasisInfo> data,String year,String superCode,String html){
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

    @Test
    public void jdasidjasio(){
        String randomIp = getRandomIp();
        System.out.println(randomIp);
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

    public static String getRandomIp(){
        int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
            { 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
            { 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
            { 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
            { 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
            { -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
            { -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
            { -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
            { -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
            { -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
        };

        Random rdint = new Random();
        int index = rdint.nextInt(10);
        String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
        return ip;
    }

    public static String num2ip(int ip) {
        int[] b = new int[4];
        String x = "";
        b[0] = (int) ((ip >> 24) & 0xff);
        b[1] = (int) ((ip >> 16) & 0xff);
        b[2] = (int) ((ip >> 8) & 0xff);
        b[3] = (int) (ip & 0xff);
        x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "." + Integer.toString(b[3]);

        return x;
    }
}
