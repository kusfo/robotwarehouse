package com.teenagemutantninjacoders.robotwarehouse.display.objectDraws;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RobotModel;

/**
 * Created by JordiRM on 13/04/2018.
 */
public class PusherRobotDraw extends GameObjectDraw {
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta) {
        RobotModel robot = (RobotModel) currentGameObject;
        drawSprite(currentGameObject, batch, delta);

        if(robot.getEnemyStatus() == EnemyModel.ENEMY_STATUS.DISABLED){
            robot.getDisableEffect().setPositionCentered(robot.getPositionXCenter(), robot.getPositionYCenter());
            robot.getDisableEffect().draw(batch, delta);
        }
    }
}
