package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 20/07/2017.
 */
public class SpaceDockMonitor extends GameObject {
    private DOCK_MONITOR_EVENT monitorEvent = DOCK_MONITOR_EVENT.WAITING;
    private Integer requestedBoxesShowed = 0;
    private BitmapFont font;
    private GlyphLayout monitorLayout;
    private float pauseCounter = 0;
    private float counter = 0;
    private float interpolationTime = 0;
    private float textScale = 1.0f;
    private float adquiredBounceCounter = 1;
    private boolean scoreReady = false;
    private AnimatedSprite screen;

    public SpaceDockMonitor(){
        setPosition(new Vector2(13,-100));
        setDepth(GameConstants.SPACESHIP_DEPTH - 5);
        setSprite(new Sprite(Assets.getTextureAtlas("generalLevelPanels").findRegion("shipPlatformMonitorFrame")));
        setActualizeWhileIsNotPlaying(true);
        screen = new AnimatedSprite(Assets.getTextureAtlas("generalLevelPanels"), "shipPlatformMonitorScreen", 0.1f, Animation.PlayMode.LOOP);
        setGameObjectDraw(new SpaceDockMonitorDraw());
        font = Assets.getFont("f_base_gb_22");
        //font.setColor(253 / 255f,232 / 255f,127 / 255f,0.85f); // Amarillo
        font.setColor(1, 1, 1, 0.85f); // Blanco

        monitorLayout = new GlyphLayout();
    }

    public BitmapFont getMonitorFont() {
        return font;
    }
    public GlyphLayout getMonitorLayout(){
        return monitorLayout;
    }
    public DOCK_MONITOR_EVENT getMonitorEvent() {
        return monitorEvent;
    }

    public void setMonitorEvent(DOCK_MONITOR_EVENT event) {
        this.monitorEvent = event;
    }

    public AnimatedSprite getScreen() {
        return screen;
    }

    public Integer getRequestedBoxesShowed() {
        return requestedBoxesShowed;
    }

    public void setRequestedBoxesShowed(Integer requestedBoxesShowed) {
        this.requestedBoxesShowed = requestedBoxesShowed;
    }

    public float getInterpolationTime() {
        return interpolationTime;
    }

    public void setInterpolationTime(float interpolationTime) {
        this.interpolationTime = interpolationTime;
    }

    public float getAdquiredBounceCounter() {
        return adquiredBounceCounter;
    }

    public void setAdquiredBounceCounter(float adquiredBounceCounter) {
        this.adquiredBounceCounter = adquiredBounceCounter;
    }

    public float getTextScale() {
        return textScale;
    }

    public void setTextScale(float textScale) {
        this.textScale = textScale;
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

    public float getCounter() {
        return counter;
    }
    public void setCounter(float counter) {
        this.counter = counter;
    }
    public void addCounter(float amount) {
        this.counter += amount;
    }

    public void setScoreReady(boolean ready){
        scoreReady = ready;
    }
    public boolean isScoreReady(){
        return scoreReady;
    }

    public enum DOCK_MONITOR_EVENT{
        WAITING, SHOWING, ENTERING, LEAVING
    }
}
