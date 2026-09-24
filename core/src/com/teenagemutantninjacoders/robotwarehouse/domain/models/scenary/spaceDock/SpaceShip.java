package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 19/07/2017.
 */
public class SpaceShip extends GameObject {
    private SHIP_EVENT shipEvent = SHIP_EVENT.WAITING;
    private float pauseCounter = 0;
    private float interpolationTime = 0;

    private float engineGlowAlpha = 0;
    private ENGINE_EVENT engineEvent = ENGINE_EVENT.INACTIVE;
    private Sprite engineGlow;
    private TextureAtlas atlas;
    private Sprite[] containerSprite;
    private Sprite shipEngines;
    private AnimatedSprite animatedEngineFire;
    private boolean assignedContainers = false;

    public SpaceShip(){
        super.setPosition(new Vector2(8,-1000));
        setDepth(GameConstants.SPACESHIP_DEPTH);
        atlas = Assets.getTextureAtlas("spaceDock");
        setActualizeWhileIsNotPlaying(true);
        setSprite(new Sprite(atlas.findRegion("ship_01_front")));
        shipEngines = new Sprite(atlas.findRegion("ship_01_back"));
        engineGlow = new Sprite(atlas.findRegion("shipEngineGlow"));
        containerSprite = new Sprite[3];
        animatedEngineFire = new AnimatedSprite(atlas, "shipEngineFireLoop", 0.06f, Animation.PlayMode.NORMAL);
        animatedEngineFire.setAlwaysAnimate(true);
        actualiceSprites();
        setGameObjectDraw(new SpaceShipDraw());
    }

    public void assignContainers(boolean random){
        int[] containerNumbers = new int[3];
        if(random) {
            for (int i = 0; i < 3; i++) {
                containerNumbers[i] = MathUtils.random(1, 6);
                containerSprite[i] = new Sprite(atlas.findRegion("ship_container", containerNumbers[i]));
            }
            GlobalLevelData.getInstance().setLastShipContainers(containerNumbers[0], containerNumbers[1], containerNumbers[2]);
        } else {
            // Si repetimos el nivel, queremos que los contenedores sean los mismos ya que la nave estacionada no ha cambiado
            containerSprite[0] = new Sprite(atlas.findRegion("ship_container", GlobalLevelData.getInstance().getLastShipContainer(0)));
            containerSprite[1] = new Sprite(atlas.findRegion("ship_container", GlobalLevelData.getInstance().getLastShipContainer(1)));
            containerSprite[2] = new Sprite(atlas.findRegion("ship_container", GlobalLevelData.getInstance().getLastShipContainer(2)));
        }
        assignedContainers = true;
    }

    @Override
    public void setPosition(Vector2 newPosition){
        super.setPosition(newPosition);
        actualiceSprites();
    }

    private void actualiceSprites(){
        if(assignedContainers) {
            for (int i = 0; i < 3; i++) {
                containerSprite[i].setPosition(getPositionX(), getPositionY() - 9 + (45 * i));
            }
        }
        shipEngines.setPosition(getPositionX(), getPositionY() - 78);
        engineGlow.setPosition(getPositionX() + 35, getPositionY() - 65);
        animatedEngineFire.setPosition(getPositionX() + 18, getPositionY() - 102);
    }

    public void enginesLoop(){
        animatedEngineFire.changeAnimation("shipEngineFireLoop", 0.06f, Animation.PlayMode.LOOP);
        setEngineEvent(ENGINE_EVENT.ACTIVE);
    }

    public void enginesTurnOff(){
        animatedEngineFire.changeAnimation("shipEngineFireOff", 0.1f, Animation.PlayMode.NORMAL);
        setEngineEvent(SpaceShip.ENGINE_EVENT.DEACTIVATING);
    }

    public void enginesTurnOn(){
        animatedEngineFire.changeAnimation("shipEngineFireOff", 0.1f, Animation.PlayMode.REVERSED);
        animatedEngineFire.addSequence("shipEngineFireLoop", 0.06f, Animation.PlayMode.LOOP);
        setEngineEvent(ENGINE_EVENT.ACTIVATING);
    }

    public SHIP_EVENT getShipEvent() {
        return shipEvent;
    }

    public void setShipEvent(SHIP_EVENT shipEvent) {
        this.shipEvent = shipEvent;
    }

    public float getPauseCounter() {
        return pauseCounter;
    }
    public void setPauseCounter(float pauseCounter) {
        this.pauseCounter = pauseCounter;
    }
    public void substractPauseCounter(float amount) {
        this.pauseCounter -= amount;
    }

    public float getInterpolationTime() {
        return interpolationTime;
    }

    public void setInterpolationTime(float interpolationTime) {
        this.interpolationTime = interpolationTime;
    }

    public float getEngineGlowAlpha() {
        return engineGlowAlpha;
    }

    public void setEngineGlowAlpha(float engineGlowAlpha) {
        this.engineGlowAlpha = engineGlowAlpha;
    }
    public ENGINE_EVENT getEngineEvent() {
        return engineEvent;
    }

    public void setEngineEvent(ENGINE_EVENT engineEvent) {
        this.engineEvent = engineEvent;
    }

    public Sprite getEngineGlow() {
        return engineGlow;
    }

    public AnimatedSprite getAnimatedEngineFire(){
        return animatedEngineFire;
    }

    public Sprite getContainer(int number){
        return containerSprite[number];
    }

    public Sprite getShipEngines(){
        return shipEngines;
    }

    public boolean hasAssignedContainers() {
        return assignedContainers;
    }

    public enum SHIP_EVENT{
        WAITING, LOADING, ENTERING, LEAVING
    }
    public enum ENGINE_EVENT {
        ACTIVE, INACTIVE, ACTIVATING, DEACTIVATING;
    }
}
