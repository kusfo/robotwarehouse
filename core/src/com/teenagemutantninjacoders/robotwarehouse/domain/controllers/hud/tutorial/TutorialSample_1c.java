package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.ParticleEffectActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by JordiRM on 11/12/2017.
 */
public class TutorialSample_1c extends TutorialSample {
    private Image[] box = new Image[3];;
    private Group hand;
    private Image[] touchCircles = new Image[2];
    private ParticleEffectActor[] transportEffect = new ParticleEffectActor[3];
    private TextureAtlas boxAtlas, tutorialAtlas;

    public TutorialSample_1c(){
        tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        for(int i = 0; i < 3; i++) {
            box[i] = new Image(boxAtlas.findRegion("redBox"));
            box[i].setPosition(116, 20);
            box[i].setColor(1, 1, 1, 0);
            box[i].setOriginX(16);
            sampleGroup.addActor(box[i]);
        }

        transportEffect[0] = null;
        transportEffect[1] = null;
        transportEffect[2] = null;

        for(int i = 0; i < 2; i++) {
            touchCircles[i] = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
            touchCircles[i].setColor(1, 1, 1, 0);
            touchCircles[i].setOrigin(Align.center);
        }
        sampleGroup.addActor(touchCircles[0]);
        sampleGroup.addActor(touchCircles[1]);
        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(-63,-63);
        hand.setColor(1,1,1,0);
        hand.setPosition(80, 80);
        sampleGroup.addActor(hand);

        ConfigureSecuenceLoop(3.0f);
    }

    @Override
    protected void beginAnimationSequence(){
        beginBoxes();
        begindHand();
    }
    @Override
    protected void FinalizeAnimationSequence(){
        FinalizeAnimationActor(hand);
        FinalizeAnimationActor(box[0]);
        FinalizeAnimationActor(box[1]);
        FinalizeAnimationActor(box[2]);
    }

    private void beginBoxes(){
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        AlphaAction alphaAction;
        ScaleToAction scaleToAction;
        MoveToAction moveToAction;

        for(int i = 0; i < 3; i++) {
            // Inicio loop
            box[i].setPosition(116 + (i * 32), 20);
            box[i].setColor(1,1,1,0);
            box[i].setScale(1,1);

            sequenceAction = new SequenceAction();

            // Aparicion
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.3f);
            sequenceAction.addAction(alphaAction);

            // Espera hasta la teletrasnportacion
            sequenceAction.addAction(new DelayAction(1.6f + (i * 0.2f)));

            // Effecto de particulas
            RunnableAction runnableAction = new RunnableAction();
            if(i == 0) {
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
                        transportEffect[0] = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.TELEPORT, box[0].getX() + 16, box[0].getY() + 26, 0));
                        sampleGroup.addActor(transportEffect[0]);

                    }
                });
            } else if(i == 1) {
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
                        transportEffect[1] = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.TELEPORT, box[1].getX() + 16, box[1].getY() + 26, 0));
                        sampleGroup.addActor(transportEffect[1]);
                    }
                });

            } else {
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
                        transportEffect[2] = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.TELEPORT, box[2].getX() + 16, box[2].getY() + 26, 0));
                        sampleGroup.addActor(transportEffect[2]);
                    }
                });
            }
            sequenceAction.addAction(runnableAction);

            // Teletransporte
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(0);
            alphaAction.setDuration(0.25f);
            scaleToAction = new ScaleToAction();
            scaleToAction.setScale(0.2f, 3.0f);
            scaleToAction.setDuration(0.4f);
            moveToAction = new MoveToAction();
            moveToAction.setPosition(box[i].getX(), 50);
            moveToAction.setDuration(0.25f);
            parallelAction = new ParallelAction(alphaAction, scaleToAction, moveToAction);
            sequenceAction.addAction(parallelAction);

            box[i].addAction(sequenceAction);
        }
    }

    private void begindHand(){
        SequenceAction sequenceAction;
        MoveToAction moveAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;

        sequenceAction = new SequenceAction();

        // Inicio loop
        hand.setPosition(100, 80);
        hand.setColor(1,1,1,0);
        hand.setScale(1,1);

        // Aparicion
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1.f);
        alphaAction.setDuration(0.5f);
        sequenceAction.addAction(alphaAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.2f));

        // Se mueve
        moveAction = new MoveToAction();
        moveAction.setPosition(116 + 15,  20 + 32);
        moveAction.setDuration(0.6f);
        sequenceAction.addAction(moveAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.2f));

        // Doble pulsacion sobre el grupo
        for(int i = 0; i < 2; i++) {
            scaleAction = new ScaleToAction();
            scaleAction.setScale(0.9f);
            scaleAction.setDuration(0.1f);
            sequenceAction.addAction(scaleAction);
            RunnableAction touchCircleAction= new RunnableAction();
            if(i == 0) {
                touchCircleAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        showTouchCircle(0);
                    }
                });
            }else{
                touchCircleAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        showTouchCircle(1);
                    }
                });
            }
            sequenceAction.addAction(touchCircleAction);
            scaleAction = new ScaleToAction();
            scaleAction.setScale(1.0f);
            scaleAction.setDuration(0.1f);
            sequenceAction.addAction(scaleAction);
        }

        hand.addAction(sequenceAction);
    }

    private void showTouchCircle(int circleNumber){

        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;

        touchCircles[circleNumber].setPosition(hand.getX() - (touchCircles[circleNumber].getWidth() / 2), hand.getY() - (touchCircles[circleNumber].getHeight() / 2));
        touchCircles[circleNumber].setScale(0.5f, 0.5f);
        touchCircles[circleNumber].setColor(1, 1, 1, 0);

        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.05f);
        sequenceAction.addAction(alphaAction);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(2);
        scaleAction.setDuration(0.3f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        parallelAction = new ParallelAction(scaleAction, alphaAction);
        sequenceAction.addAction(parallelAction);
        touchCircles[circleNumber].addAction(sequenceAction);
    }

    @Override
    public void DisposeEffects() {
        if(transportEffect[0] != null) transportEffect[0].disposeEffect();
        if(transportEffect[1] != null) transportEffect[1].disposeEffect();
        if(transportEffect[2] != null) transportEffect[2].disposeEffect();
    }
}