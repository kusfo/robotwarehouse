package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectCharacterDialogue;

/**
 * Created by JordiRM on 04/10/2019.
 */
public class DialogueEventDTO {
    private String event;
    private float value1 = 0;
    private float value2 = 0;
    public DialogueEventDTO(){}

    public GUIobjectCharacterDialogue.DIALOGUE_EVENT getEvent() {
        return GUIobjectCharacterDialogue.DIALOGUE_EVENT.fromString(event);
    }

    public void setEvent(GUIobjectCharacterDialogue.DIALOGUE_EVENT event) {
        this.event = event.getValue();
    }

    public float getValue1() {
        return value1;
    }

    public void setValue1(float value1) {
        this.value1 = value1;
    }

    public float getValue2() {
        return value2;
    }

    public void setValue2(float value2) {
        this.value2 = value2;
    }
}
