package com.teenagemutantninjacoders.robotwarehouse.android.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.teenagemutantninjacoders.robotwarehouse.android.notifications.AndroidNotificationHandler.DISMISS_ACTION;
import static com.teenagemutantninjacoders.robotwarehouse.android.notifications.AndroidNotificationHandler.LATER_ACTION;
import static com.teenagemutantninjacoders.robotwarehouse.android.notifications.AndroidNotificationHandler.NOTIFICATION_ID_EXTRA;


/**
 * Created by JordiM on 12/12/2017.
 */

public class NotificationReceiver extends IntentService {

    public static final String RATE_APP_TIMESTAMP = "rateapptimestamp";
    public static final String RATE_APP_DISABLED = "rateappdisabled";

    public NotificationReceiver() {
        super(NotificationReceiver.class.getSimpleName());
    }

    public NotificationReceiver(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        int notificationId = intent.getIntExtra(NOTIFICATION_ID_EXTRA, 0);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (DISMISS_ACTION.equals(action)) {
            Log.i("NotificationReceiver","Dismiss action received");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(RATE_APP_DISABLED, true);
            editor.commit();
        } else if(LATER_ACTION.equals(action)) {
            Log.i("NotificationReceiver","Later action received");
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(RATE_APP_TIMESTAMP, System.currentTimeMillis());
            editor.commit();
        }
    }
}
