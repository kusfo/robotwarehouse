package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;

/**
 * Created by JordiRM on 24/10/2017.
 */
public class OverBaseTrackModel extends OverBaseModel {
    private boolean cannonStop;
    private TRACK_SHAPE trackShape;
    public OverBaseTrackModel(int h, int v, String resourceName, int variation, boolean cannonStop) {
        super(h,v, OVERBASE_TYPE.TRACK);
        setResourceName(resourceName);
        this.cannonStop = cannonStop;
        setAtlas(Assets.getTextureAtlas(resourceName));
        setDepth(GameConstants.WALL_DEPTH - 5); // Por encima de los muros
        setVariation(variation);
        setTrackShape();
    }

    private void setTrackShape(){
        switch(getVariation()){
            case 1: trackShape = TRACK_SHAPE.LEFT_RIGHT; break;
            case 2: trackShape = TRACK_SHAPE.UP_DOWN; break;
            case 3: trackShape = TRACK_SHAPE.RIGHT_DOWN; break;
            case 4: trackShape = TRACK_SHAPE.LEFT_DOWN; break;
            case 5: trackShape = TRACK_SHAPE.LEFT_UP; break;
            case 6: trackShape = TRACK_SHAPE.RIGHT_UP; break;
            case 7: trackShape = TRACK_SHAPE.LEFT; break;
            case 8: trackShape = TRACK_SHAPE.RIGHT; break;
            case 9: trackShape = TRACK_SHAPE.UP; break;
            case 10: trackShape = TRACK_SHAPE.DOWN; break;
        }
    }

    public TRACK_SHAPE getTrackShape() {
        return trackShape;
    }

    public boolean isCannonStop() {
        return cannonStop;
    }

    public enum TRACK_SHAPE{
        LEFT_RIGHT, UP_DOWN, RIGHT_DOWN, LEFT_DOWN, LEFT_UP, RIGHT_UP, RIGHT, LEFT, DOWN, UP;
    }
}
