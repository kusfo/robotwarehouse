package com.teenagemutantninjacoders.robotwarehouse.notifications;

import org.robovm.apple.foundation.Foundation;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.storekit.SKStoreReviewController;

public class iOSNotificationHandler {
    public static final String RATE_APP_LAPSE_CICLE = "rateapplapsecycle";
    public static final String RATE_APP_LAPSE_TIME = "rateapp_lapse_time";
    public static final String  RATE_APP_LAPSE_SEQUENCE = "rateapp_lapse_sequence";
    public static final String  RATE_APP_VERSION = "rateapp_version";
    public static final String RATE_APP_TIMESTAMP = "rateapptimestamp";

    private int currentPokeSequence;

    public iOSNotificationHandler() {
        NSUserDefaults standardUserDefaults = NSUserDefaults.getStandardUserDefaults();
        currentPokeSequence = standardUserDefaults.getInt(RATE_APP_LAPSE_CICLE);
    }

    public void pokeRateNotification() {
        NSUserDefaults standardUserDefaults = NSUserDefaults.getStandardUserDefaults();
        long rateAppLapseSequence = standardUserDefaults.getLong(RATE_APP_LAPSE_SEQUENCE); //FirebaseRemoteConfig.getInstance().getLong(RATE_APP_LAPSE_SEQUENCE)//MOVE to xml or something (or check FireBseRemoteConfig)
        Foundation.log("iOSNotificationHandler: Poke RateNotification received, currentPokeSequence: " + currentPokeSequence + " rateAppLapseSequence: " + rateAppLapseSequence);
        if(currentPokeSequence > rateAppLapseSequence) {
            String rateAppVersion = standardUserDefaults.getString(RATE_APP_VERSION);
            NSDictionary infoDictionary = NSBundle.getMainBundle().getInfoDictionary();
            String currentVersion = infoDictionary.get(new NSString("CFBundleShortVersionString")).toString();
            long rateAppTimeStamp = standardUserDefaults.getLong(RATE_APP_TIMESTAMP);
            long rateAppLapseTime = standardUserDefaults.getLong(RATE_APP_LAPSE_TIME);//FirebaseRemoteConfig.getInstance().getLong(RATE_APP_LAPSE_TIME);//only 3 times a year
            Foundation.log("iOSNotificationHandler: Poke RateNotification evaluating variables -> rateAppVersion: " + rateAppVersion + " rateAppTimeStamp: " + rateAppTimeStamp + " rateAppLapseTime: " + rateAppLapseTime);
            if(!rateAppVersion.equals(currentVersion)) {
                long elapsedDays = (System.currentTimeMillis() - rateAppTimeStamp) / (1000L * 60 * 60 *24);
                if(elapsedDays > rateAppLapseTime) {
                    SKStoreReviewController.requestReview();
                    Foundation.log("iOSNotificationHandler: Poke RateNotification: Notification Scheduled.");
                    standardUserDefaults.put(RATE_APP_TIMESTAMP, System.currentTimeMillis());
                    standardUserDefaults.put(RATE_APP_VERSION, currentVersion);
                }
            }
            currentPokeSequence = 0;
        } else {
            currentPokeSequence++;
        }
        standardUserDefaults.put(RATE_APP_LAPSE_CICLE, currentPokeSequence);
        standardUserDefaults.synchronize();
    }
}
