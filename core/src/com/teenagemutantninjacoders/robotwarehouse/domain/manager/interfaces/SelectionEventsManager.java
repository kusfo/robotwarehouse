package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

/**
 * Created by JordiRM on 10/01/2017.
 */
public interface SelectionEventsManager {
    void addEventGoToLevel(int level);
    void addEventGoToMain();
    void goToPanel(int number);
    void showWaitingAchievements();
    void update(float delta);
}
