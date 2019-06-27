# 为什么在 Activity的onCreate 内无法获取view的 width height
 这里我们需要知道view 是在什么时机才开始进行绘制的。
 View绘制流程—performResumeActivity—>performTraversals  也就是说只有在onResume执行完成之后才可以获取到对应的宽高
 参考链接：https://blog.csdn.net/user11223344abc/article/details/81168087
这里我们需要关注下 ActivityThread.java 的源码。看下启动activity的源码。

    public final Activity startActivityNow(Activity parent, String id,
        Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state,
        Activity.NonConfigurationInstances lastNonConfigurationInstances) {
        ActivityClientRecord r = new ActivityClientRecord();
            r.token = token;
            r.ident = 0;
            r.intent = intent;
            r.state = state;
            r.parent = parent;
            r.embeddedID = id;
            r.activityInfo = activityInfo;
            r.lastNonConfigurationInstances = lastNonConfigurationInstances;
        if (localLOGV) {
            ComponentName compname = intent.getComponent();
            String name;
            if (compname != null) {
                name = compname.toShortString();
            } else {
                name = "(Intent " + intent + ").getComponent() returned null";
            }
            Slog.v(TAG, "Performing launch: action=" + intent.getAction()
                    + ", comp=" + name
                    + ", token=" + token);
        }
        // TODO(lifecycler): Can't switch to use #handleLaunchActivity() because it will try to
        // call #reportSizeConfigurations(), but the server might not know anything about the
        // activity if it was launched from LocalAcvitivyManager.
        //上面是根据信息组装需要的数据
        return performLaunchActivity(r, null /* customIntent */); 
    }
    
    那我们需要在onCreate 内获取对应的宽高该怎么做呢 ？ 
    
    1. 通过view.measure 方法获取 但是获取的值应该是不正确的
     此方法不建议使用！！！！！！！！
     
    2.view.post 方法获取 值正确
    
    <p>Causes the Runnable to be added to the message queue.
        The runnable will be run on the user interface thread.
         这是官方的注释 ：导致将Runnable添加到消息队列。runnable将在用户界面线程上运行
         即将此消息添加到UI线程队列的队尾
         </p>
   
     public boolean post(Runnable action) {
            final AttachInfo attachInfo = mAttachInfo;
            if (attachInfo != null) {
                return attachInfo.mHandler.post(action);
            }
    
            // Postpone the runnable until we know on which thread it needs to run.
            // Assume that the runnable will be successfully placed after attach.
            getRunQueue().post(action);
            return true;
        }
    
    
    
    3.通过view.getViewTreeObserver().addOnGlobalLayoutListener() 获取 值正确但是需要remove 监听
    
    
    
    4.通过view.addOnLayoutChangeListener()    获取的值正确 但是需要 remove 监听
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    