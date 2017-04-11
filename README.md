# AndroidTimeCounter
Android定时任务在不同应用场景下的应用（定点精确提示、定时刷新接口、轮询倒计时）
6种方案原理：
1 java Timer+TimerTask     （单thread）
2 thread+sleep（精确刷新在子线程中进行） 
3  Handler.postDelayed(Runnable, long)    
4  ScheduledExecutorService + TimerTask方式（多线程 ）
5  一个handler定时更新固定页面数据的情况
6  AlarmManager实现精确定时
7 RxJava 方式（java8支持，可是暂时不支持Android）

![alt tag](AndroidTimeCounter/timer_counter.png)

应用：
Google API的倒计时控件：

Google   CountDownTimer倒计时组件。（场景：注册倒计时）
Google   Chronometer计时器。（场景：打折商品倒计时）

TickerView  https://github.com/robinhood/ticker.git
