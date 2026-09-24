package com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

/**
 * Created by jordi on 05/03/2016.
 */
public interface LevelModelProvider {
    LevelModel getModelForLevel(int numEpisode, int numLevel);
}
