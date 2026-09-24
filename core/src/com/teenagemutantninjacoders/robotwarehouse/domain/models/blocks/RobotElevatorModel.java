package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.RobotElevatorDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 27/11/2016.
 */
public class RobotElevatorModel extends FloorModel {
    private final int elevatorIdentifier;
    private ROBOT_ELEVATOR_STATUS robotElevatorStatus = ROBOT_ELEVATOR_STATUS.CLOSED;
    private int priority;
    private boolean active;
    private float internalCounter = 0;
    private AnimatedSprite doorAnimation;
    private Sprite platformSprite;
    private GameObject gameObjectMask;
    private int randomValue = 0;
    private boolean disabled = false;
    private AnimatedSprite disableEffect;
    public RobotElevatorModel(int h, int v, String resourceName, int variation, int priority, GameObject gameObjectMask) {
        super(h, v);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));

        this.gameObjectMask = gameObjectMask;
        setSprite(new Sprite(getAtlas().findRegion("floor")));

        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setBoardObjectLayer(BOARD_OBJECT_LAYER.ABOVE);
        this.priority = priority;

        doorAnimation = new AnimatedSprite(getAtlas(), "door", 0.05f, Animation.PlayMode.REVERSED);
        doorAnimation.setLastFrame(); // Mostramos el fotograma final
        platformSprite = new Sprite(getAtlas().findRegion("platform"));

        resetPlatformPositionY();

        this.gameObjectMask.setSprite(new Sprite(getAtlas().findRegion("mask")));
        this.gameObjectMask.setPosition(getPosition());
        this.gameObjectMask.setDepth(getDepth() - 20);
        elevatorIdentifier = GlobalLevelData.getInstance().getNextElevatorIdentifier();

        disableEffect = new AnimatedSprite(Assets.getTextureAtlas("fx_enemyDeactivationSparks"), 0.1f, Animation.PlayMode.LOOP);
        setGameObjectDraw(new RobotElevatorDraw());
        setActiveObject(true);
    }

    public void setRobotElevatorStatus(ROBOT_ELEVATOR_STATUS newRobotElevatorStatus){
        robotElevatorStatus = newRobotElevatorStatus;

        if(robotElevatorStatus == ROBOT_ELEVATOR_STATUS.CLOSING){
            doorAnimation.setPlayMode(Animation.PlayMode.REVERSED);
        }
        else if(robotElevatorStatus == ROBOT_ELEVATOR_STATUS.OPENING){
            doorAnimation.setPlayMode(Animation.PlayMode.NORMAL);
            resetPlatformPositionY();
        }
    }

    private void resetPlatformPositionY(){
        platformSprite.setPosition(getPositionX(), getPositionY() - 30);
    }

    public ROBOT_ELEVATOR_STATUS getRobotElevatorStatus(){
        return robotElevatorStatus;
    }

    public void update() {
        if(robotElevatorStatus == ROBOT_ELEVATOR_STATUS.OPENED) {
            internalCounter += 1f;
            if (internalCounter >= 40f){
                setRobotElevatorStatus(ROBOT_ELEVATOR_STATUS.CLOSING);
                internalCounter = 0;
            }
        }

        if(getDoorAnimation().isAnimationFinished()){
            if(robotElevatorStatus == ROBOT_ELEVATOR_STATUS.CLOSING){
                setRobotElevatorStatus(ROBOT_ELEVATOR_STATUS.CLOSED);
            }
            else if(robotElevatorStatus == ROBOT_ELEVATOR_STATUS.OPENING){
                setRobotElevatorStatus(ROBOT_ELEVATOR_STATUS.OPENED);
            }
        }
    }
    public AnimatedSprite getDisableEffect() {
        return disableEffect;
    }

    public AnimatedSprite getDoorAnimation() {
        return doorAnimation;
    }

    public Sprite getPlatformSprite() {
        return platformSprite;
    }

    public int getElevatorIdentifier() {
        return elevatorIdentifier;
    }

    public int getPriority() {
        return priority;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setRandomValue(int randomValue) {
        this.randomValue = randomValue;
    }

    public int getRandomValue() {
        return randomValue;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public enum ROBOT_ELEVATOR_STATUS {
        NONE, OPENED, OPENING, CLOSING, CLOSED
    }
}
