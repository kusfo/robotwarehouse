package com.teenagemutantninjacoders.robotwarehouse.data;

import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.SortedIntList;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectEpisodeSelectionPanel;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.enums.GameAchievement;

import java.util.ArrayList;

/**
 * Created by JordiRM on 05/08/2018.
 */
public class GlobalGeneralData {
    private SortedIntList<Integer> episodesByNumber;
    private ArrayList<EpisodeDTO> episodesData;
    private int currentLevel = 0;
    private int currentEpisode = 0;
    public int lastEpisodePlayed = 0;
    public int lastLevelPlayed = 0;
    public boolean episodeHasBeenCompleted = false;
    public GlobalAttributes.EPISODE_SPECIAL_EVENT episodeSpecialEvent = GlobalAttributes.EPISODE_SPECIAL_EVENT.NONE;
    private int selectionScreenEnteringPanel = 0;
    private I18NBundle globalBundleData;
    private I18NBundle tutorialBundleData;
    private ArrayList<GameAchievement> achievementsWaitingToShow = new ArrayList<GameAchievement>();
    public boolean eventReviewModeActivated = false;    // Activar el modo para ver tutoriales y que luego vuelva al menu principal
    public int lastReviwingEventNumer = 1;

    public boolean debug_fastLevel = false;             // Para saltarte los menus y carteles de la partida
    public boolean debug_tutorialdisabled = false;      // Para saltarte los tutoriales
    public GlobalAttributes.ACTIVATION_CONDITION debug_dialogsAndEvents = GlobalAttributes.ACTIVATION_CONDITION.NORMAL;
    public boolean debug_dialoguedisabled = false;      // Para saltarte los dialogos
    public boolean debug_infiniteLevel = false;         // Para saltarte el game over y el nivel completo
    public boolean debug_selectlevel = false;           // Para poder seleccionar cualquier nivel
    public boolean debug_allepisodes_unblocked = false;
    public boolean debug_admobdisabled = false;         //Para desactivar los anuncios
    public boolean debug_drawTouchActivity = false;     // Se mostrará en pantalla donde has pulsado y la dirección del drag
    public boolean debug_showRankStats = false;
    public boolean debug_keys = false;                  // Teclas debug para para parar el juego, mostrar estadisticas, etc

    private static GlobalGeneralData instance;
    public GlobalGeneralData() {}

    public static GlobalGeneralData getInstance() {
        if(instance == null) {
            instance = new GlobalGeneralData();
        }
        return instance;
    }

    public void setEpisodesData(ArrayList<EpisodeDTO> episodesData){
        this.episodesData = episodesData;
    }

    public void setEpisodesByNumber(SortedIntList<Integer> episodeByNumber ){
        this.episodesByNumber = episodeByNumber;
    }

    public ArrayList<EpisodeDTO> getEpisodesData(){
        return episodesData;
    }

    public EpisodeDTO getEpisodeDataByIndex(int arrayIndex){
        return episodesData.get(arrayIndex);
    }

    public EpisodeDTO getEpisodeDataByNumber(int numEpisode){
        return episodesData.get(episodesByNumber.get(numEpisode));
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public void setCurrentEpisode(int currentEpisode) {
        this.currentEpisode = currentEpisode;
    }

    public int getSelectionScreenEnteringPanel() {
        return selectionScreenEnteringPanel;
    }

    public void setSelectionScreenEnteringPanel(int selectionScreenEnteringPanel) {
        this.selectionScreenEnteringPanel = selectionScreenEnteringPanel;
    }
    public void setGlobalBundleData(I18NBundle globalBundleData) {
        this.globalBundleData = globalBundleData;
    }

    public I18NBundle getGlobalBundleData() {
        return globalBundleData;
    }

    public I18NBundle getTutorialBundleData() {
        return tutorialBundleData;
    }

    public void setTutorialBundleData(I18NBundle tutorialBundleData) {
        this.tutorialBundleData = tutorialBundleData;
    }

    public void addAchievementWaitingToShow(GameAchievement achievement){
        achievementsWaitingToShow.add(achievement);
    }
    public ArrayList<GameAchievement> getAchievementsWaitingToShow(){
        return achievementsWaitingToShow;
    }
}
