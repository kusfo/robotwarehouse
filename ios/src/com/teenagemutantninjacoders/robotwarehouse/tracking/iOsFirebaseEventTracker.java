package com.teenagemutantninjacoders.robotwarehouse.tracking;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TrackingServices;

import org.robovm.apple.foundation.NSDecimal;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.foundation.NSString;
import org.robovm.pods.firebase.analytics.FIRAnalytics;
import org.robovm.pods.firebase.analytics.FIREvents;
import org.robovm.pods.firebase.analytics.FIRParameters;

import java.util.HashMap;

public class iOsFirebaseEventTracker implements TrackingServices {
    //events
    private static final String SCREEN_VIEW = "screen_view";
    private static final String LEVEL_FAIL = "level_fail";
    private static final String LEVEL_RESTART = "level_restart";
    //attributes
    private static final String BOXES_TELEPORT = "boxes_teleport";
    private static final String COLOR_DISABLE = "color_disable";
    private static final String SCREEN_NAME = "screen_name";
    private static final String BOX_COMBO = "box_combo";
    private static final String BOX_POINTS = "box_points";
    private static final String BOX_COLOR = "box_color";

    @Override
    public void trackScreen(String screenName) {
        HashMap<NSString,NSString> params = new HashMap<>();
        params.put(new NSString(SCREEN_NAME),new NSString(screenName));
        FIRAnalytics.logEvent(SCREEN_VIEW, new NSDictionary<NSString, NSString>(params));
    }

    @Override
    public void trackLevelStart(int levelNumber) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        FIRAnalytics.logEvent(FIREvents.LevelStart,new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackLevelCompleted(int levelNumber) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        FIRAnalytics.logEvent(FIREvents.LevelEnd,new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackLevelFailed(int levelNumber) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        FIRAnalytics.logEvent(LEVEL_FAIL,new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackLevelRestarted(int levelNumber) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        FIRAnalytics.logEvent(LEVEL_RESTART,new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackBoxesTeleported(int levelNumber, int combo, int points) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        params.put(new NSString(BOX_COMBO), NSNumber.valueOf(combo));
        params.put(new NSString(BOX_POINTS), NSNumber.valueOf(points));
        FIRAnalytics.logEvent(BOXES_TELEPORT, new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackBoxColorDisabled(int levelNumber, String color) {
        HashMap<NSString, NSNumber> params = new HashMap<>();
        params.put(new NSString(FIRParameters.Level), NSNumber.valueOf(levelNumber));
        params.put(new NSString(BOX_COLOR), NSNumber.valueOf(GlobalAttributes.COLOR.fromString(color).getIntValue()));
        FIRAnalytics.logEvent(COLOR_DISABLE, new NSDictionary<NSString, NSNumber>(params));
    }

    @Override
    public void trackUnlockAchivement(String achievementId) {
        HashMap<NSString,NSString> params = new HashMap<>();
        params.put(new NSString(FIRParameters.AchievementID),new NSString(achievementId));
        FIRAnalytics.logEvent(FIREvents.UnlockAchievement, new NSDictionary<NSString, NSString>(params));
    }
}
