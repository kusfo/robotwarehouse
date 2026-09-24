package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUI;

/**
 * Created by JordiRM on 25/01/2017.
 */
public interface MainEventsManager {
    void update();
    void addEventGoToSelection();
    void addEventGoToGame();
    void addEventDisplayAchievements();
    void addEventDisplayScore();
    void makeGameCrash();
    void setMainUI(MainUI mainUI);
}
