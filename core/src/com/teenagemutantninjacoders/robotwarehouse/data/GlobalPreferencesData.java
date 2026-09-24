package com.teenagemutantninjacoders.robotwarehouse.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.SavedGameDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.LevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.parsers.LevelParser;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.System.currentTimeMillis;

/**
 * Created by jordimontornes on 18/02/2017.
 */

public class
GlobalPreferencesData {

    private static final String ROBOT_WAREHOUSE_PREFS = "robot_warehouse_prefs";
    //private static final String ROBOT_WAREHOUSE_PREFS = "robot_warehouse_dev_prefs"; // Debug Development
    private static final String DEVICE_ID = "device_id";
    private static final String CONFIG_SAVE_ENABLED = "save_enabled";
    private static final String SAVED_GAME_TIME = "saved_game_time";
    private static final String CONFIG_SOUND_ENABLED = "sound_enabled";
    private static final String CONFIG_MUSIC_VOLUME_STATE = "music_volume";
    private static final String LEVEL_STARS = "level_stars_";
    private static final String EPISODE_STARS = "episode_stars_";
    private static final String TOTAL_STARS = "total_stars";
    private static final String LEVEL_SCORE = "level_score_";
    private static final String EPISODE_STATUS = "episode_status_";
    private static final String EPISODE_SCORE = "episode_score_";
    private static final String TOTAL_SCORE = "total_score";
    private static final String LEVEL_CHALLENGE_STATUS = "level_challenge_status_";
    private static final String EPISODE_CHALLENGES = "episode_challenges_";
    private static final String TOTAL_CHALLENGES = "total_challenges";
    private static final String LEVEL_STATUS = "level_status_";
    private static final String LEVEL_ACTUALIZED = "level_actualized_";
    private static final String STAR_LEVEL_PROGRESS = "starLevel_progress_";
    private static final String SAVED_GAME_VERSION = "saved_game_version";

    //Achievements section
    private static final String ROBOTS_KILLED = "robots_killed";
    private static final String BOXES_TELEPORTED = "boxes_teleported";
    private static final String SPECIAL_CHALLENGE_LEVELS_COMPLETED = "special_challenge_levels_completed";
    private static final String STAR_LEVELS_UNBLOCKED = "star_levels_unblocked";

    private boolean gameSoundEnabled = true;
    private boolean cloudSaveEnabled = true;
    private boolean cloudSaveSynced = false;

    private static GlobalPreferencesData instance;
    private Preferences preferences;

    public GlobalPreferencesData() {
        preferences = Gdx.app.getPreferences(ROBOT_WAREHOUSE_PREFS);
    }

    public Preferences getPreferences(){
        return preferences;
    }
    public static GlobalPreferencesData getInstance() {
        if(instance == null) {
            instance = new GlobalPreferencesData();
        }
        return instance;
    }

    public int getSavedGameVersion() {
        return preferences.getInteger(SAVED_GAME_VERSION, GameConstants.SAVED_GAME_VERSION);//Valor por defecto si no existe es el current save_game_version
    }

    public void setSavedGameVersion(int newVersion, boolean flushed) {
        preferences.putInteger(SAVED_GAME_VERSION, newVersion);
        if(flushed)
            preferences.flush();
    }

    public void setSavedGameVersion(int newVersion){
        preferences.putInteger(SAVED_GAME_VERSION, newVersion);
        preferences.flush();
    }

    public void setDeviceID(String deviceID, boolean flushed) {
        preferences.putString(DEVICE_ID,deviceID);
        if(flushed)
            preferences.flush();
    }

    public void setDeviceID(String deviceID) {
        setDeviceID(deviceID, true);
    }

    public String getDeviceId() {
        return preferences.getString(DEVICE_ID, "");
    }

    public void setSavedGameTime(long savedGameTime, boolean flushed) {
        preferences.putLong(SAVED_GAME_TIME,savedGameTime);
        if(flushed)
            preferences.flush();
    }

    public void setSavedGameTime(boolean flushed) {
        preferences.putLong(SAVED_GAME_TIME,currentTimeMillis());
        if(flushed)
            preferences.flush();
    }

    public void setSavedGameTime() {
        setSavedGameTime(true);
    }

    public void setSavedGameTime(long savedGameTime) {
        setSavedGameTime(savedGameTime, true);
    }

    public long getSavedGameTime() {
        return preferences.getLong(SAVED_GAME_TIME);
    }

    public int getLevelScore(int numLevel, int numEpisode) {
        return preferences.getInteger(LEVEL_SCORE + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), 0);
    }

    public void saveLevelScore(int numLevel, int numEpisode, int score, boolean flushed) {
        preferences.putInteger(LEVEL_SCORE + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), score);
        if(flushed)
            preferences.flush();
    }

    public void saveLevelScore(int numLevel, int numEpisode, int score) {
        saveLevelScore(numLevel, numEpisode, score, true);
    }

    public int getLevelStars(int numLevel, int numEpisode) {
        return preferences.getInteger(LEVEL_STARS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), 0);
    }

    public void setLevelStars(int numLevel, int numEpisode, int numStars, boolean flushed) {
        preferences.putInteger(LEVEL_STARS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), numStars);
        if(flushed)
            preferences.flush();
    }

    public void setLevelStars(int numLevel, int numEpisode, int numStars) {
        setLevelStars(numLevel, numEpisode, numStars, true);
    }

    public int getEpisodeStars(int numEpisode){
        return preferences.getInteger(EPISODE_STARS + Integer.toString(numEpisode), 0);
    }

    public void addStarsToEpisode(int numEpisode, int starsAdded, boolean flushed){
        preferences.putInteger(EPISODE_STARS + Integer.toString(numEpisode), getEpisodeStars(numEpisode) + starsAdded);
        if(flushed)
            preferences.flush();
    }

    public void addStarsToEpisode(int numEpisode, int starsAdded){
        addStarsToEpisode(numEpisode, starsAdded, true);
    }

    public void setEpisodeStars(int numEpisode, int numStars, boolean flushed) {
        preferences.putInteger(EPISODE_STARS + Integer.toString(numEpisode), numStars);
        if(flushed)
            preferences.flush();
    }

    public void setEpisodeStars(int numEpisode, int numStars) {
        setEpisodeStars(numEpisode, numStars, true);
    }

    public int getEpisodeChallengesCompleted(int numEpisode){
        return preferences.getInteger(EPISODE_CHALLENGES + Integer.toString(numEpisode), 0);
    }

    public void addChallengeCompletedToEpisode(int numEpisode, boolean flushed){
        preferences.putInteger(EPISODE_CHALLENGES + Integer.toString(numEpisode), getEpisodeChallengesCompleted(numEpisode) + 1);
        if(flushed)
            preferences.flush();
    }

    public void addChallengeCompletedToEpisode(int numEpisode){
        addChallengeCompletedToEpisode(numEpisode, true);
    }


    public void setEpisodeChallengesCompleted(int numEpisode, int numChallenges, boolean flushed) {
        preferences.putInteger(EPISODE_CHALLENGES + Integer.toString(numEpisode), numChallenges);
        if(flushed)
            preferences.flush();
    }

    public void setEpisodeChallengesCompleted(int numEpisode, int numChallenges) {
        setEpisodeChallengesCompleted(numEpisode, numChallenges, true);
    }

    public int getLevelStatus(int numLevel, int numEpisode){
        return preferences.getInteger(LEVEL_STATUS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), 0);
    }

    public void setLevelStatus(int numLevel, int numEpisode, int statusNumber, boolean flushed){
        preferences.putInteger(LEVEL_STATUS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), statusNumber);
        if(flushed)
            preferences.flush();
    }

    public void setLevelStatus(int numLevel, int numEpisode, int statusNumber){
        setLevelStatus(numLevel, numEpisode, statusNumber, true);
    }

    public boolean isLevelActualized(int numLevel, int numEpisode){
        return preferences.getBoolean(LEVEL_ACTUALIZED + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), false);
    }

    public void setLevelActualized(int numLevel, int numEpisode, boolean isActualized, boolean flushed){
        preferences.putBoolean(LEVEL_ACTUALIZED + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), isActualized);
        if(flushed)
            preferences.flush();
    }

    public void setLevelActualized(int numLevel, int numEpisode, boolean isActualized){
        setLevelActualized(numLevel, numEpisode, isActualized, true);
    }

    public void removeLevelActualized(int numLevel, int numEpisode){
        preferences.remove(LEVEL_ACTUALIZED + numEpisode + "_" + numLevel);
        preferences.flush();
    }

    public int getEpisodeStatus(int numEpisode){
        return preferences.getInteger(EPISODE_STATUS + Integer.toString(numEpisode), 0);
    }

    public void setEpisodeStatus(int numEpisode, int newStatus, boolean flushed){
        preferences.putInteger(EPISODE_STATUS + Integer.toString(numEpisode), newStatus);
        if(flushed)
            preferences.flush();
    }

    public void setEpisodeStatus(int numEpisode, int newStatus){
        setEpisodeStatus(numEpisode, newStatus, true);
    }

    public int getTotalStars() {
        return preferences.getInteger(TOTAL_STARS, 0);
    }

    public void addStarsToTotal(int numStars, boolean flushed) {
        preferences.putInteger(TOTAL_STARS, getTotalStars() + numStars);
        if(flushed)
            preferences.flush();
    }

    public void addStarsToTotal(int numStars) {
        addStarsToTotal(numStars, true);
    }

    public void setTotalStars(int numStars, boolean flushed) {
        preferences.putInteger(TOTAL_STARS, numStars);
        if(flushed)
            preferences.flush();
    }

    public void setTotalStars(int numStars) {
        setTotalStars(numStars, true);
    }

    public int getEpisodeScore(int numEpisode) {
        return preferences.getInteger(EPISODE_SCORE + Integer.toString(numEpisode), 0);
    }

    public void addScoreToEpisode(int numEpisode, int score, boolean flushed) {
        preferences.putInteger(EPISODE_SCORE + Integer.toString(numEpisode), getEpisodeScore(numEpisode) + score);
        if(flushed)
            preferences.flush();
    }

    public void addScoreToEpisode(int numEpisode, int score) {
        addScoreToEpisode(numEpisode, score, true);
    }

    public void setEpisodeScore(int numEpisode, int score, boolean flushed) {
        preferences.putInteger(EPISODE_SCORE + Integer.toString(numEpisode), score);
        if(flushed)
            preferences.flush();
    }

    public void setEpisodeScore(int numEpisode, int score) {
        setEpisodeScore(numEpisode, score, true);
    }

    public int getTotalScore() {
        return preferences.getInteger(TOTAL_SCORE, 0);
    }

    public void addScoreToTal(int score, boolean flushed) {
        preferences.putInteger(TOTAL_SCORE, getTotalScore() + score);
        if(flushed)
            preferences.flush();
    }

    public void addScoreToTal(int score) {
        addScoreToTal(score, true);
    }

    public void setTotalScore(int score, boolean flushed) {
        preferences.putInteger(TOTAL_SCORE, score);
        if(flushed)
            preferences.flush();
    }

    public void setTotalScore(int score) {
        setTotalScore(score, true);
    }

    public int getTotalChallengesCompleted() {
        return preferences.getInteger(TOTAL_CHALLENGES, 0);
    }

    public void addChallengeToTotalCompleted(boolean flushed) {
        preferences.putInteger(TOTAL_CHALLENGES, getTotalChallengesCompleted() + 1);
        if(flushed)
            preferences.flush();
    }

    public void addChallengeToTotalCompleted() {
        addChallengeToTotalCompleted(true);
    }

    public void setTotalChallengesCompleted(int totalChallengesCompleted, boolean flushed) {
        preferences.putInteger(TOTAL_CHALLENGES, totalChallengesCompleted);
        if(flushed)
            preferences.flush();
    }

    public void setTotalChallengesCompleted(int totalChallengesCompleted) {
        setTotalChallengesCompleted(totalChallengesCompleted, true);
    }

    public void saveLevelChallengeCompletion(int numLevel, int numEpisode, boolean flushed){
        preferences.putBoolean(LEVEL_CHALLENGE_STATUS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), true);
        if(flushed)
            preferences.flush();
    }

    public void saveLevelChallengeCompletion(int numLevel, int numEpisode){
        saveLevelChallengeCompletion(numLevel, numEpisode, true);
    }
    public boolean getLevelChallengeStatus(int numLevel, int numEpisode){
        return preferences.getBoolean(LEVEL_CHALLENGE_STATUS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), false);
    }

    public String getStarLevelProgress(int numLevel, int numEpisode){
        return preferences.getString(STAR_LEVEL_PROGRESS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), "");
    }

    public void setStarLevelProgress(int numLevel, int numEpisode, String progress, boolean flushed){
        preferences.putString(STAR_LEVEL_PROGRESS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), progress);
        if(flushed)
            preferences.flush();
    }

    public void setStarLevelProgress(int numLevel, int numEpisode, String progress){
        setStarLevelProgress(numLevel, numEpisode, progress, true);
    }

    public void setStarLevelProgress(int numLevel, int numEpisode, ArrayList<Integer> savedLevelsStars, boolean flushed){
        String progress = mountLevelStarProgressKey(savedLevelsStars);
        preferences.putString(STAR_LEVEL_PROGRESS + Integer.toString(numEpisode) + "_" + Integer.toString(numLevel), progress);
        if(flushed)
            preferences.flush();
    }

    public void setStarLevelProgress(int numLevel, int numEpisode, ArrayList<Integer> savedLevelStars){
        setStarLevelProgress(numLevel, numEpisode, savedLevelStars, true);
    }

    public void checkGamePreferences(){
        gameSoundEnabled = preferences.getBoolean(CONFIG_SOUND_ENABLED, true);
        cloudSaveEnabled = preferences.getBoolean(CONFIG_SAVE_ENABLED, true);
    }

    public boolean isCloudSaveEnabled() {
        return cloudSaveEnabled;
    }

    public void setCloudSaveEnabled(boolean saveEnabled) {
        this.cloudSaveEnabled = saveEnabled;
        preferences.putBoolean(CONFIG_SAVE_ENABLED, saveEnabled);
        preferences.flush();
    }

    public boolean isCloudSaveSynced(){
        return cloudSaveSynced;
    }

    public void setCloudSaveSynced(boolean synced){
        cloudSaveSynced = synced;
    }

    public boolean isGameSoundEnabled() {
        return gameSoundEnabled;
    }

    public void setGameSoundEnabled(boolean gameSoundEnabled) {
        this.gameSoundEnabled = gameSoundEnabled;
        preferences.putBoolean(CONFIG_SOUND_ENABLED, gameSoundEnabled);
        preferences.flush();
    }

    public int getMusicVolumeState() {
        return preferences.getInteger(CONFIG_MUSIC_VOLUME_STATE, 3);
    }

    public void setMusicVolumeState(int newVolume){
        preferences.putInteger(CONFIG_MUSIC_VOLUME_STATE, newVolume);
        preferences.flush();
    }

    public void setLevelIntroductionEventViewCompleted(String eventRef, boolean flushed){
        preferences.putBoolean(eventRef, true);
        if(flushed)
            preferences.flush();
    }

    public void setLevelIntroductionEventViewCompleted(String eventRef){
        setLevelIntroductionEventViewCompleted(eventRef, true);
    }

    public boolean getLevelIntroductionEventViewStatus(String eventRef) {
        return preferences.getBoolean(eventRef, false);
    }

    public void setDialogueViewCompleted(String dialogueName, boolean flushed){
        preferences.putBoolean(dialogueName, true);
        if(flushed)
            preferences.flush();
    }

    public void setDialogueViewCompleted(String dialogueName){
        setDialogueViewCompleted(dialogueName, true);
    }

    public boolean getDialogueViewStatus(String dialogueName){
        return preferences.getBoolean(dialogueName, false);
    }
