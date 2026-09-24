package com.teenagemutantninjacoders.robotwarehouse.android.tracking;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TrackingServices;

/**
 * Created by JordiM on 20/12/2017.
 */

public class AndroidFirebaseEventTracker implements TrackingServices {
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
    private FirebaseAnalytics firebaseAnalytics;

    public AndroidFirebaseEventTracker(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void trackScreen(String screenName) {
        Bundle params = new Bundle();
        params.putString(SCREEN_NAME, screenName);
        firebaseAnalytics.logEvent(SCREEN_VIEW, params);
    }

    @Override
    public void trackLevelStart(int levelNumber) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelNumber);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_START, params);
    }

    @Override
    public void trackLevelCompleted(int levelnumber) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelnumber);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, params);
    }

    @Override
    public void trackLevelFailed(int levelNumber) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelNumber);
        firebaseAnalytics.logEvent(LEVEL_FAIL, params);
    }

    @Override
    public void trackLevelRestarted(int levelNumber) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelNumber);
        firebaseAnalytics.logEvent(LEVEL_RESTART, params);
    }

    @Override
    public void trackBoxesTeleported(int levelNumber, int combo, int points) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelNumber);
        params.putInt(BOX_COMBO, combo);
        params.putInt(BOX_POINTS, points);
        firebaseAnalytics.logEvent(BOXES_TELEPORT, params);
    }

    @Override
    public void trackBoxColorDisabled(int levelNumber, String color) {
        Bundle params = new Bundle();
        params.putInt(FirebaseAnalytics.Param.LEVEL, levelNumber);
        params.putString(BOX_COLOR, color);
        firebaseAnalytics.logEvent(COLOR_DISABLE, params);
    }

    @Override
    public void trackUnlockAchivement(String achievementId) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ACHIEVEMENT_ID, achievementId);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT, params);
    }
}
