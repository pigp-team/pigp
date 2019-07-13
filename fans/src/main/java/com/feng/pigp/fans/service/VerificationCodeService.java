package com.feng.pigp.fans.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.pigp.fans.model.Api;
import com.feng.pigp.fans.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 验证码服务、
 *
 * @author logic
 * @date 2019/7/13 11:27 AM
 * @since 1.0
 */
public class VerificationCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationCodeService.class);


    private static final String APP_ID = "313955";
    private static final String app_key = "uTCNzJQRw+ANuNxzuHlKz1JRPXlr4QzA";
    private static final String pd_id = "113955";
    private static final String pd_key = "FsdxShsiN40fMhnAyVwC4E6xnt7Z2UGk";
    private static final String PREDICT_TYPE = "30500";

    /**
     *
     * @param imageBytes 图片链接
     * @return 返回识别之后的Code
     */
    public static String distinguishCode(byte[] imageBytes) {
        long start = System.currentTimeMillis();
        String result= null;
        try {
            Api api = new Api();
            api.Init(APP_ID, app_key, pd_id, pd_key);
            Util.HttpResp resp = api.PredictFromFile(PREDICT_TYPE, imageBytes);

            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(resp.rsp_data, Map.class);

             result = (String)map.get("result");
             return result;
        } catch (Exception e) {
            LOGGER.error("识别图像验证码失败", e);
        } finally {
            LOGGER.info("识别验证码结果：result={}, consumeTime={}", result, System.currentTimeMillis() - start);
        }

        return null;
    }
}
