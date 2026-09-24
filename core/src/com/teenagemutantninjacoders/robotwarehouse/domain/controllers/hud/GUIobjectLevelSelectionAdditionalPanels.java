package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.auxiliary.LevelSelectionData;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 03/08/2018.
 */
public class GUIobjectLevelSelectionAdditionalPanels {

    private LevelSelectionData levelsData;
    private Group stageLayer;
    private boolean backOrderActivated = false;
    private boolean touchActivated = false;
    private boolean titleReady, infoReady, backReady;
    private Label.LabelStyle labelStyle_gb_16;
    private Group titlePanelGroup, infoPanelsGroup[], backButtonGroup;
    private TextureAtlas generalAtlas;
    private Color c_titulo;
    private float titleScreenWidth, infoScreenWidth;
    private Label episodeLabel, challengesScoreLabel, starsScoreLabel;
    private GlobalPreferencesData gameData;

    public GUIobjectLevelSelectionAdditionalPanels (LevelSelectionData levelsData, Group stageLayer){
        this.levelsData = levelsData;
        this.stageLayer = stageLayer;
        generalAtlas = Assets.getTextureAtlas("levelSelection_general");
        labelStyle_gb_16 = new Label.LabelStyle();
        labelStyle_gb_16.font = Assets.getFont("f_base_gb_16");
        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        gameData = GlobalPreferencesData.getInstance();

        // Creamos los paneles fuera de la pantalla para desplegarlos cuando queramos
        createTitlePanel();
        createInfoPanels();
        createBackButton();
    }

    private void createTitlePanel(){
        titlePanelGroup = new Group();
        Image frame = new Image(generalAtlas.findRegion("frame_title"));
        float panelWidth = frame.getWidth();
        titlePanelGroup.setSize(panelWidth, frame.getHeight());
        titlePanelGroup.setPosition( (GameConstants.HORIZONTAL_RESOLUTION - panelWidth) / 2, GameConstants.VERTICAL_RESOLUTION);

        AnimatedImageActor screen = new AnimatedImageActor(generalAtlas, "screen_title", 0.1f, Animation.PlayMode.LOOP);
        screen.play();

        Group screenGroup = new Group();
        screenGroup.setPosition(22, 23);
        screenGroup.setSize(screen.getWidth(), screen.getHeight());
        titleScreenWidth = screenGroup.getWidth();

        episodeLabel = new Label("Nothing", labelStyle_gb_16);
        episodeLabel.setPosition(MathUtils.floor((screenGroup.getWidth() / 2) - (episodeLabel.getWidth() / 2)), 1);
        episodeLabel.setColor(c_titulo);

        screenGroup.addActor(screen);
        screenGroup.addActor(episodeLabel);

        titlePanelGroup.addActor(screenGroup);
        titlePanelGroup.addActor(frame);
        stageLayer.addActor(titlePanelGroup);
    }

    private void createInfoPanels(){
        infoPanelsGroup = new Group[2];
        int panel = 0;
        float initialPosX;

        do {
            infoPanelsGroup[panel] = new Group();
            Image frame = new Image(generalAtlas.findRegion("frame_stars"));
            float panelWidth = frame.getWidth();
            infoPanelsGroup[panel].setSize(panelWidth, frame.getHeight());

            if(panel == 0)  {
                initialPosX = 5 - 100;
                infoPanelsGroup[panel].setPosition(initialPosX, -infoPanelsGroup[panel].getHeight() - 15);
            } else {
                initialPosX = GameConstants.HORIZONTAL_RESOLUTION - infoPanelsGroup[panel].getWidth() - 5 + 100;
                infoPanelsGroup[panel].setPosition(initialPosX, -infoPanelsGroup[panel].getHeight() - 15);
            }

            AnimatedImageActor screen = new AnimatedImageActor(generalAtlas, "screen_stars", 0.1f, Animation.PlayMode.LOOP);
            screen.play();

            Group screenGroup = new Group();
            screenGroup.setPosition(22, 55);
            screenGroup.setSize(screen.getWidth(), screen.getHeight());
            screenGroup.addActor(screen);

            if(panel == 0){
                starsScoreLabel = new Label("0/0", labelStyle_gb_16);
                starsScoreLabel.setPosition(100 - (starsScoreLabel.getWidth() / 2), 0);
                starsScoreLabel.setColor(1, 1, 1, 0.85f);
                infoScreenWidth = screenGroup.getWidth();
                screenGroup.addActor(starsScoreLabel);
            }
            else{
                challengesScoreLabel = new Label("0/0", labelStyle_gb_16);
                challengesScoreLabel.setPosition(screenGroup.getWidth() - 100 - (challengesScoreLabel.getWidth() / 2), 0);
                challengesScoreLabel.setColor(1, 1, 1, 0.85f);
                screenGroup.addActor(challengesScoreLabel);
            }

            Image iconImage;
            if(panel == 0) {
                iconImage = new Image(generalAtlas.findRegion("rankStar"));
                iconImage.setPosition(5, 39);
            }else{
                iconImage = new Image(generalAtlas.findRegion("challengeStar"));
                iconImage.setPosition(135, 39);
            }

            infoPanelsGroup[panel].addActor(screenGroup);
            infoPanelsGroup[panel].addActor(frame);
            infoPanelsGroup[panel].addActor(iconImage);
            stageLayer.addActor(infoPanelsGroup[panel]);

            panel++;
        } while (panel < 2);
    }

