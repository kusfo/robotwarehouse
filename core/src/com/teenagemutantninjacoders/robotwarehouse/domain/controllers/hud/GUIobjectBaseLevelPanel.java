package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 22/11/2016.
 */
public class GUIobjectBaseLevelPanel extends GUIobjectBasePanel {
    protected GameEventsManager eventsManager;
    BUTTON_ACTION buttonActivated = BUTTON_ACTION.NONE;

    @Override
    protected void FinishPanel(){
        switch(buttonActivated){
            case SELECTION_SCREEN:
                eventsManager.changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN); //addRestartLevelEvent(); // (!) De momento, como no hay lista a la que volver, actua como restart
                break;
            case RESTART_LEVEL:
                eventsManager.addRestartLevelEvent();
                break;
            case PLAY:
                eventsManager.addNewLevelStartEvent();
                break;
        }
    }

    protected enum BUTTON_ACTION{
        NONE, SELECTION_SCREEN, RESTART_LEVEL, PLAY
    }
}