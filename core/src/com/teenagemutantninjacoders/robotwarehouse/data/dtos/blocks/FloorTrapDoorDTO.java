package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by jordi on 05/03/2016.
 */
public class FloorTrapDoorDTO extends FloorDTO {
    private String resourceName;
    private int variation;
    private String floorStatus;
    private float timeOpen;
    private float timeClosed;
    private float initialEventTime = 0;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String wallName) {
        this.resourceName = wallName;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }

    public String getFloorStatus() {
        return floorStatus;
    }

    public void setFloorStatus(String floorStatus) {
        this.floorStatus = floorStatus;
    }

    public float getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(float timeOpen) {
        this.timeOpen = timeOpen;
    }

    public float getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(float timeClosed) {
        this.timeClosed = timeClosed;
    }

    public float getInitialEventTime() {
        return initialEventTime;
    }

    public void setInitialEventTime(float initialEventTime) {
        this.initialEventTime = initialEventTime;
    }
}
