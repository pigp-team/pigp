package com.feng.spider.job;

import com.feng.spider.model.SpiderLoginEventNode;
import com.feng.spider.utils.ChromDriverSpider;
import com.feng.spider.utils.Conf;
import com.feng.spider.utils.ImageUtils;
import com.feng.spider.utils.PDFUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Random;

/**
 * @author feng
 * @date 2019/2/22 10:54
 * @since 1.0
 */
public class JKTimeMDJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKTimeMDJob.class);

    private static final String TITLE = "邱岳的产品实战";
    private static boolean isBatch = true;
    private static final String ENTENRANCE_URL = "https://account.geekbang.org/login?redirect=https%3A%2F%2Ftime.geekbang.org%2F";
    private static final String USERNAME_XPATH = "/html/body/div[1]/div[2]/div[1]/div[1]/div[1]/input";
    private static final String PASSWORD_XPATH = "/html/body/div[1]/div[2]/div[1]/div[2]/input";
    private static final String LOGIN_XPATH = "/html/body/div[1]/div[2]/div[1]/button";
    private static final String SPECIAL_COLUMN_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[1]/div[1]/a[2]";
    private static final String SPECIAL_COLUMN_LIST_TITLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/div[%s]/div[2]/div[1]/div[1]/h2";
    private static final String SEPCIAL_COLUMN_LIST_ENTER_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[1]/div[2]/div[%s]/div[1]/a";
    private static final String SPECIAL_COLUMN_LIST_LOAD_MORE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[1]/div[3]/button";
    private static final String ARTICLE_LIST_NUM_CONTENT_XPATH = "//*[@id=\"app\"]/div[1]/div[1]/div[3]/div[1]/div[2]";
    private static final String ARTICLE_LIST_ENTER_XPATH = "//*[@id=\"app\"]/div[1]/div[1]/div[3]/div[%s]";
    private static final String ARTICLE_LIST_ENTER_SUB_XPATH = "//*[@id=\"app\"]/div[1]/div[1]/div[3]/div[%s]/div[2]/div";
    private static final String ARTICLE_TITLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div[1]/h1";
    private static final String ARTICLE_CONTENT_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div[1]/div[2]/div[%s]/div";
    private static final String ARTICLE_COMMIT_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div[1]/div[4]";

    public static void main(String[] args) throws InterruptedException {
       spider(1, 1);
    }

    public static void spider(int i, int count){

        WebDriver driver = null;
        try {
            List<String> windowsList = Lists.newArrayList();

            SpiderLoginEventNode loginEventNode = new SpiderLoginEventNode();
            loginEventNode.setUserName(Conf.getConf("jk.username"));
            loginEventNode.setPasswd(Conf.getConf("jk.pwd"));
            loginEventNode.setUserNameXPath(USERNAME_XPATH);
            loginEventNode.setPasswdXPath(PASSWORD_XPATH);
            loginEventNode.setLoginXPath(LOGIN_XPATH);
            loginEventNode.setLoginURL(ENTENRANCE_URL);

            //1. 初始化浏览器
            driver = ChromDriverSpider.initDriver(-1, -1);
            windowsList.add(driver.getWindowHandle());

            //2. 创建自己的工作空间
            File parentFile = new File(TITLE);
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }

            //3. 登录
            ChromDriverSpider.login(driver, loginEventNode);

            //4. 点击专栏  专栏xpath，点击事件
            ChromDriverSpider.click(driver, SPECIAL_COLUMN_XPATH);

            //5. 从列表中匹配专题，获取对应专题的xpath，点击事件进入
            ChromDriverSpider.findTopicWithNextPage(driver, TITLE, SPECIAL_COLUMN_LIST_TITLE_XPATH, SEPCIAL_COLUMN_LIST_ENTER_XPATH, SPECIAL_COLUMN_LIST_LOAD_MORE_XPATH);

            //6. 切换窗口
            String newWindows = ChromDriverSpider.switchWindows(driver, windowsList);

            windowsList.add(newWindows);

            //8.  获取指定标签的内容信息
            int num = ChromDriverSpider.getIntContent(driver, ARTICLE_LIST_NUM_CONTENT_XPATH, " ", " ");
            System.out.println("更新：" + num);

            //10. 找到目标处理对应的操作，这里就是截图
            //11. 找到下一个目标
            for (; i <= num; i++) {

                //9. 找到匹配的文章，并点击进入
                String articleXPath = String.format(ARTICLE_LIST_ENTER_XPATH, i+3);

                try {

                    if(count>num){
                        driver.quit();
                        System.exit(0);
                    }
                    ChromDriverSpider.click(driver, articleXPath);
                    Thread.sleep(5000);

                    if(isBatch){

                        //该batch下有几篇文章
                        int subNum = ChromDriverSpider.getSubElementCount(driver,  String.format(ARTICLE_LIST_ENTER_SUB_XPATH, i+3), "/div");
                        System.out.println("该batch存在" + subNum + "篇文章");

                        if(subNum<=0){
                            continue;
                        }

                        if(subNum==1){
                            ChromDriverSpider.click(driver, String.format(ARTICLE_LIST_ENTER_SUB_XPATH, i+3)+"/div");
                            spider(driver, count++);
                            continue;
                        }

                        for(int k=1; k<=subNum; k++){
                            ChromDriverSpider.click(driver, String.format(ARTICLE_LIST_ENTER_SUB_XPATH, i+3)+"/div["+k+"]");
                            spider(driver, count++);
                        }
                        continue;
                    }

                    spider(driver, count++);

                } catch (Exception e) {
                    e.printStackTrace();
                    //重新登录重新搞
                    driver.quit();
                    System.out.println("开始下一次操作：" + i);
                    spider(i, count);
                }
            }
            driver.quit();
            System.exit(0);
        }catch(Exception e) {

            if(driver!=null){
                driver.quit();
            }
            System.out.println("开始下一次操作：" + i);
            spider(i, count);
        }
    }


    public static void spider(WebDriver driver, int i) throws IOException {
        String articleTitle = ChromDriverSpider.getContent(driver, ARTICLE_TITLE_XPATH);
        System.out.println("开始爬取文章：{}" + articleTitle);
        String savePath = TITLE + "/"+(i>=10?i+"":"0"+i) + "_"+articleTitle.replaceAll("[?*|>< :/]", "_")+".md";
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            return;
        }

        String content = "";
        String commit = ChromDriverSpider.getContent(driver, ARTICLE_COMMIT_XPATH);
        try {
            content = ChromDriverSpider.getContent(driver, String.format(ARTICLE_CONTENT_XPATH, 3));
        }catch (NoSuchElementException e){
            LOGGER.error("没有录音div， div往前-1");
            content = ChromDriverSpider.getContent(driver, String.format(ARTICLE_CONTENT_XPATH, 2));
        }

        if(StringUtils.isNotEmpty(commit)){
            commit = commit.replaceAll("<img [^>]*?>", "").replaceAll("<i class=\"iconfont\">\uE63B</i>", "<span> 点赞数：</span>");
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile)));
        bw.write("<h1 style=\"text-align:center\">" + articleTitle + "</h1>");
        bw.write(content);
        bw.newLine();
        bw.newLine();
        bw.write(commit);
        bw.newLine();
        bw.close();
    }

}