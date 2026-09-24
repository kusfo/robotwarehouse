package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by JordiRM on 21/11/2016.
 */
public class ParticleEffectActor extends Actor {
    ParticleEffectModel particleEffectModel;
    Boolean complete = false;
    public ParticleEffectActor(ParticleEffectModel particleEffectModel) {
        super();
        this.particleEffectModel = particleEffectModel;
    }

    @Override
    public void act(float delta) {
        if(!complete) {
            particleEffectModel.getParticleEffect().update(delta);
            if (particleEffectModel.getParticleEffect().isComplete()) {
                particleEffectModel.getParticleEffect().dispose();
                complete = true;
            }
        }
        super.act(delta);
    }

    public void disposeEffect(){
        if(!complete) {
            particleEffectModel.getParticleEffect().dispose();
            complete = true;
        }
    }

    public ParticleEffectModel getParticleEffectModel(){
        return particleEffectModel;
    }

    @Override
    public void draw(Batch batch,float parentAlpha) {
        if(!complete){
            particleEffectModel.getParticleEffect().draw(batch);
        }
    }
}
