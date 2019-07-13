package com.feng.pigp.fans.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author feng
 * @date 2019/7/13 11:33
 * @since 1.0
 */
public class ToolUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ToolUtil.class);

    public static void  sleep(int mill){

        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            LOGGER.error("sleep error", e);
        }
    }
}