package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.AdServices;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TrackingServices;
import com.teenagemutantninjacoders.robotwarehouse.display.painters.GamePainter;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.GameController;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectAnnouncement;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectCharacterDialogue;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GameUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.ScenarioEvent_1;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.enums.GameAchievement;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

import java.util.ArrayList;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

/**
 * Created by JordiRM on 29/04/2016.
 */
public class GameEventsManagerImpl implements GameEventsManager {
    private final IGameServiceClient playServices;
    private final TrackingServices trackingServices;
    private final AdServices adServices;
    private final PlatformServices platformServices;
    private final SavedGamesManager savedGamesManager;
    private final AchievementManager achievementManager;
    private GameController gameController;
    private GameUI gameUI;
    private GamePainter gamePainter;
    private ArrayList<GameEvent> eventsToDelete = new ArrayList<GameEvent>();
    private boolean waitingChallengeComplete = false;

    private int lastRemainingRedBoxes = 0;
    private int lastRemainingBlueBoxes = 0;
    private int lastRemainingYellowBoxes = 0;
    private float levelEventTimeTrigger = 0;        // Para ejecutar el evento de partida asignado en un margen de tiempo
    private class ActiveVisualEvents {
        public int boxPointsEvents = 0;
        public int overbasePointsEvents = 0;
        public int powerActivationEvents = 0;
    }
    private ActiveVisualEvents activeVisualEvents;

    private class GameEvent{
        private MANAGER_EVENT event;
        private float x = 0;
        private float y = 0;
        public GameEvent(MANAGER_EVENT newEvent){
            this.event = newEvent;
        }
        public GameEvent(MANAGER_EVENT newEvent, float x, float y){
            this.event = newEvent;
            this.x = x;
            this.y = y;
        }
        public MANAGER_EVENT getEvent() {
            return event;
        }
        public float getEventX() {
            return x;
        }
        public float getEventY() {
            return y;
        }
    }

    private class NewBoxPointsEvent extends GameEvent{
        private int combo;
        private int points;
        public NewBoxPointsEvent(MANAGER_EVENT newEvent, float x, float y, int newCombo, int newPoints){
            super(newEvent, x, y);
            combo = newCombo;
            points = newPoints;
        }
        public int getCombo(){return combo;}
        public int getPoints(){return points;}
    }

    private class NewOverBasePointsEvent extends GameEvent{
        private int points;
        public NewOverBasePointsEvent(MANAGER_EVENT newEvent, float x, float y, int newPoints){
            super(newEvent, x, y);
            points = newPoints;
        }
        public int getPoints(){return points;}
    }

    private class NewOverBasePowerEvent extends GameEvent{
        private GlobalAttributes.POWER power;
        public NewOverBasePowerEvent(MANAGER_EVENT newEvent, float x, float y, GlobalAttributes.POWER power){
            super(newEvent, x, y);
            this.power = power;
        }
        public GlobalAttributes.POWER getPower(){return power;}
    }

    private class ChangeScreenEvent extends GameEvent{
        private RobotWarehouseGame.SCREEN_TYPE screenType;
        public ChangeScreenEvent(MANAGER_EVENT newEvent, RobotWarehouseGame.SCREEN_TYPE screenType){
            super(newEvent);
            this.screenType = screenType;
        }
        public RobotWarehouseGame.SCREEN_TYPE getScreenType() {
            return screenType;
        }
    }

    private class BoxDisablingEvent extends GameEvent{
        private GlobalAttributes.COLOR color;
        public BoxDisablingEvent(MANAGER_EVENT newEvent, GlobalAttributes.COLOR color){
            super(newEvent);
            this.color = color;
        }
        public GlobalAttributes.COLOR getColor(){return color;}
    }

    private class GameZoneFadeEvent extends GameEvent{
        private boolean state;
        private float speed;
        public GameZoneFadeEvent(MANAGER_EVENT newEvent, boolean state, float speed){
            super(newEvent);
            this.state = state;
            this.speed = speed;
        }
        public boolean getState(){return state;}
        public float getSpeed(){return speed;}
    }

    private ArrayList<GameEvent> gameEvents = new ArrayList<GameEvent>();

    // Constructor
    public GameEventsManagerImpl(GameController gameController, GameUI gameUI, GamePainter gamePainter,
                                 IGameServiceClient playServices, TrackingServices trackingServices, AdServices adServices, PlatformServices platformServices,
                                 SavedGamesManager savedGamesManager, AchievementManager achievementManager){
        this.gameController = gameController;
        this.gameUI = gameUI;
        this.gamePainter = gamePainter;
        this.playServices = playServices;
        this.trackingServices = trackingServices;
        this.adServices = adServices;
        this.platformServices = platformServices;
        this.savedGamesManager = savedGamesManager;
        this.achievementManager = achievementManager;
        lastRemainingRedBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getRed();
        lastRemainingBlueBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getBlue();
        lastRemainingYellowBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getYellow();

        activeVisualEvents = new ActiveVisualEvents();
    }

