package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.EpisodeDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SelectionEventsManager;

import java.util.ArrayList;

/**
 * Created by JordiRM on 25/01/2017.
 */
public class GUIobjectEpisodeSelectionPanel extends GUIobjectSelectionPanel{
    private Group layer;
    private SelectionEventsManager selectionEventsManager;
    private GUIobjectCharacterDialogue characterDialogue;
    private Label.LabelStyle labelStyle_gb_11, labelStyle_gb_13, labelStyle_gb_16, labelStyle_gb_18, labelStyle_gb_22;
    private ImageButton.ImageButtonStyle episodeBlockedButtonStyle;
    private ScrollPane scrollPane;
    private PANEL_EVENT panelEvent = PANEL_EVENT.NONE;
    private int actualEpisodeIndex = 0;
    private float nextScrollPositionX = 0;
    private float eventPauseTime = 0;
    private int numEpisodes;
    private int panWidth, panHeight, episodeWidth;
    private Color c_tittle, c_menuButton;
    private Group groupPanel, groupScreen;
    private Group allEpisodesGroup;
    private Label labelSectionTitle;
    private TextureAtlas generalAtlas,episodesAtlas;
    private ImageButton leftArrow, rightArrow;
    private boolean enteringAnimation;
    private boolean interactionActive = true;
    private ArrayList<Integer> episodesWaitingToUnblock = new ArrayList<Integer>();
    private ArrayList<EpisodeDTO> episodeData;
    private boolean enteringEpisodeFocusDone = false;
    private int episodeFocus = 1;
    private int firstIncompleteEpisode = 0;
    private boolean waitingAchievementsReviewed = false;

    private boolean debug_unblockOnClick = false;

    private class EpisodeStructure {
        public int episodeNumber;
        public Group buttonGroup;
        public EpisodeStructure(int episodeNumber, Group buttonGroup){
            this.episodeNumber = episodeNumber;
            this.buttonGroup = buttonGroup;
        }
    }
    private ArrayList<EpisodeStructure> episodeStructuresList = new ArrayList<EpisodeStructure>();

    public GUIobjectEpisodeSelectionPanel(Group layer, SelectionEventsManager selectionEventsManager, GUIobjectCharacterDialogue characterDialogue, boolean enteringAnimation){
        this.selectionEventsManager = selectionEventsManager;
        this.layer = layer;
        this.characterDialogue = characterDialogue;
        this.enteringAnimation = true; // (i) Probando con la animación siempre activa
        labelStyle_gb_11 = new Label.LabelStyle();
        labelStyle_gb_11.font = Assets.getFont("f_base_gb_11");
        labelStyle_gb_13 = new Label.LabelStyle();
        labelStyle_gb_13.font = Assets.getFont("f_base_gb_13");
        labelStyle_gb_16 = new Label.LabelStyle();
        labelStyle_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_gb_22 = new Label.LabelStyle();
        labelStyle_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_gb_18 = new Label.LabelStyle();
        labelStyle_gb_18.font = Assets.getFont("f_numbers_gb_18");

        panWidth = 446;
        panHeight = 234;
        episodeWidth = MathUtils.floor(panWidth / 3);
        c_tittle = new Color(253 / 255f,232 / 255f,127 / 255f, 0.85f);
        c_menuButton = new Color(253 / 255f,232 / 255f,127 / 255f, 0.85f);

        generalAtlas = Assets.getTextureAtlas("episodeSelection_general");
        episodesAtlas = Assets.getTextureAtlas("episodeSelection_episodes");

        episodeData = GlobalGeneralData.getInstance().getEpisodesData();

        execute();
    }

    private void execute(){
        createPanel();
        createEpisodesList();
        createEpisodes();

        // Nos centramos en el último episodio jugado
        if(GlobalGeneralData.getInstance().getCurrentEpisode() != 0){
            episodeFocus = GlobalGeneralData.getInstance().getCurrentEpisode();
        } else {
            // De lo contrario nos centramos en el primer episodio que aun no esté completo
            episodeFocus = firstIncompleteEpisode;
        }

        // Comprobamos siempre que se desbloqueen toddos los episodios necesarios
        checkAutomaticUnlockEvents();

        // Si hay algun evento especial esperando, lo lanzamos
        if(GlobalGeneralData.getInstance().episodeSpecialEvent != GlobalAttributes.EPISODE_SPECIAL_EVENT.NONE) {
            LaunchEpisodeSpecialEvent(GlobalGeneralData.getInstance().episodeSpecialEvent);
        }

        // Comprobamos los desbloqueos pendientes
        else if (episodesWaitingToUnblock.size() > 0) {
            scrollPane.setTouchable(Touchable.disabled);
            eventPauseTime = 1;
            panelEvent = PANEL_EVENT.ACTUALIZING;
        }

        panelLoaded = true;
    }

