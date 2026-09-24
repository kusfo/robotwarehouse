package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 27/04/2016.
 */
public class GUIobjectNewBoxPoints {
    private float x;
    private float y;
    private int combo;
    private int points;
    private GameEventsManager gameEventsManager;
    private Label.LabelStyle labelStyle_newPoints_a, labelStyle_newPoints_b;
    private Group layer;

    public GUIobjectNewBoxPoints(float x, float y, int combo, int points, Group layer, GameEventsManager gameEventsManager){
        this.x = x;
        this.y = y;
        this.combo = combo;
        this.points = points;
        this.layer = layer;
        this.gameEventsManager = gameEventsManager;
        labelStyle_newPoints_a = new Label.LabelStyle();
        labelStyle_newPoints_a.font = Assets.getFont("f_points_a");
        labelStyle_newPoints_b = new Label.LabelStyle();
        labelStyle_newPoints_b.font = Assets.getFont("f_points_b");
        execute();
    }

    private void execute() {
        float timeBase = 0.2f;
        float pauseTime = 1.1f;

        // La escala y potencia del efecto del multiplicador aumenta con el combo
        float multiplierScaleEffect;
        if(combo >= 10){ multiplierScaleEffect = 0.7f;}
        else if(combo >= 6) multiplierScaleEffect = 0.5f;
        else if(combo > 3) multiplierScaleEffect = 0.3f;
        else multiplierScaleEffect = 0.1f;

        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_POINTS);

