package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalPreferencesData;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.IdProvider;
import com.teenagemutantninjacoders.robotwarehouse.data.providers.interfaces.TrackingServices;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.enums.GameAchievement;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.AchievementManager;

import java.util.ArrayList;
import java.util.Set;

import de.golfgl.gdxgamesvcs.IGameServiceClient;

public class AchievementManagerImpl implements AchievementManager {
    private final IGameServiceClient playServices;
    private final TrackingServices trackingServices;
    private final IdProvider idProvider;

    public AchievementManagerImpl(IGameServiceClient playServices, TrackingServices trackingServices, IdProvider idProvider) {
        this.playServices = playServices;
        this.trackingServices = trackingServices;
        this.idProvider = idProvider;
    }

    public void checkAchievement(GameAchievement achievement) {
        addWaitingAchievement(achievement);
    }

    @Override
    public void checkLevelCompletedRelatedAchievement(int episodeNumber, int levelNumber, boolean firstTimeBeat, GlobalAttributes.LEVEL_TYPE level_type) {
        if(episodeNumber == 1 && levelNumber == 1 && firstTimeBeat) {
            addWaitingAchievement(GameAchievement.FIRST_LEVEL_COMPLETED);
        } else if(level_type.equals(GlobalAttributes.LEVEL_TYPE.SPECIAL_CHALLENGE) && firstTimeBeat) {
            manageSpecialLevelCompleted();
        }
    }

    @Override
    public void checkBoxesTeleportRelatedAchievement(int combo) {
        int oldBoxesTeleported = GlobalPreferencesData.getInstance().getBoxesTeleported();
        GlobalPreferencesData.getInstance().addBoxesTeleported(combo);
        int newBoxesTeleported = GlobalPreferencesData.getInstance().getBoxesTeleported();
        if(oldBoxesTeleported < 100 && newBoxesTeleported >= 100) {
            addWaitingAchievement(GameAchievement.ONE_HUNDRED_BOXES_TELEPORTED);
        }
        else if(oldBoxesTeleported < 500 && newBoxesTeleported >= 500) {
            addWaitingAchievement(GameAchievement.FIVE_HUNDRED_BOXES_TELEPORTED);
        }
        else if(oldBoxesTeleported < 1000 && newBoxesTeleported >= 1000) {
            addWaitingAchievement(GameAchievement.ONE_THOUSAND_BOXES_TELEPORTED);
        }
    }

    @Override
    public void checkPointsRelatedAchievement() {
        if(GlobalLevelData.getInstance().getOverBasePointsActivated() == GlobalLevelData.getInstance().getInitialOverBasePoints()) {
            addWaitingAchievement(GameAchievement.FIRST_ALL_POINTS_COLLECTED);
        }
    }

    @Override
    public void checkRobotRelatedAchievement() {
        int totalRobotsKilled = GlobalPreferencesData.getInstance().getRobotsKilled();
        int robotsKilledOnLevel = GlobalLevelData.getInstance().getRobotsKilled();
        if(totalRobotsKilled == 1) {
            addWaitingAchievement(GameAchievement.FIRST_ROBOT_KILLED);
        }
        if(robotsKilledOnLevel == 10) {
            addWaitingAchievement(GameAchievement.TEN_ROBOTS_SMASHED_ON_LEVEL);
        }
        if(robotsKilledOnLevel == 20) {
            addWaitingAchievement(GameAchievement.TWENTY_ROBOTS_SMASHED_ON_LEVEL);
        }
        if(robotsKilledOnLevel == 30) {
            addWaitingAchievement(GameAchievement.THIRTY_ROBOTS_SMASHED_ON_LEVEL);
        }
    }

    @Override
    public void checkChallengeRelatedAchievement(int currentEpisode) {
        int totalChallengesCompleted = GlobalPreferencesData.getInstance().getTotalChallengesCompleted();
        if(totalChallengesCompleted == 1) {
            addWaitingAchievement(GameAchievement.ONE_CHALLENGE_COMPLETED);
        } else if(totalChallengesCompleted == 5) {
            addWaitingAchievement(GameAchievement.FIVE_CHALLENGE_COMPLETED);
        } else if(totalChallengesCompleted == 10) {
            addWaitingAchievement(GameAchievement.TEN_CHALLENGE_COMPLETED);
        } else if(totalChallengesCompleted == 15) {
            addWaitingAchievement(GameAchievement.FIFTEEN_CHALLENGE_COMPLETED);
        }
        if(GlobalPreferencesData.getInstance().getEpisodeChallengesCompleted(currentEpisode) ==
                GlobalGeneralData.getInstance().getEpisodeDataByNumber(currentEpisode).getTotalChallenges()) {
            addWaitingAchievement(GameAchievement.ALL_EPISODE_CHALLENGES_COMPLETED);
        }
    }

    @Override
    public void checkEpisodeRelatedAchievement(int currentEpisode) {
        if(currentEpisode == 1) {
            addWaitingAchievement(GameAchievement.FIRST_EPISODE_COMPLETED);
        } else if(currentEpisode == 2) {
            addWaitingAchievement(GameAchievement.SECOND_EPISODE_COMPLETED);
        } else if(currentEpisode == 3) {
            addWaitingAchievement(GameAchievement.THIRD_EPISODE_COMPLETED);
        } else if(currentEpisode == 4) {
            addWaitingAchievement(GameAchievement.FOURTH_EPISODE_COMPLETED);
        }
    }

