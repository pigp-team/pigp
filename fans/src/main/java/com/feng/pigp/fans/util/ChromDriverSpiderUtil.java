package com.feng.pigp.fans.util;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderLoginEventNode;
import com.feng.pigp.fans.model.chrom.SpiderMatchClickNode;
import com.feng.pigp.fans.service.VerificationCodeService;
import com.github.rjeschke.txtmark.Run;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.TimeoutException;

/**
 * @author feng
 * @date 2019/2/27 10:53
 * @since 1.0
 */
public class ChromDriverSpiderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromDriverSpiderUtil.class);
    public static final String DRIVER_PATH = "C:\\Users\\feng\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";
    private static final String SAVE_PATH =  "D:\\img\\";
    public static final int LOADING_WAITING_TIME = 2000;
    private static final int MAX_CLICK_ERR_COUNT = 5;
    private static ThreadLocal<Integer> clickCount = new ThreadLocal<>(); //memoery leak

    public static boolean openUrl(WebDriver driver, String url, String xpath){


        try {
            driver.get(url); //阻塞式的
            WebDriverWait wait = new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            return true;
        }catch (Exception e){
            LOGGER.debug("wait error", e);
        }
        return false;
    }


    public static void internalClick(WebElement element){

        try{
            element.click();
            clickCount.set(0);
        }catch (Exception e){
            LOGGER.debug("click error");

            if(clickCount.get()==null){
                clickCount.set(0);
            }

            if(clickCount.get()+1 > MAX_CLICK_ERR_COUNT){
                clickCount.set(0);
                throw new FansException("click error");
            }

            clickCount.set(clickCount.get()+1);
        }
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
        options.addArguments("--disable-gpu");
        //options.addArguments("--start-fullscreen");
        //options.addArguments("--headless");
        //设置ssl证书支持
        options.setCapability("acceptSslCerts", true);
        //设置截屏支持
        options.setCapability("takesScreenshot", true);
        //设置css支持
        options.setCapability("cssSelectorsEnabled", true);

        /*ProxyIp ip = ProxyService.queryIp();
        if(StringUtils.isNotEmpty(ip.getIp()) && ip.getPort()>0){
            LOGGER.info("use proxy :{}-{}", ip.getIp(), ip.getPort());*/
            Map<String, String> map = Maps.newHashMap();
            //map.put("httpProxy", ip.getIp()+":"+ip.getPort());
            map.put("httpProxy", "58.218.200.223:30198");
            Proxy proxy = new Proxy(map);
            //options.setProxy(proxy);
        /*}*/

        Map<String, Object> prefs = new HashMap<String, Object>();

        // 设置提醒的设置，2表示block
        //prefs.put("profile.managed_default_content_settings.images", 2);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_settings.cookies", 2);
        //prefs.put("--media-cache-size", 1024);
        options.setExperimentalOption("prefs", prefs);

        //创建driver对象
        WebDriver driver = new ChromeDriver(options);
        //设置隐性等待（作用于全局）
        //driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        //810*800
        if(width>0 && height>0) {
            driver.manage().window().setSize(new Dimension(width, height));
        }
        LOGGER.info("finish intit chrom driver, windows width={}, height={}", width, height);
        return driver;
    }

    public static boolean inputAndClick(WebDriver webDriver, SpiderInputClickNode node){
        try {
            WebElement webElement = webDriver.findElement(By.xpath(node.getContentXPath()));
            webElement.sendKeys(node.getContent());
            Thread.sleep(500);
            click(webDriver, node.getClickXPath());
            return true;
        } catch (Exception e) {
            LOGGER.debug("inputAndClick error", e);
        }
        return false;
    }

    /**
     * 模拟登陆
     * @param driver
     * @param eventNode
     */
    public static boolean login(WebDriver driver, SpiderLoginEventNode eventNode){

        try {
            LOGGER.info("start login, userName={}", eventNode.getUserName());
            //打开页面
            long start = System.currentTimeMillis();
            if (StringUtils.isNotEmpty(eventNode.getLoginURL())) {
                boolean result = openUrl(driver, eventNode.getLoginURL(), eventNode.getUserNameXPath());
                LOGGER.error("open url time:{}", (System.currentTimeMillis()-start));
            }

            //查找元素
            WebElement element = driver.findElement(By.xpath(eventNode.getUserNameXPath()));
            LOGGER.error("open findElement time:{}", (System.currentTimeMillis()-start));
            element.clear();
            element.sendKeys(eventNode.getUserName());
            WebElement pwdElement = driver.findElement(By.xpath(eventNode.getPasswdXPath()));
            pwdElement.sendKeys(eventNode.getPasswd());

            WebElement submitElement = driver.findElement(By.xpath(eventNode.getLoginXPath()));
            submitElement.submit();
            ToolUtil.sleep(2000);

            wait(driver, eventNode.getValidateCodeXPath());
            WebElement validate =driver.findElement(By.xpath(eventNode.getValidateCodeXPath()));

            for (int i = 0; i < 2; i++) {

                byte[] dataBytes = screenShot(driver, validate.getRect().getX(), validate.getRect().getY(),
                        Integer.parseInt(validate.getAttribute("width")), Integer.parseInt(validate.getAttribute("height")));

                if(dataBytes==null){
                    continue;
                }
                //获取解析的结果
                String code = VerificationCodeService.distinguishCode(dataBytes);
                //将原图片名称修改为正确解析的名称
                if (StringUtils.isEmpty(code)) {
                    LOGGER.error("code error");
                    click(driver, eventNode.getValidateCodeXPath());
                    continue;
                }

                WebElement codeInput = driver.findElement(By.xpath(eventNode.getValidateCodeInputXPath()));
                codeInput.sendKeys(code);

                //click(driver, eventNode.getLoginXPath());
                submitElement.submit();
                ToolUtil.sleep(2000);

                String savePath = SAVE_PATH + code + ".png";
                ToolUtil.saveValidate(dataBytes, savePath);
                return true;
            }

            //click(driver, eventNode.getLoginXPath());
            submitElement.submit();
            LOGGER.info("{} : login success");
            return true;
        } catch (Exception e) {
            LOGGER.error("login error", e);
        }

        return false;
    }



    public static boolean hasAlert(WebDriver driver){
        try {
            driver.switchTo().alert();
            return true;
        }catch (NoAlertPresentException e){
            //noting todo
        }

        return false;
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

    public static void scrollToBottom(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.documentElement.scrollTop=100000");
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
    public static boolean click(WebDriver driver, String xPath){

        try {
            WebElement clickElement = driver.findElement(By.xpath(xPath));
            boolean isDisplay = clickElement.isDisplayed();
            if (!isDisplay) {
                //滚动
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView()", clickElement);
            }
            internalClick(clickElement);
            //休眠2s等待页面加载
            Thread.sleep(LOADING_WAITING_TIME);
            return true;
        }catch (Exception e){
            LOGGER.debug("click error", e);
        }

        return false;
    }


    /**
     * key存在从key中取值与content比较
     * key不存在，content存在，直接getText取值与content比较
     * key，content都不存在，直接点击
     * @param driver
     * @param key
     * @param content
     * @param xPath
     */
    public static boolean clickOrNot(WebDriver driver, String key, String content, String xPath){

        try {
            WebElement clickElement = driver.findElement(By.xpath(xPath));

            if (StringUtils.isNotEmpty(key)) {
                String value = clickElement.getAttribute(key);
                if (!content.equals(value)) {
                    LOGGER.info("no match no click {}-{}", content, value);
                    return false;
                }
            }

            if (StringUtils.isEmpty(key) && StringUtils.isNotEmpty(content)) {
                String value = clickElement.getText();
                if (!content.equals(value)) {
                    LOGGER.info("no match no click {}-{}", content, value);
                    return false;
                }
            }

            boolean isDisplay = clickElement.isDisplayed();
            if (!isDisplay) {
                //滚动
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView()", clickElement);
            }
            internalClick(clickElement);
            //休眠2s等待页面加载
            Thread.sleep(LOADING_WAITING_TIME);
            return true;
        }catch (Exception e){
            LOGGER.debug("click error ", e);
        }

        return false;
    }

    public static boolean find(WebDriver driver, String xPath){

        try {
            WebElement element = driver.findElement(By.xpath(xPath));
            return true;
        }catch (Exception e){
            LOGGER.debug("find error", e);
        }

        return false;
    }

    public static byte[] screenShot(WebDriver driver, int x, int y, int w, int h){

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            //FileUtils.copyFile(file, new File(saveName));
            return ImageUtils.cutImageLeftAndRight(file, x, y, w, h);
        } catch (Exception e) {
            LOGGER.debug("screen shot error", e);
        }

        return null;
    }

    public static int getIntContent(WebDriver driver, String xPath, String startChar, String endChar){

        String str = getContent(driver, xPath);
        int start = str.indexOf(startChar);
        String temp = str.substring(start+1, str.indexOf(endChar, start+1));
        return Integer.parseInt(temp);
    }

    public static String getContentWithKey(WebDriver driver, String xPath, String key) {

        try {
            WebElement element = driver.findElement(By.xpath(xPath));
            if(StringUtils.isNotEmpty(key)){
                return element.getAttribute(key);
            }

            return element.getText();
        } catch (Exception e) {
            LOGGER.debug("getContent error", e);
        }

        return null;
    }

    public static String getContent(WebDriver driver, String xPath) {

        try {
            Thread.sleep(1000);
            WebElement element = driver.findElement(By.xpath(xPath));
            boolean isDsiplayed = element.isDisplayed();
            System.out.println("element is displayed : " + isDsiplayed);
            if (isDsiplayed) {
                return element.getText();
            }

            return element.getAttribute("innerHTML");
        } catch (Exception e) {
            LOGGER.debug("getContent error", e);
        }

        return null;
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


    public static long getHeight(WebDriver driver){

        JavascriptExecutor js = (JavascriptExecutor)driver;
        Object result = js.executeScript("return document.documentElement.scrollHeight");
        return (long)result;
    }


    public static void executeJS(WebDriver driver, String jsCode) {

        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsCode);

    }

    public static int getSubElementCount(WebDriver driver, String xPath, String subPath) {

        WebElement element = driver.findElement(By.xpath(xPath));
        List<WebElement> elementList = element.findElements(By.xpath(xPath+subPath));
        return elementList==null?0:elementList.size();
    }

    public static void matchAndClickWithOutIndex(WebDriver driver, SpiderMatchClickNode node) {

        try {
            String content = getContent(driver, node.getContentXPath());
            if(StringUtils.isNotEmpty(content) && content.contains(node.getMatchContent())){
                clickOrNot(driver, node.getClickKey(), node.getClickContent(),node.getClickXPath());
            }
        }catch (Exception e){
            LOGGER.debug("matchAndClick error", e);
        }
    }

    public static int matchAndClick(WebDriver driver, SpiderMatchClickNode node) {

        for(int i=node.getStratIndex(); i<=node.getEndIndex(); i++){

            try {
                String content = getContent(driver, String.format(node.getContentXPath(), i));
                if(StringUtils.isNotEmpty(content) && content.contains(node.getMatchContent())){
                    clickOrNot(driver, node.getClickKey(), node.getClickContent(),String.format(node.getClickXPath(), i));
                    return i;
                }
            }catch (Exception e){
                LOGGER.debug("matchAndClick error", e);
            }
        }

        return -1;
    }

    public static void closeAlert(WebDriver webDriver) {

        webDriver.switchTo().alert().accept();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void refresh(WebDriver webDriver) {

        webDriver.navigate().refresh();
    }

    public static void wait(WebDriver webDriver, String xPath) {

        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 5);
            wait.until(ExpectedConditions.elementToBeSelected(By.xpath(xPath)));
        }catch (Exception e){
            LOGGER.debug("wait error", e);
        }
    }
}