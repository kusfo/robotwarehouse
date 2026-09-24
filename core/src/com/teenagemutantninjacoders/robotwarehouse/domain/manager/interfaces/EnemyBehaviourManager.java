package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RatlienLairModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

/**
 * Created by jordi.montornes on 05/04/2016.
 */
public interface EnemyBehaviourManager {
    void update(float delta);
    void updateEnemy(EnemyModel enemyModel, float delta);
    void updateRobotElevator(RobotElevatorModel robotElevatorModel, LevelModel levelModel, float delta);
    void increaseRobotElevatorInternalCounter(float delta);
    void increaseRatlienLairInternalCounter(float delta);
}
