package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.screens.SelectionScreen;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.RobotWarehouseGame;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionData;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SavedGamesManager;

/**
 * Created by JordiRM on 10/01/2017.
 */
public class SelectionUI extends BaseUI implements GUIobjectLevelSelectionPanel.CurrentGameChangeListener {
    private final SavedGamesManager savedGamesManager;
    private final AchievementManager achievementManager;
    private SelectionScreen selectionScreen;
    private LevelSelectionHelper levelSelectionHelper;
    private LevelSelectionData levelSelectionData;
    private GUIobjectLevelSelectionAdditionalPanels levelSelectionAdditionalPanels;
    private GUIobjectSelectionPanel actualPanel, newPanel;
    private GUIobjectCharacterDialogue characterDialogue;
    private Group[] guiLayer;
    private boolean justEntering = true;
    private boolean selectionMusic = false;
    private boolean safeToRun = false;

    public SelectionUI(SelectionScreen selectionScreen, Viewport viewport, SavedGamesManager savedGamesManager, AchievementManager achievementManager){
        super(viewport);
        this.selectionScreen = selectionScreen;
        this.savedGamesManager = savedGamesManager;
        this.achievementManager = achievementManager;

        Image background = new Image(Assets.getTexture("bg_selectionScreen"));
        GUIstage.addActor(background);

        // Grupos generales para controlar los depths entre elementos de gui
        guiLayer = new Group[4];
        guiLayer[1] = new Group();  // Paneles
        guiLayer[2] = new Group();  // Paneles adicionales
        guiLayer[3] = new Group();  // Conversacion personaje
        GUIstage.addActor(guiLayer[1]);
        GUIstage.addActor(guiLayer[2]);
        GUIstage.addActor(guiLayer[3]);

        levelSelectionData = new LevelSelectionData();
        levelSelectionHelper = new LevelSelectionHelper(levelSelectionData);
        levelSelectionAdditionalPanels = new GUIobjectLevelSelectionAdditionalPanels(levelSelectionData, guiLayer[2]);
        characterDialogue = new GUIobjectCharacterDialogue(guiLayer[3]);

        // Mostramos la intro si el primer episodio no ha sido desbloqueado, o por el contrario, el panel de selección que toque
        if(GlobalPreferencesData.getInstance().getEpisodeStatus(1) == GameConstants.CONTENT_STATUS_BLOCKED)
            newPanel = new GUIobjectIntroductionPanel(guiLayer[1],  selectionScreen.getSelectionEventsManager(), characterDialogue);
        else {
            if(!GlobalGeneralData.getInstance().eventReviewModeActivated)
                setSelectionPanel(GlobalGeneralData.getInstance().getSelectionScreenEnteringPanel());
            else
                setSelectionPanel(1);
        }

        addFadeSystem();
        justEntering = false;
    }

    public void update(float delta){
        updateScreenFade(delta);
        GUIstage.act(delta);
        GUIstage.draw();

        if(safeToRun) {
            if (actualPanel != null && actualPanel.isPanelLoaded())
                actualPanel.update(delta);

            if (newPanel != null) {
                if (newPanel.isPanelLoaded()) {
                    if (actualPanel != null)
                        actualPanel.startRetractingPanels();
                    newPanel.startDeployingPanels();
                    actualPanel = newPanel;
                    newPanel = null;
                }
            }
            backAction();
        }
    }

    public void setSelectionPanel(int panelNumber){
        if(!selectionMusic){
            AudioManager.getInstance().playMusic(AudioManager.MUSIC.SELECTION_SCREEN, true, true);
            selectionMusic = true;
        }
        if(panelNumber == 0) {
            newPanel = new GUIobjectEpisodeSelectionPanel(guiLayer[1], selectionScreen.getSelectionEventsManager(), characterDialogue, !justEntering);
        } else {
            newPanel = new GUIobjectLevelSelectionPanel(guiLayer[1], selectionScreen.getSelectionEventsManager(),
                    levelSelectionData, levelSelectionHelper, levelSelectionAdditionalPanels,  characterDialogue, this, !justEntering);
        }
    }

    public void setActorsTouchables(boolean touchables){
        Touchable touchableStatus;
        if(touchables) touchableStatus = Touchable.enabled;
        else touchableStatus = Touchable.disabled;
        for(Actor actor : GUIstage.getActors()) {
            actor.setTouchable(touchableStatus);
        }
    }

    public void setSafeToRun(){
        safeToRun = true;
        screenFadeIn();
    }

    private void backAction() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(actualPanel.isPanelLoaded()){
                if(actualPanel instanceof GUIobjectLevelSelectionPanel){
                    ((GUIobjectLevelSelectionPanel)actualPanel).goToEpisodesIfReady();
                }
                else if(actualPanel instanceof GUIobjectEpisodeSelectionPanel){
                    ((GUIobjectEpisodeSelectionPanel)actualPanel).goToMainIfReady();
                }
            }
        }
    }

    public Stage getUIStage(){
        return GUIstage;
    }

    public void dispose(){
        GUIstage.dispose();
    }

    @Override
    public void currentGameUpdated() {
        if (savedGamesManager.isSavedRemoteGameEnabled() && GlobalPreferencesData.getInstance().isCloudSaveSynced())
            savedGamesManager.pushSavedGame(GameConstants.CLOUD_GAME_ID);
    }

    @Override
    public void starLevelUnblocked() {
        achievementManager.checkUnblockLevelRelatedAchievement(GlobalAttributes.LEVEL_TYPE.STARS);
    }
}
