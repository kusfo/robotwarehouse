package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial.TutorialSample;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.tutorials.TutorialDataElement;

import java.util.List;

/**
 * Created by JordiM on 08/10/2017.
 */

public class GUItutorialPanel extends GUIobjectBaseLevelPanel {
    private Group layer;
    private Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_11;
    private List<TutorialDataElement> tutorialDataElementList;
    private Group groupTutorialFramePanel, groupTutorialScreenPanel;
    private int screenWidth,screenHeight;
    private int currentPage;
    private Actor currentTutorialContent;
    private Label labelTutorialBody;
    private TutorialSample sample = null;
    private Color c_titulo, c_button;
    private TextureAtlas atlas;

    public GUItutorialPanel(GameEventsManager eventsManager, List<TutorialDataElement> tutorialDataElementList, Group layer){
        this.eventsManager = eventsManager;
        this.tutorialDataElementList = tutorialDataElementList;
        this.layer = layer;
        atlas = Assets.getTextureAtlas("tutorialPanel");
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_11 = new Label.LabelStyle();
        labelStyle_base_gb_11.font = Assets.getFont("f_base_gb_11");
        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_button = new Color(160/ 255f, 212/ 255f, 131/ 255f, 0.85f);
        this.currentPage = 0;
        execute();
    }

    private void execute() {

        Image imageTutorialFramePanel = new Image(atlas.findRegion("TutorialPanelFrame"));
        imageTutorialFramePanel.setPosition(0, 0);

        AnimatedImageActor animatedImageTutoriallScreen= new AnimatedImageActor(atlas, "TutorialPanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageTutoriallScreen.play();

        width = (int) imageTutorialFramePanel.getWidth();
        height = (int) imageTutorialFramePanel.getHeight();
        screenWidth = (int) animatedImageTutoriallScreen.getWidth();
        screenHeight = (int) animatedImageTutoriallScreen.getHeight();
        finalPositionY = GameConstants.VERTICAL_RESOLUTION - height + 50;
        groupTutorialFramePanel = new Group();
        groupTutorialFramePanel.setSize(width, height);
        groupTutorialFramePanel.setPosition(367 - (width / 2), GameConstants.VERTICAL_RESOLUTION + 100);

        groupTutorialScreenPanel = new Group();
        groupTutorialScreenPanel.setSize(screenWidth, screenHeight);
        groupTutorialScreenPanel.setPosition(28, 68);



        Label labelTutorialTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorial_title"), labelStyle_base_gb_22);
        labelTutorialTitle.setColor(c_titulo);
        labelTutorialTitle.setPosition((screenWidth / 2) - (labelTutorialTitle.getWidth() / 2), 204);

        currentTutorialContent = getTutorialContent();
        currentTutorialContent.setPosition(0, 20);

        groupTutorialFramePanel.addActor(groupTutorialScreenPanel);
        groupTutorialScreenPanel.addActor(animatedImageTutoriallScreen);
        groupTutorialScreenPanel.addActor(labelTutorialTitle);
        groupTutorialScreenPanel.addActor(currentTutorialContent);
        groupTutorialFramePanel.addActor(imageTutorialFramePanel);

        // Boton next
        TextureAtlas generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");
        TextButton.TextButtonStyle nextButtonStyle = new TextButton.TextButtonStyle();
        nextButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 1)));
        nextButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 2)));
        nextButtonStyle.font = Assets.getFont("f_base_gb_22");
        nextButtonStyle.fontColor = c_button;
        TextButton nextButton = new TextButton(GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorial_ok"), nextButtonStyle);
        nextButton.setPosition(119,25);
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(activePanel) {
                    if(currentPage < (tutorialDataElementList.size() - 1)) {
                        currentPage++;
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        if(sample != null) sample.DisposeEffects();
                        groupTutorialScreenPanel.removeActor(currentTutorialContent);
                        currentTutorialContent = getTutorialContent();
                        currentTutorialContent.setPosition(0, 20);
                        groupTutorialScreenPanel.addActor(currentTutorialContent);
                    } else {
                        if(sample != null) sample.DisposeEffects();
                        AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                        leave(groupTutorialFramePanel);
                    }
                }
            }
        });

        groupTutorialFramePanel.addActor(nextButton);
        layer.addActor(groupTutorialFramePanel);
        enter(groupTutorialFramePanel);
    }

    private Actor getTutorialContent() {
        if(tutorialDataElementList.get(currentPage).getCurrentSample() != null) {
            return getTutorialContentWithSample();
        } else {
            return getTutorialContentWithoutSample();
        }
    }

    private Actor getTutorialContentWithSample() {
        // Configuramos los espacios de contenidos (grupos)
        Group contentGroup = new Group();
        contentGroup.setSize(screenWidth, screenHeight - 80);

        Group sampleGroup = new Group();
        sampleGroup.setSize(325, 84);
        sampleGroup.setPosition((screenWidth - sampleGroup.getWidth()) / 2, 83);
        Group bodyGroup = new Group();
        bodyGroup.setSize(290, 76);
        bodyGroup.setPosition((screenWidth - 290) / 2, 4);

        // Llenamos los grupos
        labelTutorialBody = new Label(tutorialDataElementList.get(currentPage).getBody(), labelStyle_base_gb_11);
        labelTutorialBody.setColor(new Color(1, 1, 1, 0.85f));
        labelTutorialBody.setWrap(true);
        labelTutorialBody.setWidth(280);
        labelTutorialBody.setAlignment(Align.center);
        labelTutorialBody.setPosition((bodyGroup.getWidth() - labelTutorialBody.getWidth()) / 2,
                (bodyGroup.getHeight() - labelTutorialBody.getHeight()) / 2);
        bodyGroup.addActor(labelTutorialBody);

        Image sampleZoneImage = new Image(atlas.findRegion("Panel_Tutorial_AnimationZone"));
        sampleZoneImage.setPosition((sampleGroup.getWidth() / 2) - (sampleZoneImage.getWidth() / 2),0);
        sampleGroup.addActor(sampleZoneImage);

        sample = tutorialDataElementList.get(currentPage).getCurrentSample();
        sampleGroup.addActor(sample.getSampleGroup());

        contentGroup.addActor(sampleGroup);
        contentGroup.addActor(bodyGroup);

        return contentGroup;
    }

    private Actor getTutorialContentWithoutSample() {
        sample = null;
        // Configuramos el espacio del contenido (grupo)
        Group contentGroup = new Group();
        contentGroup.setSize(screenWidth, screenHeight - 80);
        Group bodyGroup = new Group();
        bodyGroup.setSize(290, contentGroup.getHeight());
        bodyGroup.setPosition((contentGroup.getWidth() - bodyGroup.getWidth()) / 2, 0);
        labelTutorialBody = new Label(tutorialDataElementList.get(currentPage).getBody(), labelStyle_base_gb_11);
        labelTutorialBody.setColor(new Color(1, 1, 1, 0.85f));
        labelTutorialBody.setWrap(true);
        labelTutorialBody.setAlignment(Align.center);

        // Usamos un container para centrarlo más facilmente
        Container labelContainer = new Container<Label>(labelTutorialBody);
        labelContainer.setPosition(bodyGroup.getWidth() / 2, bodyGroup.getHeight() / 2);
        bodyGroup.addActor(labelContainer);
        contentGroup.addActor(bodyGroup);

        return contentGroup;
    }
}
