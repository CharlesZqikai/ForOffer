package com.earnmoney.foroffer.standard.algorithm;

/**
 * create by tuzanhua on 2019/6/27
 *
 * 二分查找algorithm   请通过这道题理解二分查找的思想!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class BinarySearchAlgorithm {

    public static void main(String[] args) {
        int[] arr = {6, 9, 11, 15, 18, 20, 25, 29, 31};

        System.out.println(getIndex(arr, 18, 0, arr.length - 1));
        System.out.println(byWhile(arr,18));

        System.out.println(getIndex(arr, 9, 0, arr.length - 1));
        System.out.println(byWhile(arr,9));

        System.out.println(getIndex(arr, 17, 0, arr.length - 1));
        System.out.println(byWhile(arr,17));
    }

    /**
     * recursion 递归形式(理解起来有难度但是希望尽量思考一下) 无论 递归多深最终有边界条件 促使其 return
     * 在实现递归的时候我们需要进行逻辑思考: 因为根本是需要指针的移动,而且每次递归进去 指针是会改变的,那么就需要我们传递对应的指针完成递归
     */
    public static int getIndex(int[] arr, int key, int low, int high) {
        // 为了更高效的计算,首先判断临界条件

        if (key < arr[low] || key > arr[high]) {
            return -1;
        }
        if (key == arr[low]) {
            return low;
        }
        if (key == arr[high]) {
            return high;
        }
        int mid = (low + high) / 2;
        if (arr[mid] == key) {
            return mid;
        } else if (key < arr[mid]) { // 在左半区  high 指针重新赋值 赋值为 mid -1
            return getIndex(arr, key, low, mid - 1);
        } else if (key > arr[mid]) { //在右半区  low  指针重新赋值 赋值为 mid + 1
            return getIndex(arr, key, mid + 1, high);
        }
        // 未找到对应的值
        return -1;
    }

    /**
     *  while 循环的形式,此种形式相对recursion 方式相对容易理解,同样是需要移动指针来完成数据的查找,那么while 循环的条件是什么呢?
     *  low 指针 <= high 指针
     */
    public static int byWhile(int[] arr, int key) {
        // 为了更高效的计算,首先判断临界条件
        int low = 0;
        int high = arr.length - 1;
        int mid = -1;
        if (key < arr[low] || key > arr[high]) {
            return -1;
        }
        if (key == arr[low]) {
            return low;
        }
        if (key == arr[high]) {
            return high;
        }

        // 思考一下 这里的临界判断为 low <= high (为什么要这样写呢?) 我上面已经添加了临界值判断 这里是不是可以省略呢?
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
