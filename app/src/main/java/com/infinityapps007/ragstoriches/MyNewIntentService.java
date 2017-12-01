package com.infinityapps007.ragstoriches;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

import com.infinityapps007.ragstoriches.fragments.Categories;

import java.util.Random;

/**
 * Created by Magic Lenz on 21-Nov-17.
 */

public class MyNewIntentService extends IntentService {
    private static final int    NOTIFICATION_ID = 31;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Success Stories Of Great People");
        String title = titleArray[new Random().nextInt(titleArray.length)];
        builder.setContentText(title);
        builder.setSmallIcon(R.mipmap.launcher);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.putExtra("category", Categories.CATEGORIE_RECENT_DATA);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

    String titleArray[] = {
            "“If you can dream it, you can do it.” —Walt Disney\n",
            "“The future belongs to those who believe in the beauty of their dreams.” —Eleanor Roosevelt",
            "“Don’t watch the clock; do what it does. Keep going.” —Sam Levenson",
            "“Aim for the moon. If you miss, you may hit a star.” —W. Clement Stone",
            "“Keep your eyes on the stars, and your feet on the ground.” —Theodore Roosevelt",
            "“You just can’t beat the person who never gives up.” —Babe Ruth",
            "“Start where you are. Use what you have. Do what you can.” —Arthur Ashe"
    };



}