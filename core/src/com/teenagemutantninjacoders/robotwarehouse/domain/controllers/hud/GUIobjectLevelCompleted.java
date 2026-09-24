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
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
/**
 * Created by JordiRM on 11/11/2016.
 */
public class GUIobjectLevelCompleted {
    private Group layer;
    private Group panelLayer;
    private GameEventsManager gameEventsManager;
    private TextureAtlas atlas;

    private Label.LabelStyle labelStyle_Big;
    public GUIobjectLevelCompleted(GameEventsManager gameEventsManager, Group layer, Group panelLayer){
        this.gameEventsManager = gameEventsManager;
        this.layer = layer;
        this.panelLayer = panelLayer;
        atlas = Assets.getTextureAtlas("level_screen_elements");

        labelStyle_Big = new Label.LabelStyle();
        labelStyle_Big.font = Assets.getFont("f_cartel_partida_24");
        execute();
    }

    private void execute() {
        AudioManager.getInstance().playSound(AudioManager.SOUND.LEVEL_COMPLETED);
        float centerX;
        float centerY = 200;
        float posElementX;

        Label labelCompletedL = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_success_accomplished_1"), labelStyle_Big);
        labelCompletedL.setColor(218/ 255f, 253/ 255f, 78/ 255f, 1);
        labelCompletedL.setAlignment(Align.right);
        Label labelCompletedR = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_success_accomplished_2"), labelStyle_Big);
        labelCompletedR.setColor(218/ 255f, 253/ 255f, 78/ 255f, 1);
        labelCompletedR.setAlignment(Align.left);

        float additionalWidth = 0;
        // Centramos el cartel entero en la zona de juego
        float allTextWidth = labelCompletedL.getWidth() + labelCompletedR.getWidth() + 6;
        float spaceX = 500 - allTextWidth;
        // Si cabe en la zona central, lo centramos en ella, si no, intentamos que quepa aunque se descentre
        if(spaceX >= 0) centerX = (GameConstants.GAMEZONE_X_CENTER - 250) + (spaceX / 2) + labelCompletedL.getWidth() + 3;
        else centerX = GameConstants.GAMEZONE_X_CENTER + spaceX - 25;

        // Establecemos una separación minima de los marcos por si el texto fuera muy corto
        if(allTextWidth < 280) additionalWidth = (280 - allTextWidth) / 2;

        AlphaAction alphaAction1, alphaAction2;
        MoveToAction moveToAction1, moveToAction2;
        ScaleToAction scaleToAction1;

        float bounceWidth[] = {0.5f, 0.2f, 0.1f};
        float bounceHeight[] = {0.2f, 0.05f, 0.025f};
        Interpolation textInterpolation = new Interpolation.BounceOut(bounceWidth, bounceHeight);

        // FRANJA CON GRADIENTE
        Image imageDeg = new Image(atlas.findRegion("grad_textNivel"));
        imageDeg.setPosition(0, centerY - 40);
        imageDeg.setWidth(GameConstants.HORIZONTAL_RESOLUTION);
        imageDeg.setAlign(Align.center);
        imageDeg.setOriginY(40);
        imageDeg.setColor(1,1,1,0);

        SequenceAction gradientSecuence = new SequenceAction();
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.4f);
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.4f);
        gradientSecuence.addAction(alphaAction1);
        gradientSecuence.addAction( new DelayAction(1.5f));
        gradientSecuence.addAction(alphaAction2);
        gradientSecuence.addAction(new RemoveActorAction());
        imageDeg.addAction(gradientSecuence);
        layer.addActor(imageDeg);


        // ESQUINA IZQUIERDA
        SequenceAction leftSecuence = new SequenceAction();
        Image cornerLeft= new Image(atlas.findRegion("start_textFrame_b"));
        posElementX = centerX - additionalWidth - labelCompletedL.getWidth() - 20;
        cornerLeft.setPosition(posElementX - 20, centerY + 30);
        cornerLeft.setColor(1,1,1,0);

        leftSecuence.addAction(new DelayAction(0.2f));

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
        leftSecuence.addAction(new DelayAction(1.5f)); //1.1

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

        cornerLeft.addAction(leftSecuence);
        layer.addActor(cornerLeft);

        // ESQUINA DERECHA
        SequenceAction rightSecuence = new SequenceAction();
        Image cornerRight= new Image(atlas.findRegion("start_textFrame_c"));
        posElementX = centerX + additionalWidth + labelCompletedR.getWidth() - 55;
        cornerRight.setPosition(posElementX + 20, centerY - 66);
        cornerRight.setColor(1,1,1,0);

        rightSecuence.addAction(new DelayAction(0.2f));

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
        rightSecuence.addAction(new DelayAction(1.5f)); // 1.1

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

        cornerRight.addAction(rightSecuence);
        layer.addActor(cornerRight);



        // FRANJA IZQUIERDA
        SequenceAction leftImageSecuence = new SequenceAction();
        Image imageLeft = new Image(atlas.findRegion("start_textFrame_d"));
        imageLeft.setPosition(cornerLeft.getX() + 150, centerY - (imageLeft.getHeight() / 2) + 100);
        imageLeft.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.5f);

        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(cornerLeft.getX() + 50, centerY - (imageLeft.getHeight() / 2));
        moveToAction1.setDuration(0.2f);

        ParallelAction leftParallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        leftImageSecuence.addAction(leftParallelAction1);

        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.2f);

        leftImageSecuence.addAction( new DelayAction(1.6f));//1.2
        leftImageSecuence.addAction(alphaAction2);
        leftImageSecuence.addAction(new RemoveActorAction());

        imageLeft.addAction(leftImageSecuence);
        layer.addActor(imageLeft);

        // FRANJA DERECHA
        SequenceAction rightImageSecuence = new SequenceAction();
        Image imageRight = new Image(atlas.findRegion("start_textFrame_d"));
        imageRight.setPosition(cornerRight.getX() - 214, centerY - (imageLeft.getHeight() / 2) - 100);
        imageRight.setColor(1,1,1,0);

        // secuencia 1
        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.5f);
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(cornerRight.getX() - 114, centerY - (imageRight.getHeight() / 2));
        moveToAction1.setDuration(0.2f);

        rightParallelAction1 = new ParallelAction(moveToAction1, alphaAction1);
        rightImageSecuence.addAction(rightParallelAction1);


        // secuencia 2
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.2f);

        rightImageSecuence.addAction( new DelayAction(1.6f));
        rightImageSecuence.addAction(alphaAction2);
        rightImageSecuence.addAction(new RemoveActorAction());

        imageRight.addAction(rightImageSecuence);
        layer.addActor(imageRight);




        // TEXTO IZQUIERDO
        Container labelContainerL = new Container(labelCompletedL);
        labelContainerL.setPosition(centerX - 500 - (labelCompletedL.getWidth() / 2), centerY);
        labelContainerL.setTransform(true);
        labelContainerL.setScale(4, 0);
        labelContainerL.setOriginX(labelCompletedL.getWidth()/2);

        SequenceAction textSecuence1 = new SequenceAction();
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(centerX - (labelCompletedL.getWidth() / 2) - 3, centerY);
        moveToAction1.setDuration(1.5f);
        moveToAction1.setInterpolation(textInterpolation);
        scaleToAction1 = new ScaleToAction();
        scaleToAction1.setScale(1, 1);
        scaleToAction1.setDuration(0.3f);

        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(centerX - 500 - (labelCompletedL.getWidth() / 2), centerY);
        moveToAction2.setDuration(0.5f);
        moveToAction2.setInterpolation(Interpolation.pow3In);
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.3f);
        ParallelAction textParallelAction1 = new ParallelAction(moveToAction1,scaleToAction1);
        ParallelAction textParallelAction2 = new ParallelAction(moveToAction2,alphaAction2);

        textSecuence1.addAction(textParallelAction1);
        textSecuence1.addAction( new DelayAction(0.7f));
        textSecuence1.addAction(textParallelAction2);
        textSecuence1.addAction(new RemoveActorAction());

        labelContainerL.addAction(textSecuence1);
        layer.addActor(labelContainerL);


        // TEXTO DERECHO
        Container labelContainerR = new Container(labelCompletedR);
        labelContainerR.setPosition(centerX + 500 + (labelCompletedR.getWidth() / 2), centerY);
        labelContainerR.setTransform(true);
        labelContainerR.setScale(4, 0);
        labelContainerR.setOriginX(-(labelCompletedR.getWidth() / 2));

        SequenceAction textSecuence2 = new SequenceAction();
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(centerX + (labelCompletedR.getWidth() / 2) + 3, centerY);
        moveToAction1.setDuration(1.5f);
        moveToAction1.setInterpolation(textInterpolation);
        scaleToAction1 = new ScaleToAction();
        scaleToAction1.setScale(1, 1);
        scaleToAction1.setDuration(0.3f);
        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(centerX + 500 + (labelCompletedR.getWidth() / 2), centerY);
        moveToAction2.setDuration(0.5f);
        moveToAction2.setInterpolation(Interpolation.pow3In);
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.3f);
        textParallelAction1 = new ParallelAction(moveToAction1,scaleToAction1);
        textParallelAction2 = new ParallelAction(moveToAction2,alphaAction2);

        textSecuence2.addAction(textParallelAction1);
        textSecuence2.addAction( new DelayAction(0.7f));//0.3

        RunnableAction showPanel = new RunnableAction();
        showPanel.setRunnable(new Runnable() {
            @Override
            public void run() {
                gameEventsManager.processLevelEnding();
            }
        });
        textSecuence2.addAction(showPanel);
        textSecuence2.addAction(textParallelAction2);
        textSecuence2.addAction(new RemoveActorAction());

        labelContainerR.addAction(textSecuence2);
        layer.addActor(labelContainerR);

    }
}