    @Override
    public void checkStarRelatedAchievement(int currentEpisode) {
        if(GlobalPreferencesData.getInstance().getEpisodeStars(currentEpisode)
                == GlobalGeneralData.getInstance().getEpisodeDataByNumber(currentEpisode).getTotalStars()) {
            if(currentEpisode == 1) {
                addWaitingAchievement(GameAchievement.FIRST_EPISODE_ONE_HUNDRED_COMPLETE);
            } else if(currentEpisode == 2) {
                addWaitingAchievement(GameAchievement.SECOND_EPISODE_ONE_HUNDRED_COMPLETE);
            } else if(currentEpisode == 3) {
                addWaitingAchievement(GameAchievement.THIRD_EPISODE_ONE_HUNDRED_COMPLETE);
            } else if(currentEpisode == 4) {
                addWaitingAchievement(GameAchievement.FOURTH_EPISODE_ONE_HUNDRED_COMPLETE);
            }
        }
    }

    @Override
    public void checkUnblockLevelRelatedAchievement(GlobalAttributes.LEVEL_TYPE level_type) {
        if(level_type.equals(GlobalAttributes.LEVEL_TYPE.STARS)) {
            GlobalPreferencesData.getInstance().addUnblockedStarLevel(true);
            if(GlobalPreferencesData.getInstance().getUnblockedStarLevels() == 1) {
                addWaitingAchievement(GameAchievement.FIRST_STAR_LEVEL_UNBLOCKED);
            }
        }
    }

    @Override
    public void checkBoxKillsRelatedAchievement(int robotsSmashed, int ratliensSmashed) {
        if(robotsSmashed > 2) {
            addWaitingAchievement(GameAchievement.THREE_ROBOTS_KILLED_ONE_BOX);
        }
    }

    @Override
    public void pushPendingAchievementsFromPref(){
        Preferences preferences = GlobalPreferencesData.getInstance().getPreferences();
        Set<String> keyList = preferences.get().keySet();
        String achievement;
        //Gdx.app.log("AchievementManager", "Pushing pending achievements");
        for(String dinamicKey: keyList) {
            if(dinamicKey.startsWith("achievPush_")) {
                achievement = dinamicKey.substring(11);
                playServices.unlockAchievement(idProvider.getAchievementId(achievement));
                trackingServices.trackUnlockAchivement(idProvider.getTrackingId(achievement + "Achievement"));
                // Una vez subimos el logro al servicio de google, borramos el aviso
                GlobalPreferencesData.getInstance().erasePushAchievement(dinamicKey);
                //Gdx.app.log("AchievementManager", "PUSHING --> " + achievement);
            }
        }
    }

    @Override
    public void executeWaitingAchievements(){
        if(playServices.isSessionActive()) {
            ArrayList<GameAchievement> achievementsWaiting = GlobalGeneralData.getInstance().getAchievementsWaitingToShow();
            if(achievementsWaiting.size() > 0) {
                for (int i = 0; i < achievementsWaiting.size(); i++) {
                    playServices.unlockAchievement(idProvider.getAchievementId(achievementsWaiting.get(i).getValue()));
                    trackingServices.trackUnlockAchivement(idProvider.getTrackingId(achievementsWaiting.get(i).getValue() + "Achievement"));
                    // Despues de gestionar el logro borramos su aviso en las preferencias
                    GlobalPreferencesData.getInstance().erasePushAchievement("achievPush_" + achievementsWaiting.get(i).getValue());
                    //Gdx.app.log("AchievementManager","Pushing: " + achievementsWaiting.get(i).getValue());
                }
                achievementsWaiting.clear();
            }
        }
    }

    /**
     * Completa el logro y añade un aviso para mostrar el cartel cuando se pida
     */
    private void addWaitingAchievement(GameAchievement achievement){
        GlobalPreferencesData globalPreferencesData = GlobalPreferencesData.getInstance();
        //Gdx.app.log("AchievementManager","Adding achievement: " + achievement.getValue());
        if(!globalPreferencesData.isAchievementDone("achiev_" + achievement.getValue())) {
            // Añadimos el logro a la lista del global para hacerlo salar en otro momento
            GlobalGeneralData.getInstance().addAchievementWaitingToShow(achievement);
            // Aunque no lo mostremos aun, el logro está cumplido y lo guardamos como tal en las preferencias
            globalPreferencesData.markAchievement("achiev_" + achievement.getValue());
            // Si la lista del global no existiera, siempre estará el aviso de push en el preferences, así nunca se pierde
            globalPreferencesData.markPushAchievement("achievPush_" + achievement.getValue());
        }
    }

    private void manageSpecialLevelCompleted() {
        GlobalPreferencesData.getInstance().addSpecialChallengeLevelCompleted(true);
        int specialChallengeLevelCompleted = GlobalPreferencesData.getInstance().getSpecialChallengeLevelCompleted();
        if(specialChallengeLevelCompleted == 1) {
            addWaitingAchievement(GameAchievement.ONE_EXPERIMENTAL_LEVEL_COMPLETED);
        } else if(specialChallengeLevelCompleted == 5) {
            addWaitingAchievement(GameAchievement.FIVE_EXPERIMENTAL_LEVEL_COMPLETED);
        } else if(specialChallengeLevelCompleted == 10) {
            addWaitingAchievement(GameAchievement.TEN_EXPERIMENTAL_LEVEL_COMPLETED);
        }
    }
}
