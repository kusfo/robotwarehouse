package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SpaceDockManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock.SpaceDockMonitor;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock.SpaceShip;

/**
 * Created by JordiRM on 19/07/2017.
 */
public class SpaceDockManagerImpl implements SpaceDockManager {
    private final int SHIP_ENTERING_Y = -80;
    private final int SHIP_LOADING_Y = 108;
    private SpaceShip ship;
    private SpaceDockMonitor dockMonitor;

    public SpaceDockManagerImpl(LevelModel levelModel){
        // Creamos los elementos del muelle espacial
        ship = new SpaceShip();
        dockMonitor = new SpaceDockMonitor();

        // Los añadimos a la lista de gameObjects para que se dibujen
        levelModel.getCurrentGameObjects().add(ship);
        levelModel.getCurrentGameObjects().add(dockMonitor);
    }

    public void update(float delta){
        updateSpaceShip(ship, delta);
        updateSpaceDockMonitor(dockMonitor, delta);
    }

    private void updateSpaceShip(SpaceShip spaceShip, float delta){

        if(spaceShip.getShipEvent() == SpaceShip.SHIP_EVENT.ENTERING){
            Vector2 newPosition = new Vector2(spaceShip.getPositionX(), SHIP_ENTERING_Y).interpolate(new Vector2(spaceShip.getPositionX(), SHIP_LOADING_Y),
                    spaceShip.getInterpolationTime(), Interpolation.pow2Out);
            spaceShip.setPosition(newPosition);
            spaceShip.setInterpolationTime(MathUtils.clamp(spaceShip.getInterpolationTime() + (0.5f * delta), 0.0f, 1.0f));

            if(spaceShip.getInterpolationTime() == 1){
                spaceShip.setPositionY(SHIP_LOADING_Y);
                spaceShip.setShipEvent(SpaceShip.SHIP_EVENT.LOADING);
                spaceShip.enginesTurnOff();
            }
        }
        else if(spaceShip.getShipEvent() == SpaceShip.SHIP_EVENT.LOADING){
            if(spaceShip.getEngineGlowAlpha() > 0){
                spaceShip.setEngineGlowAlpha(MathUtils.clamp( spaceShip.getEngineGlowAlpha() - (0.5f * delta), 0.f, 1.0f));
            }

            if(spaceShip.getEngineEvent() == SpaceShip.ENGINE_EVENT.DEACTIVATING){
                if (spaceShip.getAnimatedEngineFire().isAnimationFinished()){
                    spaceShip.setEngineEvent(SpaceShip.ENGINE_EVENT.INACTIVE);
                }
            }
        }
        else if(spaceShip.getShipEvent() == SpaceShip.SHIP_EVENT.LEAVING){
            if(spaceShip.getEngineGlowAlpha() < 1.0){
                spaceShip.setEngineGlowAlpha(MathUtils.clamp( spaceShip.getEngineGlowAlpha() + (0.5f * delta), 0.f, 1.0f));
            }
            if(spaceShip.getPauseCounter() <= 0) {
                if(spaceShip.getEngineEvent() == SpaceShip.ENGINE_EVENT.INACTIVE){
                    spaceShip.enginesTurnOn();
                }

                Vector2 newPosition = new Vector2(spaceShip.getPositionX(), SHIP_LOADING_Y).interpolate(new Vector2(spaceShip.getPositionX(), GameConstants.VERTICAL_RESOLUTION + 180), spaceShip.getInterpolationTime(), Interpolation.pow2In);
                spaceShip.setPosition(newPosition);
                spaceShip.setInterpolationTime(MathUtils.clamp(spaceShip.getInterpolationTime() + (0.2f * delta), 0.0f, 1.0f));

                if (spaceShip.getInterpolationTime() == 1) {
                    spaceShip.setShipEvent(SpaceShip.SHIP_EVENT.WAITING);
                }
            }else{
                spaceShip.substractPauseCounter(1 * delta);
            }
        }
    }

