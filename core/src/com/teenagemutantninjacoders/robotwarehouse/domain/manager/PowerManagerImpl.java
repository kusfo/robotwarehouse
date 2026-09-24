package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.PowerManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RatlienLairModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

import java.util.ArrayList;

/**
 * Created by JordiM on 09/08/2017.
 */

public class PowerManagerImpl implements PowerManager {
    private LevelModel levelModel;
    private float disableRobots_Timer = 0;
    private float disableCannons_Timer = 0;
    private float fumigation_Timer = 0;
    private Long disabledBuzzingSound = null;

    public PowerManagerImpl(LevelModel levelModel) {
        this.levelModel = levelModel;
    }

    public void update(float delta) {
        if(disableRobots_Timer > 0) {
            disableRobots_Timer = MathUtils.clamp(disableRobots_Timer - (1 * delta), 0, 999);
            if(disableRobots_Timer == 0) {
                executeDisableRobots(false);
                AudioManager.getInstance().stopSoundId(AudioManager.SOUND.DISABLED_BUZZING, disabledBuzzingSound);
            }
        }
        if(disableCannons_Timer > 0){
            disableCannons_Timer = MathUtils.clamp(disableCannons_Timer - (1 * delta), 0, 999);
            if(disableCannons_Timer == 0){
                executeDisableCanon(false);
                AudioManager.getInstance().stopSoundId(AudioManager.SOUND.DISABLED_BUZZING, disabledBuzzingSound);
            }
        }

        if(fumigation_Timer > 0){
            fumigation_Timer = MathUtils.clamp(fumigation_Timer - (1 * delta), 0, 999);
            if(fumigation_Timer == 0){
                executeFumigation(false);
            }
        }
    }

    @Override
    public void executePower(GlobalAttributes.POWER power) {
        switch (power) {
            case POWER_DISABLE_ROBOTS:
                disableRobots_Timer = 10;
                executeDisableRobots(true);
                disabledBuzzingSound = AudioManager.getInstance().playLoopSound(AudioManager.SOUND.DISABLED_BUZZING);
                break;
            case POWER_DISABLE_CANON:
                disableCannons_Timer = 10;
                executeDisableCanon(true);
                disabledBuzzingSound = AudioManager.getInstance().playLoopSound(AudioManager.SOUND.DISABLED_BUZZING);
                break;
            case POWER_FUMIGATION:
                fumigation_Timer = 10;
                executeFumigation(true);
                AudioManager.getInstance().playSound(AudioManager.SOUND.ACTIVATE_FUMIGATION);
                break;
            default:
                break;
        }
        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_POWER_ACTIVATED);
    }


    private void executeDisableRobots(boolean state) {
        ArrayList<GameObject> gameObjects = levelModel.getCurrentGameObjects();
        for(GameObject gameObject: gameObjects) {
            if(gameObject instanceof RobotModel) {
                ((EnemyModel) gameObject).setDisabled(state);
            } else if(gameObject instanceof RobotElevatorModel) {
                ((RobotElevatorModel)gameObject).setDisabled(state);
            }
        }
    }

    private void executeDisableCanon(boolean state) {
        ArrayList<GameObject> gameObjects = levelModel.getCurrentGameObjects();
        for(GameObject gameObject: gameObjects) {
            if(gameObject instanceof CannonModel) {
                if(state) {
                    ((CannonModel) gameObject).setDisabled(true);
                 }else{
                    ((CannonModel) gameObject).setDisabled(false);
                }
            }
        }
    }

    private void executeFumigation(boolean state){
        ArrayList<RatlienLairModel> lairs = levelModel.getRatlienLairList();

        for(RatlienLairModel lair: lairs) {
            if(state) {
                ParticleEffectModel particleEffect;
                if (lair.hasExit(RatlienLairModel.EXIT_DIRECTION.UP)) {
                    particleEffect = new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE,
                            lair.getPositionX() + 14, lair.getPositionY() + 16, GameConstants.ABOVE_DEPTH - 5); //lair.getDepth()
                } else if (lair.hasExit(RatlienLairModel.EXIT_DIRECTION.DOWN)) {
                    particleEffect = new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE,
                            lair.getPositionX() + 14, lair.getPositionY() + 2, GameConstants.ABOVE_DEPTH - 5);
                } else if (lair.hasExit(RatlienLairModel.EXIT_DIRECTION.LEFT)) {
                    particleEffect = new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE,
                            lair.getPositionX(), lair.getPositionY() + 16, GameConstants.ABOVE_DEPTH - 5);
                } else {
                    particleEffect = new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE,
                            lair.getPositionX() + 28, lair.getPositionY() + 16, GameConstants.ABOVE_DEPTH - 5);
                }
                lair.setDisabled(true);
                levelModel.addNewParticleEffect(particleEffect);
                lair.setDisableParticleEffect(particleEffect);
            } else {
                lair.setDisabled(false);
                if(lair.getDisableParticleEffect() != null){
                    lair.getDisableParticleEffect().finalizeParticle();
                    lair.setDisableParticleEffect(null);
                }
            }
        }
        // Destruimos también cualquier ratlien que haya en la zona o subido a una caja.
        if(state) {
            ArrayList<GameObject> gameObjects = levelModel.getCurrentGameObjects();
            ArrayList<RatlienModel> ratliens = new ArrayList<RatlienModel>();
            ArrayList<BoxModel> boxes = new ArrayList<BoxModel>();

            // Recopilamos los objetos a los que puede afectar
            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof RatlienModel) {
                    ratliens.add((RatlienModel) gameObject);
                } else if (gameObject instanceof BoxModel) {
                    boxes.add((BoxModel) gameObject);
                }
            }
            // Añadimos los efectos al final para evitar la concurrencia
            for (RatlienModel ratlien : ratliens) {
                levelModel.addNewParticleEffect(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE_SHORT, ratlien.getPositionX() + 16,
                        ratlien.getPositionY() + 16, ratlien.getDepth() - 5));
                DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.SMASHED));
                ratlien.setDestruction(destructionDTO);
                ratlien.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
            }
            for (BoxModel box : boxes) {
                int ratliensAttached = box.howManyRatliensAttached();
                if(ratliensAttached > 0) {
                    levelModel.addNewParticleEffect(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.POISONOUS_SMOKE_SHORT, box.getPositionX() + 16,
                            box.getPositionY() + 26, box.getDepth() - 5));
                    GlobalLevelData.getInstance().subtractRatlien(ratliensAttached);
                    box.killAllRatliens(false);
                }
            }
        }
    }
}
