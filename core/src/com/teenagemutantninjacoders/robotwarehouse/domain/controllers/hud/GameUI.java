package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.GameController;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 19/04/2016.
 */
public class GameUI extends BaseUI{
    private GameController gameController;
    private Skin skin;
    private GUIobjectGamePanels gamePanels;
    private ArrayList<GUIobject> GUIobjectsList = new ArrayList<GUIobject>();
    private ArrayList<GUIobject> GUIobjectsRemoveList = new ArrayList<GUIobject>();
    private GUIobjectCharacterDialogue characterDialogue;
    private Group rankStatsGroup;
    private GUIobjectPausePanels guIobjectPausePanels;

    private boolean ActiveAnnouncement = false;
    private ArrayList<GUIobjectAnnouncement> announcementsList = new ArrayList<GUIobjectAnnouncement>();

    private Group[] guiLayer;

    public GameUI(GameController gameController, Viewport viewport){
        super(viewport);
        this.gameController = gameController;
        skin = new Skin(Gdx.files.internal("skins/testSkin/uiskin.json"));

        // Grupos generales para controlar los depths entre elementos de gui
        guiLayer = new Group[6];
        guiLayer[0] = new Group();  // Elementos sobre el board
        guiLayer[1] = new Group();  // Carteles de puntos y demás
        guiLayer[2] = new Group();  // Carteles generales de nivel
        guiLayer[3] = new Group();  // Paneles y menus
        guiLayer[4] = new Group();  // Dialogos
        guiLayer[5] = new Group();  // Debug messages

        GUIstage.addActor(guiLayer[0]);
        GUIstage.addActor(guiLayer[1]);
        GUIstage.addActor(guiLayer[2]);
        GUIstage.addActor(guiLayer[3]);
        GUIstage.addActor(guiLayer[4]);
        GUIstage.addActor(guiLayer[5]);

        characterDialogue = new GUIobjectCharacterDialogue(guiLayer[4]);

        // El fundido no necesita layer, es un actor único que se dibuja encima de todos
        addFadeSystem();

        // Controles Debug
        //debug_button_Complete();
    }

    public void initialize(){
        // Creamos los paneles de juego y los añadimos a la lista para ir actualizandolos
        gamePanels = new GUIobjectGamePanels(guiLayer[3], gameController.getEventsManager());
        GUIobjectsList.add(gamePanels);
    }

