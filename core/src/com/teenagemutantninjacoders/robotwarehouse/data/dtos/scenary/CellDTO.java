package com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary;

/**
 * Created by JordiRM on 16/02/2016.
 */
public class CellDTO {
    private int column;
    private int row;
    public CellDTO(){}
    public CellDTO(int column, int row){
        this.column = column;
        this.row = row;
    }

    public int getColumn(){
        return column;
    }
    public int getRow(){
        return row;
    }
    public void setColumn(int column){
        this.column = column;
    }
    public void setRow(int row){
        this.row = row;
    }
}
