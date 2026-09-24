package com.teenagemutantninjacoders.robotwarehouse.display.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Timer;
import com.teenagemutantninjacoders.robotwarehouse.display.painters.BasePainter;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.LogoUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.LogoEventsManagerImpl;

/**
 * Created by jordimontornes on 09/03/2017.
 */

public class LogoScreen implements Screen {
    private static final String LOGO_SCREEN = "logo_screen";
    private RobotWarehouseGame game;
    private BasePainter basePainter;
    private LogoUI logoUI;
    private float logoTime = 3.5f;

    private final LogoEventsManagerImpl logoEventsManager;

    public LogoScreen(final RobotWarehouseGame game) {
        this.game = game;
        basePainter = new BasePainter();
        logoUI = new LogoUI(basePainter.getViewport());
        logoEventsManager = new LogoEventsManagerImpl(this.game,this);

        Gdx.input.setInputProcessor(logoUI.getUIStage());

        game.trackingServices.trackScreen(LOGO_SCREEN);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
            logoEventsManager.addEventGoToMain();
            }
        }, logoTime);
    }

    public LogoUI getLogoUI() {
        return logoUI;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        basePainter.initFrame();
        logoUI.update(delta);
        logoEventsManager.update(delta);
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
        logoUI.dispose();
    }
}
