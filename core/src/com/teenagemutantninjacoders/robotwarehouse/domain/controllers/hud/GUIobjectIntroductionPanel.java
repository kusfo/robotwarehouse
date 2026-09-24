package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.SelectionEventsManager;

/**
 * Created by JordiRM on 10/11/2019.
 */
public class GUIobjectIntroductionPanel extends GUIobjectSelectionPanel{
    private SelectionEventsManager selectionEventsManager;
    private GUIobjectCharacterDialogue characterDialogue;
    private TextureAtlas introAtlas, selectionAtlas;
    private Label.LabelStyle labelStyle_bb_14;
    private Group layer;
    private Group groupPanel, groupScreen;
    private Group scenesGroup, infoPanelGroup;
    private ScrollPane scrollPane;
    private boolean panelActive = true;

    public GUIobjectIntroductionPanel(Group layer, SelectionEventsManager selectionEventsManager, GUIobjectCharacterDialogue characterDialogue){
        this.layer = layer;
        this.selectionEventsManager = selectionEventsManager;
        this.characterDialogue = characterDialogue;
        introAtlas = Assets.getTextureAtlas("intro_general");
        selectionAtlas = Assets.getTextureAtlas("levelSelection_general");
        labelStyle_bb_14 = new Label.LabelStyle();
        labelStyle_bb_14.font = Assets.getFont("f_base_bb_14");
        execute();
    }

