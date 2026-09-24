package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.PlatformServices;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.MainEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

/**
 * Created by JordiRM on 25/01/2017.
 */
public class MainUI extends BaseUI implements MainConfigListener, MainUiGameRetrievalListener, GUIModalPanel.ModalListener {
    private final float TIME_FOR_POKE_NETWORK = 3f;
    private final boolean debugBuild;
    private MainEventsManager mainEventsManager;
    private final SavedGamesManager savedGamesManager;
    private AchievementManager achievementManager;
    private IGameServiceClient playServices;
    private Skin skin;
    private TextureAtlas mainAtlas;
    private Image titleImage;
    private GUIobjectMainOption playOption, tutorialOption;
    private GUIobjectAboutPanel aboutPanel;
    private GUIobjectTutorialsMenuPanel tutorialsPanel;
    private GUIobjectMainConfigPanel configPanel;
    private GUIModalPanel modalPanel = null;
    private Label.LabelStyle labelStyle_base_gb_16, labelStyle_base_gb_13, labelStyle_base_gb_11;
    private boolean showingAbout = false;
    private boolean showingTutorials = false;
    private boolean interactionActive = true;
    private Group[] guiLayer;
    private ImageButton buttonAccount;
    private Label accountLabel, accountDisconectLabel;
    private ACCOUNT_STATUS loginButtonStatus;
    private boolean networkConnected, loginButtonNetworkConnected;
    private float timeForPokeNetwork = 0;
    private MODAL_KIND modalKind = MODAL_KIND.LOAD_GAME;
    private PlatformServices platformServices;

    public MainUI(Viewport viewport, final MainEventsManager newMainEventsManager, SavedGamesManager savedGamesManager,
                  AchievementManager achievementManager, IGameServiceClient playServices, boolean debugBuild, PlatformServices platformServices) {
        super(viewport);
        GUIstage = new Stage();
        GUIstage.setViewport(viewport);
        this.mainEventsManager = newMainEventsManager;
        this.savedGamesManager = savedGamesManager;
        this.savedGamesManager.setSavedGamesListener(this);
        this.achievementManager = achievementManager;
        this.playServices = playServices;
        this.debugBuild = debugBuild;
        this.platformServices = platformServices;
        skin = new Skin(Gdx.files.internal("skins/testSkin/uiskin.json"));
        mainAtlas = Assets.getTextureAtlas("main_title");

        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_13 = new Label.LabelStyle();
        labelStyle_base_gb_13.font = Assets.getFont("f_base_gb_13");
        labelStyle_base_gb_11 = new Label.LabelStyle();
        labelStyle_base_gb_11.font = Assets.getFont("f_base_gb_11");

        // Grupos generales para controlar los depths entre elementos de gui
        guiLayer = new Group[2];
        guiLayer[0] = new Group();  // Elementos de fondo
        guiLayer[1] = new Group();  // Elementos sobre el fondo
        GUIstage.addActor(guiLayer[0]);
        GUIstage.addActor(guiLayer[1]);

        // El fundido no necesita layer, es un actor único que se dibuja encima de todos
        addFadeSystem();

        showBackground();
        createTitle();
        showOptions();
        showAdditionalOptions();
        createConfigPanel();

        if(playServices.isSessionActive()) {
            loginButtonStatus = ACCOUNT_STATUS.CONNECTED;
            changeLoginButtonStatus(true);
            pushPendingAchievements();
            retrieveSavedGames();
        }
        else {
            loginButtonStatus = ACCOUNT_STATUS.DISCONNECTED;
            changeLoginButtonStatus(false);
            savedGamesManager.checkSavedGameVersion(false);
        }
    }

    public void startDeployingElements(){
        screenFadeIn();
        if(!GlobalGeneralData.getInstance().eventReviewModeActivated) {
            titleEnter();
            playOption.enter();
            tutorialOption.enter();
        } else {
            // Veiamos del menu de tutoriales así que lo sacamos directamente
            showTutorialsPanel();
        }
    }

