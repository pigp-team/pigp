package com.feng.spider.utils;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author feng
 * @date 2019/4/3 12:51
 * @since 1.0
 */
public class Conf {

    private static final Logger LOGGER = LoggerFactory.getLogger(Conf.class);
    private static final Splitter SPLITTER = Splitter.on("=").omitEmptyStrings().trimResults();
    private static Map<String, String> confMap = Maps.newHashMap();

    static{
        String path = Conf.class.getClassLoader().getResource("account.pwd").getPath();
        try {
            List<String> list = Files.readLines(new File(path), Charsets.UTF_8);
            if(list==null){
                LOGGER.info("conf is emypty ：{}", path);
                System.exit(-1);
            }

            for(String str : list){
                List<String> temp = SPLITTER.splitToList(str);
                if(temp.size()!=2){
                    LOGGER.info("conf format is error：{}", str);
                    continue;
                }
                confMap.put(temp.get(0), temp.get(1));
            }
        } catch (IOException e) {
            LOGGER.error("io exception", e);
        }
    }

    public static String getConf(String key){
        return confMap.get(key);
    }
}