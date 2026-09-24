package com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies.CannonStopDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.CannonDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 03/04/2016.
 */
public class CannonModel extends MovableBoardObjectModel {

    private CANNON_STATUS status = CANNON_STATUS.NONE;
    private CANNON_SHOOT_DIRECTION shootDirection = CANNON_SHOOT_DIRECTION.DOWN;
    private CANNON_SHOOT_DIRECTION nextShootDirection;
    private CANNON_SHOOT_DIRECTION actualTurretVisualShootDirection;
    private CANNON_SHOOT_DIRECTION nextTurretVisualShootDirection;
    private int wagonFrameNumber = 1;
    private int turretImageNumber = 0;
    private CLOCK_DIR wagonRotationDir = CLOCK_DIR.NONE;
    private float wagonImageTime = 0;
    private float turretImageTime = 0;
    private MovableBoardObjectModel.DIRECTION newDir = DIRECTION.NEUTRAL;
    private MovableBoardObjectModel.DIRECTION previousDir = DIRECTION.NEUTRAL;
    private boolean disabled = false;
    private Sprite laserSprite;
    private boolean isShooting = false;
    private boolean isImpacting = false;
    private Vector2 laserImpactPosition = new Vector2();
    private CellDTO shootingCell = new CellDTO(-1, -1);

    private Sprite turretSprite;
    private Vector2 turretPosition = new Vector2();
    private int turretImageIndex = 1;

    private int advancesLeft = 0;
    private float cannonEventTime = 0;
    private float cannonWaitingTime = 0;

    private AnimatedSprite disableEffect;
    private ParticleEffectModel disableParticleEffect = null;
    private AnimatedSprite impactEffect;

    private int maxAutoAdvances;
    private float pauseAfterShooting;
    private ArrayList<CannonStopDTO> programedStops;
    private int stopJumps = 0;
    private boolean programedLoop;
    private int actualProgramedStopIndex;
    private int baseVelocity; // de 20 a 150 (50 = normal)

    public CannonModel(int h, int v, String resourceName, int variation, DIRECTION initialDirection, CANNON_SHOOT_DIRECTION shootDirection,
                       int maxAutoAdvances, int baseVelocity, int pauseAfterShooting, int initialWaitingTime,
                       boolean programedLoop, ArrayList<CannonStopDTO> programedStops){
        this.shootDirection = shootDirection;
        nextShootDirection = shootDirection;
        actualTurretVisualShootDirection = shootDirection;
        nextTurretVisualShootDirection = shootDirection;
        setColumn(h);
        setRow(v);
        setResourceName(resourceName);
        setAtlas(Assets.getTextureAtlas(resourceName));

        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setBoardObjectLayer(BOARD_OBJECT_LAYER.ABOVE);
        setActiveObject(true);
        this.maxAutoAdvances = maxAutoAdvances;
        this.baseVelocity = baseVelocity;
        this.pauseAfterShooting = pauseAfterShooting;
        this.programedStops = programedStops;
        this.programedLoop = programedLoop;
        setMoveVelocity(baseVelocity);

        setAdvancesLeft(maxAutoAdvances);

        // Inicializamos segun su configuración
        if(programedStops.size() == 0) actualProgramedStopIndex = -1;
        else{
            actualProgramedStopIndex = 0;
            stopJumps =  programedStops.get(actualProgramedStopIndex).getJumps();
        }

        cannonWaitingTime = initialWaitingTime;

        setTurretSpriteByShootDirection(shootDirection);
        setWagonSpriteByDirection(initialDirection);

        setSpriteOffsetX(-3);
        setDepth(GameConstants.ABOVE_DEPTH - 400);//50
        initializeMovableObject();

        setGameObjectDraw(new CannonDraw());

        super.setDirection(initialDirection);
        newDir = initialDirection;
        previousDir = initialDirection;
        setCanCollide(false);
        setCanFall(false);
        setWillStopOnDestination(true);
        setCanAvoidFalls(false);
        setCanRunOver(false);
        disableEffect = new AnimatedSprite(Assets.getTextureAtlas("fx_enemyDeactivationSparks"), 0.1f, Animation.PlayMode.LOOP);
    }

    private void setWagonSpriteByDirection(MovableBoardObjectModel.DIRECTION dir) {
        switch (dir) {
            case LEFT:
            case RIGHT:
                setWagonSprite("horizontal", -1);
                break;
            case UP:
            case DOWN:
                setWagonSprite("vertical", -1);
                break;
            default:
                setWagonSprite("horizontal", -1);
                break;
        }
    }

