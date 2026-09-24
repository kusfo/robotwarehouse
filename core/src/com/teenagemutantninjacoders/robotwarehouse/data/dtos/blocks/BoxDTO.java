package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by jordi.montornes on 31/01/2016.
 */
public class BoxDTO {
    int cellH;
    int cellV;
    private String resourceName;
    private String color;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
