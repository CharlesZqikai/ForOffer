


根据google官方文档可知 onResume是用户可交互,并没有说绘制完成,这还是比较严谨的,我们可以看看ActivityThread中handleResumeActivity的实现


   已删除非关键代码
  @Override
    public void handleResumeActivity(IBinder token, boolean finalStateRequest, boolean isForward,
            String reason) {
        final ActivityClientRecord r = performResumeActivity(token, finalStateRequest, reason);
        //
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
                // Normally the ViewRoot sets up callbacks with the Activity
                // in addView->ViewRootImpl#setView. If we are instead reusing
                // the decor view we have to notify the view root that the
                // callbacks may have changed.
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
                    // The activity will get a callback for this {@link LayoutParams} change
                    // earlier. However, at that time the decor will not be set (this is set
                    // in this method), so no action will be taken. This call ensures the
                    // callback occurs with the decor set.
                    a.onWindowAttributesChanged(l);
                }
            }
    }
    //由上方代码可见onResume执行在view添加到window之前
    //代码追踪经WindowManagerImpl.java ->WindowManagerGlobal.java->ViewRootImpl.java
    //关键代码
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
                mChoreographer.postCallback(
                        Choreographer.CALLBACK_TRAVERSAL, mTraversalRunnable, null);
            }
        }
       //
       //到Choreographer.java
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
        }
        
MessageQueue中的next()方法可以看出异步消息优先于没有target的message,所以在onCreate中的post执行在doTraversal()之后.
关键代码如下:
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
        