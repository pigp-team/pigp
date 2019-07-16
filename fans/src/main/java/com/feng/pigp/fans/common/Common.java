package com.feng.pigp.fans.common;

/**
 * @author feng
 * @date 2019/7/10 11:32
 * @since 1.0
 */
public class Common {

    public static final String SINA_URL = "https://weibo.com/";
    public static final String SINA_LOGIN_USERNAME = "//*[@id=\"loginname\"]";
    public static final String SINA_LOGIN_PWD = "//*[@id=\"pl_login_form\"]/div/div[3]/div[2]/div/input";
    public static final String SINA_LOGIN_BUTTON = "//*[@id=\"pl_login_form\"]/div/div[3]/div[6]/a";
    public static final String SINA_LOGIN_ACK = "/html/body/div[4]/div[1]/div[1]/p[1]";
    public static final String SINA_LOGOUT_TOP = "/html/body/div[1]/div/div/div[3]/div[2]/div[2]/a";
    public static final String SINA_LOGOUT = "/html/body/div[1]/div/div/div[3]/div[2]/div[2]/div/ul/li[10]/a";
    public static final String SINA_SEARCH_INPUT = "//*[@id=\"pl_common_top\"]/div/div/div[2]/input";
    public static final String SINA_SEARCH_BUTTON = "//*[@id=\"pl_common_top\"]/div/div/div[2]/a";

    public static final String SINA_RELATIVE_USER = "//*[@id=\"pl_feed_main\"]/div[2]/div[2]/div[1]/div/div[2]/div[%d]/div[2]/div/a";

