package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;

import java.util.ArrayList;

/**
 * Created by JordiRM on 03/03/2017.
 */
public class AudioManager {
    private ArrayMap<SOUND, Long> filteredSounds = new ArrayMap<SOUND, Long>();

    private Music actualMusicAsset = null;
    private MUSIC actualMusic = MUSIC.NONE;
    private MUSIC_FADE actualMusicFade = MUSIC_FADE.NONE;
    private float musicVolume = 1.0f;
    private float newMusicVolume = 1.0f;
    private float musicMaxVolume;
    private float fadeInVelocity = 2f;
    private float fadeOutVelocity = 2f;
    private boolean soundActive;
    private ArrayList<Sound> loadSoundsList = new ArrayList<Sound>();
    private static AudioManager instance;
    private AudioManager(){
        soundActive = GlobalPreferencesData.getInstance().isGameSoundEnabled();
        musicMaxVolume = getMusicVolumeFromVolumeState(GlobalPreferencesData.getInstance().getMusicVolumeState());

        // Llenamos la lista de sonidos que queremos filtrar por tiempo
        filteredSounds.put(SOUND.BOX_DESTRUCTION, 0L);
        filteredSounds.put(SOUND.BOX_TELEPORT, 0L);
        filteredSounds.put(SOUND.BOX_LOST, 0L);
        filteredSounds.put(SOUND.BOX_DISABLING, 0L);
        filteredSounds.put(SOUND.ROTATING_FLOOR, 0L);
        filteredSounds.put(SOUND.CANNON_CHARGE, 0L);
        filteredSounds.put(SOUND.CANNON_SHOOT, 0L);
        filteredSounds.put(SOUND.FALL, 0L);
        filteredSounds.put(SOUND.FLOORBARRIER_SOLID, 0L);
        filteredSounds.put(SOUND.FLOORBARRIER_ENERGY, 0L);
        filteredSounds.put(SOUND.TRAPDOOR, 0L);
        filteredSounds.put(SOUND.RATLIEN_SMASHED_1, 0L);
        filteredSounds.put(SOUND.RATLIEN_SMASHED_2, 0L);
    }
    public static AudioManager getInstance(){
        if(instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void update(float delta){
        if(actualMusicFade != MUSIC_FADE.NONE) {
            if(newMusicVolume > musicMaxVolume) newMusicVolume = musicMaxVolume;
            if (actualMusicFade == MUSIC_FADE.FADE_OUT) {
                musicVolume = MathUtils.clamp(musicVolume - (fadeOutVelocity * delta), 0, 1.0f);
                if(actualMusicAsset != null) {
                    if (musicMaxVolume > 0){
                        actualMusicAsset.setVolume(musicVolume);
                    }
                    if (musicVolume == newMusicVolume){
                        if(newMusicVolume == 0) stopMusicNow();
                    }
                }
            } else {
                musicVolume = MathUtils.clamp(musicVolume + (fadeInVelocity * delta), 0, newMusicVolume);
                if(musicMaxVolume > 0 && actualMusicAsset != null) actualMusicAsset.setVolume(musicVolume);
                if (musicVolume == newMusicVolume) {
                    actualMusicFade = MUSIC_FADE.NONE;
                }
            }
        }
    }
    public long playSound(SOUND sound) {
        if (GlobalPreferencesData.getInstance().isGameSoundEnabled()) {
            return getProcessedSound(sound, 1, false);
        }
        return -1;
    }

    public long playSoundForced(SOUND sound) {
        // Emitimos el sonido aunque los sonidos estén desactivados
        return getProcessedSound(sound, 1, false);
    }

    public long playSound(SOUND...sounds) {
        ArrayList<SOUND> soundList = new ArrayList<SOUND>();
        for (SOUND sound : sounds) {
            soundList.add(sound);
        }
        return playSound(soundList.get(MathUtils.random(0, soundList.size() - 1)));
    }
    public long playLoopSound(SOUND sound){
        if (GlobalPreferencesData.getInstance().isGameSoundEnabled()) {
            return getProcessedSound(sound, 1, true);
        }
        return -1;
    }

    private long getProcessedSound(SOUND sound, float volume, boolean loop){
        Sound soundAsset = getSoundAsset(sound);
        float pitch = 1;
        switch(sound){
            case BOX_HIT: case RATLIEN_SMASHED_1: case RATLIEN_SMASHED_2: case RATLIEN_ENTER:
                pitch = MathUtils.random(0.95f, 1.05f);
                break;
        }

        //FILTERED SOUNDS
        int filteredIndex = filteredSounds.indexOfKey(sound);
        if(filteredIndex != -1){
            if(TimeUtils.timeSinceMillis(filteredSounds.getValueAt(filteredIndex)) > 120)
                filteredSounds.setValue(filteredIndex, TimeUtils.millis());
            else
                return -1;
        }

        if (loop)   return soundAsset.loop(volume, pitch, 0);
        else        return soundAsset.play(volume, pitch, 0);
    }

    public void stopSoundId(SOUND sound, long id){
        Sound soundAsset = getSoundAsset(sound);
        if(soundAsset != null) soundAsset.stop(id);
    }

    public void stopAllSounds(){
        for(int i = 0; i < loadSoundsList.size(); i++){
            loadSoundsList.get(i).stop();
        }
    }

    public void playMusic(MUSIC music, boolean loop, boolean fade){
        newMusicVolume = musicMaxVolume;
        this.fadeInVelocity = 2f;
        processPlayMusic(music, loop, fade);
    }
    public void playMusic(MUSIC music,boolean loop, boolean fade, float fadeVelocity){
        newMusicVolume = musicMaxVolume;
        this.fadeInVelocity = fadeVelocity;
        processPlayMusic(music, loop, fade);
    }

    private void processPlayMusic(MUSIC music, boolean loop, boolean fade){
        if(actualMusic == music) return;

        actualMusic = music;
        if (actualMusicAsset != null) actualMusicAsset.stop();

        actualMusicAsset = Assets.getMusic(music.getValue());
        actualMusicAsset.setLooping(loop);

        if(actualMusicAsset != null) {
            if (fade) {
                actualMusicFade = MUSIC_FADE.FADE_IN;
                musicVolume = 0;
                if(musicMaxVolume > 0) actualMusicAsset.setVolume(musicVolume);
                actualMusicAsset.setVolume(0);
            } else {
                actualMusicFade = MUSIC_FADE.NONE;
                musicVolume = musicMaxVolume;
                if(musicMaxVolume > 0){
                    actualMusicAsset.setVolume(musicVolume);
                }
                else{
                    actualMusicAsset.setVolume(0);
                }
            }
            actualMusicAsset.play();
        }
    }

    public void stopMusic(boolean fade){
        newMusicVolume = 0;
        if(actualMusicAsset != null) {
            if (!fade) stopMusicNow();
            else {
                fadeOutVelocity = 2f;
                actualMusicFade = MUSIC_FADE.FADE_OUT;
            }
        }
    }
    public void stopMusic(boolean fade, float fadeOutVelocity){
        newMusicVolume = 0;
        this.fadeOutVelocity = fadeOutVelocity;
        stopMusic(fade);
    }

    public void changeMusicVolume(boolean fade, float volume){
        float realNewVolume = musicMaxVolume * volume;
        if(fade) {
            fadeOutVelocity = 2f;
            if (realNewVolume >= musicVolume) actualMusicFade = MUSIC_FADE.FADE_IN;
            else actualMusicFade = MUSIC_FADE.FADE_OUT;
            newMusicVolume = realNewVolume;
        }
    }

    private void stopMusicNow(){
        actualMusicAsset.stop();
        actualMusic = MUSIC.NONE;
        actualMusicFade = MUSIC_FADE.NONE;
        actualMusicAsset = null;
    }

    public boolean isMusicPlaying(){
        return actualMusic != MUSIC.NONE;
    }

    public void activateSounds(boolean state){
        if(!state){
            stopAllSounds();
        }
        soundActive = state;
        GlobalPreferencesData.getInstance().setGameSoundEnabled(state);
    }

    public void setMusicMaxVolumeFromVolumeState(int newVolumeState){
        musicMaxVolume = getMusicVolumeFromVolumeState(newVolumeState);
        if(actualMusicAsset != null){
            if(actualMusicAsset.getVolume() != musicMaxVolume) {
                actualMusicAsset.setVolume(musicMaxVolume);
            }
        }
    }

    private float getMusicVolumeFromVolumeState(int volumeState){
        switch (volumeState){
            case 1: return 0.25f;
            case 2: return 0.60f;
            case 3: return 1.0f;
            default: return 0;
        }
    }

    public void addLoadedSoundToList(Sound sound){
        loadSoundsList.add(sound);
    }
    public void removeLoadedSoundFromList(Sound sound){
        loadSoundsList.remove(sound);
    }

    private Sound getSoundAsset(SOUND sound){
        return Assets.getSound(sound.getValue());
    }

    private enum MUSIC_FADE {
        NONE, FADE_IN, FADE_OUT
    }

    public enum MUSIC{
        NONE("none"),
        MAIN_SCREEN("mus_main"),
        SELECTION_SCREEN("mus_selectionScreen"),
        GAME_LEVEL("mus_gameLevel"),
        LEVEL_COMPLETED_PANEL("mus_levelCompletedPanel"),
        SCENE_GENERIC_01("mus_scene_generic_01");
        private String value;

        MUSIC(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static MUSIC fromString(String text) {
            if (text != null) {
                for (MUSIC var : MUSIC.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }

    public enum SOUND {
            BOX_HIT("snd_BoxHit_01"),
            BOX_DESTRUCTION("snd_boxDestruction"),
            ROBOT_SMASH("snd_robotSmash_01"),
            BOX_TELEPORT("snd_teleport_01"),
            BOX_LOST("snd_boxLost"),
            CANNON_SHOOT("snd_laserShooting_01"),
            CANNON_CHARGE("snd_laserCharging_01"),
            ROBOT_ALARM("snd_robotAlarm_01c"),
            ROBOT_EXPLOSION("snd_robotExplosion_01"),
            ROBOT_ENTER("snd_robotEnter_01"),
            ROBOT_BUBBLE("snd_robotBubble_01"),
            FALL("snd_fall_01"),
            ROBOT_PUSHING("snd_robotPushing"),
            ROTATING_FLOOR("snd_rotatingFloorImpulse"),
            FLOORBUTTON_ACTIVATE("snd_floorButtonActivate"),
            FLOORBUTTON_DEACTIVATE("snd_floorButtonDeactivate"),
            FLOORBARRIER_SOLID("snd_barrierSolid"),
            FLOORBARRIER_ENERGY("snd_barrierEnergy"),
            TRAPDOOR("snd_trapDoor"),
            BOX_POINTS("snd_BoxPointsAchieved"),
            OVERBASE_POINTS("snd_markPointsAchieved"),
            PANEL_ENTER("snd_panelEnter_01"),
            PANEL_LEAVES("snd_panelLeave_01"),
            PANEL_SHORT_ENTER("snd_panelShortEnter_01"),
            PANEL_SHORT_LEAVE("snd_panelShortLeave_01"),
            BOX_PANEL_ENTER("snd_panelBoxes_enter"),
            BOX_PANEL_LEAVE("snd_panelBoxes_leave"),
            BUTTON_GENERIC("snd_buttonClickGeneric"),
            BUTTON_CHANGE("snd_buttonClickChange"),
            LEVEL_COMPLETED("snd_level_completed_01"),
            EPISODE_COMPLETED("snd_episodeCompleted"),
            NEWS_SHORT("snd_news_short"),
            CHALLENGE_FAILED_ANNOUNCEMENT("snd_challengeFailedAnnouncement"),
            CHALLENGE_COMPLETED_ANNOUNCEMENT("snd_challengeCompleteAnnouncement"),
            LEVEL_FAILED("snd_level_failed"),
            CHALLENGE_COMPLETED("snd_challengeCompleted_01"),
            READY("snd_ready"),
            ADD_TOTAL_POINTS("snd_addTotalPoints"),
            RANK_STAR_1("snd_rankStar_1"),
            RANK_STAR_2("snd_rankStar_2"),
            RANK_STAR_3("snd_rankStar_3"),
            BOX_DISABLING("snd_boxDisabling"),
            BOX_GROUP_CREATION("snd_boxGroupCreation"),
            BOX_GROUP_LEAVE("snd_boxGroupLeave"),
            LIGHTS_LEVEL_COMPLETED("snd_lights_levelCompleted"),
            LIGHTS_GAME_OVER("snd_lights_gameOver"),
            RATLIEN_SMASHED_1("snd_ratlienImpactDead_01"),
            RATLIEN_SMASHED_2("snd_ratlienImpactDead_02"),
            RATLIEN_DEAD("snd_ratlienFallDead"),
            RATLIEN_CHEWING("snd_ratlienChewingLoop"),
            RATLIEN_ENTER("snd_ratlienEnter"),
            OVERBASE_TRIGGERED("snd_overBaseTriggered"),
            POWER_ACTIVATION("snd_powerActivation"),
            DISABLED_BUZZING("snd_disabledBuzzing"),
            ACTIVATE_FUMIGATION("snd_activateFumigation"),
            LEVEL_UNBLOCK_NORMAL("snd_levelUnblockNormal"),
            LEVEL_UNBLOCK_STAR("snd_levelUnblockStar"),
            EPISODE_UNBLOCK("snd_episodeUnblock"),
            UNBLOCK_FLYING_STAR("snd_unblocking_flying_star_01"),
            DIALOG_BUBBLE("snd_bubble");
        private String value;

        SOUND(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static SOUND fromString(String text) {
            if (text != null) {
                for (SOUND var : SOUND.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return BOX_HIT;
        }
    }
}
