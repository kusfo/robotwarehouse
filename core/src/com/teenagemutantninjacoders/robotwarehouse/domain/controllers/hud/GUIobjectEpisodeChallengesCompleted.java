package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 05/12/2018.
 */
public class GUIobjectEpisodeChallengesCompleted {
    private Group baseGroup;
    private Label.LabelStyle labelStyle_title_24;
    private Group layer;
    private TextureAtlas generalAtlas;
    private float announcementDelay = 1.2f;
    private float announcementTime = 10f;
    private Color titleColor;
    private boolean finalized = false;
    private boolean touchReady = false;

    private Image darknessImage, textFrameImage, starsImage;
    private Label frameLabel;
    private Group frameLabelGroup;

    public GUIobjectEpisodeChallengesCompleted(Group layer){
        this.layer = layer;

        labelStyle_title_24 = new Label.LabelStyle();
        labelStyle_title_24.font = Assets.getFont("f_cartel_partida_24");

        titleColor = new Color(255 / 255f, 187 / 255f, 99 / 255f, 1);
        generalAtlas = Assets.getTextureAtlas("levelSelection_general");
        execute();
    }

    private void execute(){
        int centerX = GameConstants.HORIZONTAL_RESOLUTION / 2;

        darknessImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        textFrameImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("floatingTextFrame", 2));
        frameLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenges_completed"), labelStyle_title_24);
        frameLabelGroup = new Group();
        starsImage = new Image(generalAtlas.findRegion("challengesCompletedStars"));

        baseGroup = new Group();
        baseGroup.setPosition(centerX, 150);
        baseGroup.setTouchable(Touchable.disabled);

        starsImage.setPosition( -starsImage.getWidth() / 2, 52);
        starsImage.setOrigin(Align.bottom);
        starsImage.setScale(0.4f, 0);
        starsImage.setColor(1, 1, 1, 0);

        textFrameImage.setPosition(-textFrameImage.getWidth() / 2, 10);
        textFrameImage.setColor(1, 1, 1, 0);
        textFrameImage.setScale(1, 0);
        textFrameImage.setOrigin(Align.center);

        frameLabel.setColor(titleColor);
        frameLabel.setPosition(-frameLabel.getWidth() / 2, -frameLabel.getHeight() / 2);
        frameLabelGroup.setPosition(textFrameImage.getX() + (textFrameImage.getWidth() / 2), textFrameImage.getY() + 27);
        frameLabelGroup.setScale(3, 0.2f);
        frameLabelGroup.setColor(1, 1, 1, 0);
        frameLabelGroup.addActor(frameLabel);

        darknessImage.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        darknessImage.setColor(0, 0, 0, 0);
        darknessImage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (touchReady) {
                    baseGroup.clearActions();
                    AudioManager.getInstance().changeMusicVolume(true, 1.0f);
                    finalizeAnnouncement();
                }
                return true;
            }
        });

        layer.addActor(darknessImage);
        baseGroup.addActor(textFrameImage);
        baseGroup.addActor(frameLabelGroup);
        baseGroup.addActor(starsImage);

        layer.addActor(baseGroup);

        beginAnnouncement();
    }

    private void beginAnnouncement(){
        SequenceAction sequenceAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;

        // Base
        sequenceAction = new SequenceAction();
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Activamos el sonido de completo y bajamos el sonido de la música momentaneamente
                AudioManager.getInstance().playSound(AudioManager.SOUND.CHALLENGE_COMPLETED);
                AudioManager.getInstance().changeMusicVolume(true, 0.7f);
            }
        });
        sequenceAction.addAction(new DelayAction(1.0f));
        sequenceAction.addAction(runnableAction);
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Momento en el que se puede clicar para pasarlo
                touchReady = true;
            }
        });
        sequenceAction.addAction(new DelayAction(1.0f));
        sequenceAction.addAction(runnableAction);

        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Tiempo de finalizacion del cartel si no ha clicado nadie
                finalizeAnnouncement();
            }
        });
        sequenceAction.addAction(new DelayAction(announcementTime));
        sequenceAction.addAction(runnableAction);

        baseGroup.addAction(sequenceAction);


        // Oscuridad
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.4f);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(announcementDelay - 0.5f));
        sequenceAction.addAction(alphaAction);
        darknessImage.addAction(sequenceAction);


        // Estrellas
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.1f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setInterpolation(Interpolation.bounceOut);
        scaleToAction.setDuration(0.5f);
        sequenceAction.addAction(new DelayAction(announcementDelay));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(scaleToAction);
        starsImage.addAction(sequenceAction);


        // Recuadro texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        //scaleToAction.setScale(1, 1);
        scaleToAction.setScale(1, 1.2f);
        scaleToAction.setDuration(0.2f);

        sequenceAction.addAction(new DelayAction(announcementDelay));
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        textFrameImage.addAction(sequenceAction);

        // Texto Titulo
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.15f);

        sequenceAction.addAction(new DelayAction(announcementDelay + 0.4f));
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        frameLabelGroup.addAction(sequenceAction);

    }

    private void finalizeAnnouncement(){
        touchReady = false;
        SequenceAction sequenceAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;

        sequenceAction = new SequenceAction();

        //Base
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Volvemos a poner la música a volumen normal
                AudioManager.getInstance().changeMusicVolume(true, 1.0f);
            }
        });
        sequenceAction.addAction(runnableAction);
        baseGroup.addAction(sequenceAction);


        // Oscuridad
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0f);
        alphaAction.setDuration(0.5f);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        darknessImage.addAction(sequenceAction);


        // Estrellas
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0, 0);
        scaleToAction.setDuration(0.4f);
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                setFinalized(true);
            }
        });
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new RemoveActorAction());
        starsImage.addAction(sequenceAction);


        // Recuadro Texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 0);
        scaleToAction.setDuration(0.2f);
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        sequenceAction.addAction(new RemoveActorAction());
        textFrameImage.addAction(sequenceAction);


        // Texto Titulo
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(3, 0);
        scaleToAction.setDuration(0.2f);

        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        sequenceAction.addAction(new RemoveActorAction());
        frameLabelGroup.addAction(sequenceAction);
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
