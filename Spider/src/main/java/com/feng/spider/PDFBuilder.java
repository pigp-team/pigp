package com.feng.spider;

import com.lowagie.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author feng
 * @date 2019/2/22 16:14
 * @since 1.0
 */
public class PDFBuilder {

    public static void main(String[] args) throws Exception{

        String inputFile = "C:\\Users\\feng\\Desktop\\129\\aaa.txt";

        String url = new File(inputFile).toURI().toURL().toString();

        String outputFile = "my.pdf";

        OutputStream os = new FileOutputStream(outputFile);

        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocument(url);


        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("C:/Windows/Fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // 解决中文支持问题
       // ITextFontResolver fontResolver = renderer.getFontResolver();
        //fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        // 解决图片的相对路径问题
        //renderer.getSharedContext().setBaseURL("file:/D:/Work/Demo2do/Yoda/branch/Yoda%20-%20All/conf/template/");

        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }
}