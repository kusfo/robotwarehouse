package com.teenagemutantninjacoders.robotwarehouse.display;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 01/01/2020.
 */
public class AtlasSprite {
    public int sprite = 0;
    public int hitBoxImpact_x = BoardObjectModel.HITBOX_IMPACT_X_BASE;
    public int hitBoxImpact_y = BoardObjectModel.HITBOX_IMPACT_Y_BASE;
    public int hitBoxImpact_w = BoardObjectModel.HITBOX_IMPACT_W_BASE;
    public int hitBoxImpact_h = BoardObjectModel.HITBOX_IMPACT_H_BASE;
    public AtlasSprite() {}
    public AtlasSprite(int sprite) {
        this.sprite = sprite;
    }
}