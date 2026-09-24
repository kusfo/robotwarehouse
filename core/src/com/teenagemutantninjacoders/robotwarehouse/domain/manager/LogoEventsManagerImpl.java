package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.LogoScreen;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.BaseUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.LogoEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.MainEventsManager;

/**
 * Created by JordiMontornes on 16/03/2017.
 */
public class LogoEventsManagerImpl implements LogoEventsManager {
    private RobotWarehouseGame game;
    private LogoScreen logoScreen;
    private boolean goToMain;

    public LogoEventsManagerImpl(RobotWarehouseGame game, LogoScreen logoScreen) {
        this.game = game;
        this.logoScreen = logoScreen;
    }

    @Override
    public void addEventGoToMain() {
        goToMain =  true;
    }

    @Override
    public void update(float delta) {
        if(goToMain){
            executeTransition();
        }
    }

    private void executeTransition() {

        if(logoScreen.getLogoUI().getScreenFadeStatus() ==  BaseUI.SCREEN_FADE_STATUS.FULL_IN){
            logoScreen.getLogoUI().screenFadeOut();
        }
        else if(logoScreen.getLogoUI().getScreenFadeStatus() == BaseUI.SCREEN_FADE_STATUS.FULL_OUT) {
            game.goToScreen(RobotWarehouseGame.SCREEN_TYPE.MAIN_SCREEN);
        }

    }
}
