package com.feng.pigp.fans.util;

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

    public static void cutImageLeftAndRight(String sourceName, int x, int y, int w, int h){

        try {
            File file = new File(sourceName);
            BufferedImage bufImage = ImageIO.read(file);
            BufferedImage newImage = bufImage.getSubimage(x, y, w, h);
            file.delete();

            ImageIO.write(newImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}