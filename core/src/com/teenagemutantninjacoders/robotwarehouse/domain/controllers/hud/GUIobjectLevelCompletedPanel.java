package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.Utils;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

import java.util.ArrayList;

/**
 * Created by JordiRM on 18/11/2016.
 */
public class GUIobjectLevelCompletedPanel extends GUIobjectBaseLevelPanel {
    private Group layer;
    private Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_16,labelStyle_base_gb_13, labelStyle_numbers_gb_18;
    private Group groupMissionCompletePanel;
    private Label labelTitle, labelPoints, labelTime, labelBoxes, labelRankPoints_2, labelRankPoints_3;
    private Container pointsContainer, timeContainer, boxesContainer, newRecordContainer;
    private Color c_green, c_titulo, c_red, c_timeMultiplier;
    private Integer rankAchieved;
    private Integer pointsAccumulated;
    private Integer penaltyFromLostBoxes;
    private Integer pointsFromTime;
    ArrayList<ParticleEffectActor> effectsList = new ArrayList<ParticleEffectActor>();
    private boolean isChallengeCompleted;
    private TextureAtlas basePanelAtlas;
    private TextureAtlas buttonsAtlas;
    private boolean newRecord;

    public GUIobjectLevelCompletedPanel(GameEventsManager eventsManager, Group layer){
        this.eventsManager = eventsManager;
        this.layer = layer;
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_numbers_gb_18 = new Label.LabelStyle();
        labelStyle_numbers_gb_18.font = Assets.getFont("f_numbers_gb_18");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_13 = new Label.LabelStyle();
        labelStyle_base_gb_13.font = Assets.getFont("f_base_gb_13");

        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_green = new Color(58 / 255f, 241 / 255f, 22 / 255f, 0.85f);
        c_red = new Color(255 / 255f, 65 / 255f, 65 / 255f, 0.85f);
        c_timeMultiplier = new Color(154 / 255f, 242 / 255f, 216 / 255f, 0.90f);

        // Recopilamos los resultados para mostrarlos luego
        pointsAccumulated = GlobalLevelData.getInstance().getLevelScore();
        pointsFromTime = GlobalLevelData.getInstance().getFinalLevelResults().getPointsFromTime();
        penaltyFromLostBoxes = GlobalLevelData.getInstance().getFinalLevelResults().getPenaltyFromLostBoxes();
        rankAchieved = GlobalLevelData.getInstance().getFinalLevelResults().getRankAchieved();
        newRecord = GlobalLevelData.getInstance().getFinalLevelResults().isNewRecord();
        isChallengeCompleted = ChallengeManager.getInstance().isChallengeCompleted();
        basePanelAtlas = Assets.getTextureAtlas("missionCompletePanel");
        buttonsAtlas = Assets.getTextureAtlas("general_buttons");
        execute();
    }

