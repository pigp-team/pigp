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

    static final AtomicLong atomicLong = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {

        while (true) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "_" + atomicLong.getAndAdd(1));
                    Thread.sleep(1000000000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}