    public void update(float delta){

        updateScreenFade(delta);

        // Elementos que necesitan actualización
        for(int i = 0; i<GUIobjectsList.size() ; i++){
            if(GUIobjectsList.get(i).getFinalized()){
                GUIobjectsRemoveList.add(GUIobjectsList.get(i));
            }
            else
                GUIobjectsList.get(i).update(delta);
        }
        // Elementos a borrar
        if(GUIobjectsRemoveList.size() >0) {
            for (int i = 0; i < GUIobjectsRemoveList.size(); i++) {
                GUIobjectsList.remove(GUIobjectsRemoveList.get(i));
            }
            GUIobjectsRemoveList.clear();
        }

        // Lanzamiento de anuncios en cola
        if(!ActiveAnnouncement){
            if(announcementsList.size() > 0){
                announcementsList.get(0).execute();
                ActiveAnnouncement = true;

            }
        }

        if( GlobalGeneralData.getInstance().debug_keys) {
            // Activar los stats de ranking
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                if (rankStatsGroup != null) {
                    rankStatsGroup.setVisible(!rankStatsGroup.isVisible());
                    GlobalGeneralData.getInstance().debug_showRankStats = rankStatsGroup.isVisible();
                }
            }
        }
        // Activation Debug
        /*
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            //new GUIobjectNewOverBasePower(0,0, guiLayer[3], gameController.getEventsManager());
            newChallengeCompleted();
        }
        */
    }

    public void newMissionPanel(){
        new GUIobjectMissionPanel(gameController.getEventsManager(), guiLayer[3]);
    }

    public void newStartLevel(){
        new GUIobjectStartLevel(guiLayer[2]);
    }

    public void newGameOver(){
        new GUIobjectGameOver(gameController.getEventsManager(), guiLayer[2], guiLayer[3]);
    }
    public void newGameOverPanel(){
        new GUIobjectGameOverPanel(gameController.getEventsManager(), guiLayer[3]);
    }
    public void newLevelCompleted(){
        new GUIobjectLevelCompleted(gameController.getEventsManager(), guiLayer[2], guiLayer[3]);
    }
    public void newLevelCompletedPanel(){
        new GUIobjectLevelCompletedPanel(gameController.getEventsManager(), guiLayer[3]);
    }

    public void newBoxPoints(float x, float y, int combo, int points){
        new GUIobjectNewBoxPoints(x, y, combo, points, guiLayer[1], gameController.getEventsManager() );
    }

    public void newOverBasePoints(float x, float y, int points){
        new GUIobjectNewOverBasePoints(x, y, points, guiLayer[1], gameController.getEventsManager());
    }

    public void newOverBasePower(float x, float y, GlobalAttributes.POWER power) {
        GUIobjectsList.add(new GUIobjectNewOverBasePower(x, y, power, guiLayer[1], gameController.getEventsManager()));
    }
    public void newBoxDisabled(BoardObjectModel box){
        GUIobjectsList.add(new GUIobjectBoxDisabled(box, guiLayer[0]));
    }

    public void newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT announcement){
        announcementsList.add(new GUIobjectAnnouncement(announcement, guiLayer[3], this));
    }

    public void newPauseMenu(){
        guIobjectPausePanels = new GUIobjectPausePanels(guiLayer[3], gameController.getEventsManager());
    }

    public void newFloatingLostBox(float x, float y){
        new GUIobjectFloatingLostBox(guiLayer[1], x, y);
    }

    public void newChallengeCompleted(){
        GUIobjectsList.add(new GUIobjectChallengeComplete(guiLayer[3], gameController.getEventsManager()));
    }

    public void newIntroductionEvent(String event){
        GUIobjectsList.add(new LevelIntroductionEvent(event, guiLayer[3], gameController.getEventsManager(), characterDialogue));
    }

    public void showCharacterAlone(GUIobjectCharacterDialogue.CHARACTER_POSE pose, GUIobjectCharacterDialogue.SIDE side, float delay, int additionalDespX){
        characterDialogue.launchCharacterAlone(pose, side, delay, additionalDespX);
    }

    public void makeCharacterLeave(){
        characterDialogue.forceLeaveCharacter();
    }

    public GUIobjectCharacterDialogue getCharacterDialogue(){
        return characterDialogue;
    }

    public void announcementEnded(){
        ActiveAnnouncement = false;
        announcementsList.remove(0);
    }

    public void newScenarioEvent(GUIobject scenarioEvent){
        GUIobjectsList.add(scenarioEvent);
    }

    public void deployGamePanels(float delayTime){
        if(gamePanels.getDeployStatus() == GUIobjectGamePanels.DEPLOY_STATUS.RETRACTED)
            gamePanels.deployAll(delayTime);
    }

    public void retractGamePanels(float delayTime){
        gamePanels.retractAll(delayTime);
    }

    public void deployScoresPanel(float delayTime){
        gamePanels.deployScorePanel(delayTime);
    }
    public void retractScoresPanel(float delayTime){
        gamePanels.retractScoresPanel(delayTime);
    }

    public void deployPauseButtonPanel(float delayTime){
        gamePanels.deployPauseButtonPanel(delayTime);
    }

    public void activateInsufficientStockEvent(){
        gamePanels.activateInsufficientStockEvent();
    }
    public void activateTimeOutEvent(){
        gamePanels.activateTimeOutEvent();
    }

    public void createRankStats(){
        rankStatsGroup = new Group();
        rankStatsGroup.setTouchable(Touchable.disabled);
        Image background = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        background.setColor(0, 0, 0, 0.4f);
        rankStatsGroup.addActor(background);

        VerticalGroup statsListGroup = new VerticalGroup();
        statsListGroup.align(Align.bottomLeft);
        int optimalTime = GlobalLevelData.getInstance().getLevelRankStats().optimalTime;
        int optimalTimnePoints = GlobalLevelData.getInstance().getLevelRankStats().optimalTimePoints;
        statsListGroup.addActor(new Label("Opt.Time: " + optimalTime + " (" + optimalTimnePoints + " P)", skin));
        statsListGroup.addActor(new Label("BoxPoints: " + GlobalLevelData.getInstance().getLevelRankStats().boxPointsMargin, skin));
        statsListGroup.addActor(new Label("FloorPoints: " + GlobalLevelData.getInstance().getLevelRankStats().floorPointsMax, skin));
        statsListGroup.addActor(new Label(">>> 2 Stars: " + GlobalLevelData.getInstance().getLevelRankStats().pointsTwoStars, skin));
        statsListGroup.addActor(new Label(">>> 3 Stars: " + GlobalLevelData.getInstance().getLevelRankStats().pointsThreeStars, skin));
        statsListGroup.pack();
        rankStatsGroup.addActor(statsListGroup);
        background.setSize(statsListGroup.getWidth(), statsListGroup.getHeight());

        if(!GlobalGeneralData.getInstance().debug_showRankStats) rankStatsGroup.setVisible(false);
        guiLayer[5].addActor(rankStatsGroup);
    }

    public boolean isPauseMenuReady(){
        if(guIobjectPausePanels != null){
            return guIobjectPausePanels.isPauseMenuReady();
        }
        return false;
    }

    private void debug_button_Complete(){
        // Completar nivel con 3 estrellas
        TextButton buttonComplete = new TextButton("COMPLETE", skin);
        buttonComplete.setPosition(540, 15);
        buttonComplete.setSize(95, 35);
        buttonComplete.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameController", "FORZANDO COMPLECION");
                if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
                    GlobalLevelData.getInstance().setRequestedBoxes(0);
                    GlobalLevelData.getInstance().setLevelTime(700);
                    getGameController().getEventsManager().addChangeInBoxesEvent();
                }
            }
        });
        guiLayer[3].addActor(buttonComplete);
    }


    public GUIobjectGamePanels.DEPLOY_STATUS getScoresDeployStatus(){
        return gamePanels.getDeployStatus();
    }

    private GameController getGameController(){
        return gameController;
    }

    public void dispose(){
        //Gdx.app.log("GameUI","Disposed");
        GUIstage.dispose();
        skin.dispose();
    }
}