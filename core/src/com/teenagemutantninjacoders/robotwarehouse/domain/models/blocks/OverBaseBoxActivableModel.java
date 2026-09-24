package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.OverBaseBoxActivableDraw;

/**
 * Created by JordiRM on 03/07/2016.
 */
public class OverBaseBoxActivableModel extends OverBaseModel {
    private int groupNumber;
    private GlobalAttributes.POWER power;
    private boolean activated = false;
    private boolean touched = false;
    private Sprite glowSprite;
    private String spriteName;
    private OVERBASE_TYPE overBaseType;

    public OverBaseBoxActivableModel(int h, int v, String resourceName, int variation, OVERBASE_TYPE overBaseType, int groupNumber, GlobalAttributes.POWER power) {
        super(h, v, overBaseType);
        setResourceName(resourceName);
        this.groupNumber = groupNumber;
        this.power = power;
        this.overBaseType = overBaseType;
        setAtlas(Assets.getTextureAtlas("overBaseBoxActivable"));
        switch (overBaseType) {
            case POINTS:
                spriteName = "points_gen_";
                break;
            case POWERS:
                spriteName = "skill_gen_";
                break;
        }
        //setVariation(variation);
        setSprite(new Sprite(getAtlas().findRegion(spriteName + "a")));
        setGlowSprite(new Sprite(getAtlas().findRegion(spriteName + "b")));
        setGameObjectDraw(new OverBaseBoxActivableDraw());
        setActiveObject(true);
    }

    public void setGlowSprite(Sprite sprite) {
        glowSprite = sprite;
        glowSprite.setPosition(getPositionX(), getPositionY());
        glowSprite.setColor(1, 1, 1, 0);
    }

    public Sprite getGlowSprite() {
        return glowSprite;
    }

    public void setGlowAlpha(float alpha){
        glowSprite.setAlpha(alpha);
    }

    public void activate() {
        setSprite( new Sprite(getAtlas().findRegion(spriteName + "c")));
        setDepth(-2000); // (!)
        activated = true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void touch() {
        touched = true;
    }

    public void untouch() {
        touched = false;
    }
    public boolean isTouched() {
        return touched;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public GlobalAttributes.POWER getPower() {
        return power;
    }

    public void setPower(GlobalAttributes.POWER power) {
        this.power = power;
    }
}

