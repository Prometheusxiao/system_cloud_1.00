package com.system.basis.tools;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @CreateTime: 2019-09-10 23:42
 * @Description:
 * @Author: WH
 */

public class AddressRequest {


    /**
     * 获取首页最新一年数据
     */
    private static RequestConfig getRequestConfig(Boolean redirect) {
        RequestConfig requestConfig = null;
        if (redirect != null && redirect) {
            requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).build();
        } else {
            requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).setRedirectsEnabled(false).build();
        }
        return requestConfig;
    }

    public static String getHtml(String cookie, String url) {
        String html = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = getRequestConfig(false);
        HttpGet httpGet = new HttpGet(url.trim());
        try {
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.setHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Connection", "Keep-Alive");
            httpGet.setHeader("Cache-Control", "no-cache");
            httpGet.setHeader("x-forwarded-for",getRandomIp());
            if (StringUtils.isNotBlank(cookie)) {
                httpGet.setHeader("Cookie", cookie);
            }
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                html = EntityUtils.toString(entity, "gb2312");
            }
        } catch (Exception e) {
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                    httpGet.abort();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html;
    }

    public static Map<String,String> getCookie(String url) {
        Map<String,String> map = new HashMap<>();
        String cookie = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        RequestConfig requestConfig = getRequestConfig(false);
        HttpGet httpGet = new HttpGet(url.trim());
        try {
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            httpGet.setHeader("Accept-Language", "zh-Hans-CN,zh-Hans;q=0.5");
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Connection", "Keep-Alive");
            httpGet.setHeader("Cache-Control", "no-cache");
            response = httpClient.execute(httpGet);
            if (response != null) {
                Header[] allHeaders = response.getAllHeaders();
                for (Header header : allHeaders) {
                    String name = header.getName();
                    String value = header.getValue();
                    if (name.toLowerCase().contains("cookie")) {
                        cookie = value;
                    }
                }
            } else {
                cookie = null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String html = EntityUtils.toString(entity, "gb2312");
                map.put("html",html);
            }
            map.put("cookie",cookie);
        } catch (Exception e) {
        } finally {
            try {
                if (httpGet != null) {
                    httpGet.releaseConnection();
                    httpGet.abort();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
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
