package com.earnmoney.foroffer.su.algorithm;

/**
 * file: BinarySearch
 * @author: suxianming
 * create date: 2019-06-26 18:27
 */

public class BinarySearch {
    public static void main(String[] args) {
        int[] arr = new int[]{10, 18, 22, 38, 49, 58, 61, 70};
        System.out.println(binarySearch(arr, 16));
        System.out.println(binarySearch(arr, 22));

    }


    private static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] == key) {
                return mid;
            }
            if (arr[mid] > key) {
                high = mid - 1;
            }

            if (arr[mid] < key) {
                low = mid + 1;
            }
        }
        return -1;
    }
}
