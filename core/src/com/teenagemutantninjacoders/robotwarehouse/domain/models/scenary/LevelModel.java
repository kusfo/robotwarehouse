package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.RatlienLairDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies.CannonStopDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.BoardModelHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.AbyssModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BigDecoModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorBarrierModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorButtonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorRotatingModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorTileModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorTrapDoorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseDecoModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightShine;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseTrackModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RatlienLairModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.WallModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.ExplosiveRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.PusherRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jordi on 03/02/2016.
 */
public class LevelModel {
    private BoardModel currentBoardModel;
    private ArrayList<GameObject> currentGameObjects;
    private ArrayList<RobotElevatorModel> currentRobotElevatorModels;
    private ArrayList<RatlienLairModel> currentRatlienLairModels;
    private ArrayList<PowerGroupModel> currentPowerGroupModels;
    private ArrayList<FloorBarrierModel> currentFloorBarrierModels;
    private int levelTime;
    private int requestBoxes;
    private int optimalTime;
    private int timeMultiplier;
    private int rankPointsMod;
    private int maxComboRed = -1;
    private int maxComboBlue = -1;
    private int maxComboYellow = -1;
    private int maxNumRobots;
    private int maxNumRatliens;
    private int robotSpanTime;
    private int pusherRobot;
    private int explosiveRobot;
    private String introductionEventId;
    private int ratlienSpanTime;
    private String challenge = "none";
    private int challengeValue = 0;
    private String challengeObject = "none";

    public LevelModel() {
        currentBoardModel = new BoardModel();
        BoardModelHelper.initializeBoardModel(currentBoardModel);
        currentGameObjects = new ArrayList<GameObject>();
        currentRobotElevatorModels = new ArrayList<RobotElevatorModel>();
        currentRatlienLairModels = new ArrayList<RatlienLairModel>();
        currentPowerGroupModels = new ArrayList<PowerGroupModel>();
        currentFloorBarrierModels = new ArrayList<FloorBarrierModel>();

        // Inicializamos los datos globales estandart del nivel
        GlobalLevelData.getInstance().getRemainingBoxes().setRed(0);
        GlobalLevelData.getInstance().getRemainingBoxes().setBlue(0);
        GlobalLevelData.getInstance().getRemainingBoxes().setYellow(0);
        GlobalLevelData.getInstance().setLostBoxes(0);
        GlobalLevelData.getInstance().setLevelScore(0);
        GlobalLevelData.getInstance().setBoxesTeleporting(0);
        GlobalLevelData.getInstance().setRatliensChewing(0);
        GlobalLevelData.getInstance().resetNumberRobots();
        GlobalLevelData.getInstance().resetNumberRatliens();
        GlobalLevelData.getInstance().setInitialOverBasePoints(0);
        GlobalLevelData.getInstance().setOverBasePointsActivated(0);
        GlobalLevelData.getInstance().resetEnemyIdentifier();
        GlobalLevelData.getInstance().setPlayerMoves(0);
        GlobalLevelData.getInstance().setRobotsKilled(0);

        GlobalLevelData.getInstance().getBoardButtons().initializeButtons();
    }

    public void initializeLevel() {
        // Inicializamos los datos locales del nivel
        GlobalLevelData.getInstance().setInitialLevelTime(getLevelTime());
        GlobalLevelData.getInstance().setInitialRequestedBoxes(getRequestBoxes());
        GlobalLevelData.getInstance().setRequestedBoxes(getRequestBoxes());

        GlobalLevelData.getInstance().setOptimalTime(getOptimalTime());
        GlobalLevelData.getInstance().setTimeMultiplier(getTimeMultiplier());
        GlobalLevelData.getInstance().setRankPointsMod(getRankPointsMod());
        GlobalLevelData.getInstance().setMaxComboRed(getMaxComboRed());
        GlobalLevelData.getInstance().setMaxComboBlue(getMaxComboBlue());
        GlobalLevelData.getInstance().setMaxComboYellow(getMaxComboYellow());

        GlobalLevelData.getInstance().setMaxNumRobots(getMaxNumRobots());
        GlobalLevelData.getInstance().setMaxNumRatliens(getMaxNumRatliens());
        GlobalLevelData.getInstance().setRobotSpanTime(getRobotSpanTime());
        GlobalLevelData.getInstance().setRatlienSpanTime(getRatlienSpanTime());

        GlobalLevelData.getInstance().setActualChallege(GlobalAttributes.CHALLENGE.fromString(challenge));
        GlobalLevelData.getInstance().setActualChallengeValue(challengeValue);
        GlobalLevelData.getInstance().setActualChallengeObject(GlobalAttributes.CHALLENGE_OBJECT.fromString(challengeObject));
    }

