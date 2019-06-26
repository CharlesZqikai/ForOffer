package com.earnmoney.foroffer.zhu.algorithm;

import android.os.SystemClock;

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

    public static void main(String[] args){
        int[] arr = {};
        System.out.println(binarySearch(arr,9));
        System.out.println(binarySearch(arr,16));
    }

    private static int binarySearch(int[] arr,int target){
        int max = arr.length-1;
        int min = 0;
        int index = -1;
        int mid;

        if (min>max||target<arr[min]||target>arr[max]){
            return index;
        }

        while(min<=max){
            mid = (min+max)/2;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            if (arr[mid]>target){
                max = mid-1;
            }else if (arr[mid]<target){
                min = mid+1;
            }else{
                index = mid;
                break;
            }
        }
        return index;

    }
}
