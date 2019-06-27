package com.earnmoney.foroffer.zhu.algorithm;

/**
 * 北京博瑞彤芸文化传播股份有限公司  版权所有
 * Copyright (c) 2019. bjbrty.com  All Rights Reserved
 * <p>
 * 作者：朱启凯  Email：qikai.zhu@sifude.com
 * 描述：选择排序
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * <p>
 * 创建时间： 2019-06-27
 **/


public class SelectionSort {
    public static void main(String[] args) {
        int[] arr = {38,10,22,49,61,18,70,58,6,20,90};

        for (int i:arr) {
            System.out.print(i);
            System.out.print(",");
        }
        selectionSort(arr);
        System.out.println(" ");
        for (int i:arr) {
            System.out.print(i);
            System.out.print(",");
        }
    }

    private static void selectionSort(int[] arr){
        for (int i=0,len = arr.length-1;i < len;i++){
            for (int j=i;j<=len;j++){
                if (arr[i]<arr[j]){
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
