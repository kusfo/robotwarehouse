package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 25/04/2019.
 * TUTORIAL: Eliminación de cajas cayendo al vacio y cajas grises.
 */

public class TutorialSample_2a extends TutorialSample {
    private Image[] box = new Image[3];
    private Image[] grayBox = new Image[3];
    private Group hand;
    private Image touchCircle;
    private Image[] crossTexture = new Image[3];
    private TextureAtlas boxAtlas, tutorialAtlas;
    public TutorialSample_2a() {
        tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        Image scenarioImage = new Image(tutorialAtlas.findRegion("scenario", 1));
        scenarioImage.setPosition(64, 8);
        sampleGroup.addActor(scenarioImage);

        box[0] = new Image(boxAtlas.findRegion("redBox"));
        box[0].setPosition(128,40);
        box[0].setColor(1,1,1,0);
        box[0].setOrigin(Align.center);
        box[1] = new Image(boxAtlas.findRegion("redBox"));
        box[1].setPosition(192,40);
        box[2] = new Image(boxAtlas.findRegion("redBox"));
        box[2].setPosition(224,40);
        grayBox[1] = new Image(boxAtlas.findRegion("greyBox"));
        grayBox[1].setPosition(192,40);
        grayBox[1].setColor(1,1,1,0);
        grayBox[2] = new Image(boxAtlas.findRegion("greyBox"));
        grayBox[2].setPosition(224,40);
        grayBox[2].setColor(1,1,1,0);

        sampleGroup.addActor(box[0]);
        sampleGroup.addActor(box[1]);
        sampleGroup.addActor(box[2]);
        sampleGroup.addActor(grayBox[1]);
        sampleGroup.addActor(grayBox[2]);

        crossTexture[1] = new Image(Assets.getTextureAtlas("level_screen_elements").findRegion("boxDisabledCross"));
        crossTexture[1] .setOrigin(Align.center);
        crossTexture[1] .setPosition(192 + 3, 40 + 13);
        crossTexture[1] .setColor(1, 1, 1, 0);
        crossTexture[1] .setScale(6.0f, 6.0f);
        crossTexture[2] = new Image(Assets.getTextureAtlas("level_screen_elements").findRegion("boxDisabledCross"));
        crossTexture[2] .setOrigin(Align.center);
        crossTexture[2] .setPosition(224 + 3, 40 + 13);
        crossTexture[2] .setColor(1, 1, 1, 0);
        crossTexture[2] .setScale(6.0f, 6.0f);

        touchCircle = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
        touchCircle.setOrigin(Align.center);
        touchCircle.setPosition(128 + 15 - (touchCircle.getWidth()/2),  20 + 32 - (touchCircle.getHeight()/2));
        touchCircle.setScale(0.7f, 0.7f);
        touchCircle.setColor(1, 1, 1, 0);
        sampleGroup.addActor(touchCircle);
        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(63,-63);
        hand.getChildren().get(0).setScaleX(-1);
        hand.setColor(1,1,1,0);
        hand.setPosition(128 + 40, 40 + 60);

        sampleGroup.addActor(hand);

        ConfigureSecuenceLoop(6.0f);
    }

    @Override
    protected void beginAnimationSequence(){
        beginBoxes();
        beginCrosses();
        begindHand();
    }

