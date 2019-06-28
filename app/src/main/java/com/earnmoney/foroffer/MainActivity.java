package com.earnmoney.foroffer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.earnmoney.foroffer.tu.base.measureinoncreate.TzhMeasureOnCreateActivity;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("MainActivity-send", "measure width=" + mTextView.getHeight() + " height=" + mTextView.getWidth());
        }
    };


    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.tv_hello);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mTextView.measure(w, h);
        int height = mTextView.getMeasuredHeight();
        int width = mTextView.getMeasuredWidth();
//        Log.e("MainActivity", "measure width=" + width + " height=" + height);

        mTextView.post(new Runnable() {
            @Override
            public void run() {
                Log.e("MainActivity-viewpost", "width=" + mTextView.getHeight() + " height=" + mTextView.getWidth());
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.e("MainActivity-handlerpost", "width=" + mTextView.getHeight() + " height=" + mTextView.getWidth());
            }
        });

//        mHandler.sendEmptyMessage(110);

        findViewById(R.id.btn_measure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TzhMeasureOnCreateActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}