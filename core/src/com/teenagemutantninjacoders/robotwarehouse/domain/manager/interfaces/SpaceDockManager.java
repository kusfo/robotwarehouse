package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

/**
 * Created by JordiRM on 20/07/2017.
 */
public interface SpaceDockManager {
    void update(float delta);
    void activateDockMonitor();
    void deactivateDockMonitor();
    void shipEnteringAction();
    void shipStationedAction();
    void shipLeavingAction();
}
