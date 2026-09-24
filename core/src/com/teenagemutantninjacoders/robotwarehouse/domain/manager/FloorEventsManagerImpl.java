package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.FloorEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorBarrierModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorButtonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorRotatingModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorTrapDoorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 09/03/2016.
 */
public class FloorEventsManagerImpl implements FloorEventsManager {
    private BoxListener boxListener;
    private ArrayList<FloorBarrierModel> floorBarrierModels;
    public FloorEventsManagerImpl(BoxListener boxListener, ArrayList<FloorBarrierModel> floorBarrierModels){
        this.boxListener = boxListener;
        this.floorBarrierModels = floorBarrierModels;
    }

    @Override
    public void updateBoardObject(BoardObjectModel boardObject, BoardModel boardModel, float delta) {
        BoardObjectModel boardObjectAbove = boardModel.getBoardObjectAbove(boardObject.getColumn(),  boardObject.getRow());

        // TRAP DOORS
        if (boardObject instanceof FloorTrapDoorModel) {
            FloorTrapDoorModel floorTrapDoorModel = (FloorTrapDoorModel) boardObject;
            if (floorTrapDoorModel.getFloorEventTime() >= floorTrapDoorModel.getEventTimeEnd()) {
                if (floorTrapDoorModel.getFloorStatus() == FloorTrapDoorModel.FLOOR_STATUS.CLOSE) {
                    floorTrapDoorModel.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
                    floorTrapDoorModel.setAnimationTime(0);
                    floorTrapDoorModel.setFloorStatus(FloorTrapDoorModel.FLOOR_STATUS.OPENING);
                    floorTrapDoorModel.setFloorEventTime(0);
                    AudioManager.getInstance().playSound(AudioManager.SOUND.TRAPDOOR);
                } else if (floorTrapDoorModel.getFloorStatus() == FloorTrapDoorModel.FLOOR_STATUS.OPEN) {
                    floorTrapDoorModel.getAnimation().setPlayMode(Animation.PlayMode.REVERSED);
                    floorTrapDoorModel.setAnimationTime(0);
                    floorTrapDoorModel.setFloorStatus(FloorTrapDoorModel.FLOOR_STATUS.CLOSE);
                    floorTrapDoorModel.setFloorEventTime(0);
                    AudioManager.getInstance().playSound(AudioManager.SOUND.TRAPDOOR);
                }
                floorTrapDoorModel.setFloorEventTime(0);
            }

            // No consideramos que esta realmente abierto hasta que haya acabado su animacion de apertura.
            if (floorTrapDoorModel.getFloorStatus() == FloorTrapDoorModel.FLOOR_STATUS.OPENING) {
                if (floorTrapDoorModel.getAnimation().isAnimationFinished(floorTrapDoorModel.getAnimationTime())) {
                    floorTrapDoorModel.setFloorStatus(FloorTrapDoorModel.FLOOR_STATUS.OPEN);
                }
            }

            if (floorTrapDoorModel.getFloorStatus() == FloorTrapDoorModel.FLOOR_STATUS.OPEN) {
                if (boardObjectAbove != null) {
                    if (boardObjectAbove.canFall()) {
                        if (!boardObjectAbove.isTerminated()) {
                            if (boardObjectAbove instanceof BoxModel) {
                                boxListener.setBoxBoardObjectEvent((BoxModel) boardObjectAbove, BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);
                            } else {
                                boardObjectAbove.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);
                            }
                        }
                    }
                }
            }

            floorTrapDoorModel.setFloorEventTime(floorTrapDoorModel.getFloorEventTime() + (1f * delta));
        }

