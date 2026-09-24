package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.BoardModelHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.EnemyBehaviourManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectRobotBubble;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RatlienLairModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.iDestroyableBoardObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.ExplosiveRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.PusherRobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RatlienModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.RobotModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jordi.montornes on 05/04/2016.
 */
public class EnemyBehaviourManagerImpl implements EnemyBehaviourManager {
    private static final float TIME_BETWEEN_EVENTS = 0.15f;
    private static final float TIME_FOR_MAIN_ACTION = 5.0f;//2.5f;
    private LevelModel levelModel;
    private BoardModel boardModel;
    private BoxListener boxListener;
    private float robotGeneratorInternalCounter;
    private float ratlienGeneratorInternalCounter;
    private Long ratlienChewingSound = null;
    private float ratlienChewingDelay = 0.8f;

    public EnemyBehaviourManagerImpl(LevelModel levelModel, BoardModel boardModel, BoxListener boxListener) {
        this.levelModel = levelModel;
        this.boardModel = boardModel;
        this.boxListener = boxListener;
        this.robotGeneratorInternalCounter = 0.0f;
        this.ratlienGeneratorInternalCounter = 0.0f;
    }

    @Override
    public void update(float delta){

        // Gestion del loop sonoro del ratlien mordiendo
        if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
            if (GlobalLevelData.getInstance().getRatliensChewing() > 0) {
                if (ratlienChewingSound == null) {
                    if (ratlienChewingDelay == 0)
                        ratlienChewingSound = AudioManager.getInstance().playLoopSound(AudioManager.SOUND.RATLIEN_CHEWING);
                    else if (ratlienChewingDelay > 0)
                        ratlienChewingDelay = MathUtils.clamp(ratlienChewingDelay - 1 * delta, 0, 99);
                }
            } else {
                if (ratlienChewingSound != null) {
                    AudioManager.getInstance().stopSoundId(AudioManager.SOUND.RATLIEN_CHEWING, ratlienChewingSound);
                    ratlienChewingSound = null;
                    ratlienChewingDelay = 0.8f;
                }
            }
        } else {
            if (ratlienChewingSound != null) {
                AudioManager.getInstance().stopSoundId(AudioManager.SOUND.RATLIEN_CHEWING, ratlienChewingSound);
                ratlienChewingSound = null;
            }
        }