    @Override
    public void beginLevelEnter(float safePauseTime){
        // Evento de entrada al nivel
        if(!GlobalGeneralData.getInstance().debug_fastLevel){
            if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.ENTERING) {
                addNewLevelEnterEvent();
                this.platformServices.pokeForRateNotification();
                levelEventTimeTrigger = safePauseTime;
            }
            else if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.RESTARTING){
                gameUI.screenFadeIn(0.2f);
                addNewLevelStartEvent();
                gameController.getSpaceDockManager().shipStationedAction();
            }
        }
        else{
            gameUI.screenFadeIn(0.2f);
            addNewLevelStartEvent();
            gameController.getSpaceDockManager().shipStationedAction();
        }
        AudioManager.getInstance().playMusic(AudioManager.MUSIC.GAME_LEVEL, true, false);
        setLevelRankStats();
    }

    @Override
    public void update(float delta){
        // Tiempo del nivel
        if(GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
            if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                if(!GlobalGeneralData.getInstance().debug_infiniteLevel)
                    GlobalLevelData.getInstance().decreaseTimeLevel(delta * 1);
            }
        }

        // Ejecución de eventos en espera
        if(gameEvents.size() > 0){
            ExecuteEvents();
        }

        if(levelEventTimeTrigger > 0){
            levelEventTimeTrigger -= 1 * delta;
            if(levelEventTimeTrigger < 0) levelEventTimeTrigger = 0;
        }

        processTimeEvents();
    }

    private void ExecuteEvents(){
        for(int i = 0; i < gameEvents.size(); i++){
            switch(gameEvents.get(i).getEvent()){

                case LEVEL_ENTER:
                    if(levelEventTimeTrigger == 0) {
                        gameUI.screenFadeIn();
                        gamePainter.gameZoneFade(true);
                        gameController.getSpaceDockManager().shipEnteringAction();
                        String introductionEvent = gameController.getLevelModel().getIntroductionEventId();
                        // Si hay un evento de introduccion y no esta marcado como visto lo ejecutamos primero
                        boolean showIntroductionEvent = false;
                        if (!introductionEvent.equals("")) {
                            if(GlobalGeneralData.getInstance().eventReviewModeActivated) {
                                showIntroductionEvent = true;
                            }
                            else if (GlobalGeneralData.getInstance().debug_dialogsAndEvents == GlobalAttributes.ACTIVATION_CONDITION.ALWAYS ||
                                    (GlobalGeneralData.getInstance().debug_dialogsAndEvents == GlobalAttributes.ACTIVATION_CONDITION.NORMAL &&
                                            !GlobalPreferencesData.getInstance().getLevelIntroductionEventViewStatus(introductionEvent))) {
                                showIntroductionEvent = true;
                            }
                        }

                        if (showIntroductionEvent) {
                            gameUI.newIntroductionEvent(introductionEvent);
                        } else {
                            gameUI.newMissionPanel();
                        }
                        eventsToDelete.add(gameEvents.get(i));

                    }
                    break;
                case LEVEL_ENTER_AFTER_INTRODUCTION_EVENT:
                    gameUI.newMissionPanel();
                    eventsToDelete.add(gameEvents.get(i));
                    break;
                case LEVEL_START:
                    if (gameUI.getScreenFadeStatus() == GameUI.SCREEN_FADE_STATUS.FULL_IN) {
                        GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.NONE);
                        gamePainter.gameZoneFade(false);
                        gameUI.newStartLevel(); // Esto mostrara el cartel de empezar y activará el estatus PLAYING
                        gameUI.deployGamePanels(0.4f);
                        deploySpaceDockMonitor(true);
                        eventsToDelete.add(gameEvents.get(i));
                        trackingServices.trackLevelStart(GlobalGeneralData.getInstance().getCurrentLevel());
                    }
                    break;

                case LEVEL_COMPLETED:
                    if(levelEventTimeTrigger == 0) {
                        if(activeVisualEvents.boxPointsEvents == 0 && activeVisualEvents.powerActivationEvents == 0 &&
                                GlobalLevelData.getInstance().getBoxesTeleporting() == 0) {
                            int currentEpisode = GlobalGeneralData.getInstance().getCurrentEpisode();
                            int currentLevel = GlobalGeneralData.getInstance().getCurrentLevel();
                            AudioManager.getInstance().stopMusic(true);
                            boolean firstTimeCompleted = GlobalPreferencesData.getInstance().getLevelStatus(currentLevel, currentEpisode) != GameConstants.CONTENT_STATUS_COMPLETED;
                            processFinalLevelResults();
                            processChallengeCompletion();

                            gamePainter.gameZoneFade(true);
                            gameUI.newLevelCompleted();
                            gameUI.retractGamePanels(1.4f);
                            gameController.getSpaceDockManager().deactivateDockMonitor();
                            gameController.getSpaceDockManager().shipLeavingAction();
                            eventsToDelete.add(gameEvents.get(i));
                            if(currentLevel > 0) {

                                if (savedGamesManager.isSavedRemoteGameEnabled() && GlobalPreferencesData.getInstance().isCloudSaveSynced()) {
                                    savedGamesManager.pushSavedGame(GameConstants.CLOUD_GAME_ID);
                                }
                                String introductionEvent = gameController.getLevelModel().getIntroductionEventId();
                                if (!introductionEvent.equals("")) {
                                    GlobalPreferencesData.getInstance().setLevelIntroductionEventViewCompleted(introductionEvent);
                                }
                                trackingServices.trackLevelCompleted(currentLevel);
                                int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, currentLevel);
                                GlobalAttributes.LEVEL_TYPE level_type = GlobalAttributes.LEVEL_TYPE.fromString(GlobalGeneralData.getInstance().getEpisodeDataByNumber(currentEpisode).getLevels().get(levelIndex).getType());
                                achievementManager.checkLevelCompletedRelatedAchievement(currentEpisode, currentLevel, firstTimeCompleted, level_type);
                            }
                        }
                    }
                    break;

                case GAME_OVER:
                    if(levelEventTimeTrigger == 0 && activeVisualEvents.powerActivationEvents == 0) {
                        gamePainter.gameZoneFade(true);
                        gameUI.newGameOver();
                        gameUI.retractGamePanels(1.4f);
                        gameController.getSpaceDockManager().deactivateDockMonitor();
                        gameController.getSpaceDockManager().shipLeavingAction();
                        eventsToDelete.add(gameEvents.get(i));
                        trackingServices.trackLevelFailed(GlobalGeneralData.getInstance().getCurrentLevel());
                    }
                    break;

                case RESTART_LEVEL:
                    if(gameUI.getScreenFadeStatus() == GameUI.SCREEN_FADE_STATUS.FULL_IN){
                        gameUI.screenFadeOut();
                    }
                    else if (gameUI.getScreenFadeStatus() == GameUI.SCREEN_FADE_STATUS.FULL_OUT){
                        AudioManager.getInstance().stopMusic(false);
                        AudioManager.getInstance().stopAllSounds();
                        GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.RESTARTING);
                        trackingServices.trackLevelRestarted(GlobalGeneralData.getInstance().getCurrentLevel());
                        gameController.initializeLevel();
                        eventsToDelete.add(gameEvents.get(i));
                    }
                    break;
                case SELECTION_SCREEN_AFTER_AD:
                    changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN);
                    break;
                case ENTER_PAUSE_MENU:
                    if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                        GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PAUSED);
                        gameUI.retractGamePanels(0);
                        gameUI.newPauseMenu();
                        eventsToDelete.add(gameEvents.get(i));
                    }
                    break;

                case LEAVE_PAUSE_MENU:
                        GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PLAYING);
                        gameUI.deployGamePanels(0);
                        eventsToDelete.add(gameEvents.get(i));
                    break;

                case CHANGE_SCREEN:
                    if(gameUI.getScreenFadeStatus() == GameUI.SCREEN_FADE_STATUS.FULL_IN){
                        gameUI.screenFadeOut();
                    }
                    else if (gameUI.getScreenFadeStatus() == GameUI.SCREEN_FADE_STATUS.FULL_OUT) {
                        AudioManager.getInstance().stopMusic(false);
                        AudioManager.getInstance().stopAllSounds();
                        GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.ENTERING);

                        if(((ChangeScreenEvent) gameEvents.get(i)).screenType == RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN){
                            if(!GlobalGeneralData.getInstance().debug_admobdisabled && adServices.mustShowInterstitial() && adServices.isInterstitialLoaded()) {
                                adServices.showInterstitialAd();
                                eventsToDelete.add(gameEvents.get(i));
                            } else {
                                GlobalGeneralData.getInstance().setSelectionScreenEnteringPanel(1); // Selección de nivel
                                gameController.getGame().goToScreen(((ChangeScreenEvent) gameEvents.get(i)).getScreenType());
                                eventsToDelete.add(gameEvents.get(i));
                            }
                        } else {
                            gameController.getGame().goToScreen(((ChangeScreenEvent) gameEvents.get(i)).getScreenType());
                            eventsToDelete.add(gameEvents.get(i));
                        }
                    }
                    break;

                case CHANGE_IN_BOXES:
                    processChangeInBoxes();
                    eventsToDelete.add(gameEvents.get(i));
                    break;

                case FLOATING_LOST_BOX:
                    gameUI.newFloatingLostBox(gameEvents.get(i).getEventX(), gameEvents.get(i).getEventY());
                    eventsToDelete.add(gameEvents.get(i));
                    break;

                case BOX_POINTS:
                    GlobalLevelData.LEVEL_EVENT levelEvent = GlobalLevelData.getInstance().getLevelEvent();
                    NewBoxPointsEvent gameEvent = (NewBoxPointsEvent) gameEvents.get(i);
                    if(levelEvent == GlobalLevelData.LEVEL_EVENT.NONE || levelEvent == GlobalLevelData.LEVEL_EVENT.COMPLETED) {
                        // Los puntos de cajas esperarán a que se acaben de mostrar los puntos de suelo.
                        if(activeVisualEvents.overbasePointsEvents == 0) {
                            GlobalLevelData.getInstance().addScore(gameEvent.getPoints());
                            achievementManager.checkBoxesTeleportRelatedAchievement(gameEvent.getCombo());
                            gameUI.newBoxPoints(gameEvent.getEventX(), gameEvent.getEventY(), gameEvent.getCombo(), gameEvent.getPoints());
                            trackingServices.trackBoxesTeleported(GlobalGeneralData.getInstance().getCurrentLevel(),
                                    gameEvent.getCombo(), gameEvent.getPoints());

                            eventsToDelete.add(gameEvents.get(i));
                        }
                    } else {
                        trackingServices.trackBoxesTeleported(GlobalGeneralData.getInstance().getCurrentLevel(),
                                gameEvent.getCombo(), gameEvent.getPoints());
                        eventsToDelete.add(gameEvents.get(i));
                    }
                    break;

                case OVERBASE_POINTS:
                    levelEvent = GlobalLevelData.getInstance().getLevelEvent();
                    NewOverBasePointsEvent overBasePointsEvent = (NewOverBasePointsEvent) gameEvents.get(i);
                    if(levelEvent == GlobalLevelData.LEVEL_EVENT.NONE || levelEvent == GlobalLevelData.LEVEL_EVENT.COMPLETED) {
                        GlobalLevelData.getInstance().addScore(overBasePointsEvent.getPoints());
                        GlobalLevelData.getInstance().increaseOverBasePointsActivated();
                        achievementManager.checkPointsRelatedAchievement();
                        gameUI.newOverBasePoints(overBasePointsEvent.getEventX(), overBasePointsEvent.getEventY(), overBasePointsEvent.getPoints());
                    }
                    eventsToDelete.add(gameEvents.get(i));
                    break;

                case OVERBASE_POWER:
                    NewOverBasePowerEvent triggerPowerEvent = (NewOverBasePowerEvent) gameEvents.get(i);
                    if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                        if (activeVisualEvents.boxPointsEvents == 0 && GlobalLevelData.getInstance().getBoxesTeleporting() == 0) {
                            GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PAUSED);
                            gameUI.newOverBasePower(triggerPowerEvent.getEventX(), triggerPowerEvent.getEventY(), triggerPowerEvent.getPower());
                            achievementManager.checkAchievement(GameAchievement.FIRST_POWER_USED);
                            eventsToDelete.add(gameEvents.get(i));
                        }
                    }else {
                        activeVisualEvents.powerActivationEvents = 0;
                        eventsToDelete.add(gameEvents.get(i));
                    }
                    break;

                case COLOR_DISABLING:
                    if(GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
                        BoxDisablingEvent disablingGameEvent = (BoxDisablingEvent) gameEvents.get(i);
                        if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                            switch(disablingGameEvent.getColor()){
                                case RED: gameUI.newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT.RED_DISABLED);
                                    break;
                                case BLUE: gameUI.newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT.BLUE_DISABLED);
                                    break;
                                case YELLOW: gameUI.newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT.YELLOW_DISABLED);
                                    break;
                            }
                        }
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_DISABLING);
                        trackingServices.trackBoxColorDisabled(GlobalGeneralData.getInstance().getCurrentLevel(),
                                disablingGameEvent.getColor().getValue());
                        eventsToDelete.add(gameEvents.get(i));
                    }
                    break;
                case GAME_ZONE_FADE:
                    boolean state = ((GameZoneFadeEvent) gameEvents.get(i)).getState();
                    float speed = ((GameZoneFadeEvent) gameEvents.get(i)).getSpeed();
                    gamePainter.gameZoneFade(state, speed);
                    eventsToDelete.add(gameEvents.get(i));
                    break;
            }
        }

        // Borrado de eventos despues de procesar toda la lista
        GameEvent currentEvent;
        while(eventsToDelete.size() >0){
            currentEvent = eventsToDelete.get(0);
            gameEvents.remove(currentEvent);
            eventsToDelete.remove(0);
        }
    }

    @Override
    public void processLevelEnding(){
        GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PAUSED);
        if(waitingChallengeComplete){
            waitingChallengeComplete = false;
            gameUI.newChallengeCompleted();
        } else {
            if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.COMPLETED) {
                gameUI.newLevelCompletedPanel();
                gameUI.showCharacterAlone(GUIobjectCharacterDialogue.CHARACTER_POSE.CELEBRATING, GUIobjectCharacterDialogue.SIDE.LEFT, 0.8f, 0);
            } else {
                gameUI.newGameOverPanel();
                gameUI.showCharacterAlone(GUIobjectCharacterDialogue.CHARACTER_POSE.DISCOURAGED, GUIobjectCharacterDialogue.SIDE.LEFT, 0.4f, 50);
            }
        }
    }

    @Override
    public void addNewLevelEnterEvent(){
        gameEvents.add( new GameEvent(MANAGER_EVENT.LEVEL_ENTER));
    }
    @Override
    public void addNewLevelStartEvent(){
        gameEvents.add( new GameEvent(MANAGER_EVENT.LEVEL_START));
    }
    @Override
    public void addNewLevelGameOverEvent(){
        gameEvents.add( new GameEvent(MANAGER_EVENT.GAME_OVER));
    }
    @Override
    public void addNewLevelCompletedEvent(){
        gameEvents.add( new GameEvent(MANAGER_EVENT.LEVEL_COMPLETED));
    }

    @Override
    public void addNewBoxPointsEvent(float x, float y, int combo){
        int puntosGanados = getPointsFromBoxesChain(combo);
        GameEvent newEvent = new NewBoxPointsEvent( MANAGER_EVENT.BOX_POINTS, x, y, combo, puntosGanados);
        activeVisualEvents.boxPointsEvents += 1;
        gameEvents.add(newEvent);
    }
    @Override
    public void addNewOverBasePointsEvent(float x, float y, boolean triggerAction){
        if(triggerAction)  activeVisualEvents.overbasePointsEvents += 1;
        else{
            GameEvent newEvent = new NewOverBasePointsEvent(MANAGER_EVENT.OVERBASE_POINTS, x, y, GameConstants.BASE_OVERBASE_POINTS);
            gameEvents.add(newEvent);
        }
    }
    @Override
    public void addNewOverBasePowerEvent(float x, float y, GlobalAttributes.POWER power){
        GameEvent newEvent = new NewOverBasePowerEvent(MANAGER_EVENT.OVERBASE_POWER, x, y, power);
        activeVisualEvents.powerActivationEvents += 1;
        gameEvents.add(newEvent);
    }
    @Override
    public void finalizeOverBasePointsEvent(){
        activeVisualEvents.overbasePointsEvents -= 1;
    }
    @Override
    public void finalizeBoxPointsEvent(){
        activeVisualEvents.boxPointsEvents -= 1;
    }
    @Override
    public void finalizePowerActivationEvent(GlobalAttributes.POWER power){
        if(activeVisualEvents.powerActivationEvents > 0) activeVisualEvents.powerActivationEvents -= 1;
        GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PLAYING);
        // Una vez finalizado el cartel ejecutamos el poder
        gameController.getPowerManager().executePower(power);
    }

    @Override
    public void addChangeInBoxesEvent(){
        gameEvents.add(new GameEvent(MANAGER_EVENT.CHANGE_IN_BOXES));
    }

    @Override
    public void addColorDisablingEvent(GlobalAttributes.COLOR color){
        gameEvents.add(new BoxDisablingEvent(MANAGER_EVENT.COLOR_DISABLING, color));
    }

    @Override
    public void addSelectionScreenAfterAdEvent() {
        gameEvents.add(new GameEvent(MANAGER_EVENT.SELECTION_SCREEN_AFTER_AD));
    }

    @Override
    public void addRestartLevelEvent(){
        gameEvents.add(new GameEvent(MANAGER_EVENT.RESTART_LEVEL));
    }

    @Override
    public void addFinishLevelIntroductionEvent() {
        if(!GlobalGeneralData.getInstance().eventReviewModeActivated)
            gameEvents.add(new GameEvent(MANAGER_EVENT.LEVEL_ENTER_AFTER_INTRODUCTION_EVENT));
        else
            gameEvents.add(new ChangeScreenEvent(MANAGER_EVENT.CHANGE_SCREEN, RobotWarehouseGame.SCREEN_TYPE.MAIN_SCREEN));
    }

    @Override
    public void addEnterPauseMenu() {
        if(!existGameEvent(MANAGER_EVENT.ENTER_PAUSE_MENU)) {
            gameEvents.add(new GameEvent(MANAGER_EVENT.ENTER_PAUSE_MENU));
        }
    }
    @Override
    public void addLeavePauseMenu() {
        gameEvents.add(new GameEvent(MANAGER_EVENT.LEAVE_PAUSE_MENU));
    }
    @Override
    public void addGameZoneFade(boolean state, float speed){
        gameEvents.add(new GameZoneFadeEvent(MANAGER_EVENT.GAME_ZONE_FADE, state, speed));
    }

    @Override
    public void addFloatingLostBox(float x, float y){
        gameEvents.add(new GameEvent(MANAGER_EVENT.FLOATING_LOST_BOX, x, y));
    }

    @Override
    public void makeCharacterLeave(){
        gameUI.makeCharacterLeave();
    }

    @Override
    public void changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE screenType){
        gameEvents.add(new ChangeScreenEvent(MANAGER_EVENT.CHANGE_SCREEN, screenType));
    }

    @Override
    public void enemyDestroyed(GlobalAttributes.CHALLENGE_OBJECT enemy) {
        if(enemy.equals(GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT) || enemy.equals(GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT)) {
            GlobalPreferencesData.getInstance().addRobotKilled(true);
            GlobalLevelData.getInstance().addRobotKilled();
            achievementManager.checkRobotRelatedAchievement();
        }
    }

    @Override
    public void executeScenarioEvent(int eventNumber){
        gameUI.newScenarioEvent(new ScenarioEvent_1(gameController.getLevelModel(), gameUI.getCharacterDialogue()));
    }

    @Override
    public void deploySpaceDockMonitor(boolean status){
        if(status) gameController.getSpaceDockManager().activateDockMonitor();
        else gameController.getSpaceDockManager().deactivateDockMonitor();
    }
    @Override
    public void deployScorePanels(float delay){
        gameUI.deployScoresPanel(delay);
    }
    @Override
    public void retractScorePanels(float delay){
        gameUI.retractScoresPanel(delay);
    }

    @Override
    public void deployPauseButtonPanel(float delay){
        gameUI.deployPauseButtonPanel(delay);
    }
    @Override
    public void submitNewScore(int score) {
        if(playServices.isSessionActive()) {
            playServices.submitToLeaderboard("",score,"");
        }
    }

    @Override
    public void showWaitingAchievements(){
        achievementManager.executeWaitingAchievements();
    }

    private void processChangeInBoxes(){
        if(!GlobalGeneralData.getInstance().debug_infiniteLevel) {
            if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                // Nivel Completado
                if (GlobalLevelData.getInstance().getRequestedBoxes() <= 0) {
                    GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.COMPLETED);
                    levelEventTimeTrigger = 1.8f;
                    addNewLevelCompletedEvent();
                }
            }
        }

        if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
            // Anulacion de cajas (cajas grises)
            int redBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getRed();
            if (redBoxes < 3 && redBoxes > 0 && lastRemainingRedBoxes > 2) {
                addColorDisablingEvent(GlobalAttributes.COLOR.RED);
                disableBoxColor(GlobalAttributes.COLOR.RED);
            }
            int blueBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getBlue();
            if (blueBoxes < 3 && blueBoxes > 0 && lastRemainingBlueBoxes > 2) {
                addColorDisablingEvent(GlobalAttributes.COLOR.BLUE);
                disableBoxColor(GlobalAttributes.COLOR.BLUE);
            }
            int yellowBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getYellow();
            if (yellowBoxes < 3 && yellowBoxes > 0 && lastRemainingYellowBoxes > 2) {
                addColorDisablingEvent(GlobalAttributes.COLOR.YELLOW);
                disableBoxColor(GlobalAttributes.COLOR.YELLOW);
            }
        }

        if(!GlobalGeneralData.getInstance().debug_infiniteLevel) {
            // Cajas insuficientes para completar el nivel
            checkAreEnoughBoxes();
        }
    }

    private void checkAreEnoughBoxes(){
        if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
            int activeBoxesLeft = GlobalLevelData.getInstance().getRemainingBoxes().getTransportableBoxes();
            if (activeBoxesLeft < GlobalLevelData.getInstance().getRequestedBoxes()) {
                GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.WITHOUT_BOXES);
                addNewLevelGameOverEvent();
                AudioManager.getInstance().stopMusic(true);
                gameUI.activateInsufficientStockEvent();
                levelEventTimeTrigger = 1.8f; // Esperamos un poco antes de lanzar el cartel
            }
        }
    }

    private void processTimeEvents(){
        // Tiempo agotado
        if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
            if (GlobalLevelData.getInstance().getLevelTime() <= 0) {
                GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.TIME_OUT);
                addNewLevelGameOverEvent();
                AudioManager.getInstance().stopMusic(true);
                gameUI.activateTimeOutEvent();
                levelEventTimeTrigger = 1.8f; // Esperamos un poco antes de lanzar el cartel
            }
        }
    }

    private void disableBoxColor(GlobalAttributes.COLOR color){
        switch(color){
            case RED:
                GlobalLevelData.getInstance().getRemainingBoxes().setRed(0);
                lastRemainingRedBoxes = 0;
                break;
            case BLUE:
                GlobalLevelData.getInstance().getRemainingBoxes().setBlue(0);
                lastRemainingBlueBoxes = 0;
                break;
            case YELLOW:
                GlobalLevelData.getInstance().getRemainingBoxes().setYellow(0);
                lastRemainingYellowBoxes = 0;
                break;
        }
        ArrayList<BoardObjectModel> objectsToDisable = gameController.getLevelModel().getCurrentBoardModel().getBoxesByColor(color);
        for (BoardObjectModel box: objectsToDisable) {
            if(box.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE || box.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.MOVING){
                gameUI.newBoxDisabled(box);
                ((BoxModel)box).disableBox();
            }
        }
        if(!GlobalGeneralData.getInstance().debug_infiniteLevel) checkAreEnoughBoxes();
    }

    /** Devuelve los puntos de una cadena de cajas.*/
    private int getPointsFromBoxesChain(int chain){
        int basePoints = GameConstants.BASE_BOX_POINTS;
        if(chain > 3)
            return (basePoints * chain) + ((chain - 3) * 15); // +15 por caja adicional
        else
            return basePoints * chain;
    }

    /** Devuelve los puntos que generarían todas las cadenas de esa cantidad que permita el totalColor.
     * Luego se suma una posible cadena sobrante si es de un mínimo de 3. */
    private int getPointsFromBoxesChain(int chain, int totalColor){

        int points = 0;
        int basePoints = GameConstants.BASE_BOX_POINTS;
        int limitedChains = MathUtils.floor(totalColor / chain);
        int chainRemainder = totalColor % chain;

        for(int i = 0; i < limitedChains; i++){
            points += (basePoints  * chain) + ((chain - 3) * 15);
        }
        if(chainRemainder >= 3)
            points += (basePoints  * chainRemainder) + ((chainRemainder - 3) * 15);

        return points;
    }


    private void processChallengeCompletion(){
        int currentEpisode = GlobalGeneralData.getInstance().getCurrentEpisode();
        int currentLevel = GlobalGeneralData.getInstance().getCurrentLevel();
        ChallengeManager.getInstance().processEndLevelChallengeCompletion();
        waitingChallengeComplete = ChallengeManager.getInstance().isChallengeCompleted();
        if(currentEpisode == 0)
            return;
        if(waitingChallengeComplete) {
            if (!GlobalPreferencesData.getInstance().getLevelChallengeStatus(currentLevel, currentEpisode)) {
                GlobalPreferencesData.getInstance().addChallengeCompletedToEpisode(currentEpisode);
                GlobalPreferencesData.getInstance().addChallengeToTotalCompleted();
                achievementManager.checkChallengeRelatedAchievement(currentEpisode);
                GlobalPreferencesData.getInstance().saveLevelChallengeCompletion(currentLevel, currentEpisode);
            }
            else{
                waitingChallengeComplete = false; // Si ya lo teniamos completo no queremos mostrar el cartel de nuevo
            }
        }
    }

    private void processFinalLevelResults(){
        boolean actualizedLevel = false;
        int currentEpisode = GlobalGeneralData.getInstance().getCurrentEpisode();
        int currentLevel = GlobalGeneralData.getInstance().getCurrentLevel();
        if(currentEpisode == 0)
            return;
        int baseLevelPoints = GlobalLevelData.getInstance().getLevelScore();
        int levelTime = MathUtils.floor(GlobalLevelData.getInstance().getLevelTime());
        int lotsBoxes = GlobalLevelData.getInstance().getLostBoxes();

        int pointsFromTime = getPointsFromTime(levelTime);
        int penaltyFromLostBoxes = getPenaltyFromLostBoxes(lotsBoxes);
        int totalScore = baseLevelPoints + pointsFromTime - penaltyFromLostBoxes;
        int rankAchieved = getRankAchieved(totalScore);
        boolean newRecord = false;

        // Si el nivel no estaba completado lo marcamos como completado
        if(GlobalPreferencesData.getInstance().getLevelStatus(currentLevel, currentEpisode) == GameConstants.CONTENT_STATUS_UNBLOCKED){
            GlobalPreferencesData.getInstance().setLevelStatus(currentLevel, currentEpisode, GameConstants.CONTENT_STATUS_COMPLETED);

            // Si era el último nivel, damos por completado el episodio y lo guardamos
            int goalLevel = GlobalGeneralData.getInstance().getEpisodeDataByNumber(currentEpisode).getGoalLevel();
            if(currentLevel == goalLevel){
                GlobalPreferencesData.getInstance().setEpisodeStatus(currentEpisode, GameConstants.CONTENT_STATUS_COMPLETED);
                achievementManager.checkEpisodeRelatedAchievement(currentEpisode);

                // Guardamos el aviso de compleción para que el panel de niveles lo sepa y muestre el cartel de completo
                GlobalGeneralData.getInstance().episodeHasBeenCompleted = true;
            }
            actualizedLevel = true;
        }

        // Grabamos la puntuacion si es mayor que la anterior
        int previousLevelScore = GlobalPreferencesData.getInstance().getLevelScore(currentLevel, currentEpisode);
        if(totalScore > previousLevelScore){
            if(previousLevelScore > 0) newRecord = true;
            GlobalPreferencesData.getInstance().saveLevelScore(currentLevel, currentEpisode, totalScore);
            GlobalPreferencesData.getInstance().addScoreToEpisode(currentEpisode, totalScore - previousLevelScore);
            GlobalPreferencesData.getInstance().addScoreToTal(totalScore - previousLevelScore);
            actualizedLevel = true;
        }

        // Grabamos el rango conseguido si es mayor que el anterior y actualizamos el procentaje de estrellas acumuladas
        int previousRankAchieved = GlobalPreferencesData.getInstance().getLevelStars(currentLevel, currentEpisode);
        if(rankAchieved > previousRankAchieved) {
            GlobalPreferencesData.getInstance().setLevelStars(currentLevel, currentEpisode, rankAchieved);
            GlobalPreferencesData.getInstance().addStarsToEpisode(currentEpisode, rankAchieved - previousRankAchieved);
            GlobalPreferencesData.getInstance().addStarsToTotal(rankAchieved - previousRankAchieved);
            achievementManager.checkStarRelatedAchievement(currentEpisode);
            actualizedLevel = true;
        }

        if(actualizedLevel){
            GlobalPreferencesData.getInstance().setLevelActualized(currentLevel, currentEpisode, true);
        }

        // Guardamos los resultados en la clase de reultados finales
        GlobalLevelData.getInstance().getFinalLevelResults().setPointsFromTime(pointsFromTime);
        GlobalLevelData.getInstance().getFinalLevelResults().setPenaltyFromLostBoxes(penaltyFromLostBoxes);
        GlobalLevelData.getInstance().getFinalLevelResults().setTotalLevelScore(totalScore);
        GlobalLevelData.getInstance().getFinalLevelResults().setRankAchieved(rankAchieved);
        GlobalLevelData.getInstance().getFinalLevelResults().setNewRecord(newRecord);
    }

    private int getPointsFromSavedBoxes(int savedBoxes){
        return savedBoxes * GameConstants.BASE_SAVE_BOXES_POINTS;
    }

    private int getPenaltyFromLostBoxes(int lostBoxes){
        return lostBoxes * GameConstants.BASE_LOST_BOXES_POINTS;
    }

    private int getPointsFromTime(int time){
        return time * GlobalLevelData.getInstance().getTimeMultiplier();

    }

    private void setLevelRankStats(){
        int totalRed = GlobalLevelData.getInstance().getRemainingBoxes().getInitialRed();
        int totalBlue = GlobalLevelData.getInstance().getRemainingBoxes().getInitialBlue();
        int totalYellow = GlobalLevelData.getInstance().getRemainingBoxes().getInitialYellow();

        // Recopilamos las puntuaciones máximas posibles de cada color
        // Si no hay nada especificado manualmente lo calculamos automaticamente
        int maxPointsFromRed, maxPointsFromBlue, maxPointsFromYellow;
        if(GlobalLevelData.getInstance().getMaxComboRed() < 0) {
            if (GlobalLevelData.getInstance().getRemainingBoxes().getInitialRed() > 2)
                maxPointsFromRed = getPointsFromBoxesChain(GlobalLevelData.getInstance().getRemainingBoxes().getInitialRed(), totalRed);
            else maxPointsFromRed = 0;
        } else maxPointsFromRed = getPointsFromBoxesChain(GlobalLevelData.getInstance().getMaxComboRed(), totalRed);

        if(GlobalLevelData.getInstance().getMaxComboBlue() < 0) {
            if (GlobalLevelData.getInstance().getRemainingBoxes().getInitialBlue() > 2)
                maxPointsFromBlue = getPointsFromBoxesChain(GlobalLevelData.getInstance().getRemainingBoxes().getInitialBlue(), totalBlue);
            else maxPointsFromBlue = 0;
        } else maxPointsFromBlue = getPointsFromBoxesChain(GlobalLevelData.getInstance().getMaxComboBlue(), totalBlue);

        if(GlobalLevelData.getInstance().getMaxComboYellow() < 0) {
            if (GlobalLevelData.getInstance().getRemainingBoxes().getInitialYellow() > 2)
                maxPointsFromYellow = getPointsFromBoxesChain(GlobalLevelData.getInstance().getRemainingBoxes().getInitialYellow(), totalYellow);
            else maxPointsFromYellow = 0;
        } else maxPointsFromYellow = getPointsFromBoxesChain(GlobalLevelData.getInstance().getMaxComboYellow(), totalYellow);

        int maxPointsFromBoxes = maxPointsFromRed + maxPointsFromBlue + maxPointsFromYellow;

        // Nuevo calculo de puntos minimos por cajas
        int boxesMargin = (totalRed + totalBlue + totalYellow) - GlobalLevelData.getInstance().getInitialRequestedBoxes();
        int pointsPerBoxesMargin = (GameConstants.BASE_BOX_POINTS * boxesMargin) + (boxesMargin * 15);
        int minPointsFromBoxes = maxPointsFromBoxes - pointsPerBoxesMargin;

        //Gdx.app.log("MIN/MAX from Red",Integer.toString(minPointsFromRed) + " / " + Integer.toString(maxPointsFromRed));
        //Gdx.app.log("MIN/MAX from Blue",Integer.toString(minPointsFromBlue) + " / " + Integer.toString(maxPointsFromBlue));
        //Gdx.app.log("MIN/MAX from Yellow",Integer.toString(minPointsFromYellow) + " / " + Integer.toString(maxPointsFromYellow));

        int optimalTime = GlobalLevelData.getInstance().getOptimalTime();
        int optimalTimePoints = getPointsFromTime(MathUtils.floor(optimalTime));
        int maxPointsFromOverBases = GlobalLevelData.getInstance().getInitialOverBasePoints() * GameConstants.BASE_OVERBASE_POINTS;

        int boxPointsMargin = maxPointsFromBoxes - minPointsFromBoxes;
        int baseFractions = (boxPointsMargin + optimalTimePoints + maxPointsFromOverBases) + GlobalLevelData.getInstance().getRankPointsMod();
        int twoStarsFraction = MathUtils.floor(0.45f * baseFractions);
        int threeStarsFraction = MathUtils.floor(0.75f * baseFractions);

        GlobalLevelData.getInstance().getLevelRankStats().pointsTwoStars = minPointsFromBoxes + twoStarsFraction;
        GlobalLevelData.getInstance().getLevelRankStats().pointsThreeStars = minPointsFromBoxes + threeStarsFraction;
        GlobalLevelData.getInstance().getLevelRankStats().optimalTime = optimalTime;
        GlobalLevelData.getInstance().getLevelRankStats().optimalTimePoints = optimalTimePoints;
        GlobalLevelData.getInstance().getLevelRankStats().boxPointsMargin = boxPointsMargin;
        GlobalLevelData.getInstance().getLevelRankStats().floorPointsMax = maxPointsFromOverBases;

        gameUI.createRankStats();
    }

    private int getRankAchieved(int achievedPoints){
        /*
        Gdx.app.log("","----------------- FINAL STATS ------------------");
        Gdx.app.log("Points",Integer.toString(GlobalLevelData.getInstance().getLevelScore()));
        //Gdx.app.log("OptimalTime",optimalTime + "  (" + optimalTimePoints + ") Points");
        Gdx.app.log("MaxOverBasePoints",Integer.toString(maxPointsFromOverBases));
        Gdx.app.log("MIN Boxex Points",Integer.toString(minPointsFromBoxes));
        Gdx.app.log("MAX Boxes Points",Integer.toString(maxPointsFromBoxes));
        Gdx.app.log("Box Points Margin",Integer.toString(boxPointsMargin));
        //Gdx.app.log("2 Stars",minPointsFromBoxes + twoStarsFraction  + "   Fraction(" +  twoStarsFraction+ ")");
        //Gdx.app.log("3 Stars",minPointsFromBoxes + threeStarsFraction  + "   Fraction(" +  threeStarsFraction+ ")");

        if(achievedPoints >= (minPointsFromBoxes + threeStarsFraction)) return 3;
        else if(achievedPoints >= (minPointsFromBoxes + twoStarsFraction)) return 2;
        else return 1;
        */
        if(achievedPoints >= GlobalLevelData.getInstance().getLevelRankStats().pointsThreeStars) return 3;
        else if(achievedPoints >= GlobalLevelData.getInstance().getLevelRankStats().pointsTwoStars) return 2;
        else return 1;
    }

    private boolean existGameEvent(MANAGER_EVENT event){
        for (GameEvent eventInList: gameEvents) {
            if( eventInList.getEvent() == event) {
                return true;
            }
        }
        return false;
    }

    private int getMinPointsFromColor(int groups, int isolated){
        int minPointsFromColor = 0;
        int i;
        for(i = 0; i < groups; i++){
            if(isolated > 0){
                minPointsFromColor += getPointsFromBoxesChain(3 + isolated);
                isolated = 0;
            }
            else minPointsFromColor += getPointsFromBoxesChain(3);
        }
        return minPointsFromColor;
    }

    private enum MANAGER_EVENT {
        LEVEL_ENTER, LEVEL_ENTER_AFTER_INTRODUCTION_EVENT, LEVEL_START, GAME_OVER, LEVEL_COMPLETED, RESTART_LEVEL, SELECTION_SCREEN_AFTER_AD,CHANGE_SCREEN,
        BOX_POINTS, OVERBASE_POINTS, OVERBASE_POWER,CHANGE_IN_BOXES, COLOR_DISABLING, ENTER_PAUSE_MENU, LEAVE_PAUSE_MENU, GAME_ZONE_FADE, FLOATING_LOST_BOX;
    }
}
