package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 18/05/2018.
 */
public class GUIobjectChallengeComplete extends GUIobject{
    private Group baseGroup;
    private Label.LabelStyle labelStyle_bright_21, labelStyle_base_bb_16;
    private GameEventsManager eventsManager;
    private Group layer;
    private Group medalGroup, touchGroup;
    private Image textFrameImage, medalImage, starImage, rotatingLinesImage;
    private Label  frameLabel, descriptionLabel;
    private Color c_yellow;
    private float phase = 0;
    private float waitingTime = 4.0f;

    public GUIobjectChallengeComplete(Group layer, GameEventsManager eventsManager){
        this.layer = layer;
        this.eventsManager = eventsManager;
        labelStyle_bright_21 = new Label.LabelStyle();
        labelStyle_bright_21.font = Assets.getFont("f_cartel_bb_21");

        labelStyle_base_bb_16 = new Label.LabelStyle();
        labelStyle_base_bb_16.font = Assets.getFont("f_base_bb_14");

        c_yellow = new Color(247 / 255f, 244 / 255f, 176 / 255f, 1);

        execute();
    }

    private void execute() {
        Integer centerX = GameConstants.GAMEZONE_X_CENTER;

        TextureAtlas levelAtlas = Assets.getTextureAtlas("level_screen_elements");
        TextureAtlas generalAtlas = Assets.getTextureAtlas("general_screen_elements");
        textFrameImage = new Image(generalAtlas.findRegion("floatingTextFrame", 2));
        medalImage = new Image(levelAtlas.findRegion("icon_challengeObtention_medal"));
        starImage = new Image(levelAtlas.findRegion("icon_challengeObtention_star"));
        rotatingLinesImage = new Image(generalAtlas.findRegion("rotatingLines"));

        frameLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_completed"), labelStyle_bright_21);
        descriptionLabel = new Label(ChallengeManager.getInstance().getChallengeDescription(), labelStyle_base_bb_16);
        medalGroup = new Group();

        baseGroup = new Group();
        baseGroup.setPosition(centerX, 150);
        baseGroup.setTouchable(Touchable.disabled);

        medalImage.setPosition(-medalImage.getWidth() / 2, 0);
        medalImage.setOrigin(Align.center);
        medalImage.setScale(0.2f, 0.2f);
        medalImage.setColor(1, 1, 1, 0);

        starImage.setPosition(-starImage.getWidth() / 2, 77);
        starImage.setOrigin(Align.center);
        starImage.setScale(3f, 3f);
        starImage.setColor(1, 1, 1, 0);

        medalGroup.setPosition(0, 0);
        medalGroup.setOrigin(Align.center);
        medalGroup.setSize(medalImage.getWidth(), medalImage.getHeight());

        rotatingLinesImage.setPosition(-rotatingLinesImage.getWidth() / 2, (-rotatingLinesImage.getHeight() / 2) + 100);
        rotatingLinesImage.setColor(1, 1, 1, 0);
        rotatingLinesImage.setOrigin(Align.center);

        textFrameImage.setPosition(-textFrameImage.getWidth() / 2, -30);
        textFrameImage.setColor(1, 1, 1, 0);
        textFrameImage.setScale(1, 0);
        textFrameImage.setOrigin(Align.center);

        frameLabel.setPosition(-frameLabel.getWidth() / 2, textFrameImage.getY() + 12);
        frameLabel.setColor(c_yellow.r, c_yellow.g, c_yellow.b, 0);
        descriptionLabel.setWrap(true);
        descriptionLabel.setWidth(350);
        descriptionLabel.setAlignment(Align.top);
        descriptionLabel.setColor(1, 1, 1, 0);
        descriptionLabel.setPosition(-descriptionLabel.getWidth() / 2, textFrameImage.getY() - 22);


        baseGroup.addActor(rotatingLinesImage);
        baseGroup.addActor(textFrameImage);
        baseGroup.addActor(frameLabel);
        baseGroup.addActor(descriptionLabel);
        medalGroup.addActor(medalImage);
        medalGroup.addActor(starImage);
        baseGroup.addActor(medalGroup);

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
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;
        RotateByAction rotateByAction;

        // Lineas
        sequenceAction = new SequenceAction();
        rotateByAction = new RotateByAction();
        rotateByAction.setAmount(40);
        rotateByAction.setDuration(waitingTime + 3.0f); // 4.0f
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        sequenceAction.addAction(new DelayAction(0.6f));
        sequenceAction.addAction(alphaAction);
        rotatingLinesImage.addAction(new ParallelAction(rotateByAction, sequenceAction));


        // Medalla
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.1f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setInterpolation(Interpolation.exp5Out);
        scaleToAction.setDuration(0.25f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.25f));

        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.CHALLENGE_COMPLETED);
                AudioManager.getInstance().playMusic(AudioManager.MUSIC.LEVEL_COMPLETED_PANEL, true, true, 0.3f);
            }
        });
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new DelayAction(0.3f));
        sequenceAction.addAction(parallelAction);
        medalImage.addAction(sequenceAction);


        // Estrella
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.1f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setInterpolation(new Interpolation.BounceOut(3));

        scaleToAction.setDuration(0.6f);
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(new DelayAction(0.55f));
        sequenceAction.addAction(parallelAction);
        starImage.addAction(sequenceAction);


        // Grupo medalla (medalla + estrella)
        sequenceAction = new SequenceAction();
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Consideramos el cartel iniciado y dejamos pulsar en el
                phase = 1;
            }
        });
        sequenceAction.addAction(new DelayAction(1.5f));
        sequenceAction.addAction(runnableAction);
        medalGroup.addAction(sequenceAction);



        // Recuadro texto
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(0.4f));
        parallelAction = new ParallelAction(alphaAction, scaleToAction);
        sequenceAction.addAction(parallelAction);
        textFrameImage.addAction(sequenceAction);


        // Texto Frame
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(new DelayAction(0.6f));
        sequenceAction.addAction(alphaAction);
        frameLabel.addAction(sequenceAction);


        // Texto Description
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.7f);
        sequenceAction.addAction(new DelayAction(0.9f));
        sequenceAction.addAction(alphaAction);
        descriptionLabel.addAction(sequenceAction);
    }

    private void leave(){
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableAction;
        ScaleToAction scaleToAction;
        AlphaAction alphaAction;

        // Lineas
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.35f);
        sequenceAction.addAction(alphaAction);
        rotatingLinesImage.addAction(sequenceAction);


        // Grupo medalla (medalla + estrella)
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(alphaAction);
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                eventsManager.processLevelEnding();
                touchGroup.remove();
                setFinalized(true);
            }
        });
        sequenceAction.addAction(runnableAction);
        medalGroup.addAction(sequenceAction);


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


        // Texto Frame
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(alphaAction);
        frameLabel.addAction(sequenceAction);


        // Texto Description
        sequenceAction = new SequenceAction();
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction.addAction(alphaAction);
        descriptionLabel.addAction(sequenceAction);
    }
}
