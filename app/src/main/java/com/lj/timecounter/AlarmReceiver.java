package com.lj.timecounter;


import static com.lj.timecounter.MainActivity.INTENT_BROADCAST_ALARM_ACTION;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Description
 * Created by langjian on 2017/3/19.
 * Version
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null){
            return;
        }

        String action = intent.getAction();

        if(TextUtils.equals(action, INTENT_BROADCAST_ALARM_ACTION)){
            showNotification(context,"hhhehe");
        }
    }

    private void showNotification(Context context, String s) {
        Toast.makeText(context,"Alarm is time up notification",Toast.LENGTH_SHORT).show();
    }
}
