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
 * Created by JordiRM on 07/12/2017.
 */
public class TutorialSample_1b extends TutorialSample {
    private Image[] box = new Image[3];
    private AnimatedImageActor[] boxSpark = new AnimatedImageActor[3];
    private Image[] boxFlash = new Image[3];
    private Group hand;
    private Image touchCircle;
    private TextureAtlas boxAtlas, tutorialAtlas;

    public TutorialSample_1b(){
        tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        box[0] = new Image(boxAtlas.findRegion("redBox"));
        box[0].setPosition(64,20);
        box[0].setColor(1,1,1,0);
        box[1] = new Image(boxAtlas.findRegion("redBox"));
        box[1].setPosition(96,20);
        box[1].setColor(1,1,1,0);
        box[2] = new Image(boxAtlas.findRegion("redBox"));
        box[2].setPosition(228,20);
        box[2].setColor(1,1,1,0);
        sampleGroup.addActor(box[0]);
        sampleGroup.addActor(box[1]);
        sampleGroup.addActor(box[2]);
        boxFlash[0] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[0].setColor(1,1,1,0);
        boxFlash[1] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[1].setColor(1,1,1,0);
        boxFlash[2] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[2].setColor(1,1,1,0);
        sampleGroup.addActor(boxFlash[0]);
        sampleGroup.addActor(boxFlash[1]);
        sampleGroup.addActor(boxFlash[2]);
        boxSpark[0] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        boxSpark[1] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        boxSpark[2] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        sampleGroup.addActor(boxSpark[0]);
        sampleGroup.addActor(boxSpark[1]);
        sampleGroup.addActor(boxSpark[2]);
        touchCircle = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
        touchCircle.setOrigin(Align.center);
        touchCircle.setPosition(96 + 15 - (touchCircle.getWidth()/2),  20 + 32 - (touchCircle.getHeight()/2));
        touchCircle.setScale(0.7f, 0.7f);
        touchCircle.setColor(1, 1, 1, 0);
        sampleGroup.addActor(touchCircle);
        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(-63,-63);
        hand.setColor(1,1,1,0);
        hand.setPosition(96 + 40, 20 + 60);
        sampleGroup.addActor(hand);

        ConfigureSecuenceLoop(5.0f);
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
        MoveToAction moveAction;
        AlphaAction alphaAction;

        // BOX 2
        box[2].setColor(1,1,1,0);
        sequenceAction = new SequenceAction();

        // Aparicion
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);
        sequenceAction.addAction(alphaAction);

        box[2].addAction(sequenceAction);

        // BOX 1
        // Inicio loop
        box[1].setPosition(96,20);
        box[1].setColor(1,1,1,0);

        sequenceAction = new SequenceAction();

        // Aparicion
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);
        sequenceAction.addAction(alphaAction);

        moveAction = new MoveToAction();
        moveAction.setPosition(196, box[0].getY());
        moveAction.setDuration(0.3f);
        RunnableAction moveAndHitRunnable = new RunnableAction();
        moveAndHitRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
            }
        });
        sequenceAction.addAction(new DelayAction(1.7f));
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(moveAndHitRunnable);


        box[1].addAction(sequenceAction);

        // BOX 0
        // Inicio loop
        box[0].setPosition(64,20);
        box[0].setColor(1,1,1,0);

        sequenceAction = new SequenceAction();

        // Aparicion
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);
        sequenceAction.addAction(alphaAction);

        moveAction = new MoveToAction();
        moveAction.setPosition(164, box[1].getY());
        moveAction.setDuration(0.25f);
        sequenceAction.addAction(new DelayAction(3.5f));
        sequenceAction.addAction(moveAction);
        RunnableAction flashBoxesAction = new RunnableAction();
        flashBoxesAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_GROUP_CREATION);
                showGroupCreation();
            }
        });
        sequenceAction.addAction(flashBoxesAction);

        box[0].addAction(sequenceAction);
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
        moveAction.setPosition(96 + 40, 20 + 60);
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
        moveAction.setPosition(96 + 15,  20 + 32);
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

        // Lanzamiento 1
        moveAction = new MoveToAction();
        moveAction.setPosition(130 + 15,  20 + 32);
        moveAction.setDuration(0.4f);//0.4
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(new DelayAction(0.3f));

        // Desplazamiento hacia la siguiente caja en un arco
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1.0f);
        scaleAction.setDuration(0.5f);
        moveAction = new MoveToAction();
        moveAction.setPosition(97 + 15,  20 + 38);
        moveAction.setDuration(0.5f);
        parallelAction = new ParallelAction(scaleAction, moveAction);
        sequenceAction.addAction(parallelAction);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(0.9f);
        scaleAction.setDuration(0.5f);
        moveAction = new MoveToAction();
        moveAction.setPosition(64 + 15,  20 + 32);
        moveAction.setDuration(0.5f);
        parallelAction = new ParallelAction(scaleAction, moveAction);
        sequenceAction.addAction(parallelAction);
        touchCircleAction= new RunnableAction();
        touchCircleAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                showTouchCircle();
            }
        });
        sequenceAction.addAction(touchCircleAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f)); // time despues lanzamiento 1:  1.9f

        // Lanzamiento 2
        moveAction = new MoveToAction();
        moveAction.setPosition(98 + 15,  20 + 32);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);

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

    private void showGroupCreation(){
        AlphaAction alphaAction;
        for(int i = 0; i < 3; i++) {
            boxFlash[i].setPosition(box[i].getX() - 4, box[i].getY() - 4);
            boxFlash[i].setColor(1, 1, 1, 0.9f);
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(0);
            alphaAction.setDuration(0.3f);
            boxFlash[i].addAction(alphaAction);

            boxSpark[i].setPosition(box[i].getX() - 9, box[i].getY() - 4);
            boxSpark[i].play();
        }
    }
}
