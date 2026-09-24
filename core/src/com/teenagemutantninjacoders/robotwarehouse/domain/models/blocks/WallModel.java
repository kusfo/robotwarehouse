package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 05/02/2016.
 */
public class WallModel extends BoardObjectModel {
    public WallModel(int h, int v, String resourceName, int variation){
        setColumn(h);
        setRow(v);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setVariation(variation);
        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setHitbox();
        setHitboxImpact();
        setBoardObjectLayer(BOARD_OBJECT_LAYER.BASE);
        setDepth(GameConstants.WALL_DEPTH);
        setObstacleLevel(GameConstants.WALL_OBSTACLE_LEVEL);
    }
}
