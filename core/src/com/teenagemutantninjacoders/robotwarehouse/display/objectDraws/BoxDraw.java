package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;

/**
 * Created by JordiRM on 17/11/2017.
 */
public class BoxDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta){
        BoxModel boxModel = (BoxModel) currentGameObject;
        drawSprite(currentGameObject, batch, delta);

        if(boxModel.getFlashAlpha() > 0){
            boxModel.getFlashSprite().setPosition(currentGameObject.getPositionX() - 4,currentGameObject.getPositionY() - 4);
            boxModel.getFlashSprite().draw(batch);
        }

        if(boxModel.getInGroup()){
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_CONSTANT_COLOR);
                boxModel.getGlowSprite().setPosition(currentGameObject.getPositionX(),currentGameObject.getPositionY());
                boxModel.getGlowSprite().draw(batch);

                if(!boxModel.getBoxSparkAnimation().isAnimationFinished()) {
                    boxModel.getBoxSparkAnimation().setPosition(currentGameObject.getPositionX() - 9, currentGameObject.getPositionY() - 4);
                    boxModel.getBoxSparkAnimation().draw(batch, delta);
                }
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            // Shiny Icon
            boxModel.getShinyIcon().setPosition(currentGameObject.getPositionX(),currentGameObject.getPositionY());
            boxModel.getShinyIcon().setAlpha(boxModel.getAlpha());
            boxModel.getShinyIcon().draw(batch);
        }
        if(boxModel.haveRatlienAttachedUp()) {
            boxModel.getRatlienAttachedUpAnimation().setPosition(currentGameObject.getPositionX(), currentGameObject.getPositionY());
            boxModel.getRatlienAttachedUpAnimation().draw(batch, delta);
        }
        if(boxModel.haveRatlienAttachedLeft()) {
            boxModel.getRatlienAttachedLeftAnimation().setPosition(currentGameObject.getPositionX() - 32, currentGameObject.getPositionY());
            boxModel.getRatlienAttachedLeftAnimation().draw(batch, delta);
        }
        if(boxModel.haveRatlienAttachedRight()) {
            boxModel.getRatlienAttachedRightAnimation().setPosition(currentGameObject.getPositionX(), currentGameObject.getPositionY());
            boxModel.getRatlienAttachedRightAnimation().draw(batch, delta);
        }
        if(boxModel.haveRatlienAttachedDown()) {
            boxModel.getRatlienAttachedDownAnimation().setPosition(currentGameObject.getPositionX(), currentGameObject.getPositionY() - 25);
            boxModel.getRatlienAttachedDownAnimation().draw(batch, delta);
        }
    }
}
