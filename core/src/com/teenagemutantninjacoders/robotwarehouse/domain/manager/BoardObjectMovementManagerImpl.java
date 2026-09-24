package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.Rectangle;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.BoardModelHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoardObjectMovementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorRotatingModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordiRM on 24/02/2016.
 */
public class BoardObjectMovementManagerImpl implements BoardObjectMovementManager {
    private LevelModel levelModel;
    private BoxListener boxListener;
    private final AchievementManager achievementManager;

    private class MovableObjectStats{
        int oldColumn;
        int oldRow;
        int nextColumn;
        int nextRow;
        float advanceX = 0;
        float advanceY = 0;
    }
    private MovableObjectStats movableObjectStats;
    private class CollisionData{
        BoardObjectModel collider = null;
        COLLISION_ACTION collisionAction = COLLISION_ACTION.NONE;
    }

    public BoardObjectMovementManagerImpl(LevelModel levelModel, BoxListener boxListener, AchievementManager achievementManager) {
        this.levelModel = levelModel;
        this.boxListener = boxListener;
        this.achievementManager = achievementManager;
        movableObjectStats = new MovableObjectStats();
    }

    @Override
    public void updateObject(MovableBoardObjectModel movableObject, float delta) {
        if (movableObject.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.PAUSED) return;
        if (GlobalLevelData.getInstance().getLevelEvent() != GlobalLevelData.LEVEL_EVENT.NONE &&
        movableObject.isStoppingOnLevelEvent()) return;
        movableObjectStats = implementMovableObjectStats(movableObject, movableObject.getDirection(), delta);

        // Comprobamos que no haya un cambio de dirección por culpa de un suelo giratorio
        processTurnByFloor(movableObject, delta);

        CollisionData collisionData = getCollisionData(movableObject, movableObjectStats);

        // Si hemos atropellado algo, actualizamos el objeto implicado
        if(collisionData.collisionAction == COLLISION_ACTION.RUN_OVER) runOverCollider(movableObject, collisionData.collider);

        boolean willStop = collisionData.collisionAction == COLLISION_ACTION.STOP;
        boolean willFall = checkFalls(movableObject);
        boolean willByInDanger = checkDangers(movableObject);
        if ((!willStop) && (!willFall || !movableObject.CanAvoidFalls()) && (!willByInDanger || !movableObject.canAvoidDangers())) {
            movableObject.setPositionX(movableObject.getPositionX() + movableObjectStats.advanceX);
            movableObject.setPositionY(movableObject.getPositionY() + movableObjectStats.advanceY);
        }
        else {
            if (movableObject.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.MOVING) {
                movableObject.setPositionX(movableObject.getCellPositionX(movableObject.getColumn()));
                movableObject.setPositionY(movableObject.getCellPositionY(movableObject.getRow()));
                movableObject.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.STOPED);
            }else{
                finishMovement(movableObject);
            }
        }

