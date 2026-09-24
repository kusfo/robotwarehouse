package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RemoveActorAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 07/10/2016.
 */
public class GUIobjectAnnouncement {
    private Label.LabelStyle labelStyle_announcement;
    private GameUI gameUI;
    private Group layer;
    private float screenWidth;
    private ANNOUNCEMENT announcement;
    private TextureAtlas atlas;

    public GUIobjectAnnouncement(ANNOUNCEMENT announcement, Group layer, GameUI gameUI){
        this.layer = layer;
        this.gameUI = gameUI;
        this.announcement = announcement;
        atlas = Assets.getTextureAtlas("generalLevelPanels");
        labelStyle_announcement = new Label.LabelStyle();
        labelStyle_announcement.font = Assets.getFont("f_base_gb_13");
    }

    public void execute() {

        Image imageNoticePanelFrame = new Image(atlas.findRegion("noticePanelFrame"));
        AnimatedImageActor animatedImageNoticePanelScreen = new AnimatedImageActor(atlas, "noticePanelScreen", 0.1f,  Animation.PlayMode.LOOP);
        animatedImageNoticePanelScreen.play();

        AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_ENTER);
        float width = imageNoticePanelFrame.getWidth();
        float height = imageNoticePanelFrame.getHeight();
        screenWidth = animatedImageNoticePanelScreen.getWidth();
        float screenHeight = animatedImageNoticePanelScreen.getHeight();

        Group GroupNoticePanel = new Group();
        GroupNoticePanel.setSize(width, height);
        GroupNoticePanel.setPosition(GameConstants.GAMEZONE_X_CENTER - (width / 2), -height);

        Group groupNoticePanelScreen = new Group();
        groupNoticePanelScreen.setSize(screenWidth, screenHeight);
        groupNoticePanelScreen.setPosition(21, 55);

        Group GroupNoticeLabel = getLabelGroup();



        groupNoticePanelScreen.addActor(animatedImageNoticePanelScreen);
        groupNoticePanelScreen.addActor(GroupNoticeLabel);

        GroupNoticePanel.addActor(groupNoticePanelScreen);
        GroupNoticePanel.addActor(imageNoticePanelFrame);

        // ACCIONES
        SequenceAction sequenceAction, sequenceAction2;
        ParallelAction parallelAction;
        MoveToAction moveToAction1, moveToAction2;
        RemoveActorAction removeActor;
        RunnableAction launchSoundRunnable;

        sequenceAction = new SequenceAction();

        // Entrar
        moveToAction1 = new MoveToAction();
        moveToAction1.setPosition(GroupNoticePanel.getX(), -40);
        moveToAction1.setDuration(0.65f);
        moveToAction1.setInterpolation( new Interpolation.Swing(1.7f));

        sequenceAction.addAction(moveToAction1);

