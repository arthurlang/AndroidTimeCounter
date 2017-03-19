package com.lj.timecounter.util;

/**
 * Description
 * Created by langjian on 2017/3/19.
 * Version
 */

public class CustomTimeUnit {
    public long day;
    public long hourOfDay;
    public long minute;
    public long second;

    public CustomTimeUnit(long days, long hours, long minutes, long seconds) {
        this.day = days;
        this.hourOfDay = hours;
        this.minute = minutes;
        this.second = seconds;
    }

    public CustomTimeUnit() {
    }

    public void setDDHHMM(long days, long hours, long minutes, long seconds) {
        this.day = days;
        this.hourOfDay = hours;
        this.minute = minutes;
        this.second = seconds;
    }
}
