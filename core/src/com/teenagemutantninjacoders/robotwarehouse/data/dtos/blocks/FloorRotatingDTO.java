package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

import java.util.ArrayList;

/**
 * Created by JordiRM on 17/04/2019.
 */
public class FloorRotatingDTO {
    private int cellH;
    private int cellV;
    private String resourceName = "";
    private int variation = 0;
    private ArrayList<String> directionsList;
    private float rotationTime;

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

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
    }

    public ArrayList<String> getDirectionsList() {
        return directionsList;
    }

    public void setDirectionsList(ArrayList<String> directionsList) {
        this.directionsList = directionsList;
    }

    public float getRotationTime() {
        return rotationTime;
    }

    public void setRotationTime(float rotationTime) {
        this.rotationTime = rotationTime;
    }
}
