package com.feng.pigp.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author feng
 * @date 2019/3/19 8:44
 * @since 1.0
 */
public class Demo1 {

    private static final Lock lock = new ReentrantLock();

    static class Task implements Runnable{
        @Override
        public void run() {

            while(true){
                System.out.println("获取锁前");
                synchronized (lock) {
                    System.out.println("业务代码前");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        synchronized (lock) {
            Thread thread = new Thread(new Task());
            thread.start();
            System.out.println("线程被中断");
            thread.interrupt();
            thread.join();
        }
    }
}