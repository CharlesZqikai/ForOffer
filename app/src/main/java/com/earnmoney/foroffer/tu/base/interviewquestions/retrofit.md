# retrofit 源码简单解析

// 参考链接 : https://blog.piasy.com/2016/06/25/Understand-Retrofit/index.html   这个链接还是不错的

基本用法 :
  
  // 没有设置 callFactory  默认okhttpClient   callAdapter 默认DefaultCallAdapter 
  
   Retrofit retrofit = new Retrofit.Builder()
       .baseUrl("https://api.github.com/")
       .addCallAdapterFactory(RxJavaCallAdapterFactory.create())                   //Rxjava  default
       .addConverterFactory(GsonConverterFactory.create(new Gson()))               // request response convert
       .build();
       
       
   retrofit.create(Class);                        //这一步动态代理开始
   
     public <T> T create(final Class<T> service) {
       Utils.validateServiceInterface(service);
       if (validateEagerly) {
         eagerlyValidateMethods(service);
       }
       return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[] { service },
           new InvocationHandler() {
             private final Platform platform = Platform.get();
   
             @Override public Object invoke(Object proxy, Method method, @Nullable Object[] args)
                 throws Throwable {
               // If the method is a method from Object then defer to normal invocation.
               if (method.getDeclaringClass() == Object.class) {
                 return method.invoke(this, args);
               }
               if (platform.isDefaultMethod(method)) {
                 return platform.invokeDefaultMethod(method, service, proxy, args);
               }
               ServiceMethod<Object, Object> serviceMethod =
                   (ServiceMethod<Object, Object>) loadServiceMethod(method);             // 核心 -> serviceMethod
               OkHttpCall<Object> okHttpCall = new OkHttpCall<>(serviceMethod, args);     // okhttp3.Call  transferTo Retrofit Call
               return serviceMethod.callAdapter.adapt(okHttpCall);                        // adapter.adapt(); 触发请求
             }
           });
     }
     
     
     
   CallAdapter Default  RxjavaAdapter
   
   Default : 这个是enqueue 异步调用,线程池由 okhttp 控制
   RxJavaAdapter : 这个对Call 进行了转换,线程池是使用的Rxjava 内的线程池. 使用的是同步方法