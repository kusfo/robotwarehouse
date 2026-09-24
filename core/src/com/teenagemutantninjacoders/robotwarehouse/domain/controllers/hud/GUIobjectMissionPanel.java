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
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 25/10/2016.
 */
public class GUIobjectMissionPanel extends GUIobjectBaseLevelPanel {
    private Group layer;
    private Label.LabelStyle labelSyle_base_gb_22, labelSyle_base_gb_16;
    private Group groupMissionPanel;
    private String requiredBoxesString;
    private Color c_titulo, c_timeMultiplier;
    private GlobalAttributes.CHALLENGE challenge;
    private TextureAtlas panelAtlas, buttonsAtlas;

    public GUIobjectMissionPanel(GameEventsManager eventsManager, Group layer){
        this.eventsManager = eventsManager;
        this.layer = layer;
        labelSyle_base_gb_22 = new Label.LabelStyle();
        labelSyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelSyle_base_gb_16 = new Label.LabelStyle();
        labelSyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_timeMultiplier = new Color(154 / 255f, 242 / 255f, 216 / 255f, 0.90f);
        panelAtlas = Assets.getTextureAtlas("missionPanel");
        buttonsAtlas = Assets.getTextureAtlas("general_buttons");

        Integer requiredBoxes = GlobalLevelData.getInstance().getRequestedBoxes();
        requiredBoxesString = Integer.toString(requiredBoxes);

        challenge = GlobalLevelData.getInstance().getActualChallenge();
        if(challenge == GlobalAttributes.CHALLENGE.NONE)
            execute_without_challenge();
        else
            execute_with_challenge();
    }

    private void execute_without_challenge(){
        Label labelMission, labelBoxes, labelTime, labelTimeMultiplier;
        float screenWidth, screenHeight;
        width = panelAtlas.findRegion("missionSimplePanelFrame").getRegionWidth();
        height = panelAtlas.findRegion("missionSimplePanelFrame").getRegionHeight();// - 20;
        screenWidth = panelAtlas.findRegion("missionSimplePanelScreen").getRegionWidth();
        screenHeight = panelAtlas.findRegion("missionSimplePanelScreen").getRegionWidth();

        float screenCenterX = (screenWidth/2);

        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 25; //- 20;
        groupMissionPanel = new Group();
        groupMissionPanel.setSize(width, height);
        groupMissionPanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 100);
        Group groupMissionScreen = new Group();
        groupMissionScreen.setSize(screenWidth, screenHeight);
        groupMissionScreen.setPosition(28, 60);

        Image imageMisionPanelFrame = new Image(panelAtlas.findRegion("missionSimplePanelFrame"));
        AnimatedImageActor animatedImageMissionPanelScreen = new AnimatedImageActor(panelAtlas, "missionSimplePanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageMissionPanelScreen.play();

        groupMissionScreen.addActor(animatedImageMissionPanelScreen);

        labelMission = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().format("sector_title", GlobalGeneralData.getInstance().getCurrentLevel()) , labelSyle_base_gb_22);
        labelMission.setPosition(screenCenterX - (labelMission.getWidth() / 2), 140);
        labelMission.setColor(c_titulo);

        Image imageBoxIcon = new Image(panelAtlas.findRegion("icon_box_light"));
        imageBoxIcon.setPosition(screenCenterX - 54 - (imageBoxIcon.getWidth() / 2), 58);
        Image imageClockIcon = new Image(panelAtlas.findRegion("icon_clock_light"));
        imageClockIcon.setPosition(screenCenterX + 54 - (imageClockIcon.getWidth() / 2), 58);

