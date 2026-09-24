package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 08/11/2016.
 */
public class GUIobjectGameOverPanel extends GUIobjectBaseLevelPanel {
    private Group layer;
    private Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_16;
    Button buttonReplay, buttonReturn;
    private Group groupGameOverPanel;
    private Color c_textYellow;
    private TextureAtlas panelAtlas, buttonsAtlas;
    public GUIobjectGameOverPanel(GameEventsManager eventsManager,  Group layer){
        this.eventsManager = eventsManager;
        this.layer = layer;
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        c_textYellow = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        panelAtlas = Assets.getTextureAtlas("gameOverPanel");
        buttonsAtlas = Assets.getTextureAtlas("general_buttons");
        execute();
    }
    private void execute() {
        Label labelMissionFailed, labelDescription;
        width = panelAtlas.findRegion("frame").getRegionWidth();
        height = panelAtlas.findRegion("frame").getRegionHeight();
        int screenWidth = panelAtlas.findRegion("screen").getRegionWidth();
        int screenHeight = panelAtlas.findRegion("screen").getRegionHeight();
        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 25;// - 20;
        groupGameOverPanel = new Group();
        groupGameOverPanel.setSize(width, height);
        groupGameOverPanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 100);

        Group screenGroup = new Group();
        screenGroup.setSize(screenWidth, screenHeight);
        screenGroup.setPosition(28, 60);

        Image imageGameOverFrame = new Image(panelAtlas.findRegion("frame"));

        AnimatedImageActor animatedImagePanelScreen = new AnimatedImageActor(panelAtlas, "screen", 0.1f, Animation.PlayMode.LOOP);
        animatedImagePanelScreen.play();

        Image backgroundIcon = new Image();

        labelMissionFailed = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_title"), labelStyle_base_gb_22);
        labelMissionFailed.setColor(c_textYellow);
        labelMissionFailed.setAlignment(Align.center);
        labelMissionFailed.setPosition((screenWidth/2) - (labelMissionFailed.getWidth() / 2), screenHeight - 71);

        int despX = 0;
        String texto;
        switch(GlobalLevelData.getInstance().getLevelEvent()){
            case TOO_MANY_LOSSES:
                texto = GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_body_many_losses");
                break;
            case WITHOUT_BOXES: default:
                texto = GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_body_without_boxes");
                backgroundIcon = new Image(panelAtlas.findRegion("icon_box"));
                break;
            case TIME_OUT:
                texto = GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_body_time_out");
                backgroundIcon = new Image(panelAtlas.findRegion("icon_clock"));
                despX = -4;
                break;
        }

        backgroundIcon.setColor(1, 1, 1, 0.2f);
        backgroundIcon.setScale(1.5f, 1.5f);
        backgroundIcon.setPosition((screenWidth/2) - ((backgroundIcon.getWidth() * backgroundIcon.getScaleX()) / 2) + despX, 0);

        labelDescription = new Label(texto, labelStyle_base_gb_16);
        labelDescription.setWrap(true);
        labelDescription.setWidth(220);
        labelDescription.setAlignment(Align.center);
        labelDescription.setPosition((screenWidth/2) - (labelDescription.getWidth() / 2), 50);
        labelDescription.setColor(1,1,1,0.85f);

        screenGroup.addActor(animatedImagePanelScreen);
        screenGroup.addActor(backgroundIcon);
        screenGroup.addActor(labelMissionFailed);
        screenGroup.addActor(labelDescription);

        groupGameOverPanel.addActor(screenGroup);
        groupGameOverPanel.addActor(imageGameOverFrame);

        //Boton Selection
        Drawable buttonUp = new Image(buttonsAtlas.findRegion("panelButtonList", 1)).getDrawable();
        Drawable buttonDown = new Image(buttonsAtlas.findRegion("panelButtonList", 2)).getDrawable();

        buttonReturn = new ImageButton(buttonUp, buttonDown);
        buttonReturn.setPosition(109, 21);
        buttonReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    buttonActivated = BUTTON_ACTION.SELECTION_SCREEN;
                    leave(groupGameOverPanel);
                    eventsManager.makeCharacterLeave();
                }
            }
        });
        groupGameOverPanel.addActor(buttonReturn);

        //Boton Restart
        buttonUp = new Image(buttonsAtlas.findRegion("panelButtonRestart", 1)).getDrawable();
        buttonDown = new Image(buttonsAtlas.findRegion("panelButtonRestart", 2)).getDrawable();

        buttonReplay = new ImageButton(buttonUp, buttonDown);
        buttonReplay.setPosition(183, 21);
        buttonReplay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    buttonActivated = BUTTON_ACTION.RESTART_LEVEL;
                    leave(groupGameOverPanel);
                    eventsManager.makeCharacterLeave();
                }
            }
        });
        groupGameOverPanel.addActor(buttonReplay);
        layer.addActor(groupGameOverPanel);
        enter(groupGameOverPanel);
    }
}
