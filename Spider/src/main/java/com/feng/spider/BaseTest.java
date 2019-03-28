package com.feng.spider;

import com.feng.spider.utils.PDFUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author feng
 * @date 2019/2/22 11:42
 * @since 1.0
 */
public class BaseTest {

    private static final String TITLE = "深入拆解 Java 虚拟机";
    public static void main(String[] args) {

        PDFUtils.toPdf(TITLE+"/%s.png",  19, TITLE.replaceAll(" ", "") + ".pdf");
    }
}