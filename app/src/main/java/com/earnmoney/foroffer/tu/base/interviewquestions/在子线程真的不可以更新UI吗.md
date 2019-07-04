# 面试题 安卓在子线程可以更新UI吗？ 

1。不可以（绝对吗？）

  //https://blog.csdn.net/xyh269/article/details/52728861
   在concrete 内部可以更新UI  因为这个时候viewRootImpl 还没有创建无法检查当前线程
   
   ViewRootImpl.java
   
    @Override
       public void requestLayout() {
           if (!mHandlingLayoutInLayoutRequest) {
               checkThread();  // 检查线程抛出异常
               mLayoutRequested = true;
               scheduleTraversals();
           }
       }
   
   //而我知道 viewRootImpl  创建以及执行是在 onResume 里面执行的 
       
   
   
2。如果想要在子线程更新UI 怎么做？
 Handler 抛出消息到主线程