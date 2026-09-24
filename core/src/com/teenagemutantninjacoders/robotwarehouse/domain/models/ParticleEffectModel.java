package com.teenagemutantninjacoders.robotwarehouse.domain.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

/**
 * Created by JordiRM on 21/03/2016.
 */
public class ParticleEffectModel extends GameObject {
    private ParticleEffect effect;
    public ParticleEffectModel(EFFECT_TYPE effectType, float x, float y, int depth) {
        effect = new ParticleEffect();
        switch(effectType){
            case TELEPORT:              effect.load(Gdx.files.internal("effects/teletransport_01a"), Gdx.files.internal("images/particles")); break;
            case CANNON_CHARGE:         effect.load(Gdx.files.internal("effects/cannon_charging"), Gdx.files.internal("images/particles")); break;
            case BOX_DESTROY_BLUE:      effect.load(Gdx.files.internal("effects/box_explosion_blue"), Gdx.files.internal("images/particles")); break;
            case BOX_DESTROY_RED:       effect.load(Gdx.files.internal("effects/box_explosion_red"), Gdx.files.internal("images/particles")); break;
            case BOX_DESTROY_YELLOW:    effect.load(Gdx.files.internal("effects/box_explosion_yellow"), Gdx.files.internal("images/particles")); break;
            case BOX_DESTROY_GREY:      effect.load(Gdx.files.internal("effects/box_explosion_grey"), Gdx.files.internal("images/particles")); break;

            case RATLIEN_DEAD:          effect.load(Gdx.files.internal("effects/fx_ratlien_dead"), Gdx.files.internal("images/particles")); break;

            case LASER_SPARKS_UP:       effect.load(Gdx.files.internal("effects/laser_sparks_up"), Gdx.files.internal("images/particles")); break;
            case LASER_SPARKS_DOWN:     effect.load(Gdx.files.internal("effects/laser_sparks_down"), Gdx.files.internal("images/particles")); break;
            case LASER_SPARKS_RIGHT:    effect.load(Gdx.files.internal("effects/laser_sparks_right"), Gdx.files.internal("images/particles")); break;
            case LASER_SPARKS_LEFT:     effect.load(Gdx.files.internal("effects/laser_sparks_left"), Gdx.files.internal("images/particles")); break;
            case ROBOT_EXPL_PUSHER:     effect.load(Gdx.files.internal("effects/ef_robotExplosion_pusher"), Gdx.files.internal("images/particles")); break;
            case ROBOT_EXPL_EXPLOSIVE:  effect.load(Gdx.files.internal("effects/ef_robotExplosion_explosive"), Gdx.files.internal("images/particles")); break;
            case DEACTIVATION_SPARKS:   effect.load(Gdx.files.internal("effects/enemy_deactivation_sparks_01"),Gdx.files.internal("images/particles")); break;
            case POISONOUS_SMOKE:       effect.load(Gdx.files.internal("effects/poisonousSmoke"),Gdx.files.internal("images/particles")); break;
            case POISONOUS_SMOKE_SHORT: effect.load(Gdx.files.internal("effects/poisonousSmokeShort"),Gdx.files.internal("images/particles")); break;

            case RANK_STAR_LEFT:        effect.load(Gdx.files.internal("effects/rankStars_left"), Gdx.files.internal("images/particles")); break;
            case RANK_STAR_CENTER:      effect.load(Gdx.files.internal("effects/rankStars_center"), Gdx.files.internal("images/particles")); break;
            case RANK_STAR_RIGHT:       effect.load(Gdx.files.internal("effects/rankStars_right"), Gdx.files.internal("images/particles")); break;

            case LEVEL_STAR_UNBLOCKING:         effect.load(Gdx.files.internal("effects/levelStarUnblockingStars"), Gdx.files.internal("images/particles")); break;
            case ADD_STAR_TO_BLOCKED_LEVEL:     effect.load(Gdx.files.internal("effects/addStarToBlockedLevel"), Gdx.files.internal("images/particles")); break;
            case FLYING_STAR_TO_BLOCKED_LEVEL:  effect.load(Gdx.files.internal("effects/flyingStarToBlockedLevel"), Gdx.files.internal("images/particles")); break;
            case STARLEVEL_PALPITATION:         effect.load(Gdx.files.internal("effects/starLevelPalpitation"), Gdx.files.internal("images/particles")); break;
        }

        setDepth(depth);
        effect.setPosition(x, y);
        effect.start();
    }
    public void finalizeParticle(){
        effect.allowCompletion();
        effect.setDuration(0);
    }
    public ParticleEffect getParticleEffect(){
        return effect;
    }
    public enum EFFECT_TYPE{
        TELEPORT,
        CANNON_CHARGE,
        BOX_DESTROY_RED,
        BOX_DESTROY_BLUE,
        BOX_DESTROY_YELLOW,
        BOX_DESTROY_GREY,
        RATLIEN_DEAD,
        LASER_SPARKS_UP,
        LASER_SPARKS_DOWN,
        LASER_SPARKS_RIGHT,
        LASER_SPARKS_LEFT,
        ROBOT_EXPL_PUSHER,
        ROBOT_EXPL_EXPLOSIVE,
        DEACTIVATION_SPARKS,
        POISONOUS_SMOKE,
        POISONOUS_SMOKE_SHORT,
        RANK_STAR_LEFT,
        RANK_STAR_CENTER,
        RANK_STAR_RIGHT,
        LEVEL_STAR_UNBLOCKING,
        ADD_STAR_TO_BLOCKED_LEVEL,
        FLYING_STAR_TO_BLOCKED_LEVEL,
        STARLEVEL_PALPITATION
    }
}
