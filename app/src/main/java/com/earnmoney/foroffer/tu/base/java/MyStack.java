package com.earnmoney.foroffer.tu.base.java;

import java.util.Arrays;

/**
 * create by tuzanhua on 2020/3/23
 * 使用数组实现栈
 * 自己实现一个栈，要求这个栈具有push()、pop()（返回栈顶元素并出栈）、peek() （返回栈顶元素不出栈）、isEmpty()、size()这些基本的方法。
 */
public class MyStack<T> {
    private T[] elements;
    private int size = 0;
    private int capacity = 8;

    public MyStack() {
        elements = (T[]) new Object[capacity];
    }

    /**
     * 存放元素
     */
    public void push(T t) {
        if (elements.length == size) {
            ensureCapacity();
        }
        elements[size++] = t;
    }

    /**
     * 扩容
     */
    private void ensureCapacity() {
        elements = Arrays.copyOf(elements, size * 2 + 1);
    }


    /**
     * 弹栈
     */
    public T pop() {
        if (elements.length == 0) {
            throw new IllegalArgumentException("stack is empty");
        }
        //获取栈顶元素 注意 是--size
        T element = elements[--size];
        //释放内存
        elements[size] = null;
        return element;
    }

    /**
     * 获取栈顶元素但是不弹出栈
     */
    public T peek() {
        if (elements.length == 0) {
            throw new IllegalArgumentException("stack is empty");
        }
        return elements[size - 1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

}
