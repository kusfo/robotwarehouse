package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by JordiRM on 20/12/2019.
 */
public class FloorBarrierDTO extends FloorDTO {
    private String resourceName;
    private int variation;
    private String color;
    private String barrierType, barrierStatus, barrierOrientation;
    private boolean automatic;
    private float timeBlocked, timeUnblocked,initialEventTime;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBarrierType() {
        return barrierType;
    }

    public void setBarrierType(String barrierType) {
        this.barrierType = barrierType;
    }

    public String getBarrierStatus() {
        return barrierStatus;
    }

    public void setBarrierStatus(String barrierStatus) {
        this.barrierStatus = barrierStatus;
    }

    public String getBarrierOrientation() {
        return barrierOrientation;
    }

    public void setBarrierOrientation(String barrierOrientation) {
        this.barrierOrientation = barrierOrientation;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public float getTimeBlocked() {
        return timeBlocked;
    }

    public void setTimeBlocked(float timeBlocked) {
        this.timeBlocked = timeBlocked;
    }

    public float getTimeUnblocked() {
        return timeUnblocked;
    }

    public void setTimeUnblocked(float timeUnblocked) {
        this.timeUnblocked = timeUnblocked;
    }

    public float getInitialEventTime() {
        return initialEventTime;
    }

    public void setInitialEventTime(float initialEventTime) {
        this.initialEventTime = initialEventTime;
    }
}
