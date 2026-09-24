package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;

/**
 * Created by JordiRM on 04/07/2016.
 */
public interface OverBaseManager {
    void update(float delta);
    void updateOverBase(OverBaseBoxActivableModel OverBaseBoxActivableModel, float delta);
}
