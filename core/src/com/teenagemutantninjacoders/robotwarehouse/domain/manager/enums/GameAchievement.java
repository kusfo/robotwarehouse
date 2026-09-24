package com.teenagemutantninjacoders.robotwarehouse.domain.manager.enums;

public enum GameAchievement {
    NONE(""),
    FIRST_LEVEL_COMPLETED("firstLevelDone"),

    FIRST_ROBOT_KILLED("firstRobotKilled"),
    TEN_ROBOTS_SMASHED_ON_LEVEL("tenRobotsKilledOneLevel"),
    TWENTY_ROBOTS_SMASHED_ON_LEVEL("twentyRobotsKilledOneLevel"),
    THIRTY_ROBOTS_SMASHED_ON_LEVEL("thirtyRobotsKilledOnelevel"), //fiftyRobotsKilledOnelevel

    FIRST_ALL_POINTS_COLLECTED("firstAllFloorPointsCollected"),
    FIRST_POWER_USED("firstPowerUsed"),

    FIRST_EPISODE_COMPLETED("firstEpisodeCompleted"),
    SECOND_EPISODE_COMPLETED("secondEpisodeCompleted"),
    THIRD_EPISODE_COMPLETED("thirdEpisodeCompleted"),
    FOURTH_EPISODE_COMPLETED("fourthEpisodeCompleted"),

    ONE_HUNDRED_BOXES_TELEPORTED("onehundredBoxesTeleported"),
    FIVE_HUNDRED_BOXES_TELEPORTED("fivehundredBoxesTeleported"),
    ONE_THOUSAND_BOXES_TELEPORTED("onethousandBoxesTeleported"),

    FIRST_STAR_LEVEL_UNBLOCKED("firstStarLevelUnblocked"),

    ONE_CHALLENGE_COMPLETED("oneChallengeCompleted"),
    FIVE_CHALLENGE_COMPLETED("fiveChallengeCompleted"),
    TEN_CHALLENGE_COMPLETED("tenChallengeCompleted"),
    FIFTEEN_CHALLENGE_COMPLETED("fifteenChallengeCompleted"),
    ALL_EPISODE_CHALLENGES_COMPLETED("allEpisodeChallengesCompleted"),

    FIRST_EPISODE_ONE_HUNDRED_COMPLETE("firstEpisodeOneHundredCompleted"),
    SECOND_EPISODE_ONE_HUNDRED_COMPLETE("secondEpisodeOneHundredCompleted"),
    THIRD_EPISODE_ONE_HUNDRED_COMPLETE("thirdEpisodeOneHundredCompleted"),
    FOURTH_EPISODE_ONE_HUNDRED_COMPLETE("fourthEpisodeOneHundredCompleted"),

    THREE_ROBOTS_KILLED_ONE_BOX("threeRobotsKilledWithOneBox"),

    ONE_EXPERIMENTAL_LEVEL_COMPLETED("oneExperimentalLevelCompleted"),
    FIVE_EXPERIMENTAL_LEVEL_COMPLETED("fiveExperimentalLevelCompleted"),
    TEN_EXPERIMENTAL_LEVEL_COMPLETED("tenExperimentalLevelCompleted");
    private String value;

    GameAchievement(String newValue) {
        setValue(newValue);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        value = newValue;
    }

    public static GameAchievement fromString(String text) {
        if (text != null) {
            for (GameAchievement var : GameAchievement.values()) {
                if (text.equals(var.getValue())) {
                    return var;
                }
            }
        }
        return NONE;
    }
}
