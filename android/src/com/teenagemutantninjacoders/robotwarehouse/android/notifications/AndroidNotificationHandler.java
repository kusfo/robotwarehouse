package com.teenagemutantninjacoders.robotwarehouse.android.notifications;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.teenagemutantninjacoders.robotwarehouse.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.teenagemutantninjacoders.robotwarehouse.android.notifications.NotificationReceiver.RATE_APP_DISABLED;
import static com.teenagemutantninjacoders.robotwarehouse.android.notifications.NotificationReceiver.RATE_APP_TIMESTAMP;

/**
 * Created by JordiM on 12/12/2017.
 */

public class AndroidNotificationHandler {
    private static final int rateNotificationRequestCode = 123;
    public static final String DISMISS_ACTION = "dismiss";
    public static final String LATER_ACTION = "later";
    public static final String NOTIFICATION_ID_EXTRA = "notification_id";
    private static final String RATE_APP_LAPSE_CICLE = "rateapplapsecycle";
    private static final String RATE_APP_LAPSE_TIME = "rateapp_lapse_time";
    private static final String  RATE_APP_LAPSE_SEQUENCE = "rateapp_lapse_sequence";

    private final Activity activity;
    private boolean rateNotificationScheduled;
    private int currentNotificationId;
    private int currentPokeSequence;

    public AndroidNotificationHandler(Activity activity) {
        this.activity = activity;
        rateNotificationScheduled = false;
        currentNotificationId = 0;
        createNotificationChannel();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        currentPokeSequence = preferences.getInt(RATE_APP_LAPSE_CICLE,0);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = activity.getString(R.string.notification_channel_name);
            String description = activity.getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(activity.getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void pokeRateNotification() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        long rateAppLapseSequence = FirebaseRemoteConfig.getInstance().getLong(RATE_APP_LAPSE_SEQUENCE);
        Log.d("AnddNotificationHandler","Poke RateNotification received, currentPokeSequence: " + currentPokeSequence + " rateAppLapseSequence: " + rateAppLapseSequence);
        if(currentPokeSequence > rateAppLapseSequence) {
            boolean rateAppDisabled = preferences.getBoolean(RATE_APP_DISABLED, false);
            long rateAppTimeStamp = preferences.getLong(RATE_APP_TIMESTAMP, 0);
            long rateAppLapseTime = FirebaseRemoteConfig.getInstance().getLong(RATE_APP_LAPSE_TIME);
            Log.d("AnddNotificationHandler","Poke RateNotification evaluating variables -> rateAppDisabled: " + rateAppDisabled + " rateAppTimeStamp: " + rateAppTimeStamp + " rateAppLapseTime: " + rateAppLapseTime);
            if(!rateAppDisabled) {
                long elapsedDays = (System.currentTimeMillis() - rateAppTimeStamp) / (1000L * 60 * 60 *24);
                if(elapsedDays > rateAppLapseTime) {
                    rateNotificationScheduled = true;
                    Log.i("AnddNotificationHandler", "Poke RateNotification: Notification Scheduled.");
                }
            }
            currentPokeSequence = 0;
        } else {
            currentPokeSequence++;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(RATE_APP_LAPSE_CICLE, currentPokeSequence);
        editor.apply();
    }

    public void manageStopNotifications() {
        if (rateNotificationScheduled) {
            createRateGameNotification();
            rateNotificationScheduled = false;
        }
    }

    private void createRateGameNotification() {

        String url = activity.getResources().getString(R.string.playstore_url);

        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent ratePendingIntent =
                PendingIntent.getActivity(activity, 0, rateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissIntent = new Intent(activity, NotificationReceiver.class);
        dismissIntent.setAction(DISMISS_ACTION);
        dismissIntent.putExtra(NOTIFICATION_ID_EXTRA, currentNotificationId);
        PendingIntent dismissPendingIntent = PendingIntent.getService(activity, rateNotificationRequestCode, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent laterIntent = new Intent(activity, NotificationReceiver.class);
        laterIntent.setAction(LATER_ACTION);
        dismissIntent.putExtra(NOTIFICATION_ID_EXTRA, currentNotificationId);
        PendingIntent laterPendingIntent = PendingIntent.getService(activity, rateNotificationRequestCode, laterIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationAcceptBuilder =
                new NotificationCompat.Builder(activity, activity.getString(R.string.default_notification_channel_id))
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(ContextCompat.getColor(activity, R.color.notification_color))
                        .setTicker(activity.getResources().getString(R.string.rate_notification_ticker))
                        .setContentTitle(activity.getResources().getString(R.string.rate_notification_title))
                        .setContentText(activity.getResources().getString(R.string.rate_notification_body))
                        .setContentIntent(ratePendingIntent)
                        .setCategory(NotificationCompat.CATEGORY_PROMO)
                        .setAutoCancel(true)
                        .setLocalOnly(true)
                        .setContentIntent(ratePendingIntent)
                        .addAction(R.drawable.ic_done_white, activity.getResources().getString(R.string.rate_notification_yes_action), ratePendingIntent)
                        .addAction(R.drawable.ic_reject_white, activity.getResources().getString(R.string.rate_notification_no_action), dismissPendingIntent)
                        .addAction(R.drawable.ic_later_white, activity.getResources().getString(R.string.rate_notification_later_action), laterPendingIntent);

        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(currentNotificationId, notificationAcceptBuilder.build());
        currentNotificationId++;
    }
}
