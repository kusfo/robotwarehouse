package com.teenagemutantninjacoders.robotwarehouse.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.TimeUtils;
import com.teenagemutantninjacoders.robotwarehouse.display.painters.BasePainter;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUiGameRetrievalListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.SelectionUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AchievementManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.SavedGamesManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.SelectionEventsManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SelectionEventsManager;

/**
 * Created by JordiRM on 10/01/2017.
 */
public class SelectionScreen implements Screen {
    private static final String SELECTION_SCREEN = "selection_screen";
    private BasePainter basePainter;
    private SelectionUI selectionUI;
    private SelectionEventsManager selectionEventsManager;
    private float safeIniTime;
    private boolean initiated = false;
    private final SavedGamesManager savedGamesManager;
    private final AchievementManager achievementManager;

    public SelectionScreen(RobotWarehouseGame game){
        long iniTime = TimeUtils.nanoTime();
        achievementManager = new AchievementManagerImpl(game.playServices, game.trackingServices, game.idProvider);
        selectionEventsManager = new SelectionEventsManagerImpl(this, game, achievementManager);
        savedGamesManager = new SavedGamesManagerImpl(game.playServices);
        savedGamesManager.setSavedGamesListener(new MainUiGameRetrievalListener() {
            @Override
            public void gameRetrieved(boolean success) {

            }

            @Override
            public void gameLoadEnded(boolean success, boolean moreAdvancedSavedGame, long remoteGameTimeStamp, String deviceID) {

            }

            @Override
            public void gameApplied(boolean success) {

            }

            @Override
            public void gameSaved(boolean success) {
                Gdx.app.log("SelectionScreen","Game has been saved");
            }

            @Override
            public void gameDeleted(boolean success) {

            }
        });

        basePainter = new BasePainter();
        selectionUI = new SelectionUI(this, basePainter.getViewport(), savedGamesManager, achievementManager);

        Gdx.input.setInputProcessor(selectionUI.getUIStage());
        game.trackingServices.trackScreen(SELECTION_SCREEN);

        safeIniTime = (float)(( TimeUtils.nanoTime() - iniTime) / 1000000000.0);
        Gdx.app.log("Selection Initialization Time", "" + safeIniTime);
        safeIniTime += 0.3f;
    }

    public void stopInteraction(){
        selectionUI.setActorsTouchables(false);
    }

    public SelectionEventsManager getSelectionEventsManager(){
        return selectionEventsManager;
    }

    public SelectionUI getSelectionUI(){
        return selectionUI;
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
                selectionUI.setSafeToRun();
            }
        }
        basePainter.initFrame();
        selectionUI.update(delta);
        selectionEventsManager.update(delta);
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
        selectionUI.dispose();
    }
}