    public void setInitialBoxes(){
        GlobalLevelData.getInstance().getRemainingBoxes().setInitialRed();
        GlobalLevelData.getInstance().getRemainingBoxes().setInitialBlue();
        GlobalLevelData.getInstance().getRemainingBoxes().setInitialYellow();
    }

    public BoardModel getCurrentBoardModel() {
        return currentBoardModel;
    }

    public ArrayList<GameObject> getCurrentGameObjects() {
        return currentGameObjects;
    }

    public void addNewWall(int h, int v, String resourceName, int variation) {
        WallModel newWall = new WallModel(h, v, resourceName, variation);
        currentBoardModel.setWall(newWall, h, v);
        currentGameObjects.add(newWall);
    }

    public void addNewAbyss(int h, int v, String resourceName, int variation, RatlienLairDTO ratlienLairDTO) {
        AbyssModel newAbyss;
        if(ratlienLairDTO != null) {
            newAbyss = new RatlienLairModel(h, v, resourceName, variation,ratlienLairDTO.getPriority(), ratlienLairDTO.isExitUp(),
                    ratlienLairDTO.isExitDown(), ratlienLairDTO.isExitLeft(), ratlienLairDTO.isExitRight(), ratlienLairDTO.isVisible());
            currentRatlienLairModels.add((RatlienLairModel) newAbyss);
        } else {
            newAbyss = new AbyssModel(h, v, resourceName, variation);
        }
        currentBoardModel.setAbyss(newAbyss, h, v);
        currentGameObjects.add(newAbyss);
    }

    public void addNewFloorTile(int h, int v, String resourceName, int variation) {
        FloorTileModel newFloorTile = new FloorTileModel(h, v, resourceName, variation);
        currentBoardModel.setFloor(newFloorTile, h, v);
        currentGameObjects.add(newFloorTile);
    }

    public void addNewFloorTrapDoor(int h, int v, String resourceName, int variation, FloorTrapDoorModel.FLOOR_STATUS initialFloorStatus,
                                    float timeOpen, float timeClosed, float initialEventTime) {
        GameObject gameObjectMask = new GameObject();

        FloorTrapDoorModel newFloorTrapDoor = new FloorTrapDoorModel(h, v, resourceName, variation, initialFloorStatus, timeOpen,
                timeClosed, initialEventTime, gameObjectMask);
        currentBoardModel.setFloor(newFloorTrapDoor, h, v);
        currentGameObjects.add(newFloorTrapDoor);
        currentGameObjects.add(gameObjectMask);
    }

    public void addNewFloorRotating(int h, int v, String resourceName, int variation, ArrayList<MovableBoardObjectModel.DIRECTION> directionsList,
                                    float rotationTime) {
        FloorRotatingModel newFloorRotating = new FloorRotatingModel(h, v, resourceName, variation, directionsList, rotationTime);
        currentBoardModel.setFloor(newFloorRotating, h, v);
        currentGameObjects.add(newFloorRotating);
    }

    public void addNewFloorButton(int h, int v, String resourceName, int variation, GlobalAttributes.COLOR color, boolean colorExclusive){
        FloorButtonModel floorButtonModel = new FloorButtonModel(h, v, resourceName, variation, color, colorExclusive);
        currentBoardModel.setFloor(floorButtonModel, h, v);
        currentGameObjects.add(floorButtonModel);
        GlobalLevelData.getInstance().getBoardButtons().modifyTotalButtons(color, 1);
    }

    public void addNewFloorBarrier(int h, int v, String resourceName, int variation, GlobalAttributes.COLOR color,
                                   FloorBarrierModel.BARRIER_TYPE barrier_type, FloorBarrierModel.BARRIER_STATUS barrier_status,
                                   FloorBarrierModel.BARRIER_ORIENTATION barrierOrientation,
                                   boolean timeActivatred, float timeBlocked, float timeUnblocked, float initialEventTime){
        FloorBarrierModel floorBarrierModel = new FloorBarrierModel(h, v, resourceName, variation, color, barrier_type, barrier_status,
                barrierOrientation, timeActivatred, timeBlocked, timeUnblocked, initialEventTime);
        currentBoardModel.setFloor(floorBarrierModel, h, v);
        currentGameObjects.add(floorBarrierModel);
        currentFloorBarrierModels.add(floorBarrierModel);
    }

