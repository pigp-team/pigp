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
    public static String SINA_LOGIN_ACK = "//*[@id=\"plc_top\"]/div/div/div[3]/div[1]/ul/li[5]/a/em[2]";
    public static String SINA_LOGOUT_TOP = "//*[@id=\"pl_common_top\"]/div/div/div[3]/div[2]/div[2]/a";
    public static String SINA_LOGOUT = "//*[@id=\"pl_common_top\"]/div/div/div[3]/div[2]/div[2]/div/ul/li[10]/a";
    public static String SINA_SEARCH_INPUT = "//*[@id=\"pl_common_top\"]/div/div/div[2]/input";
    public static String SINA_SEARCH_BUTTON = "//*[@id=\"pl_common_top\"]/div/div/div[2]/a";

    public static String SINA_RELATIVE_USER = "//*[@id=\"pl_feed_main\"]/div[2]/div[2]/div[1]/div/div[2]/div[%d]/div[2]/div/a";

    public static String SINA_ATTENTION = "//*[@id=\"Pl_Official_Headerv6__1\"]/div[1]/div/div[2]/div[4]/div/div[1]/a[1]";
    public static String SINA_TOPIC = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]"; //从2开始
    public static String SINA_TOPIC_TITLE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[1]/div[3]/div[4]";
    public static String SINA_TOPIC_LIKE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[4]/a";
    public static String SINA_TOPIC_COMMENT = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[3]/a";
    public static String SINA_TOPIC_SHARE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[2]/a";
    public static String SINA_TOPIC_COLLECTION = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[2]/div[2]/div/ul/li[2]/a";

    public static String SINA_TOPIC_SHARE_INPUT = "//*[@id=\"layer_15628112698041\"]/div[2]/div[3]/div/div[2]/div/div[2]/div/div[1]/div/div[1]/textarea";
    public static String SINA_TOPIC_SHARE_BUTTON = "//*[@id=\"layer_15628112698041\"]/div[2]/div[3]/div/div[2]/div/div[2]/div/div[1]/div/div[1]/textarea";

    public static String SINA_TOPIC_COMMENT_INPUT = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[3]/div/div/div[2]/div[2]/div[1]/textarea";
    public static String SINA_TOPIC_COMMENT_BUTTON = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[3]/div/div/div[2]/div[2]/div[2]/div[1]/a";

    public static String FULL_COMMENT_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[4]/div[1]/a[1]";
    public static String FULL_COMMENT_ATTENTION = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[3]/div[2]/a";

    public static String FULL_LOGIN_USERNAME = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[1]/input";
    public static String FULL_LOGIN_PWD = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[2]/input";
    public static String Full_LOGIN_BUTTON = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[6]/a";

    public static String Full_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[4]/a";
    public static String MESSAGE_COMMENT_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[3]/a";
    public static String MESSAGE_COMMENT_INFPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[2]/div[2]/div[1]/textarea";
    public static String MESSAAGE_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[2]/div[2]/div[2]/div[1]/a";

    public static String MESSAGE_SHARE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[2]/a";
    public static String MESSAGE_SHARE_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[5]/div/div[3]/div/div/div/div/div/div[1]/textarea";
    public static String MESSAGE_SHARE_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[5]/div/div[3]/div/div/div/div/div/div[2]/div[1]/a";

    public static String COMMENT_TOTOLE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]";
    public static String COMMENT_FIRST_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[1]/div[2]/div[1]/a[1]";
    public static String COMMENT_FIRST_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[1]/a[1]";
    public static String COMMENT_FIRST_TIME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[1]/div[2]/div[3]/div[2]";
    public static String COMMENT_FIRST_COMMENT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[1]/ul/li[3]/span[1]/a";
    public static String COMMENT_FIRST_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[1]/ul/li[4]/span/a";

    public static String COMMENT_SUB_TOTOLE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]";
    public static String COMMENT_SUB_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[1]/a[1]";
    public static String COMMENT_SUB_TIME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[1]/div/div[3]/div[2]";
    public static String COMMENT_SUB_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[1]/ul/li[5]/span/a";
    public static String COMMENT_TOTAL_MORE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/a";
    public static String COMMENT_FIRST_COMMENT_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[3]/div/div/div[1]/textarea";
    public static String COMMENT_FIRST_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[3]/div/div/div[2]/div[1]/a";
    public static String COMMENT_SUB_COMMENT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[1]/ul/li[4]/span[1]/a";
    public static String COMMENT_SUB_COMMENT_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[3]/div/div/div[1]/textarea";
    public static String COMMENT_SUB_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[3]/div/div/div[2]/div[1]/a";
    public static String SINA_TOPIC_MESSAGE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[4]/div[4]/a[1]";
    public static String COMMENT_SUB_MORE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/a";
}