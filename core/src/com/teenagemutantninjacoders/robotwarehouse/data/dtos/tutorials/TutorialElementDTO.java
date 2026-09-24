package com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials;

import java.util.ArrayList;

/**
 * Created by JordiM on 29/11/2017.
 */

public class TutorialElementDTO {
    String id;
    ArrayList<TutorialDataElementDTO> elements;

    public String getId() {
        return id;
    }

    public ArrayList<TutorialDataElementDTO> getElements() {
        return elements;
    }
}
