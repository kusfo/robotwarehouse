package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.MapLevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;

import java.util.ArrayList;

/**
 * Created by JordiRM on 02/08/2018.
 */
public class LevelSelectionHelper {

    private GlobalPreferencesData gameData;
    private LevelSelectionData levelsData;
    public LevelSelectionHelper(LevelSelectionData levelsData){
        this.levelsData = levelsData;
        gameData = GlobalPreferencesData.getInstance();
    }

    public void loadLevelsMap(int episode){
        levelsData.InitializeData(episode);
    }

    public ArrayList<LevelSelectionData.GridLevelButton> getAdjacentLevelButtons(int column, int row){
        ArrayList<LevelSelectionData.GridLevelButton> adjacentButtonGroup = new ArrayList<LevelSelectionData.GridLevelButton>();
        int gridColumns = levelsData.getGridLevelColumns();
        int gridRows = levelsData.getGridLevelRows();
        // Comun
        if(column > 0){
            if(levelsData.getGridLevelButton(column - 1, row) != null){
                //Gdx.app.log("", "LEFT");
                adjacentButtonGroup.add(levelsData.getGridLevelButton(column - 1, row));
            }
        }
        if(column < gridColumns - 1){
            if(levelsData.getGridLevelButton(column + 1,row) != null){
                //Gdx.app.log("", "RIGHT");
                adjacentButtonGroup.add(levelsData.getGridLevelButton(column + 1,row));
            }
        }

        // Linea Par
        if(row % 2 == 0) {
            //UP_LEFT
            if (row > 0) {
                if (levelsData.getGridLevelButton(column,row - 1) != null) {
                    //Gdx.app.log("", "UP_LEFT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column, row - 1));
                }
            }
            //UP_RIGHT
            if (column < gridColumns - 1 && row > 0) {
                if (levelsData.getGridLevelButton(column + 1,row - 1) != null) {
                    //Gdx.app.log("", "UP RIGHT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column + 1,row - 1));
                }
            }
            // DOWN_LEFT
            if (row < gridRows - 1) {
                if (levelsData.getGridLevelButton(column, row +1) != null) {
                    //Gdx.app.log("", "DOWN LEFT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column, row +1));
                }
            }
            // DOWN_RIGHT
            if (column < gridColumns - 1 && row < gridRows - 1) {
                if (levelsData.getGridLevelButton(column + 1, row + 1) != null) {
                    //Gdx.app.log("", "DOWN RIGHT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column + 1, row + 1));
                }
            }
        }else{
            //UP_LEFT
            if (column > 0 && row > 0) {
                if (levelsData.getGridLevelButton(column - 1, row - 1) != null) {
                    //Gdx.app.log("", "UP_LEFT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column - 1, row - 1));
                }
            }
            //UP_RIGHT
            if (row > 0) {
                if (levelsData.getGridLevelButton(column, row - 1) != null) {
                    //Gdx.app.log("", "UP RIGHT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column, row - 1));
                }
            }
            // DOWN_LEFT
            if (column > 0 && row < gridRows - 1) {
                if (levelsData.getGridLevelButton(column - 1, row + 1) != null) {
                    //Gdx.app.log("", "DOWN LEFT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column - 1, row + 1));
                }
            }
            // DOWN_RIGHT
            if (row < gridRows - 1) {
                if (levelsData.getGridLevelButton(column, row + 1) != null) {
                    //Gdx.app.log("", "DOWN RIGHT");
                    adjacentButtonGroup.add(levelsData.getGridLevelButton(column, row + 1));
                }
            }
        }
        return adjacentButtonGroup;
    }

