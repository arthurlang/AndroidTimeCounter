# AndroidTimeCounter
Android定时任务在不同应用场景下的应用（定点精确提示、定时刷新接口、轮询倒计时）
5种方案原理：
1  java Timer+TimerTask （单thread）

2  Handler.postDelayed(Runnable, long) 主线程中发送同步的message，(非精确)，采用异步ThreadHandler处理定时信息发送(精确定时)。

3  更精确地计时，采用AlarmManager实现。

4  ScheduledExecutorService + TimerTask方式（多线程，适用于多线程处理多个耗时操作）

5  一个handler定时更新页面可见数据。

可能会出现 RxJava 方式（java8支持，可是暂时不支持Android）

Case1:一个页面处理多个倒计时

 ScheduledServiceExecutor + TimerTask方式（多线程，适用于多线程处理多个耗时操作）
 
 一个handler定时处理当前页面数据。
 
 Chronometer、 CountDownTimer 2个组件--  但是需要多个Handler，存在性能消耗
 
 Case 2：精确定时处理任务
 Timer+TimerTask
 ThreadHandlder
 AlarmManager

应用：
Google API的倒计时控件：

Google   CountDownTimer倒计时组件。（场景：注册倒计时）
Google   Chronometer计时器。（场景：打折商品倒计时）
TickerView  https://github.com/robinhood/ticker.git



![alt tag](AndroidTimeCounter/timer_counter.png)
