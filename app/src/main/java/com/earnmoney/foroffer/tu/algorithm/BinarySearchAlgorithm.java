package com.earnmoney.foroffer.tu.algorithm;

/**
 * create by tuzanhua on 2019/6/25
 * 使用条件: 有序
 * 时间复杂度 o(logn)
 * 两种方式实现: 递归 recursion
 * 非递归 while 循环方式
 * 哪种方式更好理解呢 ??????????
 */
public class BinarySearchAlgorithm {

    public static void main(String[] arg) {
        System.out.println("main");
        int[] arr = {4, 6, 9, 10, 12, 23, 55};
        int low = 0;
        int high = arr.length - 1;
        // 基础补充 java float 4 字节 , double 8 字节  double 精度是 float 的两倍但是运算速度要慢很多
        System.out.println(3 / 2);
        float a = 1.8f;
        System.out.println((int) a);
        System.out.println(System.currentTimeMillis());
        System.out.println("===================================");
        System.out.println(recursion(arr, 9, low, high));
        System.out.println(recursion(arr, 6, low, high));
        System.out.println(recursion(arr, 10, low, high));
        System.out.println(recursion(arr, 23, low, high));
        System.out.println("===================================");
        System.out.println(forWhile(arr, 9, 0, high));
        System.out.println(forWhile(arr, 6, 0, high));
        System.out.println(forWhile(arr, 10, 0, high));
        System.out.println(forWhile(arr, 23, 0, high));
    }

    /**
     * recursion 查找对应的 index
     */
    public static int recursion(int[] arr, int key, int low, int high) {
        int mid = (low + high) / 2;
        if (key < arr[low] || key > arr[high]) {
            return -1;
        }
        if (key == arr[low]) {
            return low;
        }
        if (key == arr[high]) {
            return high;
        }
        if (key < arr[mid]) {
            return recursion(arr, key, low, mid - 1);
        } else if (key > arr[mid]) {
            return recursion(arr, key, mid + 1, high);
        } else {
            return mid;
        }
    }


    /**
     * 非递归形式实现 二分查找
     */
    public static int forWhile(int[] arr, int key, int low, int high) {
        int mid = 0;
        if (key < arr[low] || key > arr[high]) {
            return -1;
        }
        if (key == arr[low]) {
            return low;
        }
        if (key == arr[high]) {
            return high;
        }
        while (low <= high) {
            mid = (low + high) / 2;
            if (key < arr[mid]) {
                high = mid - 1;
            } else if (key > arr[mid]) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
