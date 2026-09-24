package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.BoardModelHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by JordiRM on 25/02/2016.
 */
public class BoxManagerImpl implements BoxManager, BoxListener {
    private LevelModel levelModel;
    private BoardModel boardModel;
    private float boxGlowAlpha = 0f;
    private float boxGlowDest = 1f;
    private float spaceDockCollisionX = (2 * GameConstants.CELL_WIDTH) + GameConstants.BOARD_ORIGIN_X;

    public BoxManagerImpl(LevelModel levelModel) {
        this.levelModel = levelModel;
        this.boardModel = levelModel.getCurrentBoardModel();
        updateAllBoxesGroups(); // Configuracion inicial de grupos

    }
    @Override
    public BoxListener getBoxListener(){
        return this;
    }

    @Override
    public void update(float delta) {
        // Todas las cajas brillan al unisono, así que lo calculamos en un solo sitio
        if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING){
            boxGlowAlpha = MathUtils.lerp(boxGlowAlpha, boxGlowDest, delta * 1.5f);
            if (boxGlowAlpha > 0.6f)
                boxGlowDest = 0;
            else if (boxGlowAlpha < 0.04f) {
                boxGlowDest = 1;
            }
        }
    }

    private void updateAllBoxesGroups(){
        BoardObjectModel boardObjectModel;
        for(int h = 0;h < GameConstants.BOARD_COLUMNS; h++){
            for(int v = 0;v < GameConstants.BOARD_ROWS; v++){
                boardObjectModel = boardModel.getBoardObjectAbove(h, v);
                if(boardObjectModel != null){
                    if(boardObjectModel instanceof BoxModel){
                        updateBoxGroup((BoxModel)boardObjectModel);
                    }
                }
            }
        }
    }

    @Override
    public void updateBox(BoxModel boxModel, float delta){

        // Falling
        if (BoardModelHelper.getFallingGround(levelModel.getCurrentBoardModel(), boxModel.getColumn(), boxModel.getRow())) {
            if(!boxModel.isTerminated())
                setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);
        }

        // Gestionamos el flash local si se está produciendo
        if(boxModel.getFlashAlpha() > 0){
            boxModel.setFlashAlpha(MathUtils.clamp(boxModel.getFlashAlpha() - (4 * delta), 0.f, 1.0f));
            boxModel.getFlashSprite().setAlpha(boxModel.getFlashAlpha());
        }
        if(boxModel.getInGroup()) boxModel.setGlowAlpha(boxGlowAlpha);

        if(!boxModel.isTerminated()) {
            if (boxModel.getPositionX() < spaceDockCollisionX) {
                boxModel.setDestruction(new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.EXPLOSION)));
                getBoxListener().setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
            }
            if (boxModel.howManyRatliensAttached() > 0) {
                int obstacleLevelNecessary = GameConstants.ENEMY_OBSTACLE_LEVEL + 1;
                if (boxModel.haveRatlienAttachedUp() && boxModel.getRow() > 0) {
                    if (BoardModelHelper.getObstacleInGrid(boardModel, boxModel.getColumn(), boxModel.getRow() - 1, obstacleLevelNecessary, false, true)) {
                        destroyRatlienByCollision(boxModel, MovableBoardObjectModel.DIRECTION.UP);
                    }
                }
                if (boxModel.haveRatlienAttachedDown() && (boxModel.getRow() < GameConstants.BOARD_ROWS - 1)) {
                    if (BoardModelHelper.getObstacleInGrid(boardModel, boxModel.getColumn(), boxModel.getRow() + 1, obstacleLevelNecessary, false, true)) {
                        destroyRatlienByCollision(boxModel, MovableBoardObjectModel.DIRECTION.DOWN);
                    }
                }
                if (boxModel.haveRatlienAttachedLeft() && boxModel.getColumn() > 0) {
                    if (BoardModelHelper.getObstacleInGrid(boardModel, boxModel.getColumn() - 1, boxModel.getRow(), obstacleLevelNecessary, false, true)) {
                        destroyRatlienByCollision(boxModel, MovableBoardObjectModel.DIRECTION.LEFT);
                    }
                }
                if (boxModel.haveRatlienAttachedRight() && (boxModel.getColumn() < GameConstants.BOARD_COLUMNS - 1)) {
                    if (BoardModelHelper.getObstacleInGrid(boardModel, boxModel.getColumn() + 1, boxModel.getRow(), obstacleLevelNecessary, false, true)) {
                        destroyRatlienByCollision(boxModel, MovableBoardObjectModel.DIRECTION.RIGHT);
                    }
                }
            }

            checkBoxHealth(boxModel, delta);
        }
    }

    @Override
    public void setBoxBoardObjectEvent(BoxModel boxModel, BoardObjectModel.BOARD_OBJECT_EVENT event, boolean isTerminated){
        boxModel.setEvent(event, isTerminated);
        updateBoxGroup(boxModel);
        updateNeighboursBoxesGroup(boxModel.getColumn(), boxModel.getRow(), boxModel.getColor());
    }

    @Override
    public void updateBoxGroup(BoxModel boxModel) {
        if (boxModel.getColor() == GlobalAttributes.COLOR.NONE) {
            return;
        }

        // Si comprobamos que la caja esta infestada la sacamos del grupo y salimos
        if(boxModel.getInGroup()) {
            if (boxModel.howManyRatliensAttached() > 0) {
                boxModel.softFlash();
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_GROUP_LEAVE);
                boxModel.setInGroup(false);
                boxModel.setSameAdjacentBoxesAmount(0);
                return;
            }
        }

        if(boxModel.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE) {
            int nSameBoxes = 0;
            BoxModel adjacentBox;
            if(boxModel.getColumn() - 1 >= 0) {
                adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn() - 1, boxModel.getRow()), boxModel.getColor());
                boxModel.setBoxLeft(adjacentBox);
                if (adjacentBox != null) {
                    nSameBoxes++;
                }
            }
            if(boxModel.getColumn() + 1 < GameConstants.BOARD_COLUMNS) {
                adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn() + 1, boxModel.getRow()), boxModel.getColor());
                boxModel.setBoxRight(adjacentBox);
                if (adjacentBox != null) {
                    nSameBoxes++;
                }
            }
            if(boxModel.getRow() - 1 >= 0) {
                adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn(), boxModel.getRow() - 1), boxModel.getColor());
                boxModel.setBoxUp(adjacentBox);
                if (adjacentBox != null) {
                    nSameBoxes++;
                }
            }
            if(boxModel.getRow() + 1 < GameConstants.BOARD_ROWS) {
                adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn(), boxModel.getRow() + 1), boxModel.getColor());
                boxModel.setBoxDown(adjacentBox);
                if (adjacentBox != null) {
                    nSameBoxes++;
                }
            }

            // Si el número de vecinos ha cambiado, avisamos a los vecinos restantes para que lo tomen en cuenta
            if (boxModel.getSameAdjacentBoxesAmount() != nSameBoxes) {
                boxModel.setSameAdjacentBoxesAmount(nSameBoxes);
                updateNeighboursBoxesGroup(boxModel.getColumn(), boxModel.getRow(), boxModel.getColor());
            }

            updateGroupState(boxModel);
        } else {
            // Gestionamos la perdida de grupo y el efecto de desconexion
            if(boxModel.getInGroup()) {
                switch (boxModel.getEvent()) {
                    case FALLING: case MOVING:
                        boxModel.softFlash();
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_GROUP_LEAVE);
                        boxModel.setInGroup(false);
                        boxModel.setSameAdjacentBoxesAmount(0);
                        break;
                    case DESTROYING: case ERASING:
                        boxModel.setInGroup(false);
                        boxModel.setSameAdjacentBoxesAmount(0);
                        break;
                }
            }
        }
    }

    @Override
    public void updateNeighboursBoxesGroup(int h, int v, GlobalAttributes.COLOR color) {
        BoxModel adjacentBox;
        if(h - 1 >= 0) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(h - 1, v), color);
            if (adjacentBox != null) {
                updateBoxGroup(adjacentBox);
            }
        }
        if(h + 1 < GameConstants.BOARD_COLUMNS) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(h + 1, v), color);
            if (adjacentBox != null) {
                updateBoxGroup(adjacentBox);
            }
        }
        if(v - 1 >= 0) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(h, v - 1), color);
            if (adjacentBox != null) {
                updateBoxGroup(adjacentBox);
            }
        }
        if(v + 1 < GameConstants.BOARD_ROWS) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(h, v + 1), color);
            if (adjacentBox != null) {
                updateBoxGroup(adjacentBox);
            }
        }
    }

    private void destroyRatlienByCollision(BoxModel boxModel, MovableBoardObjectModel.DIRECTION direction) {
        boxModel.killRatlien(direction, true);
        updateBoxGroup(boxModel);
        updateNeighboursBoxesGroup(boxModel.getColumn(), boxModel.getRow(), boxModel.getColor());
        GlobalLevelData.getInstance().subtractRatlien();
        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
    }

    private void checkBoxHealth(BoxModel boxModel, float delta){
        if(boxModel.howManyRatliensAttached() > 0) {

            if (boxModel.isTimeToBeChewed(delta)) {
                boxModel.makeRatlienChew();
            }
            if (boxModel.isBoxDead()) {
                reintroduceBoxRatliensOnBoard(boxModel);
                DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.CHEWED));
                boxModel.setDestruction(destructionDTO);
                setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
            }
        }
    }

    private void reintroduceBoxRatliensOnBoard(BoxModel boxModel){
        int ratliensWaiting = boxModel.howManyRatliensAttached();
        if(boxModel.haveRatlienAttachedUp()){
            if(boxModel.getRow() -1 >= 0  && BoardModelHelper.isCellFree(boardModel, boxModel.getColumn(), boxModel.getRow() - 1, false, false) ){
                levelModel.addNewRatlien(boxModel.getColumn(), boxModel.getRow() - 1, MovableBoardObjectModel.DIRECTION.UP, RatlienModel.ENTRY_TYPE.BOX);
                ratliensWaiting -=1;
            }
        }
        if(boxModel.haveRatlienAttachedDown()){
            if(boxModel.getRow() + 1 < GameConstants.BOARD_ROWS && BoardModelHelper.isCellFree(boardModel, boxModel.getColumn(), boxModel.getRow() + 1, false, false) ){
                levelModel.addNewRatlien(boxModel.getColumn(), boxModel.getRow() + 1, MovableBoardObjectModel.DIRECTION.DOWN, RatlienModel.ENTRY_TYPE.BOX);
                ratliensWaiting -=1;
            }
        }
        if(boxModel.haveRatlienAttachedLeft()){
            if(boxModel.getColumn() -1 >= 0 && BoardModelHelper.isCellFree(boardModel, boxModel.getColumn() - 1, boxModel.getRow(), false, false) ){
                levelModel.addNewRatlien(boxModel.getColumn() - 1, boxModel.getRow() , MovableBoardObjectModel.DIRECTION.LEFT, RatlienModel.ENTRY_TYPE.BOX);
                ratliensWaiting -=1;
                }
        }
        if(boxModel.haveRatlienAttachedRight()){
            if(boxModel.getColumn() + 1 < GameConstants.BOARD_COLUMNS && BoardModelHelper.isCellFree(boardModel, boxModel.getColumn() + 1, boxModel.getRow(), false, false) ){
                levelModel.addNewRatlien(boxModel.getColumn() + 1, boxModel.getRow() , MovableBoardObjectModel.DIRECTION.RIGHT, RatlienModel.ENTRY_TYPE.BOX);
                ratliensWaiting -=1;
            }
        }
        // Si algun ratlien se queda si espacio, al menos uno ocupará la celda donde estaba la caja
        if(ratliensWaiting > 0){
            levelModel.addNewRatlien(boxModel.getColumn(), boxModel.getRow() , MovableBoardObjectModel.DIRECTION.DOWN, RatlienModel.ENTRY_TYPE.NONE);
            ratliensWaiting -=1;
            if(ratliensWaiting > 0){
                // Los ratliens que no hemos podido colocar se eliminan
                GlobalLevelData.getInstance().subtractRatlien(ratliensWaiting);
            }
        }
    }

    private void updateGroupState(BoxModel boxModel) {
        if (boxModel.getColor() == GlobalAttributes.COLOR.NONE) {
            return;
        }
        BoxModel neighbourBox;
        int neighbourAdjacentsBoxes = 0;

        neighbourBox = boxModel.getBoxUp();
        if (neighbourBox != null) {
            neighbourAdjacentsBoxes += neighbourBox.getSameAdjacentBoxesAmount();
        }
        neighbourBox = boxModel.getBoxDown();
        if (neighbourBox != null) {
            neighbourAdjacentsBoxes += neighbourBox.getSameAdjacentBoxesAmount();
        }
        neighbourBox = boxModel.getBoxRight();
        if (neighbourBox != null) {
            neighbourAdjacentsBoxes += neighbourBox.getSameAdjacentBoxesAmount();
        }
        neighbourBox = boxModel.getBoxLeft();
        if (neighbourBox != null) {
            neighbourAdjacentsBoxes += neighbourBox.getSameAdjacentBoxesAmount();
        }

        if (boxModel.getSameAdjacentBoxesAmount() + neighbourAdjacentsBoxes >= 3) {
            if(!boxModel.getInGroup()){
                if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                    // La caja que desencadena la creacion del grupo es la que usamos para lanzar el sonido
                    if (!boxModel.isFlashing())
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_GROUP_CREATION);
                    flashBoxGroup(boxModel);
                }
            }
            boxModel.setInGroup(true);
        } else {
            if(boxModel.getInGroup()) boxModel.softFlash();
            boxModel.setInGroup(false);
        }
    }

    private void flashBoxGroup(BoxModel boxModel){
        BoxModel adjacentBox;
        boxModel.Flash();
        if(boxModel.getColumn() - 1 >= 0) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn() - 1, boxModel.getRow()), boxModel.getColor());
            if (adjacentBox != null) {
                if (!adjacentBox.isFlashing()) {
                    flashBoxGroup(adjacentBox);
                }
            }
        }
        if(boxModel.getColumn() + 1 < GameConstants.BOARD_COLUMNS) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn() + 1, boxModel.getRow()), boxModel.getColor());
            if (adjacentBox != null) {
                if (!adjacentBox.isFlashing()) {
                    flashBoxGroup(adjacentBox);
                }
            }
        }
        if(boxModel.getRow() - 1 >= 0) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn(), boxModel.getRow() - 1), boxModel.getColor());
            if (adjacentBox != null) {
                if (!adjacentBox.isFlashing()) {
                    flashBoxGroup(adjacentBox);
                }
            }
        }
        if(boxModel.getRow() + 1 < GameConstants.BOARD_ROWS) {
            adjacentBox = getPosibleValidBox(boardModel.getBoardObjectAbove(boxModel.getColumn(), boxModel.getRow() + 1), boxModel.getColor());
            if (adjacentBox != null) {
                if (!adjacentBox.isFlashing()) {
                    flashBoxGroup(adjacentBox);
                }
            }
        }
    }

    private BoxModel getPosibleValidBox(BoardObjectModel boardObjectModel, GlobalAttributes.COLOR color) {
        if (boardObjectModel != null) {
            if (boardObjectModel instanceof BoxModel) {
                BoxModel boxModel = (BoxModel) boardObjectModel;
                if (boxModel.howManyRatliensAttached() == 0 && boxModel.getColor() == color) {
                    switch (boardObjectModel.getEvent()){
                        case IDLE: case TELEPORTING:
                            return boxModel;
                    }
                }
            }
        }
        return null;
    }
}