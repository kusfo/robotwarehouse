package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;

/**
 * Created by JordiRM on 19/02/2016.
 */
public interface iColoredObject {
    public GlobalAttributes.COLOR getColor();
    public void setColor(GlobalAttributes.COLOR newColor);
}
