package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by JordiM on 24/08/2017.
 */

interface EnemyAction {
    void disableEnemy();
    void doEnemyMainAction(MovableBoardObjectModel.DIRECTION targetDirection, BoardObjectModel boardObjectModel);
    boolean shouldChangeAction();
}
