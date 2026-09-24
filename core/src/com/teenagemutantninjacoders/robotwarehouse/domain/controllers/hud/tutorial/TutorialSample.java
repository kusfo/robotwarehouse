package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.ParticleEffectActor;

import java.util.ArrayList;

/**
 * Created by JordiRM on 11/12/2017.
 */
public class TutorialSample {
    protected Group sampleGroup;
    protected ArrayList<ParticleEffectActor> effectsList = new ArrayList<ParticleEffectActor>();

    public Group getSampleGroup(){
        return sampleGroup;
    }

    protected void ConfigureSecuenceLoop(float pauseTime){
        // Creamos la secuencia principal
        SequenceAction sequenceAction = new SequenceAction();

        // Pausa inicial
        sequenceAction.addAction(new DelayAction(0.5f));

        // Iniciamos la animacion
        RunnableAction beginAnimationAction = new RunnableAction();
        beginAnimationAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                beginAnimationSequence();
            }
        });
        sequenceAction.addAction(beginAnimationAction);

        // Pausa hasta que termine la animación
        sequenceAction.addAction(new DelayAction(pauseTime));

        // Finalizamos la animacion
        RunnableAction finzalizeAnimationAction = new RunnableAction();
        finzalizeAnimationAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                FinalizeAnimationSequence();
            }
        });
        sequenceAction.addAction(finzalizeAnimationAction);

        // Pausa final
        sequenceAction.addAction(new DelayAction(0.5f));

        // Metemos la secuencia en un loop
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceAction);

        sampleGroup.addAction(repeatAction);
    }

    protected void FinalizeAnimationSequence(){}
    protected void beginAnimationSequence(){}
    protected void FinalizeAnimationActor(Actor actor){
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.5f);

        actor.addAction(alphaAction);
    }

    public void DisposeEffects() {
    }
}