//Achievement section
    public void addRobotKilled(boolean flushed) {
        preferences.putInteger(ROBOTS_KILLED, getRobotsKilled()+1);
        if(flushed)
            preferences.flush();
    }

    public void addRobotKilled() {
        addRobotKilled(false);
    }

    public void setRobotsKilled(int robotsKilled, boolean flushed) {
        preferences.putInteger(ROBOTS_KILLED, robotsKilled);
        if(flushed)
            preferences.flush();
    }

    public void setRobotsKilled(int robotsKilled) {
        setRobotsKilled(robotsKilled, false);
    }

    public int getRobotsKilled() {
        return preferences.getInteger(ROBOTS_KILLED,0);
    }

    public void addBoxesTeleported(int amount, boolean flushed) {
        preferences.putInteger(BOXES_TELEPORTED, getBoxesTeleported()+amount);
        if(flushed)
            preferences.flush();
    }

    public void addBoxesTeleported(int amount) {
        addBoxesTeleported(amount, true);
    }

    public void setBoxesTeleported(int boxesTeleported, boolean flushed) {
        preferences.putInteger(BOXES_TELEPORTED, boxesTeleported);
        if(flushed)
            preferences.flush();
    }

    public void setBoxesTeleported(int boxesTeleported) {
        setBoxesTeleported(boxesTeleported, false);
    }

    public int getBoxesTeleported() {
        return preferences.getInteger(BOXES_TELEPORTED,0);
    }

    public void addSpecialChallengeLevelCompleted(boolean flushed) {
        preferences.putInteger(SPECIAL_CHALLENGE_LEVELS_COMPLETED, getSpecialChallengeLevelCompleted() + 1);
        if(flushed)
            preferences.flush();
    }

    public void addSpecialChallengeLevelCompleted() {
        addSpecialChallengeLevelCompleted(false);
    }

    public void setSpecialChallengeLevelsCompleted(int specialChallengeLevelsCompleted, boolean flushed) {
        preferences.putInteger(SPECIAL_CHALLENGE_LEVELS_COMPLETED, specialChallengeLevelsCompleted);
        if(flushed)
            preferences.flush();
    }

    public void setSpecialChallengeLevelsCompleted(int specialChallengeLevelsCompleted) {
        setSpecialChallengeLevelsCompleted(specialChallengeLevelsCompleted, false);
    }

    public int getSpecialChallengeLevelCompleted() {
        return preferences.getInteger(SPECIAL_CHALLENGE_LEVELS_COMPLETED,0);
    }

    public void addUnblockedStarLevel(boolean flushed) {
        preferences.putInteger(STAR_LEVELS_UNBLOCKED, getUnblockedStarLevels() + 1);
        if(flushed)
            preferences.flush();
    }

    public void addUnblockedStarLevel() {
        addUnblockedStarLevel(false);
    }

    public void setStarLevelsUnblocked(int starLevelsUnblocked, boolean flushed) {
        preferences.putInteger(STAR_LEVELS_UNBLOCKED, starLevelsUnblocked);
        if(flushed)
            preferences.flush();
    }

    public void setStarLevelsUnblocked(int starLevelsUnblocked) {
        setStarLevelsUnblocked(starLevelsUnblocked, false);
    }

    public int getUnblockedStarLevels() {
        return preferences.getInteger(STAR_LEVELS_UNBLOCKED,0);
    }

    public void markAchievement(String achievementId, boolean flushed) {
        preferences.putBoolean(achievementId, true);
        if(flushed)
            preferences.flush();
    }

    public void markAchievement(String achievementId) {
        markAchievement(achievementId, true);
    }

    // Grabamos un aviso para que se suba el achievement a los servidores cuando se pueda
    public void markPushAchievement(String pushSchievementId){
        preferences.putBoolean(pushSchievementId, true);
    }

    public void erasePushAchievement(String pushSchievementId){
        preferences.remove(pushSchievementId);
        preferences.flush();
    }

    public boolean isAchievementDone(String achievementId) {
        return preferences.getBoolean(achievementId, false);
    }
