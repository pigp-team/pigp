package com.feng.pigp.test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Test {

    static AtomicInteger count = new AtomicInteger(0);
    private static Random RANDOM = new Random();
    private static final int THREAD_COUNT = 1000;
    static final CountDownLatch countDown = new CountDownLatch(THREAD_COUNT);
    static int num = 0;
    static Runnable addTask = new Runnable() {
        @Override
        public void run() {

            for(int i=0; i<1000000; i++) {

                int sum = count.get();
                try {
                    Thread.sleep(RANDOM.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int end = count.incrementAndGet();
                System.out.println(sum+"==="+end);
                //count.incrementAndGet();
                num++;
            }
            countDown.countDown();
        }
    };

    public static void main(String[] args) throws InterruptedException {

        for(int i=0; i<THREAD_COUNT; i++){
            new Thread(addTask, "Thread_" + i).start();
        }
        countDown.await();
        System.out.println(count.get());
        System.out.println(num);
    }

}