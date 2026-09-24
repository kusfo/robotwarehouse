package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 25/01/2021.
 */
class GUIobjectTutorialsMenuPanel extends GUIobjectBaseLevelPanel {
    private Group layer;
    private Group groupTutorialsFramePanel, groupTutorialsScreenPanel, blockingBackgroundGroup;
    private final Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_13;
    private int screenWidth,screenHeight;
    private final Color c_title, c_button;
    private final TextureAtlas atlas;
    private Image arrowUpImage, arrowDownImage;
    private Table table;
    private ScrollPane scrollPane;
    private int actualTutorialIndex;
    private int totalTutorials;
    private final int tutorialSpaceY = 7;
    private final int tutorialHeight = 30;
    private boolean enteringTutorialFocusDone = false;
    public boolean tutorialSelected = false;
    public TUTORIAL_ZONE tutorialZone;
    private GlobalPreferencesData pref = GlobalPreferencesData.getInstance();
    private boolean debug_viewAllTutorials = false;
    public GUIobjectTutorialsMenuPanel(Group layer){
        this.layer = layer;
        atlas = Assets.getTextureAtlas("tutorialsMenulPanel");
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_13 = new Label.LabelStyle();
        labelStyle_base_gb_13.font = Assets.getFont("f_base_gb_13");

        c_title = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_button = new Color(160/ 255f, 212/ 255f, 131/ 255f, 0.85f);

        execute();
    }

