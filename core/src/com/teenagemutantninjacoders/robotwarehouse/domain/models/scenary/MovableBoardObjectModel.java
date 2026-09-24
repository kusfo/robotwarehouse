package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;

/**
 * Created by JordiRM on 22/02/2016.
 */
public class MovableBoardObjectModel extends BoardObjectModel {

    private Vector2 destinationPosition = new Vector2();
    private MovableBoardObjectModel.DIRECTION direction = DIRECTION.NEUTRAL;
    private MOVE_STATUS status = MOVE_STATUS.IDLE;
    private MOVE_STATUS lastMoveStatus = MOVE_STATUS.IDLE;
    private boolean canRunOver;
    private boolean canAvoidFalls;
    private boolean canAvoidDangers;
    private boolean canCollide = true;
    private boolean stopInDestination = false;
    private boolean stopOnLevelEvent = true;
    private float moveVelocity = 300f;
    private int oldColumn;
    private int oldRow;

    public void initializeMovableObject() {
        destinationPosition.x = getPositionX();
        destinationPosition.y = getPositionY();
    }

    public MOVE_STATUS getMoveStatus() {
        return status;
    }

    public void setMoveStatus(MOVE_STATUS newStatus) {
        this.status = newStatus;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setMoveVelocity(float newVelocity) {
        this.moveVelocity = newVelocity;
    }

    public float getMoveVelocity() {
        return moveVelocity;
    }

    public boolean CanAvoidFalls() {
        return canAvoidFalls;
    }

    public void setCanAvoidFalls(boolean canAvoidFalls) {
        this.canAvoidFalls = canAvoidFalls;
    }

    public boolean canAvoidDangers() {
        return canAvoidDangers;
    }

    public void setCanAvoidDangers(boolean canAvoidDangers) {
        this.canAvoidDangers = canAvoidDangers;
    }

    public boolean CanRunOver() {
        return canRunOver;
    }

    public void setCanRunOver(boolean canRunOver) {
        this.canRunOver = canRunOver;
    }

    public boolean canCollide() {
        return canCollide;
    }

    public void setCanCollide(boolean canCollide) {
        this.canCollide = canCollide;
    }
    public Vector2 getDestinationPosition() {
        return destinationPosition;
    }

    protected void setNewDestination(MovableBoardObjectModel.DIRECTION newDirection, CellDTO newCellDTO) {
        direction = newDirection;
        int destinationColumn = newCellDTO.getColumn();
        int destinationRow = newCellDTO.getRow();
        storeDestinationPosition(getCellPositionX(destinationColumn), getCellPositionY(destinationRow));

        status = MOVE_STATUS.STARTING_MOVE;     // Asignamos su status interno de movimiento
    }

    public void storeDestinationPosition(float x, float y){
        destinationPosition.x = x;
        destinationPosition.y = y;
    }

    public void setNewMove(MovableBoardObjectModel.DIRECTION newDirection) {
        setDirection(newDirection);
        status = MOVE_STATUS.STARTING_MOVE;     // Asignamos su status interno de movimiento
    }

    protected void setDirection(MovableBoardObjectModel.DIRECTION direction) {
        this.direction = direction;
    }

    public boolean willStopOnDestination() {
        return stopInDestination;
    }

    public void setWillStopOnDestination(boolean stopInDestination) {
        this.stopInDestination = stopInDestination;
    }

    public boolean isStoppingOnLevelEvent() {
        return stopOnLevelEvent;
    }

    public void setStopOnLevelEvent(boolean stopOnLevelEvent) {
        this.stopOnLevelEvent = stopOnLevelEvent;
    }

    public void setOldColumn(int oldColumn) {
        this.oldColumn = oldColumn;
    }

    public void setOldRow(int oldRow) {
        this.oldRow = oldRow;
    }

    public int getOldColumn() {
        return oldColumn;
    }

    public int getOldRow() {
        return oldRow;
    }

    public MOVE_STATUS getLastMoveStatus() {
        return lastMoveStatus;
    }

    public void setLastMoveStatus(MOVE_STATUS lastMoveStatus) {
        this.lastMoveStatus = lastMoveStatus;
    }

    public enum DIRECTION {
        UP("up"), DOWN("down"), LEFT("left"), RIGHT("right"), NEUTRAL("neutral");
        private String value;

        DIRECTION(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static DIRECTION fromString(String text) {
            if (text != null) {
                for (DIRECTION var : DIRECTION.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NEUTRAL;
        }

        public static DIRECTION getOpposite(DIRECTION direction) {
            switch (direction) {
                case NEUTRAL:
                    return NEUTRAL;
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    return NEUTRAL;
            }
        }
    }

    public enum MOVE_STATUS {
        IDLE, STARTING_MOVE, MOVING, STOPED, PAUSED
    }
}
