package com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces;

/**
 * Created by jordimontornes on 07/05/2017.
 */

public interface TrackingServices
{
    void trackScreen(String screenName);
    void trackLevelStart(int levelNumber);
    void trackLevelCompleted(int levelnumber);
    void trackLevelFailed(int currentLevel);
    void trackLevelRestarted(int currentLevel);
    void trackBoxesTeleported(int currentLevel, int combo, int points);
    void trackBoxColorDisabled(int currentLevel, String color);
    void trackUnlockAchivement(String achievementId);
}