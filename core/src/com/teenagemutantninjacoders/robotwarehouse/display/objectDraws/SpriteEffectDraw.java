package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectModel;

/**
 * Created by JordiRM on 09/11/2018.
 */
public class SpriteEffectDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        if(!currentGameObject.getAnimation().isAnimationFinished(currentGameObject.getAnimationTime()))
            drawSprite(currentGameObject, batch, delta);
    }
}
