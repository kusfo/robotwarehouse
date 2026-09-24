package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorRotatingModel;

/**
 * Created by JordiRM on 09/04/2019.
 */
public class FloorRotatingDraw extends GameObjectDraw{
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        FloorRotatingModel rotatingFloor = (FloorRotatingModel) currentGameObject;
        rotatingFloor.getBackgroundSprite().draw(batch);
        drawSprite(currentGameObject, batch, delta);
        rotatingFloor.getFloorMaskSprite().draw(batch);
    }
}
