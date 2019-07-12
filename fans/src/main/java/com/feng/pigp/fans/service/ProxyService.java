package com.feng.pigp.fans.service;

import com.feng.pigp.fans.model.ProxyIp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;


/**
 * @author feng
 * @date 2019/7/9 10:00
 * @since 1.0
 */
public class ProxyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class);

    /**
     * 查询代理IP
     *
     * @return 返回代理IP信息
     */
    public static ProxyIp queryIp () {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet("http://www.logicluo.top:5010/get");
        CloseableHttpResponse response = null;
        ProxyIp proxyIp = new ProxyIp();
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String string = EntityUtils.toString(response.getEntity(), "UTF-8");
                if (StringUtils.isEmpty(string)) {
                    return null;
                }


                String[] split = string.split(":");
                if (split == null || split.length < 2) {
                    return null;
                }
                proxyIp.setIp(split[0]);
                proxyIp.setPort(Integer.valueOf(split[1]));
                return proxyIp;
            }
        } catch (Exception e) {
            LOGGER.error("获取代理IP异常", e);
        } finally {
            try {
                httpclient.close();
            } catch (Exception e) {
                LOGGER.error("关闭HttpClient异常", e);
            }
        }
        return null;
    }
}