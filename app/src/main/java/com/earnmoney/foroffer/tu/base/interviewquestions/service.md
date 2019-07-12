# 1.4 IntentService与Service的区别（intentservice的优点）
  
  IntentService是Service的子类，是一个异步的，会自动停止的服务，很好解决了传统的Service中处理完耗时操作忘记停止并销毁Service的问题
  
  
  会创建独立的worker线程来处理所有的Intent请求；
  会创建独立的worker线程来处理onHandleIntent()方法实现的代码，无需处理多线程问题；
  所有请求处理完成后，IntentService会自动停止，无需调用stopSelf()方法停止Service；
  为Service的onBind()提供默认实现，返回null；
  为Service的onStartCommand提供默认实现，将请求Intent添加到队列中；
  IntentService不会阻塞UI线程，而普通Serveice会导致ANR异常
  Intentservice若未执行完成上一次的任务，将不会新开一个线程，是等待之前的任务完成后，再执行新的任务，等任务完成后再次调用stopSelf()
  
  
  1.6 Service的生命周期
  Service 有绑定模式和非绑定模式,以及这两种模式的混合使用方式。不同 的使用方法生命周期方法也不同。
  
  非绑定模式:当第一次调用 startService 的时候执行的方法依次为 onCreate()、onStartCommand(),当 Service 关闭的时候调用 onDestory 方 法。
  绑定模式:第一次 bindService()的时候,执行的方法为 onCreate()、 onBind()解除绑定的时候会执行 onUnbind()、onDestory()。

  2.1 Service 的 onStartCommand 方法有几种返回值?各代表什么意思?
  有四种返回值,不同值代表的意思如下:
  
  
  START_STICKY:如果 service 进程被 kill 掉,保留 service 的状态为开始状态,但不保留递送的 intent 对象。随 后系统会尝试重新创建 service,由于服务状态为开始状态,所以创建服务后一定会调用 onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到 service,那么参数 Intent 将为 null。
  
  START_NOT_STICKY:“非粘性的”。使用这个返回值时,如果在执行完 onStartCommand 后,服务被异常 kill 掉,系统不会自动重启该服务。
  
  START_REDELIVER_INTENT:重传 Intent。使用这个返回值时,如果在执行完 onStartCommand 后,服务被异 常 kill 掉,系统会自动重启该服务,并将 Intent 的值传入。
  
  START_STICKY_COMPATIBILITY: START_STICKY 的兼容版本,但不保证服务被 kill 后一定能重启
  
 
  
  