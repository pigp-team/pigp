package com.feng.spider.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author feng
 * @date 2019/2/27 15:33
 * @since 1.0
 */
public class ImageUtils {

    public static void cutImageLeftAndRight(String sourceName, int size){

        try {
            File file = new File(sourceName);
            BufferedImage bufImage = ImageIO.read(file);
            BufferedImage newImage = bufImage.getSubimage(size, 0, bufImage.getWidth()-size-size, bufImage.getHeight());
            file.delete();

            ImageIO.write(newImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并按照sourceName规则的图片为一张图片
     * @param saveName
     * @param sourceName
     * @param count
     */
    public static void mergeImage(String saveName, String sourceName, int count) {

        try {

            File file = new File(String.format(sourceName, 0));
            Image imageFirst = ImageIO.read(file);
            int[] imageArrayOne = getRGBInfo(imageFirst);
            int width = imageFirst.getWidth(null);
            int height = imageFirst.getHeight(null);
            file.delete();
            BufferedImage imageNew = new BufferedImage(width,
                    imageFirst.getHeight(null) * count, BufferedImage.TYPE_INT_RGB);

            imageNew.setRGB(0, 0, width, height, imageArrayOne, 0, width);

            for(int i=1; i<count; i++){
                file = new File(String.format(sourceName, i));
                int[] temp = getRGBInfo(ImageIO.read(file));
                file.delete();
                imageNew.setRGB(0, height*i, width, height, temp, 0, width);
            }

            File outFile = new File(saveName);
            // 写图片
            ImageIO.write(imageNew, "png", outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int[] getRGBInfo(Image image) throws IOException {

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage bufferedImageOne = bufferedImageOne = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        bufferedImageOne.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
                null);
        int[] imageArrayOne = new int[width * height];// 从图片中读取RGB
        imageArrayOne = bufferedImageOne.getRGB(0, 0, width, height, imageArrayOne, 0, width);
        return imageArrayOne;
    }
}