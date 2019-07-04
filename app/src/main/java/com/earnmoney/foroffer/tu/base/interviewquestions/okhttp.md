# okhttp 面试相关知识点 ：

使用：
Request request = new Request.Builder().url("url").build();
okHttpClient.newCall(request).enqueue(new Callback() {
  @Override
  public void onFailure(Call call, IOException e) {

 }

@Override
public void onResponse(Call call, Response response) throws IOException {

}
});


RealCall.java

  @Override public void enqueue(Callback responseCallback) {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");  //只可以执行一次
      executed = true;
    }
    captureCallStackTrace();
    client.dispatcher().enqueue(new AsyncCall(responseCallback));       // client.dispatcher()
  }

核心重点类Dispatcher线程池介绍:

 Dispatcher.java 

 // 总共支持的并发数
 private int maxRequests = 64; 
 // 单个host 支持的最多并发数  同一个 host                          
  private int maxRequestsPerHost = 5;  
         
  private @Nullable Runnable idleCallback;

  /** Executes calls. Created lazily. */
  //线程池
  private @Nullable ExecutorService executorService;

  /** Ready async calls in the order they'll be run. */
  //异步准备好的队列
  private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<>();

  /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
  // 正在执行的队列
  private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<>();

  /** Running synchronous calls. Includes canceled calls that haven't finished yet. */
  // 同步队列
  private final Deque<RealCall> runningSyncCalls = new ArrayDeque<>();
  
  默认线程池：SynchronousQueue 对应 newCachedThreadPool 空闲线程最多活60S
  
    public synchronized ExecutorService executorService() {
      if (executorService == null) {
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
      }
      return executorService;
    }
  
   // 入队
   synchronized void enqueue(AsyncCall call) {
   // 先进行判断 将新创建的线程放到哪个队列
      if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
        runningAsyncCalls.add(call);
        executorService().execute(call);
      } else {
        readyAsyncCalls.add(call);
      }
    }
    
    
    RealCall.java
    
    // 入队之后是执行 那么runable 执行结束之后是啥
     final class AsyncCall extends NamedRunnable {
        private final Callback responseCallback;
    
        AsyncCall(Callback responseCallback) {
          super("OkHttp %s", redactedUrl());
          this.responseCallback = responseCallback;
        }
    
        String host() {
          return originalRequest.url().host();
        }
    
        Request request() {
          return originalRequest;
        }
    
        RealCall get() {
          return RealCall.this;
        }
    
        @Override protected void execute() {
          boolean signalledCallback = false;
          try {
            //这里是真正的网络请求出
            Response response = getResponseWithInterceptorChain();
            if (retryAndFollowUpInterceptor.isCanceled()) {
              signalledCallback = true;
              responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
            } else {
              signalledCallback = true;
              responseCallback.onResponse(RealCall.this, response);
            }
          } catch (IOException e) {
            if (signalledCallback) {
              // Do not signal the callback twice!
              Platform.get().log(INFO, "Callback failure for " + toLoggableString(), e);
            } else {
              responseCallback.onFailure(RealCall.this, e);
            }
          } finally {
          //无论如何都会移除出队列
            client.dispatcher().finished(this);
          }
        }
      }
      
      
      //Dispatcher.java     这里做的就是 将等待队列的 线程放到正在执行的线程，同时开始执行线程
      
      /** Used by {@code AsyncCall#run} to signal completion. */
        void finished(AsyncCall call) {
          finished(runningAsyncCalls, call, true);
        }
        
          private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
            int runningCallsCount;
            Runnable idleCallback;
            synchronized (this) {
              if (!calls.remove(call)) throw new AssertionError("Call wasn't in-flight!");
              if (promoteCalls) promoteCalls();
              runningCallsCount = runningCallsCount();
              idleCallback = this.idleCallback;
            }
        
            if (runningCallsCount == 0 && idleCallback != null) {
              idleCallback.run();
            }
          }
          
         
         // RealCall.java    责任链模式  
          
         Response getResponseWithInterceptorChain() throws IOException {
           // Build a full stack of interceptors.
           List<Interceptor> interceptors = new ArrayList<>();
           interceptors.addAll(client.interceptors());
           interceptors.add(retryAndFollowUpInterceptor);
           interceptors.add(new BridgeInterceptor(client.cookieJar()));
           interceptors.add(new CacheIntercepto r(client.internalCache()));
           interceptors.add(new ConnectInterceptor(client));
           if (!forWebSocket) {
             interceptors.addAll(client.networkInterceptors());
           }
           interceptors.add(new CallServerInterceptor(forWebSocket));
       
           Interceptor.Chain chain = new RealInterceptorChain(
               interceptors, null, null, null, 0, originalRequest);
           return chain.proceed(originalRequest);
         }
  
  