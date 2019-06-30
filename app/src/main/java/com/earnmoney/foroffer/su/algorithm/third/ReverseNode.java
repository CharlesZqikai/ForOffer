package com.earnmoney.foroffer.su.algorithm.third;

/**
 * file: ReverseNode
 * author: suxianming
 * date: 2019-06-30 23:55
 * email: suxianming@fengbangleasing.com
 */
public class ReverseNode {
    public static void main(String[] args) {
        ListNode head = new ListNode();
        ListNode node1 = new ListNode();
        ListNode node2 = new ListNode();
        ListNode node3 = new ListNode();
        ListNode node4 = new ListNode();
        head.val = 0;
        node1.val = 1;
        node2.val = 2;
        node3.val = 3;
        node4.val = 4;
        head.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        ListNode reverseNode = recursiveReverseNode(head);
        while (reverseNode != null) {
            System.out.println(reverseNode.val);
            reverseNode = reverseNode.next;
        }

    }

    public static ListNode reverseNode(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = prev;
            prev = head;
            head = temp;
        }
        return prev;
    }

    public static ListNode recursiveReverseNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = recursiveReverseNode(head.next);
        head.next.next = head;
        head.next = null;
        return prev;
    }
}
