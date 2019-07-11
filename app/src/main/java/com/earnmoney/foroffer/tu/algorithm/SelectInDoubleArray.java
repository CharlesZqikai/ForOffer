package com.earnmoney.foroffer.tu.algorithm;

/**
 * create by tuzanhua on 2019/7/11
 * 二维数组查找   输入一个值在二维数组中查找出来  找出 11
 * 二维数组 从左到右 递增 从上往下递增
 * <p>
 * 1      2     8    9
 * 2      4     9    12
 * 4      7     10   13
 * 6      8     11   15
 * <p>
 * 比如说查找 11
 * 1: 暴力解法 双重for 循环解法 好理解时间复杂度比较高 o(n2)
 * 2: 看这个二维数组 它是从左往右从上往下依次递增的
 * 通过这个规律是不是有更优于 双重for循环的解法呢?  时间复杂度 o(n)
 * <p>
 * 从左上角开始  不可以 因为左边下边都是比它大的值
 * 从右上角开始  可以   左边区域小 往下大
 * 从左下角开始  可以
 * 从右下角开始  不可以
 */
public class SelectInDoubleArray {

    public static void main(String[] args) {
        int[][] arr = {{1, 2, 8, 9}, {2, 4, 9, 12}, {4, 7, 10, 13}, {6, 8, 11, 15}};

        int key = 11;
        boolean existKey = isExistKey(arr, key);
        System.out.println(existKey);
        System.out.println("========================");
        System.out.println(exit(arr,key));
    }

    /**
     * 从右上角开始
     */
    private static boolean exit(int[][] arr, int key) {
        boolean isExist = false;
        int row = arr.length - 1;
        int column = arr[0].length - 1;
        for (int i = 0, j = column; (i >= 0 && i <= row) && (j >= 0 && j <= column); ) {
            if (arr[i][j] == key) {
                isExist = true;
                return isExist;
            } else if (arr[i][j] < key) {
                i++;
            } else if (arr[i][j] > key) {
               j--;
            }
        }
        return isExist;
    }

    /**
     * 双重 for 循环暴力解法  复杂度 o(n2)
     *
     * @return
     */
    private static boolean isExistKey(int[][] arr, int key) {
        boolean isExist = false;
        for (int i = 0, len = arr.length; i < len; i++) {
            for (int j = 0, len1 = arr[i].length; j < len1; j++) {
                if (key == arr[i][j]) {
                    isExist = true;
                    return isExist;
                }
            }
        }
        return isExist;
    }
}
