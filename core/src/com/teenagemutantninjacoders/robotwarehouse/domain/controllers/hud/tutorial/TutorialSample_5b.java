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
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.ParticleEffectActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by JordiRM on 05/09/2020.
 *  * TUTORIAL: Primeras barreras b
 */
public class TutorialSample_5b extends TutorialSample {
    private Image[] box = new Image[1];
    private AnimatedImageActor barrier;
    private Image[] touchCircles = new Image[1];
    private Group hand;
    private ParticleEffectActor destroyEffect;

    public TutorialSample_5b() {
        TextureAtlas tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        TextureAtlas boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        TextureAtlas barrierAtlas = Assets.getTextureAtlas("th01_barriers");
        TextureAtlas wallAtlas = Assets.getTextureAtlas("th01_wall01");
        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        Image scenarioImage = new Image(tutorialAtlas.findRegion("scenario", 1));
        scenarioImage.setPosition(50, 8);

        barrier = new AnimatedImageActor(barrierAtlas, "barrier_solid_none", 0.05f, Animation.PlayMode.NORMAL);
        barrier.setPosition(210, 40);
        barrier.show(true);
        barrier.setAlwaysAnimate(true);
        box[0] = new Image(boxAtlas.findRegion("redBox"));
        box[0].setPosition(82, 40);

        Image wallImage = new Image(wallAtlas.getRegions().get(0));
        wallImage.setPosition(242, 40);

        sampleGroup.addActor(scenarioImage);
        sampleGroup.addActor(barrier);
        sampleGroup.addActor(box[0]);
        sampleGroup.addActor(wallImage);

        touchCircles[0] = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
        touchCircles[0].setColor(1, 1, 1, 0);
        touchCircles[0].setOrigin(Align.center);

        hand = new Group();
        hand.addActor(new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(-63, -63);
        hand.setColor(1, 1, 1, 0);
        hand.setPosition(96 + 40, 96);

        sampleGroup.addActor(hand);
        sampleGroup.addActor(touchCircles[0]);

        ConfigureSecuenceLoop(3.5f);
    }

    @Override
    protected void beginAnimationSequence() {
        begindHand();
        beginBarrier();
        beginBoxes();
    }

    private void begindHand() {
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
        moveAction.setPosition(96 + 40, 96);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(scaleAction);

        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1.f);
        alphaAction.setDuration(0.5f);
        sequenceAction.addAction(new DelayAction(0.55f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new DelayAction(0.2f));

        // Se mueve y pulsa en caja
        moveAction = new MoveToAction();
        moveAction.setPosition(96, 68);
        moveAction.setDuration(0.4f);
        subSequenceAction = new SequenceAction();
        subSequenceAction.addAction(new DelayAction(0.2f));
        scaleAction = new ScaleToAction();
        scaleAction.setScale(0.9f);
        scaleAction.setDuration(0.2f); // (Circle) time:
        RunnableAction touchCircleAction = new RunnableAction();
        touchCircleAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                showTouchCircle(0);
            }
        });
        subSequenceAction.addAction(scaleAction);
        subSequenceAction.addAction(touchCircleAction);
        parallelAction = new ParallelAction(moveAction, subSequenceAction);
        sequenceAction.addAction(parallelAction);

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f));

        // Lanzamiento 1
        moveAction = new MoveToAction();
        moveAction.setPosition(130 + 15, 68);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);
        sequenceAction.addAction(new DelayAction(0.3f));

        hand.addAction(sequenceAction);
    }

    private void beginBarrier() {
        SequenceAction sequenceAction;
        RunnableAction runnableAction;

        // Inicio loop
        barrier.restart(false);

        sequenceAction = new SequenceAction();
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                barrier.play();
                AudioManager.getInstance().playSound(AudioManager.SOUND.FLOORBARRIER_SOLID);
                destroyBox();
            }
        });
        sequenceAction.addAction(new DelayAction(3.0f));
        sequenceAction.addAction(runnableAction);
        barrier.addAction(sequenceAction);
    }

    private void beginBoxes() {
        SequenceAction sequenceAction;
        MoveToAction moveToAction;
        AlphaAction alphaAction;

        // Inicio loop
        box[0].setColor(1, 1, 1, 1);
        box[0].setPosition(82, 40);

        sequenceAction = new SequenceAction();

        // Aparicion
        //alphaAction = new AlphaAction();
        //alphaAction.setAlpha(1);
        //alphaAction.setDuration(0.3f);
        //sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new DelayAction(0.3f));
        // Movimiento
        moveToAction = new MoveToAction();
        moveToAction.setPosition(210, box[0].getY());
        moveToAction.setDuration(0.30f);
        RunnableAction moveAndHitRunnable = new RunnableAction();

        moveAndHitRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
            }
        });

        sequenceAction.addAction(new DelayAction(1.65f));
        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(moveAndHitRunnable);

        box[0].addAction(sequenceAction);

    }

    private void destroyBox() {
        box[0].setColor(1, 1, 1, 0);
        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_DESTRUCTION);
        destroyEffect = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.BOX_DESTROY_RED, box[0].getX() + 16, box[0].getY() + 26, 0));
        sampleGroup.addActor(destroyEffect);
    }

    private void showTouchCircle(int circleNumber) {
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
        if (destroyEffect != null) destroyEffect.disposeEffect();
    }
}