    private void execute() {
        int width = selectionAtlas.findRegion("frame").getRegionWidth();
        int height = selectionAtlas.findRegion("frame").getRegionHeight();
        groupPanel = new Group();
        groupPanel.setSize(width, height);
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - width) / 2), 10);
        groupPanel.setVisible(false);
        int screenWidth = selectionAtlas.findRegion("screenLines").getRegionWidth();
        int screenHeight = selectionAtlas.findRegion("screenLines").getRegionHeight();

        groupScreen = new Group();
        groupScreen.setSize(screenWidth, screenHeight);
        groupScreen.setPosition(107, 28);

        AnimatedImageActor animatedImageScreenLines= new AnimatedImageActor(selectionAtlas, "screenLines", 0.1f, Animation.PlayMode.LOOP);
        animatedImageScreenLines.setPosition(107, 28);
        animatedImageScreenLines.setTouchable(Touchable.disabled);

        animatedImageScreenLines.play();
        Image panelImage = new Image(selectionAtlas.findRegion("frame"));
        panelImage.setTouchable(Touchable.disabled);

        scenesGroup = new Group();
        scenesGroup.setSize(groupScreen.getWidth(), groupScreen.getHeight());
        scenesGroup.addActor(getLogoScene());

        scrollPane = new ScrollPane(scenesGroup);
        scrollPane.setSize(groupScreen.getWidth(), groupScreen.getHeight());
        scrollPane.setFlingTime(0.3f);
        scrollPane.setOverscroll(false, false);
        groupScreen.addActor(scrollPane);

        groupPanel.addActor(groupScreen);
        groupPanel.addActor(animatedImageScreenLines);
        groupPanel.addActor(panelImage);

        layer.addActor(groupPanel);
        panelLoaded = true;
    }

    @Override
    public void update(float delta){
        if(panelActive) {
            if (characterDialogue.hasDialogueActive())
                processDialogueEvents();
            else {
                exitPanel();
                exitInfoPanel();
                panelActive = false;
            }
        }
    }

    @Override
    public void startDeployingPanels(){
        groupPanel.setVisible(true);
        characterDialogue.launchDialogue("dialog_intro", false, 3.0f);
        enterPanel();
        // Música escena
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playMusic(AudioManager.MUSIC.SCENE_GENERIC_01, true, true, 1f);
            }
        });
        groupPanel.addAction(new SequenceAction(new DelayAction(2.0f), runnableAction));
    }

    private Group getLogoScene(){
        Group sceneLogoGroup = new Group();
        sceneLogoGroup.setSize(groupScreen.getWidth(), groupScreen.getHeight());
        Image logoBackground = new Image(introAtlas.findRegion("background_logo"));

        Image decoLeftImage = new Image(introAtlas.findRegion("orc_lines_l"));
        decoLeftImage.setPosition(-decoLeftImage.getWidth(),120);
        decoLeftImage.setColor(1, 1, 1, 0);
        Image decoRightImage = new Image(introAtlas.findRegion("orc_lines_r"));
        decoRightImage.setColor(1, 1, 1, 0);
        decoRightImage.setPosition(groupScreen.getWidth(),130);

        Image orcImage = new Image(introAtlas.findRegion("orc"));
        orcImage.setPosition((sceneLogoGroup.getWidth() / 2) - (orcImage.getWidth() / 2), 170 - (orcImage.getHeight() / 2));
        orcImage.setColor(1, 1, 1, 0);

        Image omniImage = new Image(introAtlas.findRegion("omni"));
        Image roboticsImage = new Image(introAtlas.findRegion("robotics"));
        Image corporationImage = new Image(introAtlas.findRegion("corporation"));

        subtextEnter(omniImage, 35, orcImage.getY() - 42, 0);
        subtextEnter(roboticsImage, 35 + omniImage.getWidth() + 4, orcImage.getY() - 42, 0.2f);
        subtextEnter(corporationImage, 35 + omniImage.getWidth() + roboticsImage.getWidth() + 8, orcImage.getY() - 42, 0.4f);

        SequenceAction sequenceAction = new SequenceAction();
        AlphaAction alphaAction;
        MoveToAction moveToAction;

        // ORC
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.9f);
        sequenceAction.addAction(new DelayAction(0.6f));
        sequenceAction.addAction(alphaAction);
        orcImage.addAction(sequenceAction);

        // Decoraciones
        moveToAction = new MoveToAction();
        moveToAction.setPosition(0, decoLeftImage.getY());
        moveToAction.setDuration(0.5f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.5f);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.9f));
        sequenceAction.addAction(new ParallelAction(moveToAction, alphaAction));
        decoLeftImage.addAction(sequenceAction);

        moveToAction = new MoveToAction();
        moveToAction.setPosition(sceneLogoGroup.getWidth() - decoRightImage.getWidth(), decoRightImage.getY());
        moveToAction.setDuration(0.5f);
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.5f);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.9f));
        sequenceAction.addAction(new ParallelAction(moveToAction, alphaAction));
        decoRightImage.addAction(sequenceAction);


        // General (sonido de noticias)
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.NEWS_SHORT);
            }
        });
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.3f));
        sequenceAction.addAction(runnableAction);
        sceneLogoGroup.addAction(sequenceAction);

        sceneLogoGroup.addActor(logoBackground);
        sceneLogoGroup.addActor(decoLeftImage);
        sceneLogoGroup.addActor(decoRightImage);
        sceneLogoGroup.addActor(orcImage);
        sceneLogoGroup.addActor(omniImage);
        sceneLogoGroup.addActor(roboticsImage);
        sceneLogoGroup.addActor(corporationImage);

        return sceneLogoGroup;
    }

    private Group getSpaceScene(){
        Group sceneLogoGroup = new Group();
        sceneLogoGroup.setSize(groupScreen.getWidth(), groupScreen.getHeight());
        sceneLogoGroup.setColor(1, 1, 1, 0);
        Image spaceBackground = new Image(introAtlas.findRegion("background_space"));

        Image warehouseImage = new Image(introAtlas.findRegion("warehouse"));
        warehouseImage.setPosition(330, -20);
        warehouseImage.setScale(0.9f, 0.9f);

        Image warehouseSignImage = new Image(introAtlas.findRegion("warehouseSign"));
        warehouseSignImage.setPosition(115, -30);

        MoveToAction moveToAction;
        ScaleToAction scaleToAction;
        SequenceAction sequenceAction;

        // Aparicion escena
        AlphaAction alphaAction;
        alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(1.5f);
        sceneLogoGroup.addAction(alphaAction);

        // Cartel
        moveToAction = new MoveToAction();
        moveToAction.setPosition(130, -20);
        moveToAction.setDuration(10.0f);
        moveToAction.setInterpolation(Interpolation.circleOut);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(moveToAction);
        warehouseSignImage.addAction(sequenceAction);

        //Almacen
        moveToAction = new MoveToAction();
        moveToAction.setPosition(310, -30);
        moveToAction.setDuration(10.0f);
        moveToAction.setInterpolation(Interpolation.circleOut);
        scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1, 1);
        scaleToAction.setDuration(10.0f);
        moveToAction.setInterpolation(Interpolation.circleOut);
        sequenceAction = new SequenceAction();
        sequenceAction.addAction(new ParallelAction(moveToAction, scaleToAction));
        warehouseImage.addAction(sequenceAction);

        sceneLogoGroup.addActor(spaceBackground);
        sceneLogoGroup.addActor(warehouseImage);
        sceneLogoGroup.addActor(warehouseSignImage);

        return sceneLogoGroup;
    }

    private void subtextEnter(Actor textActor , float posx, float posy, float pause){
        textActor.setPosition(posx + 500, posy);
        pause += 1.2f;

        MoveToAction moveToAction;
        moveToAction = new MoveToAction();
        moveToAction.setPosition(posx, posy);
        moveToAction.setDuration(0.3f);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(pause));
        sequenceAction.addAction(moveToAction);

        textActor.addAction(sequenceAction);
    }

    private void createInfoPanel(){
        infoPanelGroup = new Group();
        Image frame = new Image(introAtlas.findRegion("frame_info"));
        float panelHeight = frame.getHeight();
        float panelWidth = frame.getWidth();
        infoPanelGroup.setSize(panelWidth, frame.getHeight());
        infoPanelGroup.setPosition( (GameConstants.HORIZONTAL_RESOLUTION - panelWidth) / 2, -panelHeight);

        AnimatedImageActor screen = new AnimatedImageActor(introAtlas, "screen_info", 0.1f, Animation.PlayMode.LOOP);
        screen.play();

        Group screenGroup = new Group();
        screenGroup.setPosition(22, 55);
        screenGroup.setSize(screen.getWidth(), screen.getHeight());

        Label episodeLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("intro_infopanel"), labelStyle_bb_14);

        episodeLabel.setPosition(MathUtils.floor((screenGroup.getWidth() / 2) - (episodeLabel.getWidth() / 2)), 2);
        episodeLabel.setColor(1, 1, 1, 0.75f);

        screenGroup.addActor(screen);
        screenGroup.addActor(episodeLabel);

        infoPanelGroup.addActor(screenGroup);
        infoPanelGroup.addActor(frame);
        layer.addActor(infoPanelGroup);

        enterInfoPanel();
    }

    private void enterPanel(){
        groupPanel.setPosition(((GameConstants.HORIZONTAL_RESOLUTION - groupPanel.getWidth()) / 2), GameConstants.VERTICAL_RESOLUTION);
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), 10);
        moveToAction.setDuration(0.9f);
        moveToAction.setInterpolation(new Interpolation.Swing(0.7f));
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

        ParallelAction parallelAction = new ParallelAction(sequenceAction1, sequenceAction2);
        groupPanel.addAction(parallelAction);
    }

    private void exitPanel(){
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_LEAVES);
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(groupPanel.getX(), GameConstants.VERTICAL_RESOLUTION);
        moveToAction.setDuration(0.5f);
        moveToAction.setInterpolation(Interpolation.sineIn);
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                selectionEventsManager.goToPanel(0);
            }
        });
        SequenceAction sequenceAction = new SequenceAction();
        //sequenceAction.addAction(new DelayAction(0.2f));
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(moveToAction);
        sequenceAction.addAction(new RemoveActorAction());
        groupPanel.addAction(sequenceAction);
    }

    private void enterInfoPanel(){
        SequenceAction sequenceAction = new SequenceAction();
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(infoPanelGroup.getX(), -40);
        moveToAction.setDuration(0.65f);
        moveToAction.setInterpolation( new Interpolation.Swing(1.7f));
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_ENTER);
            }
        });
        sequenceAction.addAction(new DelayAction(0.5f));
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(moveToAction);
        infoPanelGroup.addAction(sequenceAction);
    }

    private void exitInfoPanel(){
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(infoPanelGroup.getX(), -infoPanelGroup.getHeight());
        moveToAction.setDuration(0.35f);
        moveToAction.setInterpolation(Interpolation.pow4In);
        infoPanelGroup.addAction(moveToAction);
    }

    private void activeSpaceScene(){
        scenesGroup.addActor(getSpaceScene());
        createInfoPanel();
    }

    private void processDialogueEvents(){
        if (characterDialogue.getAwaitingDialogueEvents().size() > 0) {
            GUIobjectCharacterDialogue.DIALOGUE_EVENT event = characterDialogue.getAwaitingDialogueEvents().get(0).getEvent();
            switch (event) {
                case ADVANCE:
                    activeSpaceScene();
                    break;
            }
            characterDialogue.getAwaitingDialogueEvents().remove(0);
        }
    }
}
