请写出在onCreate 里获取view宽高的几种方式，以及View.post{}是如何能获得view的宽高的？

answer:

严格来说在onCreate中是无法获取view宽高的，因为此时view还没有完成measure layout,只有在onResume之后view才正常可以取到宽高,
因此想要在onCreate中获取宽高,只能采取特殊手段,将获取的时机后移到onResume之后, 当然有一种方式比较特殊,就是手动测量,但由于不知道父view的测量模式,可以与实际结果不同

1、通过注册  getViewTreeObserver().addOnGlobalLayoutListener();当视图的全局事件变化时回调,可能回调多次,所以当拿到宽高时及时取消注册
2、通过注册  getViewTreeObserver().addOnPreDrawListener();当视图树开始绘制时回调,此时已经测量、摆放完成，所以可以拿到宽高，也要及时取消注册
3、通过 View.post{} 获取
4、手动测量      int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
               int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
               textView.measure(w, h);
               int height = textView.getMeasuredHeight();
               int width = textView.getMeasuredWidth();
               Log.e("MainActivity","measure width=" + width + " height=" + height);
               
               
               
第二种方式 View.post{} 实际是调用了Handler.post{}方法,往消息队列的尾部插入了一条message.至于为什么往消息队列尾部插入一条消息问什么就可以拿到宽高,解释如下:

首先 ActivityThread 并不是一个 Thread，就只是一个 final 类而已。我们常说的主线程就是从这个类的 main 方法开始，main 方法很简短，一眼就能看全，我们看到里面有 Looper 了，
那么接下来就找找 ActivityThread 对应的 Handler 啊，就是内部类 H，其继承 Handler，贴出 handleMessage 的小部分

public void handleMessage(Message msg) {
            if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
            switch (msg.what) {
                case LAUNCH_ACTIVITY: {
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
                    final ActivityClientRecord r = (ActivityClientRecord) msg.obj;

                    r.packageInfo = getPackageInfoNoCheck(
                            r.activityInfo.applicationInfo, r.compatInfo);
                    handleLaunchActivity(r, null);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                } break;
                case RELAUNCH_ACTIVITY: {
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityRestart");
                    ActivityClientRecord r = (ActivityClientRecord)msg.obj;
                    handleRelaunchActivity(r);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                } break;
                case PAUSE_ACTIVITY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                    handlePauseActivity((IBinder)msg.obj, false, (msg.arg1&1) != 0, msg.arg2,
                            (msg.arg1&2) != 0);
                    maybeSnapshot();
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case PAUSE_ACTIVITY_FINISHING:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                    handlePauseActivity((IBinder)msg.obj, true, (msg.arg1&1) != 0, msg.arg2,
                            (msg.arg1&1) != 0);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case STOP_ACTIVITY_SHOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                    handleStopActivity((IBinder)msg.obj, true, msg.arg2);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case STOP_ACTIVITY_HIDE:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                    handleStopActivity((IBinder)msg.obj, false, msg.arg2);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SHOW_WINDOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityShowWindow");
                    handleWindowVisibility((IBinder)msg.obj, true);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case HIDE_WINDOW:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityHideWindow");
                    handleWindowVisibility((IBinder)msg.obj, false);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case RESUME_ACTIVITY:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityResume");
                    handleResumeActivity((IBinder) msg.obj, true, msg.arg1 != 0, true);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case SEND_RESULT:
                    Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityDeliverResult");
                    handleSendResult((ResultData)msg.obj);
                    Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;

            ...........
}

看完这 Handler 里处理消息的内容应该明白了吧， Activity 的生命周期都有对应的 case 条件了，ActivityThread 有个 getHandler 方法，得到这个 handler 就可以发送消息，
然后 loop 里就分发消息，然后就发给 handler, 然后就执行到 H（Handler ）里的对应代码。所以这些代码就不会卡死～，有消息过来就能执行。举个例子，在 ActivityThread 里的内部类 ApplicationThread 中就有很多 sendMessage 的方法：

......
public final void schedulePauseActivity(IBinder token, boolean finished,
                boolean userLeaving, int configChanges, boolean dontReport) {
            sendMessage(
                    finished ? H.PAUSE_ACTIVITY_FINISHING : H.PAUSE_ACTIVITY,
                    token,
                    (userLeaving ? 1 : 0) | (dontReport ? 2 : 0),
                    configChanges);
        }

        public final void scheduleStopActivity(IBinder token, boolean showWindow,
                int configChanges) {
           sendMessage(
                showWindow ? H.STOP_ACTIVITY_SHOW : H.STOP_ACTIVITY_HIDE,
                token, 0, configChanges);
        }

        public final void scheduleWindowVisibility(IBinder token, boolean showWindow) {
            sendMessage(
                showWindow ? H.SHOW_WINDOW : H.HIDE_WINDOW,
                token);
        }

        public final void scheduleSleeping(IBinder token, boolean sleeping) {
            sendMessage(H.SLEEPING, token, sleeping ? 1 : 0);
        }

        public final void scheduleResumeActivity(IBinder token, int processState,
                boolean isForward, Bundle resumeArgs) {
            updateProcessState(processState, false);
            sendMessage(H.RESUME_ACTIVITY, token, isForward ? 1 : 0);
}

......


               