package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseDecoModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iColoredObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.ExplosiveRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.PusherRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoardObjectTerminatorManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseModel;

/**
 * Created by JordiRM on 01/03/2016.
 */
public class BoardObjectTerminatorManagerImpl implements BoardObjectTerminatorManager {
    private LevelModel levelModel;
    private GameEventsManagerImpl gameEventsManager;
    private BoxListener boxListener;
    private int teleportChainCountLocal = 0;

    public BoardObjectTerminatorManagerImpl(LevelModel levelModel, GameEventsManagerImpl gameEventsManager, BoxListener boxListener) {
        this.levelModel = levelModel;
        this.gameEventsManager = gameEventsManager;
        this.boxListener = boxListener;
    }

    @Override
    public void updateObject(BoardObjectModel boardObjectModel, float delta) {
        switch (boardObjectModel.getEvent()) {
            case DESTROYING:
                destroyEvent(boardObjectModel);
                break;
            case TELEPORTING:
                teleportEvent(boardObjectModel, delta);
                break;
            case FALLING:
                fallEvent(boardObjectModel, delta);
                break;
            case ERASING:
                eraseEvent(boardObjectModel);
                break;
            default:
                break;
        }
    }

    // ERASING
    private void eraseEvent(BoardObjectModel boardObjectModel) {
        boardObjectModel.setStatus(BoardObjectModel.BOARD_OBJECT_STATUS.ERASED);
    }

