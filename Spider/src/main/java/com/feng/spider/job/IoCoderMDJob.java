package com.feng.spider.job;

import com.feng.spider.model.SpiderLoginEventNode;
import com.feng.spider.utils.ChromDriverSpider;
import com.feng.spider.utils.Conf;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/* 知识星球
 * @author feng
 * @date 2019/2/22 10:54
 * @since 1.0
 */
public class IoCoderMDJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoCoderMDJob.class);

    private static final String ENTENRANCE_URL = "http://svip.iocoder.cn";
    private static final String USERNAME_XPATH = "/html/body/div[1]/div[2]/div[1]/div[1]/div[1]/input";
    private static final String PASSWORD_XPATH = "/html/body/div[1]/div[2]/div[1]/div[2]/input";
    private static final String LOGIN_XPATH = "/html/body/div[1]/div[2]/div[1]/button";
    private static final String SPECIAL_COLUMN_COUNT_XPATH = "//*[@id=\"header\"]/nav[1]/ul";
    private static final String SPECIAL_COLUMN_XPATH = "//*[@id=\"header\"]/nav[1]/ul/li[%s]/a";
    private static final String ARTICLE_LIST_SECTION_COUNT_XPATH = "//*[@id=\"js-content\"]";
    private static final String ARTICLE_LIST_SECTION_ARTICLE_COUNT_XPATH = "//*[@id=\"js-content\"]/section[%s]/div";
    private static final String ARTICLE_LIST_ENTER_XPATH = "//*[@id=\"js-content\"]/section[%s]/div/article[%s]/div/header/h1/a";
    private static final String ARTICLE_TITLE_XPATH = "//*[@id=\"wrapper\"]/div/div[2]/article/div/header/h1";
    private static final String ARTICLE_CONTENT_XPATH = "//*[@id=\"wrapper\"]/div/div[2]/article/div/div[1]";

    public static void main(String[] args) throws InterruptedException {
       spider(6);
    }

    public static void spider(int i) {

        try {

            //1. 初始化浏览器
            WebDriver driver = ChromDriverSpider.initDriver(-1, -1);
            ChromDriverSpider.openUrl(driver, String.format("http://%s:%s@svip.iocoder.cn",
                    Conf.getConf("iocoder.username"), Conf.getConf("iocoder.pwd")));

            int columnCount = ChromDriverSpider.getSubElementCount(driver, SPECIAL_COLUMN_COUNT_XPATH, "/li");
            LOGGER.info("共有专栏：{}", columnCount);

           for(; i<=columnCount; i++){

               String columnXPath = String.format(SPECIAL_COLUMN_XPATH, i);
               String title = ChromDriverSpider.getContent(driver, columnXPath);
               if(StringUtils.isEmpty(title)){
                   LOGGER.error("title is empty:{}", i);
                   break;
               }

               title = title.replaceAll(" ", "");
               File parentFile = new File(title);
               if (!parentFile.exists()) {
                   parentFile.mkdir();
               }

               ChromDriverSpider.click(driver, columnXPath);

               //到专栏list页
               int sectionCount = ChromDriverSpider.getSubElementCount(driver, ARTICLE_LIST_SECTION_COUNT_XPATH, "/section");
               LOGGER.info("专栏：{}， 篇章：{}", columnCount, sectionCount);
               int count = 1;
               for(int j=1; j<=sectionCount; j++){

                    int articleCount = ChromDriverSpider.getSubElementCount(driver, String.format(ARTICLE_LIST_SECTION_ARTICLE_COUNT_XPATH, j), "/article");
                    LOGGER.info("专栏：{}， 篇章：{}， 文章：{}", columnCount, sectionCount, articleCount);
                    for(int z=1; z<=articleCount; z++) {
                        try {
                            String articleXPath = String.format(ARTICLE_LIST_ENTER_XPATH, j, z);
                            ChromDriverSpider.click(driver, articleXPath);
                            String articleTitle = ChromDriverSpider.getContent(driver, ARTICLE_TITLE_XPATH);
                            String content = ChromDriverSpider.getContent(driver, ARTICLE_CONTENT_XPATH);
                            //存储文章
                            saveArticle(count, title, articleTitle, content);
                            count++;
                            ChromDriverSpider.click(driver, columnXPath);
                        } catch (Exception e) {
                            LOGGER.info("article:{}-{} finish", title, z, e);
                            break;
                        }
                    }
               }

           }
        } catch (Exception e) {
            LOGGER.error("error", e);
        }
    }

    private static void saveArticle(int index, String title, String articleTitle, String content) throws IOException {
        String savePath = title + "/"+(index>=10?index+"":"0"+index) + "_"+articleTitle+".md";
        savePath = savePath.replaceAll("\n", "").replaceAll(" ","").replaceAll("[?*|><]", "_");
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            return;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveFile)));
        bw.write("<h1 style=\"text-align:center\">" + articleTitle + "</h1>");
        bw.write(content);
        bw.newLine();
        bw.close();
    }
}