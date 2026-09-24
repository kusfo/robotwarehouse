package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 09/03/2016.
 */
public interface FloorEventsManager {
    void updateBoardObject(BoardObjectModel boardObject, BoardModel boardModel, float delta);
}
