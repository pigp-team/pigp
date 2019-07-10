package com.feng.pigp.fans.common;

/**
 * @author feng
 * @date 2019/7/10 11:32
 * @since 1.0
 */
public class Common {

    public static String SINA_URL = "https://weibo.com/";
    public static String SINA_LOGIN_USERNAME = "//*[@id=\"loginname\"]";
    public static String SINA_LOGIN_PWD = "//*[@id=\"pl_login_form\"]/div/div[3]/div[2]/div/input";
    public static String SINA_LOGIN_BUTTON = "//*[@id=\"pl_login_form\"]/div/div[3]/div[6]/a";

    public static String SINA_SEARCH_INPUT = "//*[@id=\"plc_top\"]/div/div/div[2]/input";
    public static String SINA_SEARCH_BUTTON = "//*[@id=\"plc_top\"]/div/div/div[2]/a";

    public static String SINA_RELATIVE_USER = "//*[@id=\"pl_feed_main\"]/div[2]/div[2]/div[1]/div/div[2]/div[%d]/div[2]/div/a";
}