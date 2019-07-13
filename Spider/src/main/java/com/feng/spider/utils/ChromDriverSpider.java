package com.feng.spider.utils;

import com.feng.spider.exception.PigpException;
import com.feng.spider.model.SpiderClickEventNode;
import com.feng.spider.model.SpiderLoginEventNode;
import com.feng.spider.model.SpiderOperationEventNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author feng
 * @date 2019/2/27 10:53
 * @since 1.0
 */
public class ChromDriverSpider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromDriverSpider.class);
    public static final String DRIVER_PATH = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe";
    public static final int LOADING_WAITING_TIME = 3000;


    public static void openUrl(WebDriver driver, String url){
        driver.get(url);
    }

    /**
     * 初始化driver对象
     * driver对象相当于是浏览器对象
     * @return
     */
    public static WebDriver initDriver(int width, int height){

        LOGGER.info("start init chrom driver");
        // 设置ChromeDriver的路径
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        //设置浏览器最大化
        options.addArguments("--start-maximized");
        //options.addArguments("--start-fullscreen");
        //设置ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //设置截屏支持
        options.setCapability("takesScreenshot", true);
        //设置css支持
        options.setCapability("cssSelectorsEnabled", true);

        //创建driver对象
        WebDriver driver = new ChromeDriver(options);
        //设置隐性等待（作用于全局）
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        //810*800
        if(width>0 && height>0) {
            driver.manage().window().setSize(new Dimension(width, height));
        }
        LOGGER.info("finish intit chrom driver, windows width={}, height={}", width, height);
        return driver;
    }

    /**
     * 模拟登陆
     * @param driver
     * @param eventNode
     */
    public static void login(WebDriver driver, SpiderLoginEventNode eventNode){

        LOGGER.info("start login, userName={}", eventNode.getUserName());
        //打开页面
        driver.get(eventNode.getLoginURL());
        //查找元素
        WebElement element = driver.findElement(By.xpath(eventNode.getUserNameXPath()));
        element.sendKeys(eventNode.getUserName());
        WebElement pwdElement = driver.findElement(By.xpath(eventNode.getPasswdXPath()));
        pwdElement.sendKeys(eventNode.getPasswd());
        click(driver, eventNode.getLoginXPath());
        LOGGER.info("{} : login success");
    }

    /**
     * 模拟点击事件
     * 点击涉及到请求，请求后等待2s等待页面的刷新
     * @param driver
     * @param eventNode
     */
    public static void click(WebDriver driver, SpiderClickEventNode eventNode){

        boolean isMatch = true;
        if(StringUtils.isEmpty(eventNode.getMatchContent())){

            if(StringUtils.isEmpty(eventNode.getLoadMoreXPath())) {
                //找匹配内容,根据指定xpath获取内容
                String content = getContent(driver, eventNode.getMatchXPath());
                isMatch = eventNode.getMatchContent().equals(content);
            }else{
                //需要搜索获取匹配内容
                int count = findTopicWithNextPage(driver, eventNode.getMatchContent(), eventNode.getMatchXPath(), eventNode.getLoadMoreXPath());
                eventNode.setClickXPath(String.format(eventNode.getClickXPath(), count));
            }
        }

        if(isMatch) {
            click(driver, eventNode.getClickXPath());
        }
    }

    /**
     * 自动翻页查找需要找的主题
     * 该查找为精确查找
     * @param driver
     * @param topicXPath  获取专题标题的xpath
     * @param topic 主题
     * @param loadXPath   加载更多/获取下一页的xpath
     */
    public static int findTopicWithNextPage(WebDriver driver, String topic, String topicXPath, String loadXPath){
        int count = 1;
        while(true) {
            WebElement topicElement = null;
            try {
                topicElement = driver.findElement(By.xpath(String.format(topicXPath, count)));
            }catch (Exception e){
                //找不到触发加载更多
                click(driver, loadXPath);
            }

            if(topicElement==null){
                continue;
            }

            String tile = topicElement.getText();
            if(tile.equals(topic)){
                break;
            }
            count++;
        }
        return count;
    }

    /**
     * 自动翻页查找需要找的主题
     * 该查找为精确查找
     * @param driver
     * @param topicXPath  获取专题标题的xpath
     * @param topic 主题
     * @param enterXPath  进入专题的xpath
     * @param loadXPath   加载更多/获取下一页的xpath
     */
    public static void findTopicWithNextPage(WebDriver driver, String topic, String topicXPath, String enterXPath, String loadXPath){
        int count = 1;
        int errCount = 0;
        while(true) {
            WebElement topicElement = null;

            if(errCount>=20){
                click(driver, loadXPath);
            }

            try {
                topicElement = driver.findElement(By.xpath(String.format(topicXPath, count)));
            }catch (Exception e){
                //找不到触发加载更多
                JavascriptExecutor js = (JavascriptExecutor)driver;
                js.executeScript("document.documentElement.scrollTop=100000");
                //click(driver, loadXPath);
                errCount++;
            }

            if(topicElement==null){
                continue;
            }

            errCount=0;
            String tile = topicElement.getText();
            if(tile.equals(topic)){
                click(driver, String.format(enterXPath, count));
                break;
            }
            count++;
        }
    }

    /**
     * 模拟点击事件
     * 点击涉及到请求，请求后等待2s等待页面的刷新
     * @param driver
     * @param xPath
     */
    public static void click(WebDriver driver, String xPath){

        WebElement clickElement = driver.findElement(By.xpath(xPath));
        boolean isDisplay = clickElement.isDisplayed();
        if(!isDisplay){
            //滚动
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].scrollIntoView()", clickElement);
        }
        clickElement.click();
        //休眠2s等待页面加载
        try {
            Thread.sleep(LOADING_WAITING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean find(WebDriver driver, String xPath){

        try {
            WebElement element = driver.findElement(By.xpath(xPath));
            return true;
        }catch (Exception e){
            System.out.println("没有被挤下线");
        }

        return false;
    }

    public static int getIntContent(WebDriver driver, String xPath, String startChar, String endChar){

        String str = getContent(driver, xPath);
        int start = str.indexOf(startChar);
        String temp = str.substring(start+1, str.indexOf(endChar, start+1));
        return Integer.parseInt(temp);
    }

    public static String getContent(WebDriver driver, String xPath) {

        WebElement element = driver.findElement(By.xpath(xPath));
        boolean isDsiplayed = element.isDisplayed();
        System.out.println("element is displayed : " + isDsiplayed);
        if(isDsiplayed){
            return element.getText();
        }

        return element.getAttribute("innerHTML");
    }

    public static String switchWindows(WebDriver driver, List<String> excludeWindows) {

        Set<String> handleSet = driver.getWindowHandles();
        for(String handle : handleSet){
            if(excludeWindows.contains(handle)){
                continue;
            }
            driver.switchTo().window(handle);
            return handle;
        }

        return null;
    }

    public static String switchWindows(WebDriver driver, String windows) {

        Set<String> handleSet = driver.getWindowHandles();
        for(String handle : handleSet){
            if(windows.equals(handle)){
                driver.switchTo().window(handle);
                return handle;
            }
        }

        return null;
    }

    public static void screenShot(WebDriver driver, String saveName, int height){

        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.documentElement.scrollTop=" + height);
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(file, new File(saveName));
            ImageUtils.cutImageLeftAndRight(saveName, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getHeight(WebDriver driver){

        JavascriptExecutor js = (JavascriptExecutor)driver;
        Object result = js.executeScript("return document.documentElement.scrollHeight");
        System.out.println(result);
        return (long)result;
    }


    public static void executeJS(WebDriver driver, String jsCode) {

        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsCode);

    }

    public static void operation(WebDriver driver, SpiderOperationEventNode eventNode) {

        //获取指定标签的内容信息
        int num = ChromDriverSpider.getIntContent(driver, eventNode.getArticleNumXPath(), "已更新 ", " 篇");
        LOGGER.info("已更新{}篇文章", num);

        //找到匹配的文章，并点击进入
        int index = eventNode.getCurNum();
        String articleXPath = String.format(eventNode.getCurArticleXPath(), index);

        //10. 找到目标处理对应的操作，这里就是截图
        //11. 找到下一个目标
        for (; index <= num; index++) {

            try {

                while(!ChromDriverSpider.find(driver, articleXPath)){
                    LOGGER.info("找不到指定的文章，模拟滚屏操作");
                    if(ChromDriverSpider.find(driver, eventNode.getErrorXPath())){
                        driver.quit();
                        throw new PigpException("被挤下线");
                    }
                    //模拟滚动条滑到底部
                    ChromDriverSpider.executeJS(driver, "document.documentElement.scrollTop=100000");
                }

                ChromDriverSpider.click(driver, articleXPath);
                articleXPath = screenShotImage(driver, eventNode, articleXPath);
            } catch (Exception e) {
                LOGGER.error("爬取过程中出现异常", e);
                driver.quit();
                throw new PigpException("被挤下线");
            }
        }

        //保存成pdf
        PDFUtils.toPdf(eventNode.getNamespace() + "/%s.png", num, eventNode.getNamespace() + ".pdf");
        driver.quit();
        System.exit(0);
    }

    private static String screenShotImage(WebDriver driver, SpiderOperationEventNode eventNode, String articleXPath) {

        String articleTitle = ChromDriverSpider.getContent(driver, eventNode.getArticleTitleXPath());
        LOGGER.info("开始爬取文章 ：{}");
        String savePath = eventNode.getNamespace() + "/"+ eventNode.getCurNum() + ".png";
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            LOGGER.info("文件:{} 已经存在：" + articleTitle);
            return String.format(eventNode.getNextArticleXPath(), eventNode.getCurNum() <= 1 ? 1 : 2);
        }

        long height = ChromDriverSpider.getHeight(driver);
        //6. 截图
        int count = 0;
        int batch = driver.manage().window().getSize().getHeight() - 128;
        while (count * batch < height - batch) {

            ChromDriverSpider.screenShot(driver, eventNode.getNamespace() + eventNode.getCurNum() + "_" + count + ".png", count * batch);
            if(ChromDriverSpider.find(driver, eventNode.getErrorXPath())){
                System.out.println("开始下一次操作：" + eventNode.getCurNum());
                driver.quit();
                throw new PigpException("被挤下线");
            }
            count++;
        }

        //合并截图
        ImageUtils.mergeImage(savePath, eventNode.getNamespace() + eventNode.getCurNum() + "_%s.png", count);
        return String.format(eventNode.getNextArticleXPath(), eventNode.getCurNum() <= 1 ? 1 : 2);
    }

    public static int getSubElementCount(WebDriver driver, String xPath, String subPath) {

        WebElement element = driver.findElement(By.xpath(xPath));
        List<WebElement> elementList = element.findElements(By.xpath(xPath+subPath));
        return elementList==null?0:elementList.size();
    }
}