package com.teenagemutantninjacoders.robotwarehouse.domain.helpers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.MapLevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by JordiRM on 15/02/2016.
 */
public class Utils {
    public static Vector2 unprojectPosition(Viewport viewport, Vector2 touchPos){
        viewport.unproject(touchPos);
        touchPos.y =  GameConstants.VERTICAL_RESOLUTION - touchPos.y;
        return touchPos;
    }

    public static String getTimeFormatted(float timeToFormat){
        int minutes = MathUtils.floor(timeToFormat / 60);
        int seconds = MathUtils.floor(timeToFormat - (minutes * 60));
        String sMinutes = Integer.toString(minutes);
        String sSeconds;
        if(seconds < 10) sSeconds = "0" + Integer.toString(seconds);
        else sSeconds = Integer.toString(seconds);
        return (sMinutes + ":" + sSeconds);
    }

    public static ArrayList<Integer> mergeIntegerArrays(ArrayList<Integer> array1, ArrayList<Integer> array2) {
        int index1 = 0;
        int index2 = 0;
        ArrayList<Integer> mergedArray = new ArrayList<Integer>();
        while(index1 < array1.size() && index2 < array2.size()) {
            if(array1.get(index1) < array2.get(index2)) {
                mergedArray.add(array1.get(index1));
                index1++;
            } else if(array1.get(index1) > array2.get(index2)){
                mergedArray.add(array2.get(index2));
                index2++;
            } else {
                mergedArray.add(array1.get(index1));
                index1++;
                index2++;
            }
        }
        while(index1 < array1.size()) {
            mergedArray.add(array1.get(index1));
            index1++;
        }
        while(index2 < array2.size()) {
            mergedArray.add(array2.get(index2));
            index2++;
        }
        return mergedArray;
    }

    public static int getLevelIndexByLevelNumber(int episodeNumber, int levelNumber){
        int index = 0;
        EpisodeDTO episodeDTO = GlobalGeneralData.getInstance().getEpisodeDataByNumber(episodeNumber);
        int size = episodeDTO.getLevels().size();
        MapLevelDTO mapLevel;
        do{
            mapLevel = episodeDTO.getLevels().get(index);
            if(mapLevel.getLevelNumber() == levelNumber) return index;
            index++;
        }while (index < size);
        return -1;
    }
}
