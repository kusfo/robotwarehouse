package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;

/**
 * Created by JordiRM on 30/04/2016.
 */
public interface GameEventsManager {
    void beginLevelEnter(float safePauseTime);
    void update(float delta);

    void addNewBoxPointsEvent(float x, float y, int combo);
    void addNewOverBasePointsEvent(float x, float y, boolean triggerAction);
    void addNewOverBasePowerEvent(float x, float y, GlobalAttributes.POWER power);

    void addEnterPauseMenu();
    void addLeavePauseMenu();

    void addGameZoneFade(boolean state, float speed);

    void addFloatingLostBox(float x, float y);
    void makeCharacterLeave();

    void changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE screenType);
    void finalizeOverBasePointsEvent();
    void finalizeBoxPointsEvent();
    void finalizePowerActivationEvent(GlobalAttributes.POWER power);

    void addChangeInBoxesEvent();
    void addColorDisablingEvent(GlobalAttributes.COLOR color);

    void processLevelEnding();

    void addNewLevelEnterEvent();
    void addNewLevelStartEvent();
    void addNewLevelCompletedEvent();
    void addNewLevelGameOverEvent();
    void addSelectionScreenAfterAdEvent();
    void addRestartLevelEvent();
    void addFinishLevelIntroductionEvent();

    void deploySpaceDockMonitor(boolean status);
    void deployScorePanels(float delay);
    void retractScorePanels(float delay);
    void deployPauseButtonPanel(float delay);
    void submitNewScore(int score);
    void enemyDestroyed(GlobalAttributes.CHALLENGE_OBJECT enemy);
    void executeScenarioEvent(int eventNumber);

    void showWaitingAchievements();
}
