package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 04/07/2016.
 */
public class GUIobjectNewOverBasePoints {
    private float x;
    private float y;
    private int points;
    private Label.LabelStyle labelStyle_newPoints;
    private GameEventsManager eventsManager;
    private Label labelPoints;
    private Group layer;

    public GUIobjectNewOverBasePoints(float x, float y, int points, Group layer, GameEventsManager eventsManager){
        this.x = x;
        this.y = y;
        this.points = points;
        this.layer = layer;
        this.eventsManager = eventsManager;
        labelStyle_newPoints = new Label.LabelStyle();
        labelStyle_newPoints.font = Assets.getFont("f_points_b");
        execute();
    }

    private void execute() {
        AudioManager.getInstance().playSound(AudioManager.SOUND.OVERBASE_POINTS);
        labelPoints = new Label(Integer.toString(points), labelStyle_newPoints);

        Container pointsContainer = new Container(labelPoints);
        pointsContainer.setTransform(true);
        pointsContainer.setPosition(x, y);
        pointsContainer.setScale(0.5f, 0.5f);
        pointsContainer.setOrigin(0, -labelPoints.getHeight() / 4);

        SequenceAction sequenceAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;
        RemoveActorAction removeActor;


        sequenceAction = new SequenceAction();
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1f, 1f);
        scaleAction.setDuration(0.17f);
        Interpolation interpolation = new Interpolation.ElasticOut(1.0f, 0.6f, 1, 0.7f);
        scaleAction.setInterpolation(interpolation);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.5f);
        removeActor = new RemoveActorAction();

        RunnableAction endAction = new RunnableAction();
        endAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                eventsManager.finalizeOverBasePointsEvent();
            }
        });

        sequenceAction.addAction(scaleAction);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(endAction);
        sequenceAction.addAction(removeActor);

        // Ejecucion
        pointsContainer.addAction(sequenceAction);
        layer.addActor(pointsContainer);
    }
}