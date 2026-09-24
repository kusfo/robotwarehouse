package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;

/**
 * Created by JordiRM on 07/06/2019.
 */
public class GUIobjectAboutPanel extends GUIobjectBaseLevelPanel{
    private Group layer;
    private Group groupAboutFramePanel, groupAboutScreenPanel, blockingBackgroundGroup;
    private Label.LabelStyle labelStyle_base_gb_22, labelStyle_base_gb_16, labelStyle_base_gb_13;
    private int screenWidth,screenHeight;
    private Color c_titulo, c_button, c_creditTitle;
    private TextureAtlas atlas;
    private Table table;
    private ScrollPane scrollPane;
    private boolean hasBeenDragged = false;
    public GUIobjectAboutPanel(Group layer){
        this.layer = layer;
        atlas = Assets.getTextureAtlas("tutorialPanel");
        labelStyle_base_gb_22 = new Label.LabelStyle();
        labelStyle_base_gb_22.font = Assets.getFont("f_base_gb_22");
        labelStyle_base_gb_16 = new Label.LabelStyle();
        labelStyle_base_gb_16.font = Assets.getFont("f_base_gb_16");
        labelStyle_base_gb_13 = new Label.LabelStyle();
        labelStyle_base_gb_13.font = Assets.getFont("f_base_gb_13");
        c_titulo = new Color(253 / 255f,232 / 255f,127 / 255f,0.85f);
        c_button = new Color(160/ 255f, 212/ 255f, 131/ 255f, 0.85f);
        c_creditTitle = new Color(59/255f, 164/255f, 253/255f, 0.85f);
        execute();
    }

    public void update(float delta){
        if(!hasBeenDragged){
            if(scrollPane.getScrollY() < scrollPane.getMaxY())
                scrollPane.setScrollY(scrollPane.getScrollY() + (30 * delta));
        }

        if(scrollPane.isPanning()){
            hasBeenDragged = true;
        }
        backAction();
    }

