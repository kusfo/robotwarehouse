package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.AnimatedImageActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.ParticleEffectActor;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by JordiRM on 30/09/2019.
 * TUTORIAL: Obtención de poderes.
 */
public class TutorialSample_4a extends TutorialSample  {
    private Image[] box = new Image[3];
    private TextureAtlas boxAtlas, tutorialAtlas, boxActivableAtlas;
    private Image[] overbasePowerImage = new Image[3];
    private Image[] touchCircles = new Image[2];
    private Image[] boxFlash = new Image[3];
    private AnimatedImageActor[] boxSpark = new AnimatedImageActor[3];
    private ParticleEffectActor[] transportEffect = new ParticleEffectActor[3];
    private Group hand;
    public TutorialSample_4a() {
        tutorialAtlas = Assets.getTextureAtlas("tutorialPanel");
        boxAtlas = Assets.getTextureAtlas("th01_boxes01");
        boxActivableAtlas = Assets.getTextureAtlas("overBaseBoxActivable");
        sampleGroup = new Group();
        sampleGroup.setSize(325, 160);

        Image scenarioImage = new Image(tutorialAtlas.findRegion("scenario", 1));
        scenarioImage.setPosition(50, 8);

        box[0] = new Image(boxAtlas.findRegion("redBox"));
        box[0].setPosition(82,40);
        box[0].setOriginX(16);
        box[1] = new Image(boxAtlas.findRegion("redBox"));
        box[1].setPosition(210,40);
        box[1].setOriginX(16);
        box[2] = new Image(boxAtlas.findRegion("redBox"));
        box[2].setPosition(242,40);
        box[2].setOriginX(16);

        boxFlash[0] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[0].setColor(1,1,1,0);
        boxFlash[1] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[1].setColor(1,1,1,0);
        boxFlash[2] = new Image(boxAtlas.findRegion("boxFlash"));
        boxFlash[2].setColor(1,1,1,0);

        boxSpark[0] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        boxSpark[1] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        boxSpark[2] = new AnimatedImageActor(boxAtlas, "boxSpark", 0.1f, Animation.PlayMode.NORMAL);

        overbasePowerImage[0] = new Image(boxActivableAtlas.findRegion("skill_gen_a"));
        overbasePowerImage[0].setOrigin(Align.center);
        overbasePowerImage[1] = new Image(boxActivableAtlas.findRegion("skill_gen_a"));
        overbasePowerImage[1].setOrigin(Align.center);
        overbasePowerImage[2] = new Image(boxActivableAtlas.findRegion("skill_gen_a"));
        overbasePowerImage[2].setOrigin(Align.center);

        sampleGroup.addActor(scenarioImage);
        sampleGroup.addActor(overbasePowerImage[0]);
        sampleGroup.addActor(overbasePowerImage[1]);
        sampleGroup.addActor(overbasePowerImage[2]);
        sampleGroup.addActor(box[0]);
        sampleGroup.addActor(box[1]);
        sampleGroup.addActor(box[2]);
        sampleGroup.addActor(boxFlash[0]);
        sampleGroup.addActor(boxFlash[1]);
        sampleGroup.addActor(boxFlash[2]);
        sampleGroup.addActor(boxSpark[0]);
        sampleGroup.addActor(boxSpark[1]);
        sampleGroup.addActor(boxSpark[2]);

        for(int i = 0; i < 2; i++) {
            touchCircles[i] = new Image(tutorialAtlas.findRegion("tutorial_touchCircle"));
            touchCircles[i].setColor(1, 1, 1, 0);
            touchCircles[i].setOrigin(Align.center);
        }

        transportEffect[0] = null;
        transportEffect[1] = null;
        transportEffect[2] = null;

        sampleGroup.addActor(touchCircles[0]);
        sampleGroup.addActor(touchCircles[1]);

        hand = new Group();
        hand.addActor( new Image(tutorialAtlas.findRegion("tutorial_hand_point")));
        hand.getChildren().get(0).setPosition(63,-63);
        hand.getChildren().get(0).setScaleX(-1);
        hand.setColor(1,1,1,0);
        hand.setPosition(248, 100);

        sampleGroup.addActor(hand);

        ConfigureSecuenceLoop(6.5f);
    }

