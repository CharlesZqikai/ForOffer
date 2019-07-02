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
        //记录head节点的前面一个节点,第一次为空
        ListNode prev = null;
        while (head != null) {
            //记录head节点的第二个节点,不然会断链
            ListNode temp = head.next;
            //head节点的next指向 它的前一个节点,第一次相当于断开head节点与第二个节点的联系,之后相当于逆序
            head.next = prev;
            //记录当前的head节点,也就是下次循环的head节点的前一个节点
            prev = head;
            //head节点后移
            head = temp;
        }
        //prev 肯定是最后一个节点
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
