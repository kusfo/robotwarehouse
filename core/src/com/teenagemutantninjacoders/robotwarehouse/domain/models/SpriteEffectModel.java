package com.teenagemutantninjacoders.robotwarehouse.domain.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.SpriteEffectDraw;

/**
 * Created by JordiRM on 09/11/2018.
 */
public class SpriteEffectModel extends GameObject {
    private TextureAtlas.AtlasRegion sizeRegion;
    private TextureAtlas atlas;
    // Constructor para spriteEffects especificos
    SpriteEffectModel(float x, float y, int depth){
        setPosition(new Vector2(x, y));
        setDepth(depth);
        setActiveObject(true);
    }

    // Constructores para spriteEffects genericos que solo consisten en una animación
    public SpriteEffectModel(float x, float y, int depth, TextureAtlas atlas, float speed, Animation.PlayMode playMode, boolean centered){
        this.atlas = atlas;
        sizeRegion = atlas.getRegions().get(0);
        initializeGeneric(x, y, depth, centered);
        setAnimation(new Animation(speed, atlas.getRegions()));
        getAnimation().setPlayMode(playMode);
    }

    public SpriteEffectModel(float x, float y, int depth, TextureAtlas atlas, String region, float speed, Animation.PlayMode playMode, boolean centered){
        this.atlas = atlas;
        sizeRegion = atlas.findRegions(region).get(0);
        initializeGeneric(x, y, depth, centered);
        setAnimation(new Animation(speed, atlas.findRegions(region)));
        getAnimation().setPlayMode(playMode);
    }
    public SpriteEffectModel(float x, float y, int depth,  TextureAtlas atlas, boolean centered){
        this.atlas = atlas;
        sizeRegion = atlas.getRegions().get(0);
        initializeGeneric(x, y, depth, centered);
    }

    public void setEffectAnimation(String region, float speed, Animation.PlayMode playMode){
        setAnimation(new Animation(speed, atlas.findRegions(region)));
        getAnimation().setPlayMode(playMode);
    }

    private void initializeGeneric(float x, float y, int depth, boolean centered){
        if(centered) setPosition(new Vector2(x - ((float) sizeRegion.getRegionWidth() / 2), y - ((float) sizeRegion.getRegionHeight() / 2)));
        else setPosition(new Vector2(x, y));
        setDepth(depth);
        setGameObjectDraw(new SpriteEffectDraw());
        setActiveObject(true);
    }

    public void update(float delta){}

    public boolean isFinished(){
        if(hasAnimation())
           if(!getAnimation().isAnimationFinished(getAnimationTime()))  return false;
        return true;
    }
}