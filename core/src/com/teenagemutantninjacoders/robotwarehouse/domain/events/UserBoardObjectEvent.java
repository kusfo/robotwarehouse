package com.teenagemutantninjacoders.robotwarehouse.domain.events;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by JordiRM on 16/02/2016.
 */
public class UserBoardObjectEvent {
    MovableBoardObjectModel.DIRECTION direction;
    BoardObjectModel boardObjectModel;
    private EVENT_TYPE requestedEvent;

    public UserBoardObjectEvent(){
        clearEvents();
    }

    public boolean hasRequestedEvent() {
        //if (boardObjectModel != null && direction != MovableBoardObjectModel.DIRECTION.NEUTRAL) return true;
        if(requestedEvent != EVENT_TYPE.NONE) return true;
        return false;
    }

    public EVENT_TYPE getRequestedEvent(){
        return requestedEvent;
    }

    public void setBoardObjectModel(BoardObjectModel newBoardObjectModel){
        boardObjectModel = newBoardObjectModel;
    }
    public BoardObjectModel getBoardObjectModel(){
        return boardObjectModel;
    }

    public void setMovementRequest(MovableBoardObjectModel.DIRECTION newDirection){
        direction = newDirection;
        requestedEvent = EVENT_TYPE.MOVEMENT;
    }
    public void setTeleportRequest(){
        requestedEvent = EVENT_TYPE.TELEPORT;
    }

    public MovableBoardObjectModel.DIRECTION getDirection(){
        return direction;
    }

    public void clearEvents() {
        boardObjectModel = null;
        direction = MovableBoardObjectModel.DIRECTION.NEUTRAL;
        requestedEvent = EVENT_TYPE.NONE;
    }

    public enum EVENT_TYPE{
        NONE, MOVEMENT, TELEPORT
    }
}
