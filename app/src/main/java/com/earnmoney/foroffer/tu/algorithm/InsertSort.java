package com.earnmoney.foroffer.tu.algorithm;

import java.util.Arrays;

/**
 * create by tuzanhua on 2019/7/11
 * 插入排序  {9,-1,10,2,9,11,12,-2}
 *  插入排序 : 扑克牌思想
 *   以第 0个排好序为基准,每次从后往前查找.
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] arr = {-1, 9, 1, 10, 2, 9, 11, 12, -2};

        for (int i = 1; i < arr.length; i++) {
            int j = i - 1;
            int insertKey = arr[i];

            while (j >= 0 && arr[j] > insertKey) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = insertKey;
        }

        System.out.println(Arrays.toString(arr));
    }
}