    private void retrieveSavedGames() {
        this.savedGamesManager.retrieveSavedGame(GameConstants.CLOUD_GAME_ID);
    }

    private void pushPendingAchievements(){
        achievementManager.pushPendingAchievementsFromPref();
    }

    public void update(float delta){
        GUIstage.act(delta);
        GUIstage.draw();

        updateScreenFade(delta);

        // Selección de opciones
        if(interactionActive) {
            if (playOption.isSelected()) {
                GlobalGeneralData.getInstance().setSelectionScreenEnteringPanel(0); // Para que muestre le panel de episodios
                AudioManager.getInstance().stopMusic(true);
                playOption.leave();
                tutorialOption.leave();
                configPanel.forceRetractPanel();
                mainEventsManager.addEventGoToSelection();
                interactionActive = false;
                screenFadeOut();
            }
            else if(tutorialOption.isSelected()){
                playOption.leave();
                tutorialOption.leave();
                titleLeave();
                configPanel.forceRetractPanel();
                tutorialOption.unSelect();
                //interactionActive = false;
                showTutorialsPanel();
            }

            if(showingAbout){
                if(aboutPanel != null){
                    if(!aboutPanel.isPanelGone()) {
                        aboutPanel.update(delta);
                    } else {
                        showingAbout = false;
                        titleEnter();
                        playOption.enter();
                        tutorialOption.enter();
                    }
                }
            }

            if(showingTutorials){
                if(tutorialsPanel != null){
                    if(!tutorialsPanel.isPanelGone()) {
                        tutorialsPanel.update(delta);
                        if(tutorialsPanel.tutorialSelected) {
                            interactionActive = false;
                            if(tutorialsPanel.tutorialZone == GUIobjectTutorialsMenuPanel.TUTORIAL_ZONE.GAME)
                                mainEventsManager.addEventGoToGame();
                            else
                                mainEventsManager.addEventGoToSelection();
                            AudioManager.getInstance().stopMusic(true);
                            screenFadeOut();
                        }
                    } else {
                        showingTutorials = false;
                        titleEnter();
                        playOption.enter();
                        tutorialOption.enter();
                    }
                }
            }
            backAction();
        }

        // Vamos comprobando si hay conexión disponible cada cierto tiempo
        if(timeForPokeNetwork > 0)
            timeForPokeNetwork -= (1 * delta);
        else {
            networkConnected = platformServices.pokeForNetworkStatus();
            timeForPokeNetwork = TIME_FOR_POKE_NETWORK;
        }

        // Controlamos la conexion para cambiar el estado del boton y pedir las partidas
        if(playServices.isSessionActive()) {
            if(loginButtonStatus == ACCOUNT_STATUS.DISCONNECTED){
                changeLoginButtonStatus(true);
                pushPendingAchievements();
                retrieveSavedGames();
                loginButtonStatus = ACCOUNT_STATUS.CONNECTED;
                loginButtonNetworkConnected = networkConnected;
            }
        } else {
            if(loginButtonStatus == ACCOUNT_STATUS.CONNECTED) {
                changeLoginButtonStatus(false);
                loginButtonStatus = ACCOUNT_STATUS.DISCONNECTED;
                loginButtonNetworkConnected = networkConnected;
            }
        }

        if(networkConnected != loginButtonNetworkConnected){
            loginButtonNetworkConnected = networkConnected;
            changeLoginButtonStatus(loginButtonStatus == ACCOUNT_STATUS.CONNECTED);
        }

        // Para mostrar los mensajes del manager de partidas
        if(!savedGamesManager.getMainFloatingMessage().equals("")){
            showFloatingInfo(savedGamesManager.getMainFloatingMessage());
            savedGamesManager.deleteMainFloatingMessage();
        }
    }

    private void showBackground(){
        Image backgroundImage = new Image(mainAtlas.findRegion("background"));
        guiLayer[0].addActor(backgroundImage);
    }

    private void createTitle(){
        titleImage = new Image(mainAtlas.findRegion("title"));
        titleImage.setOrigin(Align.center);
        titleImage.setVisible(false);
        guiLayer[0].addActor(titleImage);
    }

