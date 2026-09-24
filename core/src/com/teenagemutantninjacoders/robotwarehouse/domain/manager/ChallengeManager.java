package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectAnnouncement;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GameUI;

/**
 * Created by JordiRM on 22/05/2018.
 */
public class ChallengeManager {
    private GameUI gameUI;
    private GlobalAttributes.CHALLENGE actualChallenge = GlobalAttributes.CHALLENGE.NONE;
    private int challengeValue = 0;
    private GlobalAttributes.CHALLENGE_OBJECT challengeObject = GlobalAttributes.CHALLENGE_OBJECT.NONE;
    private CHALLENGE_STATUS challengeStatus = CHALLENGE_STATUS.WAITING;
    private boolean challengeActive = false;

    private String challengeDescription;

    private GlobalAttributes.COLOR colorValue;

    private boolean trg_anyLostBox, trg_enemySpawned, trg_enemyDestroyed, trg_powerActivated;
    private int trg_comboAmount;
    private GlobalAttributes.COLOR trg_color = GlobalAttributes.COLOR.NONE;

    private static ChallengeManager instance;
    public static ChallengeManager getInstance(){
        if(instance == null) {
            instance = new ChallengeManager();
        }
        return instance;
    }

    public void initialize(GameUI gameEventsManager){
        this.gameUI = gameEventsManager;
        actualChallenge = GlobalLevelData.getInstance().getActualChallenge();
        challengeValue = GlobalLevelData.getInstance().getActualChallengeValue();
        challengeObject = GlobalLevelData.getInstance().getActualChallengeObject();
        challengeStatus = CHALLENGE_STATUS.WAITING;
        if(actualChallenge != GlobalAttributes.CHALLENGE.NONE) challengeActive = true;
        trg_anyLostBox = false;
        trg_enemySpawned = false;
        trg_enemyDestroyed = false;
        trg_powerActivated = false;
        trg_comboAmount = 0;
        trg_color = GlobalAttributes.COLOR.NONE;
        colorValue = getColorFromChallengeObject(); // Adquirimos el color en base al valor por si se fuera a utilizar
        challengeDescription = extractChallengeDescription();
    }