    public void setWagonSprite(String region, int index){
        setSprite(new Sprite(getAtlas().findRegion(region, index)));
        // Posicion de la torreta segun el fotograma de la vagoneta
        if(region.equals("horizontal")){
            setTurretPosition(new Vector2(-8, 4));
        } else if(region.equals("vertical")){
            setTurretPosition(new Vector2(-8, 4));
        }
        else if(region.equals("turn_left_down")) {
            switch(index) {
                case 1: setTurretPosition(new Vector2(-4, 6)); break;
                case 2: setTurretPosition(new Vector2(-8, 3)); break;
                case 3: setTurretPosition(new Vector2(-10, -1)); break;
            }
        }
        else if(region.equals("turn_right_down")){
            switch(index) {
                case 1: setTurretPosition(new Vector2(-13, 5)); break;
                case 2: setTurretPosition(new Vector2(-9, 2)); break;
                case 3: setTurretPosition(new Vector2(-6, -1)); break;
            }
        }
        else if(region.equals("turn_right_up")){
            switch(index) {
                case 1: setTurretPosition(new Vector2(-14, 7)); break;
                case 2: setTurretPosition(new Vector2(-8, 8)); break;
                case 3: setTurretPosition(new Vector2(-7, 12)); break;
            }
        }
        else if(region.equals("turn_left_up")){
            switch(index) {
                case 1: setTurretPosition(new Vector2(-3, 6)); break;
                case 2: setTurretPosition(new Vector2(-8, 8)); break;
                case 3: setTurretPosition(new Vector2(-9, 12)); break;
            }
        }
    }
    public void activeLaser(float x1, float y1,float x2, float y2){
        isShooting = true;
        isImpacting = true;
        laserImpactPosition = new Vector2(x2, y2);
        float distance;
        switch(shootDirection){
            case LEFT:case RIGHT:
                distance = x2 - x1;
                laserSprite = new Sprite(getAtlas().findRegion("cannon_laser_h"));
                laserSprite.setOriginCenter();
                laserSprite.setPosition(x1, y1 - (laserSprite.getHeight() / 2));
                laserSprite.setSize(distance, 32);
                laserSprite.setScale(1, 0);
                if(shootDirection == CANNON_SHOOT_DIRECTION.LEFT)
                    laserImpactPosition.x = x1;
                else
                    laserImpactPosition.x = x2;
                laserImpactPosition.y = y1;
                break;
            case UP: case DOWN:
                distance = y2 - y1;
                laserSprite = new Sprite(getAtlas().findRegion("cannon_laser_v"));
                laserSprite.setOriginCenter();
                laserSprite.setPosition(x1 - (laserSprite.getWidth() / 2), y1);
                laserSprite.setSize(32, distance);
                laserSprite.setScale(0, 1);
                if(shootDirection == CANNON_SHOOT_DIRECTION.UP)
                    laserImpactPosition.y = y2;
                else
                    laserImpactPosition.y = y1;
                laserImpactPosition.x = x1;
                break;
        }
        impactEffect = new AnimatedSprite(getAtlas(), "impact_" + shootDirection.getValue(), 0.05f, Animation.PlayMode.LOOP);
        impactEffect.setPositionCentered(laserImpactPosition.x, laserImpactPosition.y);
        impactEffect.play();
    }

