package com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies;

import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;

/**
 * Created by JordiRM on 04/03/2020.
 */
public class CannonStopDTO {
    private CellDTO cell;
    private int jumps;

    public CellDTO getCell() {
        return cell;
    }

    public void setCell(CellDTO cell) {
        this.cell = cell;
    }

    public int getJumps() {
        return jumps;
    }

    public void setJumps(int jumps) {
        this.jumps = jumps;
    }
}
