package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Json;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DialogueEventDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.IntroductionEventDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials.TutorialDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials.TutorialDataElementDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.tutorials.TutorialElementDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.mappers.TutorialElementDTOtoTutorialElementMapper;
import com.teenagemutantninjacoders.robotwarehouse.data.parsers.TutorialParser;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.TutorialSampleProviderImpl;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.GameEventsManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.tutorials.TutorialDataElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordiRM on 18/09/2018.
 */
public class LevelIntroductionEvent extends GUIobject {
    private EVENT_TYPE actualEventType;
    private Group layer;
    private GameEventsManager eventsManager;
    private GUIobjectCharacterDialogue guiDialogue;
    private GUItutorialPanel guiTutorial;
    private String actualEvent;
    private IntroductionEventDTO subEventsList;
    private GUIobjectTutorialArrow tutorialArrow;

    public LevelIntroductionEvent(String event, Group layer, GameEventsManager eventsManager, GUIobjectCharacterDialogue characterDialogue){
        this.actualEvent = event;
        this.layer = layer;
        this.eventsManager = eventsManager;
        guiDialogue = characterDialogue;
        loadEvent(event);
        executeNextSubEvent();
    }

    private void loadEvent(String event){
        FileHandle file = Gdx.files.internal("data/introduction_events/" + event + ".json");
        String eventJson = file.readString();
        Json json = new Json();
        subEventsList = json.fromJson(IntroductionEventDTO.class, eventJson);
    }

    @Override
    public void update(float delta){
        if(actualEventType == EVENT_TYPE.DIALOGUE){
            if(guiDialogue.getAwaitingDialogueEvents().size() > 0){
                executeDialogueEvents(guiDialogue.getAwaitingDialogueEvents());
            }
            if(!guiDialogue.hasDialogueActive()){
                finishActualEvent();
            }
        }
        else if(actualEventType == EVENT_TYPE.TUTORIAL){
            if(guiTutorial.isPanelGone()){
                finishActualEvent();
            }
        }
    }

    private void executeNextSubEvent() {
        actualEventType = EVENT_TYPE.fromString(subEventsList.getSubEvents().get(0).getType());
        if(actualEventType == EVENT_TYPE.DIALOGUE){
            guiDialogue.launchDialogue(subEventsList.getSubEvents().get(0).getId(), false, 0.5f);
        }
        else if(actualEventType == EVENT_TYPE.TUTORIAL){
            if(!GlobalGeneralData.getInstance().debug_tutorialdisabled)
                guiTutorial = new GUItutorialPanel(eventsManager, getTutorialData(subEventsList.getSubEvents().get(0).getId()), layer);
            else
                finishActualEvent();
        }
    }

    private void finishActualEvent(){
        subEventsList.getSubEvents().remove(0);
        if(subEventsList.getSubEvents().size() > 0){
            executeNextSubEvent();
        }else{
            actualEventType = EVENT_TYPE.NONE;
            eventsManager.addFinishLevelIntroductionEvent();
            setFinalized(true);
            // Guardamos que lo hemos visto
            if(subEventsList.isSingleview()) {
                GlobalPreferencesData.getInstance().setLevelIntroductionEventViewCompleted(actualEvent);
            }

            if(tutorialArrow != null) {
                tutorialArrow.removeArrow();
                tutorialArrow = null;
            }
        }
    }

    private List<TutorialDataElement> getTutorialData(String id){
        FileHandle file = Gdx.files.internal("data/tutorials/tutorials.json");
        String levelJson = file.readString();
        TutorialDTO tutorialDTO = TutorialParser.parseTutorial(levelJson);
        for(TutorialElementDTO tutorialElementDTO: tutorialDTO.getTutorials()) {
            if(id.equals(tutorialElementDTO.getId())) {
                TutorialElementDTOtoTutorialElementMapper tutorialElementDTOtoTutorialElementMapper = new TutorialElementDTOtoTutorialElementMapper(new TutorialSampleProviderImpl());
                ArrayList<TutorialDataElement> tutorialDataElementList = new ArrayList<TutorialDataElement>();
                for(TutorialDataElementDTO tutorialDataElementDTO : tutorialElementDTO.getElements()) {
                    tutorialDataElementList.add(tutorialElementDTOtoTutorialElementMapper.transform(tutorialDataElementDTO));
                }
                return tutorialDataElementList;
            }
        }
        return null;
    }

    private void executeDialogueEvents(ArrayList<DialogueEventDTO> events){
        for(DialogueEventDTO event : events){
            switch (event.getEvent()){
                case GAME_ZONE_FADE_IN:
                    eventsManager.addGameZoneFade(false, 1);
                    break;
                case DEPLOY_SPACE_DOCK_MONITOR:
                    eventsManager.deploySpaceDockMonitor(true);
                    break;
                case DEPLOY_SCORE_PANELS:
                    eventsManager.deployScorePanels(0.2f);
                    break;
                case RETRACT_SCORE_PANELS:
                    eventsManager.retractScorePanels(0.3f);
                    break;
                case DEPLOY_PAUSE_BUTTON_PANEL:
                    eventsManager.deployPauseButtonPanel(0.2f);
                    break;
                case SHOW_UP_ARROW: case SHOW_DOWN_ARROW: case SHOW_LEFT_ARROW: case SHOW_RIGHT_ARROW:
                    tutorialArrow = new GUIobjectTutorialArrow(layer, event.getValue1(), event.getValue2(), event.getEvent(), 0.8f);
                    break;
                case REMOVE_ARROW:
                    if(tutorialArrow != null) {
                        tutorialArrow.removeArrow();
                        tutorialArrow = null;
                    }
                    break;
                case CANNON_SHOOT:
                    eventsManager.executeScenarioEvent(1);
                    break;
            }
        }
        guiDialogue.clearDialogueEvents();
    }

    public enum EVENT_TYPE{
        NONE("none"), DIALOGUE("dialogue"), TUTORIAL("tutorial");
        private String value;
        EVENT_TYPE(String newValue) {
            setValue(newValue);
        }
        public String getValue() {
            return value;
        }
        public void setValue(String newValue) {
            value = newValue;
        }
        public static EVENT_TYPE fromString(String text) {
            if (text != null) {
                for (EVENT_TYPE var : EVENT_TYPE.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return NONE;
        }
    }
}