package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
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
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 05/11/2016.
 */
public class GUIobjectGameOver {
    private GameEventsManager gameEventsManager;
    private Group layer;
    private Group panelLayer;
    private Label.LabelStyle labelStyle_Big;
    private TextureAtlas atlas;

    public GUIobjectGameOver(GameEventsManager gameEventsManager, Group layer, Group panelLayer){
        this.layer = layer;
        this.panelLayer = panelLayer;
        this.gameEventsManager = gameEventsManager;
        labelStyle_Big = new Label.LabelStyle();
        labelStyle_Big.font = Assets.getFont("f_cartel_partida_24");
        atlas = Assets.getTextureAtlas("level_screen_elements");
        execute();
    }

    private void execute() {
        AudioManager.getInstance().playSound(AudioManager.SOUND.LEVEL_FAILED);
        float centerX = GameConstants.GAMEZONE_X_CENTER; //360;
        float centerY = 200;

        String textRef = "";
        if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.TIME_OUT)
            textRef = GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_time");
        else if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.WITHOUT_BOXES)
            textRef = GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_fail_no_stock");

        Label labelGameOver = new Label(textRef, labelStyle_Big);
        labelGameOver.setColor(250 / 255f, 168 / 255f, 72 / 255f, 1);
        // FRANJA CON GRADIENTE
        Image imageDeg = new Image(atlas.findRegion("grad_textGameOver"));
        imageDeg.setPosition(0, centerY - 40);
        imageDeg.setWidth(GameConstants.HORIZONTAL_RESOLUTION);
        imageDeg.setAlign(Align.center);
        imageDeg.setOriginY(40);
        imageDeg.setColor(1,1,1,0);

        AlphaAction alphaAction1, alphaAction2;
        MoveToAction moveToAction1, moveToAction2;
        DelayAction delayAction;
        ParallelAction parallelAction1;

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
        gradientSecuence.addAction( new DelayAction(1.0f));
        gradientSecuence.addAction(alphaAction2);
        gradientSecuence.addAction(new RemoveActorAction());
        imageDeg.addAction(gradientSecuence);
        layer.addActor(imageDeg);


        // IMAGEN CENTRAL
        SequenceAction centerSecuence = new SequenceAction();
        Image imageCentral = new Image(atlas.findRegion("gameOver_textFrame_a"));
        imageCentral.setPosition(centerX - (imageCentral.getWidth() / 2), centerY - (imageCentral.getHeight() / 2));
        imageCentral.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.2f);
        centerSecuence.addAction(alphaAction1);

        // Pausa
        centerSecuence.addAction( new DelayAction(1.5f));


        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.2f);

        centerSecuence.addAction(alphaAction2);
        centerSecuence.addAction(new RemoveActorAction());

        imageCentral.addAction(centerSecuence);
        layer.addActor(imageCentral);


        // IMAGEN IZQUIERDA
        SequenceAction leftSecuence = new SequenceAction();
        Image imageLeft = new Image(atlas.findRegion("gameOver_textFrame_b"));
        imageLeft.setPosition(centerX - 450, centerY - 25);
        imageLeft.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.2f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(centerX - 150, centerY - 25);
        moveToAction1.setDuration(0.2f);

        parallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        leftSecuence.addAction(parallelAction1);

        // Pausa
        leftSecuence.addAction( new DelayAction(1.5f));


        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.1f);

        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(centerX - 450, centerY - 25);
        moveToAction2.setDuration(0.9f);

        ParallelAction leftParallelAction2 = new ParallelAction(moveToAction2, alphaAction2);
        leftSecuence.addAction(leftParallelAction2);
        leftSecuence.addAction(new RemoveActorAction());

        imageLeft.addAction(leftSecuence);
        layer.addActor(imageLeft);


        // IMAGEN DERECHA
        SequenceAction rightSecuence = new SequenceAction();
        Image imageRight = new Image(atlas.findRegion("gameOver_textFrame_b"));
        imageRight.setPosition(centerX + 450 - 97, centerY - 25);
        imageRight.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.2f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(centerX + 150 - 97, centerY - 25);
        moveToAction1.setDuration(0.2f);

        parallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        rightSecuence.addAction(parallelAction1);

        // Pausa
        rightSecuence.addAction( new DelayAction(1.5f));

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.1f);

        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(centerX + 450 - 97, centerY - 25);
        moveToAction2.setDuration(0.9f);

        ParallelAction rightParallelAction2 = new ParallelAction(moveToAction2, alphaAction2);
        rightSecuence.addAction(rightParallelAction2);
        rightSecuence.addAction(new RemoveActorAction());

        imageRight.addAction(rightSecuence);
        layer.addActor(imageRight);



        // TEXTO
        Container labelContainer = new Container(labelGameOver);
        labelContainer.setPosition(centerX, centerY);
        labelContainer.setTransform(true);
        labelContainer.setScale(6, 4);
        labelContainer.setColor(220 / 255f, 150 / 255f, 40 / 255f, 0);

        SequenceAction textSecuence = new SequenceAction();

        // secuencia 1
        textSecuence.addAction(new DelayAction(0.25f));

        ScaleToAction scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1, 1);
        scaleAction1.setDuration(1.0f);
        float bounceWidth[] = {0.5f, 0.2f};
        float bounceHeight[] = {0.2f, 0.05f};
        Interpolation interpolation = new Interpolation.BounceOut(bounceWidth, bounceHeight);
        scaleAction1.setInterpolation(interpolation);
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.15f);
        ParallelAction textParallelAction = new ParallelAction(scaleAction1,alphaAction1);
        textSecuence.addAction(textParallelAction);

        // Pausa
        textSecuence.addAction(new DelayAction(0.5f));

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.15f);
        scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(4,0.5f);
        scaleAction1.setDuration(0.2f);
        ParallelAction textParallelAction2 = new ParallelAction(scaleAction1,alphaAction2);
        textSecuence.addAction(textParallelAction2);
        RunnableAction showPanel = new RunnableAction();
        showPanel.setRunnable(new Runnable() {
            @Override
            public void run() {
                gameEventsManager.processLevelEnding();
            }
        });
        textSecuence.addAction(showPanel);
        textSecuence.addAction(new RemoveActorAction());

        labelContainer.addAction(textSecuence);
        layer.addActor(labelContainer);



    }
}
