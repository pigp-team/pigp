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

    public static String SINA_ATTENTION = "//*[@id=\"Pl_Official_Headerv6__1\"]/div[1]/div/div[2]/div[4]/div/div[1]/a[1]";
    public static String SINA_TOPIC = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]"; //从2开始
    public static String SINA_TOPIC_TITLE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[1]/div[3]/div[4]";
    public static String SINA_TOPIC_LIKE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[4]/a/span/span/span";
    public static String SINA_TOPIC_COMMENT = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[3]/a/span/span/span";
    public static String SINA_TOPIC_SHARE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[2]/a/span/span/span";
    public static String SINA_TOPIC_COLLECTION = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[2]/div[2]/div/ul/li[2]/a";
}