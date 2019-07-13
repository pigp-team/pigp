package com.feng.pigp.fans.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author feng
 * @date 2019/7/13 11:33
 * @since 1.0
 */
public class ToolUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ToolUtil.class);

    public static void  sleep(int mill){

        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            LOGGER.error("sleep error", e);
        }
    }

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
}