package com.feng.pigp.util;

import com.feng.pigp.model.ListNode;

public class ListNodeUtil {

    static ListNode head = new ListNode(1);
    static ListNode second = new ListNode(2);
    static ListNode thirth = new ListNode(3);
    static ListNode fourth = new ListNode(4);
    static ListNode fiveth = new ListNode(5);


    public static ListNode getList(){

        head.next = second;
        second.next = thirth;
        thirth.next = fourth;
        fourth.next = fiveth;
        return head;
    }

    public static ListNode getycleList(){

        head.next = second;
        second.next = thirth;
        thirth.next = fourth;
        fourth.next = fiveth;

        fiveth.next = second;
        return head;
    }
}