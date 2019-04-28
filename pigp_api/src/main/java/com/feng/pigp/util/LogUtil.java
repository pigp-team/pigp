package com.feng.pigp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author feng
 * @date 2019/4/28 19:37
 * @since 1.0
 */
public class LogUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

    public static Logger newInstanace(){
        return LOGGER;
    }
}