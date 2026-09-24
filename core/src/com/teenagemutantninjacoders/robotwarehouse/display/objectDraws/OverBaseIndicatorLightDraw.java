package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightModel;

/**
 * Created by JordiRM on 11/02/2019.
 */
public class OverBaseIndicatorLightDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        OverBaseIndicatorLightModel lampObject = (OverBaseIndicatorLightModel) currentGameObject;
        drawSprite(currentGameObject, batch, delta);

        // Cristal base
        lampObject.getBaseGlassSprite().draw(batch);

        // Dibujamos el cristal iluminado si esta encendida
        if(lampObject.getLightColor() != OverBaseIndicatorLightModel.LIGHT_COLOR.NONE) {
            lampObject.getGlowingGlassSprite().draw(batch);
        }
    }
}
