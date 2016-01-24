package com.kuo.urcoco.common.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.kuo.urcoco.MainActivity;
import com.kuo.urcoco.R;

/**
 * Created by Kuo on 2015/12/16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(Color.parseColor("#FFFFFF"))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("UrCoco")
                .setContentText("在忙碌的一天，也別忘了追蹤狀況喔！");

        Notification notification = builder.build();
        notification.contentIntent = pendingIntent;

        manager.notify(0, notification);
    }


}
