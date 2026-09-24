package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by jordi on 04/03/2016.
 */
public interface BoardObjectTerminatorManager {
    void updateObject(BoardObjectModel boardObjectModel, float delta);
}
