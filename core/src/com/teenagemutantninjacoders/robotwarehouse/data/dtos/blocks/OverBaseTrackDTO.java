package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by JordiRM on 24/10/2017.
 */
public class OverBaseTrackDTO {
    int cellH;
    int cellV;
    private String resourceName ="";
    private int variation = 0;
    private boolean cannonStop;

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

    public boolean isCannonStop() {
        return cannonStop;
    }

    public void setCannonStop(boolean cannonStop) {
        this.cannonStop = cannonStop;
    }
}
