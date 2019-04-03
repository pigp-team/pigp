package com.feng.spider.job;

import com.feng.spider.model.SpiderLoginEventNode;
import com.feng.spider.utils.ChromDriverSpider;
import com.feng.spider.utils.Conf;
import com.feng.spider.utils.ImageUtils;
import com.feng.spider.utils.PDFUtils;
import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.List;

/**
 * @author feng
 * @date 2019/2/22 10:54
 * @since 1.0
 * 缺点： 还不知道怎么将滚动条设置消失
 */
public class JKTimeJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(JKTimeJob.class);

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
    private static final String ARTICLE_LIST_NUM_CONTENT_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/span";
    private static final String ARTICLE_LIST_ENTER_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div[3]/div/div[%s]/div/div[2]/a";
    private static final String ARTICLE_TITLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/h1";
    private static final String ERROR_ALERT_XPATH = "/html/body/div[*]/div[2]/div[2]/a";
    private static final String ARTICLE_NEXT_ARTICLE_XPATH = "//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[3]/div[%s]";

    public static void main(String[] args) throws InterruptedException {
       spider(1);
    }

    public static void spider(int i){

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
            WebDriver driver = ChromDriverSpider.initDriver(1200, 800);
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

            //7. 根据内容判断是否进行点击操作 调整为正序
            String sortContent = ChromDriverSpider.getContent(driver, ARTICLE_LIST_SORT_XPATH);
            if ("倒序".equals(sortContent)) {
                ChromDriverSpider.click(driver, ARTICLE_LIST_SORT_XPATH);
            }

            //8.  获取指定标签的内容信息
            int num = ChromDriverSpider.getIntContent(driver, ARTICLE_LIST_NUM_CONTENT_XPATH, "已更新 ", " 篇");
            System.out.println("更新：" + num);

            //9. 找到匹配的文章，并点击进入
            String articleXPath = String.format(ARTICLE_LIST_ENTER_XPATH, i);

            //10. 找到目标处理对应的操作，这里就是截图
            //11. 找到下一个目标
            for (; i <= num; i++) {

                try {

                    while(!ChromDriverSpider.find(driver, articleXPath)){
                        System.out.println("找不到响应的文章");
                        if(ChromDriverSpider.find(driver, ERROR_ALERT_XPATH)){
                            driver.quit();
                            spider(i);
                        }
                        //模拟滚动条滑到底部
                        ChromDriverSpider.executeJS(driver, "document.documentElement.scrollTop=100000");
                    }
                    ChromDriverSpider.click(driver, articleXPath);
                    Thread.sleep(5000);

                    String articleTitle = ChromDriverSpider.getContent(driver, ARTICLE_TITLE_XPATH);
                    System.out.println("开始爬取文章：{}" + articleTitle);
                    String savePath = TITLE + "/"+i + ".png";
                    File saveFile = new File(savePath);
                    if (saveFile.exists()) {
                        System.out.println("文件已经存在：" + articleTitle);
                        articleXPath = String.format(ARTICLE_NEXT_ARTICLE_XPATH, i <= 1 ? 1 : 2);
                        continue;
                    }

                    long height = ChromDriverSpider.getHeight(driver);

                    //6. 截图
                    int count = 0;
                    int batch = driver.manage().window().getSize().getHeight() - 128;
                    while (count * batch < height - batch) {
                        Thread.sleep(2000);
                        ChromDriverSpider.screenShot(driver, TITLE + i + "_" + count + ".png", count * batch);
                        if(ChromDriverSpider.find(driver, ERROR_ALERT_XPATH)){
                            System.out.println("开始下一次操作：" + i);
                            driver.quit();
                            spider(i);
                        }
                        count++;
                    }

                    //合并截图
                    ImageUtils.mergeImage(savePath, TITLE + i + "_%s.png", count);
                    articleXPath = String.format(ARTICLE_NEXT_ARTICLE_XPATH, i <= 1 ? 1 : 2);
                } catch (Exception e) {
                    e.printStackTrace();
                    //重新登录重新搞
                    driver.quit();
                    System.out.println("开始下一次操作：" + i);
                    spider(i);
                }
            }

            //保存成pdf
            PDFUtils.toPdf(TITLE + "/%s.png", num, TITLE + ".pdf");
            driver.quit();
            System.exit(0);
        }catch(Exception e) {

            System.out.println("开始下一次操作：" + i);
            spider(i);
        }
    }
}