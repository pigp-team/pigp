package com.feng.pigp.fans.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author feng
 * @date 2019/2/27 15:33
 * @since 1.0
 */
public class ImageUtils {

    public static byte[] cutImageLeftAndRight(File file, int x, int y, int w, int h){
        try {
            BufferedImage bufImage = ImageIO.read(file);
            BufferedImage newImage = bufImage.getSubimage(x, y, w, h);
            file.delete();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(newImage, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getGrayImage(BufferedImage image) throws IOException {

        // 转灰度图像
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);
        new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(image, grayImage);
        // getData方法返回BufferedImage的raster成员对象
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(grayImage, "png", out);
        return out.toByteArray();
    }

    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\feng\\Desktop\\images.jpg");
        BufferedImage bufImage = ImageIO.read(file);
        byte[] result = getGrayImage(bufImage);
        ToolUtil.saveValidate(result, "D:\\img\\test_gray.png");
    }
}