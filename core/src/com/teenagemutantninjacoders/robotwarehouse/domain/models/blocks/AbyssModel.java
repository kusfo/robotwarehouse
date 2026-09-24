package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 07/06/2016.
 */
public class AbyssModel extends BoardObjectModel {
    public AbyssModel(int h, int v, String resourceName, int variation){
        setColumn(h);
        setRow(v);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setResourceName(resourceName);
        setVariation(variation);
        setObstacleLevel(GameConstants.FLOOR_OBSTACLE_LEVEL);

        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setDepth(GameConstants.ABYSS_DEPTH);//2500
        setBoardObjectLayer(BOARD_OBJECT_LAYER.BASE);
    }
}