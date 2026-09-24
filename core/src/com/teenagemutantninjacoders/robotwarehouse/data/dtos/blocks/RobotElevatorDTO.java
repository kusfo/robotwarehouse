package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by Jordi Montornes on 08/11/2016.
 */
public class RobotElevatorDTO {
    int cellH;
    int cellV;
    private String resourceName;
    private int variation;
    int priority;

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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
