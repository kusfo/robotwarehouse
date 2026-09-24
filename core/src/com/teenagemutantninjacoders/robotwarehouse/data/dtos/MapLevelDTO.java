package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;

import java.util.ArrayList;

/**
 * Created by JordiRM on 12/07/2018.
 */
public class MapLevelDTO {
    private int levelNumber;
    private int cellH;
    private int cellV;
    private String type;
    private int starsNeeded;
    private boolean challenge = false;
    private String unblockingDialog = "";
    private ArrayList<CellDTO> unblockingLevels;

    public MapLevelDTO(){
        unblockingLevels = new ArrayList<CellDTO>();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getCellH() {
        return cellH;
    }

    public void setCellH(int cellH) {
        this.cellH = cellH;
    }

    public int getCellV() {
        return cellV;
    }

    public void setCellV(int cellV) {
        this.cellV = cellV;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStarsNeeded() {
        return starsNeeded;
    }

    public void setStarsNeeded(int starsNeeded) {
        this.starsNeeded = starsNeeded;
    }

    public boolean hasChallenge() {
        return challenge;
    }

    public String getUnblockingDialog() {
        return unblockingDialog;
    }

    public void setUnblockingDialog(String unblockingDialog) {
        this.unblockingDialog = unblockingDialog;
    }

    public void setChallenge(boolean challenge) {
        this.challenge = challenge;
    }

    public ArrayList<CellDTO> getUnblockingLevels() {
        return unblockingLevels;
    }

    public void setUnblockingLevels(ArrayList<CellDTO> unblockingLevels) {
        this.unblockingLevels = unblockingLevels;
    }
}
