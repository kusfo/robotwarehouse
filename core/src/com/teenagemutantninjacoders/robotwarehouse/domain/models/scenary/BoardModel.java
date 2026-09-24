package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.AbyssModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.WallModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseModel;

import java.util.ArrayList;

/**
 * Created by jordi.montornes on 14/01/2016.
 */
public class BoardModel {
    public int numColums;
    public int numRows;
    public CellModel[][] boardCells;

    public BoardModel() {
        this.numColums = GameConstants.BOARD_COLUMNS;
        this.numRows = GameConstants.BOARD_ROWS;
        boardCells = new CellModel[this.numColums][this.numRows];
    }

    public CellModel[][] getBoardCells(){
        return boardCells;
    }

    public void setWall(WallModel newWall, int h, int v){
        boardCells[h][v].setWall(newWall);
    }

    public void setFloor(FloorModel newFloor, int h, int v){
        boardCells[h][v].setFloor(newFloor);
    }

    public void setAbyss(AbyssModel newAbyss, int h, int v){
        boardCells[h][v].setAbyss(newAbyss);
    }

    public void setOverBase(OverBaseModel newOverBase, int h, int v){
        boardCells[h][v].setOverBase(newOverBase);
    }

    public void addBoardObjectAbove(BoardObjectModel newBoardObjectModel, int h, int v) {
        boardCells[h][v].addBoardObjectAbove(newBoardObjectModel);
    }

    public void deleteBoardObject(int h, int v, BoardObjectModel.BOARD_OBJECT_LAYER layer){
        switch(layer) {
            case ABOVE: boardCells[h][v].deleteBoardObjectAbove(); break;
            case OVERBASE: boardCells[h][v].deleteBoardObjectOverBase(); break;
        }
    }

    public BoardObjectModel getBoardObjectAbove(int h, int v){
        return boardCells[h][v].getBoardObjectAbove();
    }

    public BoardObjectModel getBoardObjectBase(int h,int v){
        return boardCells[h][v].getBoardObjectBase();
    }

    public BoardObjectModel getOverBase(int h, int v){
        return boardCells[h][v].getBoardObjectOverBase();
    }

    public boolean hasBoxAbove(CellDTO currentCellDTO){
        CellModel cellModel = boardCells[currentCellDTO.getColumn()][currentCellDTO.getRow()];
        return cellModel.hasBoxAbove();
    }

    public void changeBoardObjectPosition(BoardObjectModel boardObjectModel, int oldColumn, int  oldRow, int newColumn , int newRow){
        boardCells[oldColumn][oldRow].deleteBoardObjectAbove();
        boardCells[newColumn][newRow].addBoardObjectAbove(boardObjectModel);
    }

    public ArrayList<BoardObjectModel> getBoxesByColor(GlobalAttributes.COLOR color){
        BoardObjectModel boardObject;
        ArrayList<BoardObjectModel> objectsList = new ArrayList<BoardObjectModel>();
        for (int h = 0;h < this.numColums; h++){
            for (int v = 0;v < this.numRows; v++) {
                boardObject = boardCells[h][v].getBoardObjectAbove();
                if(boardObject != null && boardObject instanceof BoxModel){
                    if(((BoxModel)(boardObject)).getColor() ==  color) objectsList.add(boardObject);
                }
            }
        }
        return objectsList;
    }
}