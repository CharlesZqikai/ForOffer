package com.earnmoney.foroffer.tu.base.handlerpart;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * create by tuzanhua on 2019/6/25
 */
public class HandlerPart {


    public void handlerPS() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Handler handler = new Handler();
                Looper.loop();
                Message obtain = Message.obtain();
                obtain.obj = "123";
            }
        }).start();
    }
}
