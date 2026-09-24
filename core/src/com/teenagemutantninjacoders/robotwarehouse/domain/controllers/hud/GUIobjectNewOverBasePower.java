package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiMS on 21/07/2017.
 */
public class GUIobjectNewOverBasePower extends GUIobject{
    private Group baseGroup, touchGroup;
    private Label.LabelStyle labelStyle_bright_21;
    private GameEventsManager eventsManager;
    private Group layer;
    private GlobalAttributes.POWER power;
    private AnimatedImageActor sparkLeftAnimation, sparkRightAnimation, cogAnimation;
    private Image gradientImage, textFrameImage,powerIconImage;
    private Label textLabel;
    private Color c_yellow;
    private float phase = 0;
    private float waitingTime = 1.8f;

    public GUIobjectNewOverBasePower(float x, float y, GlobalAttributes.POWER power, Group layer, GameEventsManager eventsManager){
        this.layer = layer;
        this.eventsManager = eventsManager;
        this.power = power;
        labelStyle_bright_21 = new Label.LabelStyle();
        labelStyle_bright_21.font = Assets.getFont("f_cartel_bb_21");
        c_yellow = new Color(247 / 255f, 244 / 255f, 176 / 255f, 1);
        execute();
    }

    private void execute() {
        AudioManager.getInstance().playSound(AudioManager.SOUND.POWER_ACTIVATION);
        Integer centerX = GameConstants.GAMEZONE_X_CENTER;
        TextureAtlas levelAtlas = Assets.getTextureAtlas("level_screen_elements");

        baseGroup = new Group();
        baseGroup.setPosition(centerX, 150);
        baseGroup.setTouchable(Touchable.disabled);

        gradientImage = new Image(levelAtlas.findRegion("powerDisplayGradient"));
        cogAnimation = new AnimatedImageActor(levelAtlas, "powerCog", 0.08f, Animation.PlayMode.LOOP);
        sparkLeftAnimation = new AnimatedImageActor(Assets.getTextureAtlas("fx_powerSparkDisplay"), "begin", 0.07f, Animation.PlayMode.NORMAL);
        sparkRightAnimation = new AnimatedImageActor(Assets.getTextureAtlas("fx_powerSparkDisplay"), "begin", 0.07f, Animation.PlayMode.NORMAL);
        textFrameImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("floatingTextFrame", 1));

