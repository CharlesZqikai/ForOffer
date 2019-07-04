package com.earnmoney.foroffer.zhu.algorithm;

/**
 * 北京博瑞彤芸文化传播股份有限公司  版权所有
 * Copyright (c) 2019. bjbrty.com  All Rights Reserved
 * <p>
 * 作者：朱启凯  Email：qikai.zhu@sifude.com
 * 描述：${todo}(用一句话描述该文件做什么)
 * 修改历史:
 * 修改日期         作者        版本        描述说明
 * <p>
 * 创建时间： 2019-07-02
 *
 **/


public class FindRepeat {

    public static void main(String[] arg){

        int[] aar = {2,3,1,0,2,5,3};
        System.out.println(findRepeat(aar));
    }

    private static int findRepeat(int[] aar) {
        for (int i=0,len = aar.length-1;i<len;i++){
            for (int j=i;j<len;j++){
                if (aar[i]-aar[j]==0){
                    return aar[i];
                }
            }
        }

        return -1;
    }
}
