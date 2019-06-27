# Handler looper MessageQueue 三者之间的关系以及如何关联在一起

子线程创建Handler 会抛出    throw new RuntimeException(
                                  "Can't create handler inside thread " + Thread.currentThread()
                                          + " that has not called Looper.prepare()");

追踪核心代码 :
    第一步: Looper.prepare();

    // 每个线程只能创建一个Looper 对象    这里需要理解下 ThreadLocal  里面是一个map 保存了每个线程的副本
       private static void prepare(boolean quitAllowed) {
            if (sThreadLocal.get() != null) {
                throw new RuntimeException("Only one Looper may be created per thread");
            }
            sThreadLocal.set(new Looper(quitAllowed));   //这里帮你创建出了 一个Looper 对象 并和当前的Thread 关联起来 请看下面的set方法
        }

        ThreadLocal
            public void set(T value) {  // Thread 和 Looper 关联的具体实现
                Thread t = Thread.currentThread();
                ThreadLocalMap map = getMap(t);
                if (map != null)
                    map.set(this, value);
                else
                    createMap(t, value);
            }

     在这里我们想 looper 轮训器创建出来了 那么消息队列呢在哪里? 接下来我们看new Looper做了什么操作

      private Looper(boolean quitAllowed) {
            mQueue = new MessageQueue(quitAllowed); // 这里在构造器内创建除了对应的 MessageQueue 另外注意 messageQueue 是当前类的成员变量
            mThread = Thread.currentThread();
        }

      到了这一步 looper Thread MessageQueue 都已经关联起来了 那么 Handler 呢? 它在哪里呢?


    // 下面是 Handler 对象的构造函数在初始化的时候会获取当前Thread 的 looper 对象  怎么来的呢?你是不是忘记ThreadLocal 存储looper了呢!!!!!!!!!!!!
      public Handler(Callback callback, boolean async) {
                 if (FIND_POTENTIAL_LEAKS) {
                     final Class<? extends Handler> klass = getClass();
                     if ((klass.isAnonymousClass() || klass.isMemberClass() || klass.isLocalClass()) &&
                             (klass.getModifiers() & Modifier.STATIC) == 0) {
                         Log.w(TAG, "The following Handler class should be static or leaks might occur: " +
                             klass.getCanonicalName());
                     }
                 }

            ***     mLooper = Looper.myLooper(); // 这里获取当前的looper
            ***     if (mLooper == null) {
            ***         throw new RuntimeException(
                         "Can't create handler inside thread " + Thread.currentThread()
                                 + " that has not called Looper.prepare()");
                 }
            ***    mQueue = mLooper.mQueue;
                 mCallback = callback;
                 mAsynchronous = async;
             }

      // 到这里三者的关系就关联起来了 但是一个Looper 可以对应多个Handler 对象 当你sendMessage 的时候是如何分发到对应的Handler 呢?
       // 多说一个知识点 所有的sendMessage 其实都是调用的 sendMessageDelayed
        public final boolean sendMessage(Message msg)
          {
              return sendMessageDelayed(msg, 0);
          }


          //接下来重点来了我们看消息入队时候是怎么操作的
        class   Handler{
           private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
                            msg.target = this;  // look this 看这里看这里!!!! target  = this; 这样在对应的dispatch 的时候就可以找到对应的handler
                            if (mAsynchronous) {
                                msg.setAsynchronous(true);
                            }
                            return queue.enqueueMessage(msg, uptimeMillis);
                        }
          }


     下面看消息入队 :  MessageQueue

     // 请仔细阅读此入队代码!!!!!!!!!!!!!!! 面试会经常问到,此处阅读不方便请阅请直接查看.java 源码
     !!!! 主要就是 入队会遍历整个消息队列,如果队列为空直接放在头部,如果不为空会一直遍历直到找到一个比 放入的 when 小的 然后插入消息,否则插入消息的尾部
         boolean enqueueMessage(Message msg, long when) {
             if (msg.target == null) {
                 throw new IllegalArgumentException("Message must have a target.");
             }
             if (msg.isInUse()) {
                 throw new IllegalStateException(msg + " This message is already in use.");
             }

             synchronized (this) {
                 if (mQuitting) {
                     IllegalStateException e = new IllegalStateException(
                             msg.target + " sending message to a Handler on a dead thread");
                     Log.w(TAG, e.getMessage(), e);
                     msg.recycle();
                     return false;
                 }

                 msg.markInUse();
                 msg.when = when;
                 Message p = mMessages;
                 boolean needWake;
                 if (p == null || when == 0 || when < p.when) {
                     // New head, wake up the event queue if blocked.
                     msg.next = p;
                     mMessages = msg;
                     needWake = mBlocked;
                 } else {
                     // Inserted within the middle of the queue.  Usually we don't have to wake
                     // up the event queue unless there is a barrier at the head of the queue
                     // and the message is the earliest asynchronous message in the queue.
                     needWake = mBlocked && p.target == null && msg.isAsynchronous();
                     Message prev;
                     for (;;) {
                         prev = p;
                         p = p.next;
                         if (p == null || when < p.when) {
                             break;
                         }
                         if (needWake && p.isAsynchronous()) {
                             needWake = false;
                         }
                     }
                     msg.next = p; // invariant: p == prev.next
                     prev.next = msg;
                 }

                 // We can assume mPtr != 0 because mQuitting is false.
                 if (needWake) {
                     nativeWake(mPtr);
                 }
             }
             return true;
         }

         看完下面这一段就介绍完了 我们在(子线程创建并使用)Hadler 的时候需要手动调用 Looper.loop()

         looper.loop(); 内部是一个死循环 for(;;){}

             public static void loop() {
                 final Looper me = myLooper();
                 if (me == null) {
                     throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
                 }
                 final MessageQueue queue = me.mQueue;

                 // Make sure the identity of this thread is that of the local process,
                 // and keep track of what that identity token actually is.
                 Binder.clearCallingIdentity();
                 final long ident = Binder.clearCallingIdentity();

                 // Allow overriding a threshold with a system prop. e.g.
                 // adb shell 'setprop log.looper.1000.main.slow 1 && stop && start'
                 final int thresholdOverride =
                         SystemProperties.getInt("log.looper."
                                 + Process.myUid() + "."
                                 + Thread.currentThread().getName()
                                 + ".slow", 0);

                 boolean slowDeliveryDetected = false;

                 for (;;) {
                     Message msg = queue.next(); // might block !!!!!!!!!!!!!!!!!!!!! 大家注意看这里 想下为什么会阻塞呢????
                     if (msg == null) {
                         // No message indicates that the message queue is quitting.
                         return;
                     }

                     // This must be in a local variable, in case a UI event sets the logger
                     final Printer logging = me.mLogging;
                     if (logging != null) {
                         logging.println(">>>>> Dispatching to " + msg.target + " " +
                                 msg.callback + ": " + msg.what);
                     }

                     final long traceTag = me.mTraceTag;
                     long slowDispatchThresholdMs = me.mSlowDispatchThresholdMs;
                     long slowDeliveryThresholdMs = me.mSlowDeliveryThresholdMs;
                     if (thresholdOverride > 0) {
                         slowDispatchThresholdMs = thresholdOverride;
                         slowDeliveryThresholdMs = thresholdOverride;
                     }
                     final boolean logSlowDelivery = (slowDeliveryThresholdMs > 0) && (msg.when > 0);
                     final boolean logSlowDispatch = (slowDispatchThresholdMs > 0);

                     final boolean needStartTime = logSlowDelivery || logSlowDispatch;
                     final boolean needEndTime = logSlowDispatch;

                     if (traceTag != 0 && Trace.isTagEnabled(traceTag)) {
                         Trace.traceBegin(traceTag, msg.target.getTraceName(msg));
                     }

                     final long dispatchStart = needStartTime ? SystemClock.uptimeMillis() : 0;
                     final long dispatchEnd;
                     try {
                         msg.target.dispatchMessage(msg);   // !!!!!!!!!!!!!!!!!!!!!!! 大家看这里!!!!!! 就是在这里dispatchMessage msg.target 在sendMessage 的时候绑定在一起的呢
                         dispatchEnd = needEndTime ? SystemClock.uptimeMillis() : 0;
                     } finally {
                         if (traceTag != 0) {
                             Trace.traceEnd(traceTag);
                         }
                     }
                     if (logSlowDelivery) {
                         if (slowDeliveryDetected) {
                             if ((dispatchStart - msg.when) <= 10) {
                                 Slog.w(TAG, "Drained");
                                 slowDeliveryDetected = false;
                             }
                         } else {
                             if (showSlowLog(slowDeliveryThresholdMs, msg.when, dispatchStart, "delivery",
                                     msg)) {
                                 // Once we write a slow delivery log, suppress until the queue drains.
                                 slowDeliveryDetected = true;
                             }
                         }
                     }
                     if (logSlowDispatch) {
                         showSlowLog(slowDispatchThresholdMs, dispatchStart, dispatchEnd, "dispatch", msg);
                     }

                     if (logging != null) {
                         logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
                     }

                     // Make sure that during the course of dispatching the
                     // identity of the thread wasn't corrupted.
                     final long newIdent = Binder.clearCallingIdentity();
                     if (ident != newIdent) {
                         Log.wtf(TAG, "Thread identity changed from 0x"
                                 + Long.toHexString(ident) + " to 0x"
                                 + Long.toHexString(newIdent) + " while dispatching to "
                                 + msg.target.getClass().getName() + " "
                                 + msg.callback + " what=" + msg.what);
                     }

                     msg.recycleUnchecked();
                 }
             }