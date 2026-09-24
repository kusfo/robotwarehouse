package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;

/**
 * Created by JordiRM on 16/10/2018.
 */
public class RobotElevatorDraw extends GameObjectDraw  {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        drawSprite(currentGameObject, batch, delta);
        RobotElevatorModel elevator = (RobotElevatorModel) currentGameObject;

        // Dibujamos la plataforma, la posicion la pasará el robot que suba por ella
        elevator.getPlatformSprite().draw(batch);

        // Dibujamos la compuerta solo si no esta completamente abierta
        if(elevator.getRobotElevatorStatus() != RobotElevatorModel.ROBOT_ELEVATOR_STATUS.OPENED) {
            elevator.getDoorAnimation().setPosition(elevator.getPositionX(), elevator.getPositionY());
            elevator.getDoorAnimation().draw(batch, delta);
        }

        if(elevator.isDisabled()){
            elevator.getDisableEffect().setPositionCentered(elevator.getPositionXCenter(), elevator.getPositionYCenter());
            elevator.getDisableEffect().draw(batch, delta);
        }
    }
}
