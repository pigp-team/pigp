package com.feng.pigp.nolock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feng
 * @date 2019/6/14 15:53
 * @since 1.0
 */
public class Test {

    private static final int PRODUCE_NUMBER = 100;
    private static final int BATCH = 100000;
    private static final Random RANDOM = new Random();

    private static CountDownLatch startCountDownLatch = new CountDownLatch(1);
    private static CountDownLatch countDownLatch = new CountDownLatch(PRODUCE_NUMBER);
    private static MpscLinkedQueue<Integer> mpscLinkedQueue = new MpscLinkedQueue<>();
    private static LinkedBlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>();
    private static AtomicInteger consumerNum = new AtomicInteger(0);

    private static final Runnable produceRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                startCountDownLatch.await();
                for(int i=0; i<BATCH; i++) {
                    int count = RANDOM.nextInt();
                    long start = System.currentTimeMillis();
                    //mpscLinkedQueue.offer(count);
                    linkedBlockingQueue.put(count);
                    long time = System.currentTimeMillis()-start;
                    if(time > 0) {
                        System.out.println(Thread.currentThread().getName() + ":" + i + ":" + time);
                    }

                }
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private static final Runnable consumerRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                startCountDownLatch.await();
                while(true){

                    //mpscLinkedQueue.poll();
                    linkedBlockingQueue.take();
                    consumerNum.incrementAndGet();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {


        for(int i=0; i<PRODUCE_NUMBER; i++){
            new  Thread(produceRunnable).start();
        }

        new Thread(consumerRunnable).start();

        long start = System.currentTimeMillis();
        startCountDownLatch.countDown();
        countDownLatch.await();
        System.out.println("time is " + (System.currentTimeMillis()-start));
        System.out.println("cosumer num is " + consumerNum.get());
    }
}