package com.feng.pigp.fans.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
}