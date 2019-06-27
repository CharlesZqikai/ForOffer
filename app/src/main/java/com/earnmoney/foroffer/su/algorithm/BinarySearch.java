package com.earnmoney.foroffer.su.algorithm;

/**
 * file: BinarySearch
 *
 * @author: suxianming
 * create date: 2019-06-26 18:27
 */

public class BinarySearch {
    public static void main(String[] args) {
        int[] arr = new int[]{10, 18, 22, 38, 49, 58, 61, 70};
        System.out.println(binarySearch(arr, 16));
        System.out.println(binarySearch(arr, 22));
        System.out.println(recursiveBinarySearch(arr,61,0,arr.length-1));
    }


    private static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
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
            int mid = (low + high) >>> 1;
            if (arr[mid] == key) {
                return mid;
            } else if (arr[mid] > key) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public static int recursiveBinarySearch(int[] arr, int key, int low, int high) {
        int mid = (low + high) >>> 1;

        if (key < arr[low] || key > arr[high]) {
            return -1;
        }

        if (key == arr[low]) {
            System.out.println("low:"+low);
            return low;
        }
        if (key == arr[high]) {
            System.out.println("high:"+high);
            return high;
        }
        if (arr[mid] > key) {
            return recursiveBinarySearch(arr, key, low, mid - 1);
        } else if (arr[mid] < key) {
            return recursiveBinarySearch(arr, key, mid + 1, high);
        } else {
            System.out.println("mid:"+mid);
            return mid;
        }

    }
}
