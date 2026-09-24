package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;

/**
 * Created by JordiRM on 09/02/2021.
 */
public class GUIobjectTutorialArrow extends GUIobject{
    private float x;
    private float y;
    private final Group layer;
    private Image arrowTexture;
    private final float delay;
    private GUIobjectCharacterDialogue.DIALOGUE_EVENT event;

    public GUIobjectTutorialArrow(Group layer, float x, float y, GUIobjectCharacterDialogue.DIALOGUE_EVENT event, float delay){
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.delay = delay;
        this.event = event;
        execute();
    }

    private void execute() {
        arrowTexture = new Image(Assets.getTextureAtlas("level_screen_elements").findRegion("event_arrow"));
        arrowTexture.setOrigin(42, 68);
        arrowTexture.setColor(255, 255, 255, 0);
        layer.addActor(arrowTexture);

        showArrow();
    }

    private void showArrow(){
        int movementRange = 15;
        float posX_initial = 0, posX_final = 0, posY_initial = 0, posY_final = 0;

        // Iniciamos la posicion y damos valores segun la direccion
        switch(event){
            case SHOW_LEFT_ARROW:
                x -= 42;
                y -= 68;
                posX_initial = x + movementRange;
                posX_final = x;
                posY_initial = y;
                posY_final = y;
                arrowTexture.rotateBy(90);
                break;
            case SHOW_RIGHT_ARROW:
                x -= 42;
                y -= 68;
                posX_initial = x - movementRange;
                posX_final = x;
                posY_initial = y;
                posY_final = y;
                arrowTexture.rotateBy(270);
                break;
            case SHOW_UP_ARROW:
                y -= 68;
                x = x - (arrowTexture.getWidth() / 2);
                posX_initial = x;
                posX_final = x;
                posY_initial = y - movementRange;
                posY_final = y;
                break;
            case SHOW_DOWN_ARROW:
                y -= 68;
                x = x - (arrowTexture.getWidth() / 2);
                posX_initial = x;
                posX_final = x;
                posY_initial = y + movementRange;
                posY_final = y;
                arrowTexture.rotateBy(180);
                break;
        }

        arrowTexture.setPosition(posX_initial, posY_initial);

        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.4f);

        SequenceAction sequenceLoopAction = new SequenceAction();

        MoveToAction moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(posX_final, posY_final);
        moveToAction1.setDuration(0.4f);
        moveToAction1.setInterpolation(Interpolation.slowFast);

        ScaleToAction scaleToAction1 = new ScaleToAction();
        scaleToAction1.setScale(1, 0.8f);
        scaleToAction1.setDuration(0.2f);
        scaleToAction1.setInterpolation(Interpolation.fastSlow);
        ScaleToAction scaleToAction2 = new ScaleToAction();
        scaleToAction2.setScale(1, 1);
        scaleToAction2.setDuration(0.2f);
        scaleToAction2.setInterpolation(Interpolation.slowFast);

        MoveToAction moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(posX_initial, posY_initial);
        moveToAction2.setDuration(0.3f);
        moveToAction2.setInterpolation(Interpolation.fastSlow);

        sequenceLoopAction.addAction(moveToAction1);
        sequenceLoopAction.addAction(scaleToAction1);
        sequenceLoopAction.addAction(scaleToAction2);
        sequenceLoopAction.addAction(moveToAction2);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceLoopAction);

        ParallelAction parallelAction = new ParallelAction(alphaAction, repeatAction);
        SequenceAction sequenceGeneralAction = new SequenceAction();
        sequenceGeneralAction.addAction(new DelayAction(delay));
        sequenceGeneralAction.addAction(parallelAction);

        arrowTexture.addAction(sequenceGeneralAction);
    }

    public void removeArrow(){
        SequenceAction sequenceAction = new SequenceAction();
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.3f);
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        arrowTexture.addAction(sequenceAction);
    }
}