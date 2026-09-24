package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DialogueDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DialogueEventDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * Created by JordiRM on 11/09/2018.
 */
public class GUIobjectCharacterDialogue {
    private Group layer;
    private TextureAtlas atlas;
    private Label.LabelStyle labelStyle_charDialog_18;
    private Group generalGroup;
    private float characterPosX, characterPosY;
    private Group bubbleGroup;
    private Image characterImage;
    private Group touchScreen;
    private float characterImageDisplacementX = 0;
    private String dialogueName;
    private boolean saveViewing = false;
    private ArrayList<DialogueEventDTO> awaitingDialogueEvents = new ArrayList<DialogueEventDTO>();

    private DialogueDTO dialogueDTO;
    private int actualPage = 1;
    private SIDE side;
    private float waitToTap = 0;
    private boolean automatic = false;
    private int bubbleDespX = 0;
    private int bubbleDespY = 0;

    private boolean touchReady = false;
    private boolean dialogueActive = false;

    public GUIobjectCharacterDialogue(Group layer){
        this.layer = layer;
        generalGroup = new Group();
        labelStyle_charDialog_18 = new Label.LabelStyle();
        labelStyle_charDialog_18.font = Assets.getFont("f_character_dialogue_18");
        initialize();
    }

    private void initialize() {
        atlas = Assets.getTextureAtlas("character_conversation");
        characterPosY = -39;
    }

    private void loadDialogue(String dialogueName){
        FileHandle file = Gdx.files.internal("data/dialogues/" + dialogueName + ".json");

        String dialogueJson = file.readString();
        Json json = new Json();
        dialogueDTO = json.fromJson(DialogueDTO.class, dialogueJson);
    }