        // ROTATION FLOOR
        else if (boardObject instanceof FloorRotatingModel) {
            FloorRotatingModel rotationFloor = (FloorRotatingModel) boardObject;
            if (rotationFloor.needRotation()) {
                boolean rotationFinished = false;
                if (rotationFloor.getRotationStatus() == FloorRotatingModel.ROTATION_STATUS.NONE) {
                    // Cuenta atras para próxima rotacion
                    rotationFloor.setRotationCountdown(MathUtils.clamp(rotationFloor.getRotationCountdown() - (1 * delta), 0, 999));
                    if (rotationFloor.getRotationCountdown() == 0) {
                        if(!rotationFloor.isNextDirectionTheSame()){
                            rotationFloor.setRotationStatus(FloorRotatingModel.ROTATION_STATUS.ROTATING);
                            float rotationModificator = nextOptimalRotationModificator(rotationFloor);
                            if (Math.abs(rotationModificator) != 180)
                                rotationFloor.setRotationVelocity(rotationFloor.getRotationVelocityMultiplier());
                            else rotationFloor.setRotationVelocity(rotationFloor.getRotationVelocityMultiplier() * 2);
                            rotationFloor.setNextRotationValue(rotationFloor.getRotation() + rotationModificator);
                        } else {
                            // Si la siguiente direccion fuera la misma que la actual, hacemos caso omiso y esperamos una vuelta
                            // Sumamos al contador el tiempo que tardaria en girar para que no se desincronice con nada
                            rotationFloor.setNextIndexInDirectionsList();
                            rotationFloor.setRotationCountdown(rotationFloor.getRotationTime() + rotationFloor.getRotationDuration());
                        }
                    }
                } else if (rotationFloor.getRotationStatus() == FloorRotatingModel.ROTATION_STATUS.ROTATING) {
                    // Sumamos o restamos grados hacia la nueva rotación y finalmente lo transformamos en grados de 0 a 359
                    if (rotationFloor.getRotation() < rotationFloor.getNextRotationValue()) {
                        if (rotationFloor.getRotation() + (rotationFloor.getRotationVelocity() * delta) < rotationFloor.getNextRotationValue()) {
                            rotationFloor.setRotation(rotationFloor.getRotation() + (rotationFloor.getRotationVelocity() * delta));
                        } else {
                            rotationFloor.setRotation(get360DegreesRotation(rotationFloor.getNextRotationValue()));
                            rotationFinished = true;
                        }
                    } else {
                        if (rotationFloor.getRotation() - (rotationFloor.getRotationVelocity() * delta) > rotationFloor.getNextRotationValue()) {
                            rotationFloor.setRotation(rotationFloor.getRotation() - (rotationFloor.getRotationVelocity() * delta));
                        } else {
                            rotationFloor.setRotation(get360DegreesRotation(rotationFloor.getNextRotationValue()));
                            rotationFinished = true;
                        }
                    }

                    if(rotationFinished){
                        rotationFloor.setRotationStatus(FloorRotatingModel.ROTATION_STATUS.NONE);
                        rotationFloor.setDirectionFromRotation();
                        rotationFloor.setNextIndexInDirectionsList();
                        rotationFloor.setRotationCountdown(rotationFloor.getRotationTime() - rotationFloor.getRotationDuration());
                    }
                }
            }
        }

        // BUTTON
        else if (boardObject instanceof FloorButtonModel) {
            FloorButtonModel button = (FloorButtonModel) boardObject;
            if (boardObjectAbove != null && boardObjectAbove instanceof BoxModel) {
                if(button.isTheCorrectColor(((BoxModel) boardObjectAbove).getColor())) {
                    if (!button.isPressed()) {
                        button.setPressed(true);
                        GlobalLevelData.getInstance().getBoardButtons().modifyButtonsPressed(button.getColor(), 1);
                        if (GlobalLevelData.getInstance().getBoardButtons().getButtonsPressed(button.getColor()) ==
                                GlobalLevelData.getInstance().getBoardButtons().getTotalButtons(button.getColor())) {
                            changeBarriersStatus(button.getColor(), true, false);
                            AudioManager.getInstance().playSound(AudioManager.SOUND.FLOORBUTTON_ACTIVATE);
                        }
                    }
                } else {
                    if (button.isPressed()) {
                        button.setPressed(false);
                        GlobalLevelData.getInstance().getBoardButtons().modifyButtonsPressed(button.getColor(), -1);
                        changeBarriersStatus(button.getColor(), false, true);
                    }
                }
            } else {
                if(button.isPressed()){
                    button.setPressed(false);
                    GlobalLevelData.getInstance().getBoardButtons().modifyButtonsPressed(button.getColor(), -1);
                    changeBarriersStatus(button.getColor(), false, true);
                }
            }
        }

