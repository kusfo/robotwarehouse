package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

/**
 * Created by JordiRM on 10/06/2019.
 */
public class DecorationDTO {
    private int cellH;
    private int cellV;
    private int decoration;

    public DecorationDTO() {}

    public DecorationDTO(int cellH, int cellV, int decoration) {
        this.cellH = cellH;
        this.cellV = cellV;
        this.decoration = decoration;
    }

    public int getCellH() {
        return cellH;
    }

    public int getCellV() {
        return cellV;
    }

    public int getDecoration() {
        return decoration;
    }

}
