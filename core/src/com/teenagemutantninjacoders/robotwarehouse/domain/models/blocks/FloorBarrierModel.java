package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 11/12/2019.
 */
public class FloorBarrierModel extends FloorModel implements iColoredObject {
    private GlobalAttributes.COLOR color = GlobalAttributes.COLOR.NONE;
    private BARRIER_TYPE barrierType;
    private BARRIER_STATUS barrierStatus,nextBarrierStatus,baseBarrierStatus, changedBarrierStatus;

    private boolean automatic;
    private float timeBlocked, timeUnblocked;
    private float eventTimeEnd = 0f;

    public FloorBarrierModel(int h, int v, String resourceName, int variation, GlobalAttributes.COLOR color,
                             BARRIER_TYPE barrierType, BARRIER_STATUS barrierStatus, BARRIER_ORIENTATION barrierOrientation,
                             boolean automatic, float timeBlocked, float timeUnblocked, float initialEventTime){
        super(h, v);
        this.barrierType = barrierType;
        this.barrierStatus = barrierStatus;

        this.timeBlocked = timeBlocked;
        this.timeUnblocked = timeUnblocked;
        setFloorEventTime(initialEventTime);
        this.automatic = automatic;

        baseBarrierStatus = barrierStatus;
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setColor(color);
        setDepth(GameConstants.FLOOR_DEPTH);
        setHitbox();
        setActiveObject(true);
        switch (barrierType){
            case SOLID:
                setAnimation(new Animation(0.05f, getAtlas().findRegions("barrier_" + barrierType.getValue() + "_" + color.getValue())));
                setHitboxImpact(new Rectangle(4, 10, 24, 30));
                break;
            case ENERGY:
                if(barrierOrientation == BARRIER_ORIENTATION.HORIZONTAL) {
                    setAnimation(new Animation(0.05f, getAtlas().findRegions("barrier_" + barrierType.getValue() + "_" + color.getValue() + "_h")));
                }
                else if(barrierOrientation == BARRIER_ORIENTATION.VERTICAL) {
                    setAnimation(new Animation(0.05f, getAtlas().findRegions("barrier_" + barrierType.getValue() + "_" + color.getValue() + "_v")));
                    setDepth(GameConstants.WALL_DEPTH + 1); // Al estra contra un muro se tiene que dibujar por encima
                }
                break;
        }

        setAnimationTime(getAnimation().getAnimationDuration()); // Mostramos el fotograma final
        // Configuracion inicial
        switch (baseBarrierStatus){
            case UNBLOCKED:
                getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
                changedBarrierStatus = BARRIER_STATUS.BLOCKED;
                eventTimeEnd = timeUnblocked;
                break;
            case BLOCKED:
                getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
                changedBarrierStatus = BARRIER_STATUS.UNBLOCKED;
                if(barrierType == BARRIER_TYPE.SOLID){
                    setObstacleLevel(GameConstants.WALL_OBSTACLE_LEVEL);
                    setDepth(GameConstants.WALL_DEPTH);
                }
                else if(barrierType == BARRIER_TYPE.ENERGY) setDangerous(true);
                eventTimeEnd = timeBlocked;
                break;
        }
        nextBarrierStatus = baseBarrierStatus;
    }

    public void activateBaseStatus(){
        nextBarrierStatus = baseBarrierStatus;
    }

    public void activateChangedStatus(){
        nextBarrierStatus = changedBarrierStatus;
    }

    public BARRIER_STATUS getBarrierStatus(){
        return barrierStatus;
    }

    public void beginNewStatus(BARRIER_STATUS newBarrierStatus){

        switch (newBarrierStatus){
            case BLOCKED:
                barrierStatus = BARRIER_STATUS.BLOCKING;
                getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
                if(barrierType == BARRIER_TYPE.SOLID){
                    setObstacleLevel(GameConstants.WALL_OBSTACLE_LEVEL);
                    setDepth(GameConstants.WALL_DEPTH);
                }
                else if(barrierType == BARRIER_TYPE.ENERGY) setDangerous(true);
                setAnimationTime(0);
                setFloorEventTime(0);
                break;
            case UNBLOCKED:
                barrierStatus = BARRIER_STATUS.UNBLOCKING;
                getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
                setAnimationTime(0);
                setFloorEventTime(0);
                break;
        }
    }

    public void setIdleStatus(BARRIER_STATUS newStatus){
        barrierStatus = newStatus;
        // Solo cuando ha acabado de obstaculizar, liberamos el camino
        if(newStatus == BARRIER_STATUS.UNBLOCKED) {
            if (barrierType == BARRIER_TYPE.SOLID) {
                setObstacleLevel(GameConstants.FLOOR_OBSTACLE_LEVEL);
                setDepth(GameConstants.FLOOR_DEPTH);
            } else if (barrierType == BARRIER_TYPE.ENERGY)
                setDangerous(false);
            eventTimeEnd = timeUnblocked;
        } else if(newStatus == BARRIER_STATUS.BLOCKED)
            eventTimeEnd = timeBlocked;
    }

    public void playBarrierSound(){
        if(barrierType == BARRIER_TYPE.SOLID)
            AudioManager.getInstance().playSound(AudioManager.SOUND.FLOORBARRIER_SOLID);
        else
            AudioManager.getInstance().playSound(AudioManager.SOUND.FLOORBARRIER_ENERGY);
    }

    public BARRIER_STATUS getNextBarrierStatus() {
        return nextBarrierStatus;
    }

    public boolean isBarrierInIdle(){
        return barrierStatus == FloorBarrierModel.BARRIER_STATUS.BLOCKED ||
                barrierStatus == FloorBarrierModel.BARRIER_STATUS.UNBLOCKED;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public float getEventTimeEnd(){
        return eventTimeEnd;
    }

    public BARRIER_TYPE getBarrierType() {
        return barrierType;
    }

    @Override
    public GlobalAttributes.COLOR getColor(){
        return color;
    }
    @Override
    public void setColor(GlobalAttributes.COLOR newColor){
        color = newColor;
    }

    public enum BARRIER_TYPE {
        SOLID("solid"), ENERGY("energy");
        private String value;
        BARRIER_TYPE(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static BARRIER_TYPE fromString(String text) {
            if (text != null) {
                for (BARRIER_TYPE var : BARRIER_TYPE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return SOLID;
        }
    }

    public enum BARRIER_ORIENTATION {
        NONE("none"), HORIZONTAL("horizontal"), VERTICAL("vertical");
        private String value;
        BARRIER_ORIENTATION(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static BARRIER_ORIENTATION fromString(String text) {
            if (text != null) {
                for (BARRIER_ORIENTATION var : BARRIER_ORIENTATION.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }

    public enum BARRIER_STATUS {
        NONE("none"), BLOCKED("blocked"), BLOCKING("blocking"), UNBLOCKED("unblocked"), UNBLOCKING("unblocking");
        private String value;
        BARRIER_STATUS(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static BARRIER_STATUS fromString(String text) {
            if (text != null) {
                for (BARRIER_STATUS var : BARRIER_STATUS.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }
}