    private void updateSpaceDockMonitor(SpaceDockMonitor spaceDockMonitor, float delta){

        // Actualización del marcador
        if(!spaceDockMonitor.isScoreReady()){
            if(spaceDockMonitor.getPositionY() > 70) {
                Integer requestedBoxes = GlobalLevelData.getInstance().getRequestedBoxes();
                if (spaceDockMonitor.getRequestedBoxesShowed() < requestedBoxes) {
                    if (spaceDockMonitor.getCounter() >= 3) {
                        spaceDockMonitor.setRequestedBoxesShowed(spaceDockMonitor.getRequestedBoxesShowed() + 1);
                        spaceDockMonitor.setCounter(0);
                    } else {
                        spaceDockMonitor.addCounter(100 * delta);
                    }
                }else spaceDockMonitor.setScoreReady(true);
            }
        }else{
            Integer requestedBoxes = GlobalLevelData.getInstance().getRequestedBoxes();
            if(spaceDockMonitor.getRequestedBoxesShowed() > requestedBoxes){
                spaceDockMonitor.setRequestedBoxesShowed(requestedBoxes);
                // Al haber adquirido más cajas activamos el efecto de adquisicion
                if(spaceDockMonitor.getAdquiredBounceCounter() == 1) spaceDockMonitor.setAdquiredBounceCounter(-1);
            }
            // Escalado del numero cuando se adquiere una caja
            if(spaceDockMonitor.getAdquiredBounceCounter() < 1){
                if(spaceDockMonitor.getAdquiredBounceCounter() < 0){
                    spaceDockMonitor.setTextScale( MathUtils.clamp( spaceDockMonitor.getTextScale() + (7 * delta), 1, 1.3f));
                }else{
                    spaceDockMonitor.setTextScale( MathUtils.clamp( spaceDockMonitor.getTextScale() - (7 * delta), 1, 1.3f));
                }
                spaceDockMonitor.setAdquiredBounceCounter(MathUtils.clamp( spaceDockMonitor.getAdquiredBounceCounter() + (15 * delta), -1.0f, 1.0f));
            }
        }

        // Entrada y salida del monitor
        if (spaceDockMonitor.getMonitorEvent() == SpaceDockMonitor.DOCK_MONITOR_EVENT.ENTERING) {
            if(spaceDockMonitor.getPauseCounter() <= 0) {
                Interpolation interpolation = new Interpolation.Swing(0.5f);
                Vector2 newPosition = new Vector2(spaceDockMonitor.getPositionX(), -100).
                        interpolate(new Vector2(spaceDockMonitor.getPositionX(), 120), spaceDockMonitor.getInterpolationTime(), interpolation);
                spaceDockMonitor.setPosition(newPosition);
                spaceDockMonitor.setInterpolationTime(MathUtils.clamp(spaceDockMonitor.getInterpolationTime() + (0.8f * delta), 0.0f, 1.0f));

                if (spaceDockMonitor.getInterpolationTime() == 1) {
                    spaceDockMonitor.setPositionY(120);
                    spaceDockMonitor.setMonitorEvent(SpaceDockMonitor.DOCK_MONITOR_EVENT.SHOWING);
                }
            }else{
                spaceDockMonitor.substractPauseCounter(1 * delta);
            }
        }
        else if (spaceDockMonitor.getMonitorEvent() == SpaceDockMonitor.DOCK_MONITOR_EVENT.LEAVING) {
            if(spaceDockMonitor.getPauseCounter() <= 0) {

                Interpolation interpolation = new Interpolation.Swing(0.5f);
                Vector2 newPosition = new Vector2(spaceDockMonitor.getPositionX(), 120).
                        interpolate(new Vector2(spaceDockMonitor.getPositionX(), -100), spaceDockMonitor.getInterpolationTime(), interpolation);
                spaceDockMonitor.setPosition(newPosition);
                spaceDockMonitor.setInterpolationTime(MathUtils.clamp(spaceDockMonitor.getInterpolationTime() + (0.8f * delta), 0.0f, 1.0f));

                if (spaceDockMonitor.getInterpolationTime() == 1) {
                    spaceDockMonitor.setPositionY(-100);
                    spaceDockMonitor.setMonitorEvent(SpaceDockMonitor.DOCK_MONITOR_EVENT.WAITING);
                }
            }else{
                spaceDockMonitor.substractPauseCounter(1 * delta);
            }
        }
    }

    public void shipEnteringAction(){
        ship.assignContainers(true);
        ship.setShipEvent(SpaceShip.SHIP_EVENT.ENTERING);
        ship.setPosition(new Vector2(7, SHIP_ENTERING_Y));
        ship.setEngineEvent(SpaceShip.ENGINE_EVENT.ACTIVE);
        ship.setEngineGlowAlpha(1.0f);
        ship.enginesLoop();
    }
    public void shipStationedAction(){
        ship.assignContainers(false);
        ship.setShipEvent(SpaceShip.SHIP_EVENT.LOADING);
        ship.setPosition(new Vector2(7, SHIP_LOADING_Y));
    }
    public void shipLeavingAction(){
        ship.setPauseCounter(1.6f);
        ship.setShipEvent(SpaceShip.SHIP_EVENT.LEAVING);
        ship.setInterpolationTime(0);
    }

    public void activateDockMonitor(){
        dockMonitor.setPauseCounter(0.5f);
        dockMonitor.setMonitorEvent(SpaceDockMonitor.DOCK_MONITOR_EVENT.ENTERING);
    }
    public void deactivateDockMonitor(){
        dockMonitor.setPauseCounter(1.5f);
        dockMonitor.setInterpolationTime(0);
        dockMonitor.setMonitorEvent(SpaceDockMonitor.DOCK_MONITOR_EVENT.LEAVING);
        dockMonitor.setAdquiredBounceCounter(1);
        dockMonitor.setTextScale(1.0f);
    }
}