    public void tryToUnblockLevels(int levelNumberToActualize) {
        int levelIndex = Utils.getLevelIndexByLevelNumber(levelsData.currentEpisode, levelNumberToActualize);
        int column = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellV();

        // Primero intentamos desbloquear el propio nivel si es necesario (special_challenge)
        if (gameData.getLevelStatus(levelNumberToActualize, levelsData.currentEpisode) ==  GameConstants.CONTENT_STATUS_BLOCKED) {
            addLevelToUnblock(levelNumberToActualize);
        }

        // Buscamos niveles adyacentes para preguntarles si pueden desbloquearse
        ArrayList<LevelSelectionData.GridLevelButton> adjacentButtonGroup = getAdjacentLevelButtons(column, row);
        for (int i = 0; i < adjacentButtonGroup.size(); i++) {
            int adjacentLevelNumber = adjacentButtonGroup.get(i).levelNumber;
            int adjacentLevelIndex =Utils.getLevelIndexByLevelNumber(levelsData.currentEpisode, adjacentLevelNumber);
            boolean hasUnblockingLevelsDone;
            boolean blockedByStar = false;

            // Solo comprobamos los niveles que estén bloqueados
            if (gameData.getLevelStatus(adjacentButtonGroup.get(i).levelNumber, levelsData.currentEpisode) ==  GameConstants.CONTENT_STATUS_BLOCKED) {
                // Si es un nivel bloqueado por una estrella nos centramos en si puede restarse estrellas
                if(levelsData.getEpisodeDTO().getLevels().get(adjacentLevelIndex).getType().equals("stars")){
                    int starsAchieved = getStarLevelAccumulatedStars(adjacentLevelNumber);
                    int starsNeeded = levelsData.getEpisodeDTO().getLevels().get(adjacentLevelIndex).getStarsNeeded();
                    if(starsAchieved < starsNeeded){
                        // Actualizamos el valor por si hay que sumar estrellas
                        processStarLevelWithActualizedLevel(adjacentLevelNumber, levelNumberToActualize, column, row);
                        blockedByStar = true;
                    }
                }
                if(!blockedByStar) {
                    hasUnblockingLevelsDone = areUnblockingLevelsListDone(adjacentButtonGroup.get(i).levelNumber);
                    if (hasUnblockingLevelsDone) {
                        addLevelToUnblock(adjacentButtonGroup.get(i).levelNumber);
                    }
                }
            }
        }
    }

    public void addLevelToUnblock(int levelNumber){
        levelsData.getLevelsWaitingToUnblock().add(levelNumber);
        processLevelFocus(levelNumber);
    }

    public boolean areUnblockingLevelsListDone(int levelNumber) {
        // Revisamos este nivel adyacente para ver si sus niveles adyacentes le permiten desbloquearse
        int levelIndex = Utils.getLevelIndexByLevelNumber(levelsData.currentEpisode, levelNumber);
        ArrayList<CellDTO> unblockingLevelsList = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getUnblockingLevels();
        // Los niveles aislados los saltamos
        if(unblockingLevelsList.size() == 0) return false;
        boolean unblockLevelsCompleted = true;
        if (unblockingLevelsList.size() > 0) {
            for (int n = 0; n < unblockingLevelsList.size(); n++) {
                int unblockingColumn = unblockingLevelsList.get(n).getColumn();
                int unblockingRow = unblockingLevelsList.get(n).getRow();
                int unblockingLevelNumber = levelsData.getGridLevelButton(unblockingColumn, unblockingRow).levelNumber;

                // Si esta completo seguimos mirando y ademas contamos sus estrellas por si es necesario
                if (gameData.getLevelStatus(unblockingLevelNumber, levelsData.currentEpisode) != GameConstants.CONTENT_STATUS_COMPLETED) {
                    unblockLevelsCompleted = false;
                    break;
                }
            }
        }
        return unblockLevelsCompleted;
    }

    private void processStarLevelWithActualizedLevel(int blockedLevelNumber, int levelNumber, int column, int row){
        int bloquedLevelIndex = Utils.getLevelIndexByLevelNumber(levelsData.currentEpisode, blockedLevelNumber);
        ArrayList<CellDTO> unblockingLevelsList = levelsData.getEpisodeDTO().getLevels().get(bloquedLevelIndex).getUnblockingLevels();
        if (unblockingLevelsList.size() > 0) {
            // Recopilamos las estrellas guardadas de los niveles que necesita.
            ArrayList<Integer> savedLevelsStars = gameData.unmountLevelStarProgressKey(gameData.getStarLevelProgress(blockedLevelNumber,
                    levelsData.currentEpisode), unblockingLevelsList);
            int index;
            for (index = 0; index < unblockingLevelsList.size(); index++) {
                // Encontramos la posicion en la lista donde esta el nivel que se ha actualizado
                if(unblockingLevelsList.get(index).getColumn() == column && unblockingLevelsList.get(index).getRow() == row)
                    break;
            }
            // Comparamos las estrellas guardadas con las actualizadas por si tenemos que sumar
            int actualizedLevelStars = gameData.getLevelStars(levelNumber, levelsData.currentEpisode);
            if(actualizedLevelStars > savedLevelsStars.get(index)){

                // Guardamos en una petición la suma que hay que hacer
                levelsData.addLevelAddingStars(levelNumber, blockedLevelNumber, actualizedLevelStars - savedLevelsStars.get(index));

                // Actualizamos las estrellas en el array
                savedLevelsStars.set(index, actualizedLevelStars);
            }
            // Guardamos el key de valores en el preferences
            gameData.setStarLevelProgress(blockedLevelNumber, levelsData.currentEpisode, savedLevelsStars);
        }
    }

