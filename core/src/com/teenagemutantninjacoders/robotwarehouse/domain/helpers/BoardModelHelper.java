package com.teenagemutantninjacoders.robotwarehouse.domain.helpers;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.AbyssModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorTrapDoorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.CellModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ceated by jordimontornes on 30/08/2016.
 */
public class BoardModelHelper {

    public static BoardModel initializeBoardModel(BoardModel boardModel) {
        // Inicialmente llenamos de OFFBOARDS para tener una base
        for (int h = 0;h < boardModel.numColums; h++){
            for (int v = 0;v < boardModel.numRows; v++) {
                newCell(boardModel, h, v, null, CellModel.CELL_EVENT.NONE);
            }
        }
        return boardModel;
    }

    private static void newCell(BoardModel boardModel, int h, int v, BoardObjectModel boardObjectModelBase, CellModel.CELL_EVENT event){
        boardModel.boardCells[h][v] = new CellModel(h, v, boardObjectModelBase, event);
    }

    public static MovableBoardObjectModel.DIRECTION getTargetDirection (BoardModel boardModel, int h, int v, GlobalAttributes.ACTION_ON_TARGET action){
        ArrayList <MovableBoardObjectModel.DIRECTION> directions = new ArrayList <MovableBoardObjectModel.DIRECTION>
                (Arrays.asList(MovableBoardObjectModel.DIRECTION.values()));
        Collections.shuffle(directions);
        for(MovableBoardObjectModel.DIRECTION direction : directions) {
            BoardObjectModel neighbourBoardObject = getNeighbourBoardObject(boardModel, h, v, direction);
            if (neighbourBoardObject instanceof BoxModel) {
                switch(action){
                    case PUSH:
                        BoxModel boxModel = (BoxModel) neighbourBoardObject;
                        if(boxModel.isTargetable()) {
                            // Comprobamos si hay un obstaculo detras de la caja para saber si se puede desplazar en esa dirección
                            if (!getObstacleInDirection(boardModel, boxModel.getColumn(), boxModel.getRow(), direction, boxModel.getObstacleLevel(), false, false)) {
                                return direction;
                            }
                        }
                        break;
                    case EXPLODE:
                        return direction;
                    case INFEST:
                        if(((BoxModel) neighbourBoardObject).canAttachRatlien(direction))
                            return direction;
                        break;
                }
            }
        }
        return MovableBoardObjectModel.DIRECTION.NEUTRAL;
    }

    public static List<BoardObjectModel> getAllNeighboursBoardObjects(BoardModel boardModel, int h, int v){
        List<BoardObjectModel> result = new ArrayList<BoardObjectModel>();
        for (MovableBoardObjectModel.DIRECTION direction : MovableBoardObjectModel.DIRECTION.values()) {
            BoardObjectModel neighbourBoardObject = getNeighbourBoardObject(boardModel, h, v, direction);
            if(neighbourBoardObject != null) {
                result.add(neighbourBoardObject);
            }
        }
        return result;
    }

    public static BoardObjectModel getNeighbourBoardObject(BoardModel boardModel, int h, int v,
                                                           MovableBoardObjectModel.DIRECTION direction){
        switch (direction){
            case UP:
                if(v != 0){
                    return boardModel.getBoardObjectAbove(h, v - 1);
                } else {
                    return null;
                }
            case LEFT:
                if(h != 0){
                    return boardModel.getBoardObjectAbove(h - 1, v);
                } else {
                    return null;
                }
            case RIGHT:
                if(h != boardModel.numColums-1){
                    return boardModel.getBoardObjectAbove(h + 1, v);
                } else {
                    return null;
                }
            case DOWN:
                if(v != boardModel.numRows - 1){
                    return boardModel.getBoardObjectAbove(h, v + 1);
                } else {
                    return null;
                }
            default:
                return boardModel.getBoardObjectAbove(h, v);
        }
    }

    public static boolean getObstacleInDirection(BoardModel boardModel, int column, int row, MovableBoardObjectModel.DIRECTION directionAsked,
                                                 int obstacleLevel, boolean fallIsObstacle, boolean avoidDangers) {
        int incrementor;
        switch (directionAsked) {
            case LEFT:
            case RIGHT:
                if (directionAsked == MovableBoardObjectModel.DIRECTION.LEFT) incrementor = -1; else incrementor = 1;
                if (column + incrementor > 0 && column + incrementor <= boardModel.numColums - 1)
                    return getObstacleInGrid(boardModel, column + incrementor, row, obstacleLevel, fallIsObstacle, avoidDangers);

            case UP:
            case DOWN:
                if (directionAsked == MovableBoardObjectModel.DIRECTION.UP) incrementor = -1; else incrementor = 1;
                if (row + incrementor > 0 && row + incrementor <= boardModel.numRows - 1) {
                    return getObstacleInGrid(boardModel, column, row + incrementor, obstacleLevel, fallIsObstacle, avoidDangers);
                }
        }
        return false;
    }

    public static boolean getObstacleInGrid(BoardModel boardModel, int column, int row, int obstacleLevel, boolean fallIsObstacle, boolean avoidDangers){
        BoardObjectModel boardObject;
        // Base
        boardObject = boardModel.getBoardObjectBase(column, row);
        if(boardObject != null && boardObject.getObstacleLevel() >= obstacleLevel) return true;
        if(boardObject != null && boardObject.isDangerous() && avoidDangers) return true;
        // Above
        boardObject = boardModel.getBoardObjectAbove(column, row);
        if(boardObject != null && boardObject.getObstacleLevel() >= obstacleLevel) return true;
        // Falls
        if(fallIsObstacle && getFallingGround(boardModel, column, row)) return true;

        return false;
    }

    public static BoardObjectModel getObstacleReferenceInGrid(BoardModel boardModel, int column, int row, int obstacleLevel){
        // Base
        BoardObjectModel boardObject =  boardModel.getBoardObjectBase(column, row);
        if(boardObject != null && boardObject.getObstacleLevel() >= obstacleLevel) return boardObject;
        // Above
        boardObject = boardModel.getBoardObjectAbove(column, row);
        if(boardObject != null && boardObject.getObstacleLevel() >= obstacleLevel) return boardObject;

        return null;
    }

    public static boolean isCellFree(BoardModel boardModel, int h, int v, boolean fallIsObstacle, boolean dangerIsObstacle){
        BoardObjectModel boardObject;
        boardObject = boardModel.getBoardObjectBase(h, v);
        if(boardObject != null){
            if(boardObject.getObstacleLevel() > 0) return false;
            if(dangerIsObstacle && boardObject.isDangerous()) return false;
        }
        if(fallIsObstacle && getFallingGround(boardModel, h, v)) return false;
        boardObject = boardModel.getBoardObjectAbove(h, v);
        if(boardObject != null )  return false;

        return true;
    }

    public static boolean getFallingGround(BoardModel boardModel, int h, int v) {
        BoardObjectModel boardObjectBase = boardModel.getBoardObjectBase(h, v);
        if (boardObjectBase == null || boardObjectBase instanceof AbyssModel) {
            return true;
        } else if( boardObjectBase instanceof FloorTrapDoorModel) {
            return ((FloorTrapDoorModel) boardObjectBase).getFloorStatus() == FloorTrapDoorModel.FLOOR_STATUS.OPEN;
        }
        return false;
    }
}
