package com.lj.timecounter.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Description
 * Created by langjian on 2017/3/19.
 * Version
 */

public class AlarmUtil {

    private static AlarmManager mAlarmManager;

    public static void setAlarm(Context context, long startduration, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, startduration, pendingIntent);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 10*1000, pendingIntent);
    }

    public static void cancelAlarm(Context context, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        mAlarmManager.cancel(pendingIntent);

    }
}
