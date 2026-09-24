package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.OverBaseIndicatorLightShineDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 11/02/2019.
 */
public class OverBaseIndicatorLightShine extends GameObject {
    private Sprite glowSprite = null;
    private Sprite lightRaysSprite = null;
    private OverBaseIndicatorLightModel.LIGHT_COLOR shineLightColor = OverBaseIndicatorLightModel.LIGHT_COLOR.NONE;

    public OverBaseIndicatorLightShine(){
        setDepth(GameConstants.ABOVE_LIGHT_DEPTH);
        setGameObjectDraw(new OverBaseIndicatorLightShineDraw());
    }

    public void activateGreenLight(){
        glowSprite = new Sprite(Assets.getTextureAtlas("overBaseIndicatorLight").findRegion("glow_green"));
        glowSprite.setAlpha(0);
        shineLightColor = OverBaseIndicatorLightModel.LIGHT_COLOR.GREEN;
        setSpritesPosition();
    }

    public void activateRedLight(){
        glowSprite = new Sprite(Assets.getTextureAtlas("overBaseIndicatorLight").findRegion("glow_red"));
        glowSprite.setAlpha(0);
        lightRaysSprite = new Sprite(Assets.getTextureAtlas("overBaseIndicatorLight").findRegion("lightRays_red"));
        lightRaysSprite.setAlpha(0);
        lightRaysSprite.setOriginCenter();

        shineLightColor = OverBaseIndicatorLightModel.LIGHT_COLOR.RED;
        setSpritesPosition();
    }

    private void setSpritesPosition(){
        if(glowSprite != null) glowSprite.setPosition(getPositionX() - 35 , getPositionY() - 21);
        if(lightRaysSprite != null) lightRaysSprite.setPosition(getPositionX() + 16 - 101,  getPositionY() - 68);
    }

    public OverBaseIndicatorLightModel.LIGHT_COLOR getShineLightColor() {
        return shineLightColor;
    }

    public Sprite getGlowSprite(){
        return glowSprite;
    }
    public Sprite getLightRaysSprite(){
        return lightRaysSprite;
    }
}