        labelBoxes = new Label(requiredBoxesString, labelSyle_base_gb_22);
        labelBoxes.setPosition(screenCenterX - 54 - (labelBoxes.getWidth() / 2) - 3, 33);
        labelBoxes.setColor(1,1,1,0.85f);
        labelTime = new Label(Utils.getTimeFormatted(GlobalLevelData.getInstance().getLevelTime()), labelSyle_base_gb_22);
        labelTime.setPosition(screenCenterX + 54 - (labelTime.getWidth() / 2) - 3, 33);
        labelTime.setColor(1,1,1,0.85f);
        labelTimeMultiplier = new Label("x" + GlobalLevelData.getInstance().getTimeMultiplier(), labelSyle_base_gb_16);
        labelTimeMultiplier.setPosition(screenCenterX + 58 - (labelTimeMultiplier.getWidth() / 2) - 3, 64);
        labelTimeMultiplier.setColor(c_timeMultiplier);

        groupMissionScreen.addActor(labelMission);
        groupMissionScreen.addActor(imageBoxIcon);
        groupMissionScreen.addActor(imageClockIcon);
        groupMissionScreen.addActor(labelBoxes);
        groupMissionScreen.addActor(labelTime);
        groupMissionScreen.addActor(labelTimeMultiplier);
        groupMissionPanel.addActor(groupMissionScreen);
        groupMissionPanel.addActor(imageMisionPanelFrame);

        addButtons();

