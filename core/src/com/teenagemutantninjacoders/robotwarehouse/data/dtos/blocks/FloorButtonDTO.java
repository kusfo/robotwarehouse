package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

/**
 * Created by JordiRM on 20/12/2019.
 */
public class FloorButtonDTO extends FloorDTO  {
    private String resourceName;
    private int variation;
    private String color;
    private boolean colorExclusive;

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

    public boolean isColorExclusive() {
        return colorExclusive;
    }

    public void setColorExclusive(boolean colorExclusive) {
        this.colorExclusive = colorExclusive;
    }
}
