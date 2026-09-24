package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.OverBaseIndicatorLightDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;

/**
 * Created by JordiRM on 10/02/2019.
 */
public class OverBaseIndicatorLightModel extends OverBaseModel {
    private OverBaseIndicatorLightShine lightShine;
    private Sprite baseGlassSprite, glowingGlassSprite;
    private LIGHT_COLOR lightColor = LIGHT_COLOR.NONE;
    public OverBaseIndicatorLightModel(int h, int v, OverBaseIndicatorLightShine lightShine){
        super(h, v, OVERBASE_TYPE.INDICATOR_LIGHT);
        this.lightShine = lightShine;
        setAtlas(Assets.getTextureAtlas("overBaseIndicatorLight"));
        setSprite(new Sprite(getAtlas().findRegion("base")));
        setDepth(GameConstants.WALL_DEPTH - 5);
        setActiveObject(true);
        baseGlassSprite = new Sprite(getAtlas().findRegion("glass_neutral"));
        baseGlassSprite.setPosition(getPositionX(), getPositionY());
        lightShine.setPosition(getPosition());
        setGameObjectDraw(new OverBaseIndicatorLightDraw());
    }

    public void activateLight(LIGHT_COLOR color){
        lightColor = color;
        switch (lightColor){
            case GREEN:
                baseGlassSprite = new Sprite(getAtlas().findRegion("glass_green" , 1));
                glowingGlassSprite = new Sprite(getAtlas().findRegion("glass_green" , 2));
                lightShine.activateGreenLight();
                break;
            case RED:
                baseGlassSprite = new Sprite(getAtlas().findRegion("glass_red", 1));
                glowingGlassSprite = new Sprite(getAtlas().findRegion("glass_red" , 2));
                lightShine.activateRedLight();
                break;
        }
        baseGlassSprite.setPosition(getPositionX(), getPositionY());
        glowingGlassSprite.setPosition(getPositionX(), getPositionY());
        glowingGlassSprite.setAlpha(0);
    }

    public void setGlowindGlassAlpha(float alpha){
        glowingGlassSprite.setAlpha(alpha);
    }

    public LIGHT_COLOR getLightColor(){
        return lightColor;
    }

    public Sprite getBaseGlassSprite(){
        return baseGlassSprite;
    }
    public Sprite getGlowingGlassSprite(){
        return glowingGlassSprite;
    }

    public OverBaseIndicatorLightShine getLightShine() {
        return lightShine;
    }

    public enum LIGHT_COLOR{
        NONE, GREEN, RED
    }
}
