package com.earnmoney.foroffer.tu.base.java;

import android.support.annotation.Nullable;

/**
 * Create by tuzanhua on 2020-04-09
 */
public class HashCodeAndEquals {
    private String name;
    private int age;

    public HashCodeAndEquals() {

    }

    /**
     * {@link  Object hashCode()
     * hashcode 顾名思义 它是hash值 只会在散列表中起作用 默认object 里面的hashcode 是对象在堆中的地址值}
     *
     * 如果我们在hash表中使用对应的对象需要重写此方法 因为如果使用 object 中默认的值  == 地址值永远不一样
     */
    @Override
    public int hashCode() {
        int i = name.hashCode();
        return i ^ age;
    }

    /**
     * 此方法比较的是两个对象的值是否一样 object 方法内默认是 this == obj 即和 == 方法是一样的
     *
     * 第一步： 判断对比的是不是null
     * 第二步： 判断是不是同一个对象
     * 第三步 ： 判断是不是同一类型的值
     * 第四步： 判断所有的字段总和是不是一样
     *
     */

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj)
            return true;
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        HashCodeAndEquals obj1 = (HashCodeAndEquals) obj;

        return this.name.equals(obj1.name) && this.age == obj1.age;

    }
}
