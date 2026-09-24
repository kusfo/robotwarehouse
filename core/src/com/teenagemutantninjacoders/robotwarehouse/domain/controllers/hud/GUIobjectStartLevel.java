package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 31/10/2016.
 */
public class GUIobjectStartLevel {
    private Group layer;
    private TextureAtlas atlas;

    private Label.LabelStyle labelStyle_Big;

    public GUIobjectStartLevel( Group layer){
        this.layer = layer;
        labelStyle_Big = new Label.LabelStyle();
        labelStyle_Big.font = Assets.getFont("f_cartel_partida_24");
        atlas = Assets.getTextureAtlas("level_screen_elements");
        execute();
    }

    private void execute() {
        AudioManager.getInstance().playSound(AudioManager.SOUND.READY);
        float centerX = GameConstants.GAMEZONE_X_CENTER;//360;
        float centerY = 200;
        float posElementX;

        Label labelMission = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("start_level_go"), labelStyle_Big);
        labelMission.setColor(235 / 255f, 230 / 255f, 82 / 255f, 1);

        AlphaAction alphaAction1, alphaAction2;
        MoveToAction moveToAction1, moveToAction2;
        DelayAction delayAction;


        // FRANJA CON GRADIENTE
        Image imageDeg = new Image(atlas.findRegion("grad_textNivel"));
        imageDeg.setPosition(0, centerY - 40);
        imageDeg.setWidth(GameConstants.HORIZONTAL_RESOLUTION);
        imageDeg.setAlign(Align.center);
        imageDeg.setOriginY(40);
        imageDeg.setColor(1,1,1,0);

        SequenceAction gradientSecuence = new SequenceAction();
        delayAction = new DelayAction(0.6f);
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.4f);
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.4f);
        gradientSecuence.addAction(alphaAction1);
        gradientSecuence.addAction(delayAction);
        gradientSecuence.addAction(alphaAction2);
        gradientSecuence.addAction(new RemoveActorAction());
        imageDeg.addAction(gradientSecuence);
        layer.addActor(imageDeg);


        // IMAGEN CENTRAL
        SequenceAction centerSecuence = new SequenceAction();
        Image imageCentral = new Image(atlas.findRegion("start_textFrame_a"));
        imageCentral.setPosition(centerX - (imageCentral.getWidth() / 2) + 100, centerY - (imageCentral.getHeight() / 2) + 100);
        imageCentral.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.5f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(centerX - (imageCentral.getWidth() / 2), centerY - (imageCentral.getHeight() / 2));
        moveToAction1.setDuration(0.2f);

        ParallelAction centerParallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        centerSecuence.addAction(centerParallelAction1);

        // Pausa
        delayAction = new DelayAction();
        delayAction.setDuration(0.6f);
        centerSecuence.addAction(delayAction);

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.2f);

        centerSecuence.addAction(alphaAction2);
        centerSecuence.addAction(new RemoveActorAction());

        imageCentral.addAction(centerSecuence);
        layer.addActor(imageCentral);


        // TEXTO
        Container labelContainer = new Container(labelMission);
        labelContainer.setPosition(centerX, centerY);
        labelContainer.setTransform(true);
        labelContainer.setScale(5, 0);

        SequenceAction textSecuence = new SequenceAction();


        ScaleToAction scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1, 1);
        scaleAction1.setDuration(0.2f);
        textSecuence.addAction(scaleAction1);

        // Pausa
        delayAction = new DelayAction();
        delayAction.setDuration(0.9f);
        textSecuence.addAction(delayAction);

        ScaleToAction scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(4, 2);
        scaleAction2.setDuration(0.2f);
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(0);
        alphaAction1.setDuration(0.15f);
        ParallelAction textParallelAction = new ParallelAction(scaleAction2,alphaAction1);
        RunnableAction startAction = new RunnableAction();
        startAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PLAYING);
            }
        });

        textSecuence.addAction(startAction);
        textSecuence.addAction(textParallelAction);
        textSecuence.addAction(new RemoveActorAction());

        labelContainer.addAction(textSecuence);
        layer.addActor(labelContainer);


        // IMAGEN IZQUIERDA
        SequenceAction leftSecuence = new SequenceAction();
        Image imageLeft= new Image(atlas.findRegion("start_textFrame_b"));
        posElementX = (centerX - MathUtils.clamp((labelMission.getWidth() / 2), 90, 200)) - 20;
        imageLeft.setPosition(posElementX - 20, centerY + 30);
        imageLeft.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.5f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(posElementX, centerY);
        moveToAction1.setDuration(0.2f);

        ParallelAction parallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        leftSecuence.addAction(parallelAction1);

        // Pausa
        delayAction = new DelayAction();
        delayAction.setDuration(0.6f);
        leftSecuence.addAction(delayAction);

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.12f);

        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(posElementX - 20, centerY + 30);
        moveToAction2.setDuration(0.2f);

        ParallelAction leftParallelAction2 = new ParallelAction(moveToAction2, alphaAction2);
        leftSecuence.addAction(leftParallelAction2);
        leftSecuence.addAction(new RemoveActorAction());

        imageLeft.addAction(leftSecuence);
        layer.addActor(imageLeft);


        // IMAGEN DERECHA
        SequenceAction rightSecuence = new SequenceAction();
        Image imageRight = new Image(atlas.findRegion("start_textFrame_c"));
        posElementX =  (centerX + MathUtils.clamp((labelMission.getWidth() / 2), 90, 200)) - 55;
        imageRight.setPosition(posElementX + 20, centerY - 66);
        imageRight.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.5f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(posElementX, centerY - 36);
        moveToAction1.setDuration(0.2f);

        ParallelAction rightParallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        rightSecuence.addAction(rightParallelAction1);

        // Pausa
        delayAction = new DelayAction();
        delayAction.setDuration(0.6f);
        rightSecuence.addAction(delayAction);

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.12f);

        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(posElementX + 20, centerY - 66);
        moveToAction2.setDuration(0.2f);

        ParallelAction rightParallelAction2 = new ParallelAction(moveToAction2, alphaAction2);
        rightSecuence.addAction(rightParallelAction2);
        rightSecuence.addAction(new RemoveActorAction());

        imageRight.addAction(rightSecuence);
        layer.addActor(imageRight);
    }
}