    private void beginBoxes(){
        SequenceAction sequenceAction;
        MoveToAction moveAction;
        AlphaAction alphaAction;

        // Inicializacion
        box[0].setPosition(128, 40);
        box[0].setScale(1);

        sequenceAction = new SequenceAction();
        fadeOnActor(box[0], sequenceAction);
        moveAction = new MoveToAction();
        moveAction.setPosition(32, box[0].getY());
        moveAction.setDuration(0.3f);
        sequenceAction.addAction(new DelayAction(1.77f));
        sequenceAction.addAction(moveAction);
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0);
        scaleToAction.setDuration(0.5f);
        moveAction = new MoveToAction();
        moveAction.setPosition(32, 0);
        moveAction.setDuration(0.5f);
        RunnableAction runnableFallSound = new RunnableAction();
        runnableFallSound.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.FALL);
            }
        });

        sequenceAction.addAction(runnableFallSound);
        sequenceAction.addAction(new ParallelAction(scaleToAction, moveAction));
        box[0].addAction(sequenceAction);

        // Cajas grises solapando las rojas
        for(int i = 1; i < 3; i++) {
            // inicializacion
            grayBox[i].setColor(1, 1, 1, 0);

            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.2f);
            sequenceAction = new SequenceAction();
            sequenceAction.addAction(new DelayAction(3.0f));
            sequenceAction.addAction(alphaAction);
            grayBox[i].addAction(sequenceAction);
        }
    }

    private void beginCrosses(){
        ParallelAction parallelAction;
        SequenceAction sequenceAction;
        ScaleToAction scaleAction1;
        AlphaAction alphaAction;

        for(int i = 1; i < 3; i++) {

            // Inicializacion
            crossTexture[i] .setColor(1, 1, 1, 0);
            crossTexture[i] .setScale(6.0f, 6.0f);

            sequenceAction = new SequenceAction();
            sequenceAction.addAction(new DelayAction(2.56f));
            RunnableAction runnableDisablingSound = new RunnableAction();
            runnableDisablingSound.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_DISABLING);
                }
            });
            if(i == 1) sequenceAction.addAction(runnableDisablingSound);

            // Aparicion
            scaleAction1 = new ScaleToAction();
            scaleAction1.setScale(1.0f, 1.0f);
            scaleAction1.setDuration(0.4f);
            scaleAction1.setInterpolation(Interpolation.pow2In);
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.12f);
            parallelAction = new ParallelAction(scaleAction1, alphaAction);

            sequenceAction.addAction(new DelayAction(0.35f));
            sequenceAction.addAction(parallelAction);

            // Desaparicion
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(0);
            alphaAction.setDuration(0.8f);
            sequenceAction.addAction(new DelayAction(0.8f));
            sequenceAction.addAction(alphaAction);
            sequenceAction.addAction(new RemoveActorAction());

            crossTexture[i].addAction(sequenceAction);
            sampleGroup.addActor(crossTexture[i]);
        }
    }

    private void begindHand(){
        SequenceAction sequenceAction, subSequenceAction;
        ParallelAction parallelAction;
        MoveToAction moveAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;

        sequenceAction = new SequenceAction();

        // Inicio loop
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.f);
        moveAction = new MoveToAction();
        moveAction.setPosition(128 + 40, 40 + 60);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(scaleAction);


        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1.f);
        alphaAction.setDuration(0.5f);
        sequenceAction.addAction(new DelayAction(0.5f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new DelayAction(0.2f));

        // Se mueve y pulsa en la caja
        moveAction = new MoveToAction();
        moveAction.setPosition(128 + 15,  40 + 32);
        moveAction.setDuration(0.4f);
        subSequenceAction = new SequenceAction();
        subSequenceAction.addAction(new DelayAction(0.2f));
        scaleAction = new ScaleToAction();
        scaleAction.setScale(0.9f);
        scaleAction.setDuration(0.2f); // (Circle) time:
        RunnableAction touchCircleAction= new RunnableAction();
        touchCircleAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                showTouchCircle();
            }
        });
        subSequenceAction.addAction(scaleAction);
        subSequenceAction.addAction(touchCircleAction);
        parallelAction = new ParallelAction(moveAction,subSequenceAction);
        sequenceAction.addAction(parallelAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f));

        // Lanzamiento
        moveAction = new MoveToAction();
        moveAction.setPosition(96 + 15,  40 + 32);
        moveAction.setDuration(0.4f);//0.4
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(new DelayAction(0.6f));

        // Desaparicion
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.3f);
        sequenceAction.addAction(alphaAction);

        hand.addAction(sequenceAction);
    }

    private void showTouchCircle(){

        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;

        touchCircle.setPosition(hand.getX() - (touchCircle.getWidth() / 2), hand.getY() - (touchCircle.getHeight() / 2));
        touchCircle.setScale(0.5f, 0.5f);
        touchCircle.setColor(1, 1, 1, 0);

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
        touchCircle.addAction(sequenceAction);
    }

    private void fadeOnActor(Actor actor, SequenceAction sequenceAction){
        actor.setColor(1, 1, 1, 0);
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);

        sequenceAction.addAction(alphaAction);
        actor.addAction(alphaAction);
    }
}