        sequenceAction2 = new SequenceAction();
        launchSoundRunnable = new RunnableAction();
        launchSoundRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                launchAnnouncementSound();
            }
        });

        sequenceAction2.addAction(new DelayAction(0.2f));
        sequenceAction2.addAction(launchSoundRunnable);


        // Esperar
        sequenceAction.addAction(new DelayAction(1.5f));

        RunnableAction leaveAction = new RunnableAction();
        leaveAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.PANEL_SHORT_LEAVE);
            }
        });
        sequenceAction.addAction(leaveAction);

        // Salir
        moveToAction2 = new MoveToAction();
        moveToAction2.setPosition(GroupNoticePanel.getX(), -height);
        moveToAction2.setDuration(0.35f);
        moveToAction2.setInterpolation(Interpolation.pow4In);
        sequenceAction.addAction(moveToAction2);

        // Finalizar anuncio
        RunnableAction endAction = new RunnableAction();
        endAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                gameUI.announcementEnded();
            }
        });
        sequenceAction.addAction(endAction);

        // Destruir
        removeActor = new RemoveActorAction();
        sequenceAction.addAction(removeActor);

        parallelAction = new ParallelAction(sequenceAction, sequenceAction2);
        GroupNoticePanel.addAction(parallelAction);
        layer.addActor(GroupNoticePanel);
    }

    private Group getLabelGroup(){
        Group GroupNoticeLabel = new Group();

        switch(announcement){
            case RED_DISABLED: case BLUE_DISABLED: case YELLOW_DISABLED:
                String colorString = "";
                Color fontColor = Color.WHITE;
                switch(announcement){
                    case RED_DISABLED:
                        colorString = GlobalGeneralData.getInstance().getGlobalBundleData().get("red");
                        fontColor = new Color(252 / 255f, 83 / 255f, 83 / 255f, 0.85f);
                        break;
                    case BLUE_DISABLED:
                        colorString = GlobalGeneralData.getInstance().getGlobalBundleData().get("blue");
                        fontColor =  new Color(91 / 255f, 191 / 255f, 255 / 255f, 0.85f);
                        break;
                    case YELLOW_DISABLED:
                        colorString = GlobalGeneralData.getInstance().getGlobalBundleData().get("yellow");
                        fontColor = new Color(247 / 255f, 222 / 255f, 108 / 255f, 0.85f);
                        break;
                }
                Label labelText1 = new Label(colorString, labelStyle_announcement);
                labelText1.setColor(fontColor);
                Label labelText2 = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("color_disabled"), labelStyle_announcement);
                labelText2.setColor(new Color(1, 1, 1, 0.85f));
                float stringSize = labelText1.getWidth() + labelText2.getWidth() + 4;
                GroupNoticeLabel.setPosition( MathUtils.floor((screenWidth - stringSize) / 2), 2);

                labelText1.setPosition(0,0);
                labelText2.setPosition(labelText1.getWidth() + 4, 0);
                GroupNoticeLabel.addActor(labelText1);
                GroupNoticeLabel.addActor(labelText2);
                break;

            case CHALLENGE_COMPLETED: case CHALLENGE_FAILED:
                Label labelText;
                if(announcement == ANNOUNCEMENT.CHALLENGE_COMPLETED){
                    labelText = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_completed"), labelStyle_announcement);
                    labelText.setColor(new Color(253 / 255f,232 / 255f,127 / 255f,0.85f)); // Amarillo
                    //labelText.setColor(new Color(207 / 255f, 229 / 255f, 128 / 255f, 0.85f)); // Verde Pálido

                }
                else{
                    labelText = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_failed"), labelStyle_announcement);
                    labelText.setColor(new Color(254 / 255f, 98 / 255f, 87 / 255f, 0.85f)); // Rojo Pálido
                }
                GroupNoticeLabel.setSize(screenWidth, labelText.getHeight());

                Image star1 = new Image(atlas.findRegion("extraContentStar"));
                Image star2 = new Image(atlas.findRegion("extraContentStar"));

                labelText.setPosition( (GroupNoticeLabel.getWidth() / 2) - (labelText.getWidth() / 2), 2);
                star1.setPosition(labelText.getX() - 22,  4);
                star2.setPosition(labelText.getX() + labelText.getWidth() + 6,  4);

                GroupNoticeLabel.addActor(star1);
                GroupNoticeLabel.addActor(star2);
                GroupNoticeLabel.addActor(labelText);
                break;
        }
        return GroupNoticeLabel;
    }

    private void launchAnnouncementSound(){
        switch(announcement){
            case CHALLENGE_COMPLETED:
                AudioManager.getInstance().playSound(AudioManager.SOUND.CHALLENGE_COMPLETED_ANNOUNCEMENT);
                break;
            case CHALLENGE_FAILED:
                AudioManager.getInstance().playSound(AudioManager.SOUND.CHALLENGE_FAILED_ANNOUNCEMENT);
                break;
        }
    }

    public enum ANNOUNCEMENT{
        RED_DISABLED, BLUE_DISABLED, YELLOW_DISABLED, CHALLENGE_COMPLETED, CHALLENGE_FAILED;
    }
}
