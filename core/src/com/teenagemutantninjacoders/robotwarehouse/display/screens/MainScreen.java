package com.teenagemutantninjacoders.robotwarehouse.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;
import com.teenagemutantninjacoders.robotwarehouse.display.painters.BasePainter;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AchievementManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.MainEventsManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.SavedGamesManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.MainEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;

/**
 * Created by JordiRM on 25/01/2017.
 */
public class MainScreen implements Screen {
    private static final String MAIN_SCREEN = "main_screen";
    private RobotWarehouseGame game;
    private BasePainter basePainter;
    private MainUI mainUI;
    private MainEventsManager mainEventsManager;
    private SavedGamesManager savedGamesManager;
    private AchievementManagerImpl achievementManager;
    private boolean initiated = false;
    private float safeIniTime;
    public MainScreen(RobotWarehouseGame game){
        this.game = game;
        long iniTime = TimeUtils.nanoTime();
        basePainter = new BasePainter();
        mainEventsManager = new MainEventsManagerImpl(game);
        savedGamesManager = new SavedGamesManagerImpl(game.playServices);
        achievementManager = new AchievementManagerImpl(game.playServices, game.trackingServices, game.idProvider);
        mainUI = new MainUI(basePainter.getViewport(), mainEventsManager, savedGamesManager, achievementManager, game.playServices, game.isDebugBuild(), game.getPlatformServices());
        mainEventsManager.setMainUI(mainUI);
        Gdx.input.setInputProcessor(mainUI.getUIStage());
        game.trackingServices.trackScreen(MAIN_SCREEN);

        safeIniTime = (float)(( TimeUtils.nanoTime() - iniTime) / 1000000000.0);
        Gdx.app.log("Main Initialization Time", "" + safeIniTime);
        safeIniTime += 0.3f;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(!initiated){
            if(safeIniTime > 0)
                safeIniTime -= (1 * delta);
            else {
                initiated = true;
                mainUI.startDeployingElements();
            }
        }

        basePainter.initFrame();
        mainUI.update(delta);
        mainEventsManager.update();
        AudioManager.getInstance().update(delta);
    }

    @Override
    public void resize(int width, int height) {
        basePainter.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        mainUI.dispose();
    }
}