    public void update() {
        if (challengeActive) {
            // Agunos challenges los supervisa directamente el manager
            processActiveChallenges();

            if (challengeStatus == CHALLENGE_STATUS.COMPLETED) {
                gameUI.newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT.CHALLENGE_COMPLETED);
                challengeActive = false;
            } else if (challengeStatus == CHALLENGE_STATUS.FAILED) {
                gameUI.newAnnouncement(GUIobjectAnnouncement.ANNOUNCEMENT.CHALLENGE_FAILED);
                challengeActive = false;
            }
        }
    }

    private void processActiveChallenges(){
        switch (actualChallenge) {
            case TIME_ELAPSED:
                if (GlobalLevelData.getInstance().getLevelTime() < GlobalLevelData.getInstance().getInitialLevelTime() - challengeValue)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;
            case MAX_MOVES:
                if(GlobalLevelData.getInstance().getPlayerMoves() > challengeValue)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;

            // Los trg_ son eventos genéricos que pueden afectar a más de un desafio
            case NO_LOSSED_BOX:
                if (trg_anyLostBox)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;
            case BOX_COMBO:
                if (trg_comboAmount >= challengeValue)
                    challengeStatus = CHALLENGE_STATUS.COMPLETED;
                break;
            case LAST_COLOR:
                if(GlobalLevelData.getInstance().getRequestedBoxes() > 0){
                    if(trg_color == colorValue && GlobalLevelData.getInstance().getRemainingBoxes().getColor(colorValue) < 3) {
                        challengeStatus = CHALLENGE_STATUS.FAILED;
                    }
                }
            case NO_ENEMY:
                if(trg_enemySpawned)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;
            case ENEMY_SAFE:
                if(trg_enemyDestroyed)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;

            case NO_POWERS:
                if(trg_powerActivated)
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                break;
            case NO_BARRIER:
                if(trg_color == colorValue || (trg_color != GlobalAttributes.COLOR.NONE && colorValue == GlobalAttributes.COLOR.ANY)) {
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                }
                break;
        }
    }

    public boolean isChallengeCompleted() {
        return challengeStatus == CHALLENGE_STATUS.COMPLETED;
    }

    public void processEndLevelChallengeCompletion(){
        challengeActive = false;
        // Los desafios que llegan al final en estado Waiting hay que comprobarlos para ver si se condiseran completados o no
        if(challengeStatus == CHALLENGE_STATUS.WAITING){
            switch(actualChallenge){
                case NO_LOSSED_BOX: case TIME_ELAPSED: case MAX_MOVES: case NO_ENEMY: case ENEMY_SAFE: case NO_POWERS: case NO_BARRIER:
                    challengeStatus = CHALLENGE_STATUS.COMPLETED;
                    break;
                case LAST_COLOR:
                    if(trg_color == colorValue) {
                        challengeStatus = CHALLENGE_STATUS.COMPLETED;
                    }
                    break;
                default:
                    challengeStatus = CHALLENGE_STATUS.FAILED;
                    break;
            }
        }
    }

    public void notifyChallengeTrigger(CHALLENGE_TRIGGER trigger){
        if(!challengeActive) return;
        if(challengeStatus != CHALLENGE_STATUS.WAITING) return;
        switch (trigger){
            case TRG_LOST_BOX: trg_anyLostBox = true;
                break;
            case TRG_POWER_ACTIVATED: trg_powerActivated = true;
                break;
        }
    }

    public void notifyChallengeTrigger(CHALLENGE_TRIGGER trigger, int amount){
        if(!challengeActive) return;
        if(challengeStatus != CHALLENGE_STATUS.WAITING) return;
        switch (trigger){
            case TRG_BOX_COMBO:
                if(amount > trg_comboAmount) trg_comboAmount = amount;
                break;
        }
    }

    public void notifyChallengeTrigger(CHALLENGE_TRIGGER trigger, GlobalAttributes.CHALLENGE_OBJECT objectInvolved){
        if(!challengeActive) return;
        if(challengeStatus != CHALLENGE_STATUS.WAITING) return;
        switch (trigger) {
            case TRG_ENEMY_SPAWNED:
                if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ROBOT && getIsRobot(objectInvolved))
                    trg_enemyDestroyed = true;
                else if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ALL || challengeObject == objectInvolved)
                    trg_enemySpawned = true;
                break;
            case TRG_ENEMY_DESTROYED:
                if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ROBOT && getIsRobot(objectInvolved))
                    trg_enemyDestroyed = true;
                else if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ALL || challengeObject == objectInvolved)
                    trg_enemyDestroyed = true;
        }
    }

    public void notifyChallengeTrigger(CHALLENGE_TRIGGER trigger, GlobalAttributes.COLOR color){
        if(!challengeActive) return;
        if(challengeStatus != CHALLENGE_STATUS.WAITING) return;
        switch (trigger) {
            case TRG_TRANSPORTED_COLOR:
                if(actualChallenge == GlobalAttributes.CHALLENGE.LAST_COLOR) trg_color = color;
                break;
            case TRG_BARRIER_ACTIVATED:
                if(actualChallenge == GlobalAttributes.CHALLENGE.NO_BARRIER) trg_color = color;
                break;
        }
    }

    private String extractChallengeDescription(){
        switch(actualChallenge){
            case NO_LOSSED_BOX: return GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_desc_no_lossed_box");
            case TIME_ELAPSED:  return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_time_elapsed", challengeValue);
            case BOX_COMBO:     return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_box_combo", challengeValue);
            case MAX_MOVES:     return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_max_movements", challengeValue);
            case NO_POWERS:     return GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_desc_no_powers");
            case NO_BARRIER:
                if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ANY || challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ALL)
                    return GlobalGeneralData.getInstance().getGlobalBundleData().get("challenge_desc_no_barriers");
                else
                    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_no_barrier",
                            GlobalGeneralData.getInstance().getGlobalBundleData().format(challengeObject.getValue()).toLowerCase());

            case LAST_COLOR:    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_last_color",
                    GlobalGeneralData.getInstance().getGlobalBundleData().format(challengeObject.getValue()).toLowerCase());
            case NO_ENEMY:
                if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ALL)
                    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_no_enemy",
                            GlobalGeneralData.getInstance().getGlobalBundleData().format("enemy").toLowerCase());
                else
                    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_no_enemy",
                            GlobalGeneralData.getInstance().getGlobalBundleData().format(challengeObject.getValue()).toLowerCase());
            case ENEMY_SAFE:
                if(challengeObject == GlobalAttributes.CHALLENGE_OBJECT.ALL)
                    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_enemy_safe",
                            GlobalGeneralData.getInstance().getGlobalBundleData().format("enemy").toLowerCase());
                else
                    return GlobalGeneralData.getInstance().getGlobalBundleData().format("challenge_desc_enemy_safe",
                            GlobalGeneralData.getInstance().getGlobalBundleData().format(challengeObject.getValue()).toLowerCase());
        }
        return "no description";
    }

    public String getChallengeDescription(){
        return challengeDescription;
    }

    private GlobalAttributes.COLOR getColorFromChallengeObject(){
        switch (challengeObject){
            case RED: return GlobalAttributes.COLOR.RED;
            case BLUE: return GlobalAttributes.COLOR.BLUE;
            case YELLOW: return GlobalAttributes.COLOR.YELLOW;
            case GREEN: return GlobalAttributes.COLOR.GREEN;
            case ORANGE: return GlobalAttributes.COLOR.ORANGE;
            case PURPLE: return GlobalAttributes.COLOR.PURPLE;
            case ANY: return GlobalAttributes.COLOR.ANY;
            default: return GlobalAttributes.COLOR.NONE;
        }
    }

    private boolean getIsRobot(GlobalAttributes.CHALLENGE_OBJECT objectInvolved){
        return objectInvolved == GlobalAttributes.CHALLENGE_OBJECT.PUSHER_ROBOT || objectInvolved == GlobalAttributes.CHALLENGE_OBJECT.EXPLOSIVE_ROBOT;
    }

    public enum CHALLENGE_TRIGGER{
        TRG_LOST_BOX, TRG_BOX_COMBO, TRG_TRANSPORTED_COLOR, TRG_ENEMY_SPAWNED, TRG_ENEMY_DESTROYED, TRG_POWER_ACTIVATED, TRG_BARRIER_ACTIVATED
    }

    public enum CHALLENGE_STATUS {
        WAITING, COMPLETED, FAILED
    }
}
