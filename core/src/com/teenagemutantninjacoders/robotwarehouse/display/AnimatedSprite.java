package com.teenagemutantninjacoders.robotwarehouse.display;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;

/**
 * Created by JordiRM on 18/11/2017.
 */
public class AnimatedSprite {
    private Sprite sprite;
    private float x, y;
    private Animation<TextureRegion> animation;
    private float animationTime;
    private float initialFrame;
    private TextureAtlas atlas;
    private Animation.PlayMode playMode;
    private float speed;
    private AnimationSequence nextSequence = null;
    private boolean alwaysAnimate = false;
    private class AnimationSequence {
        String regionName;
        float speed;
        Animation.PlayMode playMode;
        AnimationSequence(String regionName, float speed, Animation.PlayMode playMode){
            this.regionName = regionName;
            this.speed = speed;
            this.playMode = playMode;
        }
    }

    public AnimatedSprite(TextureAtlas atlas, float speed, Animation.PlayMode playMode){
        animation = new Animation(speed, atlas.getRegions());
        this.speed = speed;
        this.atlas = atlas;
        processAnimation(playMode);
    }
    public AnimatedSprite(TextureAtlas atlas, String regionName, float speed, Animation.PlayMode playMode) {
        animation = new Animation(speed, atlas.findRegions(regionName));
        this.speed = speed;
        this.atlas = atlas;
        processAnimation(playMode);
    }

    private void processAnimation(Animation.PlayMode playMode){
        this.playMode = playMode;
        animation.setPlayMode(playMode);
        switch(playMode){
            case NORMAL: case LOOP:
                initialFrame = 0;
                break;
            case REVERSED: case LOOP_REVERSED:
                initialFrame = animation.getKeyFrames().length - 1;
                break;
        }
        animationTime = 0;
        sprite = new Sprite(animation.getKeyFrame(initialFrame));
        sprite.setPosition(x, y);
    }

    public void changeAnimation(String regionName, float speed, Animation.PlayMode playMode ){
        animation = new Animation<TextureRegion>(speed, atlas.findRegions(regionName), playMode);
        this.speed = speed;
        processAnimation(playMode);
    }

    /** Ordenamos que ejecute otra animacion del mismo atlas cuando acabe la actual.
     *  La secuencia no se ejecutará nunca si la animación actual esta en loop. */
    public void addSequence(String regionName, float speed, Animation.PlayMode playMode){
        nextSequence = new AnimationSequence(regionName, speed, playMode);
    }

    public void update(float delta){
        if(alwaysAnimate || GlobalLevelData.getInstance().getLevelStatus() != GlobalLevelData.LEVEL_STATUS.PAUSED){
            animationTime += delta;
            if (playMode == Animation.PlayMode.LOOP || !animation.isAnimationFinished(animationTime)) {
                sprite.setRegion(animation.getKeyFrame(animationTime, false));
            } else {
                if (nextSequence != null) {
                    changeAnimation(nextSequence.regionName, nextSequence.speed, nextSequence.playMode);
                    nextSequence = null;
                }
            }
        }
    }

    public void draw(Batch batch, float delta){
        update(delta);
        sprite.draw(batch);
    }

    public void play(){
        animationTime = 0;
    }

    public void setPlayMode(Animation.PlayMode playMode){
        processAnimation(playMode);
    }

    public void setLastFrame (){
        animationTime = animation.getAnimationDuration();
    }

    public boolean isAnimationFinished(){
        return animation.isAnimationFinished(animationTime);
    }

    public Sprite getSprite(){
        return sprite;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        sprite.setPosition(x, y);
    }

    public void setPositionCentered(float x, float y){
        this.x = x - (sprite.getWidth() / 2);
        this.y = y - (sprite.getHeight() / 2);
        sprite.setPosition(this.x, this.y);
    }

    public void setAlwaysAnimate(boolean alwaysAnimate) {
        this.alwaysAnimate = alwaysAnimate;
    }
}
