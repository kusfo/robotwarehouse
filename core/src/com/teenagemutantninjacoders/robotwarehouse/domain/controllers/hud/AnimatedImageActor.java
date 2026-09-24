package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by JordiRM on 04/12/2016.
 */
public class AnimatedImageActor  extends Image {
    protected Animation<TextureRegion> animation = null;
    private float stateTime = 0;
    private Animation.PlayMode playMode = Animation.PlayMode.LOOP;
    private boolean playing = false;
    private TextureAtlas atlas;
    private float velocity;
    private AnimationSequence nextSequence = null;
    private boolean alwaysAnimate = false;

    private class AnimationSequence {
        public String regionName;
        public float velocity;
        public Animation.PlayMode playMode;
        public AnimationSequence(String regionName, float velocity, Animation.PlayMode playMode){
            this.regionName = regionName;
            this.velocity = velocity;
            this.playMode = playMode;
        }
    }

    public AnimatedImageActor(TextureAtlas atlas, float velocity, Animation.PlayMode playMode) {
        super(atlas.getRegions().get(0));
        this.atlas = atlas;
        this.velocity = velocity;
        animation = new Animation<TextureRegion>(velocity, atlas.getRegions(), playMode);
        this.playMode = playMode;
        setVisible(false);
    }

    public AnimatedImageActor(TextureAtlas atlas, String regionName, float velocity, Animation.PlayMode playMode) {
        super(atlas.findRegions(regionName).get(0));
        this.atlas = atlas;
        this.velocity = velocity;
        animation = new Animation<TextureRegion>(velocity, atlas.findRegions(regionName), playMode);
        this.playMode = playMode;
        setVisible(false);
    }

    public void show(boolean show){
        setVisible(show);
    }

    public void play(){
        playing = true;
        setVisible(true);
        stateTime = 0;
    }

    public void stop(boolean show){
        playing = false;
        if(!show) setVisible(false);
    }

    public void restart(boolean play){
        stateTime = 0;
        playing = play;
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime, true));
    }

    public void changeAnimation(String regionName){
       animation = new Animation<TextureRegion>(velocity, atlas.findRegions(regionName), playMode);
       stateTime = 0;
    }

    public void setPlayMode(Animation.PlayMode playMode){
        this.playMode = playMode;
    }

    public void setVelocity(float velocity){
        this.velocity = velocity;
        animation.setFrameDuration(velocity);
    }

    /** Ordenamos que ejecute otra animacion del mismo atlas cuando acabe la actual.
     *  La secuencia no se ejecutará nunca si la animación actual esta en loop. */
    public void addSequence(String regionName, float velocity, Animation.PlayMode playMode){
        nextSequence = new AnimationSequence(regionName, velocity, playMode);
    }

    @Override
    public void act(float delta)
    {
        stateTime += delta;
        if(playMode == Animation.PlayMode.LOOP || !animation.isAnimationFinished(stateTime)){
            if(playing) ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime, true));
        }else{
            if(nextSequence != null){
                changeAnimation(nextSequence.regionName);
                setPlayMode(nextSequence.playMode);
                setVelocity(nextSequence.velocity);
                nextSequence = null;
            }
            else if(playing && !alwaysAnimate) stop(false);
        }
        super.act(delta);
    }

    public void setAlwaysAnimate(boolean alwaysAnimate) {
        this.alwaysAnimate = alwaysAnimate;
    }
}
