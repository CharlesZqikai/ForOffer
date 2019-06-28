package com.earnmoney.foroffer.tu.algorithm;

/**
 * create by tuzanhua on 2019/6/28
 * <p>
 * a->b->c->d->e
 * <p>
 * 链表逆序实现方案 : 1. recursion 递归
 * 2. while 循环
 * 无论哪种形式都需要搞清楚所需要退出的边界条件.
 * 下面分析下 while 循环的思路:
 * 需要让 headNode.next 指向null  headNode.next  指向 headNode,想到这里应该想到了必须需要利用临时变量来做中转交换否则链表就会中断\
 * 那么需要几个变量呢 ? while 循环跳出的条件是什么呢?
 * 我们从while 循环的第一次开始分析
 * 需要的变量 : preNode 并且 preNode = null ,在第一次结束的时候preNode = currentNode
 *  好吧上面都写出了 CurrentNode 这也是一个变量
 *
 * 第一次 : null<- a   a<- b  如何实现? 利用变量
 *        preNode = null;
 *        currentNode = a;
 *
 *        tempNode = currentNode.next; tempNode = b;
 *
 *        currentNode.next = preNode; // a.next = null;
 *        preNode = currentNode;     // preNode = a;
 *        currentNode = b;
 *
 *
 */
public class ReverseList {

    public static void main(String[] args) {
        Node<String> a = new Node<>("a");
        Node<String> b = new Node<>("b");
        Node<String> c = new Node<>("c");
        Node<String> d = new Node<>("d");
        Node<String> e = new Node<>("e");
        a.next = b;
        b.next = c;
        c.next = d;
        d.next = e;
        printList(a);
        System.out.println("================================");
        Node<String> stringNode = reverseListFunc(a);
        printList(stringNode);
    }

    private static Node<String> reverseListFunc(Node<String> headNode) {
        if (headNode == null) {
            return null;
        }
        if (headNode.next == null) {
            return headNode;
        }

        // 辅助变量 preNode
        Node preNode = null;
        // 当前node
        Node currentNode = headNode;

        // 当前node 每次 指向nextNode
        while (currentNode != null) {
            // 记录下当前 Node.next Node 否则会断链(需要将当前node 指向 nextNode)
            Node tempNode = currentNode.next;
            // 当前node next 指针指向preNode 第一次为null
            currentNode.next = preNode;
            // 然后把当前node 赋值给 preNode  这样就完成了 当前指针后移
            preNode = currentNode;
            // 当前 node 指向下一个指针
            currentNode = tempNode;
        }
        // 这里想一下为什么要return preNode ? 仔细想好好想
        return preNode;
    }

    private static void printList(Node<String> a) {
        while (a != null) {
            System.out.println(a.data + ",");
            a = a.next;
        }
    }

    public static class Node<T> {
        public T data;
        public Node next;

        public Node(T data) {
            this.data = data;
        }
    }
}
