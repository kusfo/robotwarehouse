package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by JordiRM on 09/08/2016.
 */
public class OverBaseBoxActivableDTO {
    int cellH;
    int cellV;
    private String resourceName;
    private int variation;
    private String overBaseType;
    private int overBaseGroupNumber;
    private String power;

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

    public int getOverBaseGroupNumber() {
        return overBaseGroupNumber;
    }

    public void setOverBaseGroupNumber(int overBaseGroupNumber) {
        this.overBaseGroupNumber = overBaseGroupNumber;
    }

    public String getOverBaseType() {
        return overBaseType;
    }

    public void setOverBaseType(String overBaseType) {
        this.overBaseType = overBaseType;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
