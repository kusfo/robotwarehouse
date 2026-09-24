package com.teenagemutantninjacoders.robotwarehouse.data.parsers;

import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.BoxDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.LevelDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordi.montornes on 22/02/2016.
 */
public class LevelParser {
    public static LevelDTO parseLevel(String levelJson){
        Json json = new Json();
        LevelDTO levelDTO = json.fromJson(LevelDTO.class, levelJson);
        return levelDTO;
    }

}
