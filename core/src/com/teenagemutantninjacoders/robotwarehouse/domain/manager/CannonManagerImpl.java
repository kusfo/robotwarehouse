package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.CannonManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseTrackModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.CellModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by JordiRM on 03/04/2016.
 */
public class CannonManagerImpl implements CannonManager {
    private BoardModel currentBoardModel;
    private BoxListener boxListener;
    private float wagonImageTime = 6;
    private float turretImageTime = 6;
    public CannonManagerImpl(LevelModel currentLevelModel, BoxListener boxListener){
        this.boxListener = boxListener;
        currentBoardModel = currentLevelModel.getCurrentBoardModel();
    }

    public void updateCannon(CannonModel cannonModel, float delta){
        switch(cannonModel.getCannonStatus()) {
            case NONE:
                decideNextAction(cannonModel);
                break;

            case WAITING:
                if(cannonModel.isDisabled()) {
                    cannonModel.disableCannon();
                } else {
                    cannonModel.setCannonWaitingTime(cannonModel.getCannonWaitingTime() - (1 * delta));
                    if(cannonModel.getCannonWaitingTime() <= 0)
                        decideNextAction(cannonModel);
                }
                break;

            case DISABLED:
                if(cannonModel.isDisabled()){
                    cannonModel.setCannonEventTime(cannonModel.getCannonEventTime() - (1 * delta));
                } else {
                    if(cannonModel.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.PAUSED){
                        cannonModel.setMoveStatus(cannonModel.getLastMoveStatus());
                        cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.MOVING);
                    }
                    else
                        decideNextAction(cannonModel);

                    if(cannonModel.getDisableParticleEffect() != null) {
                        cannonModel.getDisableParticleEffect().finalizeParticle();
                        cannonModel.removeDisableParticleEffect();
                    }
                }
                break;
            case MOVING:
                processMovementStatus(cannonModel, delta);
                break;

            case ROTATING:
                cannonRotate(cannonModel, delta);
                break;

            case CHARGING:
                if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                    if (cannonModel.getCannonEventTime() > 0) {
                        if (cannonModel.isDisabled()) {
                            cannonModel.disableCannon();
                        } else {
                            cannonModel.setCannonEventTime(cannonModel.getCannonEventTime() - (1 * delta));
                        }
                    } else {
                        cannonModel.activateShoot();
                        AudioManager.getInstance().playSound(AudioManager.SOUND.CANNON_SHOOT);
                        laserImpact(cannonModel, cannonModel.getColumn(), cannonModel.getRow(), cannonModel.getShootDirection());
                    }
                }
                break;

