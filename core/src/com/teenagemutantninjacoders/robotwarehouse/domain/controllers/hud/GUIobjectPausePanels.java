package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 22/02/2018.
 */
public class GUIobjectPausePanels extends GUIobjectBaseLevelPanel  {
    private Group layer;
    private Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_11, labelStyle_base_gb_16;
    private Color c_textYellow, c_white;
    private Group groupSidePanel, groupPausePanel;
    private boolean pausePanelReady = false;
    private boolean sidePanelReady = false;
    private boolean hasChallenge = false;
    private Integer screenWidth, screenHeight;
    BUTTON_ACTION sideButtonActivated = BUTTON_ACTION.NONE;
    private TextureAtlas generalButtonsAtlas, pausePanelInfoAtlas, pausePanelSideAtlas;
    public GUIobjectPausePanels(Group layer, GameEventsManager eventsManager){
        this.layer = layer;
        this.eventsManager = eventsManager;
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_11 = new Label.LabelStyle();
        labelStyle_base_gb_11.font = Assets.getFont("f_base_gb_11");
        c_textYellow = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_white = new Color(1, 1, 1, 0.85f);
        generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");
        pausePanelInfoAtlas = Assets.getTextureAtlas("pausePanel_info");
        pausePanelSideAtlas = Assets.getTextureAtlas("pausePanel_side");
        hasChallenge = GlobalLevelData.getInstance().getActualChallenge() != GlobalAttributes.CHALLENGE.NONE;
        execute();
    }

    private void execute() {
        createPausePanel();
        createSidePanel();
    }

    private void createPausePanel(){
        Group screenGroup, baseContentGroup;
        Image imageMisionPanelFrame;
        AnimatedImageActor animatedImagePanelScreen;

        if(!hasChallenge) {

            imageMisionPanelFrame = new Image(pausePanelInfoAtlas.findRegion("frameSimple"));
            animatedImagePanelScreen = new AnimatedImageActor(pausePanelInfoAtlas, "screenSimple", 0.1f, Animation.PlayMode.LOOP);
            animatedImagePanelScreen.play();

            width = (int) imageMisionPanelFrame.getWidth();
            height = (int) imageMisionPanelFrame.getHeight();
            screenWidth = (int) animatedImagePanelScreen.getWidth();
            screenHeight = (int) animatedImagePanelScreen.getHeight();
            finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 20;// - 30;
            groupPausePanel = new Group();
            groupPausePanel.setSize(width, height);
            groupPausePanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 100);

            screenGroup = new Group();
            screenGroup.setSize(screenWidth, screenHeight);
            screenGroup.setPosition(28, 23);
            screenGroup.addActor(animatedImagePanelScreen);

            baseContentGroup = getBaseContent();
            baseContentGroup.setPosition(0, -71);



        } else {
            imageMisionPanelFrame = new Image(pausePanelInfoAtlas.findRegion("frameChallenge"));

            animatedImagePanelScreen = new AnimatedImageActor(pausePanelInfoAtlas, "screenChallenge", 0.1f, Animation.PlayMode.LOOP);
            animatedImagePanelScreen.play();
            width = (int) imageMisionPanelFrame.getWidth();
            height = (int) imageMisionPanelFrame.getHeight();
            screenWidth = (int) animatedImagePanelScreen.getWidth();
            screenHeight = (int) animatedImagePanelScreen.getHeight();
            finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 52;
            groupPausePanel = new Group();
            groupPausePanel.setSize(width, height);
            groupPausePanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 100);

            screenGroup = new Group();
            screenGroup.setSize(screenWidth, screenHeight);
            screenGroup.setPosition(28, 23);

            baseContentGroup = getBaseContent();



            // Desafio
            Label labelChallengeTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_title"), labelStyle_base_gb_16);
            labelChallengeTitle.setColor(c_textYellow);
            labelChallengeTitle.setAlignment(Align.center);
            labelChallengeTitle.setPosition((screenWidth/2) - (labelChallengeTitle.getWidth() / 2), 65);

