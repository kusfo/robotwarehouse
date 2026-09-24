package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;

/**
 * Created by JordiRM on 05/04/2016.
 */
public interface CannonManager {
    void updateCannon(CannonModel cannontModel, float delta);
}
