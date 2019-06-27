package com.earnmoney.foroffer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_hello);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(w, h);
        int height = textView.getMeasuredHeight();
        int width = textView.getMeasuredWidth();
        Log.e("MainActivity","measure width=" + width + " height=" + height);

        textView.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
