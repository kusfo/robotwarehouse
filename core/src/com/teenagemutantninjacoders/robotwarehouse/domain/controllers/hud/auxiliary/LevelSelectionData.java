package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;

import java.util.ArrayList;

/**
 * Created by JordiRM on 02/08/2018.
 */
public class LevelSelectionData {
    public int currentEpisode;
    private EpisodeDTO episodeDTO;
    private GridLevelButton[][] gridLevelButtons;
    private int gridColumns, gridRows;
    public class GridLevelButton{
        public int levelNumber;
        public Actor buttonGroup;
        public GridLevelButton(int levelNumber, Actor buttonGroup){
            this.levelNumber = levelNumber;
            this.buttonGroup = buttonGroup;
        }
    }
    public class LevelAddingStars{
        private int actualizedLevelNumber;
        private int starsAdded;
        private int blockedLevel;
        public LevelAddingStars(int actualizedLevelNumber, int blockedLevel, int starsAdded){
            this.actualizedLevelNumber = actualizedLevelNumber;
            this.blockedLevel = blockedLevel;
            this.starsAdded = starsAdded;
        }
        public int getActualizedLevelNumber(){
            return actualizedLevelNumber;
        }
        public int getStarsAdded(){
            return starsAdded;
        }
        public int getBlockedLevel(){
            return blockedLevel;
        }
    }

    private ArrayList<Integer> levelsNeededActualization;
    private ArrayList<Integer> levelsActualized;
    private ArrayList<Integer> levelsWaitingToUnblock;
    private ArrayList<LevelAddingStars> levelsAddingStars;

    public Vector2 rectangleFocusMin = new Vector2();
    public Vector2 rectangleFocusMax = new Vector2();

    public int levelWidth = 88;
    public int levelHeight = 96;
    public int levelSpaceH = 98;
    public int levelSpaceV = 85;

    public LevelSelectionData(){
        // Arrays para la actualización segura de niveles
        levelsNeededActualization = new ArrayList<Integer>();
        levelsActualized = new ArrayList<Integer>();
        levelsWaitingToUnblock = new ArrayList<Integer>();
        levelsAddingStars = new ArrayList<LevelAddingStars>();
        rectangleFocusMin.setZero();
        rectangleFocusMax.setZero();
    }

    public void InitializeData(int episodeNumber){
        currentEpisode = episodeNumber;
        episodeDTO = GlobalGeneralData.getInstance().getEpisodeDataByNumber(currentEpisode);
        gridColumns = episodeDTO.getColumns();
        gridRows = episodeDTO.getRows();
        gridLevelButtons = new GridLevelButton[gridColumns][gridRows];

        levelsNeededActualization.clear();
        levelsActualized.clear();
        levelsWaitingToUnblock.clear();
        levelsAddingStars.clear();
    }

    public void setEpisodeDTO(EpisodeDTO episodeDTO){
        this.episodeDTO = episodeDTO;
    }
    public EpisodeDTO getEpisodeDTO() {
        return episodeDTO;
    }

    public int getGridLevelColumns(){
        return gridColumns;
    }
    public int getGridLevelRows(){
        return gridRows;
    }

    public void setGridLevelButton(int column, int row, int levelNumber, Actor levelButton){
        gridLevelButtons[column][row] = new GridLevelButton(levelNumber, levelButton);
    }
    public GridLevelButton getGridLevelButton(int column, int row){
        return gridLevelButtons[column][row];
    }

    public ArrayList<Integer> getLevelsWaitingToUnblock() {
        return levelsWaitingToUnblock;
    }

    public ArrayList<Integer> getLevelsNeededActualization() {
        return levelsNeededActualization;
    }

    public ArrayList<Integer> getLevelsActualized() {
        return levelsActualized;
    }

    public void addLevelAddingStars(int actualizedLevelNumber, int blockedLevelNumber, int starsAdded){
        levelsAddingStars.add(new LevelAddingStars(actualizedLevelNumber, blockedLevelNumber, starsAdded));
    }

    public ArrayList<LevelAddingStars> getLevelsAddingStars(){
        return levelsAddingStars;
    }
}