    private void execute() {
        width = basePanelAtlas.findRegion("frame").getRegionWidth();
        height = basePanelAtlas.findRegion("frame").getRegionHeight();
        Integer screenWidth = basePanelAtlas.findRegion("screen", 1).getRegionWidth();
        Integer screenHeight = basePanelAtlas.findRegion("screen", 1).getRegionHeight();

        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 55;
        groupMissionCompletePanel = new Group();
        groupMissionCompletePanel.setSize(width, height);
        groupMissionCompletePanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2) - 5, GameConstants.VERTICAL_RESOLUTION + 100); // 367
        Group groupScreen = new Group();
        groupScreen.setSize(screenWidth, screenHeight);
        groupScreen.setPosition(28, 60);

        labelTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("end_level_success_results"), labelStyle_base_gb_22);
        labelTitle.setColor(c_titulo);
        labelTitle.setPosition((screenWidth / 2) - (labelTitle.getWidth() / 2), 200);

        labelPoints = new Label(Integer.toString(pointsAccumulated), labelStyle_base_gb_22);
        labelPoints.setColor(1, 1, 1, 0.85f);

        labelTime = new Label(Utils.getTimeFormatted(GlobalLevelData.getInstance().getLevelTime()), labelStyle_numbers_gb_18);
        labelTime.setColor(1, 1, 1, 0.85f);

        Label labeTimeMultiplier = new Label("x" + GlobalLevelData.getInstance().getTimeMultiplier(), labelStyle_base_gb_16);
        labeTimeMultiplier.setPosition((screenWidth / 2) - 127 - (labeTimeMultiplier.getWidth() / 2) + 2, 7);
        labeTimeMultiplier.setColor(c_timeMultiplier);

        labelBoxes = new Label(Integer.toString(GlobalLevelData.getInstance().getLostBoxes()), labelStyle_numbers_gb_18);

        Image imageClockIcon = new Image(basePanelAtlas.findRegion("icon_time"));
        imageClockIcon.setPosition((screenWidth / 2) - 127 - (imageClockIcon.getWidth() / 2), 0);
        Image imageLostBoxIcon = new Image(basePanelAtlas.findRegion("icon_lostBoxes"));
        imageLostBoxIcon.setPosition((screenWidth / 2) + 127 - (imageLostBoxIcon.getWidth() / 2), -1);

        Image imageMisionPanel = new Image(basePanelAtlas.findRegion("frame"));
        imageMisionPanel.localToParentCoordinates(new Vector2(0, 0));
        imageMisionPanel.setPosition(0, 0);
        AnimatedImageActor animatedImageScreen = new AnimatedImageActor(basePanelAtlas, "screen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageScreen.play();

        int totalPoints = GlobalLevelData.getInstance().getFinalLevelResults().getTotalLevelScore();
        int remainingToRank2 = MathUtils.clamp(GlobalLevelData.getInstance().getLevelRankStats().pointsTwoStars - totalPoints, 0 ,9999);
        int remainingToRank3 = MathUtils.clamp( GlobalLevelData.getInstance().getLevelRankStats().pointsThreeStars - totalPoints, 0 ,9999);

        Color c_rankPoints = new Color(242 / 255f, 238 / 255f, 168 / 255f, 0); // 0.5
        labelRankPoints_2 = new Label("+" + remainingToRank2, labelStyle_base_gb_13);
        labelRankPoints_2.setPosition(225 - (labelRankPoints_2.getWidth() / 2), 136 - (labelRankPoints_2.getHeight() / 2));
        labelRankPoints_2.setColor(c_rankPoints);
        labelRankPoints_3 = new Label("+" + remainingToRank3, labelStyle_base_gb_13);
        labelRankPoints_3.setPosition(164 - (labelRankPoints_3.getWidth() / 2), 149 - (labelRankPoints_3.getHeight() / 2));
        labelRankPoints_3.setColor(c_rankPoints);

        groupScreen.addActor(animatedImageScreen);

        pointsContainer = new Container(labelPoints);
        pointsContainer.setPosition((screenWidth / 2) - 2, 98);
        pointsContainer.setTransform(true);
        pointsContainer.align(Align.center);

        timeContainer = new Container(labelTime);
        timeContainer.setPosition((screenWidth / 2) - 58, 42);
        timeContainer.setTransform(true);
        timeContainer.align(Align.center);

        boxesContainer = new Container(labelBoxes);
        boxesContainer.setPosition((screenWidth / 2) + 52, 42);
        boxesContainer.setTransform(true);
        boxesContainer.align(Align.center);

        Label newRecordLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("new_record_exclamation"), labelStyle_base_gb_16);

        newRecordLabel.setColor(252 / 255f, 194 / 255f, 93 / 255f, 0.85f); // Naranja pálido
        newRecordContainer = new Container(newRecordLabel);
        newRecordContainer.setScaleX(0.6f);
        newRecordContainer.setColor(1, 1, 1, 0);
        newRecordContainer.setPosition((screenWidth / 2) - 2, 75);
        newRecordContainer.setTransform(true);
        newRecordContainer.align(Align.center);

        groupScreen.addActor(imageClockIcon);
        groupScreen.addActor(labeTimeMultiplier);
        groupScreen.addActor(imageLostBoxIcon);
        groupScreen.addActor(labelTitle);
        groupScreen.addActor(pointsContainer);
        groupScreen.addActor(timeContainer);
        groupScreen.addActor(boxesContainer);
        groupScreen.addActor(labelRankPoints_2);
        groupScreen.addActor(labelRankPoints_3);
        groupScreen.addActor(newRecordContainer);

        groupMissionCompletePanel.addActor(groupScreen);
        groupMissionCompletePanel.addActor(imageMisionPanel);

        // Medalla de desafio completo
        if(isChallengeCompleted){
            Image imageChallengeMedal = new Image(Assets.getTextureAtlas("level_screen_elements").findRegion("icon_challengePanel_G"));
            imageChallengeMedal.setPosition(360 - 40, 300 - 70);
            groupMissionCompletePanel.addActor(imageChallengeMedal);
        }

        Drawable buttonUp, buttonDown;

        //Boton Reintentar
        buttonUp = new Image(buttonsAtlas.findRegion("panelButtonRestart", 1)).getDrawable();
        buttonDown = new Image(buttonsAtlas.findRegion("panelButtonRestart", 2)).getDrawable();
        Button buttonRestart = new ImageButton(buttonUp, buttonDown);
        buttonRestart.setPosition( 134, 21); // 172
        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    buttonActivated = BUTTON_ACTION.RESTART_LEVEL;
                    eventsManager.makeCharacterLeave();
                    leave(groupMissionCompletePanel);
                }
            }
        });
        groupMissionCompletePanel.addActor(buttonRestart);

        //Boton play
        buttonUp = new Image(buttonsAtlas.findRegion("panelButtonPlay", 1)).getDrawable();
        buttonDown = new Image(buttonsAtlas.findRegion("panelButtonPlay", 2)).getDrawable();
        Button buttonPlay = new ImageButton(buttonUp, buttonDown);
        buttonPlay.setPosition( 134 + 76, 21); // 246
        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel){
                    //buttonActivated = BUTTON_ACTION.NEXT_LEVEL;
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    buttonActivated = BUTTON_ACTION.SELECTION_SCREEN;
                    eventsManager.makeCharacterLeave();
                    leave(groupMissionCompletePanel);
                }
            }
        });
        groupMissionCompletePanel.addActor(buttonPlay);


        layer.addActor(groupMissionCompletePanel);
        enter(groupMissionCompletePanel);
    }

    @Override
    protected void arrived(){
        activePanel = true;
        AudioManager.getInstance().playMusic(AudioManager.MUSIC.LEVEL_COMPLETED_PANEL, true, false);
        addBonusPoints();
        // Aprovechamos para mostrar los logros conseguidos en la partida
        eventsManager.showWaitingAchievements();
    }

    private void addBonusPoints(){
        SequenceAction sequenceAction1;
        ScaleToAction scaleAction1, scaleAction2;
        RunnableAction actualize;

        scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1.9f,1.3f);
        scaleAction1.setDuration(0.15f);
        scaleAction1.setInterpolation(Interpolation.circleOut);
        scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(1.0f,1.0f);
        scaleAction2.setDuration(0.15f);
        scaleAction2.setInterpolation(Interpolation.circleIn);
        actualize = new RunnableAction();
        actualize.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.ADD_TOTAL_POINTS);
                labelTime.setText("+" + Integer.toString(pointsFromTime));
                pointsAccumulated += pointsFromTime;
                labelPoints.setText(Integer.toString(pointsAccumulated));
                labelTime.setColor(c_green);
                actualizePoints();
            }
        });

        sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(new DelayAction(0.5f));
        sequenceAction1.addAction(actualize);
        sequenceAction1.addAction(scaleAction1);

        sequenceAction1.addAction(scaleAction2);
        timeContainer.addAction(sequenceAction1);

        scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1.9f,1.3f);
        scaleAction1.setDuration(0.15f);
        scaleAction1.setInterpolation(Interpolation.circleOut);
        scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(1.0f,1.0f);
        scaleAction2.setDuration(0.15f);
        scaleAction2.setInterpolation(Interpolation.circleIn);
        actualize = new RunnableAction();
        actualize.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.ADD_TOTAL_POINTS);
                if(penaltyFromLostBoxes > 0) {
                    labelBoxes.setText("-" + Integer.toString(penaltyFromLostBoxes));
                    pointsAccumulated -= penaltyFromLostBoxes;
                    labelPoints.setText(Integer.toString(pointsAccumulated));
                    labelBoxes.setColor(c_red);
                }else{
                    labelBoxes.setText("0");
                    labelBoxes.setColor(c_green);
                }
                actualizePoints();
                if(newRecord) showNewRecord();
                ShowRank();
            }
        });
        sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(new DelayAction(1.1f));
        sequenceAction1.addAction(actualize);
        sequenceAction1.addAction(scaleAction1);
        sequenceAction1.addAction(scaleAction2);
        boxesContainer.addAction(sequenceAction1);
    }

    private void ShowRank(){

        SequenceAction sequenceAction1;
        ParallelAction parallelAction;
        ScaleToAction scaleAction1,scaleAction2, scaleAction3;
        AlphaAction alphaAction;
        MoveToAction moveToAction;
        RunnableAction showEffect;

        if(rankAchieved >= 1) {
            // Estrella Izquierda
            Image leftStar = new Image(basePanelAtlas.findRegion("star", 1));
            leftStar.setPosition(93 - 100, 156 + 70);//96 - 100, 160 + 70
            leftStar.setOrigin(Align.center);
            leftStar.setColor(1, 1, 1, 0);
            leftStar.setScale(5, 5);
            leftStar.setTouchable(Touchable.disabled);

            groupMissionCompletePanel.addActor(leftStar);

            moveToAction = new MoveToAction();
            moveToAction.setPosition(93,161);
            moveToAction.setDuration(0.2f);
            moveToAction.setInterpolation(Interpolation.pow2In);
            scaleAction1 = new ScaleToAction();
            scaleAction1.setScale(1f,1f);
            scaleAction1.setDuration(0.2f);
            scaleAction1.setInterpolation(Interpolation.pow2In);
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.2f);
            scaleAction2 = new ScaleToAction();
            scaleAction2.setScale(1.1f,1.1f);
            scaleAction2.setDuration(0.1f);
            scaleAction2.setInterpolation(Interpolation.circleOut);
            scaleAction3 = new ScaleToAction();
            scaleAction3.setScale(1.0f,1.0f);
            scaleAction3.setDuration(0.1f);
            scaleAction3.setInterpolation(Interpolation.bounceOut);
            showEffect = new RunnableAction();
            showEffect.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.RANK_STAR_1);
                    ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.RANK_STAR_LEFT, 130, 190, 0));
                    effectsList.add(particleEffectActor);
                    groupMissionCompletePanel.addActor(particleEffectActor);
                }
            });


            parallelAction = new ParallelAction(moveToAction, scaleAction1, alphaAction);
            sequenceAction1 = new SequenceAction();
            sequenceAction1.addAction(new DelayAction(0.5f));
            sequenceAction1.addAction(parallelAction);
            sequenceAction1.addAction(showEffect);
            sequenceAction1.addAction(scaleAction2);
            sequenceAction1.addAction(scaleAction3);
            leftStar.addAction(sequenceAction1);
        }

        if(rankAchieved >= 2) {
            // Estrella Derecha
            Image rightStar = new Image(basePanelAtlas.findRegion("star", 2));
            rightStar.setPosition(221 + 100, 156 + 70);//215 + 100, 160 + 70
            rightStar.setOrigin(Align.center);
            rightStar.setColor(1,1,1,0);
            rightStar.setScale(5,5);
            rightStar.setTouchable(Touchable.disabled);

            groupMissionCompletePanel.addActor(rightStar);

            moveToAction = new MoveToAction();
            moveToAction.setPosition(221,161);
            moveToAction.setDuration(0.2f);
            moveToAction.setInterpolation(Interpolation.pow2In);
            scaleAction1 = new ScaleToAction();
            scaleAction1.setScale(1f,1f);
            scaleAction1.setDuration(0.2f);
            scaleAction1.setInterpolation(Interpolation.pow2In);
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.2f);
            scaleAction2 = new ScaleToAction();
            scaleAction2.setScale(1.1f,1.1f);
            scaleAction2.setDuration(0.1f);
            scaleAction2.setInterpolation(Interpolation.circleOut);
            scaleAction3 = new ScaleToAction();
            scaleAction3.setScale(1.0f,1.0f);
            scaleAction3.setDuration(0.1f);
            scaleAction3.setInterpolation(Interpolation.bounceOut);
            showEffect = new RunnableAction();
            showEffect.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.RANK_STAR_2);
                    ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.RANK_STAR_RIGHT, 266, 190, 0));
                    effectsList.add(particleEffectActor);
                    groupMissionCompletePanel.addActor(particleEffectActor);
                }
            });

            parallelAction = new ParallelAction(moveToAction, scaleAction1, alphaAction);
            sequenceAction1 = new SequenceAction();
            sequenceAction1.addAction(new DelayAction(0.8f));
            sequenceAction1.addAction(parallelAction);
            sequenceAction1.addAction(showEffect);
            sequenceAction1.addAction(scaleAction2);
            sequenceAction1.addAction(scaleAction3);
            rightStar.addAction(sequenceAction1);


        }

        if(rankAchieved == 3) {
            Image centerStar = new Image(basePanelAtlas.findRegion("star", 3));
            centerStar.setPosition(141, 167 + 70);//143, 167 + 70
            centerStar.setOrigin(Align.center);
            centerStar.setColor(1, 1, 1, 0);
            centerStar.setScale(5, 5);
            centerStar.setTouchable(Touchable.disabled);

            groupMissionCompletePanel.addActor(centerStar);

            // Estrella Centro
            moveToAction = new MoveToAction();
            moveToAction.setPosition(141, 167);
            moveToAction.setDuration(0.2f);
            moveToAction.setInterpolation(Interpolation.pow2In);
            scaleAction1 = new ScaleToAction();
            scaleAction1.setScale(1f, 1f);
            scaleAction1.setDuration(0.2f);
            scaleAction1.setInterpolation(Interpolation.pow2In);
            alphaAction = new AlphaAction();
            alphaAction.setAlpha(1);
            alphaAction.setDuration(0.2f);
            scaleAction2 = new ScaleToAction();
            scaleAction2.setScale(1.1f, 1.1f);
            scaleAction2.setDuration(0.1f);
            scaleAction2.setInterpolation(Interpolation.circleOut);
            scaleAction3 = new ScaleToAction();
            scaleAction3.setScale(1.0f, 1.0f);
            scaleAction3.setDuration(0.1f);
            scaleAction3.setInterpolation(Interpolation.bounceOut);
            showEffect = new RunnableAction();
            showEffect.setRunnable(new Runnable() {
                @Override
                public void run() {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.RANK_STAR_3);
                    ParticleEffectActor particleEffectActor = new ParticleEffectActor(new ParticleEffectModel(ParticleEffectModel.EFFECT_TYPE.RANK_STAR_CENTER, 198, 214, 0));
                    effectsList.add(particleEffectActor);
                    groupMissionCompletePanel.addActor(particleEffectActor);
                }
            });

            parallelAction = new ParallelAction(moveToAction, scaleAction1, alphaAction);
            sequenceAction1 = new SequenceAction();
            sequenceAction1.addAction(new DelayAction(1.1f));
            sequenceAction1.addAction(parallelAction);
            sequenceAction1.addAction(showEffect);
            sequenceAction1.addAction(scaleAction2);
            sequenceAction1.addAction(scaleAction3);
            centerStar.addAction(sequenceAction1);
        }

        if(rankAchieved == 1) {
            showNeededPoints(labelRankPoints_2,1.4f);
            showNeededPoints(labelRankPoints_3,1.4f);
        }
        else if(rankAchieved == 2)
            showNeededPoints(labelRankPoints_3,1.9f);
    }

    private void showNeededPoints(Actor actor, float delay){
        AlphaAction alphaActionIn = new AlphaAction();
        alphaActionIn.setAlpha(0.6f);
        alphaActionIn.setDuration(1.0f);
        AlphaAction alphaActionOut = new AlphaAction();
        alphaActionOut.setAlpha(0.1f);
        alphaActionOut.setDuration(1.0f);

        SequenceAction sequenceAction_alpha = new SequenceAction();
        sequenceAction_alpha.addAction(alphaActionIn);
        sequenceAction_alpha.addAction(alphaActionOut);

        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setCount(RepeatAction.FOREVER);
        repeatAction.setAction(sequenceAction_alpha);

        SequenceAction sequenceAction_base = new SequenceAction();
        sequenceAction_base.addAction(new DelayAction(delay));
        sequenceAction_base.addAction(repeatAction);
        actor.addAction(sequenceAction_base);
    }

    private void actualizePoints(){
        SequenceAction sequenceAction1;
        ScaleToAction scaleAction1, scaleAction2;

        scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1.3f,1.2f);
        scaleAction1.setDuration(0.15f);//0.15
        scaleAction1.setInterpolation(Interpolation.circleOut);
        scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(1.0f,1.0f);
        scaleAction2.setDuration(0.15f);
        scaleAction2.setInterpolation(Interpolation.circleIn);
        sequenceAction1 = new SequenceAction();
        sequenceAction1.addAction(scaleAction1);
        sequenceAction1.addAction(scaleAction2);
        pointsContainer.addAction(sequenceAction1);
    }

    private void showNewRecord(){
        ScaleToAction scaleAction = new ScaleToAction();
        scaleAction.setScale(1, 1);
        scaleAction.setDuration(0.5f);
        scaleAction.setInterpolation(Interpolation.bounceOut);
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.15f);

        ParallelAction parallelAction = new ParallelAction(scaleAction, alphaAction);

        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.3f));
        sequenceAction.addAction(parallelAction);
        newRecordContainer.addAction(sequenceAction);
    }

    @Override
    protected void DisposePhase(){
        // Dispose de efectos
        for(ParticleEffectActor actor : effectsList){
            actor.disposeEffect();
        }
        FinishPanel();
    }
}
