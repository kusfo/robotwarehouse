package com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials;

/**
 * Created by JordiM on 11/10/2017.
 */

public class TutorialDataElementDTO {
    String labeltext;
    String sampleId;

    public void setLabeltext(String labeltext) {
        this.labeltext = labeltext;
    }

    public void setSampleId(String animationId) {
        this.sampleId = animationId;
    }

    public String getLabeltext() {
        return labeltext;
    }

    public String getSampleId() {
        return sampleId;
    }
}
