package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.WallModel;

/**
 * Created by Jordi on 03/02/2016.
 */
public class CellModel {
    private CELL_EVENT event;
    private int column;
    private int row;
    private BoardObjectModel boardObjectAbove = null;
    private BoardObjectModel boardObjectBase = null;
    private BoardObjectModel boardObjectOverBase = null;

    public CellModel(int column, int row, BoardObjectModel boardObjectBase, CELL_EVENT event){
        this.boardObjectBase = boardObjectBase;
        this.event = event;
        this.column = column;
        this.row =  row;
    }

    public CELL_EVENT getEvent(){
        return event;
    }
    public void setEvent(CELL_EVENT newEvet){
        event = newEvet;
    }

    public BoardObjectModel getBoardObjectBase(){
        return boardObjectBase;
    }
    public BoardObjectModel getBoardObjectAbove(){
        return boardObjectAbove;
    }
    public BoardObjectModel getBoardObjectOverBase(){ return boardObjectOverBase;}

    public void setFloor(BoardObjectModel newBoardObjectBase){
        boardObjectBase = newBoardObjectBase;
    }
    public void setWall(BoardObjectModel newBoardObjectBase){
        boardObjectBase = newBoardObjectBase;
    }

    public void setAbyss(BoardObjectModel newBoardObjectBase) {
        boardObjectBase = newBoardObjectBase;
    }

    public void setOverBase(BoardObjectModel newOverBase){
        boardObjectOverBase = newOverBase;
    }

    public boolean hasBoxAbove(){
        if (getBoardObjectAbove() != null && getBoardObjectAbove() instanceof BoxModel)
            return true;
        return false;
    }

    public void addBoardObjectAbove(BoardObjectModel newBoardObjectAvobe){
        boardObjectAbove = newBoardObjectAvobe;
    }

    public void deleteBoardObjectAbove(){
        boardObjectAbove = null;
    }

    public void deleteBoardObjectOverBase(){
        boardObjectOverBase = null;
    }

    public enum CELL_EVENT{
        NONE, HOLE
    }
}