        if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
            increaseRobotElevatorInternalCounter(delta);
            increaseRatlienLairInternalCounter(delta);
        }
    }

    @Override
    public void updateEnemy(EnemyModel enemyModel, float delta) {
        // Falling
        if (BoardModelHelper.getFallingGround(levelModel.getCurrentBoardModel(), enemyModel.getColumn(), enemyModel.getRow()))
            enemyModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);

        if(!enemyModel.isTerminated()) {
            if (enemyModel instanceof RobotModel) {
                updateRobot((RobotModel) enemyModel, delta);
            } else if (enemyModel instanceof RatlienModel) {
                updateRatlien((RatlienModel) enemyModel, delta);
            }
        }
    }

    private void updateRobot(RobotModel robotModel, float delta) {
        // Actualizamos el waitTime
        if(robotModel.getTimeBetweenEvents() > 0){
            robotModel.setTimeBetweenEvents(MathUtils.clamp(robotModel.getTimeBetweenEvents() - (1 * delta), 0, 999));
        }
        if(robotModel.getTimeForMainAction() > 0) {
            robotModel.setTimeForMainAction(MathUtils.clamp(robotModel.getTimeForMainAction() - (1 * delta), 0, 999));
        }
        switch(robotModel.getEnemyStatus()){
            case MOVING:
                manageMovingStatus(robotModel);
                break;
            case STOPING:
                manageStopingStatus(robotModel);
                break;
            case NONE:
                decideNewAction(robotModel);
                break;
            case CHANGING_DIRECTION:
                manageChangeDirection(robotModel, delta);
                break;
            case PUSHING:
                managePushingStatus((PusherRobotModel) robotModel, delta);
                break;
            case EXPLODING:
                manageExplodingStatus((ExplosiveRobotModel) robotModel, delta);
                break;
            case ENTERING:
                if(robotModel.getEvent() != BoardObjectModel.BOARD_OBJECT_EVENT.ERASING)
                    manageEnteringEvent(robotModel, delta);
                break;
            case WAITING:
                manageWaitingStatus(robotModel, delta);
                break;
            case DISABLED:
                manageDisabledStatus(robotModel);
        }
    }

    private void updateRatlien(RatlienModel ratlienModel, float delta) {
        if(ratlienModel.getTimeBetweenEvents() > 0){
            ratlienModel.setTimeBetweenEvents(MathUtils.clamp(ratlienModel.getTimeBetweenEvents() - (1 * delta), 0, 999));
        }
        if(ratlienModel.getTimeForMainAction() > 0){
            ratlienModel.setTimeForMainAction(MathUtils.clamp(ratlienModel.getTimeForMainAction() - (1 * delta), 0, 999));
        }
        switch(ratlienModel.getEnemyStatus()){
            case MOVING:
                manageMovingStatus(ratlienModel);
                break;
            case STOPING:
                manageStopingStatus(ratlienModel);
                break;
            case NONE:
                decideNewAction(ratlienModel);
                break;
            case CHANGING_DIRECTION:
                manageChangeDirection(ratlienModel, delta);
                break;
            case ENTERING:
                if(ratlienModel.getEvent() != BoardObjectModel.BOARD_OBJECT_EVENT.ERASING)
                    manageEnteringEvent(ratlienModel);
                break;
            case ATTACHING:
                manageAttachingStatus(ratlienModel);
                break;
            case DISABLED:
                manageDisabledStatus(ratlienModel);
        }
    }

    private void manageAttachingStatus(RatlienModel ratlienModel) {
        if(ratlienModel.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE) {
            BoxModel boxModel = ratlienModel.getTargetBox();
            if(!ratlienModel.isDisabled()) {
                if (boxModel.getColumn() == ratlienModel.getCurrentTargetBoxColumn()
                        && boxModel.getRow() == ratlienModel.getCurrentTargetBoxRow()
                        && boxModel.getEvent().equals(BoardObjectModel.BOARD_OBJECT_EVENT.IDLE)) {
                    MovableBoardObjectModel.DIRECTION targetDirection = ratlienModel.getTargetDirection();
                    boolean canAttachRatlien = boxModel.canAttachRatlien(targetDirection);
                    if(canAttachRatlien) {
                        ratlienModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.ERASING, true);
                        // Sacamos al ratlien del board y lo marcamos como que ya no pertenece a este
                        boardModel.deleteBoardObject(ratlienModel.getColumn(), ratlienModel.getRow(),
                                BoardObjectModel.BOARD_OBJECT_LAYER.ABOVE);
                        ratlienModel.setOnBoard(false);

                        boxModel.attachRatlien(targetDirection);
                        boxListener.updateBoxGroup(boxModel);
                        boxListener.updateNeighboursBoxesGroup(boxModel.getColumn(), boxModel.getRow(), boxModel.getColor());
                    } else {
                        startMoving(ratlienModel);
                    }
                } else {
                    decideNewAction(ratlienModel);
                }
            } else {
                ratlienModel.disableEnemy();
            }
        }
    }

    @Override
    public void updateRobotElevator(RobotElevatorModel robotElevatorModel, LevelModel levelModel, float delta) {
        if(robotElevatorModel.isActive()){
            if(!robotElevatorModel.isDisabled()) {
                if (robotElevatorModel.getRobotElevatorStatus() == RobotElevatorModel.ROBOT_ELEVATOR_STATUS.CLOSED) {
                    tryToOpenElevator(robotElevatorModel);
                } else if (robotElevatorModel.getRobotElevatorStatus() == RobotElevatorModel.ROBOT_ELEVATOR_STATUS.OPENED) {
                    tryToGenerateRobot(robotElevatorModel, levelModel);
                    robotElevatorModel.setActive(false);
                }
            }else{
                robotElevatorModel.setActive(false);
            }
        }
        robotElevatorModel.update();
    }

    private void tryToOpenElevator(RobotElevatorModel robotElevatorModel) {
        robotElevatorModel.setRobotElevatorStatus(RobotElevatorModel.ROBOT_ELEVATOR_STATUS.OPENING);
    }

    private void tryToGenerateRobot(RobotElevatorModel robotElevatorModel, LevelModel levelModel) {
        //Gdx.app.log("Enemymanager", "Current number of robots is " + GlobalLevelData.getInstance().getNumberRobots());
        if (GlobalLevelData.getInstance().getNumberRobots() < GlobalLevelData.getInstance().getMaxNumRobots()) {
            generateRobot(robotElevatorModel, levelModel);
        }
    }

    private void generateRobot(RobotElevatorModel robotElevatorModel, LevelModel levelModel) {
        if (MathUtils.random(1, 100) <= levelModel.getPusherRobot())
            levelModel.addRobot(robotElevatorModel.getColumn(), robotElevatorModel.getRow(), EnemyModel.ENEMY_TYPE.NORMAL_ROBOT, robotElevatorModel);
        else
            levelModel.addRobot(robotElevatorModel.getColumn(), robotElevatorModel.getRow(), EnemyModel.ENEMY_TYPE.EXPLOSIVE_ROBOT, robotElevatorModel);
    }

    private RobotElevatorModel selectRobotElevator(){
        List<RobotElevatorModel> elevatorsList = levelModel.getRobotElevatorsList();

        // Hacemos una nueva lista con los elevators que esten disponiebles y no bloqueados
        ArrayList<RobotElevatorModel> availableElevators = new ArrayList<RobotElevatorModel>();
        for(int i = 0; i < elevatorsList.size(); i++){
            // (i) Ahora mismo el ascensor se condisera bloqueado con cualquier above que haya encima
            if(elevatorsList.get(i).getRobotElevatorStatus() == RobotElevatorModel.ROBOT_ELEVATOR_STATUS.CLOSED) {
                if (boardModel.getBoardObjectAbove(elevatorsList.get(i).getColumn(), elevatorsList.get(i).getRow()) == null) {
                    availableElevators.add(elevatorsList.get(i));
                }
            }
        }

        if(availableElevators.size() == 0) return null;

        // Randomizamos los elevators disponibles
        for(RobotElevatorModel robotElevatorModel : availableElevators) {
            switch (robotElevatorModel.getPriority()) {
                case 0:
                    robotElevatorModel.setRandomValue(MathUtils.random(0,20));
                    break;
                case 1:
                    robotElevatorModel.setRandomValue(MathUtils.random(0,30));
                    break;
                case 2:
                    robotElevatorModel.setRandomValue(MathUtils.random(0,40));
                    break;
                default:
                    robotElevatorModel.setRandomValue(MathUtils.random(0,30));
                    break;
            }
        }

        // Elegimos el mas alto
        ArrayList<RobotElevatorModel> selectedElevators = new ArrayList<RobotElevatorModel>();
        for(int i = 0; i < availableElevators.size(); i++){
            if(i == 0) selectedElevators.add(availableElevators.get(i));
            else{
                if(availableElevators.get(i).getRandomValue() >= selectedElevators.get(0).getRandomValue()){
                    if(availableElevators.get(i).getRandomValue() > selectedElevators.get(0).getRandomValue()) selectedElevators.clear();
                    selectedElevators.add(availableElevators.get(i));
                }
            }
        }
        return selectedElevators.get((MathUtils.random(0, selectedElevators.size() - 1)));
    }

    private RatlienLairModel selectRatlienLair(){
        List<RatlienLairModel> ratlienLairList = levelModel.getRatlienLairList();

        // Hacemos una nueva lista con las guaridas de ratlien que esten disponibles y no bloqueadas
        List<RatlienLairModel> availableRatlienLairs = new ArrayList<RatlienLairModel>();
        for(int i = 0; i < ratlienLairList.size(); i++){
            // (i) Ratlien Lair se considera bloqueada si hay una caja en todas sus direcciónes de salida.
            RatlienLairModel ratlienLairModel = ratlienLairList.get(i);
            if(ratlienLairModel.isDisabled()) continue;
            if(ratlienLaisHasAnyExitAvailable(ratlienLairModel)) availableRatlienLairs.add(ratlienLairModel);
        }
        if(availableRatlienLairs.size() == 0) return null;

        // Randomizamos los lairs disponibles
        for(RatlienLairModel ratlienLairModel : availableRatlienLairs) {
            switch (ratlienLairModel.getPriority()) {
                case 0:
                    ratlienLairModel.setRandomValue(MathUtils.random(0,20));
                    break;
                case 1:
                    ratlienLairModel.setRandomValue(MathUtils.random(0,30));
                    break;
                case 2:
                    ratlienLairModel.setRandomValue(MathUtils.random(0,40));
                    break;
                default:
                    ratlienLairModel.setRandomValue(MathUtils.random(0,30));
                    break;
            }
        }

        // Elegimos el mas alto
        ArrayList<RatlienLairModel> selecteRatlienLairs = new ArrayList<RatlienLairModel>();
        for(int i = 0; i < availableRatlienLairs.size(); i++){
            if(i == 0) selecteRatlienLairs.add(availableRatlienLairs.get(i));
            else{
                if(availableRatlienLairs.get(i).getRandomValue() >= selecteRatlienLairs.get(0).getRandomValue()){
                    if(availableRatlienLairs.get(i).getRandomValue() > selecteRatlienLairs.get(0).getRandomValue()) selecteRatlienLairs.clear();
                    selecteRatlienLairs.add(availableRatlienLairs.get(i));
                }
            }
        }
        return selecteRatlienLairs.get((MathUtils.random(0, selecteRatlienLairs.size() - 1)));
    }

    private boolean ratlienLaisHasAnyExitAvailable(RatlienLairModel lair){
        if(lair.hasExit(RatlienLairModel.EXIT_DIRECTION.UP)){
            if (boardModel.getBoardObjectAbove(lair.getColumn(), lair.getRow() - 1) == null) return true;
        }
        if(lair.hasExit(RatlienLairModel.EXIT_DIRECTION.DOWN)){
            if (boardModel.getBoardObjectAbove(lair.getColumn(), lair.getRow() + 1) == null) return true;
        }
        if(lair.hasExit(RatlienLairModel.EXIT_DIRECTION.LEFT)){
            if (boardModel.getBoardObjectAbove(lair.getColumn() - 1, lair.getRow()) == null) return true;
        }
        if(lair.hasExit(RatlienLairModel.EXIT_DIRECTION.RIGHT)){
            if (boardModel.getBoardObjectAbove(lair.getColumn() + 1, lair.getRow()) == null) return true;
        }
        return false;
    }

    private void generateRatlien(){
        RatlienLairModel ratlienLair = selectRatlienLair();
        if (ratlienLair != null){
            RatlienLairModel.EXIT_DIRECTION selectedDirection;
            ArrayList <RatlienLairModel.EXIT_DIRECTION> directions = new ArrayList<RatlienLairModel.EXIT_DIRECTION>();
            if(ratlienLair.hasExit(RatlienLairModel.EXIT_DIRECTION.UP)){
                if (boardModel.getBoardObjectAbove(ratlienLair.getColumn(), ratlienLair.getRow() - 1) == null)
                    directions.add(RatlienLairModel.EXIT_DIRECTION.UP);
            }
            if(ratlienLair.hasExit(RatlienLairModel.EXIT_DIRECTION.DOWN)){
                if (boardModel.getBoardObjectAbove(ratlienLair.getColumn(), ratlienLair.getRow() + 1) == null)
                    directions.add(RatlienLairModel.EXIT_DIRECTION.DOWN);
            }
            if(ratlienLair.hasExit(RatlienLairModel.EXIT_DIRECTION.LEFT)){
                if (boardModel.getBoardObjectAbove(ratlienLair.getColumn() - 1, ratlienLair.getRow()) == null)
                    directions.add(RatlienLairModel.EXIT_DIRECTION.LEFT);
            }
            if(ratlienLair.hasExit(RatlienLairModel.EXIT_DIRECTION.RIGHT)){
                if (boardModel.getBoardObjectAbove(ratlienLair.getColumn() + 1, ratlienLair.getRow()) == null)
                    directions.add(RatlienLairModel.EXIT_DIRECTION.RIGHT);
            }

            if(directions.size() > 0){
                // Entre las salidas posibles elegimos una al azar
                selectedDirection = directions.get(MathUtils.random(0, directions.size() - 1));

                switch (selectedDirection) {
                    case DOWN: default:
                        levelModel.addNewRatlien(ratlienLair.getColumn(), ratlienLair.getRow() + 1, MovableBoardObjectModel.DIRECTION.DOWN, RatlienModel.ENTRY_TYPE.LAIR);
                        break;
                    case UP:
                        levelModel.addNewRatlien(ratlienLair.getColumn(), ratlienLair.getRow() - 1, MovableBoardObjectModel.DIRECTION.UP, RatlienModel.ENTRY_TYPE.LAIR);
                        break;
                    case LEFT:
                        levelModel.addNewRatlien(ratlienLair.getColumn() - 1, ratlienLair.getRow(), MovableBoardObjectModel.DIRECTION.LEFT, RatlienModel.ENTRY_TYPE.LAIR);
                        break;
                    case RIGHT:
                        levelModel.addNewRatlien(ratlienLair.getColumn() + 1, ratlienLair.getRow(), MovableBoardObjectModel.DIRECTION.RIGHT, RatlienModel.ENTRY_TYPE.LAIR);
                        break;
                }
            }
            ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_SPAWNED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
        }
    }

    @Override
    public void increaseRatlienLairInternalCounter(float delta) {
        // Si ya hay el maximo de ratliens, el contador se congela hasta que haya sitio
        if (GlobalLevelData.getInstance().getNumberRatliens() < GlobalLevelData.getInstance().getMaxNumRatliens()) {
            ratlienGeneratorInternalCounter = MathUtils.clamp(ratlienGeneratorInternalCounter + delta, 0, 9999);
            if (ratlienGeneratorInternalCounter >= GlobalLevelData.getInstance().getRatlienSpanTime()) {
                generateRatlien();
                ratlienGeneratorInternalCounter = 0;
            }
        }
    }

    @Override
    public void increaseRobotElevatorInternalCounter(float delta) {
        // Si ya hay el maximo de robots, el contador se congela hasta que haya sitio
        if (GlobalLevelData.getInstance().getNumberRobots() < GlobalLevelData.getInstance().getMaxNumRobots()) {
            robotGeneratorInternalCounter = MathUtils.clamp(robotGeneratorInternalCounter + delta, 0, 9999);
            if (robotGeneratorInternalCounter >= GlobalLevelData.getInstance().getRobotSpanTime()) {
                RobotElevatorModel elevator = selectRobotElevator();
                if (elevator != null && !elevator.isDisabled()) elevator.setActive(true);
                robotGeneratorInternalCounter = 0;
            }
        }
    }

    private void manageExplodingStatus(ExplosiveRobotModel explosiveRobotModel, float delta) {
        if(!explosiveRobotModel.isDisabled()) {
            if (explosiveRobotModel.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE) {
                if (explosiveRobotModel.isEnemyReadyToExplode()) {
                    if (!explosiveRobotModel.isTerminated()) {
                        explosiveRobotModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                        DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.EXPLOSION));
                        explosiveRobotModel.setDestruction(destructionDTO);

                        // Sacamos el robot del board y lo marcamos como que ya no pertenece a este
                        boardModel.deleteBoardObject(explosiveRobotModel.getColumn(), explosiveRobotModel.getRow(),
                                BoardObjectModel.BOARD_OBJECT_LAYER.ABOVE);
                        explosiveRobotModel.setOnBoard(false);

                        // Mandamos destruir las cajas adyacentes
                        destroyNeighbours(explosiveRobotModel);
                    }
                }
            }
        }else{
            explosiveRobotModel.disableEnemy();
            explosiveRobotModel.cancelAutoDestruction();
        }
    }

    private void manageDisabledStatus(EnemyModel enemyModel) {
        if(!enemyModel.isDisabled()){
            // Si el robot estaba moviendose al desactivarse lo reanudamos
            if(enemyModel.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.PAUSED) {
                // Recuperamos los estatus anteriores antes de desactivarse
                enemyModel.setEnemyStatus(enemyModel.getLastEnemy_status());
                enemyModel.setMoveStatus(enemyModel.getLastMoveStatus());
            }
            else enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.NONE);
            if(enemyModel.getDisableParticleEffect() != null) {
                enemyModel.getDisableParticleEffect().finalizeParticle();
                enemyModel.removeDisableParticleEffect();
            }
        }
    }

    private void destroyNeighbours(ExplosiveRobotModel explosiveRobotModel) {
        List<BoardObjectModel> boardObjectModelList = BoardModelHelper.getAllNeighboursBoardObjects
                (boardModel, explosiveRobotModel.getColumn(),
                        explosiveRobotModel.getRow());
        for (BoardObjectModel boardObjectModel : boardObjectModelList) {
            if (boardObjectModel.isDestroyableByEnemy() && !boardObjectModel.isTerminated()) {
                if(boardObjectModel instanceof BoxModel)
                    boxListener.setBoxBoardObjectEvent((BoxModel) boardObjectModel, BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);
                else
                    boardObjectModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.DESTROYING, true);

                DestructionDTO destructionDTO = new DestructionDTO((DestructionDTO.DESTRUCTION_TYPE.EXPLOSION));
                ((iDestroyableBoardObject)boardObjectModel).setDestruction(destructionDTO);
            }
        }
    }
    private void startMoving(EnemyModel enemyModel){
        MovableBoardObjectModel.DIRECTION newEnemyDirection = calculateEnemyDirection(enemyModel);
        if(newEnemyDirection != MovableBoardObjectModel.DIRECTION.NEUTRAL) {
            if(newEnemyDirection != enemyModel.getPreviousDirection()) {
                // Asignamos la nueva direccion
                enemyModel.setNextDirection(newEnemyDirection);
                // Guardamos el siguiente numero de sprite basado en la nueva direccion a la que queremos ir
                enemyModel.setNextLookingSpriteNumber(getIdleSpriteNumberByDirection(newEnemyDirection));
                enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.CHANGING_DIRECTION);
            }else{
                // Lo ponemos en movicmiento de nuevo sin espera para que no haya parones.
                if(!enemyModel.isTerminated()) enemyModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.MOVING, false);
                enemyModel.setNewMove(newEnemyDirection);
                enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.MOVING);
                enemyModel.setWillStopOnDestination(false);
            }
        }else{
            enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.NONE);
        }
    }

    private void manageChangeDirection(EnemyModel enemyModel, float delta){
        if(enemyModel.getTimeBetweenEvents() == 0) {
            if(enemyModel.isLookingToNextDirection()) {
                if (!enemyModel.isDisabled()) {
                    if (!enemyModel.isTerminated())
                        enemyModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.MOVING, false);
                    enemyModel.setNewMove(enemyModel.getNextDirection());
                    enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.MOVING);
                    enemyModel.setWillStopOnDestination(false);
                } else {
                    enemyModel.disableEnemy();
                }
            } else {
                processTurning(enemyModel, delta);
            }
        }
    }

    public void processTurning(EnemyModel enemyModel, float delta) {
        int actualSpriteNumber = enemyModel.getLookingSpriteNumber();
        int nextSpriteNumber = enemyModel.getNextLookingSpriteNumber();
        if (enemyModel.getTurnTime() == 0) {
            if (nextSpriteNumber > actualSpriteNumber) {
                // Exceptición para pasar del 0 al 6 de manera eficiente
                if(actualSpriteNumber == 0 && nextSpriteNumber == 6){
                    enemyModel.setLookingSpriteNumber(7);
                } else {
                    enemyModel.setLookingSpriteNumber(actualSpriteNumber + 1);
                }
                enemyModel.setTurnTime(0.08f);
            } else if (nextSpriteNumber < actualSpriteNumber) {
                // Exceptición para pasar del 6 al 0 de manera eficiente
                if((actualSpriteNumber == 6 || actualSpriteNumber == 7)&& nextSpriteNumber == 0){
                    if(actualSpriteNumber == 6) enemyModel.setLookingSpriteNumber(7);
                    else enemyModel.setLookingSpriteNumber(0);
                } else {
                    enemyModel.setLookingSpriteNumber(actualSpriteNumber - 1);
                }
                enemyModel.setTurnTime(0.08f);
            }

            enemyModel.setAnimation(new Animation(0.1f, enemyModel.getAtlas().findRegion("idle", enemyModel.getLookingSpriteNumber())));
            enemyModel.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
        } else {
            enemyModel.setTurnTime(MathUtils.clamp(enemyModel.getTurnTime() - (1 * delta), 0, 99));
        }
    }

    private void managePushingStatus(PusherRobotModel pusherRobotModel, float delta) {
        if(pusherRobotModel.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE) {
            // Le decimos que sprite necesita para mirar donde empuja
            pusherRobotModel.setNextLookingSpriteNumber(getIdleSpriteNumberByDirection(pusherRobotModel.getTargetDirection()));
            if(!pusherRobotModel.isLookingToNextDirection()) processTurning(pusherRobotModel, delta);
            if (pusherRobotModel.isReadyToPush()) {
                BoxModel boxModel = pusherRobotModel.getTargetBox();
                if(!pusherRobotModel.isDisabled()) {
                    if (boxModel.getColumn() == pusherRobotModel.getCurrentTargetBoxColumn()
                            && boxModel.getRow() == pusherRobotModel.getCurrentTargetBoxRow()
                            && boxModel.getEvent().equals(BoardObjectModel.BOARD_OBJECT_EVENT.IDLE)) {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_PUSHING);
                        MovableBoardObjectModel.DIRECTION targetDirection = pusherRobotModel.getTargetDirection();
                        boxListener.setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.MOVING, false);
                        boxModel.setNewMove(targetDirection);
                        startMoving(pusherRobotModel);
                        pusherRobotModel.setTimeForMainAction(TIME_FOR_MAIN_ACTION);
                    } else {
                        EffectManager.getInstance().createSpriteEffectRobotBubble(pusherRobotModel.getPositionXCenter(),
                                pusherRobotModel.getPositionYCenter(), GameConstants.ABOVE_ALL_DEPTH, 1, SpriteEffectRobotBubble.ROBOT_BUBBLE_TYPE.SURPRISE);
                        pusherRobotModel.setWaitingTime(0.5f);
                        pusherRobotModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.WAITING);
                    }
                } else {
                    pusherRobotModel.disableEnemy();
                }
            }
        }
    }

    private void decideNewAction(EnemyModel enemyModel){
        GlobalAttributes.ACTION_ON_TARGET action = GlobalAttributes.ACTION_ON_TARGET.NONE;
        if(enemyModel instanceof PusherRobotModel) action = GlobalAttributes.ACTION_ON_TARGET.PUSH;
        else if(enemyModel instanceof ExplosiveRobotModel) action = GlobalAttributes.ACTION_ON_TARGET.EXPLODE;
        else if(enemyModel instanceof RatlienModel) action = GlobalAttributes.ACTION_ON_TARGET.INFEST;
        // Intentamos interactuar con algo y ver en que dirección está
        MovableBoardObjectModel.DIRECTION targetDirection = BoardModelHelper.getTargetDirection(boardModel, enemyModel.getColumn(), enemyModel.getRow(), action);
        if(enemyModel.getTimeForMainAction() == 0 && targetDirection != MovableBoardObjectModel.DIRECTION.NEUTRAL){
            // Sabemos que hay una caja objetivo en esa direccion así que lo hacemos.
            BoardObjectModel neighbourObject = BoardModelHelper.getNeighbourBoardObject(boardModel, enemyModel.getColumn(), enemyModel.getRow(), targetDirection);
            enemyModel.doEnemyMainAction(targetDirection, neighbourObject);
        }else{
            // Si no ha encontrado nada volvemos a movernos
            startMoving(enemyModel);
        }
    }

    private void manageEnteringEvent(RobotModel robotModel, float delta){
        float finalPosY =  robotModel.getCellPositionY(robotModel.getRow());
        if(robotModel.getPositionY() < finalPosY){
            robotModel.setPositionY(robotModel.getPositionY() + (100 * delta));
            if (robotModel.getPositionY() >= finalPosY) {
                robotModel.setPositionY(finalPosY);
                robotModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.NONE);
                robotModel.setTimeForMainAction(TIME_FOR_MAIN_ACTION);
            }
            // Cuando el robot asoma buena parte de su cuerpo se considera que ya pertenece al board y se puede interactuar
            // Hasta que esto ocurre no se considera que el robot ha entrado (Desafio)
            else if(!robotModel.isOnBoard() && robotModel.getPositionY() >= finalPosY - 20)  {
                if(boardModel.getBoardObjectAbove(robotModel.getColumn(), robotModel.getRow()) == null)
                {
                    // Incluimos el robot en el board para que se tome en cuenta y ocupe su casilla
                    boardModel.addBoardObjectAbove(robotModel, robotModel.getColumn(), robotModel.getRow());
                    robotModel.setOnBoard(true);
                    AudioManager.getInstance().playSound(AudioManager.SOUND.ROBOT_ENTER);
                    if(robotModel instanceof PusherRobotModel)
                        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_SPAWNED, GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT);
                    else if (robotModel instanceof ExplosiveRobotModel)
                        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_SPAWNED, GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT);
                } else {
                    // Como no puede salir, lo borramos directamente
                    robotModel.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.ERASING, true);
                    GlobalLevelData.getInstance().subtractRobot();
                }
            }
            else if( robotModel.getPositionY() >= finalPosY - 5){
                // Le ponemos el depth correcto antes de que se meta debajo de la celda superior (si es un muro)
                robotModel.setRowDepthDependent(true);
                robotModel.setDepth(GameConstants.ABOVE_DEPTH);
            }
            // Posicionamos la plataforma elevadora del ascensor segun la posicion del robot
            robotModel.getEntryElevator().getPlatformSprite().setPosition(robotModel.getEntryElevator().getPositionX(), MathUtils.clamp(robotModel.getPositionY(), 0, finalPosY));
        }
    }

    private void manageEnteringEvent(RatlienModel ratlienModel){

        if(ratlienModel.getAnimation().isAnimationFinished(ratlienModel.getAnimationTime())){
            ratlienModel.setDepth(GameConstants.ABOVE_DEPTH);
            ratlienModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.NONE);
            // Los ratliens a veces se engancharán muy rápido a las cajas, para que sean más impredecibles
            ratlienModel.setTimeForMainAction(TIME_FOR_MAIN_ACTION - (TIME_FOR_MAIN_ACTION / MathUtils.random(1f, 5f)));
            // Al acabar la animación de entrar lo ponemos en el sprite de idle que toque a la espera de la siguiente acción.
            ratlienModel.setAnimation(new Animation(0.1f, ratlienModel.getAtlas().findRegion("idle", ratlienModel.getLookingSpriteNumber())));
            ratlienModel.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            // Normalizamos la posicion de los sprites
            ratlienModel.setSpriteOffsetX(0);
            ratlienModel.setSpriteOffsetY(0);
        }
    }

    private void setStopingStatus(EnemyModel enemyModel) {
        enemyModel.setEnemyStatus(EnemyModel.ENEMY_STATUS.STOPING);
        enemyModel.setWillStopOnDestination(true);
        enemyModel.storeDestinationPosition(enemyModel.getCellPositionX(enemyModel.getColumn()),
                enemyModel.getCellPositionY(enemyModel.getRow()));
    }

    private void manageMovingStatus(EnemyModel enemyModel) {
        if(enemyModel.isDisabled()) {
            enemyModel.setLastEnemy_status(enemyModel.getEnemyStatus());
            enemyModel.setLastMoveStatus(enemyModel.getMoveStatus());
            enemyModel.setMoveStatus(MovableBoardObjectModel.MOVE_STATUS.PAUSED);
            enemyModel.disableEnemy();
        } else {
            if(enemyModel.getMoveStatus().equals(MovableBoardObjectModel.MOVE_STATUS.IDLE)) {
                decideNewAction(enemyModel);
            } else {
                if (enemyModel.shouldChangeAction()) {
                    setStopingStatus(enemyModel);
                }
            }
        }
    }

    private void manageStopingStatus(EnemyModel enemyModel) {
        if(enemyModel.getMoveStatus() == MovableBoardObjectModel.MOVE_STATUS.IDLE){
            if(enemyModel.isDisabled()) {
                enemyModel.disableEnemy();
            } else {
                decideNewAction(enemyModel);
            }
        }
    }

    private void manageWaitingStatus(EnemyModel enemyModel, float delta){
        float pauseTime = enemyModel.getWaitingTime();
        if(pauseTime > 0){
            enemyModel.setWaitingTime(MathUtils.clamp(pauseTime - (1 * delta), 0, 999));
        }
        else decideNewAction(enemyModel);

    }

    private MovableBoardObjectModel.DIRECTION calculateEnemyDirection(EnemyModel enemyModel) {

        MovableBoardObjectModel.DIRECTION currentDirection = enemyModel.getPreviousDirection();
        MovableBoardObjectModel.DIRECTION oppositeDirection = MovableBoardObjectModel.DIRECTION.getOpposite(currentDirection);
        List<MovableBoardObjectModel.DIRECTION> posibleDirections = new ArrayList<MovableBoardObjectModel.DIRECTION>();

        // Guardamos todas las direcciones libres
        if (!BoardModelHelper.getObstacleInDirection(boardModel, enemyModel.getColumn(), enemyModel.getRow(),
                MovableBoardObjectModel.DIRECTION.UP, enemyModel.getObstacleLevel(), enemyModel.CanAvoidFalls(), enemyModel.canAvoidDangers())) {
            posibleDirections.add(MovableBoardObjectModel.DIRECTION.UP);
        }
        if (!BoardModelHelper.getObstacleInDirection(boardModel, enemyModel.getColumn(), enemyModel.getRow(),
                MovableBoardObjectModel.DIRECTION.LEFT, enemyModel.getObstacleLevel(), enemyModel.CanAvoidFalls(), enemyModel.canAvoidDangers())) {
            posibleDirections.add(MovableBoardObjectModel.DIRECTION.LEFT);
        }
        if (!BoardModelHelper.getObstacleInDirection(boardModel, enemyModel.getColumn(), enemyModel.getRow(),
                MovableBoardObjectModel.DIRECTION.DOWN, enemyModel.getObstacleLevel(), enemyModel.CanAvoidFalls(), enemyModel.canAvoidDangers())) {
            posibleDirections.add(MovableBoardObjectModel.DIRECTION.DOWN);
        }
        if (!BoardModelHelper.getObstacleInDirection(boardModel, enemyModel.getColumn(), enemyModel.getRow(),
                MovableBoardObjectModel.DIRECTION.RIGHT, enemyModel.getObstacleLevel(), enemyModel.CanAvoidFalls(), enemyModel.canAvoidDangers())) {
            posibleDirections.add(MovableBoardObjectModel.DIRECTION.RIGHT);
        }

        // Si no hay direcciones no hacemos nada
        if(posibleDirections.size() == 0) return MovableBoardObjectModel.DIRECTION.NEUTRAL;
        else
        {
            // Intentamos ir en una direccion diferente a la actual o la contraria
            Collections.shuffle(posibleDirections);
            for(MovableBoardObjectModel.DIRECTION direction :  posibleDirections){
                if(direction != currentDirection && direction != oppositeDirection){
                    return direction;
                }
            }

            // Si no hay ninguna intentamos seguir en la misma direccion
            if(posibleDirections.contains(currentDirection)){
                return currentDirection;
            }

            // Si tampoco se puede volvemos en la direccion contraria
            return oppositeDirection;
        }
    }

    private int getIdleSpriteNumberByDirection(MovableBoardObjectModel.DIRECTION direction){
        switch(direction){
            case UP: return 0;
            case RIGHT: return 2;
            case DOWN: return 4;
            case LEFT: return 6;
            default: return 4;
        }
    }
}