    public static final String SINA_ATTENTION = "//*[@id=\"Pl_Official_Headerv6__1\"]/div[1]/div/div[2]/div[4]/div/div[1]/a[1]";
    public static final String SINA_TOPIC = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]"; //从2开始
    public static final String SINA_TOPIC_TITLE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[1]/div[3]/div[4]";
    public static final String SINA_TOPIC_LIKE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[4]/a";
    public static final String SINA_TOPIC_COMMENT = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[3]/a";
    public static final String SINA_TOPIC_SHARE = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[2]/div/ul/li[2]/a";
    public static final String SINA_TOPIC_COLLECTION = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[2]/div[2]/div/ul/li[2]/a";

    public static final String SINA_TOPIC_SHARE_INPUT = "//*[@id=\"layer_15628112698041\"]/div[2]/div[3]/div/div[2]/div/div[2]/div/div[1]/div/div[1]/textarea";
    public static final String SINA_TOPIC_SHARE_BUTTON = "//*[@id=\"layer_15628112698041\"]/div[2]/div[3]/div/div[2]/div/div[2]/div/div[1]/div/div[1]/textarea";

    public static final String SINA_TOPIC_COMMENT_INPUT = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[3]/div/div/div[2]/div[2]/div[1]/textarea";
    public static final String SINA_TOPIC_COMMENT_BUTTON = "//*[@id=\"Pl_Official_MyProfileFeed__20\"]/div/div[%s]/div[3]/div/div/div[2]/div[2]/div[2]/div[1]/a";

    public static final String FULL_COMMENT_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[4]/div[1]/a[1]";
    public static final String FULL_COMMENT_ATTENTION = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[3]/div[2]/a";

    public static final String FULL_LOGIN_USERNAME = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[1]/input";
    public static final String FULL_LOGIN_PWD = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[2]/input";
    public static final String Full_LOGIN_BUTTON = "//*[@id=\"layer_15628458207771\"]/div[2]/div[3]/div[3]/div[6]/a";

    public static final String Full_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[4]/a";
    public static final String MESSAGE_COMMENT_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[3]/a";
    public static final String MESSAGE_COMMENT_INFPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[2]/div[2]/div[1]/textarea";
    public static final String MESSAAGE_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[2]/div[2]/div[2]/div[1]/a";

    public static final String MESSAGE_SHARE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[2]/div/ul/li[2]/a";
    public static final String MESSAGE_SHARE_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[5]/div/div[3]/div/div/div/div/div/div[1]/textarea";
    public static final String MESSAGE_SHARE_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[5]/div/div[3]/div/div/div/div/div/div[2]/div[1]/a";

    public static final String COMMENT_TOTOLE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]";
    public static final String COMMENT_FIRST_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[1]/div[2]/div[1]/a[1]";
    public static final String COMMENT_FIRST_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[1]/a[1]";
    public static final String COMMENT_FIRST_TIME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[1]/div[2]/div[3]/div[2]";
    public static final String COMMENT_FIRST_COMMENT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[1]/ul/li[3]/span[1]/a";
    public static final String COMMENT_FIRST_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[1]/ul/li[4]/span/a";

    public static final String COMMENT_SUB_TOTOLE_FLAG = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]";
    public static final String COMMENT_SUB_USERNAME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[1]/a[1]";
    public static final String COMMENT_SUB_TIME = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[2]/div[2]/div[4]/div/div[1]/div/div[3]/div[2]";
    public static final String COMMENT_SUB_LIKE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[1]/ul/li[5]/span/a";
    public static final String COMMENT_TOTAL_MORE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/a";
    public static final String COMMENT_FIRST_COMMENT_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[3]/div/div/div[1]/textarea";
    public static final String COMMENT_FIRST_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[3]/div[3]/div/div/div[2]/div[1]/a";
    public static final String COMMENT_SUB_COMMENT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[1]/ul/li[4]/span[1]/a";
    public static final String COMMENT_SUB_COMMENT_INPUT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[3]/div/div/div[1]/textarea";
    public static final String COMMENT_SUB_COMMENT_SUBMIT = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/div[3]/div[3]/div/div/div[2]/div[1]/a";
    public static final String SINA_TOPIC_MESSAGE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[1]/div[4]/div[4]/a[1]";
    public static final String COMMENT_SUB_MORE = "//*[@id=\"Pl_Official_WeiboDetail__73\"]/div/div/div/div[4]/div/div[3]/div[2]/div/div/div[%s]/div[2]/div[4]/div/div[%s]/div/a";

    public static final String ATTENTION_ALERT_ERROR = "/html/body/div[10]/div[2]/div[4]/a[1]";
    public static final String ATTENTION_ALERT_CLOSE = "/html/body/div[10]/div[2]/div[4]/a[2]";

    public static final String VALIDATE_CODE_IMAGE = "//*[@id=\"pl_login_form\"]/div/div[3]/div[3]/a/img";
    public static final String VALIDATE_CODE_INPUT = "//*[@id=\"pl_login_form\"]/div/div[3]/div[3]/div/input";
    public static final String COMMENT_ERROR_LIMIT = "/html/body/div[11]/div[2]/div[4]/a";
    public static final String ACCOUNT_ALL_ERROR = "//*[@id=\"message-p\"]";
    public static final String AGREE_RULE = "/html/body/div[8]/div[2]/div[3]/a[1]";

    public static final String INTERNALE_SINA_LOGIN_USERNAME = "//*[@id=\"username\"]";
    public static final String INTERNALE_SINA_LOGIN_PWD = "//*[@id=\"password\"]";
    public static final String INTERNALE_SINA_LOGIN_BUTTON = "//*[@id=\"vForm\"]/div[2]/div/ul/li[7]/div[1]/input";
    public static final String INTERNALE_VALIDATE_CODE_IMAGE ="//*[@id=\"check_img\"]";
    public static final String INTERNALE_VALIDATE_CODE_INPUT = "//*[@id=\"door\"]";
    public static final String INTERNALE_VALIDATE_CODE_IMAGE_CHANGE = "//*[@id=\"door_img\"]/a";
    public static final String INTERNALE_SINA_LOGIN_URL = "https://login.sina.com.cn/signup/signin.php";


    public static final String M_SINA_URL = "https://m.weibo.cn/u/6117570574";
    public static final String M_SINA_INDEX = "//*[@id=\"app\"]/div[1]/div[1]/div[2]/nav/div/div/div/ul/li[1]/span";
    public static final String M_SINA_INTERACTIVE = "//*[@id=\"app\"]/div[1]/div[1]/div[3]/div/div/div[2]/div/div/div[3]/div";
    public static final String M_LAST_VALUE = "//*[@id=\"app\"]/div[1]/div[2]/div[3]/div/div/div[2]/div/div/div[1]/div/h3";
    public static final String M_ALL_VALUE = "//*[@id=\"app\"]/div[1]/div[2]/div[3]/div/div/div[2]/div/div/div[2]/div/h3";

    public static final String SETTING_IMAGE_URL = "chrome://settings/content/images";
    public static final String SETTING_IMAGE_TXT = "//*[@id=\"labelWrapper\"]/div[1]";
    public static final String SETTING_IMAGE_BUTTON = "//*[@id=\"knob\"]";

}