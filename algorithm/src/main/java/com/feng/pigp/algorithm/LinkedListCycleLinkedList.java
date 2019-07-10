package com.feng.pigp.algorithm;

import com.feng.pigp.model.ListNode;
import com.feng.pigp.util.ListNodeUtil;

import java.util.HashSet;
import java.util.Set;

public class LinkedListCycleLinkedList {

    public static void main(String[] args) {

        LinkedListCycleLinkedList test = new LinkedListCycleLinkedList();
        boolean result = test.hasCycle2(ListNodeUtil.getList());
        System.out.println(result);
    }

    public boolean hasCycle(ListNode head) {

        Set<ListNode> set = new HashSet<ListNode>();

        ListNode cur = head;
        set.add(head);

        while(cur!=null){
            ListNode next = cur.next;
            if(set.contains(next)){
                return true;
            }
            set.add(next);
            cur = next;
        }

        return false;
    }

    public boolean hasCycle2(ListNode head) {

        if(head==null){
            return false;
        }

        ListNode quick = head.next;
        ListNode slow = head;

        while(quick!=null){

            if(slow==quick){
                return true;
            }

            if(quick.next==null){
                return false;
            }

            quick = quick.next.next;
            slow = slow.next;
        }

        return false;
    }
}