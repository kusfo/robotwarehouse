package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
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
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;

/**
 * Created by JordiRM on 01/12/2016.
 */
public class GUIobjectGamePanels extends GUIobject {
    private TextureAtlas atlas;
    private Label.LabelStyle labelStyle_base_gb_22,labelStyle_base_gb_16, labelStyle_numbers_gb_18, labelStyle_base_gb_11;
    private Group groupScoresPanel, groupBoxesStockPanel, groupPauseButtonPanel;
    private Label lostBoxesLabel, scoreLabel, timeLabel, stockBoxesLabel;
    private Image arrowImage;
    private Container sotckBoxesContainer, timeContainer;
    private Color c_textYellow, c_red;
    private int lastLostBoxes = 0;
    private int lastScore = 0;
    private int lastTransportableBoxes = 0;
    private DEPLOY_STATUS deployStatus = DEPLOY_STATUS.RETRACTED;
    private DEPLOY_STATUS scoresDeployStatus = DEPLOY_STATUS.RETRACTED;
    private DEPLOY_STATUS boxesDeployStatus = DEPLOY_STATUS.RETRACTED;
    private DEPLOY_STATUS buttonDeployStatus = DEPLOY_STATUS.RETRACTED;
    private Group layer;
    private GameEventsManager eventsManager;
    private boolean interactionEnabled = false;

    public GUIobjectGamePanels(Group layer, GameEventsManager eventsManager) {
        this.layer = layer;
        this.eventsManager = eventsManager;
        atlas = Assets.getTextureAtlas("generalLevelPanels");
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_11 = new Label.LabelStyle();
        labelStyle_base_gb_11.font = Assets.getFont("f_base_gb_11");
        labelStyle_numbers_gb_18 = new Label.LabelStyle();
        labelStyle_numbers_gb_18.font = Assets.getFont("f_numbers_gb_18");

        c_textYellow = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_red = new Color(255 / 255f, 65 / 255f, 65 / 255f, 0.85f);
        execute();
    }

    public void execute() {
        createScoreAndTimePanel();
        createBoxesStockPanel();
        createPauseButtonPanel();
    }

    private void createBoxesStockPanel() {

        Image imageStockPanelFrame = new Image(atlas.findRegion("stockPanelFrame"));
        imageStockPanelFrame.setPosition(0, 0);
        AnimatedImageActor animatedImageStockPanelScreen = new AnimatedImageActor(atlas, "stockPanelScreen", 0.1f,  Animation.PlayMode.LOOP);
        animatedImageStockPanelScreen.play();
        animatedImageStockPanelScreen.setPosition(18, 23);

        groupBoxesStockPanel = new Group();
        groupBoxesStockPanel.setSize(imageStockPanelFrame.getWidth(), imageStockPanelFrame.getHeight());
        groupBoxesStockPanel.setPosition(12, GameConstants.VERTICAL_RESOLUTION);
        groupBoxesStockPanel.rotateBy(30);

        Label stockTitleLabel = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("level_stock"), labelStyle_base_gb_16);
        stockTitleLabel.setColor(c_textYellow);

        stockBoxesLabel = new Label("0", labelStyle_base_gb_22);
        stockBoxesLabel.setColor(Color.SKY.r,Color.SKY.g,Color.SKY.b, 0.85f);

        Container stockTitleContainer;
        stockTitleContainer = new Container(stockTitleLabel);
        stockTitleContainer.setPosition(66, 72);
        stockTitleContainer.align(Align.center);

        sotckBoxesContainer = new Container(stockBoxesLabel);
        sotckBoxesContainer.setPosition(66, 44);
        sotckBoxesContainer.align(Align.center);
        sotckBoxesContainer.setTransform(true);

        groupBoxesStockPanel.addActor(animatedImageStockPanelScreen);
        groupBoxesStockPanel.addActor(imageStockPanelFrame);

        groupBoxesStockPanel.addActor(stockTitleContainer);
        groupBoxesStockPanel.addActor(sotckBoxesContainer);
        groupBoxesStockPanel.setVisible(false);