    private void execute(){
        addBlockingBackground();

        Image imageAboutFramePanel = new Image(atlas.findRegion("TutorialPanelFrame"));
        imageAboutFramePanel.setPosition(0, 0);
        imageAboutFramePanel.setTouchable(Touchable.disabled);

        AnimatedImageActor animatedImageAboutScreen= new AnimatedImageActor(atlas, "TutorialPanelScreen", 0.1f, Animation.PlayMode.LOOP);
        animatedImageAboutScreen.play();
        animatedImageAboutScreen.setTouchable(Touchable.disabled);

        width = (int) imageAboutFramePanel.getWidth();
        height = (int) imageAboutFramePanel.getHeight();
        screenWidth = (int) animatedImageAboutScreen.getWidth();
        screenHeight = (int) animatedImageAboutScreen.getHeight();
        groupAboutFramePanel = new Group();
        groupAboutFramePanel.setSize(width, height);
        groupAboutFramePanel.setPosition((GameConstants.HORIZONTAL_RESOLUTION / 2)  - (width / 2), GameConstants.VERTICAL_RESOLUTION + 100);

        groupAboutScreenPanel = new Group();
        groupAboutScreenPanel.setSize(screenWidth, screenHeight);
        groupAboutScreenPanel.setPosition(28, 68);

        groupAboutScreenPanel.addActor(animatedImageAboutScreen);

        Label labelaboutTitle = new Label(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_title"), labelStyle_base_gb_22);
        labelaboutTitle.setColor(c_titulo);
        labelaboutTitle.setPosition((screenWidth / 2) - (labelaboutTitle.getWidth() / 2), 204);

        // Creditos
        Label creditTitleLabel = new Label("Programación", labelStyle_base_gb_16);
        creditTitleLabel.setColor(c_creditTitle);
        creditTitleLabel.setPosition((screenWidth / 2) - (creditTitleLabel.getWidth() / 2), 150);

        Label creditNameLabel = new Label("Jordi Romero", labelStyle_base_gb_13);
        creditNameLabel.setColor(0.9f, 0.9f, 0.9f, 0.85f);
        creditNameLabel.setPosition((screenWidth / 2) - (creditNameLabel.getWidth() / 2), 120);

        groupAboutScreenPanel.addActor(labelaboutTitle);

        // Boton OK
        TextureAtlas generalButtonsAtlas = Assets.getTextureAtlas("general_buttons");
        TextButton.TextButtonStyle nextButtonStyle = new TextButton.TextButtonStyle();
        nextButtonStyle.up = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 1)));
        nextButtonStyle.down = new TextureRegionDrawable(new TextureRegion(generalButtonsAtlas.findRegion("panelTextButtonWide", 2)));
        nextButtonStyle.font = Assets.getFont("f_base_gb_22");
        nextButtonStyle.fontColor = c_button;
        TextButton okButton = new TextButton(GlobalGeneralData.getInstance().getGlobalBundleData().get("tutorial_ok"), nextButtonStyle);
        okButton.setPosition(119,25);
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioManager.getInstance().playSound(AudioManager.SOUND.BUTTON_GENERIC);
                if(activePanel) {
                    leave(groupAboutFramePanel);
                    removeBlockingBackground();
                }
            }
        });

        // Llenamos la tabla con los creditos
        table = new Table();
        addCreditsToTable();
        table.pack();

        scrollPane = new ScrollPane(table);
        scrollPane.setPosition(0, 30);
        scrollPane.setSize(340, 160);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFlingTime(0.3f);

        groupAboutScreenPanel.addActor(scrollPane);
        groupAboutFramePanel.addActor(groupAboutScreenPanel);
        groupAboutFramePanel.addActor(imageAboutFramePanel);
        groupAboutFramePanel.addActor(okButton);
        layer.addActor(groupAboutFramePanel);
        enter(groupAboutFramePanel, 0.15f);
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

    private void addCreditsToTable(){
        addSpace(90);
        addText("Robot Warehouse", Color.YELLOW);
        addSpace(40);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_programmation"), "Jordi Montornés", "Jordi Romero");
        addSpace(30);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_design&graphics"), "Jordi Romero");
        addSpace(30);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_levelDesign"), "Jordi Montornés", "Jordi Romero");
        addSpace(30);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_original_music"), "Nicole Marie T");
        addSpace(30);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_additional_music"), "Eric Matyas", "www.soundimage.org");
        addSpace(30);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_sound&FX"), "freesound.org", "freesfx.co.uk");
        addSpace(50);
        addCredit(GlobalGeneralData.getInstance().getGlobalBundleData().get("credits_developedBy"), "Mutant Ninja Coders", "(C) 2023");
        addSpace(20);
        addTextLink("[Website]", "https://www.mutantninjacoders.com");
    }

    private void addCredit(String title, String ... names){
        Label titleLabel = new Label(title, labelStyle_base_gb_16);
        titleLabel.setColor(c_creditTitle.r, c_creditTitle.g, c_creditTitle.b, 0.85f);
        titleLabel.setWrap(true);
        titleLabel.setWidth(320);
        titleLabel.setAlignment(Align.center);
        table.add(titleLabel).width(320);
        table.row();

        for(String n: names){
            Label nameLabel = new Label(n, labelStyle_base_gb_13);
            nameLabel.setColor(0.92f, 0.92f, 0.92f, 0.85f);
            nameLabel.setWrap(true);
            nameLabel.setWidth(320);
            nameLabel.setAlignment(Align.center);
            table.add(nameLabel).width(320);
            table.row();
        }
        table.row();
    }

    private void addText(String text, Color color){
        Label textLabel = new Label(text, labelStyle_base_gb_16);
        textLabel.setColor(color.r, color.g, color.b, 0.85f);
        textLabel.setWrap(true);
        textLabel.setWidth(320);
        textLabel.setAlignment(Align.center);
        table.add(textLabel).width(320);
        table.row();
    }

    private void addTextLink(String text, final String link){
        Label textLabel = new Label(text, labelStyle_base_gb_16);
        textLabel.setColor(Color.SKY.r, Color.SKY.g, Color.SKY.b, 0.85f);
        textLabel.setWrap(true);
        textLabel.setWidth(320);
        textLabel.setAlignment(Align.center);
        textLabel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(link);
            }
        });
        table.add(textLabel).width(320);
        table.row();
    }

    private void addSpace(int space){
        table.add().height(space);
        table.row();
    }

    private void backAction() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if(activePanel) {
                leave(groupAboutFramePanel);
                removeBlockingBackground();
            }
        }
    }
}