    public void addNewBox(int h, int v, String resourceName, GlobalAttributes.COLOR color) {
        BoxModel newBox = new BoxModel(h, v, resourceName, color);
        currentBoardModel.addBoardObjectAbove(newBox, h, v);
        currentGameObjects.add(newBox);
        switch(color){
            case RED: GlobalLevelData.getInstance().getRemainingBoxes().increaseRed(1);
                break;
            case BLUE: GlobalLevelData.getInstance().getRemainingBoxes().increaseBlue(1);
                break;
            case YELLOW: GlobalLevelData.getInstance().getRemainingBoxes().increaseYellow(1);
                break;
        }
    }

    public void addNewParticleEffect(ParticleEffectModel newEffect) {
        currentGameObjects.add(newEffect);
    }

    public void addNewGameObject(GameObject gameObject){
        currentGameObjects.add(gameObject);
    }

    public void addNewRatlien(int h, int v, MovableBoardObjectModel.DIRECTION initialDirection, RatlienModel.ENTRY_TYPE entryType) {
        EnemyModel enemyModel;
        enemyModel = new RatlienModel(h, v, initialDirection, entryType);
        GlobalLevelData.getInstance().addRatlien();
        currentGameObjects.add(enemyModel);
        currentBoardModel.addBoardObjectAbove (enemyModel, h, v);
    }

    public void addRobot(int h, int v, EnemyModel.ENEMY_TYPE enemyType, RobotElevatorModel elevator){
        EnemyModel enemyModel;
        if(enemyType.equals(EnemyModel.ENEMY_TYPE.NORMAL_ROBOT)){
            enemyModel = new PusherRobotModel(h, v, elevator);
            GlobalLevelData.getInstance().addRobot();
        } else if(enemyType.equals(EnemyModel.ENEMY_TYPE.EXPLOSIVE_ROBOT)){
            enemyModel = new ExplosiveRobotModel(h, v, elevator);
            GlobalLevelData.getInstance().addRobot();
        } else {
            enemyModel = new PusherRobotModel(h, v, elevator);
            GlobalLevelData.getInstance().addRobot();
        }
        currentGameObjects.add(enemyModel);
    }

    public void addNewRobotElevator(int h, int v, String resourceName, int variation, int priority) {
        GameObject gameObjectMask = new GameObject();
        RobotElevatorModel robotElevatorModel = new RobotElevatorModel(h, v, resourceName, variation, priority, gameObjectMask);

        currentBoardModel.setFloor(robotElevatorModel, h, v);
        currentGameObjects.add(robotElevatorModel);
        currentGameObjects.add(gameObjectMask);
        currentRobotElevatorModels.add(robotElevatorModel);
    }

    public void addNewCannon(int h, int v, String resourceName, int variation, MovableBoardObjectModel.DIRECTION initialDirection,
                             CannonModel.CANNON_SHOOT_DIRECTION shootDirection,
                             int maxAutoAdvances, int baseVelocity, int pauseAfterShooting, int initialWaitingTime,
                             boolean programedLoop, ArrayList<CannonStopDTO> programedStops) {
        CannonModel cannonModel = new CannonModel(h, v, resourceName, variation, initialDirection, shootDirection, maxAutoAdvances,
                baseVelocity, pauseAfterShooting, initialWaitingTime, programedLoop, programedStops);
        currentBoardModel.addBoardObjectAbove(cannonModel, h, v);
        currentGameObjects.add(cannonModel);
    }

    public void addNewOverBaseBoxActivable(int h, int v, String resourceName, int variation, OverBaseModel.OVERBASE_TYPE overBaseType,
                                           int groupNumber, GlobalAttributes.POWER power) {
        OverBaseBoxActivableModel overBase = new OverBaseBoxActivableModel(h, v, resourceName, variation, overBaseType, groupNumber, power);
        currentBoardModel.setOverBase(overBase, h, v);
        currentGameObjects.add(overBase);
        if(overBaseType == OverBaseModel.OVERBASE_TYPE.POINTS)
            GlobalLevelData.getInstance().increaseInitialOverBasePoints(1);
        else if(overBaseType == OverBaseModel.OVERBASE_TYPE.POWERS)
            addToPowerGroup(overBase);
    }

    public void addNewOverBaseDeco(int h, int v, String resourceName, int variation){
        OverBaseDecoModel overBase = new OverBaseDecoModel(h, v, resourceName, variation);
        currentBoardModel.setOverBase(overBase, h, v);
        currentGameObjects.add(overBase);
    }