        layer.addActor(groupBoxesStockPanel);
    }

    private void createScoreAndTimePanel(){
        Image imageScoresPanelFrame = new Image(atlas.findRegion("scoresPanelFrame"));
        imageScoresPanelFrame.setPosition(0, 0);

        groupScoresPanel = new Group();
        groupScoresPanel.setSize(imageScoresPanelFrame.getWidth(), imageScoresPanelFrame.getHeight());
        groupScoresPanel.setPosition(115, GameConstants.VERTICAL_RESOLUTION);//120

        AnimatedImageActor animatedImageScorePanelScren = new AnimatedImageActor(atlas, "scoresPanelScreen", 0.1f,  Animation.PlayMode.LOOP);
        animatedImageScorePanelScren.play();
        animatedImageScorePanelScren.setPosition(55, 27);
        groupScoresPanel.addActor(animatedImageScorePanelScren);
        groupScoresPanel.addActor(imageScoresPanelFrame);

        lostBoxesLabel = new Label("0", labelStyle_numbers_gb_18);
        lostBoxesLabel.setColor(1, 1, 1, 0.85f);

        scoreLabel = new Label("0", labelStyle_numbers_gb_18);
        scoreLabel.setColor(1, 1, 1, 0.85f);

        timeLabel = new Label(Utils.getTimeFormatted(GlobalLevelData.getInstance().getLevelTime()), labelStyle_numbers_gb_18);
        timeLabel.setColor(c_textYellow);
        timeLabel.setAlignment(Align.center);
        Label labelTimeMultiplier = new Label("x" + GlobalLevelData.getInstance().getTimeMultiplier(), labelStyle_base_gb_11);
        labelTimeMultiplier.setColor(154 / 255f, 242 / 255f, 216 / 255f, 0.90f);
        labelTimeMultiplier.setPosition(428 - (labelTimeMultiplier.getWidth() / 2), 18);

        Container lostBoxesContainer, scoreContainer;
        lostBoxesContainer = new Container(lostBoxesLabel);
        scoreContainer = new Container(scoreLabel);
        timeContainer = new Container(timeLabel);
        timeContainer.setTransform(true);
        lostBoxesContainer.setPosition(79, 42);
        scoreContainer.setPosition(183, 42);
        timeContainer.setPosition(394, 42);
        lostBoxesContainer.align(Align.center);
        scoreContainer.align(Align.left);
        timeContainer.align(Align.right);

        groupScoresPanel.addActor(lostBoxesContainer);
        groupScoresPanel.addActor(scoreContainer);
        groupScoresPanel.addActor(timeContainer);
        groupScoresPanel.addActor(labelTimeMultiplier);
        groupScoresPanel.setVisible(false);

        layer.addActor(groupScoresPanel);
    }

    private void createPauseButtonPanel(){
        Image imagePausePanelFrame = new Image(atlas.findRegion("levelPauseButtonFrame"));

        groupPauseButtonPanel = new Group();
        groupPauseButtonPanel.setSize(imagePausePanelFrame.getWidth(), imagePausePanelFrame.getHeight());
        groupPauseButtonPanel.setPosition(650, GameConstants.VERTICAL_RESOLUTION - 120);
        groupPauseButtonPanel.rotateBy(70);
        groupPauseButtonPanel.setOrigin(114, 34);

        groupPauseButtonPanel.addActor(imagePausePanelFrame);

        Drawable buttonUp = new Image(atlas.findRegion("pauseButton", 1)).getDrawable();
        Drawable buttonDown = new Image(atlas.findRegion("pauseButton", 2)).getDrawable();
        Button pauseButton = new ImageButton(buttonUp, buttonDown);
        pauseButton.setPosition( 16, 21);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(interactionEnabled){
                    AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                    interactionEnabled = false;
                    eventsManager.addEnterPauseMenu();
                }
            }
        });
        groupPauseButtonPanel.addActor(pauseButton);
        groupPauseButtonPanel.setVisible(false);

        layer.addActor(groupPauseButtonPanel);
    }

    private void createCargoLoadArrow(){
        RepeatAction loop = new RepeatAction();
        loop.setCount(RepeatAction.FOREVER);

        SequenceAction sequenceAction = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction();

        arrowImage = new Image(atlas.findRegion("cargoLoadArrow"));
        arrowImage.setPosition(54, 230);

        MoveToAction initialPosition = new MoveToAction();
        initialPosition.setPosition(arrowImage.getX(), arrowImage.getY() );
        initialPosition.setDuration(0);

        MoveToAction move = new MoveToAction();
        move.setPosition(arrowImage.getX(), arrowImage.getY() - 30 );
        move.setDuration(1.0f);

        AlphaAction initialAlpha = new AlphaAction();
        initialAlpha.setAlpha(1);
        initialAlpha.setDuration(0);

        AlphaAction fade = new AlphaAction();
        fade.setAlpha(0);
        fade.setDuration(1.0f);

        parallelAction.addAction(move);
        parallelAction.addAction(fade);

        sequenceAction.addAction(initialPosition);
        sequenceAction.addAction(initialAlpha);
        sequenceAction.addAction(parallelAction);

        loop.setAction(sequenceAction);
        arrowImage.addAction(loop);

        layer.addActor(arrowImage);
    }

    private void removeCargoLoadArrow(){
        arrowImage.remove();
    }

    @Override
    public void update(float delta){
        // Cajas perdidas
        int actualLostBoxes = GlobalLevelData.getInstance().getLostBoxes();
        if(lastLostBoxes != actualLostBoxes) {
            lostBoxesLabel.setText(Integer.toString(actualLostBoxes));
            if(actualLostBoxes > 0) lostBoxesLabel.setColor(c_red);
            lastLostBoxes = actualLostBoxes;
        }

        // Puntos
        int actualScore = GlobalLevelData.getInstance().getLevelScore();
        if(lastScore != actualScore) {
            scoreLabel.setText(Integer.toString(actualScore));
            lastScore = actualScore;
        }

        // Tiempo
        float levelTime = GlobalLevelData.getInstance().getLevelTime();
        timeLabel.setText(Utils.getTimeFormatted(levelTime));

        int actualTransportableBoxes = GlobalLevelData.getInstance().getRemainingBoxes().getTransportableBoxes();
        if(lastTransportableBoxes != actualTransportableBoxes) {
            stockBoxesLabel.setText(Integer.toString(actualTransportableBoxes));
            lastTransportableBoxes = actualTransportableBoxes;
        }
    }

    public void deployAll(float delayTime){
        deployScorePanel(delayTime);
        deployBoxesStockPanel(delayTime);
        deployPauseButtonPanel(delayTime);
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_ENTER);
    }

    public void deployScorePanel(float delayTime){
        if(scoresDeployStatus != DEPLOY_STATUS.DEPLOYED) {
            groupScoresPanel.setVisible(true);
            SequenceAction sequenceAction;
            MoveToAction move;
            Interpolation interpolation;

            sequenceAction = new SequenceAction();
            interpolation = new Interpolation.Swing(1.7f);
            move = new MoveToAction();
            move.setPosition(groupScoresPanel.getX(), GameConstants.VERTICAL_RESOLUTION - groupScoresPanel.getHeight() + 44);
            move.setDuration(0.7f);
            move.setInterpolation(interpolation);
            RunnableAction scoreDeployCompleted = new RunnableAction();
            scoreDeployCompleted.setRunnable(new Runnable() {
                @Override
                public void run() {
                    scoresDeployStatus = DEPLOY_STATUS.DEPLOYED;
                }
            });

            sequenceAction.addAction(new DelayAction(delayTime)); //0.4f
            sequenceAction.addAction(move);
            sequenceAction.addAction(scoreDeployCompleted);

            groupScoresPanel.addAction(sequenceAction);
        }
    }

    public void deployBoxesStockPanel(float delayTime){
        groupBoxesStockPanel.setVisible(true);
        SequenceAction sequenceAction;
        MoveToAction move;
        Interpolation interpolation;
        sequenceAction = new SequenceAction();

        RunnableAction boxPanelEnter = new RunnableAction();
        boxPanelEnter.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_PANEL_ENTER);
            }
        });

        interpolation = new Interpolation.Swing(0.7f);
        move = new MoveToAction();
        move.setPosition(groupBoxesStockPanel.getX(), GameConstants.VERTICAL_RESOLUTION - groupBoxesStockPanel.getHeight() + 50 );
        move.setDuration(0.7f);
        move.setInterpolation(interpolation);
        RotateByAction rotate = new RotateByAction();
        rotate.setAmount(-30);
        rotate.setDuration(0.6f);
        rotate.setInterpolation(interpolation);
        RunnableAction panelsReady = new RunnableAction();
        panelsReady.setRunnable(new Runnable() {
            @Override
            public void run() {
                boxesDeployStatus = DEPLOY_STATUS.DEPLOYED;
                interactionEnabled = true;
                createCargoLoadArrow();
            }
        });
        ParallelAction parallelAction = new ParallelAction(move, rotate);
        sequenceAction.addAction(new DelayAction(delayTime + 0.1f));
        sequenceAction.addAction(boxPanelEnter);
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(panelsReady);
        groupBoxesStockPanel.addAction(sequenceAction);
    }

    public void deployPauseButtonPanel(float delayTime){
        if(buttonDeployStatus != DEPLOY_STATUS.DEPLOYED) {
            groupPauseButtonPanel.setVisible(true);
            SequenceAction sequenceAction;
            MoveToAction move;
            RotateByAction rotate;
            Interpolation interpolation;

            sequenceAction = new SequenceAction();

            interpolation = new Interpolation.Swing(0.7f);
            move = new MoveToAction();
            move.setPosition(567, GameConstants.VERTICAL_RESOLUTION - 80);
            move.setDuration(0.7f);
            move.setInterpolation(interpolation);
            rotate = new RotateByAction();
            rotate.setAmount(-70);
            rotate.setDuration(0.6f);
            rotate.setInterpolation(interpolation);
            RunnableAction buttonDeployCompleted = new RunnableAction();
            buttonDeployCompleted.setRunnable(new Runnable() {
                @Override
                public void run() {
                    buttonDeployStatus = DEPLOY_STATUS.DEPLOYED;
                }
            });
            ParallelAction parallelAction = new ParallelAction(move, rotate);
            sequenceAction.addAction(new DelayAction(delayTime + 0.1f));
            sequenceAction.addAction(parallelAction);
            sequenceAction.addAction(buttonDeployCompleted);
            groupPauseButtonPanel.addAction(sequenceAction);
        }
    }

    public void retractAll(float delayTime){
        removeCargoLoadArrow();
        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_LEAVE);

        retractScoresPanel(delayTime);
        retractBoxesStockPanel(delayTime);
        retractPauseButtonPanel(delayTime);
    }

    public void retractScoresPanel(float delayTime) {
        SequenceAction sequenceAction;
        MoveToAction move;

        sequenceAction = new SequenceAction();
        move = new MoveToAction();
        move.setPosition(groupScoresPanel.getX(), GameConstants.VERTICAL_RESOLUTION);
        move.setDuration(0.6f);
        move.setInterpolation(Interpolation.pow4In);
        RunnableAction scoresRetractionCompleted = new RunnableAction();
        scoresRetractionCompleted.setRunnable(new Runnable() {
            @Override
            public void run() {
                scoresDeployStatus = DEPLOY_STATUS.RETRACTED;
            }
        });

        sequenceAction.addAction(new DelayAction(delayTime));
        sequenceAction.addAction(move);
        sequenceAction.addAction(scoresRetractionCompleted);
        groupScoresPanel.addAction(sequenceAction);
    }

    public void retractBoxesStockPanel(float delayTime) {
        SequenceAction sequenceAction;
        MoveToAction move;

        sequenceAction = new SequenceAction();

        RunnableAction boxPanelLeave = new RunnableAction();
        boxPanelLeave.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_PANEL_LEAVE);
            }
        });

        Interpolation interpolation = new Interpolation.Swing(0.7f);
        move = new MoveToAction();
        move.setPosition(groupBoxesStockPanel.getX(), GameConstants.VERTICAL_RESOLUTION);
        move.setDuration(0.8f);
        move.setInterpolation(interpolation);
        RotateByAction rotate = new RotateByAction();
        rotate.setAmount(30);
        rotate.setDuration(0.7f);
        rotate.setInterpolation(interpolation);
        RunnableAction panelsReady = new RunnableAction();
        panelsReady.setRunnable(new Runnable() {
            @Override
            public void run() {
                boxesDeployStatus = DEPLOY_STATUS.RETRACTED;
            }
        });
        ParallelAction parallelAction = new ParallelAction(move, rotate);
        sequenceAction.addAction(new DelayAction(delayTime + 0.1f));
        sequenceAction.addAction(boxPanelLeave);
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(panelsReady);
        groupBoxesStockPanel.addAction(sequenceAction);
    }

    public void retractPauseButtonPanel(float delayTime) {
        SequenceAction sequenceAction;
        MoveToAction move;
        RotateByAction rotate;
        Interpolation interpolation;

        sequenceAction = new SequenceAction();

        interpolation = new Interpolation.Swing(0.7f);
        move = new MoveToAction();
        move.setPosition(650, GameConstants.VERTICAL_RESOLUTION - 120 );
        move.setDuration(0.9f);
        move.setInterpolation(interpolation);
        rotate = new RotateByAction();
        rotate.setAmount(-30);
        rotate.setDuration(0.8f);
        rotate.setInterpolation(interpolation);
        RotateToAction rotateTo = new RotateToAction();
        rotateTo.setRotation(70);
        rotateTo.setDuration(0);
        RunnableAction buttonRetractionCompleted = new RunnableAction();
        buttonRetractionCompleted.setRunnable(new Runnable() {
            @Override
            public void run() {
                buttonDeployStatus = DEPLOY_STATUS.RETRACTED;
            }
        });

        ParallelAction parallelAction = new ParallelAction(move, rotate);
        sequenceAction.addAction(new DelayAction(delayTime));
        sequenceAction.addAction(parallelAction);
        sequenceAction.addAction(rotateTo);
        sequenceAction.addAction(buttonRetractionCompleted);
        groupPauseButtonPanel.addAction(sequenceAction);
    }

    public void activateTimeOutEvent(){
        redBlinkEvent(timeContainer, timeLabel, c_textYellow);
    }

    public void activateInsufficientStockEvent(){
        redBlinkEvent(sotckBoxesContainer, stockBoxesLabel, Color.SKY);
    }

    private void redBlinkEvent(Container container, final Label label, final Color baseColor){
        RepeatAction loopContainer = new RepeatAction();
        loopContainer.setCount(RepeatAction.FOREVER);
        RepeatAction loopLabel = new RepeatAction();
        loopLabel.setCount(RepeatAction.FOREVER);

        ScaleToAction scaleAction1 = new ScaleToAction();
        scaleAction1.setScale(1.0f, 1.0f);
        scaleAction1.setDuration(0.04f);
        ScaleToAction scaleAction2 = new ScaleToAction();
        scaleAction2.setScale(1.2f, 1.2f);
        scaleAction2.setDuration(0.04f);

        RunnableAction runnableColor1 = new RunnableAction();
        runnableColor1.setRunnable(new Runnable() {
            @Override
            public void run() {
                label.setColor(baseColor);
            }
        });
        RunnableAction runnableColor2 = new RunnableAction();
        runnableColor2.setRunnable(new Runnable() {
            @Override
            public void run() {
                label.setColor(Color.RED);
            }
        });

        SequenceAction containerSequence = new SequenceAction();
        containerSequence.addAction(scaleAction1);
        containerSequence.addAction(new DelayAction(0.2f));
        containerSequence.addAction(scaleAction2);
        containerSequence.addAction(new DelayAction(0.3f));

        SequenceAction labelSequence = new SequenceAction();
        labelSequence.addAction(runnableColor1);
        labelSequence.addAction(new DelayAction(0.2f));
        labelSequence.addAction(runnableColor2);
        labelSequence.addAction(new DelayAction(0.38f));

        loopContainer.setAction(containerSequence);
        container.addAction(loopContainer);

        loopLabel.setAction(labelSequence);
        label.addAction(loopLabel);
    }


    public DEPLOY_STATUS getDeployStatus() {
        if(scoresDeployStatus == DEPLOY_STATUS.DEPLOYED &&
                boxesDeployStatus == DEPLOY_STATUS.DEPLOYED &&
                buttonDeployStatus == DEPLOY_STATUS.DEPLOYED)
            return DEPLOY_STATUS.DEPLOYED;
        return DEPLOY_STATUS.RETRACTED;
    }

    public enum DEPLOY_STATUS{
        DEPLOYED, RETRACTED;
    }
}