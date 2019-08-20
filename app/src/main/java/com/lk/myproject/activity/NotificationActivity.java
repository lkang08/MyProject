package com.lk.myproject.activity;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.lk.myproject.R;
import com.lk.myproject.utils.NotificationUtils;

import java.security.acl.NotOwnerException;
import java.util.concurrent.LinkedBlockingQueue;

public class NotificationActivity extends Activity {
    private NotificationManager updateNotificationManager;
    RemoteViews contentView;
    private NotificationCompat.Builder mNotifyBuilder;
    String NOTIFICATION_DOWNLOAD_CHANNEL_ID = "11";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNotification2();
            }
        });
    }

    void showNotification2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.showNotification("ME", "下载", 2, "11", 0, 100);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 100; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        NotificationUtils.showNotification("ME", "下载", 2, "11", i, 100);
                    }
                }
            }).start();
        }
    }

    void showNotification() {
        updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_DOWNLOAD_CHANNEL_ID, "to-do",
                            NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.setSound(null, null);
            updateNotificationManager.createNotificationChannel(notificationChannel);
        }
        contentView = new RemoteViews(this.getPackageName(),
                R.layout.notificationupdate_new);
        contentView.setTextViewText(R.id.tvnow, "正在下载...");
        contentView.setTextViewText(R.id.tvjingdu, "0%");
        contentView.setProgressBar(R.id.progress_horizontal, 100, 0,
                false);

        Intent updateIntent = new Intent("");
        PendingIntent updatePendingIntent = PendingIntent.getActivity(this, 0,
                updateIntent, 0);
        mNotifyBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_DOWNLOAD_CHANNEL_ID)
                        .setTicker("开始下载")
                        //.setCustomContentView(contentView)
                        //.setContent(contentView)
                        .setContentText("通知内容")
                        .setContentTitle("通知标题")
                        .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                        .setVibrate(new long[]{0})
                        .setSound(null)
                        .setContentIntent(updatePendingIntent)
                        .setSmallIcon(R.drawable.notify_icon)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notify_icon))
                        .setOngoing(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        updateNotificationManager.notify(1, mNotifyBuilder.build());
        //nmc.notify(1, mNotifyBuilder.build());

    }

    void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentView.setProgressBar(R.id.progress_horizontal, 100,
                            i, false);
                    contentView.setTextViewText(R.id.tvjingdu, i + "%");
                    mNotifyBuilder.setCustomContentView(contentView);
                    if (i == 100) {
                        mNotifyBuilder.setAutoCancel(true);
                    }
                    updateNotificationManager.notify(1, mNotifyBuilder.build());
                }
            }
        }).start();
    }
}
