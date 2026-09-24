package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 05/03/2018.
 */
public class GUIobjectFloatingLostBox {
    private float x;
    private float y;
    private Group layer;
    private Label.LabelStyle labelStyle_base_gb_18;
    public GUIobjectFloatingLostBox(Group layer, float x, float y){
        this.x = x;
        this.y = y;
        this.layer = layer;
        labelStyle_base_gb_18 = new Label.LabelStyle();
        labelStyle_base_gb_18.font = Assets.getFont("f_numbers_gb_18");
        execute();
    }

    private void execute() {
        Label labellostBox = new Label("-1", labelStyle_base_gb_18);
        // Limitamos la posicion del cartel para que no se salga de la zona de juego
        if(x < GameConstants.BOARD_ORIGIN_X + 20)
            x = GameConstants.BOARD_ORIGIN_X + 20;
        else if(x > GameConstants.HORIZONTAL_RESOLUTION)
            x = GameConstants.HORIZONTAL_RESOLUTION - 20;
        if(y > 280) y = 280;

        labellostBox.setColor(255 / 255f, 65 / 255f, 65 / 255f, 0.85f);
        Container<Label> container = new Container<Label>(labellostBox);
        container.setPosition(x - (labellostBox.getWidth() / 2) , y + 20);//50
        container.setTransform(true);
        container.setOrigin(0, -labellostBox.getHeight() / 4);
        container.setColor(1,1,1,0);
        container.setScale(0.6f, 0.6f);

        // Acciones
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        MoveToAction moveToAction;
        ScaleToAction scaleAction;
        AlphaAction alphaActionOn, alphaActionOff;

        moveToAction = new MoveToAction();
        moveToAction.setPosition(container.getX(), y + 30);
        moveToAction.setDuration(1.0f);
        alphaActionOn = new AlphaAction();
        alphaActionOn.setAlpha(1);
        alphaActionOn.setDuration(0.4f);
        alphaActionOff = new AlphaAction();
        alphaActionOff.setAlpha(0);
        alphaActionOff.setDuration(0.3f);
        scaleAction = new ScaleToAction();
        scaleAction.setScale(1.0f);
        scaleAction.setDuration(0.3f);
        Interpolation interpolation = new Interpolation.ElasticOut(1.0f, 0.6f, 1, 0.7f);
        scaleAction.setInterpolation(interpolation);

        sequenceAction = new SequenceAction();
        sequenceAction.addAction(alphaActionOn);
        sequenceAction.addAction(new DelayAction(0.3f));
        sequenceAction.addAction(alphaActionOff);
        sequenceAction.addAction(new RemoveActorAction());
        parallelAction = new ParallelAction(moveToAction, scaleAction, sequenceAction);

        SequenceAction baseSequenceAction = new SequenceAction();
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_LOST);
            }
        });
        baseSequenceAction.addAction(new DelayAction(0.2f));
        baseSequenceAction.addAction(runnableAction);
        baseSequenceAction.addAction(parallelAction);
        container.addAction(baseSequenceAction);

        layer.addActor(container);
    }
}
