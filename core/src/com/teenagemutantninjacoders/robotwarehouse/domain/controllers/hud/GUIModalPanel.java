package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiM on 10/10/2019.
 */

public class GUIModalPanel extends GUIobjectBasePanel {
    private final Group layer;
    private final ModalListener listener;
    private Label.LabelStyle labelStyle_base_gb_16, labelStyle_base_gb_11;
    private Group groupModalFramePanel, groupModalScreenPanel, blockingBackgroundGroup;
    private int screenWidth,screenHeight;
    private Color c_titulo;
    private TextureAtlas panelAtlas;
    private TextureAtlas buttonsAtlas;
    private String title;
    private String body;

    public GUIModalPanel(Group layer, ModalListener listener, String title, String body){
        this.layer = layer;
        this.listener = listener;
        this.title = title;
        this.body = body;
        panelAtlas = Assets.getTextureAtlas("missionPanel");
        buttonsAtlas = Assets.getTextureAtlas("general_buttons");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_11 = new Label.LabelStyle();
        labelStyle_base_gb_11.font = Assets.getFont("f_base_gb_11");
        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        execute();
    }

    private void execute() {

        Image imageModalFramePanel = new Image(panelAtlas.findRegion("missionSimplePanelFrame"));
        imageModalFramePanel.setPosition(0, 0);

        AnimatedImageActor animatedImageModalScreen= new AnimatedImageActor(panelAtlas, "missionSimplePanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageModalScreen.play();

        width = (int) imageModalFramePanel.getWidth();
        height = (int) imageModalFramePanel.getHeight();
        screenWidth = (int) animatedImageModalScreen.getWidth();
        screenHeight = (int) animatedImageModalScreen.getHeight();
        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 25;//50
        groupModalFramePanel = new Group();
        groupModalFramePanel.setSize(width, height);
        groupModalFramePanel.setPosition((GameConstants.HORIZONTAL_RESOLUTION / 2) - (width / 2), GameConstants.VERTICAL_RESOLUTION + 100);

        groupModalScreenPanel = new Group();
        groupModalScreenPanel.setSize(screenWidth, screenHeight);
        groupModalScreenPanel.setPosition(28, 60);

        Label labelModalTitle = new Label(title, labelStyle_base_gb_16);
        labelModalTitle.setColor(c_titulo);
        labelModalTitle.setAlignment(Align.center);
        labelModalTitle.setWrap(true);
        labelModalTitle.setWidth(200);
        labelModalTitle.setPosition((screenWidth / 2) - (labelModalTitle.getWidth() / 2), 139);

        Label labelModalBody = new Label(body, labelStyle_base_gb_11);
        labelModalBody.setColor(new Color(1, 1, 1, 0.85f));
        labelModalBody.setWrap(true);
        labelModalBody.setWidth(240);

        labelModalBody.setAlignment(Align.center);
        labelModalBody.setPosition((groupModalScreenPanel.getWidth() - labelModalBody.getWidth()) / 2, 71 - (labelModalBody.getHeight() / 2));

        groupModalFramePanel.addActor(groupModalScreenPanel);
        groupModalFramePanel.addActor(imageModalFramePanel);
        groupModalScreenPanel.addActor(animatedImageModalScreen);
        groupModalScreenPanel.addActor(labelModalTitle);
        groupModalScreenPanel.addActor(labelModalBody);

        addBlockingBackground();
        layer.addActor(groupModalFramePanel);
        addButtons();
        enter(groupModalFramePanel);
    }

    private void addButtons(){
        //Boton No
        Drawable buttonUp = new Image(buttonsAtlas.findRegion("panelButtonCancel", 1)).getDrawable();
        Drawable buttonDown = new Image(buttonsAtlas.findRegion("panelButtonCancel", 2)).getDrawable();
        Button buttonNo = new ImageButton(buttonUp, buttonDown);
        buttonNo.setPosition(109, 21);
        buttonNo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    listener.noSelected();
                    leave(groupModalFramePanel);
                    removeBlockingBackground();
                }
            }
        });
        groupModalFramePanel.addActor(buttonNo);

        //Boton Yes
        buttonUp = new Image(buttonsAtlas.findRegion("panelButtonAccept", 1)).getDrawable();
        buttonDown = new Image(buttonsAtlas.findRegion("panelButtonAccept", 2)).getDrawable();
        Button buttonYes = new ImageButton(buttonUp, buttonDown);
        buttonYes.setPosition(183, 21);
        buttonYes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    listener.yesSelected();
                    leave(groupModalFramePanel);
                    removeBlockingBackground();
                }
            }
        });
        groupModalFramePanel.addActor(buttonYes);
    }
    private void addBlockingBackground(){
        blockingBackgroundGroup = new Group();
        blockingBackgroundGroup.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        blockingBackgroundGroup.setColor(1, 1, 1, 0);
        Image blackImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        blackImage.setColor(Color.BLACK);
        blackImage.setSize(blockingBackgroundGroup.getWidth(), blockingBackgroundGroup.getHeight());
        blockingBackgroundGroup.addActor(blackImage);

        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.4f);
        alphaAction.setDuration(0.5f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.2f));
        sequenceAction.addAction(alphaAction);
        blockingBackgroundGroup.addAction(sequenceAction);

        layer.addActor(blockingBackgroundGroup);
    }

    private void removeBlockingBackground(){
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.5f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        blockingBackgroundGroup.addAction(sequenceAction);
    }

    public interface ModalListener {
        void yesSelected();
        void noSelected();
    }
}
