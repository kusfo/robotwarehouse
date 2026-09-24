package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.RatlienDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

public class RatlienModel extends EnemyModel implements iDestroyableBoardObject {

    private DestructionDTO destructionDTO;
    private int currentTargetBoxColumn = 0;
    private int currentTargetBoxRow = 0;

    public RatlienModel(int h, int v, DIRECTION initialDirection, ENTRY_TYPE entryType) {
        super(h, v);
        setMoveVelocity(80);
        setCanAvoidFalls(true);
        setCanRunOver(false);
        setCanBeDestroyed(true);
        setCanAvoidDangers(true);
        setAtlas(Assets.getTextureAtlas("e_ratlien"));

        if(entryType == ENTRY_TYPE.LAIR){
            // Lo ponemos a la altura del suelo para que esconda el sprite con el suelo inferior.
            setDepth(GameConstants.FLOOR_DEPTH);
            AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_ENTER);
        }
        setEnemyStatus(ENEMY_STATUS.ENTERING);
        entryAnimation(initialDirection, entryType);
        setGameObjectDraw(new RatlienDraw());
    }

    public void entryAnimation(DIRECTION initialDirection, ENTRY_TYPE entryType){
        // Direccion hacia la que entra
        switch(initialDirection){
            case UP:
                if(entryType == ENTRY_TYPE.LAIR){
                    setAnimation(new Animation(0.15f, getAtlas().findRegions("entering_up")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(-18);
                }
                else if(entryType == ENTRY_TYPE.BOX) {
                    setAnimation(new Animation(0.08f, getAtlas().findRegions("fallOutBox_back")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(-2);
                } else {
                    setAnimation(new Animation(0.2f, getAtlas().findRegion("idle", 0)));
                }
                resetLookingSpriteNumber(0);
                break;

            case DOWN:
                if(entryType == ENTRY_TYPE.LAIR){
                    setAnimation(new Animation(0.15f, getAtlas().findRegions("entering_down")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(0);
                }
                else if(entryType == ENTRY_TYPE.BOX) {
                    setAnimation(new Animation(0.08f, getAtlas().findRegions("fallOutBox_front")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(0);
                } else
                    setAnimation(new Animation(0.2f, getAtlas().findRegion("idle", 4)));
                resetLookingSpriteNumber(4);
                break;

            case LEFT:
                if(entryType == ENTRY_TYPE.LAIR){
                    setAnimation(new Animation(0.15f, getAtlas().findRegions("entering_left")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(-7);
                }
                else if(entryType == ENTRY_TYPE.BOX) {
                    setAnimation(new Animation(0.06f, getAtlas().findRegions("fallOutBox_left")));
                    setSpriteOffsetX(0);
                    setSpriteOffsetY(0);
                } else
                    setAnimation(new Animation(0.2f, getAtlas().findRegion("idle", 6)));
                resetLookingSpriteNumber(6);
                break;

            case RIGHT:
                if(entryType == ENTRY_TYPE.LAIR){
                    setAnimation(new Animation(0.15f, getAtlas().findRegions("entering_right")));
                    setSpriteOffsetX(-9);
                    setSpriteOffsetY(-7);
                }
                else if(entryType == ENTRY_TYPE.BOX) {
                    setAnimation(new Animation(0.06f, getAtlas().findRegions("fallOutBox_right")));
                    setSpriteOffsetX(-32);
                    setSpriteOffsetY(0);
                } else
                    setAnimation(new Animation(0.2f, getAtlas().findRegion("idle", 2)));
                resetLookingSpriteNumber(2);
                break;
        }
        getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void setDestruction(DestructionDTO destructionDTO) {
        this.destructionDTO = destructionDTO;
    }

    @Override
    public void executeDestruction() {
        //switch(destructionDTO.getDestructionType()){
            //case SMASHED:
                EffectManager.getInstance().createSpriteEffect(getPositionXCenter(), getPositionYCenter(),GameConstants.BOARD_EFFECT,
                        Assets.getTextureAtlas("fx_ratlien_dead"), 0.05f, Animation.PlayMode.NORMAL, true);
                EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.RATLIEN_DEAD, getPositionXCenter(), getPositionYCenter(), GameConstants.ABOVE_ALL_DEPTH);

                AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_SMASHED_1, AudioManager.SOUND.RATLIEN_SMASHED_2);
                //break;
        //}
        GlobalLevelData.getInstance().subtractRatlien();
        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
    }

    @Override
    public void doEnemyMainAction(DIRECTION targetDirection, BoardObjectModel boardObjectModel) {
        setTargetBox((BoxModel) boardObjectModel);
        setCurrentTargetBoxPosition((BoxModel) boardObjectModel);
        setTargetDirection(targetDirection);
        setEnemyStatus(ENEMY_STATUS.ATTACHING);
        setMoveAnimation(targetDirection);
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
    public boolean shouldChangeAction() {
        if(getColumn() != getOldColumn()
                || getRow() != getOldRow()) {
            int randomValue = MathUtils.random(0, 5);
            if(randomValue == 0)
                return true;
        }
        return false;
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

    public enum ENTRY_TYPE {
        NONE, LAIR, BOX
    }
}