    private void createPanel(){
        int width = generalAtlas.findRegion("frame").getRegionWidth();
        int height = generalAtlas.findRegion("frame").getRegionHeight();
        groupPanel = new Group();
        groupPanel.setSize(width, height);
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - width) / 2), 0);
        groupPanel.setOrigin(Align.center);
        groupPanel.setVisible(false);

        int screenWidth = generalAtlas.findRegion("screen", 1).getRegionWidth();
        int screenHeight = generalAtlas.findRegion("screen", 1).getRegionHeight();

        groupScreen = new Group();
        groupScreen.setSize(screenWidth, screenHeight);
        groupScreen.setPosition(112, 73);

        AnimatedImageActor animatedImageScreen = new AnimatedImageActor(generalAtlas, "screen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageScreen.setTouchable(Touchable.disabled);
        animatedImageScreen.play();

        Image panelImage = new Image(generalAtlas.findRegion("frame"));
        panelImage.setTouchable(Touchable.disabled);

        labelSectionTitle = new Label("", labelStyle_gb_22);
        labelSectionTitle.setAlignment(Align.center);
        labelSectionTitle.setColor(c_tittle);
        labelSectionTitle.setPosition((float)(screenWidth / 2) - (labelSectionTitle.getWidth() / 2), 263);

        // Boton Menu
        TextureAtlas generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");
        TextButton.TextButtonStyle menuButtonStyle = new TextButton.TextButtonStyle();
        menuButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 1)));
        menuButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 2)));
        menuButtonStyle.font = labelStyle_gb_22.font;
        menuButtonStyle.fontColor = c_menuButton;
        TextButton menuButton = new TextButton(GlobalGeneralData.getInstance().getGlobalBundleData().get("menu_button"), menuButtonStyle);
        menuButton.setPosition(307,30);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(interactionActive) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    selectionEventsManager.addEventGoToMain();
                    interactionActive = false;
                }
            }
        });

        groupScreen.addActor(animatedImageScreen);
        groupScreen.addActor(labelSectionTitle);

        groupPanel.addActor(groupScreen);
        groupPanel.addActor(panelImage);
        groupPanel.addActor(menuButton);

        layer.addActor(groupPanel);
    }

    @Override
    public void startDeployingPanels(){
        groupPanel.setVisible(true);
        if(enteringAnimation){
            interactionActive = false;
            enterPanel();
        }
    }

    private void createEpisodesList(){
        for(int i = 0; i < episodeData.size(); i++) {
            // Llenamos la lista solo con los episodios que se vayan a mostrar... que de momento son todos
            episodeStructuresList.add(new EpisodeStructure(episodeData.get(i).getEpisodeNumber(), null));
        }
        numEpisodes = episodeStructuresList.size();
    }

    private void createEpisodes(){
        labelSectionTitle.setText(GlobalGeneralData.getInstance().getGlobalBundleData().get("select_episode"));

        // Episodios
        episodeBlockedButtonStyle = new ImageButton.ImageButtonStyle();
        episodeBlockedButtonStyle.up = new TextureRegionDrawable(new TextureRegion(episodesAtlas.findRegion("episode_blocked")));
        episodeBlockedButtonStyle.down = new TextureRegionDrawable(new TextureRegion(episodesAtlas.findRegion("episode_blocked")));

        allEpisodesGroup = new Group();
        allEpisodesGroup.setSize(episodeWidth * numEpisodes, 234);

        // Añadimos todos los episodios al grupo general
        for(int i = 0; i < episodeStructuresList.size() ; i++) {
            addEpisode(episodeStructuresList.get(i), i * episodeWidth, false);
        }

        scrollPane = new ScrollPane(allEpisodesGroup);
        scrollPane.setSize(panWidth, panHeight + 6);
        scrollPane.setPosition(53, 4);//8
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFlingTime(0);

        // BOTONES
        ImageButton.ImageButtonStyle ArrowButtonStyle = new ImageButton.ImageButtonStyle();
        ArrowButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_left_episodes", 1)));
        ArrowButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_left_episodes", 2)));
        ArrowButtonStyle.disabled = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_left_episodes", 3)));
        leftArrow = new ImageButton(ArrowButtonStyle);
        leftArrow.setPosition( 3, 15);
        leftArrow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(interactionActive) {
                    if(panelEvent == PANEL_EVENT.NONE || panelEvent == PANEL_EVENT.MOVING) {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        moveToEpisodeIndex(actualEpisodeIndex - 3, true);
                    }
                }
            }
        });

        ArrowButtonStyle = new ImageButton.ImageButtonStyle();
        ArrowButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_right_episodes", 1)));
        ArrowButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_right_episodes", 2)));
        ArrowButtonStyle.disabled = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("pag_right_episodes", 3)));
        rightArrow = new ImageButton(ArrowButtonStyle);
        rightArrow.setPosition( 490, 15);
        rightArrow.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(interactionActive) {
                    if(panelEvent == PANEL_EVENT.NONE || panelEvent == PANEL_EVENT.MOVING) {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        moveToEpisodeIndex(actualEpisodeIndex + 3, true);
                    }
                }
            }
        });

        groupScreen.addActor(scrollPane);
        groupScreen.addActor(leftArrow);
        groupScreen.addActor(rightArrow);
    }

    private void moveToEpisodeIndex(int index, boolean withMoveEvent){
        // La posicion final será en medio del panel, no en el principio, para que el episodio quede centrado
        nextScrollPositionX = MathUtils.clamp((index  * episodeWidth) - episodeWidth, 0, scrollPane.getMaxX());
        scrollPane.setScrollX(nextScrollPositionX);
        if(withMoveEvent) panelEvent = PANEL_EVENT.MOVING;
    }

    private void moveToEpisodeIndexAndUnblock(int index){
        moveToEpisodeIndex(index, false);
        panelEvent = PANEL_EVENT.MOVING_TO_UNBLOCK;
    }

    private void instantMoveToEpisodeIndex(int index){
        moveToEpisodeIndex(index, false);
        scrollPane.updateVisualScroll();
    }

    @Override
    public void update(float delta){

        // Posicionamiento inicial de la camara al entrar al panel
        if(!enteringEpisodeFocusDone){
            instantMoveToEpisodeIndex(getEpisodeIndexByNumber(episodeFocus));
            scrollPane.updateVisualScroll();
            enteringEpisodeFocusDone = true;
        }

        actualEpisodeIndex = MathUtils.clamp( MathUtils.floor((scrollPane.getScrollX() + (episodeWidth / 2)) / episodeWidth), 0, 999);
        if(actualEpisodeIndex == 0){
            if(!leftArrow.isDisabled()) leftArrow.setDisabled(true);
        } else {
            if(leftArrow.isDisabled()) leftArrow.setDisabled(false);
        }

        if(actualEpisodeIndex == numEpisodes - 3){
            if(!rightArrow.isDisabled()) rightArrow.setDisabled(true);
        } else {
            if(rightArrow.isDisabled()) rightArrow.setDisabled(false);
        }

        if(panelEvent == PANEL_EVENT.ACTUALIZING){
            if(eventPauseTime == 0) {
                moveToEpisodeIndexAndUnblock(getEpisodeIndexByNumber(episodesWaitingToUnblock.get(0)));
            }
        }
        if(panelEvent == PANEL_EVENT.MOVING || panelEvent == PANEL_EVENT.MOVING_TO_UNBLOCK){
            if(scrollPane.getVisualScrollX() == nextScrollPositionX){
                if(panelEvent == PANEL_EVENT.MOVING_TO_UNBLOCK){
                    panelEvent = PANEL_EVENT.UNBLOCKING;
                    eventPauseTime = 0.4f;
                } else if(panelEvent == PANEL_EVENT.MOVING){
                    scrollPane.setTouchable(Touchable.enabled);
                    panelEvent = PANEL_EVENT.NONE;
                }
            }
        }

        if(panelEvent == PANEL_EVENT.UNBLOCKING){
            if(eventPauseTime == 0) {
                int episodeNumber = episodesWaitingToUnblock.get(0);
                int index = getEpisodeIndexByNumber(episodesWaitingToUnblock.get(0));
                if (index != -1) unblockEpisode(episodeStructuresList.get(index));
                eventPauseTime = 1;
                // Guardamos el episodio desbloqueado
                GlobalPreferencesData.getInstance().setEpisodeStatus(episodesWaitingToUnblock.get(0), GameConstants.CONTENT_STATUS_UNBLOCKED);
                episodesWaitingToUnblock.remove(0);

                if(launchDialogueAfterUnblocking(episodeNumber)){
                    panelEvent = PANEL_EVENT.SHOWING_DIALOGUE;
                } else {
                    if (episodesWaitingToUnblock.size() > 0) {
                        panelEvent = PANEL_EVENT.ACTUALIZING;
                    } else {
                        panelEvent = PANEL_EVENT.NONE;
                        scrollPane.setTouchable(Touchable.enabled);
                    }
                }
            }
        }

        if( panelEvent == PANEL_EVENT.SHOWING_DIALOGUE){
            if(!characterDialogue.hasDialogueActive()){
                if (episodesWaitingToUnblock.size() > 0) {
                    panelEvent = PANEL_EVENT.ACTUALIZING;
                    eventPauseTime = 0.8f;
                } else {
                    panelEvent = PANEL_EVENT.NONE;
                    scrollPane.setTouchable(Touchable.enabled);
                }
            }
        }

        if(panelEvent == PANEL_EVENT.NONE) {
            // Desplazamiento automatico para centrar los elementos
            if (!scrollPane.isPanning()) {
                if (scrollPane.getScrollX() > (actualEpisodeIndex * episodeWidth)) {
                    if (scrollPane.getScrollX() <= ((actualEpisodeIndex * episodeWidth) + (episodeWidth / 2))) {
                        scrollPane.setScrollX(scrollPane.getScrollX() - (2000 * delta));
                        if (scrollPane.getScrollX() <= (actualEpisodeIndex * episodeWidth)) {
                            scrollPane.setScrollX(actualEpisodeIndex * episodeWidth);
                        }
                    }
                } else {
                    scrollPane.setScrollX(scrollPane.getScrollX() + (2000 * delta));
                    if (scrollPane.getScrollX() > (actualEpisodeIndex * episodeWidth)) {
                        scrollPane.setScrollX(actualEpisodeIndex * episodeWidth);
                    }
                }
            }
            if(!waitingAchievementsReviewed){
                selectionEventsManager.showWaitingAchievements();
                waitingAchievementsReviewed = true;
            }
        }

        if(eventPauseTime > 0){
            eventPauseTime = MathUtils.clamp(eventPauseTime - delta * 1, 0, 9999);
        }
    }


    private void addEpisode(final EpisodeStructure episodeStructure, float posX, boolean unblockingEvent){
        final int episodeNumber = episodeStructure.episodeNumber;
        ImageButton.ImageButtonStyle episodeButtonStyle;
        int episodeStatus;
        if (GlobalGeneralData.getInstance().debug_allepisodes_unblocked)
            episodeStatus = GameConstants.CONTENT_STATUS_UNBLOCKED;
        else
            episodeStatus = GlobalPreferencesData.getInstance().getEpisodeStatus(episodeNumber);

        if(debug_unblockOnClick){
            if(unblockingEvent) episodeStatus = GameConstants.CONTENT_STATUS_UNBLOCKED;
        }
        if (episodeStatus == GameConstants.CONTENT_STATUS_BLOCKED || episodeStatus == GameConstants.CONTENT_STATUS_UNBLOCKING) {
            episodeButtonStyle = episodeBlockedButtonStyle;
        } else {
            episodeButtonStyle = new ImageButton.ImageButtonStyle();
            episodeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(episodesAtlas.findRegion("episode" + episodeNumber, 1)));
            episodeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(episodesAtlas.findRegion("episode" + episodeNumber, 2)));
            if(episodeStatus == GameConstants.CONTENT_STATUS_UNBLOCKED) {
                if (firstIncompleteEpisode == 0) {
                    firstIncompleteEpisode = episodeNumber;
                }
            }
        }

        if(episodeStatus == GameConstants.CONTENT_STATUS_UNBLOCKING)
            episodesWaitingToUnblock.add(episodeNumber);
        Group episodeButtonGroup = new Group();
        episodeButtonGroup.setSize(episodeWidth, 234);
        episodeButtonGroup.setPosition(posX, 0);
        episodeButtonGroup.setOrigin(Align.center);
        ImageButton episodeButton;
        Label episodeStarsLabel;

        episodeButton = new ImageButton(episodeButtonStyle);
        episodeButton.setPosition((episodeButtonGroup.getWidth() / 2) - (episodeButton.getWidth() / 2), episodeButton.getY());
        episodeButtonGroup.addActor(episodeButton);

        // Estrella de desafio
        if(episodeStatus != GameConstants.CONTENT_STATUS_BLOCKED && episodeStatus != GameConstants.CONTENT_STATUS_UNBLOCKING){
            int episodeChallenges = GlobalGeneralData.getInstance().getEpisodeDataByNumber(episodeNumber).getTotalChallenges();
            if(episodeChallenges > 0) {
                int challengesCompleted = GlobalPreferencesData.getInstance().getEpisodeChallengesCompleted(episodeNumber);
                if(challengesCompleted == episodeChallenges) {
                    Image challengesCompletedStar = new Image(generalAtlas.findRegion("episodeChallengeStar"));
                    challengesCompletedStar.setPosition((episodeWidth / 2) - (challengesCompletedStar.getWidth() / 2), 205);
                    episodeButtonGroup.addActor(challengesCompletedStar);
                }
            }
        }
        // Separamos las dos palabras del titulo para mostrarlas en dos labels diferentes
        String episodeTitle_a = GlobalGeneralData.getInstance().getGlobalBundleData().get("episode_" + episodeNumber + "_name");
        String episodeTitle_b = "";
        int spaceIndex = episodeTitle_a.lastIndexOf(" ");
        if(spaceIndex != -1) {
            episodeTitle_b = episodeTitle_a.substring(spaceIndex + 1);
            episodeTitle_a = episodeTitle_a.substring(0, spaceIndex);
        }

        Label episodeTitleLabel_1 = new Label(episodeTitle_a, labelStyle_gb_16);
        episodeTitleLabel_1.setPosition(MathUtils.floor((episodeButtonGroup.getWidth() / 2) - (episodeTitleLabel_1.getWidth() / 2) - 2), 190); //81
        Label episodeTitleLabel_2 = new Label(episodeTitle_b, labelStyle_gb_13);
        episodeTitleLabel_2.setPosition(MathUtils.floor((episodeButtonGroup.getWidth() / 2) - (episodeTitleLabel_2.getWidth() / 2) - 2), 175);// 81
        // Sustitucion por Labels mas pequeños si el texto en algún idioma no cupiera en el panel
        if(episodeTitleLabel_1.getWidth() > 145 || episodeTitleLabel_2.getWidth() > 145){
            episodeTitleLabel_1 = new Label(episodeTitle_a, labelStyle_gb_13);
            episodeTitleLabel_1.setPosition(MathUtils.floor((episodeButtonGroup.getWidth() / 2) - (episodeTitleLabel_1.getWidth() / 2) - 2), 193); //81
            episodeTitleLabel_2 = new Label(episodeTitle_b, labelStyle_gb_11);
            episodeTitleLabel_2.setPosition(MathUtils.floor((episodeButtonGroup.getWidth() / 2) - (episodeTitleLabel_2.getWidth() / 2) - 2), 180);// 81
        }

        episodeTitleLabel_1.setTouchable(Touchable.disabled);
        episodeTitleLabel_2.setTouchable(Touchable.disabled);

        episodeButtonGroup.addActor(episodeTitleLabel_1);
        episodeButtonGroup.addActor(episodeTitleLabel_2);
        float labelAlpha;
        if(episodeStatus != GameConstants.CONTENT_STATUS_BLOCKED && episodeStatus != GameConstants.CONTENT_STATUS_UNBLOCKING){
            labelAlpha = 0.8f;
            episodeTitleLabel_1.setColor(248 / 255f, 239 / 255f, 109 / 255f, labelAlpha);
            episodeTitleLabel_2.setColor(248 / 255f, 239 / 255f, 117 / 255f, labelAlpha);

            int episodeStarsAcquired = GlobalPreferencesData.getInstance().getEpisodeStars(episodeNumber);
            int episodeStars = GlobalGeneralData.getInstance().getEpisodeDataByNumber(episodeNumber).getTotalStars();
            labelAlpha = 0.7f;
            episodeStarsLabel = new Label(Integer.toString((episodeStarsAcquired * 100) / episodeStars) + "%", labelStyle_gb_18);
            episodeStarsLabel.setPosition(96 - MathUtils.floor(episodeStarsLabel.getWidth() / 2), 24);
            episodeStarsLabel.setColor(1, 1, 1, labelAlpha);
            episodeButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (interactionActive) {
                        interactionActive = false;
                        selectEpisode(episodeNumber);
                    }
                }
            });

            episodeButtonGroup.addActor(episodeStarsLabel);
        } else {
            //episodeTitleLabel_1.setColor(0.8f, 0.8f, 0.8f, 0.7f);
            //episodeTitleLabel_2.setColor(0.8f, 0.8f, 0.8f, 0.7f);
            episodeTitleLabel_1.setColor(248 / 255f, 239 / 255f, 109 / 255f, 0);
            episodeTitleLabel_2.setColor(248 / 255f, 239 / 255f, 117 / 255f, 0);

            if(debug_unblockOnClick) {
                episodeButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        unblockEpisode(episodeStructure);
                    }
                });
            }
        }
        allEpisodesGroup.addActor(episodeButtonGroup);
        episodeStructure.buttonGroup = episodeButtonGroup;
        if(unblockingEvent) episodeButtonGroup.setVisible(false);
    }

    private void addAlphaEffectAction(Actor actor, float alpha, float time, float delay){
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(alpha);
        alphaAction.setDuration(time);
        if(delay > 0){
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.addAction(new DelayAction(delay));
            sequenceAction.addAction(alphaAction);
            actor.addAction(sequenceAction);
        }else {
            actor.addAction(alphaAction);
        }
    }

    private void unblockEpisode(final EpisodeStructure episodeStructure){
        AudioManager.getInstance().playSound(AudioManager.SOUND.EPISODE_UNBLOCK);

        // Eliminamos el boton original, que estaba bloqueado
        episodeStructure.buttonGroup.remove();

        // Desbloqueamos el episodio y los grabamos
        if(!debug_unblockOnClick) GlobalPreferencesData.getInstance().setEpisodeStatus(episodeStructure.episodeNumber, GameConstants.CONTENT_STATUS_UNBLOCKED);

        // Añadimos el episodio ya desbloqueado a lista, aunque estará invisible
        addEpisode(episodeStructure, episodeStructure.buttonGroup.getX(), true);

        // Creamos una copia visual del episodio y hacemos la animacion de desbloqueo
        Group episodeGroup = episodeStructure.buttonGroup;
        float posX = (scrollPane.getX() - scrollPane.getScrollX()) + episodeGroup.getX() - 7;
        float posY = episodeGroup.getY() + 6;

        Image blockedImage = new Image(episodesAtlas.findRegion("episode_blocked"));
        blockedImage.setPosition(posX, posY);
        blockedImage.setOrigin(Align.center);
        groupScreen.addActor(blockedImage);

        Image unblockedImage = new Image(episodesAtlas.findRegion("episode" + Integer.toString(episodeStructure.episodeNumber), 1));
        Group unblockedImageGroup = new Group();
        unblockedImageGroup.setSize(episodeWidth, 234);
        unblockedImageGroup.setOrigin(Align.center);
        unblockedImageGroup.setPosition(posX, posY);
        unblockedImageGroup.setScale(1.2f, 1.2f);
        unblockedImageGroup.setColor(1, 1, 1, 0);
        unblockedImageGroup.addActor(unblockedImage);
        groupScreen.addActor(unblockedImageGroup);

        // Copiamos los labels para que parezca el mismo
        Label originalTitle_1 =  (Label) episodeGroup.getChildren().get(1);
        Label originalTitle_2 =  (Label) episodeGroup.getChildren().get(2);
        Label originalStarsLabel =  (Label) episodeGroup.getChildren().get(3);


        Label title_1 = new Label(originalTitle_1.getText(), originalTitle_1.getStyle());
        title_1.setColor(originalTitle_1.getColor());
        title_1.setPosition(originalTitle_1.getX() + 7, originalTitle_1.getY());
        addAlphaEffectAction(title_1, 0.7f, 0.2f, 0.2f);

        Label title_2 = new Label(originalTitle_2.getText(), originalTitle_2.getStyle());
        title_2.setColor(originalTitle_2.getColor());
        title_2.setPosition(originalTitle_2.getX() + 7, originalTitle_2.getY());
        addAlphaEffectAction(title_2, 0.7f, 0.2f, 0.2f);

        Label stars = new Label(originalStarsLabel.getText(), originalStarsLabel.getStyle());
        stars.setColor(originalStarsLabel.getColor().r, originalStarsLabel.getColor().g, originalStarsLabel.getColor().b, 0);
        stars.setPosition(originalStarsLabel.getX() + 7, originalStarsLabel.getY());
        addAlphaEffectAction(stars, 0.7f, 0.2f, 0.2f);

        unblockedImageGroup.addActor(title_1);
        unblockedImageGroup.addActor(title_2);
        unblockedImageGroup.addActor(stars);

        Image flashImage = new Image(generalAtlas.findRegion("episode_flash"));
        flashImage.setColor(1, 1, 1, 0.8f);
        unblockedImageGroup.addActor(flashImage);

        Image effectImage = new Image(generalAtlas.findRegion("episode_effect"));
        effectImage.setColor(1, 1, 1, 0);
        effectImage.setPosition((scrollPane.getX() - scrollPane.getScrollX()) + episodeGroup.getX() - 8 , episodeGroup.getY() + 4);
        effectImage.setOrigin(Align.center);
        groupScreen.addActor(effectImage);

        ScaleToAction scaleToAction;
        AlphaAction alphaAction;
        SequenceAction sequenceAction;
        ParallelAction parallelAction;
        RunnableAction runnableAction;

        // Blocked Episode
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1.2f, 1.2f);
        scaleToAction.setDuration(0.1f);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(scaleToAction);
        sequenceAction.addAction(new RemoveActorAction());
        blockedImage.addAction(sequenceAction);

        //Inblocked Episode
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1f, 1f);
        scaleToAction.setDuration(0.3f);
        scaleToAction.setInterpolation(new Interpolation.BounceOut(2));
        runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                episodeStructure.buttonGroup.setVisible(true);
            }
        });

        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(scaleToAction);
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new RemoveActorAction());

        unblockedImageGroup.addAction(sequenceAction);

        // Flash
        alphaAction = new AlphaAction();
        alphaAction.setDuration(0.2f);
        alphaAction.setAlpha(0);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.1f));
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        flashImage.addAction(sequenceAction);

        // Effecto expansivo
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.1f));
        alphaAction = new AlphaAction();
        alphaAction.setDuration(0);
        alphaAction.setAlpha(1);
        sequenceAction.addAction(alphaAction);

        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1.5f, 1.5f);
        scaleToAction.setDuration(0.4f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.4f);
        parallelAction = new ParallelAction(scaleToAction, alphaAction);

        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(new RemoveActorAction());
        effectImage.addAction(sequenceAction);
    }

    private void enterPanel(){
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - groupPanel.getWidth()) / 2), -groupPanel.getHeight());
        groupPanel.setScale(0.8f);

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), 0);
        moveToAction.setDuration(0.9f);
        moveToAction.setInterpolation(new Interpolation.Swing(0.7f));
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1.0f);
        scaleToAction.setDuration(0.6f);
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_ENTER);
            }
        });
        RunnableAction activePanelRunnable = new RunnableAction();
        activePanelRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                interactionActive = true;
            }
        });
        SequenceAction sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(moveToAction);
        sequenceAction1.addAction(activePanelRunnable);
        SequenceAction sequenceAction2 = new SequenceAction();
        sequenceAction2.addAction(new DelayAction(0.1f));
        sequenceAction2.addAction(runnableAction);
        sequenceAction2.addAction(new DelayAction(0.1f));
        sequenceAction2.addAction(scaleToAction);

        ParallelAction parallelAction = new ParallelAction(sequenceAction1, sequenceAction2);

        groupPanel.addAction(parallelAction);
    }

    private void selectEpisode(int episodeNumber){
        GlobalGeneralData.getInstance().setCurrentEpisode(episodeNumber);
        GlobalLevelData.getInstance().setBackground(GlobalGeneralData.getInstance().getEpisodesData().get(getEpisodeIndexByNumber(episodeNumber)).getBackground());

        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_LEAVES);
        GlobalGeneralData.getInstance().setCurrentLevel(0);
        selectionEventsManager.goToPanel(1);
    }

    @Override
    public void startRetractingPanels(){
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), -groupPanel.getHeight());
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.pow4In);
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(0.8f);
        scaleToAction.setDuration(0.6f);

        SequenceAction sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(moveToAction);
        sequenceAction1.addAction(new RemoveActorAction());

        ParallelAction parallelAction = new ParallelAction(sequenceAction1, scaleToAction);
        groupPanel.addAction(parallelAction);
    }

    public void goToMainIfReady(){
        if(interactionActive) {
            selectionEventsManager.addEventGoToMain();
            interactionActive = false;
        }
    }

    private int getEpisodeIndexByNumber(int episodeNumber){
        for(int i = 0 ; i < episodeStructuresList.size(); i++){
            if(episodeStructuresList.get(i).episodeNumber == episodeNumber) {
                return i;
            }
        }
        return -1;
    }

    private boolean launchDialogueAfterUnblocking(int unblockedEpisode){
        if(GlobalGeneralData.getInstance().debug_dialogsAndEvents == GlobalAttributes.ACTIVATION_CONDITION.NEVER) return false;
        String fileName = "dialog_newEpisode_" + unblockedEpisode;
        FileHandle file = Gdx.files.internal("data/dialogues/" + fileName + ".json");
        boolean hasDialogue = file.exists();
        if(hasDialogue){
            characterDialogue.launchDialogue(fileName, false, 0.8f);
        } else {
            Gdx.app.log("","Dialog not exist!!");
        }
        return hasDialogue;
    }

    private void checkAutomaticUnlockEvents(){
        // Si el episodio 1 no esta desbloqueado lo hacemos directamente
        if(GlobalPreferencesData.getInstance().getEpisodeStatus(1) == GameConstants.CONTENT_STATUS_BLOCKED) {
            episodesWaitingToUnblock.add(1);
        }

        // Comprobamos si todos los episodios completados han desbloqueado su respectivo episodio siguiente
        for(int i = 0; i < episodeData.size(); i++) {
            int episodeNumber = episodeData.get(i).getEpisodeNumber();
            if(GlobalPreferencesData.getInstance().getEpisodeStatus(episodeNumber) == GameConstants.CONTENT_STATUS_COMPLETED){
                int unblockEpisodeNumber = episodeData.get(i).getUnblockEpisodeWhenCompleting();
                if(GlobalPreferencesData.getInstance().getEpisodeStatus(unblockEpisodeNumber) == GameConstants.CONTENT_STATUS_BLOCKED){
                    episodesWaitingToUnblock.add(unblockEpisodeNumber);
                }
            }
        }

        // (i) Aquí irian los deslboqueos adicionales de eventos promocionales, festividades, etc
        // . . .
    }

    private void LaunchEpisodeSpecialEvent(GlobalAttributes.EPISODE_SPECIAL_EVENT specialEvent){
        if(specialEvent == GlobalAttributes.EPISODE_SPECIAL_EVENT.BASE_EPISODES_COMPLETED) {
            characterDialogue.launchDialogue("dialog_baseEpisodes_completed", false, 1.5f);
            panelEvent = PANEL_EVENT.SHOWING_DIALOGUE;
        }
        GlobalGeneralData.getInstance().episodeSpecialEvent = GlobalAttributes.EPISODE_SPECIAL_EVENT.NONE;
    }

    private enum PANEL_EVENT {
        NONE, ACTUALIZING, MOVING, MOVING_TO_UNBLOCK, UNBLOCKING, SHOWING_DIALOGUE
    }
}
