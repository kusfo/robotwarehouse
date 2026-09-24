package com.teenagemutantninjacoders.robotwarehouse.data.parsers;

import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials.TutorialDTO;

/**
 * Created by JordiM on 29/11/2017.
 */

public class TutorialParser {
    public static TutorialDTO parseTutorial(String tutorialJson) {
        Json json = new Json();
        TutorialDTO tutorialDTO = json.fromJson(TutorialDTO.class, tutorialJson);
        return tutorialDTO;
    }
}
