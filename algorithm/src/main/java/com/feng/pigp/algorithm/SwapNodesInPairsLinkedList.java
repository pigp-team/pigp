package com.feng.pigp.algorithm;

import com.feng.pigp.model.ListNode;
import com.feng.pigp.util.ListNodeUtil;

public class SwapNodesInPairsLinkedList {

    public static void main(String[] args) {

        SwapNodesInPairsLinkedList test = new SwapNodesInPairsLinkedList();
        ListNode result = test.swapPairs2(ListNodeUtil.getList());
        System.out.println(result);
    }

    public ListNode swapPairs1(ListNode head) {

        if(head == null){
            return null;
        }

        ListNode first = head;
        ListNode second = head.next;

        while(first!=null && second!=null){

            int value = first.val;
            first.val = second.val;
            second.val = value;

            first = second.next;
            if(first==null){
                break;
            }
            second = first.next;
        }
        return head;
    }

    public ListNode swapPairs2(ListNode head) {

        if(head == null){
            return null;
        }

        ListNode first = head;
        ListNode second = head.next;
        ListNode result = new ListNode(0);
        ListNode last = result;

        while(first!=null && second!=null){

            first.next = second.next;
            second.next = first;
            last.next = second;


            last = first;
            first = first.next;
            if(first==null){
                break;
            }
            second = first.next;

        }
        return result.next;
    }

}