    private void showOptions(){
        playOption = new GUIobjectMainOption(guiLayer[1], -20, 100, GUIobjectMainOption.OPTION.PLAY);
        tutorialOption = new GUIobjectMainOption(guiLayer[1], -50, 25, GUIobjectMainOption.OPTION.TUTORIAL);
    }

    private void showAdditionalOptions(){
        //Boton Configuracion
        Drawable buttonUp = new Image(mainAtlas.findRegion("button_config", 1)).getDrawable();
        Drawable buttonDown = new Image(mainAtlas.findRegion("button_config", 2)).getDrawable();
        ImageButton buttonConfig = new ImageButton(buttonUp, buttonDown);
        buttonConfig.setPosition(10, 315);
        buttonConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                configPanel.activePanel();
            }
        });
        guiLayer[1].addActor(buttonConfig);

        //Boton Logros
        buttonUp = new Image(mainAtlas.findRegion("button_achievements", 1)).getDrawable();
        buttonDown = new Image(mainAtlas.findRegion("button_achievements", 2)).getDrawable();
        ImageButton buttonAchievements = new ImageButton(buttonUp, buttonDown);
        buttonAchievements.setPosition(10, 240);
        buttonAchievements.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                mainEventsManager.addEventDisplayAchievements();
            }
        });
        guiLayer[1].addActor(buttonAchievements);

        //Boton Creditos
        buttonUp = new Image(mainAtlas.findRegion("mnc_miniLogo_button", 1)).getDrawable();
        buttonDown = new Image(mainAtlas.findRegion("mnc_miniLogo_button", 2)).getDrawable();
        final ImageButton buttonCredits = new ImageButton(buttonUp, buttonDown);
        buttonCredits.setPosition(GameConstants.HORIZONTAL_RESOLUTION - 60, GameConstants.VERTICAL_RESOLUTION - 60); // 315
        buttonCredits.setTransform(true);
        buttonCredits.setOrigin(Align.center);
        buttonCredits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                if(playOption.isDeployed() && tutorialOption.isDeployed()) {
                    if(buttonCredits.hasActions())
                        buttonCredits.removeAction(buttonCredits.getActions().get(0));
                    showAboutPanel();
                    playOption.leave();
                    tutorialOption.leave();
                    titleLeave();
                }
            }
        });

        Label mncLabel = new Label("MNC", labelStyle_base_gb_16);
        mncLabel.setColor(new Color(86/255f, 178/255f, 255/255f, 0.9f));
        mncLabel.setPosition(buttonCredits.getX() + (buttonCredits.getWidth() / 2) - (mncLabel.getWidth() / 2), buttonCredits.getY() - 30);
        guiLayer[1].addActor(buttonCredits);
        guiLayer[1].addActor(mncLabel);
        addHeartbeatAnimationToActor(buttonCredits);

        //Boton Cuenta
        buttonUp = new Image(mainAtlas.findRegion("button_account_off", 1)).getDrawable();
        buttonDown = new Image(mainAtlas.findRegion("button_account_off", 2)).getDrawable();
        buttonAccount = new ImageButton(buttonUp, buttonDown);
        buttonAccount.setPosition(GameConstants.HORIZONTAL_RESOLUTION - 65, 5);
        buttonAccount.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!playServices.isSessionActive()) {
                    if (!playServices.isConnectionPending()) {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        playServices.logIn();
                    }
                } else {
                    if(playServices.isFeatureSupported(IGameServiceClient.GameServiceFeature.PlayerLogOut)) {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        playServices.logOff();
                    }
                }
            }
        });

        //Test Conexion
        /*
        buttonUp = new Image(mainAtlas.findRegion("button_config", 1)).getDrawable();
        buttonDown = new Image(mainAtlas.findRegion("button_config", 2)).getDrawable();
        ImageButton buttonNetworkTest = new ImageButton(buttonUp, buttonDown);
        buttonNetworkTest.setPosition(10, 10);
        buttonNetworkTest.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                if(platformServices.pokeForNetworkStatus()) Gdx.app.log("NetWork", "CONNECTED");
                else Gdx.app.log("NetWork", "DISCONNECTED");
            }
        });
        guiLayer[1].addActor(buttonNetworkTest);
        */


        accountLabel = new Label("", labelStyle_base_gb_13);
        accountLabel.setPosition(buttonAccount.getX() - 10 - accountLabel.getWidth() , buttonAccount.getY() + 20);
        accountDisconectLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("account_logout"), labelStyle_base_gb_11);
        accountDisconectLabel.setColor(Color.LIGHT_GRAY.r, Color.LIGHT_GRAY.g , Color.LIGHT_GRAY.b, 0.9f);
        accountDisconectLabel.setPosition(buttonAccount.getX() - 10 - accountDisconectLabel.getWidth() , buttonAccount.getY() + 5);
        accountDisconectLabel.setVisible(false);

        guiLayer[1].addActor(buttonAccount);
        guiLayer[1].addActor(accountLabel);
        guiLayer[1].addActor(accountDisconectLabel);
    }

    private void showAboutPanel(){
        showingAbout = true;
        aboutPanel = new GUIobjectAboutPanel(guiLayer[1]);
    }

    private void showTutorialsPanel(){
        showingTutorials = true;
        tutorialsPanel = new GUIobjectTutorialsMenuPanel(guiLayer[1]);
    }

    private void createConfigPanel(){
        configPanel = new GUIobjectMainConfigPanel(guiLayer[1], this);
    }

    private void titleEnter(){
        titleImage.setVisible(true);
        titleImage.setPosition((float)(GameConstants.HORIZONTAL_RESOLUTION / 2) - (titleImage.getWidth() / 2), GameConstants.VERTICAL_RESOLUTION - 108);
        titleImage.setColor(1, 1, 1, 0);

        float finalPosY = GameConstants.VERTICAL_RESOLUTION - 178;
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(titleImage.getX(), finalPosY);
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(new Interpolation.SwingOut(1.5f));
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.2f);

        SequenceAction generalSequence = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction(moveToAction, alphaAction);
        generalSequence.addAction(new DelayAction(0.25f));
        generalSequence.addAction(parallelAction);

        titleImage.addAction(generalSequence);
    }

    private void titleLeave(){
        float finalPosY = GameConstants.VERTICAL_RESOLUTION - 80;// - 108
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(titleImage.getX(), finalPosY);
        moveToAction.setDuration(0.6f);
        moveToAction.setInterpolation(new Interpolation.SwingOut(1.5f));
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        SequenceAction sequenceAction = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction(moveToAction, alphaAction);
        sequenceAction.addAction(new DelayAction(0.15f));
        sequenceAction.addAction(parallelAction);
        titleImage.addAction(sequenceAction);
    }

    private void changeLoginButtonStatus(boolean isConnected){
        Drawable state_buttonUp,state_buttonDown;
        if(isConnected){
            if(networkConnected) {
                state_buttonUp = new Image(mainAtlas.findRegion("button_account_on", 1)).getDrawable();
                state_buttonDown = new Image(mainAtlas.findRegion("button_account_on", 2)).getDrawable();
            } else {
                state_buttonUp = new Image(mainAtlas.findRegion("button_account_on_offline", 1)).getDrawable();
                state_buttonDown = new Image(mainAtlas.findRegion("button_account_on_offline", 2)).getDrawable();
            }
            buttonAccount.setStyle(new ImageButton.ImageButtonStyle(state_buttonUp, state_buttonDown, state_buttonUp, state_buttonUp, state_buttonDown, state_buttonUp));
            String accountName = playServices.getPlayerDisplayName();
            if(accountName != null)
                accountLabel.setText(accountName);
            else {
                accountLabel.setText("Unnamed");
            }
            accountLabel.pack();
            accountLabel.setPosition(buttonAccount.getX() - 10 - accountLabel.getWidth() , buttonAccount.getY() + 20);
            accountDisconectLabel.setVisible(true);
        } else {
            state_buttonUp = new Image(mainAtlas.findRegion("button_account_off", 1)).getDrawable();
            state_buttonDown = new Image(mainAtlas.findRegion("button_account_off", 2)).getDrawable();
            buttonAccount.setStyle(new ImageButton.ImageButtonStyle(state_buttonUp, state_buttonDown, state_buttonUp, state_buttonUp, state_buttonDown, state_buttonUp));
            accountLabel.setText(GlobalGeneralData.getInstance().getGlobalBundleData().get("account_login"));
            accountLabel.pack();
            accountLabel.setPosition(buttonAccount.getX() - 10 - accountLabel.getWidth() , buttonAccount.getY() + 12);
            accountDisconectLabel.setVisible(false);
        }
    }

    private void showFloatingInfo(String text){
        Label textLabel = new Label(text, labelStyle_base_gb_13);
        textLabel.setWrap(true);
        textLabel.setWidth(450);
        textLabel.setColor(253 / 255f, 232 / 255f, 127 / 255f, 0);
        textLabel.setPosition(10, 20 - (textLabel.getHeight() / 2));

        AlphaAction alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(0.8f);
        alphaAction1.setDuration(0.4f);

        AlphaAction alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.8f);
        SequenceAction sequenceAction = new SequenceAction(new DelayAction(0.4f), alphaAction1, new DelayAction(4),
                alphaAction2, new RemoveActorAction());
        textLabel.addAction(sequenceAction);

        guiLayer[1].addActor(textLabel);
    }

    private void addHeartbeatAnimationToActor(final Actor button){
        SequenceAction sequenceAction = new SequenceAction();
        ScaleToAction scaleToActionIn = new ScaleToAction();
        scaleToActionIn.setScale(1.2f, 1.2f);
        scaleToActionIn.setDuration(0.15f);
        ScaleToAction scaleToActionOut = new ScaleToAction();
        scaleToActionOut.setScale(1, 1);
        scaleToActionOut.setDuration(0.25f);

        sequenceAction.addAction(new DelayAction(2.5f));
        sequenceAction.addAction(scaleToActionIn);
        sequenceAction.addAction(scaleToActionOut);
        sequenceAction.addAction(new DelayAction(2.5f));

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceAction);

        button.addAction(repeatAction);
    }

    private void backAction() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (playOption.isDeployed() && tutorialOption.isDeployed()){
                dispose();
                Gdx.app.exit();
                System.exit(0);
            }
        }
    }

    public Stage getUIStage(){
        return GUIstage;
    }

    public void dispose(){
        GUIstage.dispose();
        skin.dispose();
    }

    @Override
    public void deleteGamePressed() {
        String title = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_deleteCloudSave_title");
        String body = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_deleteCloudSave_body");
        modalPanel = new GUIModalPanel(guiLayer[1], this, title, body);
        modalKind = MODAL_KIND.DELETE_GAME;
    }

    @Override
    public boolean isSessionActive() {
        return savedGamesManager.isSessionActive();
    }

    @Override
    public boolean isSavedGameSupported() {
        return savedGamesManager.isSavedGameSupported();
    }

    @Override
    public boolean isDebugBuild() {
        return debugBuild;
    }

    @Override
    public boolean savedGameExists() {
        return savedGamesManager.savedGameExists();
    }

    @Override
    public void gameRetrieved(boolean success) {
        if(success && savedGamesManager.isSavedRemoteGameEnabled()) {
            savedGamesManager.loadGameState();
        } else {
            savedGamesManager.checkSavedGameVersion(false);
        }
    }

    @Override
    public void savedGameOptionChanged(boolean enabled) {
        if(modalPanel == null && enabled) retrieveSavedGames();
    }

    @Override
    public void crashTestPressed() {
        mainEventsManager.makeGameCrash();
    }

    @Override
    public void gameLoadEnded(boolean success, boolean moreAdvancedSavedGame, long remoteGameTimeStamp, String deviceID) {
        if(success && modalPanel == null) {
            if(remoteGameTimeStamp > GlobalPreferencesData.getInstance().getSavedGameTime()) {
                String title = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_loadCloudSave_title");
                String body;
                if(!deviceID.equals(GlobalPreferencesData.getInstance().getDeviceId())) {
                    body = GlobalGeneralData.getInstance().getGlobalBundleData().format("info_loadCloudSave_body_2", deviceID);
                } else {
                    body = GlobalGeneralData.getInstance().getGlobalBundleData().format("info_loadCloudSave_body_1");
                }
                modalPanel = new GUIModalPanel(guiLayer[1], this, title, body);
                modalKind = MODAL_KIND.LOAD_GAME;
            } else {
                if(moreAdvancedSavedGame) {
                    String title = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_loadCloudSave_title");
                    String body;
                    if(!deviceID.equals(GlobalPreferencesData.getInstance().getDeviceId())) {
                        body = GlobalGeneralData.getInstance().getGlobalBundleData().format("info_loadCloudSave_body_2", deviceID);
                    } else {
                        body = GlobalGeneralData.getInstance().getGlobalBundleData().format("info_loadCloudSave_body_1");
                    }
                    modalPanel = new GUIModalPanel(guiLayer[1], this, title, body);
                    modalKind = MODAL_KIND.LOAD_GAME;
                } else {
                    if(!deviceID.equals(GlobalPreferencesData.getInstance().getDeviceId())) {
                        String title = GlobalGeneralData.getInstance().getGlobalBundleData().get("info_loadCloudSave_title");
                        String body = GlobalGeneralData.getInstance().getGlobalBundleData().format("info_loadCloudSave_body_2", deviceID);
                        modalPanel = new GUIModalPanel(guiLayer[1], this, title, body);
                        modalKind = MODAL_KIND.LOAD_GAME;
                    } else {
                        Gdx.app.log("MainScreen", "No need to do nothing");
                    }
                }
                Gdx.app.log("MainScreen", "Remote saved Game retrieved is in the same point.");
            }
        }
    }

    @Override
    public void gameApplied(boolean success) {
        if(success) {
            Gdx.app.log("MainScreen", "Game applied succesfully");
            savedGamesManager.pushSavedGame(GameConstants.CLOUD_GAME_ID);
        }
    }

    @Override
    public void gameSaved(boolean success) {
        if(success) Gdx.app.log("MainUi", "Game Saved");
    }

    @Override
    public void gameDeleted(boolean success) {
        if(success) Gdx.app.log("MainUi", "Game Deleted");
    }

    @Override
    public void yesSelected() {
        if(modalKind == MODAL_KIND.LOAD_GAME) {
            Gdx.app.log("MainUi", "User decided to load game");
            //savedGamesManager.replaceGameState();
            savedGamesManager.mergeGameState();
        } else if(modalKind == MODAL_KIND.DELETE_GAME){
            Gdx.app.log("MainUi", "User decided to delete game");
            savedGamesManager.deleteSavedGame(GameConstants.CLOUD_GAME_ID);
        }
        modalPanel = null;
    }

    @Override
    public void noSelected() {
        if(modalKind == MODAL_KIND.LOAD_GAME) {
            Gdx.app.log("MainUi", "User decided not to load game");
            GlobalPreferencesData.getInstance().setCloudSaveEnabled(false);
            configPanel.actualizeButtons();
            showFloatingInfo(GlobalGeneralData.getInstance().getGlobalBundleData().get("info_cloudSave_deactivated"));
        } else if(modalKind == MODAL_KIND.DELETE_GAME){
            Gdx.app.log("MainUi", "User decided not to delete game");
        }
        modalPanel = null;
    }

    enum ACCOUNT_STATUS{
        IDLE, CONNECTING, DISCONNECTING, CONNECTED, DISCONNECTED
    }

    enum MODAL_KIND {
        LOAD_GAME, DELETE_GAME
    }
}
