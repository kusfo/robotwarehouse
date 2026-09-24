package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 06/03/2017.
 */
public class BaseUI {
    protected Stage GUIstage;
    private Image fadeScreen;
    private float fadeScreenAlpha = 1;
    private float pauseTime = 0;
    private SCREEN_FADE_STATUS screenFadeStatus = SCREEN_FADE_STATUS.FULL_OUT;

    public BaseUI(Viewport viewport){
        GUIstage = new Stage();
        GUIstage.setViewport(viewport);
    }

    protected void addFadeSystem(){
        fadeScreen = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        fadeScreen.setColor(Color.BLACK);
        fadeScreen.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        fadeScreen.setPosition(0,0);
        fadeScreen.setVisible(true);
        fadeScreen.setTouchable(Touchable.disabled);

        GUIstage.addActor(fadeScreen);
    }

    public Stage getUIStage(){
        return GUIstage;
    }

    protected void updateScreenFade(float delta){
        if(pauseTime <= 0) {
            if (screenFadeStatus == SCREEN_FADE_STATUS.FADING_IN) {
                if (fadeScreenAlpha > 0) {
                    fadeScreenAlpha -= (2 * delta);
                    fadeScreen.setColor(0, 0, 0, fadeScreenAlpha);
                } else {
                    fadeScreenAlpha = 0;
                    screenFadeStatus = SCREEN_FADE_STATUS.FULL_IN;
                    fadeScreen.setVisible(false);
                }
            } else if (screenFadeStatus == SCREEN_FADE_STATUS.FADING_OUT) {
                if (fadeScreenAlpha < 1) {
                    fadeScreenAlpha += (2 * delta);
                    fadeScreen.setColor(0, 0, 0, fadeScreenAlpha);
                } else {
                    fadeScreenAlpha = 1;
                    screenFadeStatus = SCREEN_FADE_STATUS.FULL_OUT;
                }
            }
        } else
            pauseTime -= (1 * delta);
    }

    public void screenFadeIn(){
        screenFadeStatus = SCREEN_FADE_STATUS.FADING_IN;
        fadeScreen.setVisible(true);
    }
    public void screenFadeIn(float pauseTime){
        this.pauseTime = pauseTime;
        screenFadeIn();
    }
    public void screenFadeOut(){
        AudioManager.getInstance().stopMusic(true);
        screenFadeStatus = SCREEN_FADE_STATUS.FADING_OUT;
        fadeScreen.setVisible(true);
    }
    public void screenFadeOut(float pauseTime){
        this.pauseTime = pauseTime;
        screenFadeOut();
    }

    public SCREEN_FADE_STATUS getScreenFadeStatus(){
        return screenFadeStatus;
    }

    public enum SCREEN_FADE_STATUS {
        FADING_IN, FADING_OUT, FULL_IN, FULL_OUT;
    }
}
