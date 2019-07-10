package com.earnmoney.foroffer.tu.algorithm;

import java.util.ArrayList;

/**
 * create by tuzanhua on 2019/7/10
 * <p>
 * 找出数组中连续的 最大的和
 * <p>
 * 试着从头到尾累加每个数字，若发现有子数组和小于零，则加上后面的数字肯定会变小
 * <p>
 * 因此丢弃这组子数组，从后面一个数字开始重新累加
 */
public class ContinuousSum {

    public static void main(String[] arg) {
        int[] arr = new int[]{-10, -20, -30, 60, -5, 9, -7};
        System.out.println("sum :" + sum(arr));
    }

    private static int sum(int[] arr) {
        int sum = 0;                  // 当前和的值是多少
        int max = Integer.MIN_VALUE;  // 最大值有可能是负值

        int start = 0, end = 0;//用于存储最大子序列的起点和终点
        int p = 0;//指针，用于遍历数组。
        
        for (int i = 0, len = arr.length - 1; i <= len; i++) {
            if (sum < 0) {
                sum = arr[i];
                p = i;
            } else {
                sum += arr[i];
            }

            if (max < sum) {  // 如果最大值小于当前计算值 则重新赋值
                max = sum;

                start = p;
                end = i;
            }
        }
        System.out.println("start :" + start + "=== end :" + end);
        return max;
    }
}
