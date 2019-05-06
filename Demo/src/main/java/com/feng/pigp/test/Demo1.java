package com.feng.pigp.test;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author feng
 * @date 2019/3/19 8:44
 * @since 1.0
 */
public class Demo1 {

    public static void main(String[] args) {

        System.out.println("start");
        Thread.currentThread().interrupt();
        System.out.println("thread is interrupted");

        if(Thread.interrupted()){
            System.out.println("thread has interrupted");
        }
    }

    public static boolean isInterrupted(){

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(Thread.interrupted()){
            Thread.currentThread().interrupt();
            System.out.println("thread is interrupted");
            return true;
        }

        return false;
    }
}