        // BARRIER
        else if (boardObject instanceof FloorBarrierModel) {
            FloorBarrierModel barrier = (FloorBarrierModel) boardObject;
            // Controlamos los estados intermedios y finales
            if (barrier.getBarrierStatus() == FloorBarrierModel.BARRIER_STATUS.UNBLOCKING) {
                if (barrier.getAnimation().isAnimationFinished(barrier.getAnimationTime())) {
                    barrier.setIdleStatus(FloorBarrierModel.BARRIER_STATUS.UNBLOCKED);
                }
            } else if (barrier.getBarrierStatus() == FloorBarrierModel.BARRIER_STATUS.BLOCKING) {
                if (barrier.getAnimation().isAnimationFinished(barrier.getAnimationTime())) {
                    barrier.setIdleStatus(FloorBarrierModel.BARRIER_STATUS.BLOCKED);
                }
            }

            // Barrera temporizada
            if(barrier.isAutomatic()){
                if (barrier.getFloorEventTime() >= barrier.getEventTimeEnd()) {
                    if(barrier.isBarrierInIdle()){
                        if(barrier.getBarrierStatus() == FloorBarrierModel.BARRIER_STATUS.BLOCKED){
                            barrier.beginNewStatus(FloorBarrierModel.BARRIER_STATUS.UNBLOCKED);
                        } else {
                            barrier.beginNewStatus(FloorBarrierModel.BARRIER_STATUS.BLOCKED);
                        }
                        barrier.playBarrierSound();
                        barrier.setFloorEventTime(0);
                    }
                }
                barrier.setFloorEventTime(barrier.getFloorEventTime() + (1f * delta));

            } else {
                // Barrera activada por boton
                if(barrier.isBarrierInIdle()) {
                    if(barrier.getBarrierStatus() != barrier.getNextBarrierStatus()) {
                        barrier.beginNewStatus(barrier.getNextBarrierStatus());
                        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_BARRIER_ACTIVATED, barrier.getColor());
                    }
                }
            }

            //Destruccion de aboves
            if (boardObjectAbove != null && !boardObjectAbove.isTerminated()) {
                if (barrier.getBarrierStatus() == FloorBarrierModel.BARRIER_STATUS.BLOCKING ||
                        barrier.getBarrierStatus() == FloorBarrierModel.BARRIER_STATUS.BLOCKED) {
                    if(boardObjectAbove instanceof iDestroyableBoardObject) {
                        DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.SMASHED));
                        ((iDestroyableBoardObject) boardObjectAbove).setDestruction(destructionDTO);
                        if (boardObjectAbove instanceof BoxModel) {
                            boxListener.setBoxBoardObjectEvent((BoxModel) boardObjectAbove, BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                        } else {
                            boardObjectAbove.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                        }
                    }
                }
            }
        }
    }

    private void changeBarriersStatus(GlobalAttributes.COLOR color, boolean activate, boolean playBarrierSound){
        for(FloorBarrierModel barrier : floorBarrierModels){
            if(barrier.getColor() == color){
                if(playBarrierSound) barrier.playBarrierSound();
                if(activate) barrier.activateChangedStatus();
                else barrier.activateBaseStatus();
            }
        }
    }

    private float nextOptimalRotationModificator(FloorRotatingModel rotationFloor){
        MovableBoardObjectModel.DIRECTION actualDirection = rotationFloor.getActualDirection();
        MovableBoardObjectModel.DIRECTION nextDirection = rotationFloor.getNextDirection();
        switch(actualDirection){
            case UP:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.LEFT) return 90;
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.RIGHT) return -90;
                break;
            case RIGHT:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.UP) return 90;
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.DOWN) return -90;
                break;
            case DOWN:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.RIGHT) return 90;
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.LEFT) return -90;
                break;
            case LEFT:
                if(nextDirection == MovableBoardObjectModel.DIRECTION.UP) return -90;
                else if(nextDirection == MovableBoardObjectModel.DIRECTION.DOWN) return 90;
                break;
        }
        return 180;
    }

    private float get360DegreesRotation(float rotation){
        if(rotation >= 360)
            rotation -= 360;
        if(rotation < 0)
            rotation += 360;
        return rotation;
    }
}

