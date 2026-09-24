package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 24/07/2017.
 */
public class GameObjectDraw {
    public void draw(GameObject currentGameObject, Batch batch, float delta){
        drawSprite(currentGameObject, batch, delta);
    }

    protected void drawSprite(GameObject currentGameObject, Batch batch, float delta){
        currentGameObject.updateAnimation(delta); // Actualizamos su animación antes de pedirle el sprite actual.

        Sprite gameObjectSprite = currentGameObject.getSprite();

        gameObjectSprite.setAlpha(currentGameObject.getAlpha());
        gameObjectSprite.setScale(currentGameObject.getScaleX(), currentGameObject.getScaleY());
        gameObjectSprite.setRotation(gameObjectSprite.getRotation());
        gameObjectSprite.setPosition(currentGameObject.getPositionX() + currentGameObject.getSpriteOffsetX(),
                                     currentGameObject.getPositionY() + currentGameObject.getSpriteOffsetY());
        gameObjectSprite.draw(batch);
    }
}
