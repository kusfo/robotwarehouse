package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.display.Assets;

/**
 * Created by JordiRM on 15/03/2016.
 */
public class FloorTileModel extends FloorModel {
    public FloorTileModel(int h, int v, String resourceName, int variation) {
        super(h, v);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setResourceName(resourceName);
        setVariation(variation);
    }
}
