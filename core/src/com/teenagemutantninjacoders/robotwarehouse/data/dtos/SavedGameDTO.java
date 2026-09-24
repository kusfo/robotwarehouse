package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import com.badlogic.gdx.utils.Json;

import java.util.List;

import static com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants.CONTENT_STATUS_COMPLETED;

public class SavedGameDTO {
    public static final int NUM_EPISODES = 20;
    public static final int NUM_LEVELS = 40;
    public long savedGameTime = 0;
    public String deviceID = "";
    public int savedGameVersion = 1;
    public int levelStars[][];
    public int episodeStars[];
    public int totalstars;
    public int levelScore[][];
    public int episodeStatus[];
    public int episodeScore[];
    public int totalscore;
    public boolean levelChallengeStatus[][];
    public int episodeChallenges[];
    public int totalChallenges;
    public int levelStatus[][];
    public boolean levelActualized[][];
    public String starLevelProgress[][];
    public String[] dinamicPreferences;
    //achievements
    public int totalRobotsKilled;
    public int totalBoxesTeleported;
    public int totalSpecialChallengeLevelsCompleted;
    public int totalStarLevelsUnblocked;

    public SavedGameDTO() {

    }
    
    public SavedGameDTO(long savedGameTime, String deviceID, int savedGameVersion, int[][] levelStars, int[] episodeStars, int totalstars, int[][] levelScore, int[] episodeStatus,
                        int[] episodeScore, int totalscore, boolean[][] levelChallengeStatus, int[] episodeChallenges,
                        int totalChallenges, int[][] levelStatus, boolean[][] levelActualized, String[][] starLevelProgress, String[] dinamicPreferences,
                        int totalRobotsKilled, int totalBoxesTeleported, int totalSpecialChallengeLevelsCompleted, int totalStarLevelsUnblocked) {
        this.savedGameTime = savedGameTime;
        this.deviceID = deviceID;
        this.savedGameVersion = savedGameVersion;
        this.levelStars = levelStars;
        this.episodeStars = episodeStars;
        this.totalstars = totalstars;
        this.levelScore = levelScore;
        this.episodeStatus = episodeStatus;
        this.episodeScore = episodeScore;
        this.totalscore = totalscore;
        this.levelChallengeStatus = levelChallengeStatus;
        this.episodeChallenges = episodeChallenges;
        this.totalChallenges = totalChallenges;
        this.levelStatus = levelStatus;
        this.levelActualized = levelActualized;
        this.starLevelProgress = starLevelProgress;
        this.dinamicPreferences = dinamicPreferences;
        this.totalRobotsKilled = totalRobotsKilled;
        this.totalBoxesTeleported = totalBoxesTeleported;
        this.totalSpecialChallengeLevelsCompleted = totalSpecialChallengeLevelsCompleted;
        this.totalStarLevelsUnblocked = totalStarLevelsUnblocked;
    }

    public static SavedGameDTO parse(String jsonSavedGamed) {
        Json json = new Json();
        SavedGameDTO savedGameDTO = json.fromJson(SavedGameDTO.class, jsonSavedGamed);
        return savedGameDTO;
    }

    public String convertoToJson() {
        Json json = new Json();
        String jsonSavedGame = json.toJson(this, SavedGameDTO.class);
        return jsonSavedGame;
    }

    public long computeAdvance() {
        long advance = 0;
        for(int episodeIndex = 0;episodeIndex<NUM_EPISODES;episodeIndex++) {
            if(episodeStatus[episodeIndex] == CONTENT_STATUS_COMPLETED) {
                advance = advance + 100000;
            }
            for(int levelIndex = 0; levelIndex < NUM_LEVELS;levelIndex++) {
                if(levelStatus[episodeIndex][levelIndex] == CONTENT_STATUS_COMPLETED) {
                    advance = advance + 1000;
                }
            }
        }
        advance = advance + totalscore;
        return advance;
    }

    public static class Builder {
        long savedGameTime;
        String deviceID;
        int savedGameVersion;
        int levelStars[][];
        int episodeStars[];
        int totalstars;
        int levelScore[][];
        int episodeStatus[];
        int episodeScore[];
        int totalscore;
        boolean levelChallengeStatus[][];
        int episodeChallenges[];
        int totalChallenges;
        int levelStatus[][];
        boolean levelActualized[][];
        String starLevelProgress[][];
        private List<String> dinamicPreferences;
        //achievements
        int totalRobotsKilled;
        int totalBoxesTeleported;
        int totalSpecialChallengeLevelsCompleted;
        int totalStarLevelsUnblocked;

