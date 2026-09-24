package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.SavedGameDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUiGameRetrievalListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;

import java.io.IOException;

import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.gamestate.IFetchGameStatesListResponseListener;
import de.golfgl.gdxgamesvcs.gamestate.ILoadGameStateResponseListener;
import de.golfgl.gdxgamesvcs.gamestate.ISaveGameStateResponseListener;

public class SavedGamesManagerImpl implements SavedGamesManager {
    private IGameServiceClient playServices;
    private MainUiGameRetrievalListener savedGamesListener;
    private String gameStateId;
    private SavedGameDTO remoteSavedGameDTO;
    private String mainFloatingMessage = "";

    public SavedGamesManagerImpl(IGameServiceClient playServices) {
        this.playServices = playServices;
        gameStateId = "";
    }

    @Override
    public void setSavedGamesListener(MainUiGameRetrievalListener savedGamesListener) {
        this.savedGamesListener = savedGamesListener;
    }

    @Override
    public void retrieveSavedGame(final String gameId) {
        boolean fetchResult = playServices.fetchGameStates(new IFetchGameStatesListResponseListener() {
            @Override
            public void onFetchGameStatesListResponse(final Array<String> gameStates) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (gameStates == null) {
                            Gdx.app.log("SavedGamesManager", "Error fetching game states");
                            savedGamesListener.gameRetrieved(false);
                        } else {
                            if (gameStates.size == 0) {
                                Gdx.app.log("SavedGamesManager", "No Gamestates retrieved");
                                GlobalPreferencesData.getInstance().setCloudSaveSynced(true); // Como no hay partida se considera sincronizado para que pueda grabar
                                savedGamesListener.gameRetrieved(false);
                            } else if (gameStates.size > 0) {
                                Gdx.app.log("SavedGamesManager", gameStates.size + " Gamestates retrieved");
                                int i = 0;
                                boolean found = false;
                                do {
                                    if (gameStates.get(i).equals(gameId)) {
                                        gameStateId = gameId;
                                        savedGamesListener.gameRetrieved(true);
                                        found = true;
                                        Gdx.app.log("SavedGamesManager", "Cargado el gameState: " + gameId);
                                        break;
                                    }
                                    i++;
                                } while (i < gameStates.size);

                                if(!found){
                                    GlobalPreferencesData.getInstance().setCloudSaveSynced(true); // Como no hay partida se considera sincronizado para que pueda grabar
                                    savedGamesListener.gameRetrieved(false);
                                    Gdx.app.log("SavedGamesManager", "Ningun gameState coincide");
                                }
                            }
                        }
                    }
                });
            }
        });
        if(!fetchResult){
            savedGamesListener.gameRetrieved(false);
            Gdx.app.log("SavedGamesManager", "No hay servicios de juego");
        }
    }

    @Override
    public void loadGameState() {
        playServices.loadGameState(gameStateId, new ILoadGameStateResponseListener() {
            @Override
            public void gsGameStateLoaded(byte[] gameState) {
                Gdx.app.log("SavedGamesManager", "GameState retrieved");
                try {
                    int localSavedVersion = GlobalPreferencesData.getInstance().getSavedGameVersion();
                    boolean forcedMerge = false;
                    remoteSavedGameDTO = GlobalPreferencesData.getInstance().deserializePreferences(gameState);
                    Gdx.app.log("SavedGamesManager","[loadGameState] Remote Version: " + remoteSavedGameDTO.savedGameVersion +
                            "     Local Version: "+ GlobalPreferencesData.getInstance().getSavedGameVersion());
                    // Si la remota está desactualizada en relacion a la local, actualizamos el dto para poder hacer merge sin problemas y luego la borramos para empezar a grabar de nuevo
                    if (remoteSavedGameDTO.savedGameVersion < localSavedVersion && localSavedVersion == GameConstants.SAVED_GAME_VERSION){
                        checkSavedGameVersion(true);
                        // Forzamos el merge para conservar los avances que pudiera haber en la remota antes de borrarla
                        Gdx.app.log("SavedGamesManager","[loadGameState] Forcing Merge to save data");
                        mergeGameState();
                        deleteSavedGame(GameConstants.CLOUD_GAME_ID);
                        forcedMerge = true;
                        GlobalPreferencesData.getInstance().setCloudSaveSynced(true);
                    }
                    else if (remoteSavedGameDTO.savedGameVersion > localSavedVersion) {
                        checkSavedGameVersion(false);
                        localSavedVersion = GlobalPreferencesData.getInstance().getSavedGameVersion();

                        // Si además tambien es más alta que la version de la aplicacion, se avisa de que tu aplicacion puede estar desactualiada
                        if (remoteSavedGameDTO.savedGameVersion > GameConstants.SAVED_GAME_VERSION) {
                            mainFloatingMessage = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_cloudsave_newer_than_application");
                        }
                    }

                    if(!forcedMerge) {
                        if (remoteSavedGameDTO.savedGameVersion == localSavedVersion) { //version local y remota es la misma
                            GlobalPreferencesData.getInstance().setCloudSaveSynced(true);
                            Gdx.app.log("SavedGamesManager", "[loadGameState] La versión de la nube es la misma, sincronizando");
                            if (remoteSavedGameDTO.computeAdvance() > GlobalPreferencesData.getInstance().getSavedGameObject().computeAdvance()) {
                                //Esto indica una partida más nueva en el remoto, cargarla.
                                savedGamesListener.gameLoadEnded(true, true, remoteSavedGameDTO.savedGameTime, remoteSavedGameDTO.deviceID);
                                Gdx.app.log("SavedGamesManager", "[loadGameState] Avance de Nube más avanzada, cargando nube");
                            } else if (remoteSavedGameDTO.computeAdvance() < GlobalPreferencesData.getInstance().getSavedGameObject().computeAdvance()) {
                                //Esto indica una partida más vieja en el remoto, no hacer nada.
                                savedGamesListener.gameLoadEnded(false, false, remoteSavedGameDTO.savedGameTime, remoteSavedGameDTO.deviceID);
                                Gdx.app.log("SavedGamesManager", "[loadGameState] Avance de Nube más vieja, no hacemos nada");
                            } else { //Esto indica una partida igual en el remoto, cargarla just in case.
                                savedGamesListener.gameLoadEnded(true, false, remoteSavedGameDTO.savedGameTime, remoteSavedGameDTO.deviceID);
                                //después de cargar si es necesario la partida, actualizamos la versión si esta ha cambiado
                                Gdx.app.log("SavedGamesManager", "[loadGameState] Avance de Nube y remoto iguales, cargando nube por si acaso");
                            }
                        } else { //Si por lo que fuera, la versión remota sigue siendo diferente a la local, no cargamos la remota pero revisamos la version local
                            savedGamesListener.gameLoadEnded(false, false, remoteSavedGameDTO.savedGameTime, remoteSavedGameDTO.deviceID);
                            GlobalPreferencesData.getInstance().setCloudSaveSynced(false);
                            checkSavedGameVersion(false);
                            Gdx.app.log("SavedGamesManager", "[loadGameState] No se puede cargar por que la versión de partida local y la nube son diferentes");
                        }
                    }
                } catch(Exception ex) {
                    Gdx.app.log("SavedGamesManager","Error deserialising gamestate");
                    savedGamesListener.gameLoadEnded(false, false,  0, "");
                    GlobalPreferencesData.getInstance().setCloudSaveSynced(false);
                    checkSavedGameVersion(false);
                }
            }
        });
    }

    @Override
    public void replaceGameState() {
        try {
            Gdx.app.log("SavedGamesManager","Replacing gamestate");
            GlobalPreferencesData.getInstance().updatePreferences(remoteSavedGameDTO);
            Gdx.app.log("SavedGamesManager","Gamestate Replaced");
            savedGamesListener.gameApplied(true);
        } catch (Exception ex) {
            Gdx.app.log("SavedGamesManager","Error aplying gamestate");
            savedGamesListener.gameApplied(false);
        }
    }

    @Override
    public void mergeGameState() {
        try {
            Gdx.app.log("SavedGamesManager","Merging gamestate");
            GlobalPreferencesData.getInstance().mergePreferences(remoteSavedGameDTO);
            Gdx.app.log("SavedGamesManager","Gamestate merged");
            savedGamesListener.gameApplied(true);
        } catch (Exception ex) {
            Gdx.app.log("SavedGamesManager","Error aplying gamestate");
            savedGamesListener.gameApplied(false);
        }
    }

    @Override
    public void pushSavedGame(final String gameStateID) {
        byte[] gameState;
        try {
            GlobalPreferencesData.getInstance().setSavedGameTime();
            gameState = GlobalPreferencesData.getInstance().serializePreferences();
        } catch (IOException ex) {
            Gdx.app.log("SavedGamesManager", "Cannot serialize gameStateId");
            return;
        }
        playServices.saveGameState(gameStateID, gameState, GlobalPreferencesData.getInstance().getSavedGameObject().computeAdvance(), new ISaveGameStateResponseListener() {
            @Override
            public void onGameStateSaved(boolean success, String errorCode) {
                if(!success) {
                    Gdx.app.log("SavedGamesManager", "Error saving GameState " +  gameStateID + " , errorcode: " + errorCode);
                }
                savedGamesListener.gameSaved(success);
            }
        });
    }

    @Override
    public void deleteSavedGame(String gameID) {
        playServices.deleteGameState(gameID, new ISaveGameStateResponseListener() {
            @Override
            public void onGameStateSaved(boolean success, String errorCode) {
                if(success) {
                    gameStateId = "";
                    savedGamesListener.gameDeleted(success);
                } else {
                    Gdx.app.log("SavedGamesManager", "Error deleting game, errorcode: " + errorCode);
                }
            }
        });
    }

    @Override
    public boolean isSessionActive() {
        return playServices.isSessionActive();
    }

    @Override
    public boolean isSavedGameSupported() {
        return playServices.isFeatureSupported(IGameServiceClient.GameServiceFeature.GameStateStorage);
    }

    @Override
    public boolean isSavedRemoteGameEnabled() {
        return isSavedGameSupported()
                && GlobalPreferencesData.getInstance().isCloudSaveEnabled();
    }

    @Override
    public boolean savedGameExists() {
        return (gameStateId != null) && (!gameStateId.isEmpty());
    }

    @Override
    public void checkSavedGameVersion(boolean remoteDTO){
        // Comprobamos que la version de archivo de partida este actualizado a la última versión.
        int applicationVersion = GameConstants.SAVED_GAME_VERSION;
        int fileVersion;

        // Podemos actualizar el dto remoto de partida o la que está en las preferencias locales
        if(remoteDTO) {
            if(remoteSavedGameDTO == null) {
                Gdx.app.log("SavedGamesManager","[checkSavedGameVersion] remoteSavedGameDTO es NULL");
                return;
            }
            fileVersion = remoteSavedGameDTO.savedGameVersion;
        }
        else
            fileVersion = GlobalPreferencesData.getInstance().getSavedGameVersion();

        Gdx.app.log("SavedGamesManager","[checkSavedGameVersion] Application save version: " + applicationVersion);
        if(applicationVersion > fileVersion){
            if(fileVersion == 1){
                // [MIGRATING FROM VERSION 1 TO VERSION 2]
                // Reiniciamos el episodio 1 por que se añadieron nuevos niveles.
                if(remoteDTO){
                    Gdx.app.log("SavedGamesManager","[checkSavedGameVersion] Version REMOTA 1, la actualizariamos a 2");
                    remoteSavedGameDTO = GlobalPreferencesData.getInstance().eraseEpisodeSavedDataInDTO(1, remoteSavedGameDTO);
                    remoteSavedGameDTO.savedGameVersion = 2;
                }
                else
                {
                    Gdx.app.log("SavedGamesManager","[checkSavedGameVersion] Version LOCAL 1, la actualizariamos a 2");
                    GlobalPreferencesData.getInstance().eraseEpisodeSavedData(1);
                    GlobalPreferencesData.getInstance().setSavedGameVersion(2);
                }
                fileVersion = 2;
            }

            /* if(fileVersion ==2) {
                // [MIGRATING FROM VERSION 2 TO VERSION 3]
            } */
        }
    }
    @Override
    public String getMainFloatingMessage(){
        return mainFloatingMessage;
    }

    @Override
    public void deleteMainFloatingMessage(){
        mainFloatingMessage = "";
    }
}
