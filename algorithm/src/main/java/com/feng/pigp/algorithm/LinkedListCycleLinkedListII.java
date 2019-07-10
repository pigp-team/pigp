package com.feng.pigp.algorithm;

import com.feng.pigp.model.ListNode;
import com.feng.pigp.util.ListNodeUtil;

import java.util.HashSet;
import java.util.Set;

public class LinkedListCycleLinkedListII {

    public static void main(String[] args) {

        LinkedListCycleLinkedListII test = new LinkedListCycleLinkedListII();
        ListNode result = test.detectCycle(ListNodeUtil.getycleList());
        System.out.println(result);
    }

    public ListNode detectCycle(ListNode head) {

        Set<ListNode> set = new HashSet<ListNode>();

        ListNode cur = head;
        set.add(head);

        while(cur!=null){
            ListNode next = cur.next;
            if(set.contains(next)){
                return next;
            }
            set.add(next);
            cur = next;
        }

        return null;
    }
}