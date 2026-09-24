package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by JordiRM on 29/11/2018.
 */
public class GUIobjectEpisodeCompleted {
    private Group baseGroup;
    private Label.LabelStyle labelStyle_base_bb_16, labelStyle_title_24;
    private Group layer;
    private TextureAtlas generalAtlas;
    private Color titleColor;
    private float announcementDelay = 1.2f;
    private float announcementTime = 10;
    private boolean finalized = false;
    private int currentEpisode;
    private boolean touchReady = false;

    private Image darknessImage, textFrameImage, levelImage, checkImage, rotatingLinesImage;
    private Image starLeft_B, starLeft_M, starLeft_S, starRight_B, starRight_M, starRight_S;
    private Label frameLabel, descriptionLabel;
    private Group frameLabelGroup, starGroup_B, starGroup_M,starGroup_S;

    public GUIobjectEpisodeCompleted(Group layer, int currentEpisode){
        this.layer = layer;
        this.currentEpisode = currentEpisode;
        labelStyle_title_24 = new Label.LabelStyle();
        labelStyle_title_24.font = Assets.getFont("f_cartel_partida_24");

        labelStyle_base_bb_16 = new Label.LabelStyle();
        labelStyle_base_bb_16.font = Assets.getFont("f_base_bb_14");
        titleColor = new Color(72 / 255f, 237 / 255f, 68 / 255f, 1);
        generalAtlas = Assets.getTextureAtlas("levelSelection_general");
        execute();
    }

