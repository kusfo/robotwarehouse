package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

public interface MainUiGameRetrievalListener {
    void gameRetrieved(boolean success);
    void gameLoadEnded(boolean success, boolean moreAdvancedSavedGame, long remoteGameTimeStamp, String deviceID);
    void gameApplied(boolean success);
    void gameSaved(boolean success);
    void gameDeleted(boolean success);
}
