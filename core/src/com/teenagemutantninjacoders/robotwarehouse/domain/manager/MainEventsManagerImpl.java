package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.Gdx;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.BaseUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.MainEventsManager;

import de.golfgl.gdxgamesvcs.GameServiceException;

/**
 * Created by JordiRM on 25/01/2017.
 */
public class MainEventsManagerImpl implements MainEventsManager {
    private RobotWarehouseGame game;
    private MainUI mainUI;
    private boolean isGoingToSelection = false;
    private boolean isGoingToGame = false;

    public MainEventsManagerImpl(RobotWarehouseGame game) {
        this.game = game;
        AudioManager.getInstance().playMusic(AudioManager.MUSIC.MAIN_SCREEN, true, true);
    }

    public void addEventGoToSelection() {
        isGoingToSelection = true;
    }
    public void addEventGoToGame() {
        isGoingToGame = true;
    }

    @Override
    public void update(){
        if(isGoingToSelection){
            if(mainUI.getScreenFadeStatus() == BaseUI.SCREEN_FADE_STATUS.FULL_OUT &&
                    !AudioManager.getInstance().isMusicPlaying()) {
                game.goToScreen(RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN);
            }
        }
        else if(isGoingToGame){
            if(mainUI.getScreenFadeStatus() == BaseUI.SCREEN_FADE_STATUS.FULL_OUT &&
                    !AudioManager.getInstance().isMusicPlaying()) {
                game.goToScreen(RobotWarehouseGame.SCREEN_TYPE.GAME_SCREEN);
            }
        }
    }

    @Override
    public void addEventDisplayAchievements() {
        try {
            game.playServices.showAchievements();
        } catch (GameServiceException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addEventDisplayScore() {
        try {
            game.playServices.showLeaderboards(game.getLeaderBoardId());
        } catch (GameServiceException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void makeGameCrash() {
        game.platformServices.testCrash();
    }

    @Override
    public void setMainUI(MainUI mainUI){
        this.mainUI = mainUI;
    }
}
