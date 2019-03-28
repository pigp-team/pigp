package com.feng.spider.utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author feng
 * @date 2019/3/1 12:46
 * @since 1.0
 */
public class PDFUtils {

    public static void toPdf(String sourcePattern, int count, String saveName) {
        try {

            // 图片地址
            String imagePath = null;

            // 输出流
            FileOutputStream fos = new FileOutputStream(saveName);
            // 创建文档
            Document doc = new Document(null, 0, 0, 0, 0);
            //doc.open();
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 获取图片文件夹对象
            // 循环获取图片文件夹内的图片
            for (int i=1; i<=count; i++) {

                File file = new File(String.format(sourcePattern, i));
                if(!file.exists()){
                    continue;
                }

                imagePath = file.getAbsolutePath();
                System.out.println(file.getName());
                // 读取图片流
                img = ImageIO.read(new File(imagePath));
                // 根据图片大小设置文档大小
                doc.setPageSize(new Rectangle(img.getWidth(), img
                        .getHeight()));
                // 实例化图片
                image = Image.getInstance(imagePath);
                // 添加图片到文档
                doc.open();
                doc.add(image);
            }
            // 关闭文档
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}