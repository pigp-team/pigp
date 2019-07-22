package com.feng.pigp.fans.util;

import com.feng.pigp.fans.common.Common;
import com.feng.pigp.fans.exception.FansException;
import com.feng.pigp.fans.model.ProxyIp;
import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderLoginEventNode;
import com.feng.pigp.fans.service.ProxyService;
import com.feng.pigp.fans.service.VerificationCodeService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.*;

/**
 * @author feng
 * @date 2019/2/27 10:53
 * @since 1.0
 */
public class ChromDriverSpiderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromDriverSpiderUtil.class);
    public static final String DRIVER_PATH = "C:\\Users\\feng\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe";
    private static final String SAVE_PATH =  "D:\\img\\";
    private static final String BAN_IMG = "不显示任何图片";
    public static final int LOADING_WAITING_TIME = 2000;
    public static final int SMALL_WAITING_TIME = 200;
    private static final int MAX_CLICK_ERR_COUNT = 5;
    private static final int SMALL_RETRY_NUM = 2;
    private static ThreadLocal<Integer> clickCount = new ThreadLocal<>(); //memoery leak

    public static void openUrl(WebDriver driver, String url, String xpath){

        try {
            driver.get(url); //阻塞式的
        }catch (Exception e){
            LOGGER.debug("wait error", e);
            //关闭当前窗口，使用另一个窗口
        }
    }


    /**
     * 初始化浏览器，可以设置是否为headless模式，是否使用代理
     * @param width
     * @param height
     * @param useHeadless
     * @param useProxy
     * @return
     */
    public static WebDriver initDriver(int width, int height, boolean useHeadless, boolean useProxy){

        LOGGER.info("start init chrom driver");
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);// 设置ChromeDriver的路径

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");//设置浏览器最大化
        options.addArguments("--disable-gpu");

        if(useHeadless) {
            options.addArguments("--headless");
        }

        options.setCapability("acceptSslCerts", true);//设置ssl证书支持
        options.setCapability("takesScreenshot", true);//设置截屏支持
        options.setCapability("cssSelectorsEnabled", true);//设置css支持

        Proxy proxy = null;
        if(useProxy && (proxy=getProxy())!=null) {
            options.setProxy(proxy);
        }

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2); //禁用通知栏
        prefs.put("profile.default_content_settings.cookies", 2); // 禁用cookie
        options.setExperimentalOption("prefs", prefs); //设置参数

        WebDriver driver = new ChromeDriver(options);
        if(width>0 && height>0) {
            driver.manage().window().setSize(new Dimension(width, height));
        }
        LOGGER.info("finish intit chrom driver, windows width={}, height={}", width, height);
        return driver;
    }

    /**
     * 输入内容并点击
     * @param webDriver
     * @param node
     * @return
     */
    public static boolean inputAndClick(WebDriver webDriver, SpiderInputClickNode node){
        try {
            WebElement webElement = webDriver.findElement(By.xpath(node.getContentXPath()));
            webElement.sendKeys(node.getContent());
            ToolUtil.sleep(SMALL_WAITING_TIME);
            click(webDriver, node.getClickXPath());
            return true;
        } catch (Exception e) {
            LOGGER.debug("inputAndClick error", e);
        }
        return false;
    }

    /**
     * 模拟登陆，支持click与submit的登录
     * 支持验证码， 会多一次点击登录操作（因为有的验证码是点击登录之后才会出现的）
     * @param driver
     * @param eventNode
     * @return
     */
    public static boolean login(WebDriver driver, SpiderLoginEventNode eventNode){

        LOGGER.info("start login, userName={}", eventNode.getUserName());
        try {

            long start = System.currentTimeMillis();
            if (StringUtils.isNotEmpty(eventNode.getLoginURL())) {
                openUrl(driver, eventNode.getLoginURL(), eventNode.getUserNameXPath());
                LOGGER.error("open url time:{}", (System.currentTimeMillis()-start));
            }

            //查找元素
            WebElement element = driver.findElement(By.xpath(eventNode.getUserNameXPath()));
            LOGGER.error("open findElement time:{}", (System.currentTimeMillis()-start));
            element.clear();
            element.sendKeys(eventNode.getUserName());
            WebElement pwdElement = driver.findElement(By.xpath(eventNode.getPasswdXPath()));
            pwdElement.sendKeys(eventNode.getPasswd());
            WebElement loginButtonElement = driver.findElement(By.xpath(eventNode.getLoginXPath()));
            click(driver, loginButtonElement, eventNode.isSubmit());

            if(StringUtils.isEmpty(eventNode.getValidateCodeXPath())){
                return true;
            }

            WebElement validate =driver.findElement(By.xpath(eventNode.getValidateCodeXPath()));

            for (int i=0; i<SMALL_RETRY_NUM; i++) {

                String code = parseValidate(driver, validate);
                //将原图片名称修改为正确解析的名称
                if (StringUtils.isEmpty(code)) {
                    LOGGER.error("code error");
                    click(driver, eventNode.getValidateCodeXPath());
                    continue;
                }

                WebElement codeInput = driver.findElement(By.xpath(eventNode.getValidateCodeInputXPath()));
                codeInput.sendKeys(code);

                click(driver, loginButtonElement, eventNode.isSubmit());
                ToolUtil.sleep(LOADING_WAITING_TIME);
                return true;
            }

            click(driver, loginButtonElement, eventNode.isSubmit());
            LOGGER.info("{} : login success");
            return true;
        } catch (Exception e) {
            LOGGER.error("login error", e);
        }

        return false;
    }

    /**
     * 截屏幕后抠出验证码，并解析验证码
     * @param driver
     * @param validate
     * @return
     */
    private static String parseValidate(WebDriver driver, WebElement validate) {

        byte[] dataBytes = screenShot(driver, validate.getRect().getX(), validate.getRect().getY(),
                Integer.parseInt(validate.getAttribute("width")), Integer.parseInt(validate.getAttribute("height")));

        if(dataBytes==null){
            return null;
        }

        return VerificationCodeService.distinguishCode(dataBytes);
    }

    /**
     * 滑动滚动条到浏览器底部
     * @param driver
     */
    public static void scrollToBottom(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("document.documentElement.scrollTop=100000");
    }

    /**
     * 支持点击/提交（input）
     * @param driver
     * @param element
     * @param isSubmit
     * @return
     */
    public static boolean click(WebDriver driver, WebElement element, boolean isSubmit){

        try {
            boolean isDisplay = element.isDisplayed();
            if (!isDisplay) {
                //滚动
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView()", element);
            }

            if(isSubmit){
                element.submit();
            }else{
                element.click();
            }
            //休眠2s等待页面加载
            Thread.sleep(LOADING_WAITING_TIME);
            return true;
        }catch (Exception e){
            LOGGER.debug("click error", e);
        }

        return false;
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
            clickElement.click();
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
            clickElement.click();
            //休眠2s等待页面加载
            Thread.sleep(LOADING_WAITING_TIME);
            return true;
        }catch (Exception e){
            LOGGER.debug("click error ", e);
        }

        return false;
    }


    /**
     * 通过TakesScreenshot进行截屏
     * 通过ImageUtils进行抠图
     * @param driver
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
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

    /**
     * 获取指定标签的值
     * 存在key：获取属性值
     * 不存在key，获取text值
     * @param driver
     * @param xPath
     * @param key
     * @return
     */
    public static String getContent(WebDriver driver, String xPath, String key) {

        try {
            WebElement element = driver.findElement(By.xpath(xPath));
            if(StringUtils.isNotEmpty(key)){
                return element.getAttribute(key);
            }

            boolean isDsiplayed = element.isDisplayed();
            if (isDsiplayed) {
                return element.getText();
            }

            return element.getAttribute("innerHTML");
        } catch (Exception e) {
            LOGGER.debug("getContent error", e);
        }

        return null;
    }

    /**
     * 切换到另外一个窗口（前提是只有两个窗口）
     * @param driver
     */
    public static void switchOtherWindows(WebDriver driver) {

        String curWindown = driver.getWindowHandle();
        Set<String> handleSet = driver.getWindowHandles();
        if(handleSet.size()!=2){
            return;
        }

        for(String handle : handleSet){
            if(handle.equals(curWindown)){
                continue;
            }

            driver.switchTo().window(handle);
            return;
        }
    }

    /**
     * 获取子元素的个数
     * @param driver
     * @param xPath
     * @param subPath
     * @return
     */
    public static int getSubElementCount(WebDriver driver, String xPath, String subPath) {

        WebElement element = driver.findElement(By.xpath(xPath));
        List<WebElement> elementList = element.findElements(By.xpath(xPath+subPath));
        return elementList==null?0:elementList.size();
    }

    /**
     * 判断是否存在弹窗
     * @param driver
     * @return
     */
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
     * 关闭弹窗
     * @param webDriver
     */
    public static void closeAlert(WebDriver webDriver) {

        webDriver.switchTo().alert().accept();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新窗口
     * @param webDriver
     */
    public static void refresh(WebDriver webDriver) {
        webDriver.navigate().refresh();
    }

    /**
     * 打开一个新的窗口，driver没有提供原生方法，需要使用js
     * @param webDriver
     * @param url
     */
    public static void openNewWindows(WebDriver webDriver, String url) {

        Set<String> windowSet = webDriver.getWindowHandles();

        if(windowSet==null){
            return;
        }

        if(windowSet.size()==1 &&  StringUtils.isEmpty(webDriver.getCurrentUrl())){
            openUrl(webDriver, url, null);
            return ;
        }
        JavascriptExecutor js = (JavascriptExecutor)webDriver;
        js.executeScript("window.open(\""+url+"\")");
    }

    /**
     * 禁用/开启 加载图片设置， chrom设置页面
     * @param driver
     * @param isBan
     * @return
     */
    public static boolean chromImageSetting(WebDriver driver, boolean isBan) {

        try {

            openUrl(driver, Common.SETTING_IMAGE_URL, Common.SETTING_IMAGE_TXT);
            WebElement element = driver.findElement(By.xpath("/html/body/settings-ui"));

            List<String> list = Lists.newArrayList("div[id=container]>settings-main",
                    "settings-basic-page",
                    "div[id=advancedPage]>settings-section>settings-privacy-page",
                    "settings-animated-pages>settings-subpage>category-default-setting",
                    "settings-toggle-button",
                    "div>div>div");

            for (String str : list) {
                WebElement root = getShadowRoot(driver, element);
                element = root.findElement(By.cssSelector(str));
            }

            String content = element.getText();

            if (BAN_IMG.equals(content) && !isBan) {
                element.click();
            }

            if(!BAN_IMG.equals(content) && isBan){
                element.click();
            }

            return true;
        }catch (Exception e){
            LOGGER.debug("chrom setting error", e);
        }

        return false;
    }

    /**
     * 存在shadowRoot元素的页面，xpath使用不了（曲线救国）
     * @param driver
     * @param element
     * @return
     */
    public static WebElement getShadowRoot(WebDriver driver, WebElement element){
        return (WebElement) ((JavascriptExecutor)driver).executeScript("return arguments[0].shadowRoot", element);
    }

    /**
     * 获取代理对象
     * @return
     */
    public static Proxy getProxy() {
        ProxyIp ip = ProxyService.queryIp();
        if(StringUtils.isNotEmpty(ip.getIp()) && ip.getPort()>0){
            LOGGER.info("use proxy :{}-{}", ip.getIp(), ip.getPort());
            Map<String, String> map = Maps.newHashMap();
            map.put("httpProxy", ip.getIp()+":"+ip.getPort());
            Proxy proxy = new Proxy(map);
            return proxy;
        }
        return null;
    }
}