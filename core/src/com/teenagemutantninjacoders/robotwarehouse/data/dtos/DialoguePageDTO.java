package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import java.util.ArrayList;

/**
 * Created by JordiRM on 13/09/2018.
 */
public class DialoguePageDTO {
    private String textReference;
    private String pose;
    private ArrayList<DialogueEventDTO> events;
    private float waitTime = 0;
    private boolean automatic = false;
    public DialoguePageDTO(){}
    public DialoguePageDTO(String textReference, String pose){
        this.textReference = textReference;
        this.pose = pose;
        //events = new ArrayList<String>();
        events = new ArrayList<DialogueEventDTO>();
    }
    public String getTextReference(){
        return textReference;
    }
    public String getPose(){
        return pose;
    }

    public void setTextReference(String textReference) {
        this.textReference = textReference;
    }

    public void setPose(String pose) {
        this.pose = pose;
    }

    public ArrayList<DialogueEventDTO> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<DialogueEventDTO> events) {
        this.events = events;
    }

    public float getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(float waitTime) {
        this.waitTime = waitTime;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }
}
