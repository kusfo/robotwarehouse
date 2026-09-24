package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.display.Assets;

/**
 * Created by JordiRM on 18/10/2017.
 */
public class OverBaseDecoModel extends OverBaseModel {
    public OverBaseDecoModel(int h, int v, String resourceName, int variation) {
        super(h,v, OVERBASE_TYPE.DECORATION);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setVariation(variation);
    }
}
