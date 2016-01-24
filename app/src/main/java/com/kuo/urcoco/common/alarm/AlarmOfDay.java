package com.kuo.urcoco.common.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

/**
 * Created by Kuo on 2015/12/16.
 */
public class AlarmOfDay {

    private Context context;

    public AlarmOfDay(Context context) {
        this.context = context;
    }

    public void onCreateAlarm() {

        SharedPreferences sharedPreferences = context.getSharedPreferences("date", 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, sharedPreferences.getInt("alarm_hour_time", 12));
        calendar.set(Calendar.MINUTE, sharedPreferences.getInt("alarm_minute_time", 0));
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent();
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pending);
    }

    public void onCancelAlarm() {

        AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent();
        intent.setClass(context, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (pending != null){
            mAlarmManager.cancel(pending);
        }

    }
}
