package com.earnmoney.foroffer.zhu.algorithm;

/**
 * 北京博瑞彤芸文化传播股份有限公司  版权所有
 * Copyright (c) 2019. bjbrty.com  All Rights Reserved
 * <p>
 * 作者：朱启凯  Email：qikai.zhu@sifude.com
 * 描述：链表反转
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * <p>
 * 创建时间： 2019-07-01
 **/


public class ReverseLinkList {


    static class Node {
        Node next;
        String name;

        Node(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Node head = new Node("head");
        head.next = new Node("second");
        head.next.next = new Node("third");
        head.next.next.next = new Node("fourth");
        head.next.next.next.next = new Node("fifth");
        Node newHead = reverseList(head);
        while (newHead != null) {
            System.out.println(newHead.name);
            newHead = newHead.next;
        }
    }

    public static Node reverseList(Node node) {
        Node pre = null;
        Node next = null;
        while (node != null) {
            next = node.next;
            node.next = pre;
            pre = node;
            node = next;
        }
        return pre;
    }
}