        // Decidimos el icono y el texto dependiendo del poder desatado
        switch (power) {
            case POWER_DISABLE_ROBOTS:
                powerIconImage = new Image(levelAtlas.findRegion("icon_powerShortCircuit"));
                textLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("power_disabled_robots"), labelStyle_bright_21);
                break;
            case POWER_DISABLE_CANON:
                powerIconImage = new Image(levelAtlas.findRegion("icon_powerShortCircuit"));
                textLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("power_disabled_cannons"), labelStyle_bright_21);
                break;
            case POWER_FUMIGATION:
                powerIconImage = new Image(levelAtlas.findRegion("icon_powerFumigation"));
                textLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("power_fumigation"), labelStyle_bright_21);
                break;
            default:
                powerIconImage = new Image(levelAtlas.findRegion("icon_powerShortCircuit"));
                textLabel = new Label("None", labelStyle_bright_21);
                break;
        }

        gradientImage.setPosition(-gradientImage.getWidth() / 2, 10);
        gradientImage.setColor(1, 1, 1, 0f);
        gradientImage.setOrigin(Align.center);
        gradientImage.setScale(0.7f, 0);

        sparkLeftAnimation.setPosition(-388, (-sparkLeftAnimation.getHeight() / 2) + 62);
        sparkLeftAnimation.setOriginY(Align.center);
        sparkLeftAnimation.addSequence("loop", 0.1f, Animation.PlayMode.LOOP);

        sparkRightAnimation.setPosition(388, (-sparkRightAnimation.getHeight() / 2) + 62);
        sparkRightAnimation.setOriginY(Align.center);
        sparkRightAnimation.setScale(-1f, 1f);
        sparkRightAnimation.addSequence("loop", 0.1f, Animation.PlayMode.LOOP);

        cogAnimation.setPosition(-cogAnimation.getWidth() / 2, 6);
        cogAnimation.setColor(1, 1, 1, 0);
        cogAnimation.setScale(0.5f, 0.5f);
        cogAnimation.setOrigin(Align.center);
        cogAnimation.play();

        powerIconImage.setPosition(cogAnimation.getX() + 6, cogAnimation.getY() + 6);
        powerIconImage.setOrigin(Align.center);
        powerIconImage.setColor(1, 1, 1, 0);
        powerIconImage.setScale(2.5f, 2.5f);

        textFrameImage.setPosition(-textFrameImage.getWidth() / 2, -60);
        textFrameImage.setColor(1, 1, 1, 0);
        textFrameImage.setScale(1, 0);
        textFrameImage.setOrigin(Align.center);

        textLabel.setPosition(-textLabel.getWidth() / 2, textFrameImage.getY() + 12);
        textLabel.setColor(c_yellow.r, c_yellow.g, c_yellow.b, 0);

        baseGroup.addActor(gradientImage);
        baseGroup.addActor(sparkLeftAnimation);
        baseGroup.addActor(sparkRightAnimation);
        baseGroup.addActor(textFrameImage);
        baseGroup.addActor(textLabel);
        baseGroup.addActor(cogAnimation);
        baseGroup.addActor(powerIconImage);

        layer.addActor(baseGroup);

        touchGroup = new Group();
        touchGroup.setSize(GameConstants.HORIZONTAL_RESOLUTION, 300);
        touchGroup.setPosition(0, baseGroup.getY() - 150);
        touchGroup.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (phase == 1) {
                    waitingTime = 0;
                }
                return true;
            }
        });
        layer.addActor(touchGroup);

        enter();
    }

    @Override
    public void update(float delta){
        if(phase == 1) {
            if (waitingTime > 0) {
                waitingTime -= (delta * 1);
            } else {
                phase = 2;
                leave();
            }
        }
    }

    private void enter(){
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableAction1, runnableAction2;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;
        Interpolation interpolation;

        // Base (Rayos)
        sequenceAction = new SequenceAction();
        runnableAction1 = new RunnableAction();
        runnableAction1.setRunnable(new Runnable() {
            @Override
            public void run() {
                sparkLeftAnimation.play();
                sparkRightAnimation.play();

            }
        });
        runnableAction2 = new RunnableAction();
        runnableAction2.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Consideramos el cartel iniciado y dejamos pulsar en el
                phase = 1;
            }
        });
        sequenceAction.addAction(new DelayAction(0.15f));
        sequenceAction.addAction(runnableAction1);
        sequenceAction.addAction(new DelayAction(0.70f));
        sequenceAction.addAction(runnableAction2);
        baseGroup.addAction(sequenceAction);


        // Gradiente
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.2f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1.5f);
        scaleToAction.setDuration(0.2f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.15f));
        sequenceAction.addAction(parallelAction);
        gradientImage.addAction(sequenceAction);

        // Engranaje
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.2f);
        scaleToAction.setInterpolation(Interpolation.circleOut);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        cogAnimation.addAction(parallelAction);


        // Icono Poder
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.2f);
        interpolation = new Interpolation.BounceOut(2);
        scaleToAction.setInterpolation(interpolation);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.15f));
        sequenceAction.addAction(parallelAction);
        powerIconImage.addAction(sequenceAction);


        // Recuadro texto
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.2f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        textFrameImage.addAction(parallelAction);


        // Texto
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(0.2f));
        textLabel.addAction(alphaAction);
    }

    private void leave(){
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;

        // Base (Rayos)
        sparkLeftAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        sparkLeftAnimation.changeAnimation("ending");
        sparkRightAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        sparkRightAnimation.changeAnimation("ending");

        sequenceAction = new SequenceAction();
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                eventsManager.finalizePowerActivationEvent(power);
                touchGroup.remove();
                setFinalized(true);
            }
        });
        sequenceAction.addAction(new DelayAction(0.4f));
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new RemoveActorAction());
        baseGroup.addAction(sequenceAction);



        // Gradiente
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0.7f, 0);
        scaleToAction.setDuration(0.2f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.2f));
        sequenceAction.addAction(parallelAction);
        gradientImage.addAction(sequenceAction);


        // Engranaje
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(alphaAction);
        cogAnimation.addAction(sequenceAction);


        // Icono Poder
        sequenceAction = new SequenceAction();
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(3, 3);
        scaleToAction.setDuration(0.2f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(parallelAction);
        powerIconImage.addAction(sequenceAction);


        // Recuadro texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 0);
        scaleToAction.setDuration(0.2f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.2f));
        sequenceAction.addAction(parallelAction);
        textFrameImage.addAction(sequenceAction);


        // Texto
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        textLabel.addAction(alphaAction);
    }
}