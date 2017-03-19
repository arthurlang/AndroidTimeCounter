package com.lj.timecounter.util;

import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Description
 * Created by langjian on 2017/3/2.
 * Version
 */

public class TimeUtils{

    private static final int DAY_IN_SEC = 3600*24;
    private static final int HOUR_IN_SEC = 3600;
    private static final int MIN_IN_SEC = 60;

    /**
     * V8.3 add
     * @param totalSeconds
     * @return  calendar 包含时分秒的Calendar对象
     */
    /**
     * V8.3 add 秒数转化为时分秒格式
     * @param totalSeconds
     * @return  calendar 包含时分秒的Calendar对象
     */
    public static CustomTimeUnit calculateDayHourMinSeconds(int totalSeconds) {
        CustomTimeUnit timeUnit = new CustomTimeUnit();
        if(totalSeconds < 0){
            Log.e("","calculateDayHourMinSeconds parameter is invalide totalSeconds =" + totalSeconds);
        }else{
            long days = 0;
            long hours = 0;
            long minutes = 0;
            if(totalSeconds >= DAY_IN_SEC){
                days = java.util.concurrent.TimeUnit.SECONDS.toDays(totalSeconds);
                totalSeconds -= days * (DAY_IN_SEC);
            }
            if (totalSeconds >= HOUR_IN_SEC) {
                hours = java.util.concurrent.TimeUnit.SECONDS.toHours(totalSeconds);
                totalSeconds -= hours * HOUR_IN_SEC;
            }
            if (totalSeconds >= MIN_IN_SEC) {
                minutes = java.util.concurrent.TimeUnit.SECONDS.toMinutes(totalSeconds);
                totalSeconds -= minutes * MIN_IN_SEC;
            }
            timeUnit.setDDHHMM(days, hours, minutes, totalSeconds);
        }
        return timeUnit;
    }
}
