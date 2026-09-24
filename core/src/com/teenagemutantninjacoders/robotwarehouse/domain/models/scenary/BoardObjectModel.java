package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.teenagemutantninjacoders.robotwarehouse.display.AssetFileAttributes;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.AtlasSprite;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 10/02/2016.
 */
public class BoardObjectModel extends GameObject {
    private int column;
    private int row;
    private String resourceName;
    private int variation = 1;
    private TextureAtlas atlas;
    private Rectangle hitbox = new Rectangle();
    private Rectangle hitboxImpact = new Rectangle();
    private BOARD_OBJECT_LAYER layer = BOARD_OBJECT_LAYER.BASE;
    private BOARD_OBJECT_EVENT event = BOARD_OBJECT_EVENT.IDLE;
    private BOARD_OBJECT_STATUS status = BOARD_OBJECT_STATUS.FINE;
    private boolean rowDepthDependent  = true;          // Si es cierto, su depth base se verá alterado segun en que celda vertical se encuentre
    private boolean onBoard = true;                     // Sirve para identificar los boardObjecst que aun no han sido introducidos en la array del board
    private boolean touchable = false;                  // Sirve para saber si es un objeto manipulable por el jugador, de no serlo, no se verá afectado por los procesos de touch
    private boolean canFall = true;
    private boolean canBeDestroyed = false;
    private boolean dangerous = false;
    private boolean terminated = false;
    private int obstacleLevel = 0;
    public static int HITBOX_IMPACT_X_BASE = 0;
    public static int HITBOX_IMPACT_Y_BASE = 5;
    public static int HITBOX_IMPACT_W_BASE = 32;
    public static int HITBOX_IMPACT_H_BASE = 37;
    public int getColumn() {
        return column;
    }
    public int getRow() {
        return row;
    }
    public void setColumn(int newColumn) {
        column = newColumn;
    }
    public void setRow(int newRow) {
        row = newRow;
    }

    public Rectangle getHitbox() {
        return new Rectangle(getPositionX() + hitbox.x, getPositionY() + hitbox.y, hitbox.width, hitbox.height);
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
    public void setHitbox(){
        setHitbox(getStandartHitBox());
    }
    public Rectangle getHitboxImpact() {
        return new Rectangle(getPositionX() + hitboxImpact.x, getPositionY() + hitboxImpact.y, hitboxImpact.width, hitboxImpact.height);
    }
    public void setHitboxImpact(Rectangle hitboxImpact) {
        this.hitboxImpact = hitboxImpact;
    }
    public void setHitboxImpact() {
        AssetFileAttributes attributes = Assets.getAssetFileAttributes(resourceName);
        if(attributes != null) {
            AtlasSprite atlasSprite = attributes.atlasSpriteList.get(variation - 1);
            hitboxImpact = new Rectangle(atlasSprite.hitBoxImpact_x, atlasSprite.hitBoxImpact_y, atlasSprite.hitBoxImpact_w, atlasSprite.hitBoxImpact_h);
        } else
            hitboxImpact = new Rectangle(HITBOX_IMPACT_X_BASE, HITBOX_IMPACT_Y_BASE, HITBOX_IMPACT_W_BASE, HITBOX_IMPACT_H_BASE);
    }

    private Rectangle getStandartHitBox(){
        return new Rectangle(0, 0, GameConstants.CELL_WIDTH,GameConstants.CELL_HEIGHT);
    }

    public float getCellPositionX(int newColumn){
        return (newColumn * GameConstants.CELL_WIDTH) + GameConstants.BOARD_ORIGIN_X;
    }
    public float getCellPositionY(int newRow){
        return (   (GameConstants.BOARD_ORIGIN_Y) - ((newRow + 1) * (GameConstants.CELL_HEIGHT))    );
    }
    public float getCellPositionXCenter(int newColumn){
        return getCellPositionX(newColumn) + (GameConstants.CELL_WIDTH / 2);
    }
    public float getCellPositionYCenter(int newRow){
        return getCellPositionY(newRow) + (GameConstants.CELL_WIDTH / 2);
    }
    public void updateCellPosition() {
        setColumn((int) ((getPositionXCenter() - GameConstants.BOARD_ORIGIN_X) / GameConstants.CELL_WIDTH));
        setRow((int) ((GameConstants.BOARD_ORIGIN_Y - getPositionYCenter()) / GameConstants.CELL_HEIGHT)); //-1 Grrr
    }

    public float getPositionXCenter(){
        return getPositionX() + (GameConstants.CELL_WIDTH /2);
    }
    public float getPositionYCenter(){
        return getPositionY() + (GameConstants.CELL_HEIGHT /2);
    }

    public void setAtlas(TextureAtlas atlas){
        this.atlas = atlas;
    }
    public TextureAtlas getAtlas(){
        return atlas;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getVariation() {
        return variation;
    }

    public void setVariation(int variation) {
        this.variation = variation;
        setSprite(new Sprite(getAtlas().getRegions().get(variation - 1)));
    }

    public void setBoardObjectLayer(BOARD_OBJECT_LAYER layer){
        this.layer = layer;
    }
    public BOARD_OBJECT_LAYER getBoardObjectLayer(){
        return layer;
    }
    public BOARD_OBJECT_STATUS getStatus() {
        return status;
    }

    public void setStatus(BOARD_OBJECT_STATUS status) {
        this.status = status;
    }
    public void setEvent(BOARD_OBJECT_EVENT newState, boolean isTerminated){
        event = newState;
        terminated = isTerminated;
    }

    public BOARD_OBJECT_EVENT getEvent(){
        return event;
    }

    public boolean isTerminated(){
        return terminated;
    }

    public boolean isDestroyableByEnemy() {
        return false;
    }

    public boolean isRowDepthDependent() {
        return rowDepthDependent;
    }

    public void setRowDepthDependent(boolean rowDepthDependent) {
        this.rowDepthDependent = rowDepthDependent;
    }

    public boolean isOnBoard() {
        return onBoard;
    }

    public void setOnBoard(boolean onBoard) {
        this.onBoard = onBoard;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean canFall() {
        return canFall;
    }

    public void setCanFall(boolean canFall) {
        this.canFall = canFall;
    }

    public boolean getCanBeDestroyed() {
        return canBeDestroyed;
    }

    public void setCanBeDestroyed(boolean canBeDestroyed) {
        this.canBeDestroyed = canBeDestroyed;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public int getObstacleLevel() {
        return obstacleLevel;
    }

    public void setObstacleLevel(int obstacleLevel) {
        this.obstacleLevel = obstacleLevel;
    }

    @Override
    public int getDepth(){
        if(rowDepthDependent) return MathUtils.floor( super.getDepth() + getPositionY());
        else return super.getDepth();
    }

    public boolean isColliding(BoardObjectModel boardObjectModel2Check) {
        boolean result;
        if(getHitbox() == null || boardObjectModel2Check.getHitbox() == null)
            return false;
        Rectangle rectangleIntersection = new Rectangle();
        result = Intersector.intersectRectangles(getHitbox(),boardObjectModel2Check.getHitbox(),rectangleIntersection);
        return result;
    }

    public boolean isTargetable() {
        return false;
    }

    public enum BOARD_OBJECT_LAYER {
        BASE, OVERBASE, ABOVE
    }
    public enum BOARD_OBJECT_STATUS {
        FINE, ERASED
    }
    public enum BOARD_OBJECT_EVENT {
        IDLE, MOVING, DESTROYING, TELEPORTING, FALLING, ERASING
    }
}