    private void execute(){
        int centerX = GameConstants.HORIZONTAL_RESOLUTION / 2;

        darknessImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        textFrameImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("floatingTextFrame", 2));
        levelImage = new Image(generalAtlas.findRegion("episodeComplete_level"));
        checkImage = new Image(generalAtlas.findRegion("episodeComplete_check"));
        rotatingLinesImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("rotatingLines"));
        frameLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("episode_completed"), labelStyle_title_24);
        frameLabelGroup = new Group();
        descriptionLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("episode_" + Integer.toString(currentEpisode)
                + "_success_text"), labelStyle_base_bb_16);
        starLeft_B = new Image(generalAtlas.findRegion("episodeComplete_star_B"));
        starLeft_M = new Image(generalAtlas.findRegion("episodeComplete_star_M"));
        starLeft_S = new Image(generalAtlas.findRegion("episodeComplete_star_S"));
        starRight_B = new Image(generalAtlas.findRegion("episodeComplete_star_B"));
        starRight_M = new Image(generalAtlas.findRegion("episodeComplete_star_M"));
        starRight_S = new Image(generalAtlas.findRegion("episodeComplete_star_S"));

        baseGroup = new Group();
        baseGroup.setPosition(centerX, 150);
        baseGroup.setTouchable(Touchable.disabled);

        levelImage.setPosition( -levelImage.getWidth() / 2, 20);
        levelImage.setOrigin(Align.center);
        levelImage.setScale(0.4f, 0.4f);
        levelImage.setColor(1, 1, 1, 0);

        checkImage.setPosition( (-checkImage.getWidth() / 2) + 5, 62);
        checkImage.setOrigin(Align.center);
        checkImage.setScale(0.4f, 0f);
        checkImage.setColor(1, 1, 1, 0);

        rotatingLinesImage.setPosition( -rotatingLinesImage.getWidth() / 2, (-rotatingLinesImage.getHeight() / 2) + 70);
        rotatingLinesImage.setColor(1, 1, 1, 0);
        rotatingLinesImage.setOrigin(Align.center);

        textFrameImage.setPosition(-textFrameImage.getWidth() / 2, 0);
        textFrameImage.setColor(1, 1, 1, 0);
        textFrameImage.setScale(1, 0);
        textFrameImage.setOrigin(Align.center);

        frameLabel.setColor(titleColor);
        frameLabel.setPosition(-frameLabel.getWidth() / 2, -frameLabel.getHeight() / 2);
        frameLabelGroup.setPosition(textFrameImage.getX() + (textFrameImage.getWidth() / 2), textFrameImage.getY() + 22);
        frameLabelGroup.setScale(3, 0.2f);
        frameLabelGroup.setColor(1, 1, 1, 0);
        frameLabelGroup.addActor(frameLabel);

        descriptionLabel.setWrap(true);
        descriptionLabel.setWidth(350);
        descriptionLabel.setAlignment(Align.top);
        descriptionLabel.setColor(0.8f, 0.8f, 0.8f, 0);
        descriptionLabel.setPosition(-descriptionLabel.getWidth() / 2, textFrameImage.getY() - 45);

        darknessImage.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        darknessImage.setColor(0, 0, 0, 0);
        darknessImage.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (touchReady) {
                    baseGroup.clearActions();
                    activeFinalization();
                }
                return true;
            }
        });

        starLeft_B.setPosition(-75, 40);
        starLeft_B.setOrigin(Align.bottomRight);
        starLeft_M.setPosition(-100, 35);
        starLeft_M.setOrigin(Align.bottomRight);
        starLeft_S.setPosition(-110, 45);
        starLeft_S.setOrigin(Align.bottomRight);

        starRight_B.setScale(-1, 1);
        starRight_B.setOrigin(Align.center);
        starRight_M.setScale(-1, 1);
        starRight_M.setOrigin(Align.center);
        starRight_S.setScale(-1, 1);
        starRight_S.setOrigin(Align.center);

        starGroup_B = new Group();
        starGroup_B.setPosition(75 - starRight_B.getWidth(), 40);
        starGroup_B.setSize(starRight_B.getWidth(), starRight_B.getHeight());
        starGroup_B.addActor(starRight_B);
        starGroup_M = new Group();
        starGroup_M.setPosition(100 - starRight_M.getWidth(), 35);
        starGroup_M.setSize(starGroup_M.getWidth(), starGroup_M.getHeight());
        starGroup_M.addActor(starRight_M);
        starGroup_S = new Group();
        starGroup_S.setPosition(110 - starRight_S.getWidth(), 45);
        starGroup_S.setSize(starGroup_S.getWidth(), starGroup_S.getHeight());
        starGroup_S.addActor(starRight_S);

        layer.addActor(darknessImage);
        baseGroup.addActor(rotatingLinesImage);
        baseGroup.addActor(textFrameImage);
        baseGroup.addActor(frameLabelGroup);
        baseGroup.addActor(descriptionLabel);
        baseGroup.addActor(levelImage);
        baseGroup.addActor(checkImage);
        baseGroup.addActor(starLeft_B);
        baseGroup.addActor(starLeft_M);
        baseGroup.addActor(starLeft_S);
        baseGroup.addActor(starGroup_B);
        baseGroup.addActor(starGroup_M);
        baseGroup.addActor(starGroup_S);

        layer.addActor(baseGroup);

        beginAnnouncement();
    }

    private void beginAnnouncement(){
        SequenceAction sequenceAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;
        RotateByAction rotateByAction;

        // Acciones de estrellas
        starActionBegin(starLeft_B, -95, 55, true);
        starActionBegin(starLeft_M, -120, 40, false);
        starActionBegin(starLeft_S, -130, 65, false);

        starActionBegin(starGroup_B, 95 - starRight_B.getWidth(), 55, true);
        starActionBegin(starGroup_M, 120 - starRight_M.getWidth(), 40, false);
        starActionBegin(starGroup_S, 130 - starRight_S.getWidth(), 65, false);

        // Base
        sequenceAction = new SequenceAction();
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Activamos el sonido de completo y bajamos el sonido de la música momentaneamente
                AudioManager.getInstance().playSound(AudioManager.SOUND.EPISODE_COMPLETED);
                AudioManager.getInstance().changeMusicVolume(true, 0.5f);
            }
        });
        sequenceAction.addAction(new DelayAction(0.9f));
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
        sequenceAction.addAction(new DelayAction(announcementTime));
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                activeFinalization();
            }
        });
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


        // Lineas
        sequenceAction = new SequenceAction();
        rotateByAction = new RotateByAction();
        rotateByAction.setAmount(40);
        rotateByAction.setDuration(announcementTime + 3f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        sequenceAction.addAction(new DelayAction(announcementDelay));
        sequenceAction.addAction(alphaAction);
        rotatingLinesImage.addAction(new ParallelAction(rotateByAction, sequenceAction));


        // Nivel
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
        levelImage.addAction(sequenceAction);


        // Check
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.1f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setInterpolation(Interpolation.elasticOut);
        scaleToAction.setDuration(0.5f);
        sequenceAction.addAction(new DelayAction(announcementDelay + 0.5f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(scaleToAction);
        checkImage.addAction(sequenceAction);


        // Recuadro texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1.8f);
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

        // Texto Description
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.7f);

        sequenceAction.addAction(new DelayAction(announcementDelay + 0.7f));
        sequenceAction.addAction(alphaAction);

        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        descriptionLabel.addAction(sequenceAction);
    }

    private void finalizeAnnouncement(){
        SequenceAction sequenceAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;

        starActionEnd(starLeft_B);
        starActionEnd(starLeft_M);
        starActionEnd(starLeft_S);
        starActionEnd(starGroup_B);
        starActionEnd(starGroup_M);
        starActionEnd(starGroup_S);

        // Oscuridad
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0f);
        alphaAction.setDuration(0.5f);
        sequenceAction.addAction(new DelayAction(1.8f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        darknessImage.addAction(sequenceAction);


        // Lineas
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.35f);
        sequenceAction.addAction(new DelayAction(1.1f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        rotatingLinesImage.addAction(sequenceAction);


        // Nivel
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0, 0);
        scaleToAction.setDuration(0.4f);
        sequenceAction.addAction(new DelayAction(1.2f));
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
        levelImage.addAction(sequenceAction);


        // Check
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.1f);
        sequenceAction.addAction(new DelayAction(1.0f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        checkImage.addAction(sequenceAction);


        // Recuadro texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 0);
        scaleToAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(1.2f));
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
        sequenceAction.addAction(new DelayAction(1.2f));
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction));
        sequenceAction.addAction(new RemoveActorAction());
        frameLabelGroup.addAction(sequenceAction);


        // Texto Description
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(1.0f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        descriptionLabel.addAction(sequenceAction);
    }

    private void starActionBegin(final Actor star, float finalX, float finalY, boolean effect){
        star.setColor(1, 1, 1, 0);
        star.setScale(0);

        AlphaAction alphaAction;
        ScaleToAction scaleToAction;
        MoveToAction moveToAction;

        SequenceAction sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.2f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.2f);
        moveToAction = new MoveToAction();
        moveToAction.setPosition(finalX, finalY);
        moveToAction.setDuration(0.3f);
        moveToAction.setInterpolation(Interpolation.pow2Out);

        sequenceAction.addAction(new DelayAction(announcementDelay + 0.4f));
        if(effect) {
            RunnableAction runnableAction = new RunnableAction();
            runnableAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    if(star.getX() < 0)
                        baseGroup.addActor(new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.RANK_STAR_LEFT,
                                star.getX() + 20, star.getY() + 25, 0)));
                    else
                        baseGroup.addActor(new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.RANK_STAR_RIGHT,
                                star.getX() + 20, star.getY() + 25, 0)));
                }
            });
            sequenceAction.addAction(runnableAction);
        }
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction, moveToAction));
        star.addAction(sequenceAction);
    }

    private void starActionEnd(final Actor star) {
        AlphaAction alphaAction;
        ScaleToAction scaleToAction;
        MoveToAction moveToAction;
        SequenceAction sequenceAction = new SequenceAction();

        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.1f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0);
        scaleToAction.setDuration(0.2f);
        moveToAction = new MoveToAction();
        moveToAction.setPosition(star.getX(), star.getY());
        moveToAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(1.2f));
        sequenceAction.addAction(new ParallelAction(alphaAction, scaleToAction, moveToAction));
        sequenceAction.addAction(new RemoveActorAction());
        star.addAction(sequenceAction);
    }

    private void activeFinalization(){
        touchReady = false;
        AudioManager.getInstance().changeMusicVolume(true, 1.0f);
        finalizeAnnouncement();
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }
}
