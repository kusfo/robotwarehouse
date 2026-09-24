package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DialogueEventDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionData;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SelectionEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 10/07/2018.
 */
public class GUIobjectLevelSelectionPanel extends GUIobjectSelectionPanel{
    private final CurrentGameChangeListener gameChangeListener;
    private Group layer;
    private SelectionEventsManager selectionEventsManager;
    private LevelSelectionHelper helper;
    private LevelSelectionData levelsData;
    private GUIobjectLevelSelectionAdditionalPanels additionalPanels;
    private GUIobjectCharacterDialogue characterDialogue;
    private Label.LabelStyle labelStyle_gb_18;
    private TextureAtlas generalAtlas, mapDecosAtlas;
    private Group groupPanel, groupScreen, levelsGroup;
    private Group layerGroupBackground, layerGroupLinks, layerGroupLevels, layerEffects;
    private ScrollPane levelsScrollPane;
    private ImageButton.ImageButtonStyle levelUnblockedButtonStyle, levelCompletedButtonStyle, levelBlockedButtonStyle, levelBlockedStarButtonStyle,
            levelblockedByChallengesStyle;

    private PANEL_EVENT panelEvent = PANEL_EVENT.NONE;
    private ArrayMap linksMap = new ArrayMap();

    private int currentEpisode;
    private int episodeStatus;
    private int levelFocus = 0;
    private int firstLevelUnblocked = 0;
    private int lastLevelCompleted = 0;
    private int firstChallengeIncomplete = 0;
    private int firstLevelWithLessStars = 0;
    private Vector2 scrollPanDestinyPosition = new Vector2();
    private boolean interactionActive = true;
    private boolean changingScreen = false;
    private float eventPauseTime = 0;
    private GlobalPreferencesData gameData;
    private boolean enteringLevelFocusDone = false;
    private boolean enteringAnimation;
    private Actor goalBackground = null;
    private String dialogueWaitingToShow = "";
    private boolean waitingToCompleteEpisode = false;
    private boolean waitingToCompleteAllEpisodeChallenges = false;
    private boolean specialChallengeLevelsAreUnblocked = false;
    private boolean eventReviewModeActivated;
    private boolean waitingAchievementsReviewed = false;
    private GUIobjectEpisodeCompleted episodeCompletedAnnouncement = null;
    private GUIobjectEpisodeChallengesCompleted allChallengesCompletedAnnouncement = null;
    private String specialDialogWaitingToShow = ""; // Dialogos especiales que se ejecutan al final de todas las acciones.
    private GUIobjectTutorialArrow tutorialArrow;

    private boolean debug_dontSaveLevelChanges = false;

    private ArrayList<followParticle> followStarEffectList = new ArrayList<followParticle>();

    private class followParticle{
        ParticleEffectActor effectActor;
        Actor followActor;
        followParticle(ParticleEffectActor effectActor, Actor followActor ){
            this.effectActor = effectActor;
            this.followActor = followActor;
        }
    }

    public GUIobjectLevelSelectionPanel(Group layer, SelectionEventsManager selectionEventsManager, LevelSelectionData levelsData, LevelSelectionHelper helper,
                                        GUIobjectLevelSelectionAdditionalPanels additionalPanels, GUIobjectCharacterDialogue characterDialogue, CurrentGameChangeListener gameChangeListener,boolean enteringAnimation){
        this.layer = layer;
        this.selectionEventsManager = selectionEventsManager;
        this.levelsData = levelsData;
        this.helper = helper;
        this.additionalPanels = additionalPanels;
        this.characterDialogue = characterDialogue;
        this.enteringAnimation = enteringAnimation;
        this.gameChangeListener = gameChangeListener;
        labelStyle_gb_18 = new Label.LabelStyle();
        labelStyle_gb_18.font = Assets.getFont("f_numbers_gb_18");
        generalAtlas = Assets.getTextureAtlas("levelSelection_general");
        mapDecosAtlas = Assets.getTextureAtlas("levelSelection_mapDecos");
        currentEpisode = GlobalGeneralData.getInstance().getCurrentEpisode();
        gameData = GlobalPreferencesData.getInstance();
        episodeStatus = gameData.getEpisodeStatus(currentEpisode);

        // Cargamos el mapa de niveles
        helper.loadLevelsMap(currentEpisode);
        execute();
    }

