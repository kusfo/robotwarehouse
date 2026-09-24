package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.GameObjectDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 07/11/2017.
 */
public class SpaceShipDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta){
        SpaceShip spaceShip = (SpaceShip) currentGameObject;

        // Dibujamos la parte frontal de la nave, la base
        drawSprite(currentGameObject, batch, delta);

        // Dibujamos los contenedores
        if(spaceShip.hasAssignedContainers()) {
            spaceShip.getContainer(2).draw(batch);
            spaceShip.getContainer(1).draw(batch);
            spaceShip.getContainer(0).draw(batch);
        }

        // Dibujamos la parte de atras, los motores
        spaceShip.getShipEngines().draw(batch);

        // Engine Glow
        spaceShip.getEngineGlow().setAlpha(spaceShip.getEngineGlowAlpha());
        spaceShip.getEngineGlow().draw(batch);

        // Dibujamos los propulsores si es necesario
        if(spaceShip.getEngineEvent() != SpaceShip.ENGINE_EVENT.INACTIVE ){
            spaceShip.getAnimatedEngineFire().draw(batch, delta);
        }
    }
}