    public int getStarLevelAccumulatedStars(int numLevel){
        int stars = 0;
        String key = gameData.getStarLevelProgress(numLevel, levelsData.currentEpisode);
        if(!key.equals("")) {
            for (int i = 0; i < key.length(); i++) {
                stars += Integer.parseInt(key.substring(i, MathUtils.clamp(i + 1, 0, key.length())));
            }
        }
        return stars;
    }

    private void processLevelFocus(int levelNumber){
        Actor buttonGroup = getButtonGroupByLevelNumber(levelNumber);

        // Procesamos el focus de este nivel
        int centerX = MathUtils.floor(buttonGroup.getX() + (levelsData.levelWidth / 2));
        int centerY = MathUtils.floor(buttonGroup.getY() + (levelsData.levelHeight / 2));
        if(levelsData.rectangleFocusMin.isZero() && levelsData.rectangleFocusMax.isZero()){
            levelsData.rectangleFocusMin.x = centerX;
            levelsData.rectangleFocusMin.y = centerY;
            levelsData.rectangleFocusMax.x = centerX;
            levelsData.rectangleFocusMax.y = centerY;
        } else {
            if (centerX < levelsData.rectangleFocusMin.x) {
                levelsData.rectangleFocusMin.x = centerX;
            } else if (centerX > levelsData.rectangleFocusMax.x) {
                levelsData.rectangleFocusMax.x = centerX;
            }
            if (centerY < levelsData.rectangleFocusMin.y) {
                levelsData.rectangleFocusMin.y = centerY;
            } else if (centerY > levelsData.rectangleFocusMax.y) {
                levelsData.rectangleFocusMax.y = centerY;
            }
        }
    }

    public Actor getButtonGroupByLevelNumber(int levelNumber){
        int levelIndex = Utils.getLevelIndexByLevelNumber(levelsData.currentEpisode, levelNumber);
        int column = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellV();
        return levelsData.getGridLevelButton(column, row).buttonGroup;
    }

    public void setLinkPosition(Actor link, int column, int row, int destinyColumn, int destinyRow, float levelsGroupHeight) {
        Vector2 originCenter = getLevelCenterPosition(column, row, levelsGroupHeight);
        Vector2 destinyCenter = getLevelCenterPosition(destinyColumn, destinyRow, levelsGroupHeight);

        // Position
        Vector2 center = new Vector2();
        center.x = originCenter.x + ((destinyCenter.x - originCenter.x) / 2);
        center.y = originCenter.y + ((destinyCenter.y - originCenter.y) / 2);
        link.setPosition(center.x, center.y);

        // Rotation
        double dx = originCenter.x - destinyCenter.x;
        double dy = originCenter.y - destinyCenter.y;
        float rot = (float) Math.toDegrees(Math.atan2(dy, dx));
        link.setRotation(rot);
    }

    private Vector2 getLevelCenterPosition(int column, int row, float levelsGroupHeight) {
        int centerX = MathUtils.floor(levelsData.levelSpaceH / 2) + (column * levelsData.levelSpaceH);
        int centerY = MathUtils.floor(levelsGroupHeight - levelsData.levelSpaceV) - (row * levelsData.levelSpaceV) + 1;
        if (row % 2 == 0) centerX += levelsData.levelSpaceH;
        else centerX += MathUtils.floor(levelsData.levelSpaceH / 2);

        return new Vector2(centerX, centerY);
    }
}
