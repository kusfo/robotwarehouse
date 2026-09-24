package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
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
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.AnimatedImageActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 16/01/2021.
 * TUTORIAL: Baldosas rotatorias (a)
 */
public class TutorialSample_8a extends TutorialSample{
    private Image box;
    private Image[] touchCircles = new Image[1];
    private Group hand;
    private Group rotatingFloorGroup_1;
    private AnimatedImageActor rotatingFloorPlatform_1;
    public TutorialSample_8a(){
        TextureAtlas tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        TextureAtlas wallAtlas = Assets.getTextureAtlas("th01_wall01");
        TextureAtlas boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        TextureAtlas rotatingFloorAtlas = Assets.getTextureAtlas("th01_rotatingFloor");

        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        Image scenarioImage = new Image(tutorialAtlas.findRegion("scenario", 1));
        scenarioImage.setPosition(50, 8);

        Image wallImage = new Image(wallAtlas.getRegions().get(0));
        wallImage.setPosition(50,40);

        box = new Image(boxAtlas.findRegion("redBox"));
        box.setPosition(82,40);

        rotatingFloorGroup_1 = new Group();
        rotatingFloorGroup_1.setPosition(242,40);
        rotatingFloorPlatform_1 = new AnimatedImageActor(rotatingFloorAtlas, "arrow", 0.1f, Animation.PlayMode.LOOP);
        rotatingFloorPlatform_1.play();
        rotatingFloorPlatform_1.setOrigin(Align.center);
        rotatingFloorPlatform_1.rotateBy(180);
        Image rotatingFloorBack_1 = new Image(rotatingFloorAtlas.findRegion("floor_background"));
        Image rotatingFloorFrame_1 = new Image(rotatingFloorAtlas.findRegion("floor_mask"));
        rotatingFloorGroup_1.addActor(rotatingFloorBack_1);
        rotatingFloorGroup_1.addActor(rotatingFloorPlatform_1);
        rotatingFloorGroup_1.addActor(rotatingFloorFrame_1);

        sampleGroup.addActor(scenarioImage);
        sampleGroup.addActor(rotatingFloorGroup_1);
        sampleGroup.addActor(box);
        sampleGroup.addActor(wallImage);

        touchCircles[0] = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
        touchCircles[0].setColor(1, 1, 1, 0);
        touchCircles[0].setOrigin(Align.center);

        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(-63,-63);
        hand.setColor(1,1,1,0);
        //hand.setPosition(96 + 40, 96);
        hand.setPosition(96 + 40, 20 + 60);


        sampleGroup.addActor(hand);
        sampleGroup.addActor(touchCircles[0]);

        ConfigureSecuenceLoop(3.0f);
    }

    @Override
    protected void beginAnimationSequence(){
        begindHand();
        beginBoxes();
    }

    private void begindHand(){

        SequenceAction sequenceAction, subSequenceAction;
        ParallelAction parallelAction;
        MoveToAction moveAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction, alphaAction2;

        sequenceAction = new SequenceAction();

        // Inicio loop
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.f);
        moveAction = new MoveToAction();
        moveAction.setPosition(96 + 40, 96);
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

        // Se mueve y pulsa en caja
        moveAction = new MoveToAction();
        moveAction.setPosition(96,  68);
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
                showTouchCircle(0);
            }
        });
        subSequenceAction.addAction(scaleAction);
        subSequenceAction.addAction(touchCircleAction);
        parallelAction = new ParallelAction(moveAction,subSequenceAction);
        sequenceAction.addAction(parallelAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f));

        // Lanzamiento 1
        moveAction = new MoveToAction();
        moveAction.setPosition(130 + 15,  68);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);

        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0.f);
        alphaAction2.setDuration(0.4f);
        sequenceAction.addAction(alphaAction2);

        hand.addAction(sequenceAction);
    }

    private void beginBoxes() {
        SequenceAction sequenceAction;
        MoveToAction moveToAction1, moveToAction2;

        // Inicio loop
        box.setColor(1, 1, 1, 1);
        box.setPosition(82, 40);

        sequenceAction = new SequenceAction();

        // Aparicion
        sequenceAction.addAction(new DelayAction(0.3f));
        // Movimiento
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(230, box.getY());
        moveToAction1.setDuration(0.35f);
        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(82, box.getY());
        moveToAction2.setDuration(0.35f);
        RunnableAction speedRunnable = new RunnableAction();

        speedRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.ROTATING_FLOOR);
            }
        });
        RunnableAction moveAndHitRunnable = new RunnableAction();

        moveAndHitRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
            }
        });

        sequenceAction.addAction(new DelayAction(1.63f));
        sequenceAction.addAction(moveToAction1);
        sequenceAction.addAction(speedRunnable);
        sequenceAction.addAction(moveToAction2);
        sequenceAction.addAction(moveAndHitRunnable);

        box.addAction(sequenceAction);
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
}
