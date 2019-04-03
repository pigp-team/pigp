package com.feng.spider.job;

import com.feng.spider.model.*;
import com.feng.spider.utils.ChromDriverSpider;
import com.feng.spider.utils.Conf;
import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author feng
 * @date 2019/3/8 16:26
 * @since 1.0
 */
public class JKTimeJob2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKTimeJob2.class);
    private static final String TITLE = "从0开始学微服务";
    private static final String ENTENRANCE_URL = "https://account.geekbang.org/login?redirect=https%3A%2F%2Ftime.geekbang.org%2F";
    private static final String USERNAME_XPATH = "/html/body/div[1]/div[2]/div[1]/div[1]/div[1]/input";
    private static final String PASSWORD_XPATH = "/html/body/div[1]/div[2]/div[1]/div[2]/input";
    private static final String LOGIN_XPATH = "/html/body/div[1]/div[2]/div[1]/button";
    private static final String SPECIAL_COLUMN_XPATH = "//*[@id=\"app\"]/div[1]/div[3]/div[2]/dl/dd[2]/a";
    private static final String SPECIAL_COLUMN_LIST_TITLE_XPATH = "//*[@id=\"app\"]/div[1]/div[3]/ul/li[%s]/div[2]/h6";
    private static final String SEPCIAL_COLUMN_LIST_ENTER_XPATH = "//*[@id=\"app\"]/div[1]/div[3]/ul/li[%s]/a";
    private static final String SPECIAL_COLUMN_LIST_LOAD_MORE_XPATH = "//*[@id=\"app\"]/div[1]/button";
    private static final String ARTICLE_LIST_SORT_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/a";
    private static final String ARTICLE_LIST_SORT_CONTENT = "倒序";
    private static final String ARTICLE_LIST_NUM_CONTENT_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/span";
    private static final String ARTICLE_LIST_ENTER_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[3]/div/div[%s]/div/div[2]/a";
    private static final String ARTICLE_TITLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/h1";
    private static final String ERROR_ALERT_XPATH = "/html/body/div[*]/div[2]/div[2]/a";
    private static final String ARTICLE_NEXT_ARTICLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[3]/div[%s]";

    public static void main(String[] args) {

        SpiderEventNode eventNode = buildEventConfList(1);
        spider(eventNode, TITLE);
    }

    /**
     * 事件对象必须要抽象出一个类来，但是不同的事件对应的配置项也不相同，这里应该怎么处理
     * @return
     */
    public static SpiderEventNode buildEventConfList(int i){

        SpiderLoginEventNode loginEventNode = new SpiderLoginEventNode();
        loginEventNode.setUserName(Conf.getConf("jk.username"));
        loginEventNode.setPasswd(Conf.getConf("jk.pwd"));
        loginEventNode.setUserNameXPath(USERNAME_XPATH);
        loginEventNode.setPasswdXPath(PASSWORD_XPATH);
        loginEventNode.setLoginXPath(LOGIN_XPATH);
        loginEventNode.setLoginURL(ENTENRANCE_URL);

        SpiderClickEventNode clickEventNode = new SpiderClickEventNode();
        clickEventNode.setClickXPath(SPECIAL_COLUMN_XPATH);
        loginEventNode.setNext(clickEventNode);

        SpiderClickEventNode clickColumnListEventNode = new SpiderClickEventNode();
        clickColumnListEventNode.setMatchContent(TITLE);
        clickColumnListEventNode.setMatchXPath(SPECIAL_COLUMN_LIST_TITLE_XPATH);
        clickColumnListEventNode.setLoadMoreXPath(SPECIAL_COLUMN_LIST_LOAD_MORE_XPATH);
        clickColumnListEventNode.setClickXPath(SEPCIAL_COLUMN_LIST_ENTER_XPATH);
        clickEventNode.setNext(clickColumnListEventNode);

        SpiderTransferWindowsEventNode transferWindowsEventNode = new SpiderTransferWindowsEventNode();
        clickColumnListEventNode.setNext(transferWindowsEventNode);

        SpiderClickEventNode clickSortEventNode = new SpiderClickEventNode();
        clickSortEventNode.setMatchContent(ARTICLE_LIST_SORT_CONTENT);
        clickSortEventNode.setMatchXPath(ARTICLE_LIST_SORT_XPATH);
        clickSortEventNode.setClickXPath(ARTICLE_LIST_SORT_XPATH);
        transferWindowsEventNode.setNext(clickSortEventNode);


        SpiderClickEventNode clickArticleEventNode = new SpiderClickEventNode();
        clickArticleEventNode.setClickXPath(ARTICLE_LIST_ENTER_XPATH);
        //clickArticleEventNode.setLoadMoreXPath();
        return loginEventNode;
    }

    /**
     * 期望：传入的是一些列事件的链表
     * 就像生产线一样
     */
    public static void spider(SpiderEventNode eventNode, String title){

        //1. 初始化浏览器
        WebDriver driver = ChromDriverSpider.initDriver(1200, 800);

        //2. 创建自己的工作空间
        File parentFile = new File(title);
        if (!parentFile.exists()) {
            LOGGER.info("namespace is not exists, create : {}", parentFile.getName());
            parentFile.mkdir();
        }

        //3. 遍历按照顺序执行每一种事件操作
        for(; eventNode!=null; eventNode=eventNode.getNext()){
            //判断事件类型
            operationEvent(driver, eventNode);
        }
    }

    private static void operationEvent(WebDriver driver, SpiderEventNode eventNode) {

        switch (eventNode.getEventEnum()){
            case LOGIN:
                ChromDriverSpider.login(driver, (SpiderLoginEventNode)eventNode);
                break;
            case CLICK:
                ChromDriverSpider.click(driver, (SpiderClickEventNode)eventNode);
                break;
            case TRANSFER_WINDOWS:
                ChromDriverSpider.switchWindows(driver, Lists.newArrayList(driver.getWindowHandle()));
                break;
            case OPERATION:
                ChromDriverSpider.operation(driver, (SpiderOperationEventNode)eventNode);
                break;
        }
    }



}