        enter(groupMissionPanel);
    }

    private void execute_with_challenge(){
        Label labelMission, labelBoxes, labelTime, labelTimeMultiplier, labelChallengeTitle, labelChallengeDesc;
        float screenWidth, screenHeight;

        Label.LabelStyle labelSyle_base_gb_16 = new Label.LabelStyle();
        labelSyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        Label.LabelStyle labelSyle_base_gb_11 = new Label.LabelStyle();
        labelSyle_base_gb_11.font = Assets.getFont("f_base_gb_11");
        width = panelAtlas.findRegion("missionChallengePanelFrame").getRegionWidth();
        height = panelAtlas.findRegion("missionChallengePanelFrame").getRegionHeight();
        screenWidth = panelAtlas.findRegion("missionChallengePanelScreen").getRegionWidth();
        screenHeight = panelAtlas.findRegion("missionChallengePanelScreen").getRegionWidth();

        float screenCenterX = (screenWidth/2);

        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 60;
        groupMissionPanel = new Group();
        groupMissionPanel.setSize(width, height);
        groupMissionPanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 150);
        Group groupMissionScreen = new Group();
        groupMissionScreen.setSize(screenWidth, screenHeight);
        groupMissionScreen.setPosition(28, 60);

        Image imageMisionPanelFrame = new Image(panelAtlas.findRegion("missionChallengePanelFrame"));

        AnimatedImageActor animatedImageMissionPanelScreen = new AnimatedImageActor(panelAtlas, "missionChallengePanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageMissionPanelScreen.play();
        groupMissionScreen.addActor(animatedImageMissionPanelScreen);

        labelMission = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().format("sector_title", GlobalGeneralData.getInstance().getCurrentLevel()) , labelSyle_base_gb_22);
        labelMission.setPosition(screenCenterX - (labelMission.getWidth() / 2), 140 + 64);
        labelMission.setColor(c_titulo);

        Image imageBoxIcon = new Image(panelAtlas.findRegion("icon_box_light"));
        imageBoxIcon.setPosition(screenCenterX - 54 - (imageBoxIcon.getWidth() / 2), 58 + 64 + 6);
        Image imageClockIcon = new Image(panelAtlas.findRegion("icon_clock_light"));
        imageClockIcon.setPosition(screenCenterX + 54 - (imageClockIcon.getWidth() / 2), 58 + 64 + 6);

        labelBoxes = new Label(requiredBoxesString, labelSyle_base_gb_22);
        labelBoxes.setPosition(screenCenterX - 54 - (labelBoxes.getWidth() / 2) - 3, 33 + 64 + 6);
        labelBoxes.setColor(1, 1, 1, 0.85f);
        labelTime = new Label(Utils.getTimeFormatted(GlobalLevelData.getInstance().getLevelTime()), labelSyle_base_gb_22);
        labelTime.setPosition(screenCenterX + 54 - (labelTime.getWidth() / 2) - 3, 33 + 64 + 6);
        labelTime.setColor(1, 1, 1, 0.85f);

        labelTimeMultiplier = new Label("x" + GlobalLevelData.getInstance().getTimeMultiplier(), labelSyle_base_gb_16);
        labelTimeMultiplier.setPosition(screenCenterX + 58 - (labelTimeMultiplier.getWidth() / 2) - 3, 134);
        labelTimeMultiplier.setColor(c_timeMultiplier);

        labelChallengeTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_title"), labelSyle_base_gb_16);
        labelChallengeTitle.setPosition(screenCenterX - (labelChallengeTitle.getWidth() / 2) - 3, 70);
        labelChallengeTitle.setColor(c_titulo);
        labelChallengeTitle.setAlignment(Align.center);

        labelChallengeDesc = new Label(ChallengeManager.getInstance().getChallengeDescription(), labelSyle_base_gb_11);
        labelChallengeDesc.setWrap(true);
        labelChallengeDesc.setWidth(200);
        labelChallengeDesc.setPosition(screenCenterX - (labelChallengeDesc.getWidth() / 2), 43 - (labelChallengeDesc.getHeight() / 2));
        labelChallengeDesc.setAlignment(Align.center);
        labelChallengeDesc.setColor(1, 1, 1, 0.85f);
        Image titleStar_left = new Image(panelAtlas.findRegion("icon_ChallengeStar"));
        titleStar_left.setPosition(labelChallengeTitle.getX() - 24, 75);
        Image titleStar_right = new Image(panelAtlas.findRegion("icon_ChallengeStar"));
        titleStar_right.setPosition(labelChallengeTitle.getX() + labelChallengeTitle.getWidth() + 6, 75);

        groupMissionScreen.addActor(labelMission);
        groupMissionScreen.addActor(imageBoxIcon);
        groupMissionScreen.addActor(imageClockIcon);
        groupMissionScreen.addActor(labelBoxes);
        groupMissionScreen.addActor(labelTime);
        groupMissionScreen.addActor(labelTimeMultiplier);
        groupMissionScreen.addActor(labelChallengeTitle);
        groupMissionScreen.addActor(labelChallengeDesc);
        groupMissionScreen.addActor(titleStar_left);
        groupMissionScreen.addActor(titleStar_right);
        groupMissionPanel.addActor(groupMissionScreen);
        groupMissionPanel.addActor(imageMisionPanelFrame);

        addButtons();

        enter(groupMissionPanel);
    }

    private void addButtons(){
        //Boton Volver
        Drawable buttonUp = new Image(buttonsAtlas.findRegion("panelButtonList", 1)).getDrawable();
        Drawable buttonDown = new Image(buttonsAtlas.findRegion("panelButtonList", 2)).getDrawable();
        Button buttonReturn = new ImageButton(buttonUp, buttonDown);
        buttonReturn.setPosition(109, 21);
        buttonReturn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel) leave(groupMissionPanel);
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                buttonActivated = BUTTON_ACTION.SELECTION_SCREEN;
            }
        });
        groupMissionPanel.addActor(buttonReturn);

        //Boton play
        buttonUp = new Image(buttonsAtlas.findRegion("panelButtonPlay", 1)).getDrawable();
        buttonDown = new Image(buttonsAtlas.findRegion("panelButtonPlay", 2)).getDrawable();
        Button buttonPlay = new ImageButton(buttonUp, buttonDown);
        buttonPlay.setPosition(183, 21);
        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel) leave(groupMissionPanel);
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                buttonActivated = BUTTON_ACTION.PLAY;
            }
        });
        groupMissionPanel.addActor(buttonPlay);
        layer.addActor(groupMissionPanel);
    }
}