package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 29/07/2016.
 */
public class GUIobjectBoxDisabled extends GUIobject{
    private float x;
    private float y;
    private Group layer;
    private BoardObjectModel box;
    private Image crossTexture;

    public GUIobjectBoxDisabled(BoardObjectModel box, Group layer) {
        this.box = box;
        x = box.getPositionX();
        y = box.getPositionY();
        this.layer = layer;
        execute();
    }

    @Override
    public void update(float delta){
         if(box.getStatus() != BoardObjectModel.BOARD_OBJECT_STATUS.ERASED){
            crossTexture.setPosition(box.getPositionX() + 3, box.getPositionY() + 13);
         }
         else if(!getFinalized()){
             setFinalized(true);
             crossTexture.remove();
         }
    }

    private void execute() {
        crossTexture = new Image(Assets.getTextureAtlas("level_screen_elements").findRegion("boxDisabledCross"));
        crossTexture.setOrigin(Align.center);

        crossTexture.setPosition(x + 3, y + 13);
        crossTexture.setColor(255, 255, 255, 0);
        crossTexture.setScale(6.0f, 6.0f);

        ParallelAction parallelAction;
        SequenceAction sequenceAction;
        ScaleToAction scaleAction1;
        AlphaAction alphaAction1, alphaAction2;
        DelayAction delayAction;

        sequenceAction = new SequenceAction();

        // Secuencia de Acciones paralelas
        scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1.0f, 1.0f);
        scaleAction1.setDuration(0.4f);
        scaleAction1.setInterpolation(Interpolation.pow2In);

        alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1);
        alphaAction1.setDuration(0.12f);

        parallelAction = new ParallelAction(scaleAction1, alphaAction1);

        RunnableAction crossHit = new RunnableAction();
        crossHit.setRunnable(new Runnable() {
            @Override
            public void run() {
                ((BoxModel) box).setDisabledSprite();
            }
        });

        // Secuencia final
        sequenceAction.addAction(new DelayAction(0.35f));
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(crossHit);

        delayAction = new DelayAction();
        delayAction.setDuration(0.8f);

        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.8f);

        // Finalizar elemento
        RunnableAction endAction = new RunnableAction();
        endAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                setFinalized(true);
            }
        });

        sequenceAction.addAction(delayAction);
        sequenceAction.addAction(alphaAction2);
        sequenceAction.addAction(endAction);
        sequenceAction.addAction(new RemoveActorAction());

        // Ejecucion
        crossTexture.addAction(sequenceAction);

        layer.addActor(crossTexture);
    }
}
