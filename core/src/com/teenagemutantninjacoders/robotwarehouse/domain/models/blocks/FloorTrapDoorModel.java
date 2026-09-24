package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by jordi.montornes on 11/03/2016.
 */
public class FloorTrapDoorModel extends FloorModel{
    private FLOOR_STATUS floorStatus = FLOOR_STATUS.NONE;
    private float timeOpen = 0f;
    private float timeClosed = 0f;
    private float eventTimeEnd = 0f;
    private GameObject gameObjectMask;
    public FloorTrapDoorModel(int h, int v, String resourceName, int variation, FLOOR_STATUS initialFloorStatus, float timeOpen,
                              float timeCosed, float initialEventTime, GameObject gameObjectMask) {
        super(h, v);
        this.gameObjectMask = gameObjectMask;
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));

        setAnimation(new Animation(0.1f, getAtlas().findRegions("door")));
        setAnimationTime(getAnimation().getAnimationDuration()); // Mostramos el fotograma final
        switch (initialFloorStatus){
            case CLOSE: getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
                        break;
            case OPEN:  getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
                        eventTimeEnd = timeOpen;
                        break;
        }
        this.timeOpen = timeOpen;
        this.timeClosed = timeCosed;
        setFloorEventTime(initialEventTime);
        setFloorStatus(initialFloorStatus);
        setActiveObject(true);

        gameObjectMask.setSprite(new Sprite(getAtlas().findRegion("mask")));
        gameObjectMask.setPosition(getPosition());
        gameObjectMask.setDepth(getDepth() - 31); //-50
    }
    public FLOOR_STATUS getFloorStatus(){
        return floorStatus;
    }
    public void setFloorStatus(FLOOR_STATUS newFloorStatus){
        floorStatus = newFloorStatus;
        if(floorStatus == FLOOR_STATUS.CLOSE) eventTimeEnd = timeClosed;
        else if(floorStatus == FLOOR_STATUS.OPENING) eventTimeEnd = timeOpen;
    }
    public float getEventTimeEnd(){
        return eventTimeEnd;
    }


    public enum FLOOR_STATUS {
        NONE("none"), OPEN("open"), OPENING("opening"), CLOSE("close");
        private String value;
        FLOOR_STATUS(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static FLOOR_STATUS fromString(String text) {
            if (text != null) {
                for (FLOOR_STATUS var : FLOOR_STATUS.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }

}