        public Builder() {
            savedGameTime = 0;
            deviceID = "";
            savedGameVersion = 1;
            levelStars = new int[NUM_EPISODES+1][NUM_LEVELS+1];
            episodeStars = new int[NUM_EPISODES+1];
            totalstars = 0;
            levelScore = new int[NUM_EPISODES+1][NUM_LEVELS+1];
            episodeStatus = new int[NUM_EPISODES+1];
            episodeScore = new int[NUM_EPISODES+1];
            totalscore = 0;
            levelChallengeStatus = new boolean[NUM_EPISODES+1][NUM_LEVELS+1];
            episodeChallenges = new int[NUM_EPISODES+1];
            totalChallenges = 0;
            levelStatus = new int[NUM_EPISODES+1][NUM_LEVELS+1];
            levelActualized = new boolean[NUM_EPISODES+1][NUM_LEVELS+1];
            starLevelProgress = new String[NUM_EPISODES+1][NUM_LEVELS+1];
            totalRobotsKilled = 0;
            totalBoxesTeleported = 0;
            totalSpecialChallengeLevelsCompleted = 0;
            totalStarLevelsUnblocked = 0;
        }

        public Builder setSavedGameTime(long savedGameTime) {
            this.savedGameTime = savedGameTime;
            return this;
        }

        public Builder setDeviceId(String deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public Builder setSavedGameVersion(int savedGameVersion) {
            this.savedGameVersion = savedGameVersion;
            return this;
        }

        public Builder setLevelStar(int numEpisode, int numLevel, int starNumber) {
            this.levelStars[numEpisode][numLevel] = starNumber;
            return this;
        }

        public Builder setEpisodeStar(int numEpisode, int starNumber) {
            this.episodeStars[numEpisode] = starNumber;
            return this;
        }

        public Builder setTotalStars(int starNumber) {
            this.totalstars = starNumber;
            return this;
        }

        public Builder setLevelScore(int numEpisode, int numLevel, int score) {
            this.levelScore[numEpisode][numLevel] = score;
            return this;
        }

        public Builder setEpisodeStatus(int numEpisode, int status) {
            this.episodeStatus[numEpisode] = status;
            return this;
        }

        public Builder setEpisodeScore(int numEpisode, int score) {
            this.episodeScore[numEpisode] = score;
            return this;
        }

        public Builder setTotalScore(int totalScore) {
            this.totalscore = totalScore;
            return this;
        }

        public Builder setLevelChallengeStatus(int numEpisode, int numLevel, boolean levelChallengeStatus) {
            this.levelChallengeStatus[numEpisode][numLevel] = levelChallengeStatus;
            return this;
        }

        public Builder setEpisodeChallenges(int numEpisode, int challenges) {
            this.episodeChallenges[numEpisode] = challenges;
            return this;
        }

        public Builder setTotalChallenges(int totalChallenges) {
            this.totalChallenges = totalChallenges;
            return this;
        }

        public Builder setLevelStatus(int numEpisode, int numLevel, int status) {
            this.levelStatus[numEpisode][numLevel] = status;
            return this;
        }

        public Builder setLevelActualized(int numEpisode, int numLevel, boolean actualized) {
            this.levelActualized[numEpisode][numLevel] = actualized;
            return this;
        }

        public Builder setLevelProgress(int numEpisode, int numLevel, String levelProgress) {
            this.starLevelProgress[numEpisode][numLevel] = levelProgress;
            return  this;
        }

        public Builder setDinamicPreferences(List<String> dinamicPreferences) {
            this.dinamicPreferences = dinamicPreferences;
            return this;
        }

        //achievements
        public Builder setTotalRobotsKilled(int totalRobotsKilled) {
            this.totalRobotsKilled = totalRobotsKilled;
            return this;
        }
        public Builder setTotalBoxesTeleported(int totalBoxesTeleported) {
            this.totalBoxesTeleported = totalBoxesTeleported;
            return this;
        }

        public Builder setTotalSpecialChallengeLevelsCompleted(int totalSpecialChallengeLevelsCompleted) {
            this.totalSpecialChallengeLevelsCompleted = totalSpecialChallengeLevelsCompleted;
            return this;
        }

        public Builder setTotalStarLevelsUnblocked(int totalStarLevelsUnblocked) {
            this.totalStarLevelsUnblocked = totalStarLevelsUnblocked;
            return this;
        }

        public SavedGameDTO build() {
            return new SavedGameDTO(this.savedGameTime, this.deviceID, this.savedGameVersion, this.levelStars, this.episodeStars, this.totalstars, this.levelScore, this.episodeStatus,
            this.episodeScore, this.totalscore, this.levelChallengeStatus, this.episodeChallenges,
            this.totalChallenges, this.levelStatus, this.levelActualized, this.starLevelProgress, this.dinamicPreferences.toArray(new String[0]),
                    this.totalRobotsKilled, this.totalBoxesTeleported, this.totalSpecialChallengeLevelsCompleted, this.totalStarLevelsUnblocked);
        }
    }
}
