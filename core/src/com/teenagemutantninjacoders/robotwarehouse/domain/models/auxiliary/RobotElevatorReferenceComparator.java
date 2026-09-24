package com.teenagemutantninjacoders.robotwarehouse.domain.models.auxiliary;

import java.util.Comparator;

/**
 * Created by JordiM on 31/03/2017.
 */

public class RobotElevatorReferenceComparator implements Comparator<RobotElevatorReference> {
    @Override
    public int compare(RobotElevatorReference elevatorReference1, RobotElevatorReference elevatorReference2) {
        return elevatorReference1.getRandomValue() < elevatorReference2.getRandomValue() ? -1 :
                (elevatorReference1.getRandomValue() == elevatorReference2.getRandomValue() ? 0 : 1);
    }
}