    public void addNewOverBaseTrack(int h, int v, String resourceName, int variation, boolean isCannonStop){
        OverBaseTrackModel overBase = new OverBaseTrackModel(h, v, resourceName, variation, isCannonStop);
        currentBoardModel.setOverBase(overBase, h, v);
        currentGameObjects.add(overBase);
    }

    public void addNewOverBaseIndicatorLight(int h, int v){
        OverBaseIndicatorLightShine shineObject = new OverBaseIndicatorLightShine();
        OverBaseIndicatorLightModel overBase = new OverBaseIndicatorLightModel(h, v, shineObject);

        currentBoardModel.setOverBase(overBase, h, v);
        currentGameObjects.add(overBase);
        currentGameObjects.add(shineObject);
    }

    public void addNewBigDeco(int h, int v, String resourceName, int variation){
        BigDecoModel bigDeco = new BigDecoModel(h, v, resourceName, variation);
        currentBoardModel.setOverBase(bigDeco, h, v);
        currentGameObjects.add(bigDeco);
    }

    private void addToPowerGroup(OverBaseBoxActivableModel overBase) {
        for(PowerGroupModel powerGroup : currentPowerGroupModels) {
            if(powerGroup.getGroupNumber() == overBase.getGroupNumber()){
                powerGroup.addPowerOverBase(overBase);
                return;
            }
        }
        PowerGroupModel powerGroup = new PowerGroupModel(overBase.getGroupNumber(), overBase.getPower());
        powerGroup.addPowerOverBase(overBase);
        currentPowerGroupModels.add(powerGroup);
    }

    public ArrayList<RobotElevatorModel> getRobotElevatorsList(){
        return currentRobotElevatorModels;
    }

    public ArrayList<RatlienLairModel> getRatlienLairList() {
        return currentRatlienLairModels;
    }

    public List<PowerGroupModel> getcurrentPowerGroupModels() {
        return currentPowerGroupModels;
    }

    public ArrayList<FloorBarrierModel> getCurrentFloorBarrierModels(){
        return currentFloorBarrierModels;
    }

    public void deleteGameObject(GameObject gameObject) {
        if (gameObject instanceof BoardObjectModel) {
            BoardObjectModel boardObject =  ((BoardObjectModel)gameObject);
            if(boardObject.isOnBoard()) {
                getCurrentBoardModel().deleteBoardObject(boardObject.getColumn(), boardObject.getRow(), boardObject.getBoardObjectLayer());
            }
        }
        currentGameObjects.remove(gameObject);
    }

    public void setLevelTime(int levelTime) {
        this.levelTime = levelTime;
    }

    public void setRequestBoxes(int requestBoxes) {
        this.requestBoxes = requestBoxes;
    }

    public int getLevelTime() {
        return levelTime;
    }

    public int getRequestBoxes() {
        return requestBoxes;
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

    public int getMaxNumRobots() {
        return maxNumRobots;
    }

    public void setMaxNumRatliens(int maxNumRatliens) {
        this.maxNumRatliens = maxNumRatliens;
    }

    public int getMaxNumRatliens() {
        return maxNumRatliens;
    }

    public void setMaxNumRobots(int maxNumRobots) {
        this.maxNumRobots = maxNumRobots;
    }

    public int getRobotSpanTime() {
        return robotSpanTime;
    }

    public void setRobotSpanTime(int robotSpanTime) {
        this.robotSpanTime = robotSpanTime;
    }

    public int getRatlienSpanTime() {
        return ratlienSpanTime;
    }

    public void setRatlienSpanTime(int ratlienSpanTime) {
        this.ratlienSpanTime = ratlienSpanTime;
    }
    public int getPusherRobot() { return pusherRobot; }

    public void setPusherRobot(int pusherRobot) { this.pusherRobot = pusherRobot; }

    public int getExplosiveRobot() { return explosiveRobot; };

    public void setExplosiveRobot(int explosiveRobot) { this.explosiveRobot = explosiveRobot; }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public int getChallengeValue() {
        return challengeValue;
    }

    public void setChallengeValue(int challengeValue) {
        this.challengeValue = challengeValue;
    }

    public String getChallengeObject() {
        return challengeObject;
    }

    public void setChallengeObject(String challengeObject) {
        this.challengeObject = challengeObject;
    }

    public void setIntroductionEventId(String eventId) {
        this.introductionEventId = eventId;
    }
    public String getIntroductionEventId() {
        return introductionEventId;
    }

}