    private void createBackButton(){
        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("buttonBack", 1)));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(generalAtlas.findRegion("buttonBack", 2)));

        backButtonGroup = new Group();
        Image buttonArm = new Image(generalAtlas.findRegion("backButtonArm"));
        buttonArm.setTouchable(Touchable.disabled);

        backButtonGroup.setSize(buttonArm.getWidth(), buttonArm.getHeight());
        backButtonGroup.setOrigin(6, 34);

        backButtonGroup.setPosition(-128, GameConstants.VERTICAL_RESOLUTION - 125);
        backButtonGroup.setRotation(70);

        ImageButton backButton = new ImageButton(buttonStyle);
        backButton.setPosition(53, 21);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!backOrderActivated && touchActivated) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    backOrderActivated = true;
                }
            }
        });
        backButtonGroup.addActor(buttonArm);
        backButtonGroup.addActor(backButton);

        stageLayer.addActor(backButtonGroup);
    }

    private void deployTitlePanel(boolean withAnimation){
        if(withAnimation) {
            MoveToAction moveToAction = new MoveToAction();
            moveToAction.setPosition(titlePanelGroup.getX(), GameConstants.VERTICAL_RESOLUTION - 70);
            moveToAction.setDuration(0.7f);
            moveToAction.setInterpolation(new Interpolation.Swing(1.7f));
            RunnableAction runnableReady = new RunnableAction();
            runnableReady.setRunnable(new Runnable() {
                @Override
                public void run() {
                    titleReady = true;
                }
            });
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.addAction(moveToAction);
            sequenceAction.addAction(runnableReady);
            titlePanelGroup.addAction(sequenceAction);
        }else{
            titlePanelGroup.setPosition(titlePanelGroup.getX(), GameConstants.VERTICAL_RESOLUTION - 70);
            titleReady = true;
        }
    }

    private void deployInfoPanels(boolean withAnimation){
        int panel = 0;
        float finalPosX;
        do {
            if(withAnimation) {
                if (panel == 0) finalPosX = 5;
                else            finalPosX = GameConstants.HORIZONTAL_RESOLUTION - infoPanelsGroup[panel].getWidth() - 5;

                // Acciones
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setPosition(finalPosX, -37);
                moveToAction.setDuration(0.7f);
                moveToAction.setInterpolation(new Interpolation.Swing(1.7f));
                RunnableAction runnableSound = new RunnableAction();
                runnableSound.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_ENTER);
                    }
                });
                RunnableAction runnableReady = new RunnableAction();
                runnableReady.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        infoReady = true;
                    }
                });
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(new DelayAction(0.1f));
                if (panel == 0) sequenceAction.addAction(runnableSound);
                sequenceAction.addAction(moveToAction);
                sequenceAction.addAction(runnableReady);
                infoPanelsGroup[panel].addAction(sequenceAction);
            } else {
                if(panel == 0) infoPanelsGroup[panel].setPosition(5, -37);
                else  infoPanelsGroup[panel].setPosition(GameConstants.HORIZONTAL_RESOLUTION - infoPanelsGroup[panel].getWidth() - 5, -37);
                infoReady = true;
            }
            panel++;
        } while (panel < 2);
    }

    private void deployBackButton(boolean withAnimation){
        if(withAnimation) {
            // Posición inicial
            backButtonGroup.setPosition(-128, GameConstants.VERTICAL_RESOLUTION - 125);
            backButtonGroup.setRotation(70);

            SequenceAction sequenceAction = new SequenceAction();
            MoveToAction move = new MoveToAction();
            move.setPosition(-35, GameConstants.VERTICAL_RESOLUTION - 85);
            move.setDuration(0.7f);
            move.setInterpolation(new Interpolation.Swing(0.7f));
            RotateByAction rotate = new RotateByAction();
            rotate.setAmount(-70);
            rotate.setDuration(0.6f);
            rotate.setInterpolation(new Interpolation.Swing(0.7f));
            RotateToAction rotateToFinalPosition = new RotateToAction();
            rotateToFinalPosition.setRotation(0);
            rotateToFinalPosition.setDuration(0);
            RunnableAction runnableAction = new RunnableAction();
            runnableAction.setRunnable(new Runnable() {
                @Override
                public void run() {
                    touchActivated = true;
                    backReady = true;
                }
            });
            ParallelAction parallelAction = new ParallelAction(move, rotate);
            sequenceAction.addAction(parallelAction);
            sequenceAction.addAction(rotateToFinalPosition);
            sequenceAction.addAction(runnableAction);
            backButtonGroup.addAction(sequenceAction);
        } else {
            backButtonGroup.setPosition(-35, GameConstants.VERTICAL_RESOLUTION - 85);
            backButtonGroup.setRotation(0);
            touchActivated = true;
            backReady = true;
        }
    }

    private void retractTitlePanel(){
        titleReady = false;
        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(titlePanelGroup.getX(), GameConstants.VERTICAL_RESOLUTION);
        moveToAction.setDuration(0.35f);
        moveToAction.setInterpolation(Interpolation.pow4In);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(moveToAction);
        titlePanelGroup.addAction(sequenceAction);
    }

    private void retractInfoPanels(){
        infoReady = false;
        int panel = 0;
        do {
            MoveToAction moveToAction = new MoveToAction();
            if(panel == 0) moveToAction.setPosition(infoPanelsGroup[panel].getX() - 100, -infoPanelsGroup[panel].getHeight() - 15);
            else moveToAction.setPosition(infoPanelsGroup[panel].getX() + 100, -infoPanelsGroup[panel].getHeight() - 15);

            moveToAction.setDuration(0.35f);
            moveToAction.setInterpolation(Interpolation.pow4In);
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.addAction(moveToAction);
            infoPanelsGroup[panel].addAction(sequenceAction);

            panel++;
        } while (panel < 2);
    }

    private void retractBackButton(){
        backReady = false;
        SequenceAction sequenceAction = new SequenceAction();

        MoveToAction move = new MoveToAction();
        move.setPosition(-128, GameConstants.VERTICAL_RESOLUTION - 125);
        move.setDuration(0.7f);
        move.setInterpolation(new Interpolation.Swing(0.7f));
        RotateByAction rotate = new RotateByAction();
        rotate.setAmount(70);
        rotate.setDuration(0.6f);
        rotate.setInterpolation(new Interpolation.Swing(0.7f));
        ParallelAction parallelAction = new ParallelAction(move, rotate);
        sequenceAction.addAction(parallelAction);

        backButtonGroup.addAction(sequenceAction);
    }

    public void deployPanels(boolean enteringAnimation, boolean deployTitle, boolean deployInfo, boolean deployButton){
        actualizeData();
        if(deployTitle)  deployTitlePanel(enteringAnimation);
        if(deployInfo)   deployInfoPanels(enteringAnimation);
        if(deployButton) deployBackButton(enteringAnimation);
    }

    public void retractPanels(){
        backOrderActivated = false;
        touchActivated = false;
        retractTitlePanel();
        retractInfoPanels();
        retractBackButton();
    }

    public boolean getPanelsDeployed(){
        return titleReady && infoReady && backReady;
    }

    private void actualizeData(){
        GlyphLayout layout;
        String text;
        text = GlobalGeneralData.getInstance().getGlobalBundleData().get("episode_" + GlobalGeneralData.getInstance().getCurrentEpisode() + "_name");
        episodeLabel.setText(text);
        layout = new GlyphLayout(labelStyle_gb_16.font, text);
        episodeLabel.setPosition(MathUtils.floor((titleScreenWidth / 2) - (layout.width / 2)), 1);

        String episodeTotalStars = Integer.toString(levelsData.getEpisodeDTO().getTotalStars());
        String currentEpisodeStars =  Integer.toString(gameData.getEpisodeStars(GlobalGeneralData.getInstance().getCurrentEpisode()));
        text =  currentEpisodeStars + "/" + episodeTotalStars;
        layout = new GlyphLayout(labelStyle_gb_16.font, text);
        starsScoreLabel.setText(text);
        starsScoreLabel.setPosition(100 - (layout.width / 2), 0);

        String episodeTotaChallenges = Integer.toString(levelsData.getEpisodeDTO().getTotalChallenges());
        String currentEpisodeChallenges =  Integer.toString(gameData.getEpisodeChallengesCompleted(GlobalGeneralData.getInstance().getCurrentEpisode()));
        text =  currentEpisodeChallenges + "/" + episodeTotaChallenges;
        layout = new GlyphLayout(labelStyle_gb_16.font, text);
        challengesScoreLabel.setText(text);
        challengesScoreLabel.setPosition(infoScreenWidth - 100 - (layout.width / 2), 0);
    }

    public boolean isBackOrderActivated(){
        return backOrderActivated;
    }
    public void cancelBackOrder(){
        backOrderActivated = false;
    }
}