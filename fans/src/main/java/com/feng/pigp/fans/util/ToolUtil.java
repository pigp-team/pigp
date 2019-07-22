package com.feng.pigp.fans.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Random;

/**
 * @author feng
 * @date 2019/7/13 11:33
 * @since 1.0
 */
public class ToolUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ToolUtil.class);
    private static Random RANDOM = new Random();

    /**
     * 休眠
     * @param mill
     */
    public static void  sleep(int mill){

        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            LOGGER.error("sleep error", e);
        }
    }

    /**
     * 随机函数
     * @param value
     * @return
     */
    public static boolean validateRandom(int value){

        int temp = RANDOM.nextInt(100);
        return temp<value;

    }

    /**
     * 获取图片的字节流
     * @param file
     * @return
     */
    public static byte[] getBytes(File file){

        if(file==null){
            return null;
        }

        ByteArrayOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            out = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = 0;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, b.length);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] s = out.toByteArray();
        return s;
    }

    /**
     * 将字节流保存在图片
     * @param dataBytes
     * @param savePath
     */
    public static void saveFile(byte[] dataBytes, String savePath) {

        if(dataBytes==null || StringUtils.isEmpty(savePath)){
            return;
        }

        FileOutputStream out = null;

        try{
            out = new FileOutputStream(savePath);
            out.write(dataBytes);
            out.flush();
        }catch (Exception e){
            LOGGER.debug("write file error", e);
        }finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.debug("write file close error", e);
                }
            }
        }
    }
}