            case SHOOTING:
                if (cannonModel.getCannonEventTime() > 0) {
                    cannonModel.setCannonEventTime(cannonModel.getCannonEventTime() - (1 * delta));
                } else {
                    // Despues de disparar actualizamos para el siguiente posible movimiento
                    cannonModel.setAdvancesLeft(cannonModel.getMaxAutoAdvances());
                    cannonModel.setNextProgramedStop();

                    if(cannonModel.getPauseAfterShooting() > 0){
                        cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.WAITING);
                        cannonModel.setCannonWaitingTime(cannonModel.getPauseAfterShooting());
                    } else
                        decideNextAction(cannonModel);
                }
                break;
        }

        updateTurretRotation(cannonModel, delta);
        // Posible laser
        updateLaserCannon(cannonModel, delta);
    }

    private void decideNextAction(CannonModel cannonModel) {
        if(cannonModel.isDisabled()) {
            cannonModel.disableCannon();
        } else {
            if(cannonModel.getCannonWaitingTime() > 0)
                cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.WAITING);
            else if(isReadyToShoot(cannonModel))
                activateCharge(cannonModel);
            else {
                cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.MOVING);
                assignNewDirection(cannonModel);
                CellDTO newDest = getCannonDestination(cannonModel, new CellDTO(cannonModel.getColumn(), cannonModel.getRow()), cannonModel.getNewDir());
                if(!cannonModel.isTerminated()) cannonModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.MOVING, false);
                cannonModel.setNewDestination(cannonModel.getNewDir(), newDest);
            }
        }
    }

    private boolean isReadyToShoot(CannonModel cannonModel){
        return cannonModel.getColumn() == cannonModel.getShootingCell().getColumn() && cannonModel.getRow() == cannonModel.getShootingCell().getRow();
    }

    private void processMovementStatus(CannonModel cannonModel, float delta){

        // Si el movimiento está en IDLE es por que ha completado su trayecto asignado, ya sea en una recta o un giro
        if (cannonModel.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.IDLE) {
            decideNextAction(cannonModel);
            return;
        }

        OverBaseTrackModel trackActual = (OverBaseTrackModel) currentBoardModel.getOverBase(cannonModel.getColumn(), cannonModel.getRow());
        OverBaseTrackModel.TRACK_SHAPE trackShape = trackActual.getTrackShape();
        boolean needRotation = false;

        // Comprobamos constantemente si entramos en una curva para empezar el evento de giro.
        switch (cannonModel.getNewDir()) {
            case LEFT:
                if (trackShape == OverBaseTrackModel.TRACK_SHAPE.RIGHT_DOWN || trackShape == OverBaseTrackModel.TRACK_SHAPE.RIGHT_UP) {
                    if(cannonModel.getPositionX() < trackActual.getPositionX() + 12) needRotation = true;
                }
                break;
            case RIGHT:
                if (trackShape == OverBaseTrackModel.TRACK_SHAPE.LEFT_DOWN || trackShape == OverBaseTrackModel.TRACK_SHAPE.LEFT_UP) {
                    if(cannonModel.getPositionX() > trackActual.getPositionX() - 12) needRotation = true;
                }
                break;
            case UP:
                if (trackShape == OverBaseTrackModel.TRACK_SHAPE.RIGHT_DOWN || trackShape == OverBaseTrackModel.TRACK_SHAPE.LEFT_DOWN){
                    if(cannonModel.getPositionY() > trackActual.getPositionY() - 11) needRotation = true;//-5
                }
                break;
            case DOWN:
                if (trackShape == OverBaseTrackModel.TRACK_SHAPE.RIGHT_UP|| trackShape == OverBaseTrackModel.TRACK_SHAPE.LEFT_UP)
                    if(cannonModel.getPositionY() < trackActual.getPositionY() + 11) needRotation = true; //+ 17
                break;
        }

        // Interrumpimos el movimiento para rotar
        if(needRotation) {
            // Vamos a cambiar de direccion, asignamos la nueva para usarla luego
            assignNewDirection(cannonModel);

            // Asignamos las variables visuales de giro para la torreta, para que gire independientemente del valor real
            cannonModel.setActualTurretVisualShootDirection(cannonModel.getShootDirection());
            cannonModel.setNextTurretVisualShootDirection(cannonModel.getNextShootDirection());

            // Reposicionamos el cañon en la casilla de curva
            cannonModel.setPositionX(cannonModel.getCellPositionX(cannonModel.getColumn()));
            cannonModel.setPositionY(cannonModel.getCellPositionY(cannonModel.getRow()));

            // Paramos el movimiento y activamos el evento de rotacion
            cannonModel.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.IDLE);
            cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.ROTATING);

            // Cambiamos al fotograma de giro inmediatamente para que el cañon se adapte a la via
            cannonModel.setWagonImageTime(wagonImageTime);
            cannonRotate(cannonModel, delta);
            return ;//break; // Nos saltamos el resto del codigo de movimiento.
        }

        //Interrumpimos el movimiento para desactivar el cañon
        if (cannonModel.isDisabled()) {
            cannonModel.disableCannon();
            cannonModel.setLastMoveStatus(cannonModel.getMoveStatus());
            cannonModel.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.PAUSED);
            return;
        }
    }

    private void assignNewDirection(CannonModel cannonModel){
        MovableBoardObjectModel.DIRECTION newDir = cannonModel.getNewDir();
        OverBaseTrackModel trackActual = (OverBaseTrackModel) currentBoardModel.getOverBase(cannonModel.getColumn(), cannonModel.getRow());

        switch (trackActual.getTrackShape()) {
            case LEFT_RIGHT: //RAIL_H
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                    newDir = MovableBoardObjectModel.DIRECTION.RIGHT;
                }else{
                    newDir = MovableBoardObjectModel.DIRECTION.LEFT;
                }
                break;
            case UP_DOWN: //RAIL_V
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.UP) {
                    newDir = MovableBoardObjectModel.DIRECTION.UP;
                }else{
                    newDir = MovableBoardObjectModel.DIRECTION.DOWN;
                }
                break;
            case LEFT_UP: //RAIL_UL:
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                    newDir = MovableBoardObjectModel.DIRECTION.UP;
                } else if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.DOWN) {
                    newDir = MovableBoardObjectModel.DIRECTION.LEFT;
                }
                break;
            case LEFT_DOWN://RAIL_DL:
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.UP) {
                    newDir = MovableBoardObjectModel.DIRECTION.LEFT;
                } else if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                    newDir = MovableBoardObjectModel.DIRECTION.DOWN;
                }
                break;
            case RIGHT_DOWN://RAIL_DR:
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.UP) {
                    newDir = MovableBoardObjectModel.DIRECTION.RIGHT;
                } else if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                    newDir = MovableBoardObjectModel.DIRECTION.DOWN;
                }
                break;
            case RIGHT_UP://RAIL_UR:
                if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.DOWN) {
                    newDir = MovableBoardObjectModel.DIRECTION.RIGHT;
                } else if (cannonModel.getNewDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                    newDir = MovableBoardObjectModel.DIRECTION.UP;
                }
                break;

            case RIGHT: //RAIL_ENDR:
                newDir = MovableBoardObjectModel.DIRECTION.LEFT;
                break;
            case LEFT: //RAIL_ENDL:
                newDir = MovableBoardObjectModel.DIRECTION.RIGHT;
                break;
            case UP: //RAIL_ENDU:
                newDir = MovableBoardObjectModel.DIRECTION.DOWN;
                break;
            case DOWN: //RAIL_ENDD:
                newDir = MovableBoardObjectModel.DIRECTION.UP;
                break;
        }

        cannonModel.setPreviousDir(cannonModel.getNewDir());
        cannonModel.setNewDir(newDir);
        cannonModel.setNextShootDirection(getNextShootDirection(cannonModel, newDir));
    }


    private CannonModel.CANNON_SHOOT_DIRECTION getNextShootDirection(CannonModel cannonModel, MovableBoardObjectModel.DIRECTION nextDirection){
        // Buscamos y guardamos la proxima direccion en la que apuntara el cañon una vez cambie de dirección.
        CannonModel.CANNON_SHOOT_DIRECTION actualShootDirection = cannonModel.getShootDirection();
        switch(actualShootDirection){
            case UP:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.UP){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.RIGHT;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.LEFT;
                }
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.DOWN){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.LEFT;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.RIGHT;
                }
                break;
            case DOWN:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.UP){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.LEFT;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.RIGHT;
                }
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.DOWN){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.RIGHT;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT)
                        return CannonModel.CANNON_SHOOT_DIRECTION.LEFT;
                }
                break;
            case LEFT:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.LEFT){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP)
                        return CannonModel.CANNON_SHOOT_DIRECTION.DOWN;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN)
                        return CannonModel.CANNON_SHOOT_DIRECTION.UP;
                }
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.RIGHT){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP)
                        return CannonModel.CANNON_SHOOT_DIRECTION.UP;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN)
                        return CannonModel.CANNON_SHOOT_DIRECTION.DOWN;
                }
                break;
            case RIGHT:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.LEFT){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP)
                        return CannonModel.CANNON_SHOOT_DIRECTION.UP;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN)
                        return CannonModel.CANNON_SHOOT_DIRECTION.DOWN;
                }
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.RIGHT){
                    if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP)
                        return CannonModel.CANNON_SHOOT_DIRECTION.DOWN;
                    else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN)
                        return CannonModel.CANNON_SHOOT_DIRECTION.UP;
                }
                break;
        }
        return actualShootDirection;
    }

    private void cannonRotate(CannonModel cannonModel, float delta){
        if (cannonModel.getWagonImageTime() == 0 || cannonModel.getWagonRotationDir() == CannonModel.CLOCK_DIR.NONE){
            // Si aun no hemos empezado a rotar, decidimos la animacion de rotacion
            if(cannonModel.getWagonRotationDir() == CannonModel.CLOCK_DIR.NONE) {
                switch (cannonModel.getShootDirection()) {
                    case UP:
                        if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.RIGHT) {

                            if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.SIX_NINE);
                                cannonModel.setSpriteOffsetX(-1);
                            } else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.TWELVE_THREE);
                                cannonModel.setSpriteOffsetX(-6); // (V)
                            }
                        } else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.LEFT) {

                            if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.TWELVE_NINE);
                                cannonModel.setSpriteOffsetX(0); // (V)
                            } else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.SIX_THREE);
                                cannonModel.setSpriteOffsetX(-6);
                            }
                        }
                        break;
                    case DOWN:
                        if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.RIGHT) {

                                if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                                    cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.TWELVE_NINE);
                                    cannonModel.setSpriteOffsetX(0); // (V)
                                }
                                else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                                    cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.SIX_THREE);
                                    cannonModel.setSpriteOffsetX(-6); // (V)
                                }
                        }
                        else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.LEFT) {

                                if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.LEFT) {
                                    cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.SIX_NINE);
                                    cannonModel.setSpriteOffsetX(-1); // (V)
                                }
                                else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.RIGHT) {
                                    cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.TWELVE_THREE);
                                    cannonModel.setSpriteOffsetX(-6); // (V)
                                }
                        }
                        break;
                    case RIGHT:
                        if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP){
                            if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.UP) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.THREE_TWELVE);
                                cannonModel.setSpriteOffsetX(-6);// (V)
                            } else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.DOWN) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.NINE_TWELVE);
                                cannonModel.setSpriteOffsetX(0); // (V)
                            }

                        }else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN) {

                            if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.UP) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.NINE_SIX);
                                cannonModel.setSpriteOffsetX(-1); // (V)
                            } else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.DOWN) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.THREE_SIX);
                                cannonModel.setSpriteOffsetX(-6); // (V)
                            }
                        }
                        break;

                    case LEFT:
                        if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.UP){

                            if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.UP) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.NINE_TWELVE);
                                cannonModel.setSpriteOffsetX(0);
                            } else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.DOWN) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.THREE_TWELVE);
                                cannonModel.setSpriteOffsetX(-6); // (V)
                            }
                        }
                        else if(cannonModel.getPreviousDir() == MovableBoardObjectModel.DIRECTION.DOWN){

                            if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.UP) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.THREE_SIX);
                                cannonModel.setSpriteOffsetX(-6);
                            } else if (cannonModel.getNextShootDirection() == CannonModel.CANNON_SHOOT_DIRECTION.DOWN) {
                                cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.NINE_SIX);
                                cannonModel.setSpriteOffsetX(-1); // (V)
                            }
                        }
                        break;
                }
                cannonModel.setWagonFrameNumber(1);
                setWagonSpriteByClockDir(cannonModel);
                cannonModel.setWagonImageTime(wagonImageTime);

            } else {
                if(cannonModel.getWagonFrameNumber() < 3) {
                    // animamos la rotacion
                    cannonModel.setWagonFrameNumber(cannonModel.getWagonFrameNumber() + 1);
                    setWagonSpriteByClockDir(cannonModel);
                    cannonModel.setWagonImageTime(wagonImageTime);
                } else {
                    // Acaba la rotación, lo ponemos recto, reposicionamos y retomamos el movimiento
                    cannonModel.setWagonRotationDir(CannonModel.CLOCK_DIR.NONE);
                    // Hemos acabado el giro, asignamos el valor real de direccion de disparo
                    cannonModel.setShootDirection(cannonModel.getNextShootDirection());
                    // Activamos el movimiento de nuevo
                    cannonModel.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.IDLE);
                    cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.MOVING);

                    cannonModel.setSpriteOffsetX(-3);

                    // Reposicionamiento al salir del giro para que quede más coherente
                    switch (cannonModel.getNewDir()) {
                        case LEFT:
                            cannonModel.setPositionX(cannonModel.getPositionX() - 12);
                            cannonModel.setWagonSprite("horizontal", -1);
                            break;
                        case RIGHT:
                            cannonModel.setPositionX(cannonModel.getPositionX() + 12);
                            cannonModel.setWagonSprite("horizontal", -1);
                            break;
                        case UP:
                            cannonModel.setPositionY(cannonModel.getPositionY() + 11);// + 17
                            cannonModel.setWagonSprite("vertical", -1);
                            break;
                        case DOWN:
                            cannonModel.setPositionY(cannonModel.getPositionY() - 11);//- 5
                            cannonModel.setWagonSprite("vertical", -1);
                            break;
                    }
                }
            }
        }
        else {
            cannonModel.setWagonImageTime(MathUtils.clamp(cannonModel.getWagonImageTime() - (80 * delta), 0, 999));
        }
    }

    private void updateTurretRotation(CannonModel cannonModel, float delta){

        if(cannonModel.getTurretImageTime() == 0) {
            CannonModel.CANNON_SHOOT_DIRECTION shootDirection = cannonModel.getActualTurretVisualShootDirection();
            CannonModel.CANNON_SHOOT_DIRECTION nextShootDirection = cannonModel.getNextTurretVisualShootDirection();
            int imageIndex = cannonModel.getTurretImageIndex();
            if (shootDirection != nextShootDirection) {
                switch (shootDirection) {
                    case UP:
                        if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.LEFT)
                            imageIndex += 1;
                        else if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.RIGHT)
                            imageIndex -= 1;
                        break;
                    case DOWN:
                        if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.LEFT)
                            imageIndex -= 1;
                        else if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.RIGHT)
                            imageIndex += 1;
                        break;
                    case LEFT:
                        if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.UP)
                            imageIndex -= 1;
                        else if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.DOWN)
                            imageIndex += 1;
                        break;
                    case RIGHT:
                        if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.UP)
                            imageIndex += 1;
                        else if (nextShootDirection == CannonModel.CANNON_SHOOT_DIRECTION.DOWN)
                            imageIndex -= 1;
                        break;
                }
                if (imageIndex == 0) imageIndex = 16;
                else if (imageIndex == 17) imageIndex = 1;

                cannonModel.setTurretSpriteByIndex(imageIndex);
                // Si hemos llegado a un sprite recto es que hemos terminado de girar
                if (imageIndex == 1 || imageIndex == 5 || imageIndex == 9 || imageIndex == 13) {
                    cannonModel.setActualTurretVisualShootDirection(cannonModel.getNextTurretVisualShootDirection());
                }else{
                    cannonModel.setTurretImageTime(turretImageTime);
                }
            }
        } else {
            cannonModel.setTurretImageTime(MathUtils.clamp(cannonModel.getTurretImageTime() - (80 * delta), 0, 999));
        }
    }

    private void setWagonSpriteByClockDir(CannonModel cannonModel){
        switch (cannonModel.getWagonRotationDir()){
            case TWELVE_THREE:
                cannonModel.setWagonSprite("turn_right_down", cannonModel.getWagonFrameNumber());
                break;
            case THREE_TWELVE:
                cannonModel.setWagonSprite("turn_right_down", 4 - cannonModel.getWagonFrameNumber());
                break;

            case SIX_THREE:
                cannonModel.setWagonSprite("turn_right_up", cannonModel.getWagonFrameNumber());
                break;
            case THREE_SIX:
                cannonModel.setWagonSprite("turn_right_up", 4 - cannonModel.getWagonFrameNumber());
                break;

            case SIX_NINE:
                cannonModel.setWagonSprite("turn_left_up", cannonModel.getWagonFrameNumber());
                break;
            case NINE_SIX:
                cannonModel.setWagonSprite("turn_left_up", 4 - cannonModel.getWagonFrameNumber());
                break;

            case TWELVE_NINE:
                cannonModel.setWagonSprite("turn_left_down", cannonModel.getWagonFrameNumber());
                break;
            case NINE_TWELVE:
                cannonModel.setWagonSprite("turn_left_down", 4 - cannonModel.getWagonFrameNumber());
                break;
        }
    }

    private CellDTO getCannonDestination(CannonModel cannonModel, CellDTO currentCellDTO, MovableBoardObjectModel.DIRECTION directionAsked){
        CellDTO destinationCellDTO = new CellDTO(currentCellDTO.getColumn(), currentCellDTO.getRow());
        CellModel[][] cellModels = currentBoardModel.getBoardCells();
        CellDTO actualProgramedStop = cannonModel.getActualProgramedStop();
        int incrementor;
        int advancesLeft = cannonModel.getAdvancesLeft();

        // Si el cañon no va a moverse, lo dejamos donde está
        if(actualProgramedStop != null){
            if(actualProgramedStop.getColumn() == destinationCellDTO.getColumn() && actualProgramedStop.getRow() == destinationCellDTO.getRow()) {
                if (cannonModel.getProgramedStopJumps() <= 0) {
                    cannonModel.setShootingCell(new CellDTO(destinationCellDTO.getColumn(), destinationCellDTO.getRow()));
                    return destinationCellDTO;
                } else
                    cannonModel.decreaseProgramedStopJumps();
            }
        } else if (advancesLeft <= 0 && ((OverBaseTrackModel) cellModels[destinationCellDTO.getColumn()][destinationCellDTO.getRow()].getBoardObjectOverBase()).isCannonStop()) {
            cannonModel.setShootingCell(new CellDTO(destinationCellDTO.getColumn(), destinationCellDTO.getRow()));
            return destinationCellDTO;
        }

        // Si el cañon tiene que moverse, calculamos la siguiente parada, para girar o disparar
        switch (directionAsked) {
            case LEFT: case RIGHT:
                if(directionAsked == MovableBoardObjectModel.DIRECTION.LEFT) incrementor = -1; else incrementor = 1;
                while(destinationCellDTO.getColumn() + incrementor >= 0 && destinationCellDTO.getColumn() + incrementor <= GameConstants.BOARD_COLUMNS -1) {
                    CellModel nextCell = cellModels[destinationCellDTO.getColumn() + incrementor][destinationCellDTO.getRow()];
                    destinationCellDTO.setColumn(destinationCellDTO.getColumn() + incrementor);
                    advancesLeft -= 1;
                    // Si hay paradas en la lista, solo usamos esas
                    if(actualProgramedStop != null){
                        if (actualProgramedStop.getColumn() == destinationCellDTO.getColumn() && actualProgramedStop.getRow() == destinationCellDTO.getRow()) {
                            if (cannonModel.getProgramedStopJumps() <= 0) {
                                cannonModel.setShootingCell(new CellDTO(destinationCellDTO.getColumn(), destinationCellDTO.getRow()));
                                break;
                            } else
                                cannonModel.decreaseProgramedStopJumps();
                        }
                    } else {
                        if (advancesLeft <= 0 && ((OverBaseTrackModel) nextCell.getBoardObjectOverBase()).isCannonStop()) {
                            cannonModel.setShootingCell(new CellDTO(nextCell.getBoardObjectOverBase().getColumn(), nextCell.getBoardObjectOverBase().getRow()));
                            break;
                        }
                    }
                    // Si llegamos a una esquina o final, paramos para recalcular la nueva dirección y seguir avanzando
                    if (((OverBaseTrackModel) nextCell.getBoardObjectOverBase()).getTrackShape() != OverBaseTrackModel.TRACK_SHAPE.LEFT_RIGHT)
                        break;
                }
                break;
            case UP: case DOWN:
                if(directionAsked == MovableBoardObjectModel.DIRECTION.UP) incrementor = -1; else incrementor = 1;
                while(destinationCellDTO.getRow() + incrementor >= 0 && destinationCellDTO.getRow() + incrementor <= GameConstants.BOARD_ROWS - 1) {
                    CellModel nextCell = cellModels[destinationCellDTO.getColumn()][destinationCellDTO.getRow() + incrementor];
                    destinationCellDTO.setRow(destinationCellDTO.getRow() + incrementor);
                    advancesLeft -=1;
                    // Si hay paradas en la lista, solo usamos esas
                    if(actualProgramedStop != null){
                        if(actualProgramedStop.getColumn() == destinationCellDTO.getColumn() && actualProgramedStop.getRow() == destinationCellDTO.getRow()) {
                            if (cannonModel.getProgramedStopJumps() <= 0) {
                                cannonModel.setShootingCell(new CellDTO(destinationCellDTO.getColumn(), destinationCellDTO.getRow()));
                                break;
                            } else
                                cannonModel.decreaseProgramedStopJumps();
                        }
                    } else {
                        if (advancesLeft <= 0 && ((OverBaseTrackModel) nextCell.getBoardObjectOverBase()).isCannonStop()) {
                            cannonModel.setShootingCell(new CellDTO(nextCell.getBoardObjectOverBase().getColumn(), nextCell.getBoardObjectOverBase().getRow()));
                            break;
                        }
                    }
                    // Si llegamos a una esquina o final, paramos para recalcular la nueva dirección y seguir avanzando
                    if (((OverBaseTrackModel) nextCell.getBoardObjectOverBase()).getTrackShape() != OverBaseTrackModel.TRACK_SHAPE.UP_DOWN)
                        break;
                }
                break;
        }
        cannonModel.setAdvancesLeft(MathUtils.clamp(advancesLeft, 0, 99));
        return destinationCellDTO;
    }

    private void activateCharge(CannonModel cannonModel){
        cannonModel.setShootingCell(new CellDTO(-1, -1));
        cannonModel.activateCharge();
        AudioManager.getInstance().playSound(AudioManager.SOUND.CANNON_CHARGE);
        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.CANNON_CHARGE, cannonModel
                .getPositionX() + 16, cannonModel.getPositionY() + 34, cannonModel.getDepth() - 1200);
    }

    private void laserImpact(CannonModel cannonModel, int cellH, int cellV, CannonModel.CANNON_SHOOT_DIRECTION shootDirection){
        BoardObjectModel boardObjectModel;
        Rectangle impactZone = new Rectangle();
        float distance = 0;
        boolean lostInSpace = false;
        do {
            switch (shootDirection) {
                case UP:    cellV--; break;
                case DOWN:  cellV++; break;
                case LEFT:  cellH--; break;
                case RIGHT: cellH++; break;
            }
            if(cellH < GameConstants.BOARD_COLUMNS && cellH >=0 && cellV < GameConstants.BOARD_ROWS && cellV >=0) {
                // Base
                boardObjectModel = currentBoardModel.getBoardObjectBase(cellH, cellV);
                if(boardObjectModel != null && boardObjectModel.getObstacleLevel() >= GameConstants.WALL_OBSTACLE_LEVEL){
                    impactZone = boardObjectModel.getHitboxImpact();
                    break;
                }
                // Above
                boardObjectModel = currentBoardModel.getBoardObjectAbove(cellH, cellV);
                if (boardObjectModel instanceof BoxModel) {
                    impactZone = boardObjectModel.getHitboxImpact();
                    if (!boardObjectModel.isTerminated()) {
                        boxListener.setBoxBoardObjectEvent((BoxModel)boardObjectModel, BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                        DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.EXPLOSION));
                        ((iDestroyableBoardObject)boardObjectModel).setDestruction(destructionDTO);
                    }
                    break;
                }
            } else {
                // Si sale por la izquierda, impacta contra la vaina, si no, se pierde en el espacio.
                switch (shootDirection) {
                    case UP:
                        impactZone = new Rectangle(0, GameConstants.VERTICAL_RESOLUTION + 20, GameConstants.HORIZONTAL_RESOLUTION, 1);
                        lostInSpace = true;
                        break;
                    case DOWN:
                        impactZone = new Rectangle(0, -20, GameConstants.HORIZONTAL_RESOLUTION, 1);
                        lostInSpace = true;
                        break;
                    case LEFT:
                        impactZone = new Rectangle(145, 0, 1, GameConstants.VERTICAL_RESOLUTION);
                        break;
                    case RIGHT:
                        impactZone = new Rectangle(GameConstants.HORIZONTAL_RESOLUTION + 20, 0, 1 ,GameConstants.VERTICAL_RESOLUTION);
                        lostInSpace = true;
                        break;
                }
                distance += 32;
                break;
            }

            distance += 32;
        } while (true);

        // LASER
        if(distance > 0){
            float cannonMouthOffsetX, cannonMouthOffsetY;
            float laserX1 = 0;
            float laserY1 = 0;
            float laserX2 = 0;
            float laserY2 = 0;
            switch (shootDirection) {
                case UP:
                    cannonMouthOffsetY = 22;
                    laserX1 = cannonModel.getPositionX() + 16;
                    laserY1 = cannonModel.getPositionY() + GameConstants.CELL_HEIGHT  + cannonMouthOffsetY;
                    laserX2 = laserX1;
                    laserY2 = impactZone.y;
                    EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_DOWN, laserX1, laserY1, cannonModel.getDepth() - 1000);
                    if(!lostInSpace)
                        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_UP, laserX2, laserY2, cannonModel.getDepth() - 1000);
                    break;
                case DOWN:
                    cannonMouthOffsetY = 4;
                    laserX1 = cannonModel.getPositionX() + 16;
                    laserY1 = impactZone.y + impactZone.height;
                    laserX2 = laserX1;
                    laserY2 = cannonModel.getPositionY() + cannonMouthOffsetY + GameConstants.CELL_SIDE;
                    EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_UP, laserX2, laserY2, cannonModel.getDepth() - 1000);
                    if(!lostInSpace)
                        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_DOWN, laserX1, laserY1, cannonModel.getDepth() - 1000);
                    break;
                case RIGHT:
                    cannonMouthOffsetX = 3;
                    laserX1 = cannonModel.getPositionX() + GameConstants.CELL_WIDTH + cannonMouthOffsetX;
                    laserY1 = cannonModel.getPositionY() + 33;
                    laserX2 = impactZone.x;
                    laserY2 = laserY1;
                    EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_LEFT, laserX1, laserY1, cannonModel.getDepth() - 1000);
                    if(!lostInSpace)
                        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_RIGHT, laserX2, laserY2, cannonModel.getDepth() - 1000);
                    break;
                case LEFT:
                    cannonMouthOffsetX = 3;
                    laserX1 = impactZone.x + impactZone.width;
                    laserY1 = cannonModel.getPositionY() + 33;
                    laserX2 = cannonModel.getPositionX() - cannonMouthOffsetX;
                    laserY2 = laserY1;
                    EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_RIGHT, laserX2, laserY2, cannonModel.getDepth() - 1000);
                    if(!lostInSpace)
                        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.LASER_SPARKS_LEFT, laserX1, laserY1, cannonModel.getDepth() - 1000);
                    break;
            }
            cannonModel.activeLaser(laserX1, laserY1, laserX2, laserY2);
        }
    }

    private void updateLaserCannon(CannonModel cannonModel, float delta){

        if(cannonModel.isShooting()){
            float laserScaleX = cannonModel.getLaserSprite().getScaleX();
            float laserScaleY = cannonModel.getLaserSprite().getScaleY();
            if(cannonModel.isImpacting()){
                switch(cannonModel.getShootDirection()){
                    case UP:case DOWN:
                        if(laserScaleX + (2f * delta) < 1) cannonModel.getLaserSprite().setScale( laserScaleX + (2f  * delta), laserScaleY);
                        else{
                            cannonModel.getLaserSprite().setScale(1, 1);
                            cannonModel.setImpacting(false);
                        }
                        break;
                    case LEFT:case RIGHT:
                        if(laserScaleY + (2f  * delta) < 1) cannonModel.getLaserSprite().setScale( 1, (laserScaleY + (2f  * delta)));
                        else{
                            cannonModel.getLaserSprite().setScale(1, 1);
                            cannonModel.setImpacting(false);
                        }
                        break;
                }
            }else{
                switch(cannonModel.getShootDirection()){
                    case UP:case DOWN:
                        if(laserScaleX - (3 * delta) > 0) cannonModel.getLaserSprite().setScale( laserScaleX - (3 * delta), laserScaleY);
                        else{
                            cannonModel.getLaserSprite().setScale(0, 1);
                            cannonModel.setIsShooting(false);
                        }
                        break;
                    case LEFT:case RIGHT:
                        if(laserScaleY + (3 * delta) > 0) cannonModel.getLaserSprite().setScale( 1, (laserScaleY - (3 * delta)));
                        else{
                            cannonModel.getLaserSprite().setScale(1, 0);
                            cannonModel.setIsShooting(false);
                        }
                        break;
                }
            }
        }
    }
}
