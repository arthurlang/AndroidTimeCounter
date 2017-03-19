package com.lj.timecounter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.lj.timecounter.util.CustomTimeUnit;
import com.lj.timecounter.util.TimeUtils;
import com.lj.timecounter.view.CustomChronometer;

import java.util.Formatter;
import java.util.IllegalFormatException;
import java.util.Locale;

/**
 * Description 仿api24中Chronometer完成countDown功能
 * Created by langjian on 2017/2/17.
 * Version
 */

public class ChronometerActivity extends Activity {
    private CustomChronometer mChronometer;
    private String mFormatDHHMMSS = "还剩%1$d天%2$d时%3$d分到期";
    private Formatter mFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_chronometer_layout);

        mFormatter = new Formatter(null, Locale.getDefault());
        // 如果秒钟有余数，则余数进1，即数据显示为整数+1
        // 举例：剩余1分钟20秒的时候，显示为2分钟
        mChronometer = (CustomChronometer) findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime() + 62000);
        mChronometer.setOnChronometerTickListener(
                new CustomChronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(CustomChronometer cArg) {

                    }

                    @Override
                    public void onChronometerTickComplete(CustomChronometer chronometer) {

                    }

                    @Override
                    public String onFormatTime(long elapsedSeconds) {

                        CustomTimeUnit unit = TimeUtils.calculateDayHourMinSeconds(
                                (int) elapsedSeconds);
                        // 如果秒钟有余数，则余数进1，即数据显示为整数+1
                        // 举例：剩余1分钟20秒的时候，显示为2分钟
                        if (unit != null) {
                            try {

                                long days = unit.day;
                                long hourOfDays = unit.hourOfDay;
                                long minutes = unit.minute;
                                // V8.3 add 如果秒钟有余数，则余数进1，即数据显示为整数+1.举例：剩余1分钟20秒的时候，显示为2分钟
                                if (unit.second > 0) {
                                    minutes++;
                                    if (minutes == 60) {
                                        minutes = 0;
                                        hourOfDays++;
                                        if (hourOfDays == 24) {
                                            hourOfDays = 0;
                                            days++;
                                        }
                                    }
                                }

                                mFormatter = mFormatter.format(mFormatDHHMMSS, days, hourOfDays,
                                        minutes);
                                return mFormatter.toString();
                            } catch (IllegalFormatException ex) {
                                Log.e(TAG, "Illegal format string: " + mFormatDHHMMSS);
                            }
                            return null;
                        }

                        return null;
                    }
                });
        mChronometer.start();
    }
}