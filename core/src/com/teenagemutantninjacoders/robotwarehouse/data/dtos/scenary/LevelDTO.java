package com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary;

import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.*;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorTileDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorTrapDoorDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseBoxActivableDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseDecoDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseTrackDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.RobotElevatorDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.WallDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies.CannonDTO;

import java.util.ArrayList;

/**
 * Created by jordi on 22/02/2016.
 */
public class LevelDTO {
    private ArrayList<WallDTO> walls;
    private ArrayList<FloorTileDTO> floorTiles;
    private ArrayList<FloorTrapDoorDTO> floorTrapDoors;
    private ArrayList<FloorButtonDTO> floorButtons;
    private ArrayList<FloorBarrierDTO> floorBarriers;
    private ArrayList<FloorRotatingDTO> rotatingFloors;
    private ArrayList<BoxDTO> boxes;
    private ArrayList<AbyssDTO> abyss;
    private ArrayList<OverBaseBoxActivableDTO> overBaseBoxActivables;
    private ArrayList<OverBaseDecoDTO> overBaseDecos;
    private ArrayList<OverBaseTrackDTO> overBaseTracks;
    private ArrayList<OverBaseIndicatorLightDTO> overBaseIndicatorLights;
    private ArrayList<BigDecoDTO> bigDecos;
    private ArrayList<RobotElevatorDTO> robotElevators;
    private ArrayList<CannonDTO> cannons;
    private int levelTime;
    private int requestedBoxes;
    private int optimalTime;
    private int timeMultiplier = 3;
    private int rankPointsMod;
    private int maxComboRed = -1;
    private int maxComboBlue = -1;
    private int maxComboYellow = -1;
    private int maxNumRobots;
    private int maxNumRatliens;
    private int robotSpanTime;
    private int ratlienSpanTime;
    private int pusherRobot;
    private int explosiveRobot;
    private String challenge = "none";
    private int challengeValue = 0;
    private String challengeObject = "none";
    private String introductionEvent = "";

    public LevelDTO() {
        walls = new ArrayList<WallDTO>();
        floorTiles = new ArrayList<FloorTileDTO>();
        floorTrapDoors = new ArrayList<FloorTrapDoorDTO>();
        floorButtons = new ArrayList<FloorButtonDTO>();
        floorBarriers = new ArrayList<FloorBarrierDTO>();
        rotatingFloors = new ArrayList<FloorRotatingDTO>();
        boxes = new ArrayList<BoxDTO>();
        abyss = new ArrayList<AbyssDTO>();
        overBaseBoxActivables = new ArrayList<OverBaseBoxActivableDTO>();
        overBaseDecos = new ArrayList<OverBaseDecoDTO>();
        overBaseTracks = new ArrayList<OverBaseTrackDTO>();
        overBaseIndicatorLights = new ArrayList<OverBaseIndicatorLightDTO>();
        bigDecos = new ArrayList<BigDecoDTO>();
        robotElevators = new ArrayList<RobotElevatorDTO>();
        cannons = new ArrayList<CannonDTO>();
    }

    public ArrayList<WallDTO> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<WallDTO> walls) {
        this.walls = walls;
    }

    public ArrayList<FloorTileDTO> getFloorTiles() {
        return floorTiles;
    }

    public void setFloorTiles(ArrayList<FloorTileDTO> floorTiles) {
        this.floorTiles = floorTiles;
    }

    public ArrayList<FloorTrapDoorDTO> getFloorTrapDoors() {
        return floorTrapDoors;
    }

    public ArrayList<FloorButtonDTO> getFloorButtons() {
        return floorButtons;
    }

    public void setFloorButtons(ArrayList<FloorButtonDTO> floorButtons) {
        this.floorButtons = floorButtons;
    }

    public ArrayList<FloorBarrierDTO> getFloorBarriers() {
        return floorBarriers;
    }

    public void setFloorBarriers(ArrayList<FloorBarrierDTO> floorBarriers) {
        this.floorBarriers = floorBarriers;
    }

    public ArrayList<FloorRotatingDTO> getRotatingFloors() {
        return rotatingFloors;
    }

    public void setRotatingFloors(ArrayList<FloorRotatingDTO> rotatingFloors) {
        this.rotatingFloors = rotatingFloors;
    }

    public ArrayList<BoxDTO> getBoxes() {
        return boxes;
    }

    public void setBoxes(ArrayList<BoxDTO> boxes) {
        this.boxes = boxes;
    }

    public ArrayList<AbyssDTO> getAbyss() {
        return abyss;
    }

    public void setAbyss(ArrayList<AbyssDTO> abyss) {
        this.abyss = abyss;
    }

    public ArrayList<OverBaseBoxActivableDTO> getOverBaseBoxActivables() {
        return overBaseBoxActivables;
    }

    public ArrayList<OverBaseDecoDTO> getOverBaseDecos() {
        return overBaseDecos;
    }

    public ArrayList<OverBaseTrackDTO> getOverBaseTracks() {
        return overBaseTracks;
    }

    public ArrayList<OverBaseIndicatorLightDTO> getOverBaseIndicatorLights(){
        return overBaseIndicatorLights;
    }

    public ArrayList<BigDecoDTO> getBigDecos() {
        return bigDecos;
    }

    public void setBigDecos(ArrayList<BigDecoDTO> bigDecos) {
        this.bigDecos = bigDecos;
    }

    public ArrayList<RobotElevatorDTO> getRobotElevators() {
        return robotElevators;
    }

    public void setRobotElevators(ArrayList<RobotElevatorDTO> robotElevators) {
        this.robotElevators = robotElevators;
    }

    public ArrayList<CannonDTO> getCannons() {
        return cannons;
    }

    public void setCannons(ArrayList<CannonDTO> cannons) {
        this.cannons = cannons;
    }

    public int getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(int levelTime) {
        this.levelTime = levelTime;
    }

    public int getRequestedBoxes() {
        return requestedBoxes;
    }

    public void setRequestedBoxes(int requestedBoxes) {
        this.requestedBoxes = requestedBoxes;
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

    public void setMaxNumRobots(int maxNumRobots) {
        this.maxNumRobots = maxNumRobots;
    }

    public int getRobotSpanTime() {
        return robotSpanTime;
    }

    public void setRobotSpanTime(int robotSpanTime) {
        this.robotSpanTime = robotSpanTime;
    }

    public int getMaxNumRatliens() {
        return maxNumRatliens;
    }

    public void setMaxNumRatliens(int maxNumRatliens) {
        this.maxNumRatliens = maxNumRatliens;
    }

    public int getRatlienSpanTime() {
        return ratlienSpanTime;
    }

    public void setRatlienSpanTime(int ratlienSpanTime) {
        this.ratlienSpanTime = ratlienSpanTime;
    }

    public int getPusherRobot() { return pusherRobot; }

    public void setPusherRobot(int pusherRobot) { this.pusherRobot = pusherRobot; }

    public int getExplosiveRobot() { return explosiveRobot; }

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

    public String getIntroductionEventId() {
        return introductionEvent;
    }

    public void setIntroductionEventId(String introductionEvent) {
        this.introductionEvent = introductionEvent;
    }
}
