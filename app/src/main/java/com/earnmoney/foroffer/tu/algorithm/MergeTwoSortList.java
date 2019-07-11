package com.earnmoney.foroffer.tu.algorithm;

/**
 * create by tuzanhua on 2019/7/11
 */
public class MergeTwoSortList {

    public static void main(String[] args) {

        ListNode a = new ListNode();
        ListNode a1 = new ListNode();
        ListNode a2 = new ListNode();
        ListNode a3 = new ListNode();
        ListNode a4 = new ListNode();
        a.data = 1;
        a1.data = 4;
        a2.data = 6;
        a3.data = 8;
        a4.data = 10;

        a.nextNode = a1;
        a1.nextNode = a2;
        a2.nextNode = a3;
        a3.nextNode = a4;

        ListNode b = new ListNode();
        ListNode b1 = new ListNode();
        ListNode b2 = new ListNode();
        ListNode b3 = new ListNode();
        ListNode b4 = new ListNode();
        ListNode b5 = new ListNode();
        b.data = 2;
        b1.data = 5;
        b2.data = 7;
        b3.data = 9;
        b4.data = 11;
        b5.data = 15;
        b.nextNode = b1;
        b1.nextNode = b2;
        b2.nextNode = b3;
        b3.nextNode = b4;
        b4.nextNode = b5;

        mergeList(a, b);
    }

    public static void mergeList(ListNode head1, ListNode head2) {

        ListNode newNode = null;
        ListNode curh1 = head1;
        ListNode curh2 = head2;
        newNode = curh1.data <= curh2.data ? curh1 : curh2;

       if(curh1.data <= curh2.data){
           curh1 = curh1.nextNode;
       }else {
           curh2 =curh2.nextNode;
       }

       ListNode curentNode = newNode;

        while (curh1 != null && curh2 != null) {
            if (curh1.data <= curh2.data) {
                curentNode.nextNode = curh1;
                curentNode = curentNode.nextNode;
                curh1 = curh1.nextNode;
            } else {
                curentNode.nextNode = curh2;
                curentNode = curentNode.nextNode;
                curh2 = curh2.nextNode;
            }
        }

        if (curh1 != null) {
            curentNode.nextNode = curh1;
        }

        if (curh2 != null) {
            curentNode.nextNode = curh2;
        }
        print(newNode);

        ListNode node = newNode;
        node.nextNode = null;
        System.out.println("============================");
        print(newNode);

    }

    public static void print(ListNode node) {
        while (node != null) {
            System.out.print(node.data + ",");
            node = node.nextNode;
        }
    }

    public static class ListNode {
        public ListNode nextNode;
        public Integer data;
    }
}
