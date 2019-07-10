package com.feng.pigp.algorithm;

import com.feng.pigp.model.ListNode;
import com.feng.pigp.util.ListNodeUtil;

public class SwapNodesInKGroupLinkedList {

    public static void main(String[] args) {

        SwapNodesInKGroupLinkedList test = new SwapNodesInKGroupLinkedList();
        ListNode result = test.swapPairs(ListNodeUtil.getList(), 5);
        System.out.println(result);
    }


    public ListNode swapPairs(ListNode head, int k) {

        if(head == null){
            return null;
        }

        if(k<=1){
            return head;
        }

        ListNode result = new ListNode(0);
        result.next = head;
        ListNode last = result;
        ListNode cur = last.next;

        int count = 1;
        while(cur!=null){

            if(count >= k){

                //反转 last->next到next之间的节点
                ListNode temp = last.next.next;
                ListNode next = cur.next;
                ListNode newLast = last.next;
                while(temp!=next) {

                    ListNode tempNext = temp.next;
                    temp.next = last.next;
                    last.next = temp;

                    temp = tempNext;
                }

                newLast.next = next;
                cur = next;
                count = 1;
                last = newLast;

            }

            if(cur==null){
                break;
            }
            count++;
            cur = cur.next;
        }

        return result.next;
    }

}