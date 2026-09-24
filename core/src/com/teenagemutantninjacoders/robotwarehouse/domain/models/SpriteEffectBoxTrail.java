package com.teenagemutantninjacoders.robotwarehouse.domain.models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.SpriteEffectBoxTrailDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 19/04/2019.
 */
public class SpriteEffectBoxTrail extends SpriteEffectModel {
    public class BoxTrail{
        private Vector2 position;
        private float alpha;
        public BoxTrail(Vector2 position, float alpha){
            this.position = position;
            this.alpha = alpha;
        }
        public Vector2 getPosition(){
            return position;
        }
        public float getAlpha(){
            return alpha;
        }
    }
    private int remainingTrails = 3;
    private ArrayList<BoxTrail> boxTrails = new ArrayList<BoxTrail>();
    private BoxModel boxModel;
    private int step = 0;
    private boolean finalized = false;
    public SpriteEffectBoxTrail(float x, float y, int depth, BoxModel boxModel){
        super(x, y, depth);
        this.boxModel = boxModel;
        setGameObjectDraw(new SpriteEffectBoxTrailDraw());
        AudioManager.getInstance().playSound(AudioManager.SOUND.ROTATING_FLOOR);
    }

    public  ArrayList<BoxTrail> getTrailsList(){
        return boxTrails;
    }

    @Override
    public void update(float delta){
        if(!finalized) {
            if (boxModel != null && boxModel.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.MOVING) {
                if (remainingTrails > 0 && step % 2 == 0) {
                    boxTrails.add(new BoxTrail(new Vector2(boxModel.getPositionX(), boxModel.getPositionY()), 0.5f));
                    remainingTrails--;
                }
                step++;
                if (boxTrails.size() == 0) finalized = true;
                else executeTrailPhase(delta);
            } else {
                finalized = true;
            }
        }
    }
    @Override
    public boolean isFinished(){
        return finalized;
    }

    public void executeTrailPhase(float delta){
        int i = 0;
        do{
            float alpha = boxTrails.get(i).alpha;
            alpha = MathUtils.clamp(alpha - (1.5f * delta), 0, 1);
            if(alpha == 0) boxTrails.remove(i);
            else {
                boxTrails.get(i).alpha = alpha;
                i++;
            }
        } while (i < boxTrails.size());
    }

    public BoxModel getBoxModel() {
        return boxModel;
    }
}
