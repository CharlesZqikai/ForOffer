package com.earnmoney.foroffer.tu.base.java;

/**
 * Create by tuzanhua on 2020-04-09
 * <p>
 * java 中只有值传递;
 *
 *  1：一个方法不能修改一个基本数据类型的参数（八种基本数据类型）
 *  2：一个方法可以改变一个对象参数的状态
 *  3：一个方法不能让对象参数引用一个新的对象 ⭐️⭐️⭐️⭐️⭐️⭐️⭐️
 */
public class ObjectAndValue {

    public static void main(String[] args) {

        int[] arr = {1, 2, 3, 4, 5, 6};
        swap(arr);
        System.out.println("after value :" + arr[0]);

        Person p1 = new Person("小李");
        Person p2 = new Person("小张");
        swap(p1,p2);
        System.out.println("after p1 :" + p1.name);
        System.out.println("after p2 :" + p2.name);

    }

    /**
     * ⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️⭐️
     */

    public static void swap(Person p1, Person p2) {
//        Person temp = p1;
//        p1 = p2;
//        p2 = temp;
        p1.name="123";

        p1 = new Person("gaiming li");
        p2 = new Person("gaiming zhang");
        System.out.println("swap p1 :" + p1.name);
        System.out.println("swap p2 :" + p2.name);
    }

    public static void swap(int[] array) {

        int[] arr1 = array;
        arr1[0] = 11;
        System.out.println("value :" + arr1[0]);
    }


    public static class Person {
        public String name;

        public Person(String name) {
            this.name = name;
        }
    }
}
