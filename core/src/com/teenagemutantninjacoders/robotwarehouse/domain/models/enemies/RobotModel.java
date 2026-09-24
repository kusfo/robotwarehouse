package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;

/**
 * Created by JordiM on 10/02/2017.
 */
public class RobotModel extends EnemyModel{
    private RobotElevatorModel entryElevator;
    public RobotModel(int h, int v) {
        super(h, v);

        setOnBoard(false);
        setRowDepthDependent(false);
        // Ajustamos su depth inicial sobre el suelo para que parezca que sale desde abajo.
        setDepth(MathUtils.floor( GameConstants.FLOOR_DEPTH + getPositionY()) - 10);
        setPositionY(getCellPositionY(getRow()) - 33);//37
        setEnemyStatus(ENEMY_STATUS.ENTERING);
    }

    public RobotElevatorModel getEntryElevator() {
        return entryElevator;
    }

    public void setEntryElevator(RobotElevatorModel entryElevator) {
        this.entryElevator = entryElevator;
    }

    @Override
    public boolean shouldChangeAction() {
        if(getColumn() != getOldColumn()
                || getRow() != getOldRow()) {
            int randomValue = MathUtils.random(0, 5);
            if(randomValue == 0)
                return true;
        }
        return false;
    }
}
