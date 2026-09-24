package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 03/07/2016.
 */
public class OverBaseModel extends BoardObjectModel {
    private OVERBASE_TYPE overBaseType;
    public OverBaseModel(int h, int v, OVERBASE_TYPE overBaseType) {
        setColumn(h);
        setRow(v);
        this.overBaseType = overBaseType;
        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setDepth(GameConstants.OVERBASE_DEPTH);
        setBoardObjectLayer(BOARD_OBJECT_LAYER.OVERBASE);
        setObstacleLevel(GameConstants.FLOOR_OBSTACLE_LEVEL);
    }

    public OVERBASE_TYPE getOverBaseType() {
        return overBaseType;
    }

    public enum OVERBASE_TYPE {
        DECORATION("decoration"), TRACK("track"), POINTS("points"), POWERS("powers"), INDICATOR_LIGHT("indicator_light");
        private String value;
        OVERBASE_TYPE(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static OVERBASE_TYPE fromString(String text) {
            if (text != null) {
                for (OVERBASE_TYPE var : OVERBASE_TYPE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return POINTS;
        }
    }
}
