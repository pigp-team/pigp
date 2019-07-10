package com.feng.pigp.test;

import mjson.Json;

/**
 * @author feng
 * @date 2019/7/4 13:31
 * @since 1.0
 */
public class MJsonDemo {

    public static void main(String[] args) {

        String str = "{\"name\":null, \"age\":%s}";

        for(int i=0; i<10; i++){
            Json.read(String.format(str, i));
        }

        System.out.println("finish");
    }
}