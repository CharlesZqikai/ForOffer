package com.earnmoney.foroffer.zhu.algorithm;

/**
 * 北京博瑞彤芸文化传播股份有限公司  版权所有
 * Copyright (c) 2019. bjbrty.com  All Rights Reserved
 * <p>
 * 作者：朱启凯  Email：qikai.zhu@sifude.com
 * 描述：二分查找
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * <p>
 * 创建时间： 2019-06-26
 **/


public class BinarySearchAlgorithm {

    public static void main(String[] args) {
        int[] arr = {10, 18, 22, 38, 49, 58, 61, 70};
//        System.out.println(binarySearch(arr, 9));
//        System.out.println(binarySearch(arr, 16));

        System.out.println(binarySearchRecursion(arr,61,arr.length-1,0));
        System.out.println(binarySearchRecursion(arr,38,arr.length-1,0));
    }

    private static int binarySearch(int[] arr, int target) {
        int max = arr.length - 1;
        int min = 0;
        int index = -1;
        int mid;

        if (min > max || target < arr[min] || target > arr[max]) {
            return index;
        }

        while (min <= max) {
            mid = (min + max) / 2;
            if (arr[mid] > target) {
                max = mid - 1;
            } else if (arr[mid] < target) {
                min = mid + 1;
            } else {
                index = mid;
                break;
            }
        }
        return index;
    }



//    int max = arr.length-1;
//    int min = 0;
    /**
     * 递归实现
     */
    private static int binarySearchRecursion(int[] arr, int target, int max, int min) {

        int index = -1;
        if (min > max || target < arr[min] || target > arr[max]) {
            return index;
        }
        int mid = (min + max) / 2;
        if (arr[mid] > target) {
            index = binarySearchRecursion(arr, target, mid - 1, min);
        } else if (arr[mid] < target) {
            index = binarySearchRecursion(arr, target, max, mid + 1);
        } else {
            index = mid;
        }
        return index;
    }

}
