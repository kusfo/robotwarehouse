package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;

/**
 * Created by JordiRM on 19/12/2017.
 */
public class OverBaseBoxActivableDraw extends GameObjectDraw{
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta){
        drawSprite(currentGameObject, batch, delta);

        if ( !((OverBaseBoxActivableModel) currentGameObject).isActivated()){
            Sprite glowSprite = ((OverBaseBoxActivableModel) currentGameObject).getGlowSprite();
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_CONSTANT_COLOR);
            glowSprite.draw(batch);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }
}
