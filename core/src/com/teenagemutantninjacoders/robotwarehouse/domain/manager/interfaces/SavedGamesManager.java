package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUiGameRetrievalListener;

public interface SavedGamesManager {
    void setSavedGamesListener(MainUiGameRetrievalListener savedGamesListener);
    void retrieveSavedGame(String gameID);
    void loadGameState();
    void replaceGameState();
    void mergeGameState();
    void pushSavedGame(String gameID);
    void deleteSavedGame(String gameID);
    boolean isSessionActive();
    boolean isSavedGameSupported();
    boolean isSavedRemoteGameEnabled();
    boolean savedGameExists();
    void checkSavedGameVersion(boolean remoteDTO);
    String getMainFloatingMessage();
    void deleteMainFloatingMessage();
}
