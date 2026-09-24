package com.teenagemutantninjacoders.robotwarehouse.domain.controllers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;

/**
 * Created by JordiRM on 12/02/2016.
 */
public class GameInputProcessor implements InputProcessor {
    private Vector2 lastTouchDownPos = Vector2.Zero;
    private Vector2 lastTouchDownRealPos = Vector2.Zero;
    private int lastTouchColumn = -1;
    private int lastTouchRow = -1;

    private Viewport currentViewPort;
    private int screenDraggedX;
    private int screenDraggedY;

    private GameController listener;

    public GameInputProcessor() {

    }

    public void setCurrentViewPort(Viewport currentViewPort){
        this.currentViewPort = currentViewPort;
    }
    public void setGameControllerInputListener( GameController listener){
        this.listener = listener;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float originX =  GameConstants.BOARD_ORIGIN_X;
        float originY =  GameConstants.VERTICAL_RESOLUTION - GameConstants.BOARD_ORIGIN_Y;

        // Hacemos un unproject de las coordenadas para que nos de un resultado escalado a la pantalla
        Vector2 touchPos = Utils.unprojectPosition(currentViewPort, new Vector2(screenX, screenY));
        lastTouchDownRealPos = touchPos;
        lastTouchDownPos.x = touchPos.x;
        lastTouchDownPos.y = touchPos.y + GameConstants.CELL_SIDE;

        lastTouchColumn =   (int) Math.floor((touchPos.x - originX) / GameConstants.CELL_WIDTH);
        lastTouchRow =      (int) Math.floor((touchPos.y - originY + GameConstants.CELL_SIDE) / GameConstants.CELL_HEIGHT);

        listener.touchDown(true);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenDraggedX = 0;
        screenDraggedY = 0;
        listener.touchDown(false);
        listener.touchDragged(false);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        screenDraggedX = screenX;
        screenDraggedY = screenY;
        listener.touchDragged(true);
        return true;
    }

    public Vector2 getLastTouchDownPosition() {
        return lastTouchDownPos;
    }
    public Vector2 getLastTouchDownRealPosition() {
        return lastTouchDownRealPos;
    }
    public int getLastTouchDownColumn(){
        return lastTouchColumn;
    }
    public int getLastTouchDownRow(){
        return lastTouchRow;
    }
    public int getScreenDraggedX(){
        return screenDraggedX;
    }
    public int getScreenDraggedY(){
        return screenDraggedY;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
