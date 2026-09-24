package com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample;

/**
 * Created by JordiM on 29/11/2017.
 */

public interface TutorialSampleProvider {
    TutorialSample getTutorialSample(String sampleId);
}
