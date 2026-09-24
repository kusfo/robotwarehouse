package com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies;

import java.util.ArrayList;

/**
 * Created by jordi on 09/11/2016.
 */

public class CannonDTO {
    private int cellH;
    private int cellV;
    private String resourceName;
    private int variation;
    private String direction;
    private String shootDirection;
    private int maxAutoAdvances;
    private int baseVelocity;
    private int pauseAfterShooting;
    private int initialWaitingTime;
    private boolean programedLoop;
    private ArrayList<CannonStopDTO> programedStops;

    public int getCellH() {
        return cellH;
    }

    public void setCellH(int cellH) {
        this.cellH = cellH;
    }

    public int getCellV() {
        return cellV;
    }

    public void setCellV(int cellV) {
        this.cellV = cellV;
    }

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

    public String getShootDirection() {
        return shootDirection;
    }

    public void setShootDirection(String shootDirection) {
        this.shootDirection = shootDirection;
    }

    public int getMaxAutoAdvances() {
        return maxAutoAdvances;
    }

    public void setMaxAutoAdvances(int maxAutoAdvances) {
        this.maxAutoAdvances = maxAutoAdvances;
    }

    public int getBaseVelocity() {
        return baseVelocity;
    }

    public void setBaseVelocity(int baseVelocity) {
        this.baseVelocity = baseVelocity;
    }

    public int getPauseAfterShooting() {
        return pauseAfterShooting;
    }

    public void setPauseAfterShooting(int pauseAfterShooting) {
        this.pauseAfterShooting = pauseAfterShooting;
    }

    public int getInitialWaitingTime() {
        return initialWaitingTime;
    }

    public void setInitialWaitingTime(int initialWaitingTime) {
        this.initialWaitingTime = initialWaitingTime;
    }

    public boolean hasProgramedLoop() {
        return programedLoop;
    }

    public void setProgramedLoop(boolean programedLoop) {
        this.programedLoop = programedLoop;
    }

    public ArrayList<CannonStopDTO> getProgramedStops() {
        return programedStops;
    }

    public void setProgramedStops(ArrayList<CannonStopDTO> programedStops) {
        this.programedStops = programedStops;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
