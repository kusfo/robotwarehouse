package com.teenagemutantninjacoders.robotwarehouse.domain.models.tutorials;

import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample;

/**
 * Created by JordiM on 13/11/2017.
 */

public class TutorialDataElement {
    String body;
    TutorialSample currentSample;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TutorialSample getCurrentSample() {
        return currentSample;
    }

    public void setCurrentSample(TutorialSample currentSample) {
        this.currentSample = currentSample;
    }
}
