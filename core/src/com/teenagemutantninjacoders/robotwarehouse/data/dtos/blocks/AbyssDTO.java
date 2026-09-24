package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by JordiRM on 07/06/2016.
 */
public class AbyssDTO {
    int cellH;
    int cellV;
    private String resourceName;
    private int variation;
    private RatlienLairDTO ratlienLair;

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

    public RatlienLairDTO getRatlienLair() {
        return ratlienLair;
    }

    public void setRatlienLair(RatlienLairDTO ratlienLair) {
        this.ratlienLair = ratlienLair;
    }
}
