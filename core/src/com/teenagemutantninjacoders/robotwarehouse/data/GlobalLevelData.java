package com.teenagemutantninjacoders.robotwarehouse.data;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by JordiRM on 14/02/2016.
 */
public class GlobalLevelData {
    private int levelScore = 0;
    private float levelTime = 0;
    private float initialLevelTime = 0;
    private int background = 1;
    private RemainingBoxes remainingBoxes;
    private BoardButtons boardButtons;
    private FinalLevelResults finalLevelResults;
    private LevelRankStats levelRankStats;
    private int requestedBoxes = 0;
    private int initialRequestedBoxes = 0;
    private int optimalTime;
    private int timeMultiplier = 3;
    private int rankPointsMod;
    private int maxComboRed = -1;
    private int maxComboBlue = -1;
    private int maxComboYellow = -1;
    private int levelMaxNumRobots = 3;
    private int levelMaxNumRatliens = 3;
    private int robotSpanTime = 30;
    private int ratlienSpanTime = 30;
    private int lostBoxes = 0;
    private int initialOverBasePoints = 0;
    private int overBasePointsActivated = 0;
    private int numberRobots = 0;
    private int numberRatliens = 0;
    private int enemyIdentifier = 0;
    private int elevatorIdentifier = 0;
    private int ratliensChewing = 0;
    private GlobalAttributes.CHALLENGE actualChallenge = GlobalAttributes.CHALLENGE.NONE;
    private int actualChallengeValue = 0;
    private GlobalAttributes.CHALLENGE_OBJECT challengeObject = GlobalAttributes.CHALLENGE_OBJECT.NONE;
    private int playerMoves = 0;
    private int robotsKilled = 0;

    private int boxesTeleporting = 0;   // Para saber si hay cajas en proceso de carga
    private int[] lastShipContainers;

    private LEVEL_STATUS levelStatus = LEVEL_STATUS.PLAYING;
    private LEVEL_EVENT levelEvent = LEVEL_EVENT.ENTERING;

    private boolean paintFxAlways = false;

    public void setMaxNumRobots(int maxNumRobots) {
        this.levelMaxNumRobots = maxNumRobots;
    }

    public void setRobotSpanTime(int robotSpanTime) {
        this.robotSpanTime = robotSpanTime;
    }

    public void setMaxNumRatliens(int maxNumRatliens) {
        this.levelMaxNumRatliens = maxNumRatliens;
    }

    public void setRatlienSpanTime(int ratlienSpanTime) {
        this.ratlienSpanTime = ratlienSpanTime;
    }

    public int getNextEnemyIdentifier() {
        return enemyIdentifier++;
    }

    public int getNextElevatorIdentifier() {
        return elevatorIdentifier++;
    }

    public void resetEnemyIdentifier() {
        enemyIdentifier = 0;
    }

    public class RemainingBoxes {
        private int red = 0;
        private int blue = 0;
        private int yellow = 0;
        private int initialRed = 0;
        private int initialBlue = 0;
        private int initialYellow = 0;

