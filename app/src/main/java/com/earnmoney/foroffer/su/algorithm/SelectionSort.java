package com.earnmoney.foroffer.su.algorithm;

import java.util.Arrays;

/**
 * file: SelectionSort
 * @author: suxianming
 * create date: 2019-06-27 17:39
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] arr = {2, 5, 8, 6, 19, 12, 10, 46, 32, 15, 23, 67, 45};
        selectionSort(arr);
        System.out.println(Arrays.toString(arr));

    }


    public static void selectionSort(int[] arr) {
        for (int i = 0, outLen = arr.length - 1; i < outLen; i++) {
            for (int j = i + 1, innerLen = arr.length; j < innerLen; j++) {
                if (arr[i] > arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                }
            }
        }
    }
}
