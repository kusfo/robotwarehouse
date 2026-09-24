package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;

/**
 * Created by JordiRM on 27/06/2019.
 */
public class BigDecoModel extends OverBaseModel{
    public BigDecoModel(int h, int v, String resourceName, int variation) {
        super(h,v, OVERBASE_TYPE.DECORATION);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setVariation(variation);
        setDepth(GameConstants.WALL_DEPTH - 5);
    }
}
