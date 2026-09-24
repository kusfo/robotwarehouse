package com.teenagemutantninjacoders.robotwarehouse.domain.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 13/10/2019.
 */
public class SpriteEffectRobotBubble extends SpriteEffectModel {
    private boolean finalized = false;
    private int phase = 1;
    private String simbolRef;
    private float timeShowing;
    public SpriteEffectRobotBubble(float x, float y, int depth, float timeShowing, ROBOT_BUBBLE_TYPE type){
        super(x, y, depth, Assets.getTextureAtlas("fx_robot_bubble"), false);
        this.timeShowing = timeShowing;
        setActualizeWhileIsNotPlaying(true);
        AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_BUBBLE);

        switch(type){
            case SURPRISE:
                if (MathUtils.random(0, 1) == 0) simbolRef = "exclamation";
                else simbolRef = "interrogation";
                break;
            case EVIL:
                simbolRef = "evil";
                break;
        }

        setEffectAnimation("fx_robot_" + simbolRef + "_in",0.03f, Animation.PlayMode.NORMAL);
    }

    @Override
    public void update(float delta) {
        if (!finalized) {
            if (phase == 1) {
                if(getAnimation().isAnimationFinished(getAnimationTime())) {
                    setEffectAnimation("fx_robot_" + simbolRef + "_loop", 0.5f * timeShowing, Animation.PlayMode.NORMAL);
                    phase = 2;
                }
            } else if(phase == 2){
                if(getAnimation().isAnimationFinished(getAnimationTime())) {
                    setEffectAnimation("fx_robot_" + simbolRef + "_out", 0.04f, Animation.PlayMode.NORMAL);
                    phase = 3;
                }
            } else {
                if(getAnimation().isAnimationFinished(getAnimationTime())) finalized = true;
            }
        }
    }

    @Override
    public boolean isFinished(){
        return finalized;
    }

    public enum ROBOT_BUBBLE_TYPE{
        SURPRISE, EVIL;
    }
}