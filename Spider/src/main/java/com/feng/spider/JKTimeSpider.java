package com.feng.spider;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import javax.lang.model.element.Element;
import java.io.*;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.title;

/**
 * @author feng
 * @date 2019/2/21 14:56
 * @since 1.0
 */
public class JKTimeSpider {

    public static void main(String[] args) throws IOException, InterruptedException {


        System.out.println("A\\B测试与灰度发布必知必会".replaceAll("[?*|>< :\\\\]", "_"));
        // 设置ChromeDriver的路径
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");


        ChromeOptions options = new ChromeOptions();
        //options.setBinary("C:\\Users\\feng\\AppData\\Local\\Google\\Chrome\\Application");
/*        options.addArguments("--start-maximized");
        options.addArguments("--start-fullscreen");*/
        options.setCapability("acceptSslCerts", true);
        options.setCapability("takesScreenshot", true);
        options.setCapability("cssSelectorsEnabled", true);

        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持（第二参数表明的是你的phantomjs引擎所在的路径）
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\tools\\plantom\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象
        WebDriver driver = new ChromeDriver(options);
        //driver.manage().window().maximize();
        //driver.manage().window().setSize(new Dimension(1920,1080));
        //设置隐性等待（作用于全局）
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        //打开页面
        driver.get("https://account.geekbang.org/signin?redirect=https%3A%2F%2Ftime.geekbang.org%2F");
        //查找元素
        WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[1]/div[1]/input"));
        element.sendKeys("您的用户名");
        WebElement pwdElement = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/div[2]/input"));
        pwdElement.sendKeys("您的密码");
        WebElement loginElement = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div[1]/button"));
        loginElement.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("点击专栏");

        //点击专栏
        WebElement nextElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[3]/div[2]/dl/dd[2]/a"));
        nextElement.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("获取专栏列表");
        int count = 1;
        while(true) {

            WebElement titleElement=null;
            //获取专栏列表
            try {
                titleElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[3]/ul/li[" + count + "]/div[2]/h6"));
            }catch (Exception e){
                //出发加载更多
                WebElement loadElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/button"));
                loadElement.click();
            }

            if(titleElement==null){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                System.out.println("title信息为空");
                continue;
            }
            String tile = titleElement.getText();
            System.out.println("匹配到文章：" + tile);
            if(tile.equals("机器学习40讲")){
                WebElement firstElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[3]/ul/li["+count+"]/a"));
                firstElement.click();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            count++;
        }

        System.out.println("处理文章排序---新开了一个网页" + driver.getCurrentUrl()+"==="+driver.getWindowHandles());

        Set<String> handleSet = driver.getWindowHandles();
        for(String handle : handleSet){
            if(handle.equals(driver.getWindowHandle())){
                continue;
            }
            driver.switchTo().window(handle);
        }

        //判断正序还是逆序，逆序触发点击
        WebElement sortElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/a"));
        String sort = sortElement.getText();
        if("倒序".equals(sort)){
            System.out.println("目前是倒序，修改为正序");
            sortElement.click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("获取文章列表" + driver.getCurrentUrl());
        //获取文章列表
        WebElement totalElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/span"));
        String total = totalElement.getText();
        System.out.println("更新篇数"+total);
        String count1 = total.replace("已更新 ","").replace(" 篇", "");
        int count2 = Integer.parseInt(count1);
        System.out.println("篇数：" + count2);
        WebElement articleElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[3]/div/div[1]/div/div[2]/a"));
        articleElement.click();
        sortElement.click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


       // File file = articleElement.getScreenshotAs(OutputType.FILE);
        System.out.println("获取文章内容");
        JavascriptExecutor js = (JavascriptExecutor)driver;
        count = 0;
        int height = driver.manage().window().getSize().getHeight()-40;
        while(count* height< 10000) {
            js.executeScript("document.documentElement.scrollTop="+count*height);
            File file1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file1, new File("screenshot"+count+".png"));
            count++;
        }
        Thread.sleep(2000);
        //获取到文章
        WebElement contentElement = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div/div[2]/div[2]/div[2]/div"));
        String content = contentElement.getText();
        System.out.println(content);
    }
}