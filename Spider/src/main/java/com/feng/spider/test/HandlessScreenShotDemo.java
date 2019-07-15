package com.feng.spider.test;

import com.feng.spider.utils.ChromDriverSpider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author feng
 * @date 2019/7/15 14:07
 * @since 1.0
 */
public class HandlessScreenShotDemo {

    public static void main(String[] args) {

        WebDriver webDriver = ChromDriverSpider.initDriver(0,0);
        ChromDriverSpider.openUrl(webDriver, "http://www.baidu.com");
        ChromDriverSpider.screenShot(webDriver, "D://test.png");
    }
}