    public void launchDialogue(String dialogueName, boolean saveViewing, float delay){
        if(!GlobalGeneralData.getInstance().debug_dialoguedisabled) {
            this.dialogueName = dialogueName;
            this.saveViewing = saveViewing;
            dialogueActive = true;
            loadDialogue(dialogueName);
            actualPage = 1;
            side = SIDE.fromString(dialogueDTO.getSide());
            bubbleDespX = dialogueDTO.getBubbleDespX();
            bubbleDespY = dialogueDTO.getBubbleDespY();
            waitToTap = dialogueDTO.getPages().get(actualPage - 1).getWaitTime();
            automatic = dialogueDTO.getPages().get(actualPage - 1).isAutomatic();
            if (side == SIDE.RIGHT) characterPosX = 460;
            else characterPosX = 0;

            CHARACTER_POSE pose =  CHARACTER_POSE.fromString(dialogueDTO.getPages().get(actualPage - 1).getPose());
            layer.addActor(generalGroup);
            createCharacter(pose, delay);
            createBubble(delay + 0.4f);
            fillEventsList();

            touchScreen = new Group();
            touchScreen.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
            touchScreen.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (touchReady && awaitingDialogueEvents.size() == 0) {
                        nextPage();
                    }
                    return true;
                }
            });
            generalGroup.addActor(touchScreen);
        }else
            dialogueActive = false;
    }

    public void launchCharacterAlone(CHARACTER_POSE pose, SIDE side, float delay, int additionalDespX){
        this.side = side;
        if (side == SIDE.RIGHT) characterPosX = 460 + additionalDespX;
        else characterPosX = 0 + additionalDespX;
        layer.addActor(generalGroup);
        createCharacter(pose, delay);
    }

    public void forceLeaveCharacter(){
       if(characterImage != null) characterLeave(true);
    }

    private void fillEventsList(){
        ArrayList<DialogueEventDTO> tempList = dialogueDTO.getPages().get(actualPage - 1).getEvents();
        if(tempList.size() > 0){
            for(int i = 0; i < tempList.size(); i++){
                awaitingDialogueEvents.add(tempList.get(i));
            }
        } else {
            awaitingDialogueEvents.clear();
        }
    }

    private void createCharacter(CHARACTER_POSE pose, float delay){
        characterImage = new Image();
        assignCharacterPose(pose, true);
        characterImage.setColor(1, 1, 1, 0);
        characterImage.setTouchable(Touchable.disabled);

        MoveToAction moveToAction = new MoveToAction();
        moveToAction.setPosition(characterPosX + characterImageDisplacementX, characterPosY);
        moveToAction.setDuration(0.4f);
        moveToAction.setInterpolation(Interpolation.pow2Out);
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.4f);

        SequenceAction sequenceAction = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction(moveToAction, alphaAction);
        sequenceAction.addAction(new DelayAction(delay));
        sequenceAction.addAction(parallelAction);
        characterImage.addAction(sequenceAction);

        generalGroup.addActor(characterImage);
    }

    private void characterLeave(boolean destroyOnFinalize){
        MoveToAction moveToAction = new MoveToAction();
        if(side == SIDE.RIGHT) moveToAction.setPosition(characterPosX + 80, characterPosY - 25);
        else moveToAction.setPosition(characterPosX - 80, characterPosY - 25);
        moveToAction.setDuration(0.3f);
        moveToAction.setInterpolation(Interpolation.exp5In);
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.3f);

        SequenceAction sequenceAction = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction(moveToAction, alphaAction);
        sequenceAction.addAction(parallelAction);
        if(destroyOnFinalize) sequenceAction.addAction(new RemoveActorAction());
        characterImage.addAction(sequenceAction);
    }

    private void assignCharacterPose(CHARACTER_POSE pose, boolean entering){
        int poseNumber;
        switch(pose){
            case SHOWING:
                poseNumber = 2;
                if(side == SIDE.RIGHT) characterImageDisplacementX = -67;
                break;
            case CELEBRATING:
                poseNumber = 3;
                characterImageDisplacementX = 0;
                break;
            case DISCOURAGED:
                poseNumber = 4;
                characterImageDisplacementX = 30;
                break;
            case WORRIED:
                poseNumber = 5;
                if(side == SIDE.RIGHT) characterImageDisplacementX = -15;
                break;
            default:
                poseNumber = 1;
                characterImageDisplacementX = 0;
                break;
        }
        String characterSet;
        if(side == SIDE.RIGHT) characterSet = "character_right";
        else characterSet = "character_left";
        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion(characterSet, poseNumber);
        characterImage.setDrawable(new SpriteDrawable(new Sprite(atlasRegion)));
        characterImage.setSize(atlasRegion.getRegionWidth(),atlasRegion.getRegionHeight() );
        if(entering){
            if(side == SIDE.RIGHT)
                characterImage.setPosition(characterPosX + characterImageDisplacementX + 80, characterPosY - 25);
            else
                characterImage.setPosition(characterPosX + characterImageDisplacementX - 80, characterPosY - 25);
        } else {
            characterImage.setPosition(characterPosX + characterImageDisplacementX, characterPosY);
        }
    }

    private void createBubble(float delay){
        Image bubbleImage = new Image(atlas.findRegion("text_bubble", dialogueDTO.getBubbleType()));
        String text = GlobalGeneralData.getInstance().getGlobalBundleData().get(dialogueDTO.getPages().get(actualPage - 1).getTextReference());

        Label textLabel = new Label(text, labelStyle_charDialog_18);
        textLabel.setAlignment(Align.center);
        textLabel.setWrap(true);
        textLabel.setWidth(220);

        GlyphLayout layout = new GlyphLayout(labelStyle_charDialog_18.font, text, Color.WHITE, 220, Align.left, true );
        bubbleImage.setHeight(layout.height + 80);
        if (side == SIDE.LEFT ) bubbleImage.setScaleX(-1);

        bubbleGroup = new Group();
        bubbleGroup.setTouchable(Touchable.disabled);
        bubbleGroup.setSize(bubbleImage.getWidth(), bubbleImage.getHeight());
        bubbleGroup.setColor(1, 1, 1, 0);
        bubbleGroup.setScale(0.2f, 0.6f);

        if (side == SIDE.RIGHT) textLabel.setPosition(29, (bubbleGroup.getHeight() / 2) - 12);
        else textLabel.setPosition(-bubbleImage.getWidth() + 61, (bubbleGroup.getHeight() / 2) - 12);

        // Nos aseguramos que el bocadillo no se salga de la parte superior o inferior
        float bubbleFinalPosY = MathUtils.clamp(165 + bubbleDespY, 0, GameConstants.VERTICAL_RESOLUTION - bubbleGroup.getHeight());

        if (side == SIDE.RIGHT){
            bubbleGroup.setPosition(characterPosX - bubbleImage.getWidth() + 10 + bubbleDespX, bubbleFinalPosY); // +20, 145
            bubbleGroup.setOrigin(Align.right);
        }
        else {
            bubbleGroup.setPosition(characterPosX + 165 + bubbleImage.getWidth() + bubbleDespX, bubbleFinalPosY);
            bubbleGroup.setOrigin( -bubbleGroup.getWidth() - 50, bubbleGroup.getHeight() / 2);
        }

        // Aparición
        ScaleToAction scaleToAction = new ScaleToAction();
        scaleToAction.setScale(1);
        scaleToAction.setDuration(0.6f); // 0.6
        scaleToAction.setInterpolation(Interpolation.elasticOut);
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(1);
        alphaAction.setDuration(0.3f);
        RunnableAction showRunnable = new RunnableAction();
        showRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                AudioManager.getInstance().playSound(AudioManager.SOUND.DIALOG_BUBBLE);
            }
        });
        RunnableAction touchReadyRunnable = new RunnableAction();
        touchReadyRunnable.setRunnable(new Runnable() {
            @Override
            public void run() {
                touchReady = true;
                // Si el automatico está en true, pasamos de página directamente
                if(automatic) nextPage();
            }
        });
        SequenceAction runnableSequence = new SequenceAction();
        runnableSequence.addAction(new DelayAction(0.2f + waitToTap));
        runnableSequence.addAction(touchReadyRunnable);

        SequenceAction sequenceAction = new SequenceAction();
        ParallelAction parallelAction = new ParallelAction(showRunnable, scaleToAction, alphaAction, runnableSequence);
        sequenceAction.addAction(new DelayAction(delay));
        sequenceAction.addAction(parallelAction);
        bubbleGroup.addAction(sequenceAction);
        bubbleGroup.addActor(bubbleImage);
        bubbleGroup.addActor(textLabel);
        generalGroup.addActor(bubbleGroup);
    }

    private void finishBubble(){
        AlphaAction alphaAction = new AlphaAction();
        alphaAction.setAlpha(0);
        alphaAction.setDuration(0.2f);
        bubbleGroup.addAction(alphaAction);
    }

    private void nextPage(){
        if(actualPage < dialogueDTO.getPages().size()){
            actualPage +=1;
            touchReady = false;
            fillEventsList();
            waitToTap = dialogueDTO.getPages().get(actualPage - 1).getWaitTime();
            automatic = dialogueDTO.getPages().get(actualPage - 1).isAutomatic();
            finishBubble();
            createBubble(0.2f);
            CHARACTER_POSE pose =  CHARACTER_POSE.fromString(dialogueDTO.getPages().get(actualPage - 1).getPose());
            assignCharacterPose(pose, false);
        } else {
            finishDialogue();
        }
    }

    private void finishDialogue(){
        touchScreen.remove();
        touchReady = false;
        finishBubble();
        characterLeave(false);
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                dialogueActive = false;
                if(saveViewing) GlobalPreferencesData.getInstance().setDialogueViewCompleted(dialogueName);
            }
        });
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(new DelayAction(0.35f));
        sequenceAction.addAction(runnableAction);
        sequenceAction.addAction(new RemoveActorAction());
        generalGroup.addAction(sequenceAction);
    }

    public boolean hasDialogueActive(){
        return dialogueActive;
    }

    public ArrayList<DialogueEventDTO> getAwaitingDialogueEvents(){
        return awaitingDialogueEvents;
    }

    public void clearDialogueEvents(){
        awaitingDialogueEvents.clear();
    }

    public int getActualPage(){
        return actualPage;
    }

    public enum CHARACTER_POSE {
        NEUTRAL("neutral"), SHOWING("showing"), CELEBRATING("celebrating"), DISCOURAGED("discouraged"), WORRIED("worried");
        private String value;

        CHARACTER_POSE(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static CHARACTER_POSE fromString(String text) {
            if (text != null) {
                for (CHARACTER_POSE var : CHARACTER_POSE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NEUTRAL;
        }
    }

    public enum SIDE {
        LEFT("left"), RIGHT("right");
        private String value;

        SIDE(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static SIDE fromString(String text) {
            if (text != null) {
                for (SIDE var : SIDE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return RIGHT;
        }
    }

    public enum DIALOGUE_EVENT{
        NONE("none"), GAME_ZONE_FADE_IN("game_zone_fade_in"),  DEPLOY_SPACE_DOCK_MONITOR("deploy_spaceDockMonitor"),
        DEPLOY_SCORE_PANELS("deploy_scorePanels"), RETRACT_SCORE_PANELS("retract_scorePanels"),
        DEPLOY_PAUSE_BUTTON_PANEL("deploy_pauseButtonPanel"),
        SHOW_UP_ARROW("show_up_arrow"), SHOW_DOWN_ARROW("show_down_arrow"), SHOW_LEFT_ARROW("show_left_arrow"), SHOW_RIGHT_ARROW("show_right_arrow"), REMOVE_ARROW("remove_arrow"),
        PAUSE_EVENT("pause_event"), ADVANCE("advance"), MAP_MOVE_TO_POSITION("map_move_to_position"), CANNON_SHOOT("cannon_shoot");
        private String value;

        DIALOGUE_EVENT(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static DIALOGUE_EVENT fromString(String text) {
            if (text != null) {
                for (DIALOGUE_EVENT var : DIALOGUE_EVENT.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }
}