    @Override
    protected void beginAnimationSequence(){
        begindPowers();
        begindHand();
        beginBoxes();
    }

    private void begindPowers(){
        for(int i = 0; i < 3; i++) {
            overbasePowerImage[i].setDrawable(new SpriteDrawable(new Sprite(boxActivableAtlas.findRegion("skill_gen_a"))));
            overbasePowerImage[i].setPosition(82 + (i * 32), 40);
            overbasePowerImage[i].setScale(1, 1);
            overbasePowerImage[i].setColor(1, 1, 1, 1);
        }
    }

    private void begindHand() {
        SequenceAction sequenceAction, subSequenceAction;
        ParallelAction parallelAction;
        MoveToAction moveAction;
        ScaleToAction scaleAction;
        AlphaAction alphaAction;
        RunnableAction touchCircleAction;

        sequenceAction = new SequenceAction();

        // Inicio loop
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.f);
        moveAction = new MoveToAction();
        moveAction.setPosition(248, 100);
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
        // 1.2

        // Se mueve y pulsa en la caja (1)
        moveAction = new MoveToAction();
        moveAction.setPosition(223, 72);
        moveAction.setDuration(0.4f);
        subSequenceAction = new SequenceAction();
        subSequenceAction.addAction(new DelayAction(0.2f));
        scaleAction = new ScaleToAction();
        scaleAction.setScale(0.9f);
        scaleAction.setDuration(0.2f); // (Circle) time:
        touchCircleAction = new RunnableAction();
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
        //0.6

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f));

        // Lanzamiento (1)
        moveAction = new MoveToAction();
        moveAction.setPosition(191, 72);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);

        // Pausa y alza la mano
        sequenceAction.addAction(new DelayAction(0.2f));
        moveAction = new MoveToAction();
        moveAction.setPosition(270, 100);//223
        moveAction.setDuration(0.4f);
        sequenceAction.addAction(moveAction);

        // Se mueve y pulsa en la caja (2)
        moveAction = new MoveToAction();
        moveAction.setPosition(255, 72);
        moveAction.setDuration(0.4f);
        subSequenceAction = new SequenceAction();
        subSequenceAction.addAction(new DelayAction(0.2f));
        scaleAction = new ScaleToAction();
        scaleAction.setScale(0.9f);
        scaleAction.setDuration(0.2f); // (Circle) time:
        touchCircleAction = new RunnableAction();
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

        // Lanzamiento (2)
        moveAction = new MoveToAction();
        moveAction.setPosition(223, 72);
        moveAction.setDuration(0.4f);
        moveAction.setInterpolation(Interpolation.exp5);
        sequenceAction.addAction(moveAction);
        //sequenceAction.addAction(new DelayAction(0.6f));

        // Pausa
        sequenceAction.addAction(new DelayAction(0.2f));

        // Se mueve al grupo
        moveAction = new MoveToAction();
        moveAction.setPosition(162, 72);
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
            touchCircleAction= new RunnableAction();
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
                        teleportBoxes();
                    }
                });
            }
            sequenceAction.addAction(touchCircleAction);
            scaleAction = new ScaleToAction();
            scaleAction.setScale(1.0f);
            scaleAction.setDuration(0.1f);
            sequenceAction.addAction(scaleAction);
        }

        // Pausa
        sequenceAction.addAction(new DelayAction(0.1f));

        // Se mueve fuera del grupo y desaparece
        moveAction = new MoveToAction();
        moveAction.setPosition(190, 40);
        moveAction.setDuration(0.4f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.f);
        alphaAction.setDuration(0.2f);
        subSequenceAction = new SequenceAction();
        subSequenceAction.addAction(new DelayAction(0.2f));
        subSequenceAction.addAction(alphaAction);
        parallelAction = new ParallelAction( moveAction, subSequenceAction);
        sequenceAction.addAction(parallelAction);

        hand.addAction(sequenceAction);
    }

    private void beginBoxes(){
        SequenceAction sequenceAction;
        MoveToAction moveToAction;
        AlphaAction alphaAction;

        for(int i = 0; i < 3; i++) {
            // Inicio loop
            box[i].setColor(1, 1, 1, 0);
            box[i].setScale(1, 1);
            if(i == 1 || i == 2)
                box[i].setPosition(210 + ((i - 1) * 32), 40);
            else
                box[i].setPosition(82, 40);

            sequenceAction = new SequenceAction();

            // Aparicion
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.3f);
            sequenceAction.addAction(alphaAction);

            // Movimiento de las dos cajas de la izquierda
            if(i == 1 || i == 2) {
                moveToAction = new MoveToAction();
                moveToAction.setPosition(82 + (i * 32), box[i].getY());
                moveToAction.setDuration(0.25f);
                RunnableAction moveAndHitRunnable = new RunnableAction();
                final int finalI = i;
                moveAndHitRunnable.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
                        if(finalI == 2){
                            showGroupCreation();
                            AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_GROUP_CREATION);
                        }
                    }
                });
                if(i == 1) sequenceAction.addAction(new DelayAction(1.65f));
                else sequenceAction.addAction(new DelayAction(3.25f));
                sequenceAction.addAction(moveToAction);
                sequenceAction.addAction(moveAndHitRunnable);
            }

            box[i].addAction(sequenceAction);
        }
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
    private void teleportBoxes(){
        SequenceAction sequenceAction;
        MoveToAction moveToAction;
        AlphaAction alphaAction;
        ParallelAction parallelAction;
        ScaleToAction scaleToAction;

        for(int i = 0; i < 3; i++) {
            sequenceAction = new SequenceAction();
            // Espera hasta la teletrasnportacion
            //sequenceAction.addAction(new DelayAction(1.6f + (i * 0.2f)));
            sequenceAction.addAction(new DelayAction(0.4f - (i * 0.2f)));

            // Effecto de particulas
            RunnableAction runnableAction = new RunnableAction();
            if (i == 0) {
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
                        transportEffect[0] = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.TELEPORT, box[0].getX() + 16, box[0].getY() + 26, 0));
                        sampleGroup.addActor(transportEffect[0]);
                    }
                });
            } else if (i == 1) {
                runnableAction.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
                        transportEffect[1] = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.TELEPORT, box[1].getX() + 16, box[1].getY() + 26, 0));
                        sampleGroup.addActor(transportEffect[1]);
                        activePower();
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

    private void activePower(){
        for(int i = 0; i < 3; i++) {
            overbasePowerImage[i].setDrawable(new SpriteDrawable(new Sprite(boxActivableAtlas.findRegion("skill_gen_c"))));
            MoveToAction moveToAction = new MoveToAction();
            moveToAction.setPosition(overbasePowerImage[i].getX(), 80);
            moveToAction.setDuration(1.0f);
            ScaleToAction scaleAction = new ScaleToAction();
            scaleAction.setScale(1.6f);
            scaleAction.setDuration(1.0f);
            AlphaAction alphaAction = new AlphaAction();
            alphaAction.setAlpha(0);
            alphaAction.setDuration(0.7f);

            SequenceAction subSequenceAction = new SequenceAction();
            subSequenceAction.addAction(new DelayAction(0.3f));
            subSequenceAction.addAction(alphaAction);

            ParallelAction parallelAction = new ParallelAction(moveToAction, scaleAction, subSequenceAction);
            SequenceAction sequenceAction = new SequenceAction();

            RunnableAction runnableAction = new RunnableAction();
            final int finalI = i;
            runnableAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    if(finalI == 0) AudioManager.getInstance().playSound(AudioManager.SOUND.OVERBASE_TRIGGERED);
                }
            });

            sequenceAction.addAction(new DelayAction(0.3f));
            sequenceAction.addAction(runnableAction);
            sequenceAction.addAction(parallelAction);
            overbasePowerImage[i].addAction(sequenceAction);
        }
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
