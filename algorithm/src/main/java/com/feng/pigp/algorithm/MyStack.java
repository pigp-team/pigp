package com.feng.pigp.algorithm;

import java.util.ArrayDeque;
import java.util.Queue;

public class MyStack {

    Queue<Integer> queue1;
    Queue<Integer> queue2;

    /** Initialize your data structure here. */
    public MyStack() {
        queue1 = new ArrayDeque<>();
        queue2 = new ArrayDeque<>();
    }

    /** Push element x onto stack. */
    public void push(int x) {
        if(queue1.isEmpty()){
            queue2.add(x);
            return;
        }

        queue1.add(x);
    }

    /** Removes the element on top of the stack and returns that element. */
    public int pop() {
        if(queue1.isEmpty() && queue2.isEmpty()){
            return -1; //这里其实不知道返回什么
        }

        if(queue1.isEmpty()){
            while(queue2.size()!=1){
                queue1.add(queue2.remove());
            }

            return queue2.remove();
        }

        while(queue1.size()!=1){
            queue2.add(queue1.remove());
        }

        return queue1.remove();
    }

    /** Get the top element. */
    public int top() {

        if(queue1.isEmpty() && queue2.isEmpty()){
            return -1; //这里其实不知道返回什么
        }

        if(queue1.isEmpty()){
            while(queue2.size()!=1){
                queue1.add(queue2.remove());
            }

            int temp = queue2.remove();
            queue1.add(temp);
            return temp;
        }

        while(queue1.size()!=1){
            queue2.add(queue1.remove());
        }

        int temp = queue1.remove();
        queue2.add(temp);
        return temp;
    }

    /** Returns whether the stack is empty. */
    public boolean empty() {
        return queue1.isEmpty() && queue2.isEmpty();
    }
}