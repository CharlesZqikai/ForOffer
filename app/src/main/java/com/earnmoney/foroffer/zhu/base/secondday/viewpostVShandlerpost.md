---
   根据google官方文档可知 onResume是用户可交互,并没有说绘制完成,这还是比较严谨的,我们可以看看ActivityThread中handleResumeActivity的实现
---

>已删除非关键代码
```java
    @Override
    public void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward,
            String reason) {
        final ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        //省略部分非关键代码
        View decor = r.window.getDecorView();
        decor.setVisibility(View.INVISIBLE);
        ViewManager wm = a.getWindowManager();
            WindowManager.LayoutParams l = r.window.getAttributes();
            a.mDecor = decor;
            l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
            l.softInputMode |= forwardBit;
            if (r.mPreserveWindow) {
                a.mWindowAdded = true;
                r.mPreserveWindow = false;
                ViewRootImpl impl = decor.getViewRootImpl();
                if (impl != null) {
                    impl.notifyChildRebuilt();
                }
            }
            if (a.mVisibleFromClient) {
                if (!a.mWindowAdded) {
                    a.mWindowAdded = true;
                    //关键代码
                    wm.addView(decor, l);
                } else {
                    a.onWindowAttributesChanged(l);
                }
            }
    }
```

>由上方代码可见onResume执行在view添加到window之前
>代码追踪经WindowManagerImpl.java ->WindowManagerGlobal.java->ViewRootImpl.java
>关键代码如下
```java
        @Override
        public void requestLayout() {
            if (!mHandlingLayoutInLayoutRequest) {
                checkThread();
                mLayoutRequested = true;
                scheduleTraversals();
            }
        }

        void scheduleTraversals() {
            if (!mTraversalScheduled) {
                //关键中的关键 省略部分代码
                mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();
                mChoreographer.postCallback(
                        Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
            }
        }
        
        final class TraversalRunnable implements Runnable {
            @Override
            public void run() {
                doTraversal();
            }
        };
        
        void doTraversal() {
            performTraversals();
        }
        
        performTraversals(){
//          ...
//最为关键的代码
          host.dispatchAttachedToWindow(mAttachInfo, 0);
//          ...
        }
        
        //转到Choreographer.java
        public void postCallback(int callbackType, Runnable action, Object token) {
               postCallbackDelayed(callbackType, action, token, 0);
           }
           
        private void postCallbackDelayedInternal(int callbackType,
                    Object action, Object token, long delayMillis) {
                    //最终封装成了一个异步message 省略部分代码
                    Message msg = mHandler.obtainMessage(MSG_DO_SCHEDULE_CALLBACK, action);
                    msg.arg1 = callbackType;
                    msg.setAsynchronous(true);
                    mHandler.sendMessageAtTime(msg, dueTime);
       }
```
        
>接下来分析view.post(runnable)方法
```java
      public boolean post(Runnable action) {
            final AttachInfo attachInfo = mAttachInfo;
            if (attachInfo != null) {
                return attachInfo.mHandler.post(action);
            }
            getRunQueue().post(action);
            return true;
        }
        //参考HandlerActionQueue.java 中的代码
```
        
> View.post(runnable)内部有两种判断，attachInfo为空，通过类HandlerActionQueue内部将Runnable缓存下来，在view的dispatchAttachedToWindow中真正的post的消息队列中,此时已然测量绘制完成,拿到宽高自然不成问题
> 否则就直接通过mAttachInfo.mHandler将这些Runnable操作post到主线程的MessageQueue中等待执行mAttachInfo.mHandler是ViewRootImpl中的成员变量，绑定主线程的Looper,
> 所以View.post的操作会转到主线程的Looper,所以View.post的操作会转到主线程之中，自然可以作为更新UI的根据了
> Handler消息机制是不断的从队列中获取Message对象，所以View.post(Runnable)中的Runnable操作肯定会在performMeasure()之后才执行，所以此时可以获取到View的宽高.
> 关键代码如下
```java
  void dispatchAttachedToWindow(AttachInfo info, int visibility) {
        mAttachInfo = info;
       // Transfer all pending runnables.
        if (mRunQueue != null) {
            mRunQueue.executeActions(info.mHandler);
            mRunQueue = null;
        }    
    }
    //参考HandlerActionQueue.java 中的代码
```

由此可得出结论在onCreate中使用View.post可以获取宽高,使用new Handler().post 不能



至于为什么在非launcherActivity中可以获取原因如下,activity点击按钮跳转的时候会引起视图重绘,阻塞了handlerPost的消息,如不通过点击按钮跳转则获取不到.
>MessageQueue中的next()方法可以看出异步消息优先于没有target的message,此处可以得出结论,view绘制优先其他message
>关键代码如下:

```java
     Message next() {
        Message prevMsg = null;
        Message msg = mMessages;
        if (msg != null && msg.target == null) {
            // Stalled by a barrier.  Find the next asynchronous message in the queue.
            do {
                prevMsg = msg;
                msg = msg.next;
            } while (msg != null && !msg.isAsynchronous());
        }
     }
```        