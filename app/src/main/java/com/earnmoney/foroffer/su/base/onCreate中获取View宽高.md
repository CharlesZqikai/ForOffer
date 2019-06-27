---
onCreate()中获取View的宽高
---
- onWindowFocusChanged
	
>这个方法的含义是：View已经初始化完毕了，宽/高以及准备好了，这个时候去获取宽高是没问题的。这个方法会被调用多次，当Activity的窗口得到焦点和失去焦点时均会被调用一次。具体来说，当Activity继续执行和暂停执行时，会被调用。如果频繁地onPause和onResume，这个方法会被频繁调用。典型的代码如下

```java
	public void onWindowFocusChanged(boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		if(hasFocus){
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
		}
	}
```	


- view.post(runnable)

 	- View.post(runnable)内部有两种判断，如果当前View没有绘制完成，通过类HandlerActionQueue内部将Runnable缓存下来，否则就直接通过mAttachInfo.mHandler将这些Runnable操作post到主线程的MessageQueue中等待执行
 	- mAttachInfo.mHandler是ViewRootImpl中的成员变量，绑定主线程的Looper,所以View.post的操作会转到主线程的Looper,所以View.post的操作会转到主线程之中，自然可以作为更新UI的根据了
 	- Handler消息机制是不断的从队列中获取Message对象，所以View.post(Runnable)中的Runnable操作肯定会在performMeasure()之后才执行，所以此时可以获取到View的宽高
	
	典型的代码如下：
	
	```java
	protected void onResume(){
		super.onResume();
		view.post(()->{
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
		});
	}
	```
- ViewTreeObserver
	
	使用ViewTreeObserver的回调接口可以完成这个功能，比如使用onPrewDrawListener这个接口，当视图树要被绘制时，onPrewDrawListener方法会被回调，这是获取View宽高一个很好的时机。这个方法会被回调多次，所以回调的时候要进行移除操作。代码如下：

```java	
	protected void onResume(){
		super.onResume();
		final ViewTreeObserver obs = tv.getViewTreeObserver();
		obs.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
   		 @Override
    	public boolean onPreDraw () {
    		obs.removeOnPreDrawListener(this);
      		int width = view.getMeasuredWidth();
      		int height = view.getMeasuredHeight();
       	return true;
   		}
	});
}
```
 	
