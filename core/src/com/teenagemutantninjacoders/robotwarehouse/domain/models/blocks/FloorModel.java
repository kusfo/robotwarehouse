package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 10/02/2016.
 */
public class FloorModel extends BoardObjectModel {
    private float eventTime = 0;

    public FloorModel(int h, int v){
        setColumn(h);
        setRow(v);
        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setDepth(GameConstants.FLOOR_DEPTH);
        setBoardObjectLayer(BOARD_OBJECT_LAYER.BASE);
        setObstacleLevel(GameConstants.FLOOR_OBSTACLE_LEVEL);
    }

    public float getFloorEventTime(){
        return eventTime;
    }
    public void setFloorEventTime(float value){
        eventTime = value;
    }
}