        Label labelCombo = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().format("combo_achieved"), labelStyle_newPoints_a);
        Label labelMultiplier = new Label("x" + Integer.toString(combo), labelStyle_newPoints_a);

        // Limitamos la posicion del cartel para que no se salga de la zona de juego
        float multiplierWidth =  labelMultiplier.getWidth() * (1 + multiplierScaleEffect);
        if(x - labelCombo.getWidth() < GameConstants.BOARD_ORIGIN_X + 60)
            x = GameConstants.BOARD_ORIGIN_X + 60 + labelCombo.getWidth();
        else if(x + 50 + multiplierWidth > GameConstants.HORIZONTAL_RESOLUTION)
            x = GameConstants.HORIZONTAL_RESOLUTION - multiplierWidth - 50;
        if(y > 260) y = 260;

        final AnimatedImageActor baseSparkAnimation = new AnimatedImageActor( Assets.getTextureAtlas("fx_boxPointsBaseSpark"), 0.04f, Animation.PlayMode.NORMAL);
        baseSparkAnimation.setPosition(x - 35 - (baseSparkAnimation.getWidth() / 2), y - 40);
        baseSparkAnimation.play();

        // COMBO
        Container comboContainer = new Container(labelCombo);
        comboContainer.setOrigin(0, -labelCombo.getHeight() / 4);
        comboContainer.setTransform(true);
        comboContainer.align(Align.right);
        comboContainer.setPosition(x - 70, y + 15);
        comboContainer.setScale(1, 0);

        // MULTIPLICADOR
        Container multiplierContainer = new Container(labelMultiplier);
        multiplierContainer.setColor(1,1,1,0);
        multiplierContainer.setOrigin(0, -labelMultiplier.getHeight() / 4);
        multiplierContainer.setTransform(true);
        multiplierContainer.align(Align.center);
        multiplierContainer.setPosition(x + 30 + ((labelMultiplier.getWidth() * (1 + multiplierScaleEffect)) / 2), y + 15);

        multiplierContainer.setScale(0.7f + multiplierScaleEffect, 0.7f + multiplierScaleEffect);

        // PUNTOS
        Label labelPoints = new Label(Integer.toString(points), labelStyle_newPoints_b);
        Container pointsContainer = new Container(labelPoints);
        pointsContainer.setOrigin(0, -labelPoints.getHeight() / 4);
        pointsContainer.setTransform(true);
        pointsContainer.align(Align.right);
        pointsContainer.setPosition(x + 130, y - 18); //15
        pointsContainer.setScale(1, 0);

        // Acciones
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        MoveToAction moveToAction;
        ScaleToAction scaleAction;
        DelayAction delayAction;
        RemoveActorAction removeActor;
        AlphaAction alphaAction;

        // Spark
        sequenceAction = new SequenceAction();
        sequenceAction.addAction( new DelayAction(1.5f));
        sequenceAction.addAction(new RemoveActorAction());
        baseSparkAnimation.addAction(sequenceAction);


        // Secuencia Combo
        // --------------------------------------------------------------------------
        sequenceAction = new SequenceAction();

        // move + scale
        moveToAction = new MoveToAction();
        moveToAction.setPosition(x + 30, y + 15);
        moveToAction.setDuration(timeBase);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1, 1);
        scaleAction.setDuration(timeBase);

        parallelAction = new ParallelAction(moveToAction, scaleAction);
        sequenceAction.addAction(parallelAction);

        // delay
        delayAction = new DelayAction();
        delayAction.setDuration(pauseTime);
        sequenceAction.addAction(delayAction);

        // move + scale
        moveToAction = new MoveToAction();
        moveToAction.setPosition(x - 70, y + 15);
        moveToAction.setDuration(timeBase);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1, 0);
        scaleAction.setDuration(timeBase);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(timeBase);
        parallelAction = new ParallelAction(moveToAction, scaleAction, alphaAction);
        sequenceAction.addAction(parallelAction);

        // remove
        removeActor = new RemoveActorAction();
        sequenceAction.addAction(removeActor);
        // Ejecucion
        comboContainer.addAction(sequenceAction);



        // Secuencia Multiplicador
        // --------------------------------------------------------------------------
        sequenceAction = new SequenceAction();
        Interpolation interpolation = new Interpolation.ElasticOut(1.0f,0.7f + multiplierScaleEffect,3,0.4f + multiplierScaleEffect);

        delayAction = new DelayAction();
        delayAction.setDuration(timeBase - (timeBase / 3));
        sequenceAction.addAction(delayAction);

        // scale + alpha
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1.0f + multiplierScaleEffect, 1.0f + multiplierScaleEffect);
        scaleAction.setDuration(timeBase);
        scaleAction.setInterpolation(interpolation);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(timeBase);

        parallelAction = new ParallelAction(alphaAction, scaleAction);
        sequenceAction.addAction(parallelAction);

        // delay
        delayAction = new DelayAction();
        delayAction.setDuration(pauseTime - timeBase);
        sequenceAction.addAction(delayAction);


        scaleAction = new ScaleToAction();
        scaleAction.setScale(0 + multiplierScaleEffect, 0 + multiplierScaleEffect);
        scaleAction.setDuration(timeBase);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(timeBase - (timeBase / 2));

        parallelAction = new ParallelAction(alphaAction, scaleAction);
        sequenceAction.addAction(parallelAction);

        // remove
        removeActor = new RemoveActorAction();
        sequenceAction.addAction(removeActor);

        // Ejecucion
        multiplierContainer.addAction(sequenceAction);



        // Secuencia Puntos
        // --------------------------------------------------------------------------
        sequenceAction = new SequenceAction();

        // move + scale
        moveToAction = new MoveToAction();
        moveToAction.setPosition(x + 20, y - 18);
        moveToAction.setDuration(timeBase);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1, 1);
        scaleAction.setDuration(timeBase);

        parallelAction = new ParallelAction(moveToAction, scaleAction);
        sequenceAction.addAction(parallelAction);

        // delay
        delayAction = new DelayAction();
        delayAction.setDuration(pauseTime);
        sequenceAction.addAction(delayAction);

        // move + scale
        moveToAction = new MoveToAction();
        moveToAction.setPosition(x + 130, y - 18);
        moveToAction.setDuration(timeBase);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(2, 0);
        scaleAction.setDuration(timeBase);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(timeBase);
        parallelAction = new ParallelAction(moveToAction, scaleAction, alphaAction);
        sequenceAction.addAction(parallelAction);

        // Fin del cartel
        RunnableAction endPoints = new RunnableAction();
        endPoints.setRunnable(new Runnable() {
            @Override
            public void run() {
                gameEventsManager.finalizeBoxPointsEvent();

            }
        });
        sequenceAction.addAction(endPoints);

        // remove
        removeActor = new RemoveActorAction();
        sequenceAction.addAction(removeActor);
        // Ejecucion
        pointsContainer.addAction(sequenceAction);

        layer.addActor(baseSparkAnimation);
        layer.addActor(comboContainer);
        layer.addActor(multiplierContainer);
        layer.addActor(pointsContainer);
    }
}
