package com.teenagemutantninjacoders.robotwarehouse.domain.models.auxiliary;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;

/**
 * Created by JordiM on 29/03/2017.
 */

public class RobotElevatorReference {
    private int id;
    private int priority;
    private int randomValue;

    public RobotElevatorReference(RobotElevatorModel robotElevatorModel) {
        this.id = robotElevatorModel.getElevatorIdentifier();
        this.priority = robotElevatorModel.getPriority();
        this.randomValue = 0;
    }

    public int getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public void setRandomValue(int randomValue) {
        this.randomValue = randomValue;
    }

    public int getRandomValue() {
        return randomValue;
    }
}
