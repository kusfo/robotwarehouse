package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;

public class RatlienDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        RatlienModel ratlienModel = (RatlienModel) currentGameObject;
        drawSprite(currentGameObject, batch, delta);
    }
}