        // Si el objeto necesita pararse en una celda comprobamos si ha llegado a la posicion indicada
        if(movableObject.willStopOnDestination()){
            boolean hasArrived = false;
            switch (movableObject.getDirection()) {
                case DOWN:
                    if (movableObject.getPositionY() <= movableObject.getDestinationPosition().y) {
                        movableObject.setPositionY(movableObject.getDestinationPosition().y);
                        hasArrived = true;
                    }
                    break;
                case LEFT:
                    if (movableObject.getPositionX() <= movableObject.getDestinationPosition().x) {
                        movableObject.setPositionX(movableObject.getDestinationPosition().x);
                        hasArrived = true;
                    }
                    break;
                case RIGHT:
                    if (movableObject.getPositionX() >= movableObject.getDestinationPosition().x) {
                        movableObject.setPositionX(movableObject.getDestinationPosition().x);
                        hasArrived = true;
                    }
                    break;
                case UP:
                    if (movableObject.getPositionY() >= movableObject.getDestinationPosition().y) {
                        movableObject.setPositionY(movableObject.getDestinationPosition().y);
                        hasArrived = true;
                    }
                    break;
            }

            if (hasArrived) movableObject.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.STOPED);
        }

        // Actualizamos en el objeto, la casilla donde esta
        movableObject.updateCellPosition();

        // Actualizamos en la grid, la casilla en la que estaba y en la que esta actualmente
        levelModel.getCurrentBoardModel().changeBoardObjectPosition(movableObject, movableObjectStats.oldColumn, movableObjectStats.oldRow, movableObject
                .getColumn(), movableObject.getRow());

        movableObject.setOldColumn(movableObjectStats.oldColumn);
        movableObject.setOldRow(movableObjectStats.oldRow);

        // Gestionamos los posibles eventos de inicio o finalizacion del movimiento
        if (movableObject.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.STARTING_MOVE) {
            movableObject.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.MOVING);
        } else if (movableObject.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.STOPED) {
            finishMovement(movableObject);
            if (movableObject instanceof BoxModel) {
                // (?) Ya se aplica en el finishMovement()
                //boxListener.updateBoxGroup(((BoxModel) movableObject));
                //boxListener.updateNeighboursBoxesGroup(movableObject.getColumn(), movableObject.getRow(), ((BoxModel) movableObject).getColor());

                achievementManager.checkBoxKillsRelatedAchievement(((BoxModel)movableObject).getRobotsSmashed(), ((BoxModel)movableObject).getRatliensSmashed());
                ((BoxModel)movableObject).resetSmashedObjectCounters();
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_HIT);
            }
        }
    }

    private void processTurnByFloor(MovableBoardObjectModel movableObject, float delta){
        // De momento solo afecta a las cajas
        if (movableObject instanceof BoxModel) {
            BoxModel boxModel = (BoxModel) movableObject;

            // Comrobamos si estamos en una baldosa de giro
            MovableBoardObjectModel.DIRECTION turnDirection = MovableBoardObjectModel.DIRECTION.NEUTRAL;
            BoardObjectModel floor = levelModel.getCurrentBoardModel().getBoardObjectBase(movableObject.getColumn(), movableObject.getRow());
            if(floor instanceof FloorRotatingModel){
                turnDirection = ((FloorRotatingModel)floor).getActualDirection();
            }

            if(turnDirection != MovableBoardObjectModel.DIRECTION.NEUTRAL && turnDirection != movableObject.getDirection()){
                boolean hasArrived = false;
                switch (movableObject.getDirection()) {
                    case DOWN:
                        if (movableObject.getPositionY() > movableObject.getCellPositionY(movableObject.getRow()) &&
                                movableObject.getPositionY() + movableObjectStats.advanceY <= movableObject.getCellPositionY(movableObject.getRow())) {
                            hasArrived = true;
                        }
                        break;
                    case LEFT:
                        if (movableObject.getPositionX() > movableObject.getCellPositionX(movableObject.getColumn()) &&
                                movableObject.getPositionX() + movableObjectStats.advanceX <= movableObject.getCellPositionX(movableObject.getColumn())) {
                            hasArrived = true;
                        }
                        break;
                    case RIGHT:
                        if (movableObject.getPositionX() < movableObject.getCellPositionX(movableObject.getColumn()) &&
                                movableObject.getPositionX() + movableObjectStats.advanceX >= movableObject.getCellPositionX(movableObject.getColumn())) {
                            hasArrived = true;
                        }
                        break;
                    case UP:
                        if (movableObject.getPositionY() < movableObject.getCellPositionY(movableObject.getRow()) &&
                                movableObject.getPositionY() + movableObjectStats.advanceY >= movableObject.getCellPositionY(movableObject.getRow())) {
                            hasArrived = true;
                        }
                        break;
                }

                if (hasArrived) {
                    // Creamos unos stats de movimiento y colision temporales para asegurarnos que la nueva dirección no estará obstruida, si no, seguimos
                    MovableObjectStats nextMovableObjectStats = implementMovableObjectStats(movableObject, turnDirection, delta);
                    CollisionData collisionData = getCollisionData(movableObject, nextMovableObjectStats);
                    if (collisionData.collisionAction != COLLISION_ACTION.STOP) {
                        // Lo centramos en la casilla de la flecha
                        movableObject.setPositionX(movableObject.getCellPositionX(movableObject.getColumn()));
                        movableObject.setPositionY(movableObject.getCellPositionY(movableObject.getRow()));
                        // Cambiamos la direccion a la nueva y seguimos sin interrupción
                        boxModel.setNewMove(turnDirection);
                        // Como hemos cambiado de dirección necesitamos nuevos stats de movimiento
                        movableObjectStats = implementMovableObjectStats(movableObject, movableObject.getDirection(), delta);
                        // Finalmente creamos un efecto de estela en la caja
                        EffectManager.getInstance().createSpriteEffectBoxTrail(boxModel.getPositionX(), boxModel.getPositionY(), GameConstants.BOARD_EFFECT, boxModel);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDirectionObstructed(MovableBoardObjectModel movableObject, MovableBoardObjectModel.DIRECTION direction, float delta){
        movableObjectStats = implementMovableObjectStats(movableObject, direction, delta);
        return checkCollisions(movableObject);
    }

    private MovableObjectStats implementMovableObjectStats(MovableBoardObjectModel movableObject, MovableBoardObjectModel.DIRECTION direction, float delta){
        MovableObjectStats localMovableObjectStats = new MovableObjectStats();
        localMovableObjectStats.oldColumn =  movableObject.getColumn();
        localMovableObjectStats.oldRow = movableObject.getRow();
        localMovableObjectStats.nextColumn = localMovableObjectStats.oldColumn;
        localMovableObjectStats.nextRow = localMovableObjectStats.oldRow;
        localMovableObjectStats.advanceX = 0;
        localMovableObjectStats.advanceY = 0;
        float moveVelocity = movableObject.getMoveVelocity();

        switch (direction) {
            case UP:
                localMovableObjectStats.nextRow -= 1;
                localMovableObjectStats.advanceY = moveVelocity * delta;
                break;
            case DOWN:
                localMovableObjectStats.nextRow += 1;
                localMovableObjectStats.advanceY = -(moveVelocity * delta);
                break;
            case LEFT:
                localMovableObjectStats.nextColumn -= 1;
                localMovableObjectStats.advanceX = -(moveVelocity * delta);
                break;
            case RIGHT:
                localMovableObjectStats.nextColumn += 1;
                localMovableObjectStats.advanceX = moveVelocity * delta;
                break;
        }
        return localMovableObjectStats;
    }

    private CollisionData getCollisionData(MovableBoardObjectModel movableObject, MovableObjectStats nextMovableObjectStats){
        CollisionData collisionData = new CollisionData();
        if(movableObject.canCollide()){
            // Guardamos el posible objeto que vamos a encontrarnos
            BoardObjectModel collidedObject = getCollidedObject(movableObject, nextMovableObjectStats.nextColumn, nextMovableObjectStats.nextRow, nextMovableObjectStats.advanceX, nextMovableObjectStats.advanceY);
            if (collidedObject != null) {
                collisionData.collider = collidedObject;
                collisionData.collisionAction = getCollisionAction(movableObject, collidedObject);
                return collisionData;
            }
        }
        return collisionData;
    }

    private boolean checkCollisions(MovableBoardObjectModel movableObject){
        if(movableObject.canCollide()){
            // Guardamos el posible objeto que vamos a encontrarnos
            BoardObjectModel collidedObject = getCollidedObject(movableObject, movableObjectStats.nextColumn, movableObjectStats.nextRow, movableObjectStats.advanceX, movableObjectStats.advanceY);
            if (collidedObject != null) {
                if(getCollisionAction(movableObject, collidedObject) == COLLISION_ACTION.STOP) return true;
            }
        }
        return false;
    }

    private COLLISION_ACTION getCollisionAction(MovableBoardObjectModel movableObject, BoardObjectModel collidedObject){
        // Caja colisiona con Enemigo
        if (movableObject instanceof BoxModel && collidedObject instanceof EnemyModel) {
            if (movableObject.CanRunOver() && collidedObject.getCanBeDestroyed()) {
                return COLLISION_ACTION.RUN_OVER;
            }
        }
        return COLLISION_ACTION.STOP;
    }

    private void runOverCollider(MovableBoardObjectModel movableObject, BoardObjectModel collidedObject){
        if (collidedObject.getEvent() != BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING &&
                collidedObject.getEvent() != BoardObjectModel.BOARD_OBJECT_EVENT.FALLING) {
            DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.SMASHED));
            if (!collidedObject.isTerminated()) collidedObject.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
            ((iDestroyableBoardObject) collidedObject).setDestruction(destructionDTO);

            // Lo sacamos inmediatamente del board y lo marcamos como que ya no pertenece a este
            levelModel.getCurrentBoardModel().deleteBoardObject(collidedObject.getColumn(), collidedObject.getRow(),
                    BoardObjectModel.BOARD_OBJECT_LAYER.ABOVE);
            collidedObject.setOnBoard(false);
            if(movableObject instanceof BoxModel) {
                if(collidedObject instanceof RobotModel) {
                    ((BoxModel)movableObject).addRobotSmashed();
                } else if(collidedObject instanceof RatlienModel) {
                    ((BoxModel)movableObject).addRatlienSmashed();
                }
            }
        }
    }

    private void finishMovement(MovableBoardObjectModel movableObject){
        movableObject.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.IDLE);
        if(!movableObject.isTerminated()){
            if(movableObject instanceof BoxModel)
                boxListener.setBoxBoardObjectEvent((BoxModel)movableObject, BoardObjectModel.BOARD_OBJECT_EVENT.IDLE, false);
            else
                movableObject.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.IDLE, false);
        }
    }

    private boolean checkFalls(MovableBoardObjectModel movableObject) {
        if(movableObject.canFall()) {
            boolean isFallingGround = BoardModelHelper.getFallingGround(levelModel.getCurrentBoardModel(), movableObjectStats.nextColumn, movableObjectStats.nextRow);
            if (isFallingGround)
                return isHitboxOverlapingNextPosition(movableObject);
        }
        return false;
    }

    private boolean checkDangers(MovableBoardObjectModel movableObject){
        BoardObjectModel boardObjectBase = levelModel.getCurrentBoardModel().getBoardObjectBase(movableObjectStats.nextColumn, movableObjectStats.nextRow);
        if(boardObjectBase != null && boardObjectBase.isDangerous())
            return isHitboxOverlapingNextPosition(movableObject);
        return false;
    }

    private boolean isHitboxOverlapingNextPosition(MovableBoardObjectModel movableObject){
        // Montamos un rectangulo de la celda a la que queremos ir para saber si algun vecino ha entrado en ella.
        int destinationX = (movableObjectStats.nextColumn * GameConstants.CELL_WIDTH) + GameConstants.BOARD_ORIGIN_X;
        int destinationY = (GameConstants.BOARD_ORIGIN_Y) - ((movableObjectStats.nextRow + 1) * (GameConstants.CELL_HEIGHT));
        Rectangle destinationRectangle = new Rectangle(destinationX, destinationY, GameConstants.CELL_WIDTH, GameConstants.CELL_HEIGHT);
        // Montamos el rectangulo de donde estaremos en el siguiente avance
        Rectangle futureHitBox = new Rectangle(movableObject.getPositionX() + movableObjectStats.advanceX, movableObject.getPositionY() + movableObjectStats.advanceY, GameConstants.CELL_WIDTH, GameConstants.CELL_HEIGHT);
        return futureHitBox.overlaps(destinationRectangle);
    }

    private BoardObjectModel getCollidedObject(MovableBoardObjectModel movableObject, int nextColumn, int nextRow, float advanceX, float advanceY) {

        // Buscamos todos los posibles objetos que podrian impedirnos avanzar
        BoardObjectModel neighborObject;
        List<BoardObjectModel> boardObjectsList = new ArrayList<BoardObjectModel>();

        neighborObject = BoardModelHelper.getObstacleReferenceInGrid(levelModel.getCurrentBoardModel(), nextColumn, nextRow, 1);
        if (neighborObject != null) boardObjectsList.add(neighborObject);

        if (nextColumn - 1 >= 0) {
            neighborObject = BoardModelHelper.getObstacleReferenceInGrid(levelModel.getCurrentBoardModel(), nextColumn - 1, nextRow, 1);
            if (neighborObject != null) boardObjectsList.add(neighborObject);
        }
        if (nextColumn + 1 < GameConstants.BOARD_COLUMNS) {
            neighborObject = BoardModelHelper.getObstacleReferenceInGrid(levelModel.getCurrentBoardModel(), nextColumn + 1, nextRow, 1);
            if (neighborObject != null) boardObjectsList.add(neighborObject);
        }
        if (nextRow - 1 >= 0) {
            neighborObject = BoardModelHelper.getObstacleReferenceInGrid(levelModel.getCurrentBoardModel(), nextColumn, nextRow - 1, 1);
            if (neighborObject != null) boardObjectsList.add(neighborObject);
        }
        if (nextRow + 1 < GameConstants.BOARD_ROWS) {
            neighborObject =  BoardModelHelper.getObstacleReferenceInGrid(levelModel.getCurrentBoardModel(),nextColumn, nextRow + 1, 1);
            if (neighborObject != null) boardObjectsList.add(neighborObject);
        }

        // Montamos un rectangulo de la celda a la que queremos ir para saber si algun vecino ha entrado en ella.
        int destinationX = (nextColumn * GameConstants.CELL_WIDTH) + GameConstants.BOARD_ORIGIN_X;
        int destinationY = (GameConstants.BOARD_ORIGIN_Y) - ((nextRow + 1) * (GameConstants.CELL_HEIGHT));
        Rectangle destinationRectangle = new Rectangle(destinationX, destinationY, GameConstants.CELL_WIDTH, GameConstants.CELL_HEIGHT);
        // Montamos el rectangulo de donde estaremos en el siguiente avance
        Rectangle futureHitBox = new Rectangle(movableObject.getPositionX() + advanceX, movableObject.getPositionY() + advanceY, GameConstants.CELL_WIDTH, GameConstants.CELL_HEIGHT);

        int i = 0;
        while (i < boardObjectsList.size()) {
            neighborObject = boardObjectsList.get(i);
            // No queremos comprobarnos a nosotros mismos
            if (neighborObject.getColumn() != movableObject.getColumn() || neighborObject.getRow() != movableObject.getRow()) {
                // Si un objeto con colision ha entrado en la casilla a la que intentamos entrar, devolvemos el interceptor para actuar en consecuencia
                if (neighborObject.getHitbox().overlaps(destinationRectangle) && futureHitBox.overlaps(destinationRectangle)) {
                    return neighborObject;
                }
            }
            i++;
        }
        return null;
    }

    private enum COLLISION_ACTION{
        NONE, STOP, RUN_OVER
    }
}
