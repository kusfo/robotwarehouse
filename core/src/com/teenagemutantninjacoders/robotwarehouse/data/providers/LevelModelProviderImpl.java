package com.teenagemutantninjacoders.robotwarehouse.data.providers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.LevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.mappers.LevelDTOtoLevelMapper;
import com.teenagemutantninjacoders.robotwarehouse.data.parsers.LevelParser;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.LevelModelProvider;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

/**
 * Created by jordi on 05/03/2016.
 */
public class LevelModelProviderImpl implements LevelModelProvider {
    @Override
    public LevelModel getModelForLevel(int numEpisode, int numLevel){
        String currentEpisode = String.format("%02d", numEpisode);
        String currentLevel = String.format("%02d", numLevel);
        String folderString = "data/episodes/episode_" + currentEpisode + "/";
        String fileString = "level_" + currentEpisode + "_" + currentLevel + ".json";
        FileHandle file = Gdx.files.internal(folderString + fileString);
        String levelJson = file.readString();
        LevelDTO levelDTO = LevelParser.parseLevel(levelJson);
        LevelDTOtoLevelMapper levelDTOtoLevelMapper = new LevelDTOtoLevelMapper();
        LevelModel levelModel = levelDTOtoLevelMapper.transform(levelDTO);
        return levelModel;
    }
}
