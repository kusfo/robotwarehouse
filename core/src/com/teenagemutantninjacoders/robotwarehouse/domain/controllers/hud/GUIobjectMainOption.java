package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.badlogic.gdx.graphics.Color;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 30/01/2019.
 */
public class GUIobjectMainOption {
    private float x, y;
    private OPTION option;
    private TextureAtlas mainAtlas;
    private Label.LabelStyle labelStyle_option;
    private Group layer;
    private Group groupOption;
    private Color optionColor_normal, optionColor_push;
    private float finalPosX;
    private float delay;
    private String text;
    private int labelOptionDespX = 0;
    private boolean selected = false;
    private boolean interactive = true;
    private boolean inhabilitated = false;
    private boolean over = false;
    private boolean deployed = false;

    public GUIobjectMainOption(Group layer, float x, float y, OPTION option){
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.option = option;
        finalPosX = x;
        mainAtlas = Assets.getTextureAtlas("main_title");

        labelStyle_option = new Label.LabelStyle();
        labelStyle_option.font = Assets.getFont("f_base_gb_22");

        configOption();
        execute();
    }

    private void configOption(){
        Color greenOptionColor_normal = new Color(167 / 255f, 232 / 255f, 82 / 255f, 0.90f);
        Color greenOptionColor_push = new Color(227 / 255f, 255 / 255f, 190 / 255f, 0.9f);
        Color blueOptionColor_normal = new Color(89 / 255f, 213 / 255f, 194 / 255f, 0.9f);
        Color blueOptionColor_push = new Color(204 / 255f, 255 / 255f, 247 / 255f, 0.9f);

        switch(option){
            case PLAY:
                optionColor_normal = greenOptionColor_normal;
                optionColor_push = greenOptionColor_push;
                text = GlobalGeneralData.getInstance().getGlobalBundleData().get("play_button");
                break;
            case TUTORIAL:
                optionColor_normal = blueOptionColor_normal;
                optionColor_push = blueOptionColor_push;
                text = GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorial_button");
                labelOptionDespX = 20;
                break;
        }
        if(inhabilitated){
            optionColor_normal = Color.GRAY;
            optionColor_push = Color.GRAY;
        }
        delay = Math.abs(x) / 200;
    }

    private void execute(){
        Image imageOptionFrame = new Image(mainAtlas.findRegion("option_frame"));
        AnimatedImageActor animatedImageOptionScreen = new AnimatedImageActor(mainAtlas, "option_screen", 0.1f,  Animation.PlayMode.LOOP);
        animatedImageOptionScreen.play();

        float width = imageOptionFrame.getWidth();
        float height = imageOptionFrame.getHeight();
        float screenWidth = animatedImageOptionScreen.getWidth();
        float screenHeight = animatedImageOptionScreen.getHeight();

        groupOption = new Group();
        groupOption.setSize(width, height);
        groupOption.setPosition(x - (groupOption.getWidth() + 20), y);
        groupOption.setVisible(false);

        Group screenGroup = new Group();
        screenGroup.setSize(screenWidth, screenHeight);
        screenGroup.setPosition(0, 23);

        final Label labelOption = new Label(text, labelStyle_option);
        labelOption.setColor(optionColor_normal);
        labelOption.setPosition((screenWidth / 2) - (labelOption.getWidth() / 2) + labelOptionDespX, (screenHeight / 2) - (labelOption.getHeight() / 2));
        screenGroup.addActor(animatedImageOptionScreen);
        screenGroup.addActor(labelOption);

        // Creamos un boton propio para poder usar el label como indicativo para los eventos de pulsacion
        Group virtualButton = new Group();
        virtualButton.setPosition(0, screenHeight / 2);
        virtualButton.setSize(screenWidth, screenHeight);
        virtualButton.addListener(new InputListener(){
            public void enter(InputEvent event, float x, float y, int pointer, Actor button) {
                over = true;
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor button) {
                over = false;
                labelOption.setColor(optionColor_normal);
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!selected && !inhabilitated && interactive) labelOption.setColor(optionColor_push);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!selected && interactive) {
                    if (over && !inhabilitated){
                        selectOption();
                    }
                }
                labelOption.setColor(optionColor_normal);
            }
        });

        groupOption.addActor(screenGroup);
        groupOption.addActor(imageOptionFrame);
        groupOption.addActor(virtualButton);
        layer.addActor(groupOption);
    }

    private void selectOption(){
        // Por seguridad, las opciones de juego solo funcionarán si la versión de la partida esta al dia
        if(GlobalPreferencesData.getInstance().getSavedGameVersion() == GameConstants.SAVED_GAME_VERSION)
            selected = true;
        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
    }

    public void enter(){
        groupOption.setVisible(true);
        interactive = true;
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(finalPosX, y);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation( new Interpolation.SwingOut(0.7f));
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                deployed = true;
            }
        });

        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.4f + delay));
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(runnableAction);
        groupOption.addAction(sequenceAction);
    }

    public void leave(){
        interactive = false;
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(-(groupOption.getWidth() + 20), y);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.pow2In);
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                deployed = false;
            }
        });
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.08f));
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(runnableAction);
        groupOption.addAction(sequenceAction);
    }

    public boolean isSelected(){
        return selected;
    }

    public void unSelect() {
        selected = false;
    }

    public boolean isDeployed() {
        return deployed;
    }

    enum OPTION{
        PLAY, TUTORIAL, LOGIN
    }
}