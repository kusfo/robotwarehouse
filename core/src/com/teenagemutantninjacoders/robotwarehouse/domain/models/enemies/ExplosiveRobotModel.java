package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.ExplosiveRobotDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by jordimontornes on 26/07/2016.
 */
public class ExplosiveRobotModel extends RobotModel implements iDestroyableBoardObject {

    private float lapseTimeToExplode;
    private DestructionDTO destructionDTO;
    private long sndIdAlarm;

    public ExplosiveRobotModel(int h, int v, RobotElevatorModel elevator) {
        super(h, v);
        setAnimationTime(0); // Mostramos el fotograma final
        setMoveVelocity(70);
        setCanAvoidFalls(true);
        setCanAvoidDangers(true);
        setCanRunOver(false);
        setCanBeDestroyed(true);
        setAtlas(Assets.getTextureAtlas("robot_explosive"));

        setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_down")));
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);

        setGameObjectDraw(new ExplosiveRobotDraw());
        lapseTimeToExplode = 1.5f;
        setEntryElevator(elevator);
    }

    @Override
    public void setNewMove(DIRECTION newDirection) {
        super.setNewMove(newDirection);
        setMoveAnimation(newDirection);
    }
    @Override
    public void setMoveAnimation(DIRECTION newDirection){
        switch (newDirection) {
            case NEUTRAL:
            case DOWN:
                setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_down")));
                break;
            case UP:
                setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_up")));
                break;
            case LEFT:
                setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_left")));
                break;
            case RIGHT:
                setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_right")));
                break;
        }
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);
    }

    public boolean isEnemyReadyToExplode() {
        return getTimeBetweenEvents() == 0;
    }

    public void cancelAutoDestruction(){
        setSprite(new Sprite(getAtlas().getRegions().get(0)));
        removeAnimation();
    }

    @Override
    public void doEnemyMainAction(MovableBoardObjectModel.DIRECTION targetDirection, BoardObjectModel boardObjectModel) {
        setEnemyStatus(EnemyModel.ENEMY_STATUS.EXPLODING);
        switch(getLookingSpriteNumber()){
            case 0:
                setAnimation(new Animation(0.07f, getAtlas().findRegions("autodestruction_back")));
                break;
            case 2:
                setAnimation(new Animation(0.07f, getAtlas().findRegions("autodestruction_right")));
                break;
            case 4: default:
                setAnimation(new Animation(0.07f, getAtlas().findRegions("autodestruction_front")));
                break;
            case 6:
                setAnimation(new Animation(0.07f, getAtlas().findRegions("autodestruction_left")));
                break;
        }
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);
        sndIdAlarm = AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_ALARM);
        setTimeBetweenEvents(lapseTimeToExplode);
    }

    @Override
    public void setDestruction(DestructionDTO destructionDTO) {
        this.destructionDTO = destructionDTO;
    }

    @Override
    public void executeDestruction() {
        switch(destructionDTO.getDestructionType()){
            case SMASHED:
                EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.ROBOT_EXPL_EXPLOSIVE,
                        getPositionX() + 16, getPositionY() + 26, GameConstants.ABOVE_ALL_DEPTH);
                AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_SMASH);
                break;
            case EXPLOSION:
                EffectManager.getInstance().createSpriteEffect(getPositionXCenter(), getPositionYCenter() + 8,GameConstants.BOARD_EFFECT,
                        Assets.getTextureAtlas("fx_robot_explosion"), 0.06f, Animation.PlayMode.NORMAL, true);
                EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.ROBOT_EXPL_EXPLOSIVE,
                        getPositionX() + 16, getPositionY() + 26, GameConstants.ABOVE_ALL_DEPTH);
                AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_EXPLOSION);
                break;
        }
        AudioManager.getInstance().stopSoundId(AudioManager.SOUND.ROBOT_ALARM, sndIdAlarm);
        if(getDisableParticleEffect() != null) getDisableParticleEffect().finalizeParticle();
        GlobalLevelData.getInstance().subtractRobot();
    }
}
