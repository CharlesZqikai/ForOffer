package com.earnmoney.foroffer.tu.algorithm;

import java.util.Arrays;

/**
 * create by tuzanhua on 2019/6/27
 * {38,10,22,49,61,18,70,58,6,20,90}
 * 选择排序: 思想第一个和第二个比较如果第二个比第一数小互换位置,依次比较第一个和第三个,第四个第五个....,第一轮结束从 第二个数比较
 * 6,38,22,49,61,18,70,58,10,20,90  第一轮结束
 */
public class SelectSort {
    public static void main(String[] args){
        int[] arr = {38,10,22,49,61,18,70,38,58,6,20,90};
        for(int i = 0,len = arr.length -1;i <len;i++){
            for(int j = i +1,len1 = arr.length -1;j<=len1;j++ ){
                // 比较第几轮的值arr[i] 与内层 第几个数的值arr[j] 比较互换位置
                if(arr[j] < arr[i]){
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] =  temp;
                }
            }
        }

        System.out.println(Arrays.toString(arr));
    }
}
