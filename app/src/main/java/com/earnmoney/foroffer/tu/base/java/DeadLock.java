package com.earnmoney.foroffer.tu.base.java;

/**
 * create by tuzanhua on 2019/7/10
 *
 * Dead Lock Demo
 */
public class DeadLock {

    public static void main(String[] args){
        deadLockSimple();
    }

    private static void deadLockSimple() {
        final String lockA = "A";
        final String lockB = "B";

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    synchronized (lockA){
                        synchronized (lockB){
                            System.out.println("thread a");
                        }
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    synchronized (lockB){
                        synchronized (lockA){
                            System.out.println("thread B");
                        }
                    }
                }
            }
        }).start();
    }
}