    private void execute(){
        addBlockingBackground();

        Image imageTutorialsFramePanel = new Image(atlas.findRegion("TutorialsPanelFrame"));
        imageTutorialsFramePanel.setPosition(0, 0);
        imageTutorialsFramePanel.setTouchable(Touchable.disabled);

        AnimatedImageActor animatedImageTutorialsScreen = new AnimatedImageActor(atlas, "TutorialsPanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageTutorialsScreen.play();
        animatedImageTutorialsScreen.setTouchable(Touchable.disabled);

        width = (int) imageTutorialsFramePanel.getWidth();
        height = (int) imageTutorialsFramePanel.getHeight();
        screenWidth = (int) animatedImageTutorialsScreen.getWidth();
        screenHeight = (int) animatedImageTutorialsScreen.getHeight();
        groupTutorialsFramePanel = new Group();
        groupTutorialsFramePanel.setSize(width, height);
        groupTutorialsFramePanel.setPosition((GameConstants.HORIZONTAL_RESOLUTION / 2)  - (width / 2), GameConstants.VERTICAL_RESOLUTION + 100);

        groupTutorialsScreenPanel = new Group();
        groupTutorialsScreenPanel.setSize(screenWidth, screenHeight);
        groupTutorialsScreenPanel.setPosition(28, 68);

        groupTutorialsScreenPanel.addActor(animatedImageTutorialsScreen);

        Label labelTutorialsTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorials_title"), labelStyle_base_gb_22);
        labelTutorialsTitle.setColor(c_title);
        labelTutorialsTitle.setPosition((screenWidth / 2) - (labelTutorialsTitle.getWidth() / 2), 231); //204

        groupTutorialsScreenPanel.addActor(labelTutorialsTitle);

        arrowUpImage = new Image(atlas.findRegion("TutorialsArrowUp"));
        arrowUpImage.setPosition((screenWidth / 2) - (arrowUpImage.getWidth() / 2), 215);
        arrowDownImage = new Image(atlas.findRegion("TutorialsArrowDown"));
        arrowDownImage.setPosition((screenWidth / 2) - (arrowDownImage.getWidth() / 2), 10);

        // Boton OK
        TextureAtlas generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");
        TextButton.TextButtonStyle nextButtonStyle = new TextButton.TextButtonStyle();
        nextButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 1)));
        nextButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 2)));
        nextButtonStyle.font = Assets.getFont("f_base_gb_22");
        nextButtonStyle.fontColor = c_button;
        TextButton okButton = new TextButton(GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorial_ok"), nextButtonStyle);
        okButton.setPosition(139,25); //119,25
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                if(activePanel) {
                    exitPanel();
                }
            }
        });

        // Llenamos la tabla con los creditos
        table = new Table();
        addTutorialsToTable();
        table.pack();

        scrollPane = new ScrollPane(table);
        scrollPane.setPosition(22, 32); //0,30
        scrollPane.setSize(340, 180);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlingTime(0.3f);
        scrollPane.setFlingTime(0.3f);

        groupTutorialsScreenPanel.addActor(scrollPane);
        groupTutorialsScreenPanel.addActor(arrowUpImage);
        groupTutorialsScreenPanel.addActor(arrowDownImage);
        groupTutorialsFramePanel.addActor(groupTutorialsScreenPanel);
        groupTutorialsFramePanel.addActor(imageTutorialsFramePanel);
        groupTutorialsFramePanel.addActor(okButton);

        layer.addActor(groupTutorialsFramePanel);

        if(!GlobalGeneralData.getInstance().eventReviewModeActivated)
            enter(groupTutorialsFramePanel, 0.15f);
        else
        {
            groupTutorialsFramePanel.setPosition(groupTutorialsFramePanel.getX(), finalPositionY);
            arrived();
        }

        // Cada vez que entramos en el menu, desactivamos el modo, por si estabamos volviendo de una visualización
        GlobalGeneralData.getInstance().eventReviewModeActivated = false;
    }

    public void update(float delta){
        // Al entrar, intentamos que muestre de manera destacada el último tutorial que hemos visto
        if(!enteringTutorialFocusDone){
            scrollPane.updateVisualScroll();
            FocusOnTutorial(GlobalGeneralData.getInstance().lastReviwingEventNumer);
            enteringTutorialFocusDone = true;
        }

        int tutorialOptionHeight = tutorialHeight + tutorialSpaceY;
        actualTutorialIndex = MathUtils.clamp( MathUtils.floor((scrollPane.getScrollY() + (tutorialOptionHeight / 2)) / tutorialOptionHeight), 0, 999);

        if(actualTutorialIndex == 0){
            if(arrowUpImage.isVisible()) arrowUpImage.setVisible(false);
        } else {
            if(!arrowUpImage.isVisible()) arrowUpImage.setVisible(true);
        }

        if(actualTutorialIndex >= totalTutorials - 5){
            if(arrowDownImage.isVisible()) arrowDownImage.setVisible(false);
        } else {
            if(!arrowDownImage.isVisible()) arrowDownImage.setVisible(true);
        }

        // Desplazamiento automatico para centrar los elementos
        if (!scrollPane.isPanning()) {
            if (scrollPane.getScrollY() > (actualTutorialIndex * tutorialOptionHeight)) {
                if (scrollPane.getScrollY() <= ((actualTutorialIndex * tutorialOptionHeight) + (tutorialOptionHeight / 2))) {
                    scrollPane.setScrollY(scrollPane.getScrollY() - (4000 * delta));
                    if (scrollPane.getScrollY() <= (actualTutorialIndex * tutorialOptionHeight)) {
                        scrollPane.setScrollY(actualTutorialIndex * tutorialOptionHeight);
                    }
                }
            } else {
                scrollPane.setScrollY(scrollPane.getScrollY() + (4000 * delta));
                if (scrollPane.getScrollY() > (actualTutorialIndex * tutorialOptionHeight)) {
                    scrollPane.setScrollY(actualTutorialIndex * tutorialOptionHeight);
                }
            }
        }

        backAction();
    }

    private void FocusOnTutorial(int tutorialNumber){
        int centerPositionY = (tutorialHeight + tutorialSpaceY) * 3;
        scrollPane.setScrollY( (tutorialNumber * (tutorialHeight + tutorialSpaceY)) - centerPositionY );
        scrollPane.updateVisualScroll();
    }

    private void addBlockingBackground(){
        blockingBackgroundGroup = new Group();
        blockingBackgroundGroup.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        blockingBackgroundGroup.setColor(1, 1, 1, 0);
        Image blackImage = new Image(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        blackImage.setColor(Color.BLACK);
        blackImage.setSize(blockingBackgroundGroup.getWidth(), blockingBackgroundGroup.getHeight());
        blockingBackgroundGroup.addActor(blackImage);

        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0.4f);
        alphaAction.setDuration(0.7f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.2f));
        sequenceAction.addAction(alphaAction);
        blockingBackgroundGroup.addAction(sequenceAction);

        layer.addActor(blockingBackgroundGroup);
    }

    private void removeBlockingBackground(){
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.7f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(alphaAction);
        sequenceAction.addAction(new RemoveActorAction());
        blockingBackgroundGroup.addAction(sequenceAction);
    }

    private void addTutorialsToTable(){
        addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 1, "event_firstLevelTutorial"); // Conceptos básicos 1
        addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 2, "event_secondLevelTutorial"); // Conceptos básicos 2
        addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 6, "event_restartLevelTutorial"); // Reiniciar nivel en encalles
        addTutorialToTable(TUTORIAL_ZONE.MAP, 1, 9, "dialog_levelSelection_challenges_basic"); // Sectores con desafío

        addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 7, "event_LosingBoxesTutorial_1"); // Pérdida de cajas
        //addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 5, "event_stopBoxesTutorial"); // Apoyos con cajas
        addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 11, "event_surplusBoxesTutorial"); // Cajas sobrantes

        //addTutorialToTable(TUTORIAL_ZONE.MAP, 1, 7, "dialog_levelSelection_ramification"); // Sectores enlazados
        //addTutorialToTable(TUTORIAL_ZONE.GAME, 1, 7, "event_trapDoorsTutorial"); // Trampillas de suelo

        //addTutorialToTable(TUTORIAL_ZONE.MAP, 1, 11, "dialog_levelSelection_lastSector"); // Último sector

        //addTutorialToTable(TUTORIAL_ZONE.GAME, 2, 1, "event_firstRobotsTutorial"); // Robots almaceneros
        addTutorialToTable( TUTORIAL_ZONE.GAME, 2, 3, "event_floorPointsTutorial"); // Puntos recolectables
        //addTutorialToTable(TUTORIAL_ZONE.GAME, 2, 5, "event_firstExplosiveRobotsTutorial"); // Robots explosivos

        addTutorialToTable(TUTORIAL_ZONE.MAP, 2, 9, "dialog_levelSelection_starSector"); // Sectores estrella
        addTutorialToTable(TUTORIAL_ZONE.GAME, 2, 9, "event_improveScoreTutorial"); // Conseguir estrellas

        addTutorialToTable(TUTORIAL_ZONE.GAME, 2, 13, "event_floorPowersTutorial"); // Activar ventajas

        addTutorialToTable(TUTORIAL_ZONE.GAME, 3, 1, "event_firstCannonTutorial"); // Torretas
        addTutorialToTable(TUTORIAL_ZONE.GAME, 3, 6, "event_firstBarriersTutorial"); // Barreras
        addTutorialToTable(TUTORIAL_ZONE.GAME, 3, 10, "event_firstButtonsTutorial"); // Botones genéricos
        addTutorialToTable(TUTORIAL_ZONE.GAME, 3, 15, "event_boxButtonsTutorial"); // Botones exclusivos

        addTutorialToTable(TUTORIAL_ZONE.GAME, 4, 4, "event_rotatingFloorsTutorial"); // Baldosas rotatorias
        addTutorialToTable(TUTORIAL_ZONE.GAME, 4, 16, "event_energyBarriersTutorial"); // Barreras de energia

    }

    private void addTutorialToTable(final TUTORIAL_ZONE buttonTutorialZone, final int episode, final int level, String prefString){
        boolean disabled = false;

        if(!debug_viewAllTutorials) {
            if (buttonTutorialZone == TUTORIAL_ZONE.GAME) {
                if (!pref.getLevelIntroductionEventViewStatus(prefString))
                    disabled = true;
            } else if (buttonTutorialZone == TUTORIAL_ZONE.MAP) {
                if (!pref.getDialogueViewStatus(prefString))
                    disabled = true;
            }
        }
        String tutorialName;
        if(disabled) {
            tutorialName = "? ? ? ?";
        } else
            tutorialName = GlobalGeneralData.getInstance().getGlobalBundleData().get(prefString + "_title");

        TextButton.TextButtonStyle tutorialButtonStyle = new TextButton.TextButtonStyle();
        tutorialButtonStyle.up = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("TutorialsOptionFrame", 1)));
        tutorialButtonStyle.down = new TextureRegionDrawable(new TextureRegion(atlas.findRegion("TutorialsOptionFrame", 2)));
        tutorialButtonStyle.font = Assets.getFont("f_base_gb_13");
        final TextButton tutorialButton = new TextButton(tutorialName, tutorialButtonStyle);

        tutorialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (activePanel) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    jumpToGameLevel(episode, level);
                    activePanel = false;
                    tutorialZone = buttonTutorialZone;
                    GlobalGeneralData.getInstance().lastReviwingEventNumer = table.getCell(tutorialButton).getRow() + 1;
                }
            }
        });

        if(disabled) {
            tutorialButton.setTouchable(Touchable.disabled);
            tutorialButtonStyle.fontColor =  new Color(0.65f, 0.65f, 0.65f, 0.70f);
        } else {
            tutorialButtonStyle.fontColor =  new Color(1, 1, 1, 0.75f);
        }
        table.add(tutorialButton).padBottom(tutorialSpaceY);
        table.row();

        totalTutorials++;
    }

    private void jumpToGameLevel(int episode, int level){
        GlobalGeneralData.getInstance().setCurrentEpisode(episode);
        GlobalGeneralData.getInstance().setCurrentLevel(level);
        GlobalGeneralData.getInstance().eventReviewModeActivated = true;
        tutorialSelected = true;
    }

    private void exitPanel(){
        leave(groupTutorialsFramePanel);
        removeBlockingBackground();
    }

    private void backAction() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (activePanel){
                exitPanel();
            }
        }
    }

    public enum TUTORIAL_ZONE{
        GAME, MAP
    }
}
