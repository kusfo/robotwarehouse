package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectBoxTrail;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectRobotBubble;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordiRM on 15/03/2017.
 */
public class EffectManager {
    private static EffectManager instance;
    private LevelModel levelModel;
    private List<ParticleEffectModel> pendingEffectList;

    public static EffectManager getInstance(){
        if(instance == null) {
            instance = new EffectManager();
        }
        return instance;
    }

    public void initializeManager(LevelModel levelModel){
        this.levelModel = levelModel;
        this.pendingEffectList = new ArrayList<ParticleEffectModel>();
    }

    public ParticleEffectModel createEffect(ParticleEffectModel.EFFECT_TYPE effectType, float x, float y, int depth){
        ParticleEffectModel newEffect = new ParticleEffectModel(effectType, x, y, depth);
        pendingEffectList.add(newEffect);
        return newEffect;
    }

    public SpriteEffectModel createSpriteEffect(float x, float y, int depth, TextureAtlas atlas, float speed, Animation.PlayMode playMode, boolean centered){
        SpriteEffectModel spriteEffect = new SpriteEffectModel(x, y, depth, atlas, speed, playMode, centered);
        levelModel.addNewGameObject(spriteEffect);
        return spriteEffect;
    }
    public SpriteEffectModel createSpriteEffect(float x, float y, int depth, TextureAtlas atlas, String region, float speed, Animation.PlayMode playMode, boolean centered){
        SpriteEffectModel spriteEffect = new SpriteEffectModel(x, y, depth, atlas, region, speed, playMode, centered);
        levelModel.addNewGameObject(spriteEffect);
        return spriteEffect;
    }

    public void createSpriteEffectBoxTrail(float x, float y, int depth, BoxModel boxModel){
        levelModel.addNewGameObject(new SpriteEffectBoxTrail(x, y, depth, boxModel));
    }

    public void createSpriteEffectRobotBubble(float x, float y, int depth, float timeShowing, SpriteEffectRobotBubble.ROBOT_BUBBLE_TYPE type){
        levelModel.addNewGameObject(new SpriteEffectRobotBubble(x, y, depth, timeShowing, type));
    }

    public void addPendingEffectsToLevel() {
        for(ParticleEffectModel particleEffectModel : pendingEffectList) {
            levelModel.addNewParticleEffect(particleEffectModel);
        }
        pendingEffectList.clear();
    }
}