//End achievement section
    public SavedGameDTO getSavedGameObject() {
        SavedGameDTO.Builder builder = new SavedGameDTO.Builder();
        builder.setSavedGameTime(getSavedGameTime());
        builder.setDeviceId(getDeviceId());
        builder.setSavedGameVersion((getSavedGameVersion()));
        for(int episodeIndex = 1;episodeIndex <= SavedGameDTO.NUM_EPISODES; episodeIndex++) {
            for(int levelIndex = 1;levelIndex <= SavedGameDTO.NUM_LEVELS; levelIndex++) {
                builder.setLevelStar(episodeIndex,levelIndex, getLevelStars(levelIndex, episodeIndex));
                builder.setLevelScore(episodeIndex, levelIndex, getLevelScore(levelIndex, episodeIndex));
                builder.setLevelChallengeStatus(episodeIndex, levelIndex, getLevelChallengeStatus(levelIndex, episodeIndex));
                builder.setLevelStatus(episodeIndex, levelIndex, getLevelStatus(levelIndex, episodeIndex));
                builder.setLevelActualized(episodeIndex, levelIndex, isLevelActualized(levelIndex, episodeIndex));
                builder.setLevelProgress(episodeIndex, levelIndex, getStarLevelProgress(levelIndex, episodeIndex));
            }
            builder.setEpisodeStar(episodeIndex, getEpisodeStars(episodeIndex));
            builder.setEpisodeStatus(episodeIndex, getEpisodeStatus(episodeIndex));
            builder.setEpisodeScore(episodeIndex, getEpisodeScore(episodeIndex));
            builder.setEpisodeChallenges(episodeIndex, getEpisodeChallengesCompleted(episodeIndex));
        }
        builder.setTotalStars(getTotalStars());
        builder.setTotalScore(getTotalScore());
        builder.setTotalChallenges(getTotalChallengesCompleted());

        //achievements
        builder.setTotalRobotsKilled(getRobotsKilled());
        builder.setTotalBoxesTeleported(getBoxesTeleported());
        builder.setTotalSpecialChallengeLevelsCompleted(getSpecialChallengeLevelCompleted());
        builder.setTotalStarLevelsUnblocked(getUnblockedStarLevels());

        List<String> dinamicPreferences = getDinamicPreferences();
        builder.setDinamicPreferences(dinamicPreferences);
        return builder.build();
    }

    private List<String> getDinamicPreferences() {
        Set<String> keyList = preferences.get().keySet();
        List<String> dinamicPreferences = new ArrayList<String>();
        for(String key: keyList) {
            if(key.contains("event_")) {
                boolean eventValue = preferences.getBoolean(key);
                if(eventValue) {
                    dinamicPreferences.add(key);
                }
            } else if(key.contains("dialog_")) {
                boolean dialogValue = preferences.getBoolean(key);
                if(dialogValue) {
                    dinamicPreferences.add(key);
                }
            } else if(key.contains("achiev_")) {
                boolean achievementValue = preferences.getBoolean(key);
                if(achievementValue) {
                    dinamicPreferences.add(key);
                }
            }
        }
        return dinamicPreferences;
    }

    public byte[] serializePreferences() throws IOException {
        SavedGameDTO savedGameDTO = getSavedGameObject();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(savedGameDTO.convertoToJson());
        return out.toByteArray();
    }

    public SavedGameDTO deserializePreferences(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        String json = (String) is.readObject();
        SavedGameDTO savedGameDTO = SavedGameDTO.parse(json);
        return savedGameDTO;
    }
    public void updatePreferences(SavedGameDTO savedGameDTO) {
        String deviceId = getDeviceId();
        int savedGameVersion = getSavedGameVersion();
        int musicVolumeState = getMusicVolumeState();
        boolean gameSoundActivated = isGameSoundEnabled();
        preferences.clear();
        setMusicVolumeState(musicVolumeState);
        setGameSoundEnabled(gameSoundActivated);
        setDeviceID(deviceId, false);
        setSavedGameVersion(savedGameVersion, false);
        setSavedGameTime(savedGameDTO.savedGameTime,false);
        for(int episodeIndex = 1;episodeIndex <= SavedGameDTO.NUM_EPISODES; episodeIndex++) {
            for(int levelIndex = 1;levelIndex <= SavedGameDTO.NUM_LEVELS; levelIndex++) {
                setLevelStars(levelIndex, episodeIndex, savedGameDTO.levelStars[episodeIndex][levelIndex], false);
                saveLevelScore(levelIndex, episodeIndex, savedGameDTO.levelScore[episodeIndex][levelIndex], false);
                if(savedGameDTO.levelChallengeStatus[episodeIndex][levelIndex])
                    saveLevelChallengeCompletion(levelIndex, episodeIndex, false);
                setLevelStatus(levelIndex, episodeIndex, savedGameDTO.levelStatus[episodeIndex][levelIndex], false);
                setLevelActualized(levelIndex, episodeIndex, savedGameDTO.levelActualized[episodeIndex][levelIndex], false);
                setStarLevelProgress(levelIndex, episodeIndex, savedGameDTO.starLevelProgress[episodeIndex][levelIndex], false);
            }
            setEpisodeStars(episodeIndex, savedGameDTO.episodeStars[episodeIndex], false);
            setEpisodeStatus(episodeIndex, savedGameDTO.episodeStatus[episodeIndex], false);
            setEpisodeScore(episodeIndex, savedGameDTO.episodeScore[episodeIndex], false);
            setEpisodeChallengesCompleted(episodeIndex, savedGameDTO.episodeChallenges[episodeIndex], false);
        }
        setTotalStars(savedGameDTO.totalstars, false);
        setTotalScore(savedGameDTO.totalscore, false);
        setTotalChallengesCompleted(savedGameDTO.totalChallenges, false);
        for(String dinamicKey: savedGameDTO.dinamicPreferences) {
            if(dinamicKey.startsWith("event_")) {
                setLevelIntroductionEventViewCompleted(dinamicKey, false);
            } else if(dinamicKey.startsWith("dialog_")) {
                setDialogueViewCompleted(dinamicKey, false);
            } else if(dinamicKey.startsWith("achiev_")) {
                markAchievement(dinamicKey.substring(7), false);
            }
        }
        setRobotsKilled(savedGameDTO.totalRobotsKilled, false);
        setBoxesTeleported(savedGameDTO.totalBoxesTeleported, false);
        setSpecialChallengeLevelsCompleted(savedGameDTO.totalSpecialChallengeLevelsCompleted, false);
        setStarLevelsUnblocked(savedGameDTO.totalStarLevelsUnblocked, false);
        preferences.flush();
    }

    public void mergePreferences(SavedGameDTO savedGameDTO) {
        String deviceId = getDeviceId();
        setDeviceID(deviceId, false);
        setSavedGameVersion(getSavedGameVersion(), false);
        setSavedGameTime(false);
        int totalStars = 0;
        int totalScore = 0;
        int totalChallenges = 0;
        for(int episodeIndex = 1;episodeIndex <= SavedGameDTO.NUM_EPISODES; episodeIndex++) {
            int episodeStars = 0;
            int episodeScore = 0;
            int episodeChallenges = 0;
            for(int levelIndex = 1;levelIndex <= SavedGameDTO.NUM_LEVELS; levelIndex++) {
                boolean actualized = false;
                if(savedGameDTO.levelStars[episodeIndex][levelIndex] > getLevelStars(levelIndex, episodeIndex)) {
                    setLevelStars(levelIndex, episodeIndex, savedGameDTO.levelStars[episodeIndex][levelIndex], false);
                    episodeStars += savedGameDTO.levelStars[episodeIndex][levelIndex];
                    actualized = true;
                } else {
                    episodeStars += getLevelStars(levelIndex, episodeIndex);
                }
                if(savedGameDTO.levelScore[episodeIndex][levelIndex] > getLevelScore(levelIndex, episodeIndex)) {
                    saveLevelScore(levelIndex, episodeIndex, savedGameDTO.levelScore[episodeIndex][levelIndex], false);
                    episodeScore += savedGameDTO.levelScore[episodeIndex][levelIndex];
                    actualized = true;
                } else {
                    episodeScore += getLevelScore(levelIndex, episodeIndex);
                }

                if(savedGameDTO.levelChallengeStatus[episodeIndex][levelIndex] && !getLevelChallengeStatus(levelIndex,episodeIndex)) {
                    saveLevelChallengeCompletion(levelIndex, episodeIndex, false);
                    actualized = true;
                }
                if(getLevelChallengeStatus(levelIndex, episodeIndex)) {
                    episodeChallenges += 1;
                    actualized = true;
                }
                if(savedGameDTO.levelStatus[episodeIndex][levelIndex] > getLevelStatus(levelIndex, episodeIndex)) {
                    setLevelStatus(levelIndex, episodeIndex, savedGameDTO.levelStatus[episodeIndex][levelIndex], false);
                    actualized = true;
                }
                ArrayList<Integer> currentStarProgress = unmountLevelStarProgressKey(getStarLevelProgress(levelIndex, episodeIndex), new ArrayList<CellDTO>());//Blank list in case progress is empty
                ArrayList<Integer> remoteStarProgress = unmountLevelStarProgressKey(savedGameDTO.starLevelProgress[episodeIndex][levelIndex], new ArrayList<CellDTO>());
                ArrayList<Integer> mergedStarProgress = Utils.mergeIntegerArrays(currentStarProgress, remoteStarProgress);
                setStarLevelProgress(levelIndex, episodeIndex, mergedStarProgress, false);
                if(!currentStarProgress.containsAll(mergedStarProgress)) {
                    actualized = true;
                }
                if(actualized) {
                    setLevelActualized(levelIndex, episodeIndex, true, false);
                }
            }
            totalStars += episodeStars;
            totalScore += episodeScore;
            totalChallenges += episodeChallenges;
            setEpisodeStars(episodeIndex, episodeStars, false);
            setEpisodeScore(episodeIndex, episodeScore, false);
            setEpisodeChallengesCompleted(episodeIndex, episodeChallenges);
            if(savedGameDTO.episodeStatus[episodeIndex] == GameConstants.CONTENT_STATUS_UNBLOCKING) {
                if(getEpisodeStatus(episodeIndex) == GameConstants.CONTENT_STATUS_BLOCKED) {
                    setEpisodeStatus(episodeIndex, savedGameDTO.episodeStatus[episodeIndex], false);
                }
            } else {
                if(savedGameDTO.episodeStatus[episodeIndex] > getEpisodeStatus(episodeIndex))
                    setEpisodeStatus(episodeIndex, savedGameDTO.episodeStatus[episodeIndex], false);
            }
        }
        setTotalStars(totalStars, false);
        setTotalScore(totalScore, false);
        setTotalChallengesCompleted(totalChallenges, false);
        for(String dinamicKey: savedGameDTO.dinamicPreferences) {
            if(dinamicKey.startsWith("event_")) {
                setLevelIntroductionEventViewCompleted(dinamicKey, false);
            } else if(dinamicKey.startsWith("dialog_")) {
                setDialogueViewCompleted(dinamicKey, false);
            }else if(dinamicKey.startsWith("achiev_")) {
                markAchievement(dinamicKey.substring(7), false);
            }
        }
        //TODO: check if it's supposed to work
        if(savedGameDTO.totalRobotsKilled > getRobotsKilled()) {
            setRobotsKilled(savedGameDTO.totalRobotsKilled, false);
        }
        if(savedGameDTO.totalBoxesTeleported > getBoxesTeleported()) {
            setBoxesTeleported(savedGameDTO.totalBoxesTeleported, false);
        }
        if(savedGameDTO.totalSpecialChallengeLevelsCompleted > getSpecialChallengeLevelCompleted()) {
            setSpecialChallengeLevelsCompleted(savedGameDTO.totalSpecialChallengeLevelsCompleted, false);
        }
        if(savedGameDTO.totalStarLevelsUnblocked > getUnblockedStarLevels()) {
            setStarLevelsUnblocked(savedGameDTO.totalStarLevelsUnblocked, false);
        }
        preferences.flush();
    }

    private String mountLevelStarProgressKey(ArrayList<Integer> adjacentLevelStars){
        String key = "";
        for(int i = 0; i < adjacentLevelStars.size(); i++){
            key += Integer.toString(adjacentLevelStars.get(i));
        }
        return key;
    }

    public ArrayList<Integer> unmountLevelStarProgressKey(String starLevelProgress, ArrayList<CellDTO> unblockingLevelsList){
        ArrayList<Integer> levelsStarsList = new ArrayList<Integer>();
        if(!starLevelProgress.equals("")) {
            for (int i = 0; i < starLevelProgress.length(); i++) {
                levelsStarsList.add(Integer.parseInt(starLevelProgress.substring(i, MathUtils.clamp(i + 1, 0, starLevelProgress.length()))));
            }
        } else {
            // No habia ningun progreso guardado así que le devolvemos la lista con ceros
            for (int i = 0; i < unblockingLevelsList.size(); i++) {
                levelsStarsList.add(0);
            }
        }
        return levelsStarsList;
    }

    public void eraseEpisodeSavedData(int episodeNumber){
        // Restamos del total lo conseguido en el episodio (aunque no lo usemos de momento...)
        int totalScore = getTotalScore() - getEpisodeScore(episodeNumber);
        int totalStars = getTotalStars() - getEpisodeStars(episodeNumber);
        int totalChallenges = getTotalChallengesCompleted() - getEpisodeChallengesCompleted(episodeNumber);
        setTotalScore(totalScore, false);
        setTotalStars(totalStars, false);
        setTotalChallengesCompleted(totalChallenges, false);

        //ArrayList<EpisodeDTO> episodes = GlobalGeneralData.getInstance().getEpisodesData();
        EpisodeDTO episodeDTO  = GlobalGeneralData.getInstance().getEpisodeDataByNumber(episodeNumber);
        if(preferences.contains(EPISODE_STATUS + episodeNumber))
            preferences.remove(EPISODE_STATUS + episodeNumber);

        if(preferences.contains(EPISODE_SCORE + episodeNumber))
            preferences.remove(EPISODE_SCORE + episodeNumber);

        if(preferences.contains(EPISODE_STARS + episodeNumber))
            preferences.remove(EPISODE_STARS + episodeNumber);

        if(preferences.contains(EPISODE_CHALLENGES + episodeNumber))
            preferences.remove(EPISODE_CHALLENGES + episodeNumber);

        //Gdx.app.log("Prefrences", "Total levels: " + episodeDTO.getTotalLevels());
        //LevelModelProviderImpl levelModelProvider = new LevelModelProviderImpl();

        for(int i = 0; i < episodeDTO.getTotalLevels(); i++){
            int levelNumber = episodeDTO.getLevels().get(i).getLevelNumber();
            //Gdx.app.log("Prefrences", "Level Number: " + levelNumber);
            if(preferences.contains(LEVEL_STATUS + episodeNumber + "_" + levelNumber))
                preferences.remove(LEVEL_STATUS + episodeNumber + "_" + levelNumber);
            if(preferences.contains(LEVEL_SCORE + episodeNumber + "_" + levelNumber))
                preferences.remove(LEVEL_SCORE + episodeNumber + "_" + levelNumber);
            if(preferences.contains(LEVEL_STARS + episodeNumber + "_" + levelNumber))
                preferences.remove(LEVEL_STARS + episodeNumber + "_" + levelNumber);
            if(preferences.contains(LEVEL_CHALLENGE_STATUS + episodeNumber + "_" + levelNumber))
                preferences.remove(LEVEL_CHALLENGE_STATUS + episodeNumber + "_" + levelNumber);
            if(preferences.contains(LEVEL_ACTUALIZED + episodeNumber + "_" + levelNumber))
                preferences.remove(LEVEL_ACTUALIZED + episodeNumber + "_" + levelNumber);

            // Eventos de nivel en la selección, incrustados en el EpisodeDTO
            String levelEvent = episodeDTO.getLevels().get(i).getUnblockingDialog();
            if(!levelEvent.equals("")){
                if(preferences.contains(levelEvent)) {
                    Gdx.app.log("Prefrences", "Erasing Selection Event: " + levelEvent + " on level_" + episodeNumber + "_" + levelNumber);
                    preferences.remove(levelEvent);
                }
            }

            // Eventos de nivel dentro de la partida, incrustados en el LevelDTO
            levelEvent = getLevelEventFromFile(episodeNumber, levelNumber);
            if(!levelEvent.equals(""))            {
                if(preferences.contains(levelEvent)) {
                    Gdx.app.log("Prefrences", "Erasing Game Event: " + levelEvent + " on level_" + episodeNumber + "_" + levelNumber);
                    preferences.remove(levelEvent);
                }
            }
        }
        preferences.flush();
    }

    public SavedGameDTO eraseEpisodeSavedDataInDTO(int episodeIndex, SavedGameDTO savedGameDTO){
        // Restamos del total lo conseguido en el episodio (aunque no lo usemos de momento...)
        int totalScore = savedGameDTO.totalscore - savedGameDTO.episodeScore[episodeIndex];
        int totalStars = savedGameDTO.totalstars - savedGameDTO.episodeStars[episodeIndex];
        int totalChallenges = savedGameDTO.totalChallenges - savedGameDTO.episodeChallenges[episodeIndex];
        savedGameDTO.totalscore = totalScore;
        savedGameDTO.totalstars = totalStars;
        savedGameDTO.totalChallenges = totalChallenges;

        savedGameDTO.episodeStatus[episodeIndex] = 0;
        savedGameDTO.episodeScore[episodeIndex] = 0;
        savedGameDTO.episodeStars[episodeIndex] = 0;
        savedGameDTO.episodeChallenges[episodeIndex] = 0;

        // Transformamos el array en una lista
        List<String> dinamicPreferencesList = new LinkedList<String>(Arrays.asList(savedGameDTO.dinamicPreferences));

        EpisodeDTO episodeDTO  = GlobalGeneralData.getInstance().getEpisodeDataByNumber(episodeIndex);
        for(int levelIndex = 0; levelIndex < episodeDTO.getTotalLevels(); levelIndex++) {
            Gdx.app.log("Preferences", "Cleared Level_ " + episodeDTO.getEpisodeNumber() + "_" + episodeDTO.getLevels().get(levelIndex).getLevelNumber());

            savedGameDTO.levelStatus[episodeIndex][levelIndex] = 0;
            savedGameDTO.levelScore[episodeIndex][levelIndex] = 0;
            savedGameDTO.levelStars[episodeIndex][levelIndex] = 0;
            savedGameDTO.levelChallengeStatus[episodeIndex][levelIndex] = false;
            savedGameDTO.levelActualized[episodeIndex][levelIndex] = false;

            // Eventos de nivel en la selección, incrustados en el EpisodeDTO
            String levelEvent = episodeDTO.getLevels().get(levelIndex).getUnblockingDialog();
            if(!levelEvent.equals("")) {
                for(int e = 0; e < dinamicPreferencesList.size(); e++){
                    if(dinamicPreferencesList.get(e).equals(levelEvent)) {
                        Gdx.app.log("Prefrences", "Removed Selection Event: " + dinamicPreferencesList.get(e));
                        dinamicPreferencesList.remove(e);
                        break;
                    }
                }
            }

            // Eventos de nivel dentro de la partida, incrustados en el LevelDTO
            int levelNumber = episodeDTO.getLevels().get(levelIndex).getLevelNumber();
            levelEvent = getLevelEventFromFile(episodeIndex, levelNumber);
            if(!levelEvent.equals("")) {
                for(int e = 0; e < dinamicPreferencesList.size(); e++){
                    if(dinamicPreferencesList.get(e).equals(levelEvent)) {
                        Gdx.app.log("Prefrences", "Removed Game Event: " + dinamicPreferencesList.get(e));
                        dinamicPreferencesList.remove(e);
                        break;
                    }
                }
            }
        }

        // Volvemos a convertir la lista en el array original
        savedGameDTO.dinamicPreferences = new String[dinamicPreferencesList.size()];
        dinamicPreferencesList.toArray(savedGameDTO.dinamicPreferences);

        return savedGameDTO;
    }

    private String getLevelEventFromFile(int episodeNumber, int levelNumber){
        String currentEpisode = String.format("%02d", episodeNumber);
        String currentLevel = String.format("%02d", levelNumber);
        String folderString = "data/episodes/episode_" + currentEpisode + "/";
        String fileString = "level_" + currentEpisode + "_" + currentLevel + ".json";
        FileHandle file = Gdx.files.internal(folderString + fileString);
        String levelJson = file.readString();
        LevelDTO levelDTO = LevelParser.parseLevel(levelJson);
        return levelDTO.getIntroductionEventId();
    }
}