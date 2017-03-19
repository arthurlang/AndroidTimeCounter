package com.lj.timecounter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lj.timecounter.util.AlarmUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 *  汇总5种常见实现定时器的原理
 1 thread+sleep
 2 Handler.postDelayed(Runnable, long)
 3（单thread）Timer+TimerTask
 4 ScheduledExecutorService + TimerTask方式（多线程 ）
 5 AlarmManager实现精确定时
 */
public class MainActivity extends AppCompatActivity {
    private static final long HEART_BEAT_RATE = 30 * 1000;//目前心跳检测频率为30s
    public static final String INTENT_BROADCAST_ALARM_ACTION = "intent_filter_action";
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            // excute task
            processTask();
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            processTask();
        }
    };
    private Timer mTimer = new Timer();

    private ScheduledExecutorService mThreadPool = Executors.newSingleThreadScheduledExecutor();
    private boolean isSetAlarm = true;
    private Thread mThread;

    private void processTask()
    {
        Log.e("processTask","----execute task");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mHandler != null){
            mHandler.removeCallbacks(heartBeatRunnable);
        }

        mTimer.cancel();
    }

    protected void onHandler(View v){
        if(mHandler != null){
            mHandler.removeCallbacks(heartBeatRunnable);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    }

    protected void onSleep(View v){
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mThread.isInterrupted()){
                    processTask();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mThread.interrupt();
            }
        },10000);
    }

    protected void onTimer(View v){
        if(mTimer != null){
            mTimer.cancel();
        }
        mTimer.scheduleAtFixedRate(mTimerTask,0,1000);

    }

    protected void onScheduledExecutorService(View v){
        if(mThreadPool != null && mThreadPool.isShutdown()){
            mThreadPool.shutdown();
        }
        mThreadPool.schedule(mTimerTask, 1000, TimeUnit.MILLISECONDS);

    }

    protected void onAlarmManager(View v){

        Intent intent = new Intent(INTENT_BROADCAST_ALARM_ACTION);
        if(isSetAlarm){
            isSetAlarm = false;
            long warmTime = System.currentTimeMillis()  + 10;
            AlarmUtil.setAlarm(this, warmTime * 1000, intent);

        }else{
            isSetAlarm = true;
            AlarmUtil.cancelAlarm(this,intent);
        }

    }

    protected void onRxjava(View view) {
        Toast.makeText(this,"java8支持，可是暂时不支持Android",Toast.LENGTH_SHORT).show();
    }

    protected void onChronometer(View v){
        Toast.makeText(this,"https://developer.android.com/reference/android/widget/Chronometer.html",Toast.LENGTH_SHORT).show();
    }
    protected void onCountDownTimer(View v){
        Toast.makeText(this,"https://developer.android.com/reference/android/os/CountDownTimer.html",Toast.LENGTH_SHORT).show();
    }
    protected void onCustomChronometer(View v){
        startActivity(new Intent(MainActivity.this,ChronometerActivity.class));
    }
    protected void onTicker(View v){
        Toast.makeText(this,"https://github.com/robinhood/ticker.git",Toast.LENGTH_SHORT).show();
    }

}
