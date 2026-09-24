package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.SelectionScreen;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.BaseUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SelectionEventsManager;

import java.util.ArrayList;

/**
 * Created by JordiRM on 10/01/2017.
 */
public class SelectionEventsManagerImpl implements SelectionEventsManager{
    private RobotWarehouseGame game;
    private AchievementManager achievementManager;
    private SelectionScreen selectionScreen;
    private class SelectionEvent {
        private SELECTION_EVENT event;
        public SelectionEvent(SELECTION_EVENT newEvent){
            this.event = newEvent;
        }
        public SELECTION_EVENT getEvent() {
            return event;
        }
    }

    private class GoToLevelEvent extends SelectionEvent {
        private int level;
        public GoToLevelEvent(SELECTION_EVENT newEvent, int level) {
            super(newEvent);
            this.level = level;
        }
        public int getLevel(){
            return level;
        }
    }

    private ArrayList<SelectionEvent> selectionEvents = new ArrayList<SelectionEvent>();


    public SelectionEventsManagerImpl(SelectionScreen selectionScreen, RobotWarehouseGame game, AchievementManager achievementManager){
        this.game = game;
        this.achievementManager = achievementManager;
        this.selectionScreen = selectionScreen;
    }

    @Override
    public void update(float delta){
        if(selectionEvents.size() > 0){
            ExecuteEvents();
        }
    }

    private void ExecuteEvents() {
        for (int i = 0; i < selectionEvents.size(); i++) {
            switch (selectionEvents.get(i).getEvent()) {

                case GO_TO_LEVEL:
                    if(selectionScreen.getSelectionUI().getScreenFadeStatus() ==  BaseUI.SCREEN_FADE_STATUS.FULL_IN){
                        selectionScreen.getSelectionUI().screenFadeOut();
                        selectionScreen.stopInteraction();
                        AudioManager.getInstance().stopMusic(true);
                    }
                    else if(selectionScreen.getSelectionUI().getScreenFadeStatus() == BaseUI.SCREEN_FADE_STATUS.FULL_OUT) {
                        if(!AudioManager.getInstance().isMusicPlaying()) {
                            GlobalGeneralData.getInstance().setCurrentLevel(((GoToLevelEvent) selectionEvents.get(i)).getLevel());
                            GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.ENTERING);
                            game.goToScreen(RobotWarehouseGame.SCREEN_TYPE.GAME_SCREEN);
                        }
                    }
                    break;
                case GO_TO_MAIN:
                    if(selectionScreen.getSelectionUI().getScreenFadeStatus() ==  BaseUI.SCREEN_FADE_STATUS.FULL_IN){
                        selectionScreen.getSelectionUI().screenFadeOut();
                        selectionScreen.stopInteraction();
                        AudioManager.getInstance().stopMusic(true);
                    }
                    else if(selectionScreen.getSelectionUI().getScreenFadeStatus() == BaseUI.SCREEN_FADE_STATUS.FULL_OUT) {
                        if(!AudioManager.getInstance().isMusicPlaying()) {
                            game.goToScreen(RobotWarehouseGame.SCREEN_TYPE.MAIN_SCREEN);
                        }
                    }
                    break;
            }
        }
    }

    public void addEventGoToLevel(int level){
        selectionEvents.add(new GoToLevelEvent(SELECTION_EVENT.GO_TO_LEVEL, level));
    }

    public void addEventGoToMain(){
        if(!existGameEvent(SELECTION_EVENT.GO_TO_MAIN)) {
            selectionEvents.add(new SelectionEvent(SELECTION_EVENT.GO_TO_MAIN));
        }
    }

    public void goToPanel(int number){
        selectionScreen.getSelectionUI().setSelectionPanel(number);
    }

    public void showWaitingAchievements(){
        achievementManager.executeWaitingAchievements();
    }

    private boolean existGameEvent(SelectionEventsManagerImpl.SELECTION_EVENT event){
        for (SelectionEventsManagerImpl.SelectionEvent eventInList: selectionEvents) {
            if( eventInList.getEvent() == event) {
                return true;
            }
        }
        return false;
    }

    private enum SELECTION_EVENT {
        GO_TO_MAIN, GO_TO_LEVEL;
    }
}
