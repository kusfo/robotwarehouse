package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by jordi.montornes on 30/01/2016.
 */
public class EnemyModel extends MovableBoardObjectModel implements EnemyAction {

    private ENEMY_STATUS enemy_status;
    private ENEMY_STATUS lastEnemy_status;
    private BoxModel targetBox;
    private DIRECTION targetDirection;
    private DIRECTION previousDirection;
    private DIRECTION nextDirection = DIRECTION.NEUTRAL;
    private boolean disabled = false;
    private int lookingSpriteNumber = 4; //abajo
    private int nextLookingSpriteNumber = 4;
    private float turnTime = 0;
    private float waitingTime = 0;

    public AnimatedSprite disableEffect;
    public ParticleEffectModel disableParticleEffect = null;

    private final int enemyIdentifier;

    private float timeBetweenEvents = 0.f;
    private float timeForMainAction = 0.f;

    public EnemyModel(int h, int v) {
        setColumn(h);
        setRow(v);
        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setHitbox();
        initializeMovableObject(); // Una vez hemos inicializado el objeto, hacemos lo mismo con su boardObject
        setBoardObjectLayer(BOARD_OBJECT_LAYER.ABOVE);
        setDepth(GameConstants.ABOVE_DEPTH);
        setObstacleLevel(GameConstants.ENEMY_OBSTACLE_LEVEL);
        setActiveObject(true);
        targetDirection = DIRECTION.NEUTRAL;
        previousDirection = DIRECTION.NEUTRAL;
        enemyIdentifier = GlobalLevelData.getInstance().getNextEnemyIdentifier();
        disableEffect = new AnimatedSprite(Assets.getTextureAtlas("fx_enemyDeactivationSparks"), 0.1f, Animation.PlayMode.LOOP);
    }

    public AnimatedSprite getDisableEffect() {
        return disableEffect;
    }

    public ParticleEffectModel getDisableParticleEffect() {
        return disableParticleEffect;
    }
    public void removeDisableParticleEffect(){
        disableParticleEffect = null;
    }
    public ENEMY_STATUS getEnemyStatus() {
        return enemy_status;
    }

    public void setEnemyStatus(ENEMY_STATUS enemyStatus) {
        this.enemy_status = enemyStatus;
    }

    public ENEMY_STATUS getLastEnemy_status() {
        return lastEnemy_status;
    }

    public void setLastEnemy_status(ENEMY_STATUS lastEnemy_status) {
        this.lastEnemy_status = lastEnemy_status;
    }

    public float getTimeBetweenEvents() {
        return timeBetweenEvents;
    }

    public void setTimeBetweenEvents(float timeBetweenEvents) {
        this.timeBetweenEvents = timeBetweenEvents;
    }

    public float getTimeForMainAction() {
        return timeForMainAction;
    }

    public void setTimeForMainAction(float timeForMainAction) {
        this.timeForMainAction = timeForMainAction;
    }

    public int getEnemyIdentifier() {
        return enemyIdentifier;
    }

    @Override
    public void setNewMove(DIRECTION newDirection) {
        if(newDirection != null && newDirection != DIRECTION.NEUTRAL){
            previousDirection = newDirection;
        }
        super.setNewMove(newDirection);
    }

    public void setMoveAnimation(DIRECTION newMoveAnimationDirection){}

    public void setTargetBox(BoxModel targetBox) {
        this.targetBox = targetBox;
    }

    public void setTargetDirection(DIRECTION targetDirection) {
        this.targetDirection = targetDirection;
    }

    public BoxModel getTargetBox() {
        return targetBox;
    }

    public DIRECTION getTargetDirection() {
        return targetDirection;
    }

    public DIRECTION getPreviousDirection(){
        return previousDirection;
    }

    public DIRECTION getNextDirection() {
        return nextDirection;
    }

    public void setNextDirection(DIRECTION nextDirection) {
        this.nextDirection = nextDirection;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getLookingSpriteNumber() {
        return lookingSpriteNumber;
    }

    public void setLookingSpriteNumber(int dir) {
        this.lookingSpriteNumber = dir;
    }

    public int getNextLookingSpriteNumber() {
        return nextLookingSpriteNumber;
    }

    public void setNextLookingSpriteNumber(int nextLookingSpriteNumber) {
        this.nextLookingSpriteNumber = nextLookingSpriteNumber;
    }
    public void resetLookingSpriteNumber(int lookingSpriteNumber){
        this.lookingSpriteNumber = lookingSpriteNumber;
        this.nextLookingSpriteNumber = lookingSpriteNumber;
    }
    public boolean isLookingToNextDirection(){
        return lookingSpriteNumber == nextLookingSpriteNumber;
    }

    public float getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(float turnTime) {
        this.turnTime = turnTime;
    }

    public float getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(float waitingTime) {
        this.waitingTime = waitingTime;
    }

    @Override
    public void disableEnemy() {
        disableParticleEffect = EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.DEACTIVATION_SPARKS,getPositionXCenter(), getPositionY() + 30, getDepth() - 5);
        this.setEnemyStatus(EnemyModel.ENEMY_STATUS.DISABLED);
    }

    public enum ENEMY_STATUS{
        NONE, MOVING, STOPING, PUSHING, EXPLODING, ENTERING, ATTACHING, CHANGING_DIRECTION, DISABLED, WAITING
    }

    public enum ENEMY_TYPE {
        NORMAL_ROBOT, EXPLOSIVE_ROBOT, RATLIEN
    }

    @Override
    public void doEnemyMainAction(MovableBoardObjectModel.DIRECTION targetDirection, BoardObjectModel boardObjectModel) {

    }

    @Override
    public boolean shouldChangeAction() {
        return false;
    }
}
