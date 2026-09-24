package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.PusherRobotDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by jordi.montornes on 30/01/2016.
 */
public class PusherRobotModel extends RobotModel implements iDestroyableBoardObject {

    private final float timelapseToPush;
    private int currentTargetBoxColumn = 0;
    private int currentTargetBoxRow = 0;
    private boolean pushingAnimation = false;
    private boolean waitingToPush = false;

    private DestructionDTO destructionDTO;

    public PusherRobotModel(int h, int v, RobotElevatorModel elevator) {
        super(h,v);
        setMoveVelocity(70);
        setCanAvoidFalls(true);
        setCanRunOver(false);
        setCanBeDestroyed(true);
        setCanAvoidDangers(true);
        setAtlas(Assets.getTextureAtlas("robot_pusher"));
        setAnimation(new Animation(0.1f, getAtlas().findRegions("moving_down")));
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);
        setGameObjectDraw(new PusherRobotDraw());
        timelapseToPush = 0.4f;//0.2f;
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
                setAnimation(new Animation(0.12f, getAtlas().findRegions("moving_down")));
                break;
            case UP:
                setAnimation(new Animation(0.12f, getAtlas().findRegions("moving_up")));
                break;
            case LEFT:
                setAnimation(new Animation(0.12f, getAtlas().findRegions("moving_left")));
                break;
            case RIGHT:
                setAnimation(new Animation(0.12f, getAtlas().findRegions("moving_right")));
                break;
        }
        getAnimation().setPlayMode(Animation.PlayMode.LOOP);
    }

    public boolean isReadyToPush() {
        if(isLookingToNextDirection()) {
            if(!waitingToPush){
                setTimeBetweenEvents(timelapseToPush);
                waitingToPush = true;
            } else {
                if(getTimeBetweenEvents() < 0.8f && !pushingAnimation) {
                    activePushAnimation(getLookingSpriteNumber());
                    pushingAnimation = true;
                }
                if(getTimeBetweenEvents() == 0){
                    return true;
                }
            }
        }
        return false;
    }

    private void activePushAnimation(int lookingSpriteNumber){
        switch (lookingSpriteNumber) {
            case 0:
                setAnimation(new Animation(0.09f, getAtlas().findRegions("pushing_up")));
                break;
            case 2:
                setAnimation(new Animation(0.09f, getAtlas().findRegions("pushing_right")));
                break;
            case 4:
            default:
                setAnimation(new Animation(0.09f, getAtlas().findRegions("pushing_down")));
                break;
            case 6:
                setAnimation(new Animation(0.09f, getAtlas().findRegions("pushing_left")));
                break;
        }
    }

    @Override
    public void doEnemyMainAction(MovableBoardObjectModel.DIRECTION targetDirection, BoardObjectModel boardObjectModel) {
        setTargetBox((BoxModel) boardObjectModel);
        setTargetDirection(targetDirection);
        setCurrentTargetBoxPosition((BoxModel) boardObjectModel);
        setEnemyStatus(EnemyModel.ENEMY_STATUS.PUSHING);
        pushingAnimation = false;
        waitingToPush = false;
    }

    private void setCurrentTargetBoxPosition(BoxModel boxModel) {
        currentTargetBoxColumn = boxModel.getColumn();
        currentTargetBoxRow = boxModel.getRow();
    }

    public int getCurrentTargetBoxColumn() {
        return currentTargetBoxColumn;
    }

    public int getCurrentTargetBoxRow() {
        return currentTargetBoxRow;
    }

    @Override
    public void setDestruction(DestructionDTO destructionDTO) {
        this.destructionDTO = destructionDTO;
    }

    @Override
    public void executeDestruction() {
        switch(destructionDTO.getDestructionType()){
            case SMASHED:
                EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.ROBOT_EXPL_PUSHER,
                        getPositionX() + 16, getPositionY() + 26, GameConstants.ABOVE_ALL_DEPTH); //getDepth() - 1000
                AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_SMASH);
                break;
        }
        if(getDisableParticleEffect() != null) getDisableParticleEffect().finalizeParticle();
        GlobalLevelData.getInstance().subtractRobot();
    }
}
