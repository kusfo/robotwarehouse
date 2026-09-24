package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.enums.GameAchievement;

public interface AchievementManager {
    void checkAchievement(GameAchievement achievement);
    void checkLevelCompletedRelatedAchievement(int episodeNumber, int levelNumber, boolean firstTimeBeat, GlobalAttributes.LEVEL_TYPE level_type);
    void checkBoxesTeleportRelatedAchievement(int combo);
    void checkPointsRelatedAchievement();
    void checkRobotRelatedAchievement();
    void checkChallengeRelatedAchievement(int currentEpisode);
    void checkEpisodeRelatedAchievement(int currentEpisode);
    void checkStarRelatedAchievement(int currentEpisode);
    void checkUnblockLevelRelatedAchievement(GlobalAttributes.LEVEL_TYPE level_type);
    void checkBoxKillsRelatedAchievement(int robotsSmashed, int ratliensSmashed);
    void pushPendingAchievementsFromPref();
    void executeWaitingAchievements();
}
