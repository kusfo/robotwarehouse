package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;

/**
 * Created by JordiRM on 21/03/2018.
 */
public class CannonDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        // Dibujamos la vagoneta
        CannonModel cannonModel = (CannonModel) currentGameObject;
        drawSprite(currentGameObject, batch, delta);

        // Dibujamos la torreta
        Sprite turretSprite = cannonModel.getTurretSprite();
        turretSprite.setPosition(cannonModel.getPositionX() + cannonModel.getTurretPosition().x + cannonModel.getSpriteOffsetX(),
                cannonModel.getPositionY() + cannonModel.getTurretPosition().y + cannonModel.getSpriteOffsetY());
        turretSprite.draw(batch);

        // Si esta disparando, dibujamos el laser y el efecto de impacto
        if(cannonModel.isShooting()){
            cannonModel.getLaserSprite().draw(batch);
            cannonModel.getImpactEffect().getSprite().setScale(cannonModel.getLaserSprite().getScaleX(), cannonModel.getLaserSprite().getScaleY());
            cannonModel.getImpactEffect().draw(batch, delta);
        }

        // Efecto de desactivación
        if(cannonModel.getCannonStatus() == CannonModel.CANNON_STATUS.DISABLED){
            cannonModel.getDisableEffect().setPositionCentered(cannonModel.getPositionXCenter(), cannonModel.getPositionYCenter() + 16);
            cannonModel.getDisableEffect().draw(batch, delta);
        }
    }
}
