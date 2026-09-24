package com.teenagemutantninjacoders.robotwarehouse.display;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 05/02/2016.
 */
public class Assets {
    private static final AssetManager assetManager = new AssetManager();
    private static ArrayMap<String, AssetFile> filesMap, baseFilesMap;
    private static ArrayMap<String, AssetFileAttributes> filesAttributesMap;
    private static Application.ApplicationType applicationType;

    public static void initialize(Application.ApplicationType type){
        applicationType = type;
        filesMap = new ArrayMap<String, AssetFile>();
        filesAttributesMap = new ArrayMap<String, AssetFileAttributes>();
        baseFilesMap = new ArrayMap<String, AssetFile>();
    }

    public static void loadAllAssets() {

        // TEXTURAS
        filesMap.put("bg_space_1", new AssetFile("images/backgrounds/bg_space_1.png", Texture.class));
        filesMap.put("bg_space_2", new AssetFile("images/backgrounds/bg_space_2.png", Texture.class));
        filesMap.put("bg_space_3", new AssetFile("images/backgrounds/bg_space_3.png", Texture.class));
        filesMap.put("bg_space_4", new AssetFile("images/backgrounds/bg_space_4.png", Texture.class));
        filesMap.put("bg_selectionScreen", new AssetFile("images/backgrounds/bg_selectionScreen.png", Texture.class));

        //TEXTURE ATLAS
        filesMap.put("main_title", new AssetFile("images/ui/main_title.atlas", TextureAtlas.class));
        filesMap.put("level_screen_elements", new AssetFile("images/ui/level_screen_elements.atlas", TextureAtlas.class));
        filesMap.put("general_screen_elements", new AssetFile("images/ui/general_screen_elements.atlas", TextureAtlas.class));
        filesMap.put("generalLevelPanels", new AssetFile("images/ui/generalLevelPanels.atlas", TextureAtlas.class));
        filesMap.put("general_buttons", new AssetFile("images/ui/general_buttons.atlas", TextureAtlas.class));
        filesMap.put("tutorialPanel", new AssetFile("images/ui/tutorialPanel.atlas", TextureAtlas.class));
        filesMap.put("missionPanel", new AssetFile("images/ui/missionPanel.atlas", TextureAtlas.class));
        filesMap.put("gameOverPanel", new AssetFile("images/ui/gameOverPanel.atlas", TextureAtlas.class));
        filesMap.put("missionCompletePanel", new AssetFile("images/ui/missionCompletePanel.atlas", TextureAtlas.class));
        filesMap.put("pausePanel_info", new AssetFile("images/ui/pausePanelInfo.atlas", TextureAtlas.class));
        filesMap.put("pausePanel_side", new AssetFile("images/ui/pauseSidePanel.atlas", TextureAtlas.class));
        filesMap.put("intro_general", new AssetFile("images/ui/intro_general.atlas", TextureAtlas.class));
        filesMap.put("episodeSelection_general", new AssetFile("images/ui/episodeSelection_general.atlas", TextureAtlas.class));
        filesMap.put("episodeSelection_episodes", new AssetFile("images/ui/episodeSelection_episodes.atlas", TextureAtlas.class));
        filesMap.put("levelSelection_general", new AssetFile("images/ui/levelSelection_general.atlas", TextureAtlas.class));
        filesMap.put("levelSelection_mapDecos", new AssetFile("images/ui/levelSelection_mapDecos.atlas", TextureAtlas.class));
        filesMap.put("tutorialsMenulPanel", new AssetFile("images/ui/tutorialsMenulPanel.atlas", TextureAtlas.class));
        filesMap.put("character_conversation", new AssetFile("images/ui/character_conversation.atlas", TextureAtlas.class));
        filesMap.put("logos", new AssetFile("images/ui/logos.atlas", TextureAtlas.class));

        filesMap.put("spaceDock", new AssetFile("images/ship/spaceDock.atlas", TextureAtlas.class));

        filesMap.put("th01_boxes01", new AssetFile("images/sheets/th01_boxes01.atlas", TextureAtlas.class));
        filesMap.put("th01_wall01", new AssetFile("images/sheets/th01_wall01.atlas", TextureAtlas.class));
        addFileAttributes("th01_wall01","images/sheets/th01_wall01.json");
        filesMap.put("th01_floor01", new AssetFile("images/sheets/th01_floor01.atlas", TextureAtlas.class));
        filesMap.put("th01_abyss01", new AssetFile("images/sheets/th01_abyss01.atlas", TextureAtlas.class));
        filesMap.put("th01_trapDoor01", new AssetFile("images/sheets/th01_trapDoor01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseBeam01", new AssetFile("images/sheets/th01_overBaseBeam01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseDeco01", new AssetFile("images/sheets/th01_overBaseDeco01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseDetail", new AssetFile("images/sheets/th01_overBaseDetail.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseTrack01", new AssetFile("images/sheets/th01_overBaseTrack01.atlas", TextureAtlas.class));
        filesMap.put("th01_bigDeco", new AssetFile("images/sheets/th01_bigDeco.atlas", TextureAtlas.class));
        filesMap.put("th01_robotElevator01", new AssetFile("images/sheets/th01_robotElevator01.atlas", TextureAtlas.class));
        filesMap.put("th01_rotatingFloor", new AssetFile("images/sheets/th01_rotatingFloor.atlas", TextureAtlas.class));
        filesMap.put("th01_barriers", new AssetFile("images/sheets/th01_barriers.atlas", TextureAtlas.class));
        filesMap.put("th01_buttons", new AssetFile("images/sheets/th01_buttons.atlas", TextureAtlas.class));

        filesMap.put("robot_pusher", new AssetFile("images/sheets/robot_pusher.atlas", TextureAtlas.class));
        filesMap.put("robot_explosive", new AssetFile("images/sheets/robot_explosive.atlas", TextureAtlas.class));
        filesMap.put("e_cannon01", new AssetFile("images/sheets/e_cannon01.atlas", TextureAtlas.class));
        filesMap.put("e_ratlien", new AssetFile("images/sheets/e_ratlien.atlas", TextureAtlas.class));

        filesMap.put("overBaseBoxActivable", new AssetFile("images/sheets/overBaseBoxActivable.atlas", TextureAtlas.class));
        filesMap.put("overBaseIndicatorLight", new AssetFile("images/sheets/overBaseIndicatorLight.atlas", TextureAtlas.class));

        filesMap.put("fx_powerSparkDisplay", new AssetFile("images/sheets/fx_powerSparkDisplay.atlas", TextureAtlas.class));
        filesMap.put("fx_boxPointsBaseSpark", new AssetFile("images/sheets/fx_boxPointsBaseSpark.atlas", TextureAtlas.class));
        filesMap.put("fx_enemyDeactivationSparks", new AssetFile("images/sheets/fx_enemyDeactivationSparks.atlas", TextureAtlas.class));
        filesMap.put("fx_ratlien_dead", new AssetFile("images/sheets/fx_ratlien_dead.atlas", TextureAtlas.class));
        filesMap.put("fx_robot_bubble", new AssetFile("images/sheets/fx_robot_bubble.atlas", TextureAtlas.class));
        filesMap.put("fx_robot_explosion", new AssetFile("images/sheets/fx_robot_explosion.atlas", TextureAtlas.class));


        // SONIDOS
        filesMap.put("snd_BoxHit_01", new AssetFile("sounds/snd_BoxHit_01.wav", Sound.class));
        filesMap.put("snd_boxDestruction", new AssetFile("sounds/snd_boxDestruction.wav", Sound.class));
        filesMap.put("snd_robotSmash_01", new AssetFile("sounds/snd_robotSmash_01.wav", Sound.class));
        filesMap.put("snd_teleport_01", new AssetFile("sounds/snd_teleport_01.wav", Sound.class));
        filesMap.put("snd_laserShooting_01", new AssetFile("sounds/snd_laserShooting_01.wav", Sound.class));
        filesMap.put("snd_laserCharging_01", new AssetFile("sounds/snd_laserCharging_01.wav", Sound.class));
        filesMap.put("snd_robotAlarm_01c", new AssetFile("sounds/snd_robotAlarm_01c.wav", Sound.class));
        filesMap.put("snd_robotExplosion_01", new AssetFile("sounds/snd_robotExplosion_01.wav", Sound.class));
        filesMap.put("snd_fall_01", new AssetFile("sounds/snd_fall_01.wav", Sound.class));
        filesMap.put("snd_robotPushing", new AssetFile("sounds/snd_robotPushing.wav", Sound.class));
        filesMap.put("snd_robotEnter_01", new AssetFile("sounds/snd_robotEnter_01.wav", Sound.class));
        filesMap.put("snd_robotBubble_01", new AssetFile("sounds/snd_robot_bubble_01.wav", Sound.class));
        filesMap.put("snd_ratlienImpactDead_01", new AssetFile("sounds/snd_ratlienImpactDead_01.wav", Sound.class));
        filesMap.put("snd_ratlienImpactDead_02", new AssetFile("sounds/snd_ratlienImpactDead_02.wav", Sound.class));
        filesMap.put("snd_ratlienFallDead", new AssetFile("sounds/snd_ratlienFallDead.wav", Sound.class));
        filesMap.put("snd_ratlienEnter", new AssetFile("sounds/snd_ratlienEnter.wav", Sound.class));
        filesMap.put("snd_ratlienChewingLoop", new AssetFile("sounds/snd_ratlienChewingLoop.wav", Sound.class));
        filesMap.put("snd_rotatingFloorImpulse", new AssetFile("sounds/snd_rotatingFloorImpulse.wav", Sound.class));
        filesMap.put("snd_floorButtonActivate", new AssetFile("sounds/snd_floorButtonActivate.wav", Sound.class));
        filesMap.put("snd_floorButtonDeactivate", new AssetFile("sounds/snd_floorButtonDeactivate.wav", Sound.class));
        filesMap.put("snd_barrierSolid", new AssetFile("sounds/snd_barrierSolid.wav", Sound.class));
        filesMap.put("snd_barrierEnergy", new AssetFile("sounds/snd_barrierEnergy.wav", Sound.class));
        filesMap.put("snd_trapDoor", new AssetFile("sounds/snd_trapDoor.wav", Sound.class));
        filesMap.put("snd_panelEnter_01", new AssetFile("sounds/snd_panelEnter_01.wav", Sound.class));
        filesMap.put("snd_panelLeave_01", new AssetFile("sounds/snd_panelLeave_01.wav", Sound.class));
        filesMap.put("snd_panelShortEnter_01", new AssetFile("sounds/snd_panelShortEnter_01.wav", Sound.class));
        filesMap.put("snd_panelShortLeave_01", new AssetFile("sounds/snd_panelShortLeave_01.wav", Sound.class));
        filesMap.put("snd_panelBoxes_enter", new AssetFile("sounds/snd_panelBoxes_enter.wav", Sound.class));
        filesMap.put("snd_panelBoxes_leave", new AssetFile("sounds/snd_panelBoxes_leave.wav", Sound.class));
        filesMap.put("snd_buttonClickGeneric", new AssetFile("sounds/snd_buttonClickGeneric.wav", Sound.class));
        filesMap.put("snd_buttonClickChange", new AssetFile("sounds/snd_buttonClickChange.wav", Sound.class));
        filesMap.put("snd_BoxPointsAchieved", new AssetFile("sounds/snd_BoxPointsAchieved.wav", Sound.class));
        filesMap.put("snd_markPointsAchieved", new AssetFile("sounds/snd_markPointsAchieved.wav", Sound.class));
        filesMap.put("snd_ready", new AssetFile("sounds/snd_ready.wav", Sound.class));
        filesMap.put("snd_addTotalPoints", new AssetFile("sounds/snd_addTotalPoints.wav", Sound.class));
        filesMap.put("snd_rankStar_1", new AssetFile("sounds/snd_rankStar_1.wav", Sound.class));
        filesMap.put("snd_rankStar_2", new AssetFile("sounds/snd_rankStar_2.wav", Sound.class));
        filesMap.put("snd_rankStar_3", new AssetFile("sounds/snd_rankStar_3.wav", Sound.class));
        filesMap.put("snd_boxDisabling", new AssetFile("sounds/snd_boxDisabling.wav", Sound.class));
        filesMap.put("snd_boxGroupCreation", new AssetFile("sounds/snd_boxGroupCreation.wav", Sound.class));
        filesMap.put("snd_boxGroupLeave", new AssetFile("sounds/snd_boxGroupLeave.wav", Sound.class));
        filesMap.put("snd_boxLost", new AssetFile("sounds/snd_boxLost.wav", Sound.class));
        filesMap.put("snd_powerActivation", new AssetFile("sounds/snd_powerActivation.wav", Sound.class));
        filesMap.put("snd_overBaseTriggered", new AssetFile("sounds/snd_overBaseTriggered.wav", Sound.class));
        filesMap.put("snd_disabledBuzzing", new AssetFile("sounds/snd_disabledBuzzing.wav", Sound.class));
        filesMap.put("snd_activateFumigation", new AssetFile("sounds/snd_activateFumigation.wav", Sound.class));
        filesMap.put("snd_challengeFailedAnnouncement", new AssetFile("sounds/snd_challengeFailedAnnouncement.wav", Sound.class));
        filesMap.put("snd_challengeCompleteAnnouncement", new AssetFile("sounds/snd_challengeCompleteAnnouncement.wav", Sound.class));
        filesMap.put("snd_challengeCompleted_01", new AssetFile("sounds/snd_challengeCompleted_01.wav", Sound.class));
        filesMap.put("snd_lights_levelCompleted", new AssetFile("sounds/snd_lights_levelCompleted.wav", Sound.class));
        filesMap.put("snd_lights_gameOver", new AssetFile("sounds/snd_lights_gameOver.wav", Sound.class));

        filesMap.put("snd_levelUnblockNormal", new AssetFile("sounds/snd_levelUnblockNormal.wav", Sound.class));
        filesMap.put("snd_levelUnblockStar", new AssetFile("sounds/snd_levelUnblockStar.wav", Sound.class));
        filesMap.put("snd_episodeUnblock", new AssetFile("sounds/snd_episodeUnblock.wav", Sound.class));
        filesMap.put("snd_unblocking_flying_star_01", new AssetFile("sounds/snd_unblocking_flying_star_01.wav", Sound.class));
        filesMap.put("snd_bubble", new AssetFile("sounds/snd_bubble.wav", Sound.class));

        filesMap.put("snd_episodeCompleted", new AssetFile("sounds/snd_episodeCompleted.wav", Sound.class));

        // MUSICAS
        if(applicationType == Application.ApplicationType.iOS) {

            filesMap.put("snd_level_failed", new AssetFile("sounds/snd_level_failed.m4a", Sound.class)); // m4a
            filesMap.put("snd_level_completed_01", new AssetFile("sounds/snd_level_completed_01.m4a", Sound.class)); //m4a
            filesMap.put("snd_news_short", new AssetFile("sounds/snd_news_short.m4a", Sound.class)); //m4a
            filesMap.put("mus_main", new AssetFile("music/mus_main.m4a", Music.class));
            filesMap.put("mus_gameLevel", new AssetFile("music/mus_gameLevel.m4a", Music.class));
            filesMap.put("mus_selectionScreen", new AssetFile("music/mus_selectionScreen.m4a", Music.class));
            filesMap.put("mus_levelCompletedPanel", new AssetFile("music/mus_levelCompletedPanel.m4a", Music.class));
            filesMap.put("mus_scene_generic_01", new AssetFile("music/mus_scene_generic_01.m4a", Music.class));
        } else {
            filesMap.put("snd_level_failed", new AssetFile("sounds/snd_level_failed.ogg", Sound.class)); // OGG
            filesMap.put("snd_level_completed_01", new AssetFile("sounds/snd_level_completed_01.ogg", Sound.class)); //OGG
            filesMap.put("snd_news_short", new AssetFile("sounds/snd_news_short.ogg", Sound.class)); //OGG
            filesMap.put("mus_main", new AssetFile("music/mus_main.ogg", Music.class));
            filesMap.put("mus_gameLevel", new AssetFile("music/mus_gameLevel.ogg", Music.class));
            filesMap.put("mus_selectionScreen", new AssetFile("music/mus_selectionScreen.ogg", Music.class));
            filesMap.put("mus_levelCompletedPanel", new AssetFile("music/mus_levelCompletedPanel.ogg", Music.class));
            filesMap.put("mus_scene_generic_01", new AssetFile("music/mus_scene_generic_01.ogg", Music.class));
        }




        //FUENTES
        filesMap.put("f_base_gb_11", new AssetFile("fonts/f_base_gb_11.fnt", BitmapFont.class));
        filesMap.put("f_base_gb_13", new AssetFile("fonts/f_base_gb_13.fnt", BitmapFont.class));
        filesMap.put("f_base_gb_16", new AssetFile("fonts/f_base_gb_16.fnt", BitmapFont.class));
        filesMap.put("f_base_gb_22", new AssetFile("fonts/f_base_gb_22.fnt", BitmapFont.class));
        filesMap.put("f_numbers_gb_18", new AssetFile("fonts/f_numbers_gb_18.fnt", BitmapFont.class));
        filesMap.put("f_points_a", new AssetFile("fonts/f_points_a.fnt", BitmapFont.class));
        filesMap.put("f_points_b", new AssetFile("fonts/f_points_b.fnt", BitmapFont.class));
        filesMap.put("f_cartel_bb_21", new AssetFile("fonts/f_cartel_bb_21.fnt", BitmapFont.class));
        filesMap.put("f_base_bb_14", new AssetFile("fonts/f_base_bb_14.fnt", BitmapFont.class));
        filesMap.put("f_character_dialogue_18", new AssetFile("fonts/f_character_dialogue_18.fnt", BitmapFont.class));
        filesMap.put("f_cartel_partida_24", new AssetFile("fonts/f_cartel_partida_24.fnt", BitmapFont.class));

        initiateLoadProcess();

        // Le pasamos al audioManager la lista de sonidos cargados
        addSoundsToManagerList();
    }

    public static void loadLogoAssets(){
        filesMap.put("logos", new AssetFile("images/ui/logos.atlas", TextureAtlas.class));
        initiateLoadProcess();
    }

    public static void loadMainAssets(){
        filesMap.put("tutorialPanel", new AssetFile("images/ui/tutorialPanel.atlas", TextureAtlas.class));
        filesMap.put("pausePanel_side", new AssetFile("images/ui/pauseSidePanel.atlas", TextureAtlas.class));
        filesMap.put("main_title", new AssetFile("images/ui/main_title.atlas", TextureAtlas.class));
        filesMap.put("general_buttons", new AssetFile("images/ui/general_buttons.atlas", TextureAtlas.class));
        filesMap.put("tutorialsMenulPanel", new AssetFile("images/ui/tutorialsMenulPanel.atlas", TextureAtlas.class));
        initiateLoadProcess();
    }

    public static void loadSelectionAssets(){
        filesMap.put("bg_selectionScreen", new AssetFile("images/backgrounds/bg_selectionScreen.png", Texture.class));
        filesMap.put("intro_general", new AssetFile("images/ui/intro_general.atlas", TextureAtlas.class));
        filesMap.put("episodeSelection_general", new AssetFile("images/ui/episodeSelection_general.atlas", TextureAtlas.class));
        filesMap.put("episodeSelection_episodes", new AssetFile("images/ui/episodeSelection_episodes.atlas", TextureAtlas.class));
        filesMap.put("levelSelection_general", new AssetFile("images/ui/levelSelection_general.atlas", TextureAtlas.class));
        filesMap.put("levelSelection_mapDecos", new AssetFile("images/ui/levelSelection_mapDecos.atlas", TextureAtlas.class));
        filesMap.put("character_conversation", new AssetFile("images/ui/character_conversation.atlas", TextureAtlas.class));
        initiateLoadProcess();
    }

    public static void loadLevelAssets(){

        filesMap.put("bg_space_1", new AssetFile("images/backgrounds/bg_space_1.png", Texture.class)); // Todo: Optimizar
        filesMap.put("bg_space_2", new AssetFile("images/backgrounds/bg_space_2.png", Texture.class)); // Todo: Optimizar
        filesMap.put("bg_space_3", new AssetFile("images/backgrounds/bg_space_3.png", Texture.class)); // Todo: Optimizar

        filesMap.put("level_screen_elements", new AssetFile("images/ui/level_screen_elements.atlas", TextureAtlas.class));
        filesMap.put("generalLevelPanels", new AssetFile("images/ui/generalLevelPanels.atlas", TextureAtlas.class));
        filesMap.put("tutorialPanel", new AssetFile("images/ui/tutorialPanel.atlas", TextureAtlas.class));
        filesMap.put("missionPanel", new AssetFile("images/ui/missionPanel.atlas", TextureAtlas.class));
        filesMap.put("gameOverPanel", new AssetFile("images/ui/gameOverPanel.atlas", TextureAtlas.class));
        filesMap.put("missionCompletePanel", new AssetFile("images/ui/missionCompletePanel.atlas", TextureAtlas.class));
        filesMap.put("pausePanel_info", new AssetFile("images/ui/pausePanelInfo.atlas", TextureAtlas.class));
        filesMap.put("pausePanel_side", new AssetFile("images/ui/pauseSidePanel.atlas", TextureAtlas.class));
        filesMap.put("intro_general", new AssetFile("images/ui/intro_general.atlas", TextureAtlas.class));
        filesMap.put("character_conversation", new AssetFile("images/ui/character_conversation.atlas", TextureAtlas.class));

        filesMap.put("spaceDock", new AssetFile("images/ship/spaceDock.atlas", TextureAtlas.class));

        filesMap.put("th01_boxes01", new AssetFile("images/sheets/th01_boxes01.atlas", TextureAtlas.class));
        filesMap.put("th01_wall01", new AssetFile("images/sheets/th01_wall01.atlas", TextureAtlas.class));
        addFileAttributes("th01_wall01","images/sheets/th01_wall01.json");
        filesMap.put("th01_floor01", new AssetFile("images/sheets/th01_floor01.atlas", TextureAtlas.class));
        filesMap.put("th01_abyss01", new AssetFile("images/sheets/th01_abyss01.atlas", TextureAtlas.class));
        filesMap.put("th01_trapDoor01", new AssetFile("images/sheets/th01_trapDoor01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseBeam01", new AssetFile("images/sheets/th01_overBaseBeam01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseDeco01", new AssetFile("images/sheets/th01_overBaseDeco01.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseDetail", new AssetFile("images/sheets/th01_overBaseDetail.atlas", TextureAtlas.class));
        filesMap.put("th01_overBaseTrack01", new AssetFile("images/sheets/th01_overBaseTrack01.atlas", TextureAtlas.class));
        filesMap.put("th01_bigDeco", new AssetFile("images/sheets/th01_bigDeco.atlas", TextureAtlas.class));
        filesMap.put("th01_robotElevator01", new AssetFile("images/sheets/th01_robotElevator01.atlas", TextureAtlas.class));
        filesMap.put("th01_rotatingFloor", new AssetFile("images/sheets/th01_rotatingFloor.atlas", TextureAtlas.class));
        filesMap.put("th01_barriers", new AssetFile("images/sheets/th01_barriers.atlas", TextureAtlas.class));
        filesMap.put("th01_buttons", new AssetFile("images/sheets/th01_buttons.atlas", TextureAtlas.class));

        filesMap.put("robot_pusher", new AssetFile("images/sheets/robot_pusher.atlas", TextureAtlas.class));
        filesMap.put("robot_explosive", new AssetFile("images/sheets/robot_explosive.atlas", TextureAtlas.class));
        filesMap.put("e_cannon01", new AssetFile("images/sheets/e_cannon01.atlas", TextureAtlas.class));
        filesMap.put("e_ratlien", new AssetFile("images/sheets/e_ratlien.atlas", TextureAtlas.class));

        filesMap.put("overBaseBoxActivable", new AssetFile("images/sheets/overBaseBoxActivable.atlas", TextureAtlas.class));
        filesMap.put("overBaseIndicatorLight", new AssetFile("images/sheets/overBaseIndicatorLight.atlas", TextureAtlas.class));

        filesMap.put("fx_powerSparkDisplay", new AssetFile("images/sheets/fx_powerSparkDisplay.atlas", TextureAtlas.class));
        filesMap.put("fx_boxPointsBaseSpark", new AssetFile("images/sheets/fx_boxPointsBaseSpark.atlas", TextureAtlas.class));
        filesMap.put("fx_enemyDeactivationSparks", new AssetFile("images/sheets/fx_enemyDeactivationSparks.atlas", TextureAtlas.class));
        filesMap.put("fx_ratlien_dead", new AssetFile("images/sheets/fx_ratlien_dead.atlas", TextureAtlas.class));
        filesMap.put("fx_robot_bubble", new AssetFile("images/sheets/fx_robot_bubble.atlas", TextureAtlas.class));
        initiateLoadProcess();
    }

    public static void unloadScreenAssets(){
        for(AssetFile asset : filesMap.values()){
            assetManager.unload(asset.path);
        }
        filesAttributesMap.clear();
        filesMap.clear();
    }

    private static void loadSoundsAndMusic(){
        // SONIDOS
        baseFilesMap.put("snd_BoxHit_01", new AssetFile("sounds/snd_BoxHit_01.wav", Sound.class));
        baseFilesMap.put("snd_boxDestruction", new AssetFile("sounds/snd_boxDestruction.wav", Sound.class));
        baseFilesMap.put("snd_robotSmash_01", new AssetFile("sounds/snd_robotSmash_01.wav", Sound.class));
        baseFilesMap.put("snd_teleport_01", new AssetFile("sounds/snd_teleport_01.wav", Sound.class));
        baseFilesMap.put("snd_laserShooting_01", new AssetFile("sounds/snd_laserShooting_01.wav", Sound.class));
        baseFilesMap.put("snd_laserCharging_01", new AssetFile("sounds/snd_laserCharging_01.wav", Sound.class));
        baseFilesMap.put("snd_robotAlarm_01c", new AssetFile("sounds/snd_robotAlarm_01c.wav", Sound.class));
        baseFilesMap.put("snd_robotExplosion_01", new AssetFile("sounds/snd_robotExplosion_01.wav", Sound.class));
        baseFilesMap.put("snd_fall_01", new AssetFile("sounds/snd_fall_01.wav", Sound.class));
        baseFilesMap.put("snd_robotPushing", new AssetFile("sounds/snd_robotPushing.wav", Sound.class));
        baseFilesMap.put("snd_robotEnter_01", new AssetFile("sounds/snd_robotEnter_01.wav", Sound.class));
        baseFilesMap.put("snd_robotBubble_01", new AssetFile("sounds/snd_robot_bubble_01.wav", Sound.class));
        baseFilesMap.put("snd_ratlienImpactDead_01", new AssetFile("sounds/snd_ratlienImpactDead_01.wav", Sound.class));
        baseFilesMap.put("snd_ratlienImpactDead_02", new AssetFile("sounds/snd_ratlienImpactDead_02.wav", Sound.class));
        baseFilesMap.put("snd_ratlienFallDead", new AssetFile("sounds/snd_ratlienFallDead.wav", Sound.class));
        baseFilesMap.put("snd_ratlienEnter", new AssetFile("sounds/snd_ratlienEnter.wav", Sound.class));
        baseFilesMap.put("snd_ratlienChewingLoop", new AssetFile("sounds/snd_ratlienChewingLoop.wav", Sound.class));
        baseFilesMap.put("snd_rotatingFloorImpulse", new AssetFile("sounds/snd_rotatingFloorImpulse.wav", Sound.class));
        baseFilesMap.put("snd_floorButtonActivate", new AssetFile("sounds/snd_floorButtonActivate.wav", Sound.class));
        baseFilesMap.put("snd_floorButtonDeactivate", new AssetFile("sounds/snd_floorButtonDeactivate.wav", Sound.class));
        baseFilesMap.put("snd_panelEnter_01", new AssetFile("sounds/snd_panelEnter_01.wav", Sound.class));
        baseFilesMap.put("snd_panelLeave_01", new AssetFile("sounds/snd_panelLeave_01.wav", Sound.class));
        baseFilesMap.put("snd_panelShortEnter_01", new AssetFile("sounds/snd_panelShortEnter_01.wav", Sound.class));
        baseFilesMap.put("snd_panelShortLeave_01", new AssetFile("sounds/snd_panelShortLeave_01.wav", Sound.class));
        baseFilesMap.put("snd_panelBoxes_enter", new AssetFile("sounds/snd_panelBoxes_enter.wav", Sound.class));
        baseFilesMap.put("snd_panelBoxes_leave", new AssetFile("sounds/snd_panelBoxes_leave.wav", Sound.class));
        baseFilesMap.put("snd_buttonClickGeneric", new AssetFile("sounds/snd_buttonClickGeneric.wav", Sound.class));
        baseFilesMap.put("snd_buttonClickChange", new AssetFile("sounds/snd_buttonClickChange.wav", Sound.class));
        baseFilesMap.put("snd_BoxPointsAchieved", new AssetFile("sounds/snd_BoxPointsAchieved.wav", Sound.class));
        baseFilesMap.put("snd_markPointsAchieved", new AssetFile("sounds/snd_markPointsAchieved.wav", Sound.class));
        baseFilesMap.put("snd_ready", new AssetFile("sounds/snd_ready.wav", Sound.class));
        baseFilesMap.put("snd_addTotalPoints", new AssetFile("sounds/snd_addTotalPoints.wav", Sound.class));
        baseFilesMap.put("snd_rankStar_1", new AssetFile("sounds/snd_rankStar_1.wav", Sound.class));
        baseFilesMap.put("snd_rankStar_2", new AssetFile("sounds/snd_rankStar_2.wav", Sound.class));
        baseFilesMap.put("snd_rankStar_3", new AssetFile("sounds/snd_rankStar_3.wav", Sound.class));
        baseFilesMap.put("snd_boxDisabling", new AssetFile("sounds/snd_boxDisabling.wav", Sound.class));
        baseFilesMap.put("snd_boxGroupCreation", new AssetFile("sounds/snd_boxGroupCreation.wav", Sound.class));
        baseFilesMap.put("snd_boxGroupLeave", new AssetFile("sounds/snd_boxGroupLeave.wav", Sound.class));
        baseFilesMap.put("snd_boxLost", new AssetFile("sounds/snd_boxLost.wav", Sound.class));
        baseFilesMap.put("snd_powerActivation", new AssetFile("sounds/snd_powerActivation.wav", Sound.class));
        baseFilesMap.put("snd_overBaseTriggered", new AssetFile("sounds/snd_overBaseTriggered.wav", Sound.class));
        baseFilesMap.put("snd_disabledBuzzing", new AssetFile("sounds/snd_disabledBuzzing.wav", Sound.class));
        baseFilesMap.put("snd_activateFumigation", new AssetFile("sounds/snd_activateFumigation.wav", Sound.class));
        baseFilesMap.put("snd_challengeFailedAnnouncement", new AssetFile("sounds/snd_challengeFailedAnnouncement.wav", Sound.class));
        baseFilesMap.put("snd_challengeCompleteAnnouncement", new AssetFile("sounds/snd_challengeCompleteAnnouncement.wav", Sound.class));
        baseFilesMap.put("snd_challengeCompleted_01", new AssetFile("sounds/snd_challengeCompleted_01.wav", Sound.class));
        baseFilesMap.put("snd_lights_levelCompleted", new AssetFile("sounds/snd_lights_levelCompleted.wav", Sound.class));
        baseFilesMap.put("snd_lights_gameOver", new AssetFile("sounds/snd_lights_gameOver.wav", Sound.class));

        baseFilesMap.put("snd_levelUnblockNormal", new AssetFile("sounds/snd_levelUnblockNormal.wav", Sound.class));
        baseFilesMap.put("snd_levelUnblockStar", new AssetFile("sounds/snd_levelUnblockStar.wav", Sound.class));
        baseFilesMap.put("snd_episodeUnblock", new AssetFile("sounds/snd_episodeUnblock.wav", Sound.class));
        baseFilesMap.put("snd_unblocking_flying_star_01", new AssetFile("sounds/snd_unblocking_flying_star_01.wav", Sound.class));
        baseFilesMap.put("snd_bubble", new AssetFile("sounds/snd_bubble.wav", Sound.class));

        baseFilesMap.put("snd_episodeCompleted", new AssetFile("sounds/snd_episodeCompleted.wav", Sound.class));
        baseFilesMap.put("snd_level_failed", new AssetFile("sounds/snd_level_failed.ogg", Sound.class)); // OGG
        baseFilesMap.put("snd_level_completed_01", new AssetFile("sounds/snd_level_completed_01.ogg", Sound.class)); //OGG
        baseFilesMap.put("snd_news_short", new AssetFile("sounds/snd_news_short.ogg", Sound.class)); //OGG


        // MUSICAS
        baseFilesMap.put("mus_main", new AssetFile("music/mus_main.ogg", Music.class));
        baseFilesMap.put("mus_gameLevel", new AssetFile("music/mus_gameLevel.ogg", Music.class));
        baseFilesMap.put("mus_selectionScreen", new AssetFile("music/mus_selectionScreen.ogg", Music.class));
        baseFilesMap.put("mus_levelCompletedPanel", new AssetFile("music/mus_levelCompletedPanel.ogg", Music.class));
        baseFilesMap.put("mus_scene_generic_01", new AssetFile("music/mus_scene_generic_01.ogg", Music.class));

        initiateLoadProcess();
    }

    private static void loadFonts(){
        //FUENTES
        baseFilesMap.put("f_base_gb_11", new AssetFile("fonts/f_base_gb_11.fnt", BitmapFont.class));
        baseFilesMap.put("f_base_gb_13", new AssetFile("fonts/f_base_gb_13.fnt", BitmapFont.class));
        baseFilesMap.put("f_base_gb_16", new AssetFile("fonts/f_base_gb_16.fnt", BitmapFont.class));
        baseFilesMap.put("f_base_gb_22", new AssetFile("fonts/f_base_gb_22.fnt", BitmapFont.class));
        baseFilesMap.put("f_numbers_gb_18", new AssetFile("fonts/f_numbers_gb_18.fnt", BitmapFont.class));
        baseFilesMap.put("f_points_a", new AssetFile("fonts/f_points_a.fnt", BitmapFont.class));
        baseFilesMap.put("f_points_b", new AssetFile("fonts/f_points_b.fnt", BitmapFont.class));
        baseFilesMap.put("f_cartel_bb_21", new AssetFile("fonts/f_cartel_bb_21.fnt", BitmapFont.class));
        baseFilesMap.put("f_base_bb_14", new AssetFile("fonts/f_base_bb_14.fnt", BitmapFont.class));
        baseFilesMap.put("f_character_dialogue_18", new AssetFile("fonts/f_character_dialogue_18.fnt", BitmapFont.class));
        baseFilesMap.put("f_cartel_partida_24", new AssetFile("fonts/f_cartel_partida_24.fnt", BitmapFont.class));
    }

    public static void loadBaseAssets(){
        loadSoundsAndMusic();
        loadFonts();
        baseFilesMap.put("general_buttons", new AssetFile("images/ui/general_buttons.atlas", TextureAtlas.class));
        baseFilesMap.put("general_screen_elements", new AssetFile("images/ui/general_screen_elements.atlas", TextureAtlas.class));

        // Cargamos los assets
        for(AssetFile asset : baseFilesMap.values()){
            assetManager.load(asset.path, asset.type);
        }
        // Paramos la aplicacion hasta que todos los assets estén cargados
        assetManager.finishLoading();
    }

    private static void initiateLoadProcess(){
        // Cargamos los assets
        for(AssetFile asset : filesMap.values()){
            assetManager.load(asset.path, asset.type);
        }
        // Paramos la aplicacion hasta que todos los assets estén cargados
        assetManager.finishLoading();
    }

    private static Object get(String mapKey){
        //return assetManager.get(filesMap.get(mapKey).path, filesMap.get(mapKey).type);
        if(filesMap.containsKey(mapKey))
            return assetManager.get(filesMap.get(mapKey).path, filesMap.get(mapKey).type);
        else
            return assetManager.get(baseFilesMap.get(mapKey).path, baseFilesMap.get(mapKey).type);

    }

    public static Texture getTexture(String mapKey){
        return (Texture) get(mapKey);
    }
    public static TextureAtlas getTextureAtlas(String mapKey){
        TextureAtlas textureAtlas = (TextureAtlas) get(mapKey);
        return textureAtlas;
    }

    public static Sound getSound(String mapKey){
        return (Sound) get(mapKey);
    }

    public static Music getMusic(String mapKey){
        return (Music) get(mapKey);
    }

    public static BitmapFont getFont(String mapKey){
        return (BitmapFont) get(mapKey);
    }

    private static void addSoundsToManagerList(){
        AudioManager audioManager = AudioManager.getInstance();
        Array<Sound> sounds = assetManager.getAll(Sound.class, new Array<Sound>());
        for(Sound sound : sounds){
            audioManager.addLoadedSoundToList(sound);
        }
    }

    // Atributos adicionales para los disntintos sprites de un atlas
    private static void addFileAttributes(String textureAtlasName, String jsonPath){
        FileHandle file = Gdx.files.internal(jsonPath);
        Json json = new Json();
        String jsonString = file.readString();
        AssetFileAttributes assetFileAttributes = json.fromJson(AssetFileAttributes.class,jsonString );
        filesAttributesMap.put(textureAtlasName, assetFileAttributes);
    }
    public static AssetFileAttributes getAssetFileAttributes(String mapKey){
        return filesAttributesMap.get(mapKey);
    }

    public static void dispose() {
        assetManager.dispose();

        filesAttributesMap.clear();
        filesMap.clear();
        baseFilesMap.clear();
    }
}
