package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by jordi on 04/03/2016.
 */
public interface BoardObjectMovementManager {
    void updateObject(MovableBoardObjectModel movableObject, float delta);

    boolean isDirectionObstructed(MovableBoardObjectModel movableObject, MovableBoardObjectModel.DIRECTION direction, float delta);
}