    // DESTROY
    private void destroyEvent(BoardObjectModel boardObjectModel) {
        boardObjectModel.setStatus(BoardObjectModel.BOARD_OBJECT_STATUS.ERASED);
        if (boardObjectModel instanceof BoxModel) {
            gameEventsManager.addFloatingLostBox(boardObjectModel.getPositionXCenter(), boardObjectModel.getPositionYCenter());
            GlobalLevelData.getInstance().subtractRatlien(((BoxModel) boardObjectModel).howManyRatliensAttached());
            ((iDestroyableBoardObject) boardObjectModel).executeDestruction();
            if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                updateBoxesStatus(OBJECT_TERMINATION_EVENT.ELIMINATED, ((BoxModel) boardObjectModel).getColor());
            }
        } else {
            ((iDestroyableBoardObject) boardObjectModel).executeDestruction();
            if (boardObjectModel instanceof PusherRobotModel) {
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT);
                this.gameEventsManager.enemyDestroyed(GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT);
            } else if (boardObjectModel instanceof ExplosiveRobotModel) {
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT);
                this.gameEventsManager.enemyDestroyed(GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT);
            } else if (boardObjectModel instanceof RatlienModel) {
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
                this.gameEventsManager.enemyDestroyed(GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
            }
        }
    }

    // FALL
    private void fallEvent(BoardObjectModel boardObjectModel, float delta) {
        if (boardObjectModel.getScaleY() > 0) {

            // Movimiento hacia el centro del agujero si el objeto estaba en movimiento para una transición suave
            if ((boardObjectModel.getPositionXCenter()) > boardObjectModel.getCellPositionXCenter(boardObjectModel.getColumn())) {
                boardObjectModel.setPositionX(MathUtils.clamp(boardObjectModel.getPositionX() - (300 * delta), boardObjectModel.getCellPositionX(boardObjectModel.getColumn()), 9999)); //300
            } else if ((boardObjectModel.getPositionXCenter()) < boardObjectModel.getCellPositionXCenter(boardObjectModel.getColumn())) {
                boardObjectModel.setPositionX(MathUtils.clamp(boardObjectModel.getPositionX() + (300 * delta), 0, boardObjectModel.getCellPositionX(boardObjectModel.getColumn())));
            }

            if (boardObjectModel.getScaleY() >= 1.0f) {
                if ((boardObjectModel.getPositionYCenter()) > boardObjectModel.getCellPositionYCenter(boardObjectModel.getRow())) {
                    boardObjectModel.setPositionY(MathUtils.clamp(boardObjectModel.getPositionY() - (300 * delta), boardObjectModel.getCellPositionY(boardObjectModel.getRow()), 9999));
                } else if ((boardObjectModel.getPositionYCenter()) < boardObjectModel.getCellPositionYCenter(boardObjectModel.getRow())) {
                    boardObjectModel.setPositionY(MathUtils.clamp(boardObjectModel.getPositionY() + (300 * delta), 0, boardObjectModel.getCellPositionY(boardObjectModel.getRow())));
                }
            }

            // Solo si estamos centrados en la casilla o ya hemos empezado a caer
            if ( (boardObjectModel.getPositionY() == boardObjectModel.getCellPositionY(boardObjectModel.getRow())
                    && boardObjectModel.getPositionX() == boardObjectModel.getCellPositionX(boardObjectModel.getColumn()) )
                    || boardObjectModel.getScaleY() < 1.0f) {
                // Inicio de la caida
                if (boardObjectModel.getScaleY() == 1.0f){
                    // Ponemos la caja a ras de suelo por si tiene que pasar por detras de la casilla inferior
                    boardObjectModel.setDepth(boardObjectModel.getDepth() + 31);
                    boardObjectModel.setRowDepthDependent(false);
                    AudioManager.getInstance().playSound(AudioManager.SOUND.FALL);

                    // Eliminamos el objeto del board para que no haya más interacciones con el.
                    levelModel.getCurrentBoardModel().deleteBoardObject(boardObjectModel.getColumn(), boardObjectModel.getRow(), boardObjectModel.getBoardObjectLayer());
                    boardObjectModel.setOnBoard(false);

                    // Si es una caja, matamos los ratliens que pueda tener enganchados
                    if (boardObjectModel instanceof BoxModel) {
                        if (((BoxModel) boardObjectModel).howManyRatliensAttached() > 0) {
                            GlobalLevelData.getInstance().subtractRatlien(((BoxModel) boardObjectModel).howManyRatliensAttached());
                            ((BoxModel) boardObjectModel).killAllRatliens(false);
                        }
                    }
                }

                boardObjectModel.setScaleY(MathUtils.clamp(boardObjectModel.getScaleY() - (2 * delta), 0, 1));
                boardObjectModel.setScaleX(MathUtils.clamp(boardObjectModel.getScaleX() - (2 * delta), 0, 1));
                boardObjectModel.setPositionY(boardObjectModel.getPositionY() - (110 * delta));//70

                // Acciones al principio de la caida.
                if (boardObjectModel.getScaleY() < 0.8f) {

                    // Si cae y debajo hay una decoracion, choca con ella y se destruye
                    BoardObjectModel overBaseModel = levelModel.getCurrentBoardModel().getOverBase(boardObjectModel.getColumn(), boardObjectModel.getRow());
                    if (overBaseModel != null) {
                        if (overBaseModel instanceof OverBaseDecoModel) {
                            if(boardObjectModel instanceof iDestroyableBoardObject) {
                                DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.EXPLOSION));
                                ((iDestroyableBoardObject) boardObjectModel).setDestruction(destructionDTO);
                                boardObjectModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                            }
                        }
                    }
                }
            }
        } else {
            boardObjectModel.setStatus(BoardObjectModel.BOARD_OBJECT_STATUS.ERASED);
            if (boardObjectModel instanceof BoxModel) {
                gameEventsManager.addFloatingLostBox(boardObjectModel.getPositionXCenter(), boardObjectModel.getPositionYCenter() + 20);
                if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                    updateBoxesStatus(OBJECT_TERMINATION_EVENT.ELIMINATED, ((BoxModel) boardObjectModel).getColor());
                }
            } else if (boardObjectModel instanceof RobotModel) {
                GlobalLevelData.getInstance().subtractRobot();
                if (boardObjectModel instanceof PusherRobotModel)
                    ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT);
                else if (boardObjectModel instanceof ExplosiveRobotModel)
                    ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT);
            } else if (boardObjectModel instanceof RatlienModel) {
                GlobalLevelData.getInstance().subtractRatlien();
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
            }
        }
    }

    // TELEPORT
    private void teleportEvent(BoardObjectModel boardObjectModel, float delta) {
        boolean teleportSpread;
        if (boardObjectModel.getAlpha() > 0.55f) {
            teleportSpread = false;
        } else {
            teleportSpread = true;
        }

        // FX
        if (boardObjectModel.getAlpha() == 1.0f) {
            EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.TELEPORT, boardObjectModel.getPositionX() + 16,
                    boardObjectModel.getPositionY() + 26, boardObjectModel.getDepth() - 1000);
            AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_TELEPORT);
            GlobalLevelData.getInstance().increaseBoxesTeleporting(1);

            boardObjectModel.setDepth(boardObjectModel.getDepth() - 420); // Lo ponemos muy por delante para que sobrepase los cañones, que van por encima
            boardObjectModel.setRowDepthDependent(false);
        }
        if (boardObjectModel.getAlpha() > 0) {
            boardObjectModel.setAlpha(MathUtils.clamp(boardObjectModel.getAlpha() - (2.8f * delta), 0, 1));
            boardObjectModel.setScaleY(boardObjectModel.getScaleY() + (6.0f * delta));
            boardObjectModel.setScaleX(boardObjectModel.getScaleX() - (2.0f * delta));
            boardObjectModel.setPositionY(boardObjectModel.getPositionY() + (240 * delta));

            // Propagación del teleport al resto del grupo
            if (boardObjectModel.getAlpha() <= 0.55f && !teleportSpread) {
                if (boardObjectModel instanceof BoxModel) {
                    if (((BoxModel) boardObjectModel).getBoxUp() != null) {
                        if(((BoxModel) boardObjectModel).getBoxUp().isReadyForTeleport())
                            boxListener.setBoxBoardObjectEvent(((BoxModel) boardObjectModel).getBoxUp(), BoardObjectModel.BOARD_OBJECT_EVENT.TELEPORTING, true);
                    }
                    if (((BoxModel) boardObjectModel).getBoxDown() != null) {
                        if(((BoxModel) boardObjectModel).getBoxDown().isReadyForTeleport())
                            boxListener.setBoxBoardObjectEvent(((BoxModel) boardObjectModel).getBoxDown(), BoardObjectModel.BOARD_OBJECT_EVENT.TELEPORTING, true);
                    }
                    if (((BoxModel) boardObjectModel).getBoxLeft() != null) {
                        if(((BoxModel) boardObjectModel).getBoxLeft().isReadyForTeleport())
                            boxListener.setBoxBoardObjectEvent(((BoxModel) boardObjectModel).getBoxLeft(), BoardObjectModel.BOARD_OBJECT_EVENT.TELEPORTING, true);
                    }
                    if (((BoxModel) boardObjectModel).getBoxRight() != null) {
                        if(((BoxModel) boardObjectModel).getBoxRight().isReadyForTeleport())
                            boxListener.setBoxBoardObjectEvent(((BoxModel) boardObjectModel).getBoxRight(), BoardObjectModel.BOARD_OBJECT_EVENT.TELEPORTING, true);
                    }

                    // Eliminamos el objeto del board para que no haya más interacciones con el.
                    levelModel.getCurrentBoardModel().deleteBoardObject(boardObjectModel.getColumn(), boardObjectModel.getRow(), boardObjectModel.getBoardObjectLayer());
                    boardObjectModel.setOnBoard(false);

                    // Aunque no debería pasar, nos aseguramos restar cualquier ratíen que pueda seguir enganchado
                    if (((BoxModel) boardObjectModel).howManyRatliensAttached() > 0) {
                        GlobalLevelData.getInstance().subtractRatlien(((BoxModel) boardObjectModel).howManyRatliensAttached());
                    }
                }
            }
        } else {
            boardObjectModel.setStatus(BoardObjectModel.BOARD_OBJECT_STATUS.ERASED);
            updateBoxesStatus(OBJECT_TERMINATION_EVENT.OBTAINED, ((BoxModel) boardObjectModel).getColor());
            // Activación de posibles puntos de suelo
            OverBaseModel overBase = (OverBaseModel) levelModel.getCurrentBoardModel().getOverBase(boardObjectModel.getColumn(), boardObjectModel.getRow());
            if (overBase != null && overBase instanceof OverBaseBoxActivableModel) {
                OverBaseBoxActivableModel overBaseActivable = (OverBaseBoxActivableModel) overBase;
                if (overBaseActivable.getOverBaseType() == OverBaseModel.OVERBASE_TYPE.POINTS) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.OVERBASE_TRIGGERED);
                    overBaseActivable.activate();
                    gameEventsManager.addNewOverBasePointsEvent(0, 0, true); // Anunciamos que se ha activado
                } else if(overBaseActivable.getOverBaseType() == OverBaseModel.OVERBASE_TYPE.POWERS) {
                    overBaseActivable.touch();
                }
            }

            // Recuento y finalización de combo
            GlobalLevelData.getInstance().decreaseBoxesTeleporting(1);
            teleportChainCountLocal += 1;
            if (GlobalLevelData.getInstance().getBoxesTeleporting() == 0) {
                gameEventsManager.addChangeInBoxesEvent();
                gameEventsManager.addNewBoxPointsEvent(boardObjectModel.getPositionXCenter(), boardObjectModel.getPositionYCenter() - 30, teleportChainCountLocal);
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_BOX_COMBO, teleportChainCountLocal);
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_TRANSPORTED_COLOR, ((iColoredObject) boardObjectModel).getColor());
                teleportChainCountLocal = 0;
            }
        }
    }

    private void updateBoxesStatus(OBJECT_TERMINATION_EVENT event, GlobalAttributes.COLOR color) {
        switch (color) {
            case RED:
                GlobalLevelData.getInstance().getRemainingBoxes().decreaseRed(1);
                //Gdx.app.log("Red Boxes", Integer.toString(GlobalLevelData.getInstance().getRemainingBoxes().getRed()));
                break;
            case BLUE:
                GlobalLevelData.getInstance().getRemainingBoxes().decreaseBlue(1);
                //Gdx.app.log("Blue Boxes", Integer.toString(GlobalLevelData.getInstance().getRemainingBoxes().getBlue()));
                break;
            case YELLOW:
                GlobalLevelData.getInstance().getRemainingBoxes().decreaseYellow(1);
                //Gdx.app.log("Yellow Boxes", Integer.toString(GlobalLevelData.getInstance().getRemainingBoxes().getYellow()));
                break;
        }
        switch (event) {
            case OBTAINED:
                GlobalLevelData.getInstance().decreaseRequestedBoxes(1);
                break;
            case ELIMINATED:
                GlobalLevelData.getInstance().increaseLostBoxes(1);
                gameEventsManager.addChangeInBoxesEvent();
                ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_LOST_BOX);
                break;
        }
    }

    private enum OBJECT_TERMINATION_EVENT {
        ELIMINATED, OBTAINED
    }
}
