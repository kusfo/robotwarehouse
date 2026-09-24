package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectBoxTrail;

/**
 * Created by JordiRM on 19/04/2019.
 */
public class SpriteEffectBoxTrailDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        SpriteEffectBoxTrail boxTrail = (SpriteEffectBoxTrail) currentGameObject;
        if(boxTrail.getTrailsList().size() > 0) {
            if( boxTrail.getBoxModel() != null) {
                for (int i = 0; i < boxTrail.getTrailsList().size(); i++) {
                    boxTrail.getBoxModel().getSprite().setAlpha(boxTrail.getTrailsList().get(i).getAlpha());
                    boxTrail.getBoxModel().getSprite().setPosition(boxTrail.getTrailsList().get(i).getPosition().x, boxTrail.getTrailsList().get(i).getPosition().y);
                    boxTrail.getBoxModel().getSprite().draw(batch);
                }
            }
        }
    }
}
