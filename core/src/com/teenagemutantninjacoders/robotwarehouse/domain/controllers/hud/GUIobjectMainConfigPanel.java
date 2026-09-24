package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 04/02/2019.
 */
public class GUIobjectMainConfigPanel {
    private final MainConfigListener listener;
    private Group layer;
    private Group groupSidePanel;
    private STATUS status = STATUS.RETRACTED;
    private TextureAtlas panelAtlas, generalButtonsAtlas;
    private float posXIn, posXOut;
    private float[] mediumButtonPos;
    private ImageButton testCrashButton;
    private CheckBox cloudSaveCheckButton;
    private ImageButton deleteGameButton;

    public GUIobjectMainConfigPanel(Group layer, MainConfigListener listener){
        this.layer = layer;
        this.listener = listener;
        panelAtlas = Assets.getTextureAtlas("pausePanel_side");
        generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");

        posXIn = 515;
        posXOut = GameConstants.HORIZONTAL_RESOLUTION + 50;
        mediumButtonPos = new float[2];

        createSidePanel();
    }

    private void createSidePanel() {
        groupSidePanel = new Group();
        Group groupSidePanelScreen = new Group();
        groupSidePanel.setPosition(posXOut, -2);
        groupSidePanelScreen.setPosition(43, -2);

        Image imageSidePanelFrame = new Image(panelAtlas.findRegion("frame_left"));
        imageSidePanelFrame.setTouchable(Touchable.disabled);

        AnimatedImageActor animatedImageSidePanelScreen = new AnimatedImageActor(panelAtlas, "screen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageSidePanelScreen.play();

        int screenWidth = (int) animatedImageSidePanelScreen.getWidth();
        int mediumButtonsWidth = generalButtonsAtlas.findRegion("roundDigitalButtons_sound_state1", 1).getRegionWidth();

        mediumButtonPos[0] = (screenWidth / 4) - (mediumButtonsWidth / 2) + 6;
        mediumButtonPos[1] = screenWidth - (screenWidth / 4) - (mediumButtonsWidth / 2) - 6;

        // Exit
        Drawable buttonUp = new Image(panelAtlas.findRegion("buttonPlay", 1)).getDrawable();
        Drawable buttonDown = new Image(panelAtlas.findRegion("buttonPlay", 2)).getDrawable();

        Button resumeButton = new ImageButton(buttonUp, buttonDown);
        resumeButton.setPosition(16, 173);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (status == STATUS.DEPLOYED) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    retractPanel();
                }
            }
        });

        // Sound
        Drawable on =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_sound_state1", 1)).getDrawable();
        Drawable off =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_sound_state0", 1)).getDrawable();
        CheckBox.CheckBoxStyle soundCheckBoxStyle = new CheckBox.CheckBoxStyle(off, on, Assets.getFont("f_base_gb_16"), Color.BLACK);
        final CheckBox checkButtonSound = new CheckBox("",soundCheckBoxStyle);
        checkButtonSound.setChecked(GlobalPreferencesData.getInstance().isGameSoundEnabled());
        checkButtonSound.setPosition(mediumButtonPos[0], 310);
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
        musicButton.setPosition(mediumButtonPos[0], 240);
        musicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (status == STATUS.DEPLOYED) {
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

        //Debug Crash
        on =  new Image(generalButtonsAtlas.findRegion("panelButtonCancel", 1)).getDrawable();
        off =  new Image(generalButtonsAtlas.findRegion("panelButtonCancel", 2)).getDrawable();
        testCrashButton = new ImageButton(on, off);
        testCrashButton.setPosition(mediumButtonPos[0] + 21, 176);
        testCrashButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (status == STATUS.DEPLOYED) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    listener.crashTestPressed();
                    retractPanel();
                }
            }
        });

        // Cloud Saving
        on =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_cloud_state1", 1)).getDrawable();
        off =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_cloud_state0", 1)).getDrawable();
        CheckBox.CheckBoxStyle cloudSaveCheckBoxStyle = new CheckBox.CheckBoxStyle(off, on, Assets.getFont("f_base_gb_16"), Color.BLACK);
        cloudSaveCheckButton = new CheckBox("",cloudSaveCheckBoxStyle);
        cloudSaveCheckButton.setChecked(GlobalPreferencesData.getInstance().isCloudSaveEnabled());
        cloudSaveCheckButton.setPosition(mediumButtonPos[0], 80);
        cloudSaveCheckButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSoundForced(AudioManager.SOUND.BUTTON_CHANGE);
                GlobalPreferencesData.getInstance().setCloudSaveEnabled(cloudSaveCheckButton.isChecked());
                retractPanel();
                listener.savedGameOptionChanged(cloudSaveCheckButton.isChecked());
            }
        });


        //Delete Saved Game Button
        on =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_eraseCloud", 1)).getDrawable();
        off =  new Image(generalButtonsAtlas.findRegion("roundDigitalButtons_eraseCloud", 2)).getDrawable();
        deleteGameButton = new ImageButton(on, off);
        deleteGameButton.setPosition(mediumButtonPos[0], 10);
        deleteGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (status == STATUS.DEPLOYED) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    listener.deleteGamePressed();
                    retractPanel();
                }
            }
        });

        groupSidePanelScreen.addActor(animatedImageSidePanelScreen);
        groupSidePanelScreen.addActor(checkButtonSound);
        groupSidePanelScreen.addActor(musicButton);
        //groupSidePanelScreen.addActor(testCrashButton); //FIXME: Deleteeventually
        groupSidePanelScreen.addActor(cloudSaveCheckButton);
        groupSidePanelScreen.addActor(deleteGameButton);

        groupSidePanel.addActor(groupSidePanelScreen);
        groupSidePanel.addActor(imageSidePanelFrame);
        groupSidePanel.addActor(resumeButton);
        layer.addActor(groupSidePanel);
    }

    public void actualizeButtons(){
        cloudSaveCheckButton.setChecked(GlobalPreferencesData.getInstance().isCloudSaveEnabled());
    }

    private void deployPanel(){
        status = STATUS.MOVING;
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_ENTER);
        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(posXIn, groupSidePanel.getY());
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineOut);
        RunnableAction ready = new RunnableAction();
        ready.setRunnable(new Runnable() {
            @Override
            public void run() {
                status = STATUS.DEPLOYED;
            }
        });

        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(ready);

        if(listener.isSessionActive() && listener.isSavedGameSupported()) {
            boolean saveEnabled = GlobalPreferencesData.getInstance().isCloudSaveEnabled();
            if(listener.savedGameExists()) {
                deleteGameButton.setVisible(true);
            } else {
                deleteGameButton.setVisible(false);
            }
            cloudSaveCheckButton.setVisible(true);
            cloudSaveCheckButton.setChecked(saveEnabled);
        } else {
            cloudSaveCheckButton.setVisible(false);
            deleteGameButton.setVisible(false);
        }
        if(listener.isDebugBuild()) {
            testCrashButton.setVisible(true);
        } else {
            testCrashButton.setVisible(true); //TODO: change for false
        }

        groupSidePanel.addAction(sequenceAction);
    }

    private void retractPanel(){
        status = STATUS.MOVING;
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_LEAVE);
        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(posXOut, groupSidePanel.getY());
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineIn);

        RunnableAction finalize = new RunnableAction();
        finalize.setRunnable(new Runnable() {
            @Override
            public void run() {
                status = STATUS.RETRACTED;
            }
        });

        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(finalize);

        groupSidePanel.addAction(sequenceAction);
    }

    public void activePanel(){
        if(status != STATUS.MOVING) {
          if(status == STATUS.RETRACTED) deployPanel();
          else retractPanel();
        }
    }

    public void forceRetractPanel(){
        if(status != STATUS.RETRACTED) retractPanel();
    }

    private enum STATUS{
        DEPLOYED, RETRACTED, MOVING
    }
}