        public int getAll() {
            return red + blue + yellow;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        public int getInitialRed() {
            return initialRed;
        }

        public void setInitialRed() {
            this.initialRed = red;
        }

        public void increaseRed(int amount) {
            setRed(getRed() + amount);
        }

        public void decreaseRed(int amount) {
            setRed(getRed() - amount);
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public int getInitialBlue() {
            return initialBlue;
        }

        public void setInitialBlue() {
            this.initialBlue = blue;
        }

        public void increaseBlue(int amount) {
            setBlue(getBlue() + amount);
        }

        public void decreaseBlue(int amount) {
            setBlue(getBlue() - amount);
        }

        public int getYellow() {
            return yellow;
        }

        public void setYellow(int yellow) {
            this.yellow = yellow;
        }

        public int getInitialYellow() {
            return initialYellow;
        }

        public void setInitialYellow() {
            this.initialYellow = yellow;
        }

        public void increaseYellow(int amount) {
            setYellow(getYellow() + amount);
        }

        public void decreaseYellow(int amount) {
            setYellow(getYellow() - amount);
        }

        public int getTransportableBoxes(){
            return red + blue + yellow;
        }

        public int getColor(GlobalAttributes.COLOR color){
            switch(color){
                case RED: return red;
                case BLUE:  return blue;
                case YELLOW: return yellow;
                default: return -1;
            }
        }
    }

    public class BoardButtons{
        private ArrayMap<String, Integer> totalButtonsMap = new ArrayMap<String, Integer>();
        private ArrayMap<String, Integer> buttonsPressedMap = new ArrayMap<String, Integer>();
        public BoardButtons(){
            initializeButtons();
        }
        public int getTotalButtons(GlobalAttributes.COLOR color){
            return totalButtonsMap.get(color.getValue());
        }
        public int getButtonsPressed(GlobalAttributes.COLOR color){
            return buttonsPressedMap.get(color.getValue());
        }
        public void modifyTotalButtons(GlobalAttributes.COLOR color, int modifier){
            int index = totalButtonsMap.indexOfKey(color.getValue());
            totalButtonsMap.setValue(index, totalButtonsMap.getValueAt(index) + modifier);
        }
        public void modifyButtonsPressed(GlobalAttributes.COLOR color, int modifier){
            int index = buttonsPressedMap.indexOfKey(color.getValue());
            buttonsPressedMap.setValue(index, buttonsPressedMap.getValueAt(index) + modifier);
        }

        public void initializeButtons(){
            totalButtonsMap.clear();
            totalButtonsMap.put("red", 0);
            totalButtonsMap.put("blue", 0);
            totalButtonsMap.put("yellow", 0);
            totalButtonsMap.put("green", 0);
            totalButtonsMap.put("orange", 0);
            totalButtonsMap.put("purple", 0);
            buttonsPressedMap.clear();
            buttonsPressedMap.put("red", 0);
            buttonsPressedMap.put("blue", 0);
            buttonsPressedMap.put("yellow", 0);
            buttonsPressedMap.put("green", 0);
            buttonsPressedMap.put("orange", 0);
            buttonsPressedMap.put("purple", 0);
        }
    }

    public class FinalLevelResults{
        private int pointsFromTime;
        private int penaltyFromLostBoxes;
        private int rankAchieved;
        private int totalLevelScore;
        private boolean newRecord = false;

        public int getPointsFromTime() {
            return pointsFromTime;
        }

        public void setPointsFromTime(int pointsFromTime) {
            this.pointsFromTime = pointsFromTime;
        }

        public int getPenaltyFromLostBoxes() {
            return penaltyFromLostBoxes;
        }

        public void setPenaltyFromLostBoxes(int penaltyFromLostBoxes) {
            this.penaltyFromLostBoxes = penaltyFromLostBoxes;
        }

        public int getRankAchieved() {
            return rankAchieved;
        }

        public void setRankAchieved(int rankAchieved) {
            this.rankAchieved = rankAchieved;
        }

        public int getTotalLevelScore() {
            return totalLevelScore;
        }

        public void setTotalLevelScore(int totalLevelScore) {
            this.totalLevelScore = totalLevelScore;
        }

        public boolean isNewRecord() {
            return newRecord;
        }

        public void setNewRecord(boolean newRecord) {
            this.newRecord = newRecord;
        }
    }

    public class LevelRankStats{
        public int pointsTwoStars = 0;
        public int pointsThreeStars = 0;
        public int optimalTime = 0;
        public int optimalTimePoints = 0;
        public int boxPointsMargin = 0;
        public int floorPointsMax = 0;
    }

    private static GlobalLevelData instance;

    private GlobalLevelData() {
        remainingBoxes = new RemainingBoxes();
        boardButtons = new BoardButtons();
        finalLevelResults = new FinalLevelResults();
        levelRankStats = new LevelRankStats();
        lastShipContainers = new int[]{1, 1, 1};
    }

    public static GlobalLevelData getInstance() {
        if (instance == null) {
            instance = new GlobalLevelData();
        }
        return instance;
    }

    // Estado de la partida
    public LEVEL_STATUS getLevelStatus() {
        return levelStatus;
    }

    public void setLevelStatus(LEVEL_STATUS levelStatus) {
        this.levelStatus = levelStatus;
    }

    public LEVEL_EVENT getLevelEvent() {
        return levelEvent;
    }

    public void setLevelEvent(LEVEL_EVENT levelEvent) {
        this.levelEvent = levelEvent;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    // Score
    public int getLevelScore() {
        return levelScore;
    }

    public void setLevelScore(int levelScore) {
        this.levelScore = levelScore;
    }

    public void addScore(int scoreToAdd) {
        levelScore += scoreToAdd;
    }

    // Remaining boxes
    public RemainingBoxes getRemainingBoxes() {
        return remainingBoxes;
    }

    public BoardButtons getBoardButtons(){
        return boardButtons;
    }
    public FinalLevelResults getFinalLevelResults(){
        return finalLevelResults;
    }

    public LevelRankStats getLevelRankStats() {
        return levelRankStats;
    }

    // Requested boxes
    public int getRequestedBoxes() {
        return requestedBoxes;
    }

    public void setRequestedBoxes(int requestedBoxes) {
        this.requestedBoxes = requestedBoxes;
    }

    public void decreaseRequestedBoxes(int amount) {
        requestedBoxes = MathUtils.clamp(requestedBoxes - amount, 0, 9999);
    }

    public int getInitialRequestedBoxes() {
        return initialRequestedBoxes;
    }

    public void setInitialRequestedBoxes(int initialRequestedBoxes) {
        this.initialRequestedBoxes = initialRequestedBoxes;
    }

    public int getOptimalTime() {
        return optimalTime;
    }

    public void setOptimalTime(int optimalTime) {
        this.optimalTime = optimalTime;
    }

    public int getTimeMultiplier() {
        return timeMultiplier;
    }

    public void setTimeMultiplier(int timeMultiplier) {
        this.timeMultiplier = timeMultiplier;
    }

    public int getRankPointsMod() {
        return rankPointsMod;
    }

    public void setRankPointsMod(int rankPointsMod) {
        this.rankPointsMod = rankPointsMod;
    }

    public int getMaxComboRed() {
        return maxComboRed;
    }

    public void setMaxComboRed(int maxComboRed) {
        this.maxComboRed = maxComboRed;
    }

    public int getMaxComboBlue() {
        return maxComboBlue;
    }

    public void setMaxComboBlue(int maxComboBlue) {
        this.maxComboBlue = maxComboBlue;
    }

    public int getMaxComboYellow() {
        return maxComboYellow;
    }

    public void setMaxComboYellow(int maxComboYellow) {
        this.maxComboYellow = maxComboYellow;
    }

    public void setLostBoxes(int amount){
        lostBoxes = amount;
    }
    public void increaseLostBoxes(int amount){
        lostBoxes += amount;
    }
    public int getLostBoxes(){
        return lostBoxes;
    }

    // LevelTime
    public float getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(float newLevelTime) {
        this.levelTime = newLevelTime;
    }

    public void setInitialLevelTime(float newLevelTime) {
        this.initialLevelTime = newLevelTime;
        setLevelTime(initialLevelTime);
    }

    public float getInitialLevelTime() {
        return initialLevelTime;
    }

    public void decreaseTimeLevel(float amountTime) {
        levelTime = MathUtils.clamp(levelTime - amountTime, 0, 9999);
    }

    public int getInitialOverBasePoints() {
        return initialOverBasePoints;
    }

    public void setInitialOverBasePoints(int amount) {
        initialOverBasePoints = amount;
    }

    public void increaseInitialOverBasePoints(int amount) {
        initialOverBasePoints += amount;
    }

    public int getOverBasePointsActivated() {
        return overBasePointsActivated;
    }

    public void setOverBasePointsActivated(int amount) {
        overBasePointsActivated = amount;
    }

    public void increaseOverBasePointsActivated() {
        overBasePointsActivated++;
    }

    public int getNumberRobots() {
        return numberRobots;
    }

    public int getMaxNumRobots() {
        return levelMaxNumRobots;
    }

    public int getNumberRatliens() {
        return numberRatliens;
    }

    public int getMaxNumRatliens() {
        return levelMaxNumRatliens;
    }

    public int getRobotSpanTime() {
        return robotSpanTime;
    }

    public int getRatlienSpanTime() {
        return ratlienSpanTime;
    }

    public void addRobot() {
        numberRobots++;
    }

    public void subtractRobot() {
        numberRobots--;
    }

    public void resetNumberRobots() {
        numberRobots = 0;
    }

    public void addRatlien() {
        numberRatliens++;
    }

    public void subtractRatlien() {
        numberRatliens--;
    }
    public void subtractRatlien(int amount) {
        numberRatliens -= amount;
    }

    public void resetNumberRatliens() {
        numberRatliens = 0;
    }

    public int getBoxesTeleporting() {
        return boxesTeleporting;
    }

    public void setBoxesTeleporting(int amount) {
        boxesTeleporting = amount;
    }

    public void increaseBoxesTeleporting(int amount) {
        boxesTeleporting += amount;
    }

    public void decreaseBoxesTeleporting(int amount) {
        boxesTeleporting = MathUtils.clamp(boxesTeleporting - amount, 0, 9999);
    }

    public int getRatliensChewing(){
        return ratliensChewing;
    }
    public void increaseRatliensChewing(int amount){
        ratliensChewing += amount;
    }
    public void decreasingRatliensChewing(int amount){
        ratliensChewing = MathUtils.clamp(ratliensChewing - amount, 0, 9999);
    }
    public void setRatliensChewing(int amount){
        ratliensChewing = amount;
    }


    public void setActualChallege(GlobalAttributes.CHALLENGE challenge){
        this.actualChallenge = challenge;
    }

    public GlobalAttributes.CHALLENGE getActualChallenge(){
        return actualChallenge;
    }

    public int getActualChallengeValue() {
        return actualChallengeValue;
    }

    public void setActualChallengeValue(int actualChallengeValue) {
        this.actualChallengeValue = actualChallengeValue;
    }

    public GlobalAttributes.CHALLENGE_OBJECT getActualChallengeObject() {
        return challengeObject;
    }

    public void setActualChallengeObject(GlobalAttributes.CHALLENGE_OBJECT challengeObject) {
        this.challengeObject = challengeObject;
    }

    public int getPlayerMoves() {
        return playerMoves;
    }
    public void setPlayerMoves(int amount) {
        playerMoves = amount;
    }
    public void addPlayerMove() {
        playerMoves += 1;
    }

    public int getRobotsKilled() {
        return robotsKilled;
    }
    public void setRobotsKilled(int amount) {
        robotsKilled = amount;
    }
    public void addRobotKilled() {
        robotsKilled++;
    }

    public void setLastShipContainers(int first, int second, int third){
        lastShipContainers[0] = first;
        lastShipContainers[1] = second;
        lastShipContainers[2] = third;
    }
    public int getLastShipContainer(int number){
        return lastShipContainers[number];
    }

    public boolean isPaintingFxAlways() {
        return paintFxAlways;
    }

    public void setPaintFxAlways(boolean paintFxAlways) {
        this.paintFxAlways = paintFxAlways;
    }

    public enum LEVEL_STATUS {
        PLAYING, PAUSED
    }

    public enum LEVEL_EVENT {
        NONE, ENTERING, RESTARTING, COMPLETED, TIME_OUT, WITHOUT_BOXES, TOO_MANY_LOSSES
    }
}
