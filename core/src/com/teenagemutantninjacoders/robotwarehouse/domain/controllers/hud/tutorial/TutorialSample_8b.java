package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.AnimatedImageActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 17/01/2021.
 * TUTORIAL: Baldosas rotatorias (b)
 */
public class TutorialSample_8b extends TutorialSample{
    private Image box;
    private Image[] touchCircles = new Image[1];
    private Group hand;
    private Group rotatingFloorGroup_1, rotatingFloorGroup_2;
    private AnimatedImageActor rotatingFloorPlatform_1, rotatingFloorPlatform_2;
    public TutorialSample_8b() {
        TextureAtlas tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        TextureAtlas boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        TextureAtlas rotatingFloorAtlas = Assets.getTextureAtlas("th01_rotatingFloor");

        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        Image scenarioImage = new Image(tutorialAtlas.findRegion("scenario", 1));
        scenarioImage.setPosition(50, 8);

        box = new Image(boxAtlas.findRegion("redBox"));
        box.setPosition(82,40);
        box.setOrigin(Align.center);

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

        rotatingFloorGroup_2 = new Group();
        rotatingFloorGroup_2.setPosition(50,40);
        rotatingFloorPlatform_2 = new AnimatedImageActor(rotatingFloorAtlas, "arrow", 0.1f, Animation.PlayMode.LOOP);
        rotatingFloorPlatform_2.play();
        rotatingFloorPlatform_2.setOrigin(Align.center);

        Image rotatingFloorBack_2 = new Image(rotatingFloorAtlas.findRegion("floor_background"));
        Image rotatingFloorFrame_2 = new Image(rotatingFloorAtlas.findRegion("floor_mask"));
        rotatingFloorGroup_2.addActor(rotatingFloorBack_2);
        rotatingFloorGroup_2.addActor(rotatingFloorPlatform_2);
        rotatingFloorGroup_2.addActor(rotatingFloorFrame_2);

        sampleGroup.addActor(scenarioImage);
        sampleGroup.addActor(rotatingFloorGroup_1);
        sampleGroup.addActor(rotatingFloorGroup_2);
        sampleGroup.addActor(box);

        touchCircles[0] = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
        touchCircles[0].setColor(1, 1, 1, 0);
        touchCircles[0].setOrigin(Align.center);

        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(-63,-63);
        hand.setColor(1,1,1,0);
        hand.setPosition(96 + 40, 20 + 60);

        sampleGroup.addActor(hand);
        sampleGroup.addActor(touchCircles[0]);

        ConfigureSecuenceLoop(5.5f);
    }

    @Override
    protected void beginAnimationSequence(){
        begindHand();
        beginBoxes();
        beginFloorRotation();
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
        ScaleToAction scaleAction;

        // Inicio loop
        box.setColor(1, 1, 1, 1);
        box.setPosition(82, 40);
        box.setScale(1, 1);

        sequenceAction = new SequenceAction();

        // Aparicion
        sequenceAction.addAction(new DelayAction(0.3f));
        sequenceAction.addAction(new DelayAction(1.63f));
        // Movimiento
        for(int i = 0 ; i < 3; i++) {
            moveToAction1 = new MoveToAction();
            moveToAction1.setPosition(230, box.getY());
            moveToAction1.setDuration(0.35f);
            moveToAction2 = new MoveToAction();
            moveToAction2.setPosition(62, box.getY());
            moveToAction2.setDuration(0.35f);
            RunnableAction speed1Runnable = new RunnableAction();

            speed1Runnable.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.ROTATING_FLOOR);
                }
            });
            RunnableAction speed2Runnable = new RunnableAction();

            speed2Runnable.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.ROTATING_FLOOR);
                }
            });
            sequenceAction.addAction(moveToAction1);
            sequenceAction.addAction(speed1Runnable);
            sequenceAction.addAction(moveToAction2);
            sequenceAction.addAction(speed2Runnable);
        }

        // Ultimo movimiento hacia la derecha y caida hacia abajo
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(242, box.getY());
        moveToAction1.setDuration(0.35f);
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0);
        scaleToAction.setDuration(0.6f);//0.5
        scaleToAction.setInterpolation(Interpolation.sineIn);
        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(242, box.getY() - 100);
        moveToAction2.setDuration(0.9f);//0.5
        moveToAction2.setInterpolation(Interpolation.fastSlow);
        RunnableAction runnableFallSound = new RunnableAction();
        runnableFallSound.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.FALL);
                AudioManager.getInstance().playSound(AudioManager.SOUND.ROTATING_FLOOR);
            }
        });

        sequenceAction.addAction(moveToAction1);
        ParallelAction parallelAction = new ParallelAction(scaleToAction, moveToAction2, runnableFallSound);
        sequenceAction.addAction(parallelAction);

        box.addAction(sequenceAction);
    }

    private void beginFloorRotation()
    {
        // Inicio loop
        rotatingFloorPlatform_1.setRotation(180);

        SequenceAction sequenceAction = new SequenceAction();
        RotateByAction rotateByAction = new RotateByAction();
        rotateByAction.setAmount(90);
        rotateByAction.setDuration(0.3f);
        RotateByAction rotateByAction2 = new RotateByAction();
        rotateByAction2.setAmount(-90);
        rotateByAction2.setDuration(0.3f);

        sequenceAction.addAction(new DelayAction(1.9f)); // Hasta que lanza caja
        sequenceAction.addAction(new DelayAction(1.9f)); // Tiempo de caja yendo y volviendo
        sequenceAction.addAction(rotateByAction);
        sequenceAction.addAction(new DelayAction(1.3f));
        sequenceAction.addAction(rotateByAction2);

        rotatingFloorPlatform_1.addAction(sequenceAction);
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