    private void execute(){
        int width = generalAtlas.findRegion("frame").getRegionWidth();
        int height = generalAtlas.findRegion("frame").getRegionHeight();
        groupPanel = new Group();
        groupPanel.setSize(width, height);
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - width) / 2), 10);
        groupPanel.setVisible(false);
        int screenWidth = generalAtlas.findRegion("screenLines").getRegionWidth();
        int screenHeight = generalAtlas.findRegion("screenLines").getRegionHeight();

        groupScreen = new Group();
        groupScreen.setSize(screenWidth, screenHeight);
        groupScreen.setPosition(107, 28);

        AnimatedImageActor animatedImageScreenLines= new AnimatedImageActor(generalAtlas, "screenLines", 0.1f, Animation.PlayMode.LOOP);
        animatedImageScreenLines.setPosition(107, 28);
        animatedImageScreenLines.setTouchable(Touchable.disabled);

        animatedImageScreenLines.play();
        Image panelImage = new Image(generalAtlas.findRegion("frame"));
        panelImage.setTouchable(Touchable.disabled);

        groupPanel.addActor(groupScreen);
        groupPanel.addActor(animatedImageScreenLines);
        groupPanel.addActor(panelImage);
        layer.addActor(groupPanel);

        eventReviewModeActivated = GlobalGeneralData.getInstance().eventReviewModeActivated;

        mountLevelsMap();

        levelsScrollPane.setTouchable(Touchable.disabled);
        interactionActive = false;

        if(!eventReviewModeActivated) {
            // Comprobamos si se ha completado todos los desafios para desbloquear los niveles especiales
            if (!specialChallengeLevelsAreUnblocked) {
                int totalChallenges = levelsData.getEpisodeDTO().getTotalChallenges();
                if (totalChallenges > 0 && gameData.getEpisodeChallengesCompleted(currentEpisode) == totalChallenges) {
                    waitingToCompleteAllEpisodeChallenges = true;
                }
            }

            waitingToCompleteEpisode = GlobalGeneralData.getInstance().episodeHasBeenCompleted;
            if (!waitingToCompleteEpisode) {
                // Si el primer nivel no está desbloqueado lo añadimos a la lista para que lo haga al principio
                if (gameData.getLevelStatus(1, levelsData.currentEpisode) == GameConstants.CONTENT_STATUS_BLOCKED) {
                    levelsData.getLevelsNeededActualization().add(1);
                }

                //Actualizamos los niveles señalados
                if (levelsData.getLevelsNeededActualization().size() > 0) {
                    if (eventPauseTime == 0) eventPauseTime = 1.0f;
                    // Si un nivel se va a actualizar, nos centramos en el al entrar
                    levelFocus = levelsData.getLevelsNeededActualization().get(0);
                }
            }

            // Si no se ha centrado en ningun nivel lo hacemos segun diversos criteros
            if (levelFocus == 0) {
                int lastEpisodePlayed = GlobalGeneralData.getInstance().lastEpisodePlayed;
                int lastLevelPlayed = GlobalGeneralData.getInstance().lastLevelPlayed;
                if(currentEpisode == lastEpisodePlayed){
                    if(lastLevelPlayed != 0){
                        levelFocus = lastLevelPlayed;
                    }
                }

                if (levelFocus == 0) {
                    // El primer nivel que falte por completar
                    if (firstLevelUnblocked != 0) {
                        levelFocus = firstLevelUnblocked;
                    } else {
                        // Si todos los niveles están completados nos centramos en otras cosas
                        if(firstChallengeIncomplete != 0) {
                            levelFocus = firstChallengeIncomplete;
                        } else if (firstLevelWithLessStars != 0) {
                            levelFocus = firstLevelWithLessStars;
                        } else {
                            levelFocus = levelsData.getEpisodeDTO().getGoalLevel();
                        }
                    }
                }
            }

            panelEvent = PANEL_EVENT.SEARCHING_EVENTS;
            panelLoaded = true;
        } else {
            // Venimos del menu tutoriales para visualizar alguno
            int level = GlobalGeneralData.getInstance().getCurrentLevel();
            int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, level); // test
            dialogueWaitingToShow = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getUnblockingDialog();
            characterDialogue.launchDialogue(dialogueWaitingToShow, true, 0.7f);
            panelEvent = PANEL_EVENT.SHOWING_DIALOGUE;
            levelFocus = level;
        }

        panelLoaded = true;
    }

    @Override
    public void startDeployingPanels(){
        // Animacion de entrada y paneles auxiliares
        if (enteringAnimation) enterPanel();
        else {
            groupPanel.setVisible(true);
            additionalPanels.deployPanels(false, true, true, true);
        }
    }

    @Override
    public void update(float delta){
        // Posicionamiento inicial de la camara al entrar al panel
        if(!enteringLevelFocusDone){
            levelsScrollPane.updateVisualScroll();
            moveScrollToLevel(levelFocus, true);
            enteringLevelFocusDone = true;
        }

        if(!changingScreen) {
            // Evento general de busqueda de posibles eventos pendientes
            if(panelEvent == PANEL_EVENT.SEARCHING_EVENTS) {

                // Eventos especiales
                if(waitingToCompleteAllEpisodeChallenges) {
                    panelEvent = PANEL_EVENT.COMPLETING_CHALLENGES;
                    unblockSpecialChallenges();
                }
                else if(waitingToCompleteEpisode && levelsData.getLevelsNeededActualization().size() == 0 && specialDialogWaitingToShow.equals("") ){
                    panelEvent = PANEL_EVENT.COMPLETING_EPISODE;
                    waitingToCompleteEpisode = false;
                    eventPauseTime = 0.5f;
                }
                // Actualización de niveles y diálogos
                else if(levelsData.getLevelsNeededActualization().size() > 0){
                    panelEvent = PANEL_EVENT.ACTUALIZING;
                }
                else if (!specialDialogWaitingToShow.equals("")) {
                    characterDialogue.launchDialogue(specialDialogWaitingToShow, true, 0.7f);
                    specialDialogWaitingToShow = "";
                    panelEvent = PANEL_EVENT.SHOWING_DIALOGUE;
                }
                else {
                    panelEvent = PANEL_EVENT.NONE;
                    levelsScrollPane.setTouchable(Touchable.enabled);
                    interactionActive = true;
                }
            }

            // Procesamos los niveles que han de ser actualizados
            if(panelEvent == PANEL_EVENT.ACTUALIZING) {
                if(eventPauseTime == 0) {
                    levelsData.getLevelsAddingStars().clear();
                    levelsData.rectangleFocusMin.setZero();
                    levelsData.rectangleFocusMax.setZero();
                    // Intentamos desbloquear algo con el primer nivel que tenia una actualizacion
                    helper.tryToUnblockLevels(levelsData.getLevelsNeededActualization().get(0));
                    panelEvent = PANEL_EVENT.PROCESSING_ACTIONS;
                }
            }

            if(panelEvent == PANEL_EVENT.PROCESSING_ACTIONS) {
                // Si hay estrellas que sumar a algun nivel bloqueado por estrella lo hacemos primero
                if (eventPauseTime == 0) {
                    if (levelsData.getLevelsAddingStars().size() > 0 && !GlobalGeneralData.getInstance().debug_selectlevel) {
                        int actualizedLevelNumber = levelsData.getLevelsAddingStars().get(0).getActualizedLevelNumber();
                        int blockedLevelNumber = levelsData.getLevelsAddingStars().get(0).getBlockedLevel();
                        int starsAdded = levelsData.getLevelsAddingStars().get(0).getStarsAdded();
                        addStarsToBlockedLevel(actualizedLevelNumber, blockedLevelNumber, starsAdded);
                        panelEvent = PANEL_EVENT.UNBLOCKING_STAR_LEVEL;
                    } else {
                        processPossibleLevelsWaitingToUnblock();
                    }
                }
            }

            if(panelEvent == PANEL_EVENT.MOVING_TO_UNBLOCK) {
                if (eventPauseTime == 0) {
                    if (levelsScrollPane.getVisualScrollX() == scrollPanDestinyPosition.x && levelsScrollPane.getVisualScrollY() == scrollPanDestinyPosition.y) {
                        eventPauseTime = 0.4f;
                        panelEvent = PANEL_EVENT.UNBLOCKING;
                    }
                }
            }

            // Procesamos los desbloqueos que toquen
            if(panelEvent == PANEL_EVENT.UNBLOCKING) {
                if(eventPauseTime == 0) {
                    for (int i = 0; i < levelsData.getLevelsWaitingToUnblock().size(); i++) {
                        unblockLevel(levelsData.getLevelsWaitingToUnblock().get(i), i == 0);
                    }
                    levelsData.getLevelsWaitingToUnblock().clear();

                    if(!dialogueWaitingToShow.equals("")){
                        characterDialogue.launchDialogue(dialogueWaitingToShow, true, 0.7f);
                        panelEvent = PANEL_EVENT.SHOWING_DIALOGUE;
                    } else {
                        panelEvent = PANEL_EVENT.SEARCHING_EVENTS;
                    }
                }
            }

            if( panelEvent == PANEL_EVENT.SHOWING_DIALOGUE){
                processDialogueEvents();
                if(!characterDialogue.hasDialogueActive()) {
                    dialogueWaitingToShow = "";
                    if(!eventReviewModeActivated)
                        panelEvent = PANEL_EVENT.SEARCHING_EVENTS;
                    else {
                        panelEvent = PANEL_EVENT.NONE;
                        selectionEventsManager.addEventGoToMain();
                    }
                }
            }

            if(panelEvent == PANEL_EVENT.COMPLETING_CHALLENGES) {
                if(allChallengesCompletedAnnouncement == null || allChallengesCompletedAnnouncement.isFinalized()) {
                    panelEvent = PANEL_EVENT.SEARCHING_EVENTS;
                }
            }

            if(panelEvent == PANEL_EVENT.COMPLETING_EPISODE) {
                if(eventPauseTime == 0) {
                    if (episodeCompletedAnnouncement != null) {
                        if (episodeCompletedAnnouncement.isFinalized()) {
                            // Si hemos terminado los 4 episodios base, activamos un evento especial
                            if(currentEpisode == 4) {
                                GlobalGeneralData.getInstance().episodeSpecialEvent = GlobalAttributes.EPISODE_SPECIAL_EVENT.BASE_EPISODES_COMPLETED;
                            }
                            goToEpisodes();
                            GlobalGeneralData.getInstance().episodeHasBeenCompleted = false;
                            panelEvent = PANEL_EVENT.NONE;


                        }
                    } else {
                        moveScrollToLevel(levelsData.getEpisodeDTO().getGoalLevel(), false);
                        episodeCompletedAnnouncement = new GUIobjectEpisodeCompleted(layer, currentEpisode);
                    }
                }
            }

            if(panelEvent == PANEL_EVENT.NONE) {
                // Solo guardamos los cambios en el archivo si se han finalizado todos los procesos de actualizacion y desbloqueo
                if (levelsData.getLevelsActualized().size() > 0) {
                    for (int i = 0; i < levelsData.getLevelsActualized().size(); i++) {
                        // Borramos la entrada da actualización
                        if(!debug_dontSaveLevelChanges)
                            gameData.removeLevelActualized(levelsData.getLevelsActualized().get(i), currentEpisode);
                    }
                    levelsData.getLevelsActualized().clear();
                    gameChangeListener.currentGameUpdated();
                }

                if(!waitingAchievementsReviewed){
                    selectionEventsManager.showWaitingAchievements();
                    waitingAchievementsReviewed = true;
                }
            }

            if(eventPauseTime > 0){
                eventPauseTime = MathUtils.clamp(eventPauseTime - delta * 1, 0, 9999);
            }

            if (additionalPanels.isBackOrderActivated()) {
                if(interactionActive) goToEpisodes();
                else additionalPanels.cancelBackOrder();
            }
        }

        // Seguimiento de las particulas que tienen que hacer una trayectoria ya que no se puede hacer automaticamente
        if(followStarEffectList.size() > 0){
            for(int i = 0; i < followStarEffectList.size(); i++){
                if(followStarEffectList.get(i).effectActor != null && followStarEffectList.get(i).followActor != null) {
                    float followX = followStarEffectList.get(i).followActor.getX() + ((Group) followStarEffectList.get(i).followActor).getChildren().get(0).getX() + 15;
                    float followY = followStarEffectList.get(i).followActor.getY() + ((Group) followStarEffectList.get(i).followActor).getChildren().get(0).getY() + 15;
                    followStarEffectList.get(i).effectActor.getParticleEffectModel().getParticleEffect().setPosition(followX, followY);
                }
            }
        }

        // Debug
        /*
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            //new GUIobjectEpisodeCompleted(layer, currentEpisode);
        }*/
        //debugMoveScroll(); // Usado para configurar posicionamientos en eventos
    }

    private void processDialogueEvents(){
        if(eventPauseTime == 0) {
            if (characterDialogue.getAwaitingDialogueEvents().size() > 0) {
                DialogueEventDTO event = characterDialogue.getAwaitingDialogueEvents().get(0);
                switch (event.getEvent()) {
                    case PAUSE_EVENT:
                        eventPauseTime = event.getValue1();
                        break;
                    case MAP_MOVE_TO_POSITION:
                        float posX = event.getValue1();
                        float posY = event.getValue2();
                        moveScrollToPos(posX, posY, false);
                        break;
                    case SHOW_UP_ARROW: case SHOW_DOWN_ARROW: case SHOW_LEFT_ARROW: case SHOW_RIGHT_ARROW:
                        tutorialArrow = new GUIobjectTutorialArrow(layer, event.getValue1(), event.getValue2(), event.getEvent(), 0.8f);
                        break;
                    case REMOVE_ARROW:
                        if(tutorialArrow != null) {
                            tutorialArrow.removeArrow();
                            tutorialArrow = null;
                        }
                        break;
                }
                characterDialogue.getAwaitingDialogueEvents().remove(0);
            }
        }
    }

    private void processPossibleLevelsWaitingToUnblock(){
        // Comprobamos la lista de desbloqueos resultante
        if (levelsData.getLevelsWaitingToUnblock().size() > 0) {
            // Hay desbloqueos, asi que creamos un rectangulo con sus posiciones y centramos la camara en el centro
            if (levelsData.rectangleFocusMin.x == levelsData.rectangleFocusMax.x && levelsData.rectangleFocusMin.y == levelsData.rectangleFocusMax.y) {
                scrollPanDestinyPosition.set(levelsData.rectangleFocusMin.x, levelsData.rectangleFocusMin.y);
            } else {
                scrollPanDestinyPosition.x = levelsData.rectangleFocusMin.x + ((levelsData.rectangleFocusMax.x - levelsData.rectangleFocusMin.x) / 2);
                scrollPanDestinyPosition.y = levelsData.rectangleFocusMin.y + ((levelsData.rectangleFocusMax.y - levelsData.rectangleFocusMin.y) / 2);
            }

            scrollPanDestinyPosition.x = screenXToScrollX(scrollPanDestinyPosition.x);
            scrollPanDestinyPosition.y = screenYToScrollY(scrollPanDestinyPosition.y);

            moveScrollToZone(scrollPanDestinyPosition.x, scrollPanDestinyPosition.y, false);
            // Lo añadimos para limpiar su estado al final del proceso
            levelsData.getLevelsActualized().add(levelsData.getLevelsNeededActualization().get(0));
            // Borramos la actualizacion actual de la lista para darla como procesada
            levelsData.getLevelsNeededActualization().remove(0);
            panelEvent = PANEL_EVENT.MOVING_TO_UNBLOCK;
        } else {
            levelsData.getLevelsActualized().add(levelsData.getLevelsNeededActualization().get(0)); // Lo añadimos para limpiar su estado al final del proceso
            levelsData.getLevelsNeededActualization().remove(0);
            panelEvent = PANEL_EVENT.SEARCHING_EVENTS;
        }
    }

    private void mountLevelsMap() {
        levelsGroup = new Group();
        // (i) Lo recortamos 32 para que encaje exacto verticalmente en la ventana y quede centrado si es el tamaño mínimo (3 filas)
        if(levelsData.getGridLevelRows() % 2 != 0) {
            levelsGroup.setSize(98 * (levelsData.getGridLevelColumns() + 2), (85 * (levelsData.getGridLevelRows()) + 85 - 32));
        } else {
            levelsGroup.setSize(98 * (levelsData.getGridLevelColumns() + 2), (85 * (levelsData.getGridLevelRows() + 1)) - 32);
        }

        // (i) Desplazamos los elementos 16 para centrarlos debido al recorte para centrar el levelsGroup
        layerGroupBackground = new Group();
        layerGroupBackground.setSize(levelsGroup.getWidth(), levelsGroup.getHeight());
        layerGroupBackground.setPosition(0, 16);
        layerGroupLinks = new Group();
        layerGroupLinks.setSize(levelsGroup.getWidth(), levelsGroup.getHeight());
        layerGroupLinks.setPosition(0, 16);
        layerGroupLevels = new Group();
        layerGroupLevels.setSize(levelsGroup.getWidth(), levelsGroup.getHeight());
        layerGroupLevels.setPosition(0, 16);
        layerEffects = new Group();
        layerEffects.setTouchable(Touchable.disabled);
        layerEffects.setSize(levelsGroup.getWidth(), levelsGroup.getHeight());
        layerEffects.setPosition(0, 16);

        //Background
        TiledDrawable tiledDrawable = new TiledDrawable(generalAtlas.findRegion("spaceBackground"));

        Image spaceBackground = new Image(tiledDrawable);
        spaceBackground.setSize(levelsGroup.getWidth(), levelsGroup.getHeight() + 16);
        spaceBackground.setPosition(0, -16);
        levelsGroup.addActor(spaceBackground);

        tiledDrawable = new TiledDrawable(generalAtlas.findRegion("mapBackground"));
        Image levelsBackground = new Image(tiledDrawable);

        if(levelsData.getGridLevelRows() % 2 != 0) {
            levelsBackground.setSize(levelsGroup.getWidth(), levelsGroup.getHeight() + 16);
            levelsBackground.setPosition(0, -16);
        } else {
            levelsBackground.setSize(levelsGroup.getWidth(), levelsGroup.getHeight() + 16 + 85);
            levelsBackground.setPosition(0, -(16 + 85));
        }
        levelsGroup.addActor(levelsBackground);

        // Capas de contenidos
        levelsGroup.addActor(layerGroupBackground);
        levelsGroup.addActor(layerGroupLinks);
        levelsGroup.addActor(layerGroupLevels);
        levelsGroup.addActor(layerEffects);

        levelUnblockedButtonStyle = new ImageButton.ImageButtonStyle();
        levelUnblockedButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_new", 1)));
        levelUnblockedButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_new", 2)));

        levelCompletedButtonStyle = new ImageButton.ImageButtonStyle();
        levelCompletedButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_completed", 1)));
        levelCompletedButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_completed", 2)));

        levelBlockedButtonStyle = new ImageButton.ImageButtonStyle();
        levelBlockedButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blocked")));
        levelBlockedButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blocked")));

        levelBlockedStarButtonStyle = new ImageButton.ImageButtonStyle();
        levelBlockedStarButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blockedBase")));
        levelBlockedStarButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blockedBase")));

        levelblockedByChallengesStyle = new ImageButton.ImageButtonStyle();
        levelblockedByChallengesStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blockeByChallenges")));
        levelblockedByChallengesStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("levelButton_blockeByChallenges")));

        levelsScrollPane = new ScrollPane(levelsGroup);
        levelsScrollPane.setSize(groupScreen.getWidth(), groupScreen.getHeight());
        levelsScrollPane.setFlingTime(0.3f);
        levelsScrollPane.setOverscroll(false, false);
        groupScreen.addActor(levelsScrollPane);

        // Levels
        for(int i = 0; i < levelsData.getEpisodeDTO().getLevels().size(); i++) {
            addLevelInMap(i, false);
        }

        addLevelLinks();

        // Decorations
        for(int i = 0; i < levelsData.getEpisodeDTO().getDecorations().size(); i++) {
            int column = levelsData.getEpisodeDTO().getDecorations().get(i).getCellH();
            int row = levelsData.getEpisodeDTO().getDecorations().get(i).getCellV();
            int number = levelsData.getEpisodeDTO().getDecorations().get(i).getDecoration();
            addDecoration(column, row, number);
        }

        if(goalBackground != null){
            addGoalEffectToBackground(goalBackground);
        }
    }

    private float screenXToScrollX(float x) {
        x = x - (groupScreen.getWidth() / 2);
        x = MathUtils.floor( MathUtils.clamp(x, 0, levelsScrollPane.getMaxX()) );
        return x;
    }

    private float screenYToScrollY(float y) {
        y = (levelsGroup.getHeight() - y) - (groupScreen.getHeight() / 2);
        //y = MathUtils.floor(MathUtils.clamp(y, 16, levelsScrollPane.getMaxY() - 15) );
        y = MathUtils.floor(MathUtils.clamp(y, 0, levelsScrollPane.getMaxY()) );
        return y;
    }

    private void moveScrollToLevel(int numLevel, boolean instantMovement){
        int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, numLevel);
        int column = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellV();
        float posX = levelsData.getGridLevelButton(column, row).buttonGroup.getX() + (levelsData.levelWidth / 2);
        float posY = levelsData.getGridLevelButton(column, row).buttonGroup.getY() + (levelsData.levelHeight / 2);

        levelsScrollPane.setScrollX(screenXToScrollX(posX));
        levelsScrollPane.setScrollY(screenYToScrollY(posY));
        if(instantMovement) levelsScrollPane.updateVisualScroll();
    }

    private void moveScrollToPos(float x, float y, boolean instantMovement){
        levelsScrollPane.setScrollX(x);
        levelsScrollPane.setScrollY(y);
        if(instantMovement) levelsScrollPane.updateVisualScroll();
    }

    private void moveScrollToZone(float panPosX, float panPosY,  boolean instantMovement){
        levelsScrollPane.setScrollX(panPosX);
        levelsScrollPane.setScrollY(panPosY);
        if(instantMovement) levelsScrollPane.updateVisualScroll();
    }

    private void unblockSpecialChallenges(){
        for(int i = 0; i < levelsData.getEpisodeDTO().getLevels().size(); i++){
            if(levelsData.getEpisodeDTO().getLevels().get(i).getType().equals("special_challenge")){
                levelsData.getLevelsNeededActualization().add(levelsData.getEpisodeDTO().getLevels().get(i).getLevelNumber());
            }
        }
        waitingToCompleteAllEpisodeChallenges = false;
        allChallengesCompletedAnnouncement = new GUIobjectEpisodeChallengesCompleted(layer);
        // La primera vez que se desbloqueen todos los desafios lanzaremos un dialogo explicativo
        if (!gameData.getDialogueViewStatus("dialog_levelSelection_all_challenges_completed"))
            specialDialogWaitingToShow = "dialog_levelSelection_all_challenges_completed";
    }

    private void addDecoration(int column, int row, int decorationNumber){
        int posX = MathUtils.floor(levelsData.levelSpaceH / 2) + (column * levelsData.levelSpaceH);
        int posY = MathUtils.floor(levelsGroup.getHeight() - levelsData.levelSpaceV) - (row * levelsData.levelSpaceV) + 1;
        if(row % 2 == 0) posX += levelsData.levelSpaceH;
        else posX += MathUtils.floor(levelsData.levelSpaceH / 2);

        Image decoBackground = new Image(generalAtlas.findRegion("decoBackground"));
        decoBackground.setPosition(posX - (decoBackground.getWidth() / 2), posY - (decoBackground.getHeight() / 2));
        decoBackground.setTouchable(Touchable.disabled);

        Image decoImage = new Image(mapDecosAtlas.findRegion("deco", decorationNumber));
        decoImage.setPosition(posX - (decoImage.getWidth() / 2), posY - (decoImage.getHeight() / 2));
        decoImage.setTouchable(Touchable.disabled);

        layerGroupLevels.addActor(decoImage);
        layerGroupBackground.addActor(decoBackground);
    }

    private void addLevelInMap(final int levelIndex, boolean unblockEffect) {
        int column = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellV();
        int levelNumber = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getLevelNumber();

        GlobalAttributes.LEVEL_TYPE levelType = GlobalAttributes.LEVEL_TYPE.fromString(levelsData.getEpisodeDTO().getLevels().get(levelIndex).getType());

        // Si era un nivel bloqueado con estrella pero ya se han completado las necesarias, se mostrará como un bloqueado normal
        int starsAccumulated = 0;
        if(levelType == GlobalAttributes.LEVEL_TYPE.STARS && !eventReviewModeActivated){
            starsAccumulated = helper.getStarLevelAccumulatedStars(levelNumber);
            if(starsAccumulated >= levelsData.getEpisodeDTO().getLevels().get(levelIndex).getStarsNeeded()){
                levelType = GlobalAttributes.LEVEL_TYPE.NORMAL;
            }
        }

        // Centro del hexagono que toca
        int posX = MathUtils.floor(levelsData.levelSpaceH / 2) + (column * levelsData.levelSpaceH);
        int posY = MathUtils.floor(levelsGroup.getHeight() - levelsData.levelSpaceV) - (row * levelsData.levelSpaceV) + 1;
        if(row % 2 == 0) posX += levelsData.levelSpaceH;
        else posX += MathUtils.floor(levelsData.levelSpaceH / 2);

        final Group levelButtonGroup = new Group();
        levelButtonGroup.setSize(levelsData.levelWidth, levelsData.levelHeight);
        levelButtonGroup.setPosition(posX - (levelsData.levelWidth / 2), posY - (levelsData.levelHeight / 2));
        levelButtonGroup.setOrigin(Align.center);
        if(unblockEffect) levelButtonGroup.setTouchable(Touchable.disabled);

        // Añadimos el boton al grid de niveles
        levelsData.setGridLevelButton(column, row, levelNumber, levelButtonGroup);

        // Si el nivel tiene alguna actualizacion pendiente lo añadimos a la lista de actualizaciones
        if(gameData.isLevelActualized(levelNumber, currentEpisode)) levelsData.getLevelsNeededActualization().add(levelNumber);

        Image levelBackground;
        if(levelType != GlobalAttributes.LEVEL_TYPE.SPECIAL_CHALLENGE)
            levelBackground = new Image(generalAtlas.findRegion("levelBackground"));
        else
            levelBackground = new Image(generalAtlas.findRegion("levelBackgroundSpecial"));

        levelBackground.setPosition(posX - (levelBackground.getWidth() / 2), posY - (levelBackground.getHeight() / 2));
        levelBackground.setTouchable(Touchable.disabled);

        int statusNumber = gameData.getLevelStatus(levelNumber, currentEpisode);

        // Modo revision de tutoriales
        if(eventReviewModeActivated) {
            // En ReviewMode desbloqueamos los niveles anteriores y bloqueamos los siguientes para que quede mas autentico
            int levelWatching = GlobalGeneralData.getInstance().getCurrentLevel();
            if(levelNumber < levelWatching)
                statusNumber = GameConstants.CONTENT_STATUS_COMPLETED;
            else if(levelNumber == levelWatching)
                statusNumber = GameConstants.CONTENT_STATUS_UNBLOCKED;
            else
                statusNumber = GameConstants.CONTENT_STATUS_BLOCKED;
        }
        else if(GlobalGeneralData.getInstance().debug_selectlevel && statusNumber == GameConstants.CONTENT_STATUS_BLOCKED)
            statusNumber = GameConstants.CONTENT_STATUS_UNBLOCKED;

        switch(statusNumber){
            // COMPLETED
            case GameConstants.CONTENT_STATUS_COMPLETED:
                addCompletedLevelButton(levelButtonGroup, levelNumber, levelCompletedButtonStyle);
                if(levelNumber > lastLevelCompleted) lastLevelCompleted = levelNumber;
                break;
            // UNBLOCKED
            case GameConstants.CONTENT_STATUS_UNBLOCKED:
                addUnblockedLevelButton(levelButtonGroup, levelNumber, levelUnblockedButtonStyle);
                // Guardamos el primer nivel sin completar, dando prioridad a los niveles base si el episodio no está completo
                if(firstLevelUnblocked == 0) {
                    if (episodeStatus == GameConstants.CONTENT_STATUS_UNBLOCKED) {
                        if(levelType != GlobalAttributes.LEVEL_TYPE.SPECIAL_CHALLENGE)
                            firstLevelUnblocked = levelNumber;
                    } else {
                        firstLevelUnblocked = levelNumber;
                    }
                }
                break;
            // BLOCKED
            case GameConstants.CONTENT_STATUS_BLOCKED:
                     if(levelType == GlobalAttributes.LEVEL_TYPE.NORMAL) addBlockedLevelButton(levelButtonGroup, levelBlockedButtonStyle);
                     else if(levelType == GlobalAttributes.LEVEL_TYPE.STARS)  addBlockedStarLevelButton(levelButtonGroup, levelIndex, starsAccumulated, levelBlockedStarButtonStyle);
                     else if(levelType == GlobalAttributes.LEVEL_TYPE.SPECIAL_CHALLENGE)  addBlockedLevelButton(levelButtonGroup, levelblockedByChallengesStyle);

                break;
        }

        // Si el nivel es de desbloqueo por desafios y ya fue desbloqueado, lo indicamos
        if(levelType == GlobalAttributes.LEVEL_TYPE.SPECIAL_CHALLENGE && statusNumber != GameConstants.CONTENT_STATUS_BLOCKED){
            specialChallengeLevelsAreUnblocked = true;
        }

        // Si el nivel final no esta completo, se mostrara un efecto sobre ese nivel
        if(statusNumber != GameConstants.CONTENT_STATUS_COMPLETED){
            if(levelNumber == levelsData.getEpisodeDTO().getGoalLevel()) {
                goalBackground = levelBackground;
            }
        }

        layerGroupBackground.addActor(levelBackground);
        layerGroupLevels.addActor(levelButtonGroup);

        if(unblockEffect) addUnblockingEffectToLevelButton(levelButtonGroup);
    }

    private void addLevelLinks(){
        for(int i = 0; i < levelsData.getEpisodeDTO().getLevels().size(); i++) {
            int levelNumber = levelsData.getEpisodeDTO().getLevels().get(i).getLevelNumber();
            int column = levelsData.getEpisodeDTO().getLevels().get(i).getCellH();
            int row = levelsData.getEpisodeDTO().getLevels().get(i).getCellV();
            int levelWatching = GlobalGeneralData.getInstance().getCurrentLevel();
            ArrayList<CellDTO> unblockingLevelsList = levelsData.getEpisodeDTO().getLevels().get(i).getUnblockingLevels();
            if (unblockingLevelsList.size() > 0) {
                for(int n = 0; n < unblockingLevelsList.size(); n++) {
                    int otherColumn = unblockingLevelsList.get(n).getColumn();
                    int otherRow = unblockingLevelsList.get(n).getRow();
                    if(!eventReviewModeActivated)
                        addLevelLink(column, row, otherColumn, otherRow, gameData.getLevelStatus(levelNumber, currentEpisode));
                    else {
                        if(levelNumber < levelWatching)
                            addLevelLink(column, row, otherColumn, otherRow,GameConstants.CONTENT_STATUS_COMPLETED);
                        else if(levelNumber == levelWatching)
                            addLevelLink(column, row, otherColumn, otherRow,GameConstants.CONTENT_STATUS_UNBLOCKED);
                        else
                            addLevelLink(column, row, otherColumn, otherRow,GameConstants.CONTENT_STATUS_BLOCKED);
                    }
                }
            }
        }
    }

    private void addCompletedLevelButton(Group levelButtonGroup, int levelNumber, ImageButton.ImageButtonStyle levelButtonStyle){
        addBaseOfPlayableLevelButton(levelButtonGroup, levelNumber, levelButtonStyle);
        addChallengeStarToLevelButton(levelButtonGroup, levelNumber);
    }

    private void addUnblockedLevelButton(Group levelButtonGroup, int levelNumber, ImageButton.ImageButtonStyle levelButtonStyle){
        addBaseOfPlayableLevelButton(levelButtonGroup, levelNumber, levelButtonStyle);

        // Brillo
        Image shineImage = new Image(generalAtlas.findRegion("effect", 2));
        shineImage.setPosition(5, 4);
        shineImage.setColor(1, 1, 1, 0f);
        shineImage.setTouchable(Touchable.disabled);

        AlphaAction alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(1.0f);
        alphaAction1.setDuration(1.0f);
        AlphaAction alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.7f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(alphaAction1);
        sequenceAction.addAction(alphaAction2);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        shineImage.addAction(repeatAction);
        levelButtonGroup.addActor(shineImage);

        addChallengeStarToLevelButton(levelButtonGroup, levelNumber);
    }

    private void addBlockedLevelButton(Group levelButtonGroup, ImageButton.ImageButtonStyle levelButtonStyle){
        ImageButton levelButton = new ImageButton(levelButtonStyle);
        levelButtonGroup.addActor(levelButton);
    }

    private void addBlockedStarLevelButton(Group levelButtonGroup, int levelIndex, int starsAccumulated, ImageButton.ImageButtonStyle levelButtonStyle){
        ImageButton levelButton = new ImageButton(levelButtonStyle);
        Image starImage = new Image(generalAtlas.findRegion("blockingStar"));
        Group starGroup = new Group();
        starGroup.setSize(starImage.getWidth(), starImage.getHeight());
        starGroup.setPosition((levelButtonGroup.getWidth() / 2) - (starImage.getWidth() / 2), (levelButtonGroup.getHeight() / 2) - (starImage.getHeight() / 2) + 4);
        starGroup.setOrigin(Align.center);
        starGroup.setTouchable(Touchable.disabled);

        int starsNeeded = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getStarsNeeded();
        int starsLeft = starsNeeded - starsAccumulated;
        Label starsNeededLabel = new Label(Integer.toString(starsLeft), labelStyle_gb_18);
        Container<Label> labelContainer = new Container<Label>(starsNeededLabel);
        labelContainer.setTransform(true);
        labelContainer.setPosition((starGroup.getWidth() / 2) - 1, (starGroup.getHeight() / 2) - 1);
        labelContainer.setOrigin(Align.center);
        labelContainer.setTouchable(Touchable.disabled);

        starsNeededLabel.setColor(Color.LIME.r, Color.LIME.g, Color.LIME.b, 0.80f);

        starGroup.addActor(starImage);
        starGroup.addActor(labelContainer);

        levelButtonGroup.addActor(levelButton);
        levelButtonGroup.addActor(starGroup);
        addStarLevelLoopAnimation(starGroup, levelButtonGroup);
    }
    private void addBaseOfPlayableLevelButton(Group levelButtonGroup, final int levelNumber, ImageButton.ImageButtonStyle levelButtonStyle){
        ImageButton levelButton = new ImageButton(levelButtonStyle);
        levelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (interactionActive) {
                    selectLevel(levelNumber);
                }
            }
        });
        levelButtonGroup.addActor(levelButton);

        int stars;
        if(eventReviewModeActivated && levelNumber == GlobalGeneralData.getInstance().getCurrentLevel())
            stars = 0;
        else
            stars = gameData.getLevelStars(levelNumber, currentEpisode);

        // Guardamos el nivel con menos estrellas conseguidas por si tenemos que centrarnos en el
        if(stars > 0 && stars < 3){
            if(stars < firstLevelWithLessStars)
                firstLevelWithLessStars = levelNumber;
        }

        Label levelNumberLabel = new Label(Integer.toString(levelNumber), labelStyle_gb_18);
        levelNumberLabel.setColor(1, 1, 1, 0.80f);
        levelNumberLabel.setTouchable(Touchable.disabled);
        levelNumberLabel.setPosition((levelButtonGroup.getWidth() / 2) - (levelNumberLabel.getWidth() / 2) - 2, 43);

        Image levelStarsImage = new Image(generalAtlas.findRegion("levelStars", stars));
        levelStarsImage.setPosition((levelButtonGroup.getWidth() / 2) - (levelStarsImage.getWidth() / 2), 16);
        levelStarsImage.setTouchable(Touchable.disabled);

        levelButtonGroup.addActor(levelNumberLabel);
        levelButtonGroup.addActor(levelStarsImage);
    }

    private void addChallengeStarToLevelButton(Group levelButtonGroup, int levelNumber){
        int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, levelNumber);
        if(levelsData.getEpisodeDTO().getLevels().get(levelIndex).hasChallenge()) {
            Image challengeStar;
            if (gameData.getLevelChallengeStatus(levelNumber, currentEpisode) && !eventReviewModeActivated) {
                challengeStar = new Image(generalAtlas.findRegion("levelStarChallenge", 1));
            } else {
                challengeStar = new Image(generalAtlas.findRegion("levelStarChallenge", 0));
                if(firstChallengeIncomplete == 0) firstChallengeIncomplete = levelNumber;
            }
            challengeStar.setPosition(levelsData.levelWidth / 2, levelsData.levelHeight - 13, Align.center);
            challengeStar.setTouchable(Touchable.disabled);
            levelButtonGroup.addActor(challengeStar);
        }
    }

    private void addUnblockingEffectToLevelButton(final Group levelButtonGroup){
        levelButtonGroup.setScale(1.3f, 1.3f);
        ScaleToAction scaleToAction;
        RunnableAction runnableAction;
        SequenceAction sequenceAction;

        // Boton
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1f, 1f);
        scaleToAction.setDuration(0.3f);
        scaleToAction.setInterpolation(new Interpolation.BounceOut(2));
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                levelButtonGroup.setTouchable(Touchable.enabled);
            }
        });

        sequenceAction = new SequenceAction();
        sequenceAction.addAction(scaleToAction);
        sequenceAction.addAction(runnableAction);
        levelButtonGroup.addAction(sequenceAction);
    }

    private void addGoalEffectToBackground(Actor background){
        final Image effectImage = new Image(generalAtlas.findRegion("effect", 3));
        effectImage.setPosition(background.getX() - 9, background.getY() - 9);
        effectImage.setOrigin(Align.center);
        effectImage.setColor(1, 1, 1, 0);
        SequenceAction sequenceAction = new SequenceAction();
        SequenceAction sequenceAlphas = new SequenceAction();
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                effectImage.setScale(1.18f);
            }
        });

        AlphaAction alphaAction1 = new AlphaAction();
        alphaAction1.setAlpha(0.7f);
        alphaAction1.setDuration(0.4f);
        AlphaAction alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.6f);
        sequenceAlphas.addAction(alphaAction1);
        sequenceAlphas.addAction(alphaAction2);

        ScaleToAction scaleToAction1 = new ScaleToAction();
        scaleToAction1.setScale(0.95f);
        scaleToAction1.setDuration(1);
        ParallelAction parallelAction = new ParallelAction(scaleToAction1, sequenceAlphas);
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(new DelayAction(0.1f));

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(sequenceAction);
        repeatAction.setCount(RepeatAction.FOREVER);
        effectImage.addAction(repeatAction);
        layerGroupBackground.addActor(effectImage);
    }

    private void addLevelLink(int column, int row, int destinyColumn, int destinyRow, int originStatus){
        Image linkImage;
        if (originStatus != GameConstants.CONTENT_STATUS_BLOCKED)
            linkImage = new Image(generalAtlas.findRegion("levelLink", 1));
        else
            linkImage = new Image(generalAtlas.findRegion("levelLink", 0));

        //Centrado en el grupo
        linkImage.setX(-linkImage.getWidth() / 2);
        linkImage.setY(-linkImage.getHeight() / 2);

        Group linkContainer = new Group();
        linkContainer.setTouchable(Touchable.disabled);
        linkContainer.addActor(linkImage);
        helper.setLinkPosition(linkContainer, column, row, destinyColumn, destinyRow, levelsGroup.getHeight());

        // Flechas
        float destinyStatus;
        if (!eventReviewModeActivated){
            destinyStatus = gameData.getLevelStatus(levelsData.getGridLevelButton(destinyColumn, destinyRow).levelNumber, currentEpisode);
        }
        // Modo revision de tutoriales
        else {
            int levelWatching = GlobalGeneralData.getInstance().getCurrentLevel();
            int destinyLevelNumber = levelsData.getGridLevelButton(destinyColumn, destinyRow).levelNumber;
            if (originStatus == GameConstants.CONTENT_STATUS_BLOCKED && destinyLevelNumber == levelWatching)
                destinyStatus = GameConstants.CONTENT_STATUS_UNBLOCKED;
            else
                destinyStatus = GameConstants.CONTENT_STATUS_BLOCKED;
        }

        if(originStatus == GameConstants.CONTENT_STATUS_BLOCKED && destinyStatus == GameConstants.CONTENT_STATUS_UNBLOCKED) {
            AnimatedImageActor animatedArrows = new AnimatedImageActor(generalAtlas, "linkArrows", 0.1f, Animation.PlayMode.LOOP);
            animatedArrows.setPosition(linkImage.getX() + 5, linkImage.getY() - 3);
            animatedArrows.play();
            linkContainer.addActor(animatedArrows);
        }


        int key = (column * 1000) + (row * 100) + (destinyColumn * 10) + destinyRow;
        // Si ya habia un link lo eliminamos
        if(linksMap.containsKey(key)){
            ((Group)linksMap.get(key)).remove();
            linksMap.removeKey(key);
        }
        linksMap.put(key, linkContainer);

        layerGroupLinks.addActor(linkContainer);
    }

    private void selectLevel(int levelNumber){

        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
        selectionEventsManager.addEventGoToLevel(levelNumber);
        changingScreen = true;

        GlobalGeneralData.getInstance().lastEpisodePlayed = currentEpisode;
        GlobalGeneralData.getInstance().lastLevelPlayed = levelNumber;

        /*
        // EMULACIÓN DE COMPLECION DE NIVEL
        int column = levelsData.getEpisodeDTO().getLevels().get(levelNumber - 1).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelNumber - 1).getCellV();
        GlobalPreferencesData.getInstance().setLevelStars(levelNumber , currentEpisode, MathUtils.random(1, 3));
        GlobalPreferencesData.getInstance().setLevelStatus(levelNumber , currentEpisode, 2);
        levelsData.getLevelsNeededActualization().add(levelNumber);
        panelEvent = PANEL_EVENT.ACTUALIZING;
        eventPauseTime = 1.0f;
        levelsScrollPane.setTouchable(Touchable.disabled);
        interactionActive = false;
        levelsData.getGridLevelButton(column,row).buttonGroup.remove();
        addLevelInMap(levelNumber, false, false);
        */
    }

    private void unblockLevel(int levelNumber, boolean sound){
        // Grabamos el estado de desbloqueo
        if(!debug_dontSaveLevelChanges) gameData.setLevelStatus(levelNumber, currentEpisode, GameConstants.CONTENT_STATUS_UNBLOCKED);
        int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, levelNumber);
        int column = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellH();
        int row = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getCellV();
        Actor buttonGroup = levelsData.getGridLevelButton(column, row).buttonGroup;

        if(sound) AudioManager.getInstance().playSound(AudioManager.SOUND.LEVEL_UNBLOCK_NORMAL);
        levelUnblockEffect(levelNumber, buttonGroup);
        eventPauseTime = 0.8f; // Si se ejecutara otro evento luego dejamos margen para que se vea bien este

        // Desbloqueamos todos los links que vienen hacia este nivel
        ArrayList<CellDTO> arrayList = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getUnblockingLevels();
        if(arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                addLevelLink(column, row, arrayList.get(i).getColumn(), arrayList.get(i).getRow(), 2);
            }
        }

        // Buscamos los niveles bloqueados y les añadimos los links con flechas
        ArrayList<LevelSelectionData.GridLevelButton> adjacentLevels = helper.getAdjacentLevelButtons(column, row);
        for(LevelSelectionData.GridLevelButton level : adjacentLevels){
            if(gameData.getLevelStatus(level.levelNumber, currentEpisode) == GameConstants.CONTENT_STATUS_BLOCKED){
                int adjacentLevelindex = Utils.getLevelIndexByLevelNumber(currentEpisode, level.levelNumber);
                ArrayList<CellDTO> unblockingLevelsList = levelsData.getEpisodeDTO().getLevels().get(adjacentLevelindex).getUnblockingLevels();
                for (int i = 0; i < unblockingLevelsList.size(); i++) {
                    int levelColumn = levelsData.getEpisodeDTO().getLevels().get(adjacentLevelindex).getCellH();
                    int levelRow = levelsData.getEpisodeDTO().getLevels().get(adjacentLevelindex).getCellV();
                    addLevelLink(levelColumn, levelRow, unblockingLevelsList.get(i).getColumn(), unblockingLevelsList.get(i).getRow(), 0);
                }
            }
        }

        // Los niveles que se desbloquean pueden ejecutar dialogos
        String unblockingDialog = levelsData.getEpisodeDTO().getLevels().get(levelIndex).getUnblockingDialog();
        boolean showIntroductionEvent = false;
        if(!unblockingDialog.equals("")){
            if (GlobalGeneralData.getInstance().debug_dialogsAndEvents == GlobalAttributes.ACTIVATION_CONDITION.ALWAYS ||
                    (GlobalGeneralData.getInstance().debug_dialogsAndEvents == GlobalAttributes.ACTIVATION_CONDITION.NORMAL &&
                            !gameData.getDialogueViewStatus(unblockingDialog))) {
                showIntroductionEvent = true;
            }
            if (showIntroductionEvent)
                dialogueWaitingToShow = unblockingDialog;
        }
    }

    private void levelUnblockEffect(int levelNumber, Actor buttonGroup){
        final int levelIndex = Utils.getLevelIndexByLevelNumber(currentEpisode, levelNumber);
        Image hexagonEffectImage = new Image(generalAtlas.findRegion("effect", 1));
        hexagonEffectImage.setTouchable(Touchable.disabled);
        hexagonEffectImage.setScale(1.3f, 1.3f);
        int buttonCenterX = MathUtils.floor(buttonGroup.getX() + (buttonGroup.getWidth() / 2));
        int buttonCenterY = MathUtils.floor(buttonGroup.getY() + (buttonGroup.getHeight() / 2));
        hexagonEffectImage.setPosition(buttonCenterX - (hexagonEffectImage.getWidth() / 2), buttonCenterY - (hexagonEffectImage.getHeight() / 2) - 1);
        hexagonEffectImage.setOrigin(Align.center);

        ScaleToAction scaleToAction;
        AlphaAction alphaAction2;
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableAction;

        // Boton
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1.3f, 1.3f);
        scaleToAction.setDuration(0.1f);
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                addLevelInMap(levelIndex, true);
            }
        });
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(scaleToAction);
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new RemoveActorAction());
        buttonGroup.addAction(sequenceAction);

        // Efecto
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(2, 2);
        scaleToAction.setDuration(0.4f);
        alphaAction2 = new AlphaAction();
        alphaAction2.setAlpha(0);
        alphaAction2.setDuration(0.4f);
        parallelAction = new ParallelAction(scaleToAction, alphaAction2);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(new RemoveActorAction());
        hexagonEffectImage.addAction(sequenceAction);
        layerEffects.addActor(hexagonEffectImage);
    }

    private void addStarsToBlockedLevel(int levelUnblockingNumber, final int levelBlockedNumber, int starsNumber){
        Group unblockerButtonGroup = (Group) helper.getButtonGroupByLevelNumber(levelUnblockingNumber);
        final Group blockedButtonGroup = (Group) helper.getButtonGroupByLevelNumber(levelBlockedNumber);

        final float posCenterX_1 = unblockerButtonGroup.getX() + (levelsData.levelWidth / 2);
        final float posCenterY_1 = unblockerButtonGroup.getY() + (levelsData.levelHeight / 2);
        final float posCenterX_2 = blockedButtonGroup.getX() + (levelsData.levelWidth / 2);
        final float posCenterY_2 = blockedButtonGroup.getY() + (levelsData.levelHeight / 2);

        final Group starGroup = (Group) blockedButtonGroup.getChildren().get(1);
        final Container labelContainer = ((Container) starGroup.getChildren().get(1));
        final Label label = (Label) labelContainer.getChildren().get(0);

        int starsNeeded = Integer.parseInt(label.getText().toString());
        final int starsResult = MathUtils.clamp(starsNeeded - starsNumber, 0, 99);

        // Paramos el palpitar de la estrella para que no interfiera con el evento
        cancelLevelLoopAnimation(starGroup);

        // Sequencia repetible
        SequenceAction repeatableSequenceAction = new SequenceAction();
        RunnableAction runnableShootStar = new RunnableAction();
        runnableShootStar.setRunnable(new Runnable() {
            @Override
            public void run() {
                flyingStarEffect(posCenterX_1, posCenterY_1, posCenterX_2, posCenterY_2, labelContainer, label);
            }
        });
        repeatableSequenceAction.addAction(runnableShootStar);
        repeatableSequenceAction.addAction(new DelayAction(0.3f));
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(starsNumber);
        repeatAction.setAction(repeatableSequenceAction);

        // Sequencia final
        RunnableAction runnableFinalize = new RunnableAction();
        runnableFinalize.setRunnable(new Runnable() {
            @Override
            public void run() {
                // Si hemos completado las estrellas llamamos al efecto de desbloqueo de estrella, si no finalizamos.
                if(starsResult == 0){
                    starUnblockEffect(levelBlockedNumber, blockedButtonGroup);
                } else {
                    if(levelsData.getLevelsAddingStars().size() > 0) levelsData.getLevelsAddingStars().remove(0);
                    panelEvent = PANEL_EVENT.PROCESSING_ACTIONS;
                    eventPauseTime = 0.5f;
                    addStarLevelLoopAnimation(starGroup, blockedButtonGroup);
                }
            }
        });

        SequenceAction baseSequence = new SequenceAction();
        baseSequence.addAction(repeatAction);
        baseSequence.addAction(runnableFinalize);

        unblockerButtonGroup.addAction(baseSequence);
    }

    private void addStarLevelLoopAnimation(final Actor star, final Actor levelButton){
        SequenceAction sequenceAction = new SequenceAction();

        ScaleToAction scaleToActionIn = new ScaleToAction();
        scaleToActionIn.setScale(1.2f, 1.2f);
        scaleToActionIn.setDuration(0.15f);
        RunnableAction runnableStarsFX = new RunnableAction();
        runnableStarsFX.setRunnable(new Runnable() {
            @Override
            public void run() {
                ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(
                        ParticleEffectModel.EFFECT_TYPE.STARLEVEL_PALPITATION, levelButton.getX() + (levelButton.getWidth()/2), levelButton.getY() + (levelButton.getHeight()/2), 0));
                layerEffects.addActor(particleEffectActor);
            }
        });
        ScaleToAction scaleToActionOut = new ScaleToAction();
        scaleToActionOut.setScale(1, 1);
        scaleToActionOut.setDuration(0.25f);

        sequenceAction.addAction(new DelayAction(3.5f));
        sequenceAction.addAction(runnableStarsFX);
        sequenceAction.addAction(scaleToActionIn);
        sequenceAction.addAction(scaleToActionOut);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceAction);

        star.addAction(repeatAction);
    }

    private void cancelLevelLoopAnimation(Actor star){
        star.clearActions();
        ScaleToAction scaleToActionOut = new ScaleToAction();
        scaleToActionOut.setScale(1, 1);
        scaleToActionOut.setDuration(0.2f);
        star.addAction(scaleToActionOut);
    }

    private void flyingStarEffect(float posCenterX_1, float posCenterY_1, final float posCenterX_2, final float posCenterY_2, final Container labelContainer, final Label label){
        AudioManager.getInstance().playSound(AudioManager.SOUND.UNBLOCK_FLYING_STAR);
        // ESTRELLA
        // Calculamos los datos para el zigzag de la estrella
        Vector2 originPosition = new Vector2(posCenterX_1, posCenterY_1);
        Vector2 targetPosition = new Vector2(posCenterX_2, posCenterY_2);
        Vector2 toTargetVec = new Vector2( (targetPosition.sub (originPosition)));
        Vector2 directionalVec = new Vector2(toTargetVec.nor());
        if(MathUtils.random(0, 1) == 1) directionalVec.rotate(90); else directionalVec.rotate(-90);
        float curveX = directionalVec.x * MathUtils.random(10, 20);
        float curveY = directionalVec.y * MathUtils.random(10, 20);

        Image movingStar = new Image(generalAtlas.findRegion("movingStar"));
        Group starContainer = new Group();
        starContainer.setOrigin(Align.center);
        starContainer.setSize(movingStar.getWidth(), movingStar.getHeight());
        starContainer.setPosition(posCenterX_1 - (movingStar.getWidth() / 2), posCenterY_1 - (movingStar.getHeight() / 2));
        starContainer.setScale(0.5f);

        ParticleEffectActor particleFlyingStar = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.FLYING_STAR_TO_BLOCKED_LEVEL, 16, 16, 0));
        followStarEffectList.add(new followParticle(particleFlyingStar, starContainer));
        layerEffects.addActor(particleFlyingStar);

        starContainer.addActor(movingStar);

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition (posCenterX_2 - (movingStar.getWidth() / 2), posCenterY_2 - (movingStar.getHeight() / 2));
        moveToAction.setDuration(0.5f);
        RunnableAction runnableExplosion = new RunnableAction();
        runnableExplosion.setRunnable(new Runnable() {
            @Override
            public void run() {
                ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(
                        ParticleEffectModel.EFFECT_TYPE.ADD_STAR_TO_BLOCKED_LEVEL, posCenterX_2, posCenterY_2, 0));
                layerEffects.addActor(particleEffectActor);
            }
        });

        ScaleToAction scaleToAction2 = new ScaleToAction();
        scaleToAction2.setScale(1.0f);//0.7
        scaleToAction2.setDuration(0.35f);
        SequenceAction scaleSequenceAction = new SequenceAction();
        scaleSequenceAction.addAction(new DelayAction(0.15f));
        scaleSequenceAction.addAction(scaleToAction2);

        ParallelAction parallelAction = new ParallelAction(moveToAction, scaleSequenceAction);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(runnableExplosion);
        sequenceAction.addAction(new RemoveActorAction());
        starContainer.addAction(sequenceAction);

        // Movimiento interior
        MoveToAction moveToActionInner_1 = new MoveToAction();
        moveToActionInner_1.setPosition (curveX, curveY);
        moveToActionInner_1.setDuration(0.1f);
        moveToActionInner_1.setInterpolation(Interpolation.circleOut);
        MoveToAction moveToActionInner_2 = new MoveToAction();
        moveToActionInner_2.setPosition (0, 0);
        moveToActionInner_2.setDuration(0.3f);
        moveToActionInner_2.setInterpolation(Interpolation.fade);
        SequenceAction sequenceActionInner = new SequenceAction();
        sequenceActionInner.addAction(moveToActionInner_1);
        sequenceActionInner.addAction(moveToActionInner_2);
        movingStar.addAction(sequenceActionInner);
        layerEffects.addActor(starContainer);

        // RESTA DE ESTRELLAS
        ScaleToAction scaleToActionIn = new ScaleToAction();
        scaleToActionIn.setScale(1.4f, 1.4f);
        scaleToActionIn.setDuration(0.1f);
        RunnableAction runnableChangeNumber = new RunnableAction();
        runnableChangeNumber.setRunnable(new Runnable() {
            @Override
            public void run() {
                int starsNeeded = Integer.parseInt(label.getText().toString());
                String newStarsText = Integer.toString( MathUtils.clamp(starsNeeded - 1, 0, 99));
                label.setText(newStarsText);
            }
        });
        ScaleToAction scaleToActionOut = new ScaleToAction();
        scaleToActionOut.setScale(1, 1);
        scaleToActionOut.setDuration(0.1f);

        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.5f));
        sequenceAction.addAction(scaleToActionIn);
        sequenceAction.addAction(runnableChangeNumber);
        sequenceAction.addAction(scaleToActionOut);
        labelContainer.addAction(sequenceAction);
    }

    private void starUnblockEffect(final int levelNumber, final Group buttonGroup){

        float initialPause = 0.7f;
        final float buttonCenterX = buttonGroup.getX() + (levelsData.levelWidth / 2);
        final float buttonCenterY = buttonGroup.getY() + (levelsData.levelHeight / 2);

        AlphaAction alphaAction;
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableExplosion;

        // Localizamos los elementos del boton para modificarlos
        final ImageButton button = ((ImageButton) buttonGroup.getChildren().get(0));
        Group starGroup = (Group) buttonGroup.getChildren().get(1);
        Actor label = starGroup.getChildren().get(1);

        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(initialPause + 0.5f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        label.addAction(sequenceAction);

        // Estrella interior
        ScaleToAction scaleToActionIn, scaleToActionOut, scaleToActionFinal;

        RunnableAction runnableBegin = new RunnableAction();
        runnableBegin.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.LEVEL_UNBLOCK_STAR);
            }
        });
        RunnableAction runnableStarsSmallFX = new RunnableAction();
        runnableStarsSmallFX.setRunnable(new Runnable() {
            @Override
            public void run() {
                ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(
                        ParticleEffectModel.EFFECT_TYPE.STARLEVEL_PALPITATION, buttonGroup.getX() + (buttonGroup.getWidth()/2), buttonGroup.getY() + (buttonGroup.getHeight()/2), 0));
                layerEffects.addActor(particleEffectActor);
            }
        });

        scaleToActionIn = new ScaleToAction();
        scaleToActionIn.setScale(1.5f, 1.5f);
        scaleToActionIn.setDuration(0.1f);
        scaleToActionOut = new ScaleToAction();
        scaleToActionOut.setScale(1, 1);
        scaleToActionOut.setDuration(0.1f);

        scaleToActionFinal = new ScaleToAction();
        scaleToActionFinal.setScale(3, 3);
        scaleToActionFinal.setDuration(0.3f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.15f);
        runnableExplosion = new RunnableAction();
        runnableExplosion.setRunnable(new Runnable() {
            @Override
            public void run() {
                ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.LEVEL_STAR_UNBLOCKING, buttonCenterX, buttonCenterY, 0));
                layerEffects.addActor(particleEffectActor);
                button.setStyle(levelBlockedButtonStyle);
            }
        });
        RunnableAction runnableFinalize = new RunnableAction();
        runnableFinalize.setRunnable(new Runnable() {
            @Override
            public void run() {
                if(levelsData.getLevelsAddingStars().size() > 0) levelsData.getLevelsAddingStars().remove(0);
                // Si hemos completado las estrellas, añadimos una peticion de desbloqueo para el nivel
                if(helper.areUnblockingLevelsListDone(levelNumber)) helper.addLevelToUnblock(levelNumber);
                panelEvent = PANEL_EVENT.PROCESSING_ACTIONS;
                gameChangeListener.starLevelUnblocked();
            }
        });
        SequenceAction alphaSequenceAction = new SequenceAction();
        alphaSequenceAction.addAction(new DelayAction(0.15f));
        alphaSequenceAction.addAction(alphaAction);

        parallelAction = new ParallelAction(scaleToActionFinal, alphaSequenceAction);
        sequenceAction = new SequenceAction();

        sequenceAction.addAction(new DelayAction(initialPause));
        sequenceAction.addAction(runnableBegin);
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(runnableStarsSmallFX);
        sequenceAction.addAction(scaleToActionIn);
        sequenceAction.addAction(scaleToActionOut);
        sequenceAction.addAction(new DelayAction(0.17f));
        sequenceAction.addAction(runnableExplosion);
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(runnableFinalize);
        sequenceAction.addAction(new RemoveActorAction());
        starGroup.addAction(sequenceAction);
    }

    private void enterPanel(){
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - groupPanel.getWidth()) / 2), GameConstants.VERTICAL_RESOLUTION);
        groupPanel.setVisible(true);
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), 10);
        moveToAction.setDuration(0.9f);
        moveToAction.setInterpolation(new Interpolation.Swing(0.7f));
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                additionalPanels.deployPanels(true, true, true, true);
            }
        });
        RunnableAction runnableSound = new RunnableAction();
        runnableSound.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_ENTER);
            }
        });
        SequenceAction sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(moveToAction);
        SequenceAction sequenceAction2 = new SequenceAction();
        sequenceAction2.addAction(new DelayAction(0.1f));
        sequenceAction2.addAction(runnableSound);
        sequenceAction2.addAction(new DelayAction(0.3f));
        sequenceAction2.addAction(runnableAction);

        ParallelAction parallelAction = new ParallelAction(sequenceAction1, sequenceAction2);
        groupPanel.addAction(parallelAction);
    }
    private void goToEpisodes(){
        selectionEventsManager.goToPanel(0);
        interactionActive = false;
    }
    public void goToEpisodesIfReady(){
        if(interactionActive && additionalPanels.getPanelsDeployed()) {
            selectionEventsManager.goToPanel(0);
            interactionActive = false;
        }
    }

    @Override
    public void startRetractingPanels(){
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_LEAVES);
        additionalPanels.retractPanels();

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), GameConstants.VERTICAL_RESOLUTION);
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineIn);

        SequenceAction sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(moveToAction);
        sequenceAction1.addAction(new RemoveActorAction());
        SequenceAction sequenceAction2 = new SequenceAction();
        sequenceAction2.addAction(new DelayAction(0.1f));

        ParallelAction parallelAction = new ParallelAction(sequenceAction1, sequenceAction2);
        groupPanel.addAction(parallelAction);
    }

    private enum PANEL_EVENT {
        NONE, SEARCHING_EVENTS, ACTUALIZING, PROCESSING_ACTIONS, MOVING_TO_UNBLOCK, UNBLOCKING, UNBLOCKING_STAR_LEVEL, SHOWING_DIALOGUE, COMPLETING_CHALLENGES,
        COMPLETING_EPISODE
    }
    public interface CurrentGameChangeListener {
        void currentGameUpdated();
        void starLevelUnblocked();
    }
}