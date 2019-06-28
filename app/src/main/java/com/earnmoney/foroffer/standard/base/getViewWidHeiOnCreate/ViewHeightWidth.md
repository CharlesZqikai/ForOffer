#Android 获取 View 宽高的方式  参考 https://zhooker.github.io/2017/12/18/Android-%E8%8E%B7%E5%8F%96-View-%E5%AE%BD%E9%AB%98%E7%9A%84%E6%96%B9%E5%BC%8F/


前言
在Android系统中，如果在 onCreate() 或 onResume() 生命周期方法中通过 getWidth() 之类的方法获取 View 的宽高值，那结果肯定是 0。这需要我们去了解一下View的绘制流程，View 需要onMeasure、onLayout、onDraw 三个过程才会真正画出来，执行onMeasure后可以得到 mMeasuredWidth、mMeasuredHeight， 执行onLayout 后可以得到 mLeft、mTop、mRight、mBottom 四个值，getWidth和getHeight就是通过这四个值计算的，如果我们在onCreate() 或 onResume() 中获取View 的宽高值，由于此时View还没measure\layout\draw，所以此时获取的就是0。在measure\layout\draw的每个过程都有方法获取View 的宽高值，下面一一介绍。

View#measure 获取宽高
我们可以直接调用 View.measure 来获取 mMeasuredWidth与mMeasuredHeight，但是这个测量宽高可能与实际宽高不一致。
getMeasuredWidth() 与 getMeasuredHeight() 获取的是mMeasuredWidth与mMeasuredHeight，这是在 measure 过程计算出来的测量宽高，而 getWidth() 与 getHeight() 是在 layout 过程之后计算出来的宽高。我们知道 View 的宽高是由 View 本身和 parent 容器共同决定的。mMeasuredWidth与mMeasuredHeight是View 本身的size，但是如果 parent 容器 没有足够大，View就不得不降低自己的尺寸。比如，View 通过自身 measure() 方法向 parent 请求 100x100 的宽高，那么这个宽高就是 measuredWidth 和 measuredHeight 值。但是，在 parent 的 onLayout() 阶段，通过 childview.layout() 方法只分配给 childview 50x50 的宽高。那么，这个 50x50 宽高就是 childview 实际绘制并显示到屏幕的宽高，也就是 width 和 height 值。

如果view是match_parent，理论上需要父view的size，即父View的剩余空间，所以我们需要在measure时传递正确的父View的Size，而如果在初始化阶段，父view的size也很难确定，那理论上这种情况是测量不出view的size。
如果view宽高是具体的数值，比如 100x100px，那可以如下measure :

int widthMeasureSpec = MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
int heightMeasureSpec = MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
view.measure(widthMeasureSpec, heightMeasureSpec);

如果view宽高是wrap_content，理论上可以用View能支持的最大值去构造MeasureSpec ，然后再measure出view的宽高 :

int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec, heightMeasureSpec);
综上所述，利用measure 需要考虑多种情况，而且最终得到的是mMeasuredWidth、mMeasuredHeight，还不一定与 getWidth() 、getHeight()相同，所以这种方式不推荐。

View#onLayout 获取宽高
上面说过要想拿到真正的宽高值，就要在 onLayout之后去获取，对于自定义的View，可以重写 onLayout() 并在此函数中获取宽高：

view = new View(this) {
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width = view.getWidth();
        int height = view.getHeight();
    }
};

View#OnLayoutChangeListener() 获取宽高
View一旦 layout 变化的时候，立即回调 onLayoutChange 方法：

view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        view.removeOnLayoutChangeListener(this);
        int width = view.getWidth();
        int height = view.getHeight();
    }
});
ViewTreeObserver#addOnGlobalLayoutListener() 获取宽高
ViewTreeObserver 可以监听 View 的全局变化事件。比如，layout 变化，draw 事件等。当 layout 变化 就会回调 OnGlobalLayoutListener：

view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= 16) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        int width = view.getWidth();
        int height = view.getHeight();
    }
});
ViewTreeObserver#addOnGlobalLayoutListener与View#OnLayoutChangeListener有些许不同，对于View#OnLayoutChangeListener官方文档是这么说的 ：

Interface definition for a callback to be invoked when the layout bounds of a view changes due to layout processing.

对于ViewTreeObserver#addOnGlobalLayoutListener官方文档是这么说的 ：

Interface definition for a callback to be invoked when the global layout state or the visibility of views within the view tree changes.

从中可以发现在view本身的layout时才会回调OnLayoutChangeListener。而对于OnGlobalLayoutListener, 不管view本身或其父view的layout时都会回调。

ViewTreeObserver#OnPreDrawListener() 获取宽高
OnPreDrawListener是在draw之前的回调，此时已经 layout 过，可以获取到 View 的宽高值。OnPreDrawListener还可以控制绘制流程，返回false的时候就取消当前绘制流程，View会再schedule下一次绘制：

view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
    @Override
    public boolean onPreDraw() {
        view.getViewTreeObserver().removeOnPreDrawListener(this);
        int width = view.getWidth();
        int height = view.getHeight();
        return true;
    }
});
view#post() 获取宽高
view.post(new Runnable() {
    @Override
    public void run() {
        int width = view.getWidth();
        int height = view.getHeight();
    }
});
为什么这个函数可以获取宽高值，就需要了解一下View的绘制流程，在onResume之前view.post的时候，会把Runnable添加到一个RunQueue中，在 onResume的时候 DecorView 会被添加到 ViewRoomImpl，之后会向主线程的消息队列发送一个绘制消息 TraversalRunnable，当执行到这个绘制消息的时候就会执行ViewRoomImpl.performTraversals()，ViewRoomImpl.performTraversals()先绘制整个DecorView，然后执行RunQueue中的消息，所以执行 view.post 的消息会在draw流程之后执行。

Activity#onWindowFocusChanged() 获取宽高
当整个DecorView已经绘制完成，Activty已经要显示出来的时候，就会回调Activity#onWindowFocusChanged。

@Override
public void onWindowFocusChanged(boolean hasFocus) {
	super.onWindowFocusChanged(hasFocus);
        int width = view.getWidth();
        int height = view.getHeight();
}
