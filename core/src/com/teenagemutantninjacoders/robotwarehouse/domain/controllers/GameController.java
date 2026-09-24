package com.teenagemutantninjacoders.robotwarehouse.domain.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.TimeUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.LevelModelProviderImpl;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.LevelModelProvider;
import com.teenagemutantninjacoders.robotwarehouse.display.painters.GamePainter;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectGamePanels;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GameUI;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.MainUiGameRetrievalListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.events.UserBoardObjectEvent;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AchievementManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.BoxManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.BoardObjectMovementManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.BoardObjectTerminatorManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.CannonManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EnemyBehaviourManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.FloorEventsManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.GameEventsManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.OverBaseManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.PowerManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.SavedGamesManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.SpaceDockManagerImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoardObjectMovementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoardObjectTerminatorManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.CannonManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.EnemyBehaviourManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.FloorEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.PowerManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SpaceDockManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.RobotElevatorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;

import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Created by JordiRM on 04/02/2016.
 */
public class GameController implements Screen {

    private static final String GAME_SCREEN = "game_screen";
    private final LevelModelProvider levelModelProvider;
    private RobotWarehouseGame game;
    private LevelModel levelModel;
    private GamePainter gamePainter;
    private GameInputProcessor gameInputProcessor;
    private boolean touchDown = false;
    private boolean touchDragged = false;
    private float doubleTapTime = .0f;
    private CellDTO lastCellTouched;
    private UserBoardObjectEvent userBoardObjectEvent;
    private boolean acceptInput = true;
    private ArrayList<GameObject> objectsToDelete = new ArrayList<GameObject>();

    private BoardObjectMovementManager boardObjectMovementManager;
    private BoxManager boxManager;
    private BoardObjectTerminatorManager boardObjectTerminatorManager;
    private FloorEventsManager floorEventsManager;
    private CannonManager cannonManager;
    private EnemyBehaviourManager enemyBehaviourManager;
    private GameEventsManagerImpl gameEventsManager;
    private PowerManager powerManager;
    private OverBaseManagerImpl overBaseManager;
    private SpaceDockManager spaceDockManager;
    private GameUI gameUI;
    private boolean active = true;

    public GameController(RobotWarehouseGame game) {
        this(game, new LevelModelProviderImpl());
    }

    public GameController(RobotWarehouseGame game, LevelModelProvider levelModelProvider) {
        this.game = game;

        gamePainter = new GamePainter();
        userBoardObjectEvent = new UserBoardObjectEvent();
        this.levelModelProvider = levelModelProvider;
        this.game.trackingServices.trackScreen(GAME_SCREEN);
        initializeLevel();
    }

    public void initializeLevel() {
        Long iniTime = TimeUtils.nanoTime();

        // Si ya habiamos cargado algun nivel hacemos un dispose antes.
        if (levelModel != null) disposeGameObjectsAndPainterAndUI(false);

        // El episode 0 se usa para saber que aun no se ha jugado ninguno, así que en los test del editor tenemos que forzarlo a uno que exista.
        if(GlobalGeneralData.getInstance().getCurrentEpisode() == 0)
            GlobalGeneralData.getInstance().setCurrentEpisode(1);

        this.levelModel = levelModelProvider.getModelForLevel(GlobalGeneralData.getInstance().getCurrentEpisode(),
                GlobalGeneralData.getInstance().getCurrentLevel());
        levelModel.initializeLevel();

        initializeGameUI();
        initializeGameInputProcessor();
        initializeGlobalGameDataForLevel();
        createManagers(levelModel);
        ChallengeManager.getInstance().initialize(gameUI);
        gameUI.initialize();
        gamePainter.initializePainter();
        updateViewPort();
        lastCellTouched = new CellDTO(-1, -1);
        active = true;

        // Solo iniciamos el nivel cuando la carga de procesos ha terminado para evitar tirones al principio
        float safePause = (float)(( TimeUtils.nanoTime() - iniTime) / 1000000000.0);
        Gdx.app.log("Level Initialization Time", "" + safePause);
        gameEventsManager.beginLevelEnter(safePause + 0.4f);

    }

