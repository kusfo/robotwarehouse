package com.teenagemutantninjacoders.robotwarehouse.domain.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.GameObjectDraw;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by JordiRM on 08/02/2016.
 */
public class GameObject {

    private Vector2 position;
    private int depth = 0;
    private Sprite sprite;
    private Animation<TextureRegion> animation;
    private float animationTime;
    private float alpha = 1.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float rotation = 0;
    private float spriteOffsetX = 0;
    private float spriteOffsetY = 0;
    private boolean actualizeWhileIsNotPlaying = false;
    private boolean visible = true;
    private boolean activeObject = false;
    private GameObjectDraw gameObjectDraw = null;
    private int id;

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(1000);


    public GameObject() {
        id = ID_GENERATOR.getAndIncrement();
    }

    public Vector2 getPosition(){
        return position;
    }
    public void setPosition(Vector2 newPosition){
        position = newPosition;
    }
    public float getPositionX(){
        return position.x;
    }
    public void setPositionX(float newPositionX){
        position.x = newPositionX;
    }
    public float getPositionY(){
        return position.y;
    }
    public void setPositionY(float newPositionY){
        position.y = newPositionY;
    }

    public float getAlpha() {
        return alpha;
    }
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public float getScaleX() {
        return scaleX;
    }
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public int getDepth(){
        return depth;
    }
    public void setDepth(int newDepth){
        depth = newDepth;
    }

    public Sprite getSprite(){
        return sprite;
    }
    public void setSprite(Sprite newSprite){
        sprite = newSprite;
    }

    public float getSpriteOffsetX() {
        return spriteOffsetX;
    }

    public void setSpriteOffsetX(float spriteOffsetX) {
        this.spriteOffsetX = spriteOffsetX;
    }

    public float getSpriteOffsetY() {
        return spriteOffsetY;
    }

    public void setSpriteOffsetY(float spriteOffsetY) {
        this.spriteOffsetY = spriteOffsetY;
    }

    public void setAnimation(Animation<TextureRegion> newAnimation){
        animation = newAnimation;
        animationTime = 0;
        setSprite(new Sprite(animation.getKeyFrame(0)));
        sprite.setRotation(getRotation());
    }
    public void removeAnimation(){
        animation = null;
    }
    public Animation getAnimation(){
        return animation;
    }
    public boolean hasAnimation(){
        if(animation != null) return true;
        return false;
    }
    public void updateAnimation(float delta) {
        if (hasAnimation()){
            animationTime += delta;
            sprite.setRegion(animation.getKeyFrame(animationTime, false));
            sprite.setRotation(getRotation());
        }
    }
    public void setAnimationTime(float time){
        animationTime = time;
    }
    public float getAnimationTime(){
        return animationTime;
    }

    public boolean getActualizeWhileIsNotPlaying() {
        return actualizeWhileIsNotPlaying;
    }

    public void setActualizeWhileIsNotPlaying(boolean actualizeWhileIsNotPlaying) {
        this.actualizeWhileIsNotPlaying = actualizeWhileIsNotPlaying;
    }

    public GameObjectDraw getGameObjectDraw() {
        return gameObjectDraw;
    }

    public void setGameObjectDraw(GameObjectDraw gameObjectDraw) {
        this.gameObjectDraw = gameObjectDraw;
    }

    public boolean canActualizeObject(){
        if(GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) return true;
        else if(actualizeWhileIsNotPlaying) return true;
        return false;
    }

    public int getId() {
        return id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isActiveObject() {
        return activeObject;
    }

    public void setActiveObject(boolean activeObject) {
        this.activeObject = activeObject;
    }
}
