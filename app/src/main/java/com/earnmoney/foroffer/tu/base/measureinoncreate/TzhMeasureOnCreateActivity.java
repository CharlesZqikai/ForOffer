package com.earnmoney.foroffer.tu.base.measureinoncreate;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.earnmoney.foroffer.R;

/**
 * create by tuzanhua on 2019/6/27
 *  getWidth() getMeasuredWidth()  区别
 * The size of a view is expressed with a width and a height. A view actually possess two pairs of width and height values.
 *
 * The first pair is known as measured width and measured height. These dimensions define how big a view wants to be within its parent
 * (see Layout for more details.) The measured dimensions can be obtained by calling getMeasuredWidth() and getMeasuredHeight().
 *
 * The second pair is simply known as width and height, or sometimes drawing width and drawing height. These dimensions define the actual
 * size of the view on screen, at drawing time and after layout. These values may, but do not have to, be different from the measured width
 * and height. The width and height can be obtained by calling getWidth() and getHeight().
 *
 * Translate(译文) :
 * 这段话足以解释 getMeasuredXXX() 与 getXXX() 的区别和联系所在。说得直白一点，measuredWidth 与 width 分别对应于视图绘制 的 measure 与 layout 阶段。
 * 很重要的一点是，我们要明白，View 的宽高是由 View 本身和 parent 容器共同决定的，要知道有这个 MeasureSpec 类的存在。
 *
 * 比如，View 通过自身 measure() 方法向 parent 请求 100x100 的宽高，那么这个宽高就是 measuredWidth 和 measuredHeight 值。
 * 但是，在 parent 的 onLayout() 方法中，通过调用 childview.layout() 方法只分配给 childview 50x50 的宽高。那么，这个 50x50 宽高就是
 * childview 实际绘制并显示到屏幕的宽高，也就是 width 和 height 值。
 *
 * 在onCreate 内获取控件的宽高 直接获取为 0 , 0 原因view 没有开始测量以及绘制操作
 * 1. 通过view.measure 方法获取 但是获取的值应该是不正确的
 * 2.view.post 方法获取 值正确
 * 3.通过view.getViewTreeObserver().addOnGlobalLayoutListener() 获取 值正确但是需要remove 监听
 * 4.通过view.addOnLayoutChangeListener()    获取的值正确 但是需要 remove 监听
 */
public class TzhMeasureOnCreateActivity extends FragmentActivity {

    private TextView mtvMeasure;
    private static final String TAG = "TzhMeasureOn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_measure_inoncreate);
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 912 , 106
                Log.e(TAG, "onClick :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight());// 912 106
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, " Handler post :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight());
                Log.e(TAG, "Handler post :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight());
            }
        });

        mtvMeasure = findViewById(R.id.tv_measure);
        Log.e(TAG, mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 0,0
        Log.e(TAG, mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight());// 0, 0

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        mtvMeasure.measure(widthMeasureSpec, heightMeasureSpec);

        mtvMeasure.setText("12");
        Log.e(TAG, "measure :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 0 , 0
        Log.e(TAG, "measure :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight()); // 899  106

        mtvMeasure.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mtvMeasure.removeOnLayoutChangeListener(this);
                // resume 之后调用
                Log.e(TAG, "onLayoutChange :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 912,106
                Log.e(TAG, "onLayoutChange :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight()); //912,106
            }
        });

        mtvMeasure.post(new Runnable() {
            @Override
            public void run() {
                // 在 getViewTreeObserver 之后打印
                Log.e(TAG, "post :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 912  106
                Log.e(TAG, "post :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight()); // 912  106
            }
        });

        mtvMeasure.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mtvMeasure.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                // resume 之后打印
                Log.e(TAG, "getViewTreeObserver :   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 912  106
                Log.e(TAG, "getViewTreeObserver :   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight()); // 912  106
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:   " + mtvMeasure.getWidth() + "==" + mtvMeasure.getHeight()); // 0 , 0
        Log.e(TAG, "onResume:   " + mtvMeasure.getMeasuredWidth() + "==" + mtvMeasure.getMeasuredHeight());// 899 , 106
    }

}
