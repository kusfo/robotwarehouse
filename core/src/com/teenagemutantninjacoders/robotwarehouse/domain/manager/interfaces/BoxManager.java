package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;

/**
 * Created by jordi on 04/03/2016.
 */
public interface BoxManager {
    void update(float delta);
    void updateBox(BoxModel boxModel, float delta);
    BoxListener getBoxListener();
}