            Image titleStar_left = new Image(pausePanelInfoAtlas.findRegion("icon_ChallengeStar"));
            titleStar_left.setPosition(labelChallengeTitle.getX() - 24, 70);
            Image titleStar_right = new Image(pausePanelInfoAtlas.findRegion("icon_ChallengeStar"));
            titleStar_right.setPosition(labelChallengeTitle.getX() + labelChallengeTitle.getWidth() + 6, 70);

            Label labelChallengeDesc = new Label(ChallengeManager.getInstance().getChallengeDescription(), labelStyle_base_gb_11);
            labelChallengeDesc.setWrap(true);
            labelChallengeDesc.setWidth(220);
            labelChallengeDesc.setPosition((screenWidth/2) - (labelChallengeDesc.getWidth() / 2), 37 - (labelChallengeDesc.getHeight() / 2));
            labelChallengeDesc.setAlignment(Align.center);
            labelChallengeDesc.setColor(1, 1, 1, 0.85f);

            screenGroup.addActor(animatedImagePanelScreen);
            screenGroup.addActor(labelChallengeTitle);
            screenGroup.addActor(titleStar_left);
            screenGroup.addActor(titleStar_right);
            screenGroup.addActor(labelChallengeDesc);
        }


        screenGroup.addActor(baseContentGroup);
        groupPausePanel.addActor(screenGroup);
        groupPausePanel.addActor(imageMisionPanelFrame);

        layer.addActor(groupPausePanel);
        enter(groupPausePanel, 0.4f);
    }

    private Group getBaseContent(){
        Group baseContentGroup = new Group();

        Label labelTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("pause_title"), labelStyle_base_gb_22);
        labelTitle.setColor(c_textYellow);
        labelTitle.setAlignment(Align.center);
        labelTitle.setPosition((screenWidth/2) - (labelTitle.getWidth() / 2), 225);

        Label labelActualLevel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().format("sector_title",
                GlobalGeneralData.getInstance().getCurrentLevel()), labelStyle_base_gb_22);
        labelActualLevel.setAlignment(Align.center);
        labelActualLevel.setPosition((screenWidth/2) - (labelActualLevel.getWidth() / 2), 162 + 11);
        labelActualLevel.setColor(c_white);
        Label labelBestScoreTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("pause_best_score"), labelStyle_base_gb_16);
        labelBestScoreTitle.setColor(c_textYellow);
        labelBestScoreTitle.setAlignment(Align.center);
        labelBestScoreTitle.setPosition((screenWidth/2) - (labelBestScoreTitle.getWidth() / 2), 125 + 18);

        Integer bestScore = GlobalPreferencesData.getInstance().getLevelScore(GlobalGeneralData.getInstance().getCurrentLevel(),
                GlobalGeneralData.getInstance().getCurrentEpisode());
        Label labelBestScore = new Label(bestScore.toString(), labelStyle_base_gb_16);
        labelBestScore.setAlignment(Align.center);
        labelBestScore.setPosition((screenWidth/2) - (labelBestScore.getWidth() / 2) + 3, 99 + 18);
        labelBestScore.setColor(c_white);

        int levelRank = GlobalPreferencesData.getInstance().getLevelStars(GlobalGeneralData.getInstance().getCurrentLevel(), GlobalGeneralData.getInstance().getCurrentEpisode());
        Image imageStars = new Image(pausePanelInfoAtlas.findRegion("rankStars", levelRank));
        imageStars.setPosition(((screenWidth/2) - (imageStars.getWidth() / 2)) + 2, 84 + 17);

        baseContentGroup.addActor(labelTitle);
        baseContentGroup.addActor(labelActualLevel);
        baseContentGroup.addActor(labelBestScoreTitle);
        baseContentGroup.addActor(labelBestScore);
        baseContentGroup.addActor(imageStars);

        return baseContentGroup;
    }

    private void createSidePanel(){
        groupSidePanel = new Group();
        Group groupSidePanelScreen = new Group();
        groupSidePanel.setPosition(-190, -2);
        groupSidePanelScreen.setPosition(-120, -2);

        Image imageSidePanelFrame = new Image(pausePanelSideAtlas.findRegion("frame"));
        imageSidePanelFrame.setTouchable(Touchable.disabled);

        AnimatedImageActor animatedImageSidePanelScreen = new AnimatedImageActor(pausePanelSideAtlas,"screen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageSidePanelScreen.play();

        int screenWidth = (int) animatedImageSidePanelScreen.getWidth();

        // Play button
        Drawable buttonUp = new Image(pausePanelSideAtlas.findRegion("buttonPlay", 1)).getDrawable();
        Drawable buttonDown = new Image(pausePanelSideAtlas.findRegion("buttonPlay", 2)).getDrawable();

        Button resumeButton = new ImageButton(buttonUp, buttonDown);
        resumeButton.setPosition( 9, 173);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sidePanelReady && pausePanelReady){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    sideButtonActivated = BUTTON_ACTION.PLAY;
                    sidePanelReady = false;
                    pausePanelReady = false;
                    LeaveSidePanel();
                    leave(groupPausePanel);
                }
            }
        });

        int bigButtonsWidth = generalButtonsAtlas.findRegion("roundDigitalButtons_restart", 1).getRegionWidth();
        int mediumButtonsWidth = generalButtonsAtlas.findRegion("roundDigitalButtons_music_state0", 1).getRegionWidth();

        // Digital buttons
        buttonUp = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_restart", 1)).getDrawable();
        buttonDown = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_restart", 2)).getDrawable();
        Button buttonRestart = new ImageButton(buttonUp, buttonDown);
        buttonRestart.setPosition((screenWidth / 2) - (bigButtonsWidth / 2), 300);
        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sidePanelReady && pausePanelReady){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    sideButtonActivated = BUTTON_ACTION.RESTART_LEVEL;
                    sidePanelReady = false;
                    pausePanelReady = false;
                    LeaveSidePanel();
                    leave(groupPausePanel);
                }
            }
        });

        buttonUp = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_selection", 1)).getDrawable();
        buttonDown = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_selection", 2)).getDrawable();
        Button buttonSelection = new ImageButton(buttonUp, buttonDown);
        buttonSelection.setPosition((screenWidth / 2) - (bigButtonsWidth / 2), 220);
        buttonSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sidePanelReady && pausePanelReady){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    sideButtonActivated = BUTTON_ACTION.SELECTION_SCREEN;
                    sidePanelReady = false;
                    pausePanelReady = false;
                    LeaveSidePanel();
                    leave(groupPausePanel);
                }
            }
        });

        /// Sound
        Drawable on =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_sound_state1", 1)).getDrawable();
        Drawable off =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_sound_state0", 1)).getDrawable();
        CheckBox.CheckBoxStyle soundCheckBoxStyle = new CheckBox.CheckBoxStyle(off, on, Assets.getFont("f_base_gb_16"), Color.BLACK);
        final CheckBox checkButtonSound = new CheckBox("",soundCheckBoxStyle);
        checkButtonSound.setChecked(GlobalPreferencesData.getInstance().isGameSoundEnabled());
        checkButtonSound.setPosition((screenWidth / 4) - (mediumButtonsWidth / 2) + 6, 90);
        checkButtonSound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().activateSounds(checkButtonSound.isChecked());
                AudioManager.getInstance().playSoundForced(AudioManager.SOUND.BUTTON_CHANGE);
            }
        });

        // Music
        final Drawable[] state_buttonUp = new Drawable[4];
        final Drawable[] state_buttonDown = new Drawable[4];
        state_buttonUp[0] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state0", 1)).getDrawable();
        state_buttonDown[0] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state0", 2)).getDrawable();
        state_buttonUp[1] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state1", 1)).getDrawable();
        state_buttonDown[1] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state1", 2)).getDrawable();
        state_buttonUp[2] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state2", 1)).getDrawable();
        state_buttonDown[2] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state2", 2)).getDrawable();
        state_buttonUp[3] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state3", 1)).getDrawable();
        state_buttonDown[3] = new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_music_state3", 2)).getDrawable();

        int musicVolumeState = GlobalPreferencesData.getInstance().getMusicVolumeState();
        final ImageButton musicButton = new ImageButton(state_buttonUp[musicVolumeState], state_buttonDown[musicVolumeState]);
        musicButton.setPosition(screenWidth - (screenWidth / 4) - (mediumButtonsWidth / 2) - 6, 90);
        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(sidePanelReady && pausePanelReady){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_CHANGE);
                    int musicVolumeState = GlobalPreferencesData.getInstance().getMusicVolumeState();
                    if (musicVolumeState == 3) musicVolumeState = 0;
                    else musicVolumeState ++;
                    GlobalPreferencesData.getInstance().setMusicVolumeState(musicVolumeState);
                    AudioManager.getInstance().setMusicMaxVolumeFromVolumeState(musicVolumeState);
                    musicButton.setStyle(new ImageButton.ImageButtonStyle(state_buttonUp[musicVolumeState], state_buttonDown[musicVolumeState], state_buttonUp[musicVolumeState],
                            state_buttonUp[musicVolumeState], state_buttonDown[musicVolumeState], state_buttonUp[musicVolumeState]));
                }
            }
        });

        groupSidePanelScreen.addActor(animatedImageSidePanelScreen);
        groupSidePanelScreen.addActor(buttonRestart);
        groupSidePanelScreen.addActor(buttonSelection);
        groupSidePanelScreen.addActor(checkButtonSound);
        groupSidePanelScreen.addActor(musicButton);
        groupSidePanel.addActor(groupSidePanelScreen);
        groupSidePanel.addActor(imageSidePanelFrame);
        groupSidePanel.addActor(resumeButton);

        layer.addActor(groupSidePanel);
        EnterSidePanel(groupSidePanel);
    }

    private void EnterSidePanel(Actor panelGroup){
        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(120, panelGroup.getY());
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineOut);
        RunnableAction entering =  new RunnableAction();
        entering.setRunnable(new Runnable() {
            @Override
            public void run() {
                eventsManager.addGameZoneFade(true, 1.0f);
            }
        });
        RunnableAction ready = new RunnableAction();
        ready.setRunnable(new Runnable() {
            @Override
            public void run() {
                sidePanelReady = true;
            }
        });

        sequenceAction.addAction(new DelayAction(0.65f));
        sequenceAction.addAction(entering);
        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(ready);

        panelGroup.addAction(sequenceAction);
    }

    private void LeaveSidePanel(){

        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(-190, groupSidePanel.getY());
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineIn);

        RunnableAction activeFadeIn = new RunnableAction();
        activeFadeIn.setRunnable(new Runnable() {
            @Override
            public void run() {
                if(sideButtonActivated == BUTTON_ACTION.PLAY) eventsManager.addGameZoneFade(false, 0.6f);
            }
        });

        RunnableAction finalizePause = new RunnableAction();
        finalizePause.setRunnable(new Runnable() {
            @Override
            public void run() {
                activateButton();
            }
        });

        sequenceAction.addAction(activeFadeIn);
        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(finalizePause);
        sequenceAction.addAction(new RemoveActorAction());

        groupSidePanel.addAction(sequenceAction);
    }

    public boolean isPauseMenuReady(){
        return sidePanelReady && pausePanelReady;
    }

    private void activateButton(){
        switch (sideButtonActivated){
            case PLAY:
                eventsManager.addLeavePauseMenu();
                break;
            case RESTART_LEVEL:
                eventsManager.addRestartLevelEvent();
                break;
            case SELECTION_SCREEN:
                eventsManager.changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN);
                break;
        }
    }

    @Override
    protected void arrived(){
        pausePanelReady = true;
    }
}
