package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiM on 14/10/2019.
 */
public class GUIobjectBasePanel {
    protected Integer width, height;
    protected float finalPositionY;
    protected boolean activePanel = false;
    protected boolean hasGone = false;

    protected void enter(Group panelGroup){
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_ENTER);
        SequenceAction sequenceAction;
        MoveToAction moveToAction1;
        sequenceAction = new SequenceAction();

        // Bajar
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(panelGroup.getX(), finalPositionY);
        moveToAction1.setDuration(0.9f);

        Interpolation interpolation = new Interpolation.Swing(0.7f);

        moveToAction1.setInterpolation(interpolation);

        sequenceAction.addAction(moveToAction1);

        // Activar interaccion
        RunnableAction starInteraction = new RunnableAction();
        starInteraction.setRunnable(new Runnable() {
            @Override
            public void run() {
                arrived();
            }
        });
        sequenceAction.addAction(starInteraction);

        panelGroup.addAction(sequenceAction);
    }

    protected void enter(final Group panelGroup, float delayTime){

        RunnableAction beginEntering = new RunnableAction();
        beginEntering.setRunnable(new Runnable() {
            @Override
            public void run() {
                enter(panelGroup);
            }
        });
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(delayTime));
        sequenceAction.addAction(beginEntering);
        panelGroup.addAction(sequenceAction);
    }

    protected void arrived(){
        activePanel = true;
    }

    protected void leave(Group panelGroup){
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_LEAVES);
        activePanel = false;
        SequenceAction sequenceAction;
        MoveToAction moveToAction;
        RemoveActorAction removeActor;

        sequenceAction = new SequenceAction();

        moveToAction = new MoveToAction();
        moveToAction.setPosition(panelGroup.getX(), GameConstants.VERTICAL_RESOLUTION + 100);
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineIn);
        sequenceAction.addAction(moveToAction);

        // Finalizar panel
        RunnableAction finish = new RunnableAction();
        finish.setRunnable(new Runnable() {
            @Override
            public void run() {
                hasGone = true;
                DisposePhase();
            }
        });
        sequenceAction.addAction(finish);
        sequenceAction.addAction(new RemoveActorAction());

        panelGroup.addAction(sequenceAction);
    }

    protected void DisposePhase(){
        FinishPanel();
    }

    protected void FinishPanel(){

    }

    public boolean isPanelGone(){
        return hasGone;
    }
}