package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseIndicatorLightModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.PowerGroupModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.OverBaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordiRM on 04/07/2016.
 */
public class OverBaseManagerImpl implements OverBaseManager {
    private LevelModel currentLevelModel;
    private GameEventsManagerImpl gameEventsManager;
    private float boxActivableGlow = 0;
    private float boxActivableDest = 1f;
    private float indicatorLightAlpha = 0;
    private float indicatorLightDest = 1f;
    private float indicatorsDelay = 0.6f;

    private ArrayList<OverBaseIndicatorLightModel> indicatorLights;
    private boolean indicatorLightsActivated = false;

    public OverBaseManagerImpl(LevelModel levelModel, GameEventsManagerImpl gameEventsManager) {
        this.currentLevelModel = levelModel;
        this.gameEventsManager = gameEventsManager;
    }

    @Override
    public void update(float delta) {
        if (GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING) {
            boxActivableGlow = MathUtils.lerp(boxActivableGlow, boxActivableDest, delta * 0.7f);
            if (boxActivableGlow > 0.7f)
                boxActivableDest = 0;
            else if (boxActivableGlow < 0.05f)
                boxActivableDest = 1;
        }

        if(!indicatorLightsActivated){
            if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.COMPLETED){
                if(indicatorsDelay == 0) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.LIGHTS_LEVEL_COMPLETED);
                    activateIndicatorLights(OverBaseIndicatorLightModel.LIGHT_COLOR.GREEN);
                    indicatorLightsActivated = true;
                } else
                    indicatorsDelay = MathUtils.clamp(indicatorsDelay - (1 * delta), 0, 1);
            }else if(GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.TIME_OUT ||
                    GlobalLevelData.getInstance().getLevelEvent() == GlobalLevelData.LEVEL_EVENT.WITHOUT_BOXES){
                if(indicatorsDelay == 0) {
                    AudioManager.getInstance().playSound(AudioManager.SOUND.LIGHTS_GAME_OVER);
                    activateIndicatorLights(OverBaseIndicatorLightModel.LIGHT_COLOR.RED);
                    indicatorLightsActivated = true;
                } else
                    indicatorsDelay = MathUtils.clamp(indicatorsDelay - (1 * delta), 0, 1);
            }
        } else {
            updateIndicatorLights(delta);
        }
    }

    @Override
    public void updateOverBase(OverBaseBoxActivableModel overBaseBoxActivableModel, float delta) {

        if (overBaseBoxActivableModel.isActivated()) {

            if (overBaseBoxActivableModel.getScaleX() > 1.2) {
                overBaseBoxActivableModel.setAlpha(MathUtils.clamp(overBaseBoxActivableModel.getAlpha() - (2.4f * delta), 0, 1));
            }

            overBaseBoxActivableModel.setScaleY(overBaseBoxActivableModel.getScaleY() + (1.0f * delta));
            overBaseBoxActivableModel.setScaleX(overBaseBoxActivableModel.getScaleX() + (1.0f * delta));
            overBaseBoxActivableModel.setPositionY(overBaseBoxActivableModel.getPositionY() + (40 * delta));

            if (overBaseBoxActivableModel.getAlpha() == 0) {
                if (overBaseBoxActivableModel.getStatus() != BoardObjectModel.BOARD_OBJECT_STATUS.ERASED) {
                    overBaseBoxActivableModel.setStatus(BoardObjectModel.BOARD_OBJECT_STATUS.ERASED);
                    if (overBaseBoxActivableModel.getOverBaseType().equals(OverBaseModel.OVERBASE_TYPE.POINTS)) {
                        gameEventsManager.addNewOverBasePointsEvent(overBaseBoxActivableModel.getPositionXCenter(), overBaseBoxActivableModel.getPositionYCenter(), false);
                    } else if (overBaseBoxActivableModel.getOverBaseType().equals(OverBaseModel.OVERBASE_TYPE.POWERS)) {
                        Gdx.app.log("POWERS", "Power piece enabled");
                        checkPowersInGroupActivated(overBaseBoxActivableModel);
                    }
                }
            }
        } else if (overBaseBoxActivableModel.isTouched()) {
            if (overBaseBoxActivableModel.getOverBaseType().equals(OverBaseModel.OVERBASE_TYPE.POWERS)) {
                PowerGroupModel powerGroupModel = getPowerGroupModel(overBaseBoxActivableModel);
                checkPowersInGroupTouched(powerGroupModel, delta);
            }
        } else {
            overBaseBoxActivableModel.setGlowAlpha(boxActivableGlow);
        }
    }

    private void checkPowersInGroupActivated(OverBaseBoxActivableModel OverBaseBoxActivableModel) {
        PowerGroupModel powerGroupModel = getPowerGroupModel(OverBaseBoxActivableModel);
        if (powerGroupModel != null &&
                powerGroupModel.areAllPowersInGroupActivated() && !powerGroupModel.isConsumed()) {
            Gdx.app.log("Power OverBase", "All powers enabled!");
            powerGroupModel.consumeGroup();
            gameEventsManager.addNewOverBasePowerEvent(OverBaseBoxActivableModel.getPositionXCenter(), OverBaseBoxActivableModel.getPositionYCenter(), powerGroupModel.getPower());
        }
    }

    private void checkPowersInGroupTouched(PowerGroupModel powerGroupModel, float delta) {
        if (powerGroupModel != null && powerGroupModel.areAllPowersInGroupTouched() && !powerGroupModel.isConsumed()) {
            Gdx.app.log("Power OverBase", "All powers touched!");
            AudioManager.getInstance().playSound(AudioManager.SOUND.OVERBASE_TRIGGERED);
            powerGroupModel.activateAllPowersInGroup();
            powerGroupModel.unTouchAllPowersInGroup();
        } else {
            powerGroupModel.tickTimer(delta);
        }
    }

    private PowerGroupModel getPowerGroupModel(OverBaseBoxActivableModel OverBaseBoxActivableModel) {
        List<PowerGroupModel> powerGroupModels = currentLevelModel.getcurrentPowerGroupModels();
        for (PowerGroupModel powerGroupModel : powerGroupModels) {
            if (OverBaseBoxActivableModel.getGroupNumber() == powerGroupModel.getGroupNumber()) {
                return powerGroupModel;
            }
        }
        return null;
    }

    private void activateIndicatorLights(OverBaseIndicatorLightModel.LIGHT_COLOR color){
        indicatorLights = new ArrayList<OverBaseIndicatorLightModel>();
        ArrayList<GameObject> gameObjects = currentLevelModel.getCurrentGameObjects();
        for(int i = 0; i < gameObjects.size(); i++){
            if(gameObjects.get(i) instanceof OverBaseIndicatorLightModel){
                ((OverBaseIndicatorLightModel) gameObjects.get(i)).activateLight(color);
                indicatorLights.add((OverBaseIndicatorLightModel)gameObjects.get(i));
            }
        }
    }

    private void updateIndicatorLights(float delta) {
        if (indicatorLights.size() > 0) {
            if (indicatorLights.get(0).getLightColor() == OverBaseIndicatorLightModel.LIGHT_COLOR.GREEN) {
                indicatorLightAlpha = MathUtils.clamp(indicatorLightAlpha + (delta * 6f), 0, 1);
                for (int i = 0; i < indicatorLights.size(); i++) {
                    indicatorLights.get(i).setGlowindGlassAlpha(indicatorLightAlpha);
                    indicatorLights.get(i).getLightShine().getGlowSprite().setAlpha(indicatorLightAlpha);
                    indicatorLights.get(i).getLightShine().getGlowSprite().setAlpha(indicatorLightAlpha / 1.8f);
                }
            }
            else if (indicatorLights.get(0).getLightColor() == OverBaseIndicatorLightModel.LIGHT_COLOR.RED) {
                if (indicatorLightDest == 1) {
                    indicatorLightAlpha += delta * 3f;
                } else {
                    indicatorLightAlpha -= delta * 3f;
                }
                if (indicatorLightAlpha >= 1.2f) {
                    indicatorLightDest = 0;
                } else if (indicatorLightAlpha < 0.05f) {
                    indicatorLightDest = 1;
                }

                Sprite lightSprite, glowSprite;
                float clampedAlpha = MathUtils.clamp(indicatorLightAlpha, 0, 1);
                for (int i = 0; i < indicatorLights.size(); i++) {
                    lightSprite = indicatorLights.get(i).getLightShine().getLightRaysSprite();
                    glowSprite = indicatorLights.get(i).getLightShine().getGlowSprite();

                    if (lightSprite != null) {
                        lightSprite.setAlpha(clampedAlpha / 1.9f);
                        lightSprite.setRotation(lightSprite.getRotation() + (70 * delta));
                    }
                    glowSprite.setAlpha(clampedAlpha);
                    indicatorLights.get(i).setGlowindGlassAlpha(clampedAlpha);
                }
            }
        }
    }
}