package com.feng.pigp.algorithm;

import com.feng.pigp.model.ListNode;
import com.feng.pigp.util.ListNodeUtil;

public class ReverseLinkedList {

    public static void main(String[] args) {

        ReverseLinkedList linkedList = new ReverseLinkedList();
        ListNode result = linkedList.reverseList(ListNodeUtil.getList());
        System.out.println(result);
    }

    public ListNode reverseList(ListNode head) {

        if(head==null || head.next==null){
            return head;
        }

        ListNode result = new ListNode(0);
        while(head!=null){

            ListNode next = head.next;
            head.next = result.next;
            result.next = head;
            head = next;
        }

        return result.next;

    }
}