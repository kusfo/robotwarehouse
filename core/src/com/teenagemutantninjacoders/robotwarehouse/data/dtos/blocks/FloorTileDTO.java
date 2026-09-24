package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by jordi.montornes on 17/03/2016.
 */
public class FloorTileDTO extends FloorDTO {
    private String resourceName;
    private int variation;


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