    public void restoreBaseVelocity(){
        setMoveVelocity(baseVelocity);
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

    public AnimatedSprite getImpactEffect() {
        return impactEffect;
    }

    public boolean isShooting(){
        return isShooting;
    }

    public void setIsShooting(boolean state){
        this.isShooting = state;
    }

    public boolean isImpacting() {
        return isImpacting;
    }

    public void setImpacting(boolean impacting) {
        isImpacting = impacting;
    }

    public Sprite getLaserSprite(){
        return laserSprite;
    }

    public void activateCharge(){
        setCannonStatus(CANNON_STATUS.CHARGING);
        setCannonEventTime(4);
    }
    public void activateShoot(){
        setCannonStatus(CANNON_STATUS.SHOOTING);
        setCannonEventTime(1);
    }

    public float getCannonEventTime() {
        return cannonEventTime;
    }

    public void setCannonEventTime(float cannonEventTime) {
        this.cannonEventTime = cannonEventTime;
    }

    public float getCannonWaitingTime() {
        return cannonWaitingTime;
    }

    public void setCannonWaitingTime(float cannonWaitingTime) {
        this.cannonWaitingTime = cannonWaitingTime;
    }

    public int getMaxAutoAdvances() {
        return maxAutoAdvances;
    }

    public void setPauseAfterShooting(float amount){
        pauseAfterShooting = amount;
    }
    public float getPauseAfterShooting() {
        return pauseAfterShooting;
    }

    public CellDTO getActualProgramedStop() {
        if(actualProgramedStopIndex != -1){
            return programedStops.get(actualProgramedStopIndex).getCell();
        }
        return null;
    }

    public void decreaseProgramedStopJumps(){
        stopJumps--;
    }
    public int getProgramedStopJumps(){
        return stopJumps;
    }
    public void setNextProgramedStop(){
        if(actualProgramedStopIndex != -1) {
            if (actualProgramedStopIndex < programedStops.size() - 1)
                actualProgramedStopIndex++;
            else {
                if (programedLoop) actualProgramedStopIndex = 0;
                else actualProgramedStopIndex = -1;
            }
            if(actualProgramedStopIndex >= 0) stopJumps = programedStops.get(actualProgramedStopIndex).getJumps();
            else stopJumps = 0;
        }
    }

    public CellDTO getShootingCell() {
        return shootingCell;
    }

    public void setShootingCell(CellDTO shootingCell) {
        this.shootingCell = shootingCell;
    }

    public CANNON_STATUS getCannonStatus() {
        return status;
    }
    public void setCannonStatus(CANNON_STATUS status) {
        this.status = status;
    }

    public CANNON_SHOOT_DIRECTION getShootDirection() {
        return shootDirection;
    }

    public void setShootDirection(CANNON_SHOOT_DIRECTION shootDirection) {
        this.shootDirection = shootDirection;
    }

    public CANNON_SHOOT_DIRECTION getNextShootDirection() {
        return nextShootDirection;
    }

    public void setNextShootDirection(CANNON_SHOOT_DIRECTION nextShootDirection) {
        this.nextShootDirection = nextShootDirection;
    }

    public CANNON_SHOOT_DIRECTION getActualTurretVisualShootDirection() {
        return actualTurretVisualShootDirection;
    }

    public void setActualTurretVisualShootDirection(CANNON_SHOOT_DIRECTION actualTurretVisualShootDirection) {
        this.actualTurretVisualShootDirection = actualTurretVisualShootDirection;
    }

    public CANNON_SHOOT_DIRECTION getNextTurretVisualShootDirection() {
        return nextTurretVisualShootDirection;
    }

    public void setNextTurretVisualShootDirection(CANNON_SHOOT_DIRECTION nextTurretVisualShootDirection) {
        this.nextTurretVisualShootDirection = nextTurretVisualShootDirection;
    }

    public Sprite getTurretSprite() {
        return turretSprite;
    }

    public void setTurretSpriteByIndex(int index) {
        turretSprite = new Sprite(getAtlas().findRegion("cannon_1", index));
        setTurretImageIndex(index);
    }

    public Vector2 getTurretPosition() {
        return turretPosition;
    }

    public void setTurretPosition(Vector2 turretPosition) {
        this.turretPosition.x = turretPosition.x;
        this.turretPosition.y = turretPosition.y;
    }

    public void setTurretSpriteByShootDirection(CANNON_SHOOT_DIRECTION direction) {
        int index = 1;
        switch (direction){
            case UP: index = 1; break;
            case DOWN: index = 9; break;
            case LEFT: index = 5; break;
            case RIGHT: index = 13; break;
        }
        setTurretImageIndex(index);
        setTurretSpriteByIndex(index);
    }

    public int getTurretImageIndex() {
        return turretImageIndex;
    }

    public void setTurretImageIndex(int turretImageIndex) {
        this.turretImageIndex = turretImageIndex;
    }

    public int getWagonFrameNumber() {
        return wagonFrameNumber;
    }

    public void setWagonFrameNumber(int wagonFrameNumber) {
        this.wagonFrameNumber = wagonFrameNumber;
    }

    public void setNewDestination(MovableBoardObjectModel.DIRECTION newDirection, CellDTO newCellDTO) {
        super.setNewDestination(newDirection, newCellDTO);
    }

    public CLOCK_DIR getWagonRotationDir() {
        return wagonRotationDir;
    }

    public void setWagonRotationDir(CLOCK_DIR wagonRotationDir) {
        this.wagonRotationDir = wagonRotationDir;
    }

    public float getWagonImageTime() {
        return wagonImageTime;
    }

    public void setWagonImageTime(float imageTime) {
        this.wagonImageTime = imageTime;
    }

    public float getTurretImageTime() {
        return turretImageTime;
    }

    public void setTurretImageTime(float turretImageTime) {
        this.turretImageTime = turretImageTime;
    }

    public DIRECTION getNewDir() {
        return newDir;
    }

    public void setNewDir(DIRECTION newDir) {
        this.newDir = newDir;
    }

    public DIRECTION getPreviousDir() {
        return previousDir;
    }

    public void setPreviousDir(DIRECTION previousDir) {
        this.previousDir = previousDir;
    }

    public int getAdvancesLeft() {
        return advancesLeft;
    }

    public void setAdvancesLeft(int advancesLeft) {
        this.advancesLeft = advancesLeft;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void disableCannon() {
        disableParticleEffect = EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.DEACTIVATION_SPARKS,getPositionXCenter(), getPositionY() + 30, getDepth() - 5);
        this.status = CANNON_STATUS.DISABLED;
    }

    public enum CLOCK_DIR {
        NONE, TWELVE_THREE, THREE_SIX, SIX_NINE, NINE_TWELVE, TWELVE_NINE ,NINE_SIX, SIX_THREE, THREE_TWELVE
    }
    public enum CANNON_STATUS{
        NONE, MOVING, ROTATING, CHARGING, SHOOTING, DISABLED, WAITING
    }
    public enum CANNON_SHOOT_DIRECTION {
        UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");
        private String value;

        CANNON_SHOOT_DIRECTION(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static CANNON_SHOOT_DIRECTION fromString(String text) {
            if (text != null) {
                for (CANNON_SHOOT_DIRECTION var : CANNON_SHOOT_DIRECTION.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return DOWN;
        }
    }
}
