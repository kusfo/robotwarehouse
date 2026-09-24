package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by jordi on 05/03/2016.
 */
public class WallDTO {
    int cellH;
    int cellV;
    private String resourceName;
    private int variation;

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

}
