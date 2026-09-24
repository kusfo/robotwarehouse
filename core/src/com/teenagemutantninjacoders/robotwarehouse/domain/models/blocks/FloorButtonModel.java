package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;

/**
 * Created by JordiRM on 10/12/2019.
 */
public class FloorButtonModel extends FloorModel implements iColoredObject{
    private GlobalAttributes.COLOR color = GlobalAttributes.COLOR.NONE;
    private boolean colorExclusive;
    private boolean pressed = false;
    public FloorButtonModel(int h, int v, String resourceName, int variation, GlobalAttributes.COLOR color, boolean colorExclusive){
        super(h, v);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));
        setColor(color);
        setActiveObject(true);
        this.colorExclusive = colorExclusive;
        if(colorExclusive) setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue() + "_ex", 1)));
        else setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue(), 1)));
    }

    public void setPressed(boolean pressed){
        this.pressed = pressed;
        if (pressed) {
            if(colorExclusive) setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue() + "_ex", 2)));
            else setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue(), 2)));
        } else {
            if(colorExclusive) setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue() + "_ex", 1)));
            else setSprite(new Sprite(getAtlas().findRegion("button_" + getColor().getValue(), 1)));
        }
    }

    public boolean isTheCorrectColor(GlobalAttributes.COLOR colorPressing){
        if(colorExclusive) {
            if (getColor() == colorPressing) return true;
            else return false;
        }
        return true;
    }

    public boolean isPressed(){
        return pressed;
    }
    @Override
    public GlobalAttributes.COLOR getColor(){
        return color;
    }
    @Override
    public void setColor(GlobalAttributes.COLOR newColor){
        color = newColor;
    }
}
