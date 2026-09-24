package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

public interface MainConfigListener {
    void deleteGamePressed();
    boolean isSessionActive();
    boolean isSavedGameSupported();
    boolean isDebugBuild();
    boolean savedGameExists();
    void savedGameOptionChanged(boolean enabled);
    void crashTestPressed();
}
