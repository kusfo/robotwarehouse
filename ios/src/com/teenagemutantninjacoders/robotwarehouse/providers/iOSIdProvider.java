package com.teenagemutantninjacoders.robotwarehouse.providers;

import com.teenagemutantninjacoders.robotwarehouse.ads.iOSAdHandler;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;
import com.teenagemutantninjacoders.robotwarehouse.notifications.iOSNotificationHandler;

import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;

public class iOSIdProvider implements IdProvider {

    private final NSDictionary<NSString, NSString> achievementID;
    private final NSDictionary<NSString, NSString> trackingID;

    public iOSIdProvider() {
        achievementID = new NSDictionary<>();
        achievementID.setAssociatedObject(new NSString("firstLevelDone"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstRobotKilled"), "whatever");
        achievementID.setAssociatedObject(new NSString("tenRobotsKilledOneLevel"), "whatever");
        achievementID.setAssociatedObject(new NSString("twentyRobotsKilledOneLevel"), "whatever");
        achievementID.setAssociatedObject(new NSString("thirtyRobotsKilledOnelevel"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstAllFloorPointsCollected"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstPowerUsed"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstEpisodeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("secondEpisodeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("thirdEpisodeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("fourthEpisodeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("onehundredBoxesTeleported"), "whatever");
        achievementID.setAssociatedObject(new NSString("fivehundredBoxesTeleported"), "whatever");
        achievementID.setAssociatedObject(new NSString("onethousandBoxesTeleported"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstStarLevelUnblocked"), "whatever");
        achievementID.setAssociatedObject(new NSString("oneChallengeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("fiveChallengeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("tenChallengeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("fifteenChallengeCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("allEpisodeChallengesCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("firstEpisodeOneHundredCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("secondEpisodeOneHundredCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("thirdEpisodeOneHundredCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("fourthEpisodeOneHundredCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("threeRobotsKilledWithOneBox"), "whatever");
        achievementID.setAssociatedObject(new NSString("oneExperimentalLevelCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("fiveExperimentalLevelCompleted"), "whatever");
        achievementID.setAssociatedObject(new NSString("tenExperimentalLevelCompleted"), "whatever");
        trackingID = new NSDictionary<>();
        trackingID.setAssociatedObject(new NSString("firstLevelDoneAchievement"), "FirstLevelDoneTrack");
        trackingID.setAssociatedObject(new NSString("firstRobotKilledAchievement"), "FirstRobotKilledTrack");
        trackingID.setAssociatedObject(new NSString("tenRobotsKilledOneLevelAchievement"), "tenRobotsKilledOneLevelTrack");
        trackingID.setAssociatedObject(new NSString("twentyRobotsKilledOneLevelAchievement"), "twentyRobotsKilledOneLevelTrack");
        trackingID.setAssociatedObject(new NSString("thirtyRobotsKilledOnelevelAchievement"), "thirtyRobotsKilledOnelevelTrack");
        trackingID.setAssociatedObject(new NSString("firstAllFloorPointsCollectedAchievement"), "firstAllFloorPointsCollectedTrack");
        trackingID.setAssociatedObject(new NSString("firstPowerUsed"), "firstPowerUsedTrack");
        trackingID.setAssociatedObject(new NSString("firstEpisodeCompletedAchievement"), "firstEpisodeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("secondEpisodeCompletedAchievement"), "secondEpisodeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("thirdEpisodeCompletedAchievement"), "thirdEpisodeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("fourthEpisodeCompletedAchievement"), "fourthEpisodeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("onehundredBoxesTeleportedAchievement"), "onehundredBoxesTeleportedTrack");
        trackingID.setAssociatedObject(new NSString("fivehundredBoxesTeleportedAchievement"), "fivehundredBoxesTeleportedTrack");
        trackingID.setAssociatedObject(new NSString("onethousandBoxesTeleportedAchievement"), "onethousandBoxesTeleportedTrack");
        trackingID.setAssociatedObject(new NSString("firstStarLevelUnblockedAchievement"), "firstStarLevelUnblockedTrack");
        trackingID.setAssociatedObject(new NSString("oneChallengeCompletedAchievement"), "oneChallengeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("fiveChallengeCompletedAchievement"), "fiveChallengeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("tenChallengeCompletedAchievement"), "tenChallengeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("fifteenChallengeCompletedAchievement"), "fifteenChallengeCompletedTrack");
        trackingID.setAssociatedObject(new NSString("allEpisodeChallengesCompletedAchievement"), "allEpisodeChallengesCompletedTrack");
        trackingID.setAssociatedObject(new NSString("firstEpisodeOneHundredCompletedAchievement"), "firstEpisodeOneHundredCompletedTrack");
        trackingID.setAssociatedObject(new NSString("secondEpisodeOneHundredCompletedAchievement"), "secondEpisodeOneHundredCompletedTrack");
        trackingID.setAssociatedObject(new NSString("thirdEpisodeOneHundredCompletedAchievement"), "thirdEpisodeOneHundredCompletedTrack");
        trackingID.setAssociatedObject(new NSString("fourthEpisodeOneHundredCompletedAchievement"), "fourthEpisodeOneHundredCompletedTrack");
        trackingID.setAssociatedObject(new NSString("threeRobotsKilledWithOneBoxAchievement"), "threeRobotsKilledWithOneBoxTrack");
        trackingID.setAssociatedObject(new NSString("oneExperimentalLevelCompletedAchievement"), "oneExperimentalLevelCompletedTrack");
        trackingID.setAssociatedObject(new NSString("fiveExperimentalLevelCompletedAchievement"), "fiveExperimentalLevelCompletedTrack");
        trackingID.setAssociatedObject(new NSString("tenExperimentalLevelCompletedAchievement"), "tenExperimentalLevelCompletedTrack");
    }

    @Override
    public String getAchievementId(String achievement) {
        return achievementID.getString(achievement);
    }

    @Override
    public String getTrackingId(String event) {
        return trackingID.getString(event);
    }
}
