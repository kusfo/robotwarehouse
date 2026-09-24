package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;

/**
 * Created by JordiM on 09/08/2017.
 */

public interface PowerManager {
    void update(float delta);
    void executePower(GlobalAttributes.POWER power);
}
