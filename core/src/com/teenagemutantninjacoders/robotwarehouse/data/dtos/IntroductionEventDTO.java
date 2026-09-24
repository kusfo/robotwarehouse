package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import java.util.ArrayList;

/**
 * Created by JordiRM on 20/09/2018.
 */
public class IntroductionEventDTO {
    private boolean singleview;
    private ArrayList<IntroductionSubEventDTO> subEvents;
    public IntroductionEventDTO(){
        subEvents = new ArrayList<IntroductionSubEventDTO>();
        singleview = true;
    }

    public ArrayList<IntroductionSubEventDTO> getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(ArrayList<IntroductionSubEventDTO> events) {
        this.subEvents = events;
    }

    public boolean isSingleview() {
        return singleview;
    }

    public void setSingleview(boolean isSingleview) {
        singleview = isSingleview;
    }
}
