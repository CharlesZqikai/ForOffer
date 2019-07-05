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

// 第一个核心
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
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!这里是真正的网络请求出
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
          
      
          // 第二个核心   
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
      
      // OKHTTP中的核心了，复用连接池   下面的排序是倒着的才对
       1、RealConnection类     connetc socket send receive
       2、ConnectionPool类     socket连接池
       3、StreamAllocation类   入口
      // 下面看每一个拦截器的作用以及真正发起请求的拦截器是哪一个   
      
      retryAndFollowUpInterceptor
      
       创建   StreamAllocation
      
      ConnectInterceptor 
      
      public final class ConnectInterceptor implements Interceptor {
        public final OkHttpClient client;
      
        public ConnectInterceptor(OkHttpClient client) {
          this.client = client;
        }
      
        @Override public Response intercept(Chain chain) throws IOException {
          RealInterceptorChain realChain = (RealInterceptorChain) chain;
          Request request = realChain.request();
          StreamAllocation streamAllocation = realChain.streamAllocation();
      
          // We need the network to satisfy this request. Possibly for validating a conditional GET.
          boolean doExtensiveHealthChecks = !request.method().equals("GET");
          HttpCodec httpCodec = streamAllocation.newStream(client, chain, doExtensiveHealthChecks);
           //sockket 入口 这里通过socket 建立连接  那么 streamAllocation 是干嘛的呢?
          RealConnection connection = streamAllocation.connection(); 
      
          return realChain.proceed(request, streamAllocation, httpCodec, connection);
        }
      }
      
      CallServerInterceptor
      
      组装 response  数据
        
   // socket 
   RealConnection.java
   /** Does all the work necessary to build a full HTTP or HTTPS connection on a raw socket. */
    
     private void connectSocket(int connectTimeout, int readTimeout, Call call,
         EventListener eventListener) throws IOException {
       Proxy proxy = route.proxy();
       Address address = route.address();
   
       rawSocket = proxy.type() == Proxy.Type.DIRECT || proxy.type() == Proxy.Type.HTTP
           ? address.socketFactory().createSocket()
           : new Socket(proxy);
   
       eventListener.connectStart(call, route.socketAddress(), proxy);
       rawSocket.setSoTimeout(readTimeout);
       try {
       // 建立连接
         Platform.get().connectSocket(rawSocket, route.socketAddress(), connectTimeout);
       } catch (ConnectException e) {
         ConnectException ce = new ConnectException("Failed to connect to " + route.socketAddress());
         ce.initCause(e);
         throw ce;
       }
   
       // The following try/catch block is a pseudo hacky way to get around a crash on Android 7.0
       // More details:
       // https://github.com/square/okhttp/issues/3245
       // https://android-review.googlesource.com/#/c/271775/
       try {
         source = Okio.buffer(Okio.source(rawSocket));
         sink = Okio.buffer(Okio.sink(rawSocket));
       } catch (NullPointerException npe) {
         if (NPE_THROW_WITH_NULL.equals(npe.getMessage())) {
           throw new IOException(npe);
         }
       }
     }   
  
// 最后一个核心 okio  NIO    https://www.jianshu.com/p/a6b7410a6fbe    此链接只是随手找来的,请自己查找相关资料并了解
 source(input) sink(out)