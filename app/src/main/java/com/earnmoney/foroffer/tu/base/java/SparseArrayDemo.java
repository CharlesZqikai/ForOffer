package com.earnmoney.foroffer.tu.base.java;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.util.HashMap;

/**
 * Create by tuzanhua on 2019-07-04
 *
 * SparseArray 采用两个数据来实现一个Map
 * key arr[int]     value    arr[]    key 强制使用 int 基本类型避免了数据的拆箱装箱工作
 * 它的出现是为了节省内存而出现的 （HashMap） 效率高但是会占用大量内存
 *
 * HashMap  必须使用应用数据类型
 *
 */
public class SparseArrayDemo {

    public static void main(String[] args) {

        SparseArray<String> sparseArray = new SparseArray<>();
        sparseArray.put(1, "S1");
        sparseArray.put(2, "S1");
        sparseArray.put(3, "S1");
        show();

    }

    private static void show(){
        for(int i = 0;i < 100;i++){
            if(i != 10){
                System.out.println("currentX :" + i);
            }else{
                System.out.println("currentX :" + i);
                return;
            }

        }
        System.out.println("return le ma ");
    }
}









