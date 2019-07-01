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
 * 好吧上面都写出了 CurrentNode 这也是一个变量
 * <p>
 * 第一次 : null<- a   a<- b  如何实现? 利用变量
 * preNode = null;
 * currentNode = a;
 * <p>
 * tempNode = currentNode.next; tempNode = b;
 * <p>
 * currentNode.next = preNode; // a.next = null;
 * preNode = currentNode;     // preNode = a;
 * currentNode = b;
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


        System.out.println("recursion ================================");
        Node<String> a1 = new Node<>("a");
        Node<String> b1 = new Node<>("b");
        Node<String> c1 = new Node<>("c");
        Node<String> d1 = new Node<>("d");
        Node<String> e1 = new Node<>("e");

        a1.next = b1;
        b1.next = c1;
        c1.next = d1;
        d1.next = e1;
        printList(recursion(a1));
    }


    private static Node<String> recursion(Node<String> headNode) {
        if (headNode == null || headNode.next == null) {
            return headNode;
        }

        Node<String> pre = recursion(headNode.next);
        headNode.next.next = headNode;
        headNode.next = null;
        return pre;
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

    private static void printList1(Node<String> a) {
        while (a != null) {
            System.out.print(a.data + ",");
            a = a.next;
        }
        System.out.println("<====>");
    }

    public static class Node<T> {
        public T data;
        public Node next;

        public Node(T data) {
            this.data = data;
        }
    }


//    recursion(a) {
//
//        recursion(b) {
//
//            recursion(c) {
//
//                recursion(d) {
//
//                    recursion(e) {
//                        return e;  // e 弹栈
//                    }
//
//                    Node<String> pre = recursion(headNode.next); // pre = e；  head = d;
//                    headNode.next.next = headNode;               // d.next.next = d ; e.next = d;
//                    headNode.next = null;                        // d.next = null;
//                    return pre;                                  // pre = e   弹栈
//
//                }
//
//                Node<String> pre = recursion(headNode.next); // pre = e - > d；    head = c;
//                headNode.next.next = headNode;               // c.next.next = c ; d.next = c;
//                headNode.next = null;                        // c.next = null;
//                return pre;                                  // pre = e - >d ->c   弹栈
//
//            }
//
//            Node<String> pre = recursion(headNode.next); // pre = e - > d - >c；    head = b;
//            headNode.next.next = headNode;               // b.next.next = b ; c.next = b;
//            headNode.next = null;                        // b.next = null;
//            return pre;                                  // pre = e - >d ->c ->b   弹栈
//        }
//
//        Node<String> pre = recursion(headNode.next); // pre = e - > d - >c ->b；    head = a;
//        headNode.next.next = headNode;               // a.next.next = a ; b.next = a;
//        headNode.next = null;                        // a.next = null;
//        return pre;                                  // pre = e - >d ->c ->b ->a->null  弹栈
//    }

}
