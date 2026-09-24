package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.FloorRotatingDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 09/04/2019.
 */
public class FloorRotatingModel extends FloorModel{
    private Sprite floorMaskSprite, backgroundSprite;
    private MovableBoardObjectModel.DIRECTION actualDirection;
    private float rotationTime, rotationCountdown;
    private ArrayList<MovableBoardObjectModel.DIRECTION> directionsList;
    private int actualIndexInDirectionList = 0;
    private float rotationVelocity;
    private ROTATION_STATUS rotationStatus = ROTATION_STATUS.NONE;
    private float nextRotationValue;
    private float rotationVelocityMultiplier = 200f;
    private float rotationDuration = (90f / rotationVelocityMultiplier); // El tiempo que tarda en girar 90 grados. 180 será el mismo por que lo hará al doble de velocidad
    public FloorRotatingModel(int h, int v, String resourceName, int variation, ArrayList<MovableBoardObjectModel.DIRECTION> directionsList, float rotationTime) {
        super(h, v);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));

        this.rotationTime = rotationTime;
        rotationCountdown = rotationTime - rotationDuration;
        this.directionsList = directionsList;
        this.actualDirection = directionsList.get(0);

        setActiveObject(true);
        setAnimation(new Animation(0.07f, getAtlas().findRegions("arrow")));
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);
        floorMaskSprite = new Sprite(getAtlas().findRegion("floor_mask"));
        floorMaskSprite.setPosition(getPositionX(), getPositionY());
        backgroundSprite = new Sprite(getAtlas().findRegion("floor_background"));
        backgroundSprite.setPosition(getPositionX(), getPositionY());
        setGameObjectDraw(new FloorRotatingDraw());
        setRotationFromDirection();
    }
    private void setRotationFromDirection(){
        switch(getActualDirection()) {
            case RIGHT:
                setRotation(0);
                break;
            case UP:
                setRotation(90);
                break;
            case LEFT:
                setRotation(180);
                break;
            case DOWN:
                setRotation(270);
                break;
        }
    }

    public void setDirectionFromRotation(){
        switch((int) getRotation()) {
            case 0:
                actualDirection = MovableBoardObjectModel.DIRECTION.RIGHT;
                break;
            case 90:
                actualDirection = MovableBoardObjectModel.DIRECTION.UP;
                break;
            case 180:
                actualDirection = MovableBoardObjectModel.DIRECTION.LEFT;
                break;
            case 270:
                actualDirection = MovableBoardObjectModel.DIRECTION.DOWN;
                break;
        }
    }

    public boolean isNextDirectionTheSame(){
        return actualDirection == directionsList.get(getNextIndexInDirectionsList());
    }

    private int getNextIndexInDirectionsList(){
        if((actualIndexInDirectionList + 1) > (directionsList.size() - 1)){
            return 0;
        } else return actualIndexInDirectionList + 1;
    }

    public void setNextIndexInDirectionsList(){
        actualIndexInDirectionList = getNextIndexInDirectionsList();
    }

    public MovableBoardObjectModel.DIRECTION getNextDirection(){
        return directionsList.get(getNextIndexInDirectionsList());
    }

    public Sprite getFloorMaskSprite() {
        return floorMaskSprite;
    }

    public Sprite getBackgroundSprite() {
        return backgroundSprite;
    }

    public MovableBoardObjectModel.DIRECTION getActualDirection(){
        return actualDirection;
    }

    public float getRotationTime() {
        return rotationTime;
    }

    public boolean needRotation(){
        return directionsList.size() > 1;
    }

    public float getRotationCountdown() {
        return rotationCountdown;
    }

    public void setRotationCountdown(float rotationCountdown) {
        this.rotationCountdown = rotationCountdown;
    }

    public ROTATION_STATUS getRotationStatus() {
        return rotationStatus;
    }

    public void setRotationStatus(ROTATION_STATUS rotationStatus) {
        this.rotationStatus = rotationStatus;
    }

    public float getNextRotationValue() {
        return nextRotationValue;
    }

    public void setNextRotationValue(float nextRotationValue) {
        this.nextRotationValue = nextRotationValue;
    }

    public float getRotationVelocity() {
        return rotationVelocity;
    }

    public void setRotationVelocity(float rotationVelocity) {
        this.rotationVelocity = rotationVelocity;
    }

    public float getRotationVelocityMultiplier() {
        return rotationVelocityMultiplier;
    }

    public float getRotationDuration() {
        return rotationDuration;
    }

    public enum ROTATION_STATUS{
        NONE, ROTATING
    }
}
