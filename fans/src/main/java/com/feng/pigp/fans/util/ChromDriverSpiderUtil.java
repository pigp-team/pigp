package com.feng.pigp.fans.util;

import com.feng.pigp.fans.model.chrom.SpiderInputClickNode;
import com.feng.pigp.fans.model.chrom.SpiderLoginEventNode;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author feng
 * @date 2019/2/27 10:53
 * @since 1.0
 */
public class ChromDriverSpiderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChromDriverSpiderUtil.class);
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

    public static void inputAndClick(WebDriver webDriver, SpiderInputClickNode node) throws InterruptedException {
        WebElement webElement = webDriver.findElement(By.xpath(node.getContentXPath()));
        webElement.sendKeys(node.getContent());
        Thread.sleep(1000);
        ChromDriverSpiderUtil.click(webDriver, node.getClickXPath());
    }

    /**
     * 模拟登陆
     * @param driver
     * @param eventNode
     */
    public static void login(WebDriver driver, SpiderLoginEventNode eventNode) throws InterruptedException {

        LOGGER.info("start login, userName={}", eventNode.getUserName());
        //打开页面
        driver.get(eventNode.getLoginURL());
        Thread.sleep(10000);
        //查找元素
        WebElement element = driver.findElement(By.xpath(eventNode.getUserNameXPath()));
        element.sendKeys(eventNode.getUserName());
        Thread.sleep(1000);
        WebElement pwdElement = driver.findElement(By.xpath(eventNode.getPasswdXPath()));
        pwdElement.sendKeys(eventNode.getPasswd());
        Thread.sleep(1000);
        click(driver, eventNode.getLoginXPath());
        LOGGER.info("{} : login success");
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

    public static int getSubElementCount(WebDriver driver, String xPath, String subPath) {

        WebElement element = driver.findElement(By.xpath(xPath));
        List<WebElement> elementList = element.findElements(By.xpath(xPath+subPath));
        return elementList==null?0:elementList.size();
    }
}