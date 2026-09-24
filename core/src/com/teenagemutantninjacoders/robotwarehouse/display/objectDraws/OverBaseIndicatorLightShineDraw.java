package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightShine;

/**
 * Created by JordiRM on 11/02/2019.
 */
public class OverBaseIndicatorLightShineDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        OverBaseIndicatorLightShine shineObject = (OverBaseIndicatorLightShine) currentGameObject;
        if(shineObject.getShineLightColor() != OverBaseIndicatorLightModel.LIGHT_COLOR.NONE) {

            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_CONSTANT_COLOR);
            //batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ONE);
            //batch.setBlendFunction(shineObject.getBlend(1), shineObject.getBlend(2));

            if (shineObject.getShineLightColor() == OverBaseIndicatorLightModel.LIGHT_COLOR.RED) {
                shineObject.getLightRaysSprite().draw(batch);
            }
            shineObject.getGlowSprite().draw(batch);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }
}