    private void initializeGameUI() {
        gameUI = new GameUI(this, gamePainter.getViewport());
    }

    private void initializeGameInputProcessor() {
        gameInputProcessor = new GameInputProcessor();
        gameInputProcessor.setGameControllerInputListener(this);

        InputMultiplexer gameInputMultiplexer = new InputMultiplexer();
        gameInputMultiplexer.addProcessor(gameUI.getUIStage()); // Primera prioridad
        gameInputMultiplexer.addProcessor(gameInputProcessor);  // Segunda prioridad

        Gdx.input.setInputProcessor(gameInputMultiplexer);
    }

    private void initializeGlobalGameDataForLevel() {
        GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PAUSED);
    }

    private void createManagers(LevelModel levelModel) {
        boxManager = new BoxManagerImpl(levelModel);
        spaceDockManager = new SpaceDockManagerImpl(levelModel);
        SavedGamesManager savedGamesManager = new SavedGamesManagerImpl(game.playServices);
        AchievementManager achievementManager = new AchievementManagerImpl(game.playServices, game.trackingServices, game.idProvider);
        savedGamesManager.setSavedGamesListener(new MainUiGameRetrievalListener() {
            @Override
            public void gameRetrieved(boolean success) {

            }

            @Override
            public void gameLoadEnded(boolean success, boolean moreAdvancedSavedGame, long remoteGameTimeStamp, String deviceID) {

            }

            @Override
            public void gameApplied(boolean success) {

            }

            @Override
            public void gameSaved(boolean success) {
                if(success) {
                    Gdx.app.log("GameController","Game Saved succesfully");
                } else {
                    Gdx.app.log("GameController","Game cannot be Saved");
                }
            }

            @Override
            public void gameDeleted(boolean success) {

            }
        });
        gameEventsManager = new GameEventsManagerImpl(this, gameUI, gamePainter, this.game.playServices, this.game.trackingServices, this.game.adServices, this.game.platformServices, savedGamesManager, achievementManager);
        boardObjectMovementManager = new BoardObjectMovementManagerImpl(levelModel, boxManager.getBoxListener(), achievementManager);
        boardObjectTerminatorManager = new BoardObjectTerminatorManagerImpl(levelModel, gameEventsManager,  boxManager.getBoxListener());
        floorEventsManager = new FloorEventsManagerImpl(boxManager.getBoxListener(), levelModel.getCurrentFloorBarrierModels());
        cannonManager = new CannonManagerImpl(levelModel, boxManager.getBoxListener());
        enemyBehaviourManager = new EnemyBehaviourManagerImpl(levelModel, levelModel.getCurrentBoardModel(), boxManager.getBoxListener());
        overBaseManager = new OverBaseManagerImpl(levelModel, gameEventsManager);
        powerManager = new PowerManagerImpl(levelModel);

        EffectManager.getInstance().initializeManager(levelModel);
    }

    public void updateViewPort() {
        this.gameInputProcessor.setCurrentViewPort(gamePainter.getViewport());
    }

    private I18NBundle getLevelLabelBundle(int levelNumber) {
        FileHandle bundleFileHandle;
        I18NBundle bundle;
        try {
            bundleFileHandle = Gdx.files.internal("i18n/levelbundle"+Integer.toString(levelNumber));
            bundle = I18NBundle.createBundle(bundleFileHandle);
        } catch (MissingResourceException exception) {
            bundleFileHandle = Gdx.files.internal("i18n/levelbundle");
            bundle = I18NBundle.createBundle(bundleFileHandle);
        }
        return bundle;
    }

    // Loop principal
    public void render(float delta) {
        gameEventsManager.update(delta);
        AudioManager.getInstance().update(delta);
        ChallengeManager.getInstance().update();
        EffectManager.getInstance().addPendingEffectsToLevel();
        enemyBehaviourManager.update(delta);

        spaceDockManager.update(delta);
        boxManager.update(delta);
        overBaseManager.update(delta);
        powerManager.update(delta);

        updateGameObjects(delta);

        acceptInput = false;
        if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
            // Solo tendremos control en la zona de juego si no hay ningun evento de nivel activo
            if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                acceptInput = true;
                readInput(delta);
                processInput(delta);
            }
        }
        debugControls();
        backAction();
        if (active) {
            gameUI.update(delta);
            paint(delta);
        }
    }



    private void updateGameObjects(float delta) {
        int i = 0;
        ArrayList<GameObject> gameObjects = levelModel.getCurrentGameObjects();
        GameObject currentGameObject;
        BoardObjectModel currentBoardObject;

        while (i < gameObjects.size()) {
            currentGameObject = gameObjects.get(i);

            // BOARDOBJECTS
            if (currentGameObject instanceof BoardObjectModel) {
                currentBoardObject = (BoardObjectModel) currentGameObject;
                if (currentBoardObject.isActiveObject() && currentBoardObject.canActualizeObject()) {

                    // Movement
                    if (currentBoardObject instanceof MovableBoardObjectModel) {
                        if (currentBoardObject.getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.MOVING) {
                            boardObjectMovementManager.updateObject((MovableBoardObjectModel) currentBoardObject, delta);
                        }
                    }
                    // Floors
                    if (currentBoardObject instanceof FloorModel) {
                        // Robot Elevator
                        if (currentGameObject instanceof RobotElevatorModel) {
                            if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                                enemyBehaviourManager.updateRobotElevator((RobotElevatorModel) currentBoardObject, levelModel, delta);
                            }
                        } else {
                            floorEventsManager.updateBoardObject(currentBoardObject, levelModel.getCurrentBoardModel(), delta);
                        }
                    }
                    // OverBases
                    else if (currentGameObject instanceof OverBaseBoxActivableModel) {
                        overBaseManager.updateOverBase((OverBaseBoxActivableModel) currentBoardObject, delta);
                    }
                    // Boxes
                    else if (currentBoardObject instanceof BoxModel) {
                        boxManager.updateBox((BoxModel) currentBoardObject, delta);
                    }
                    // Enemies
                    else if (currentGameObject instanceof EnemyModel) {
                        if (GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.NONE) {
                            enemyBehaviourManager.updateEnemy((EnemyModel) currentGameObject, delta);
                        }
                    }
                    // Cannon
                    else if (currentBoardObject instanceof CannonModel) {
                        cannonManager.updateCannon((CannonModel) currentBoardObject, delta);
                    }
                }

                // Eliminación de objetos
                if (currentBoardObject.getStatus() != BoardObjectModel.BOARD_OBJECT_STATUS.ERASED) {
                    boardObjectTerminatorManager.updateObject(currentBoardObject, delta);
                } else {
                    // Añadimos el objeto para ser borrado al finalizar la revision de la lista
                    objectsToDelete.add(currentBoardObject);
                }

            } else {

                // GAMEOBJECTS
                if (currentGameObject.isActiveObject() && currentGameObject.canActualizeObject()) {
                    if (currentGameObject instanceof SpriteEffectModel){
                        if(((SpriteEffectModel)currentGameObject).isFinished()){
                            levelModel.deleteGameObject(currentGameObject);
                        } else {
                            ((SpriteEffectModel)currentGameObject).update(delta);
                        }
                    }
                    else if (currentGameObject instanceof ParticleEffectModel) {
                        ParticleEffect effect = ((ParticleEffectModel) currentGameObject).getParticleEffect();
                        if (effect.isComplete()) {
                            effect.dispose();
                            levelModel.deleteGameObject(currentGameObject);
                        }
                    }
                }
            }

            i++;
        }

        // Borrado de objetos despues de procesar toda la lista
        while (objectsToDelete.size() > 0) {
            currentGameObject = objectsToDelete.get(0);
            levelModel.deleteGameObject(currentGameObject);
            objectsToDelete.remove(0);
        }
    }

    private void readInput(float delta) {
        if (doubleTapTime < 1) {
            doubleTapTime += 1 * delta;
        }
        if (touchDown) {

            //Testing touch position
            Vector2 pos = gameInputProcessor.getLastTouchDownRealPosition();
            if (GlobalGeneralData.getInstance().debug_drawTouchActivity) {
                gamePainter.touchDownTest(pos.x, GameConstants.VERTICAL_RESOLUTION - pos.y);
            }

            int column = gameInputProcessor.getLastTouchDownColumn();
            int row = gameInputProcessor.getLastTouchDownRow();
            if (column > -1 && column < GameConstants.BOARD_COLUMNS && row > -1 && row < GameConstants.BOARD_ROWS) {
                userBoardObjectEvent.clearEvents();
                boolean validBoardObject = false;
                BoardObjectModel boardObjectModel = levelModel.getCurrentBoardModel().getBoardObjectAbove(column, row);
                if (boardObjectModel != null) {
                    if (boardObjectModel.isTouchable()) validBoardObject = true;
                }
                if (validBoardObject) {
                    userBoardObjectEvent.setBoardObjectModel(boardObjectModel);
                    // Doble Tap
                    if (doubleTapTime < 0.30f) {// && (lastCellTouched.getColumn() == column && lastCellTouched.getRow() == row)) {
                        userBoardObjectEvent.setTeleportRequest();
                    }
                } else {
                    // Predicción para cajas cercanas
                    Vector2 touchPosition = gameInputProcessor.getLastTouchDownPosition();
                    touchPosition.y = GameConstants.VERTICAL_RESOLUTION - touchPosition.y;
                    Vector2 cellPosition = new Vector2();
                    cellPosition.x = (column * GameConstants.CELL_WIDTH) + GameConstants.BOARD_ORIGIN_X;
                    cellPosition.y = ((GameConstants.BOARD_ORIGIN_Y) - ((row + 1) * (GameConstants.CELL_HEIGHT)));
                    int cellSize = GameConstants.CELL_WIDTH;
                    float leftDist = cellSize;
                    float rightDist = cellSize;
                    float upDist = cellSize;
                    float downDist = cellSize;

                    if (touchPosition.x < cellPosition.x + 10) {
                        if (column - 1 >= 0) leftDist = touchPosition.x - cellPosition.x;
                    } else if (touchPosition.x > cellPosition.x + (cellSize - 10)) {
                        if (column + 1 < GameConstants.BOARD_COLUMNS)
                            rightDist = (cellPosition.x + cellSize) - touchPosition.x;
                    }
                    if (touchPosition.y < cellPosition.y + 10) {
                        if (row + 1 < GameConstants.BOARD_ROWS)
                            upDist = touchPosition.y - cellPosition.y;
                    } else if (touchPosition.y > cellPosition.y + (cellSize - 10)) {
                        if (row - 1 >= 0) downDist = (cellPosition.y + cellSize) - touchPosition.y;
                    }

                    float horizontal = Math.min(leftDist, rightDist);
                    float vertical = Math.min(upDist, downDist);
                    if (horizontal < 10 || vertical < 10) {
                        Vector2 cellDesp = new Vector2(0, 0);
                        if (horizontal == vertical) {
                            if (MathUtils.random(0, 1) == 1) horizontal = cellSize;
                            else vertical = cellSize;
                        }

                        if (horizontal < vertical) {
                            if (leftDist < rightDist) cellDesp.x = -1;
                            else cellDesp.x = 1;
                        } else if (vertical < horizontal) {
                            if (upDist < downDist) cellDesp.y = 1;
                            else cellDesp.y = -1;
                        }

                        boardObjectModel = levelModel.getCurrentBoardModel().getBoardObjectAbove(column + (int) cellDesp.x, row + (int) cellDesp.y);
                        if (boardObjectModel != null) {
                            if (boardObjectModel.isTouchable()) validBoardObject = true;
                            else validBoardObject = false;
                        }
                        if (validBoardObject) {
                            userBoardObjectEvent.setBoardObjectModel(boardObjectModel);
                        }
                    }
                }
            }
            touchDown = false;
            doubleTapTime = .0f;
            lastCellTouched.setColumn(column);
            lastCellTouched.setRow(row);
        }
        if (touchDragged) {
            if (userBoardObjectEvent.getBoardObjectModel() != null) {
                int screenX = gameInputProcessor.getScreenDraggedX();
                int screenY = gameInputProcessor.getScreenDraggedY();

                if(!(screenX == 0 && screenY == 0)) {
                    // Obtenemos las posiciones en la resolución nativa para trabajar con ellas
                    Vector2 draggedPos = Utils.unprojectPosition(gamePainter.getViewport(), new Vector2(screenX, screenY));
                    Vector2 lastTouchDownPos = gameInputProcessor.getLastTouchDownRealPosition();

                    Vector2 dragDistance = new Vector2(Math.abs(draggedPos.x - lastTouchDownPos.x), Math.abs(draggedPos.y - lastTouchDownPos.y));
                    if (dragDistance.x > 20 || dragDistance.y > 20) {
                        if (dragDistance.x > dragDistance.y) {
                            if (draggedPos.x > lastTouchDownPos.x) {
                                userBoardObjectEvent.setMovementRequest(MovableBoardObjectModel.DIRECTION.RIGHT);
                            } else {
                                userBoardObjectEvent.setMovementRequest(MovableBoardObjectModel.DIRECTION.LEFT);
                            }
                        } else {
                            if (draggedPos.y < lastTouchDownPos.y) {
                                userBoardObjectEvent.setMovementRequest(MovableBoardObjectModel.DIRECTION.UP);
                            } else {
                                userBoardObjectEvent.setMovementRequest(MovableBoardObjectModel.DIRECTION.DOWN);
                            }
                        }
                        if (GlobalGeneralData.getInstance().debug_drawTouchActivity) {
                            if (draggedPos.x != 0)
                                gamePainter.touchdragTest(draggedPos.x, GameConstants.VERTICAL_RESOLUTION - draggedPos.y);
                        }
                    }
                }
            }
            touchDragged = false;
        }
    }

    private void processInput(float delta) {
        if (acceptInput) {
            if (userBoardObjectEvent.hasRequestedEvent()) {
                // BOX
                if (userBoardObjectEvent.getBoardObjectModel() instanceof BoxModel) {
                    if (userBoardObjectEvent.getBoardObjectModel().getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE) {
                        BoxModel boxModel = (BoxModel) userBoardObjectEvent.getBoardObjectModel();

                        if (userBoardObjectEvent.getRequestedEvent() == UserBoardObjectEvent.EVENT_TYPE.MOVEMENT) {
                            if (!boardObjectMovementManager.isDirectionObstructed(boxModel, userBoardObjectEvent.getDirection(), delta)) {
                                boxManager.getBoxListener().setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.MOVING, false);
                                boxModel.setNewMove(userBoardObjectEvent.getDirection());
                                GlobalLevelData.getInstance().addPlayerMove();
                            }
                        }

                        if (userBoardObjectEvent.getRequestedEvent() == UserBoardObjectEvent.EVENT_TYPE.TELEPORT) {
                            if (boxModel.getInGroup() && !boxModel.isTerminated()) {
                                // (!) El sistema actual no soporta varias cadenas diferentes de teletransportes, asi que solo dejamos activar una a la vez.
                                if (GlobalLevelData.getInstance().getBoxesTeleporting() == 0)
                                    boxManager.getBoxListener().setBoxBoardObjectEvent((BoxModel)userBoardObjectEvent.getBoardObjectModel(), BoardObjectModel.BOARD_OBJECT_EVENT.TELEPORTING, true);
                            }
                        }
                    }
                }
                userBoardObjectEvent.clearEvents();
            }
        } else if (userBoardObjectEvent.hasRequestedEvent()) {
            // Si hacemos alguna accion sobre un objeto mientras no tenemos control, la anulamos
            userBoardObjectEvent.clearEvents();
        }
    }

    private void debugControls(){
        // Controles para acciones de debugación
        // Pausa manual
        if( GlobalGeneralData.getInstance().debug_keys) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
                    GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PAUSED);
                    Gdx.app.log("GameController", "PAUSED");
                } else {
                    GlobalLevelData.getInstance().setLevelStatus(GlobalLevelData.LEVEL_STATUS.PLAYING);
                    Gdx.app.log("GameController", "ACTIVE");
                }
            }
            // Completar el nivel
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Gdx.app.log("GameController", "FORZANDO COMPLECION");
                if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
                    GlobalLevelData.getInstance().setRequestedBoxes(0);
                    GlobalLevelData.getInstance().setLevelTime(700);//5
                    gameEventsManager.addChangeInBoxesEvent();
                }
            }
        }
    }

    private void backAction() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
                if(gameUI.getScoresDeployStatus() == GUIobjectGamePanels.DEPLOY_STATUS.DEPLOYED) {
                    gameEventsManager.addEnterPauseMenu();
                }
            } else {
                if(gameUI.isPauseMenuReady()){
                    gameEventsManager.changeScreenEvent(RobotWarehouseGame.SCREEN_TYPE.SELECTION_SCREEN);
                }
            }
        }
    }

    @Override
    public void show() {

    }

    private void paint(float delta) {
        gamePainter.initFrame();
        gamePainter.initBatch();
        gamePainter.paintGameObjects(levelModel.getCurrentGameObjects(), delta);
        gamePainter.endBatch();
        // Los efectos de la zona de juego usan otro batch
        //gamePainter.paintGameZoneFX(delta);
        // Scene2d tiene su propio SpriteBatch
        gamePainter.paintUIElements(gameUI.getUIStage(), delta);
    }

    public RobotWarehouseGame getGame() {
        return game;
    }

    public GameEventsManager getEventsManager() {
        return gameEventsManager;
    }

    public SpaceDockManager getSpaceDockManager() {
        return spaceDockManager;
    }

    public PowerManager getPowerManager() {
        return powerManager;
    }

    public LevelModel getLevelModel() {
        return levelModel;
    }

    public void resize(int width, int height) {
        gamePainter.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    //@Override
    public void touchDown(boolean state) {
        if(state){
            if (acceptInput) touchDown = true;
        } else touchDown = false;
    }

    //@Override
    public void touchDragged(boolean state) {
        if(state){
            if (acceptInput) touchDragged = true;
        } else touchDragged = false;
    }

    public void disposeGameObjectsAndPainterAndUI(boolean screenDispose) {
        // Finalizamos todos los efectos de particulas activos.
        //Gdx.app.log("", "GameCointroller disposeGameObjectsAndPainterAndUI");
        ArrayList<GameObject> gameObjects = levelModel.getCurrentGameObjects();
        for (GameObject currentGameObject : gameObjects) {
            if (currentGameObject instanceof ParticleEffectModel) {
                ((ParticleEffectModel) currentGameObject).getParticleEffect().dispose();
                //Gdx.app.log("", "FX forced to disposeGameObjectsAndPainterAndUI");
            }
        }

        if (screenDispose) gamePainter.dispose();
        if (gameUI != null) {
            gameUI.dispose();
        }
        active = false;
    }
}
