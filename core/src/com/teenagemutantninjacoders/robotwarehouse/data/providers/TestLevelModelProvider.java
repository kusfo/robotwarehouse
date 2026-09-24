package com.teenagemutantninjacoders.robotwarehouse.data.providers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.LevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.mappers.LevelDTOtoLevelMapper;
import com.teenagemutantninjacoders.robotwarehouse.data.parsers.LevelParser;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.LevelModelProvider;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

public class TestLevelModelProvider implements LevelModelProvider {

    private final String launchLevel;

    public TestLevelModelProvider(String launchLevel) {
        this.launchLevel = launchLevel;
    }

    @Override
    public LevelModel getModelForLevel(int numEpisode, int numLevel) {
        FileHandle file = Gdx.files.absolute(launchLevel);
        String levelJson = file.readString();
        LevelDTO levelDTO = LevelParser.parseLevel(levelJson);
        LevelDTOtoLevelMapper levelDTOtoLevelMapper = new LevelDTOtoLevelMapper();
        Gdx.app.log("TestLevelProvider","LevelDTO has leveltime " + levelDTO.getLevelTime());
        LevelModel levelModel = levelDTOtoLevelMapper.transform(levelDTO);
        return levelModel;
    }
}
