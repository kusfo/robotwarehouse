package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.tutorial;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.CellDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobject;
import com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud.GUIobjectCharacterDialogue;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.SpriteEffectRobotBubble;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

/**
 * Created by JordiRM on 10/09/2020.
 */
public class ScenarioEvent_1 extends GUIobject {
    private int actualEvent = 0;
    private LevelModel levelModel;
    private GUIobjectCharacterDialogue characterDialogue;
    private BoardModel boardModel;
    private CannonModel cannonModel;
    private int advancesLeft_saved;
    private float pauseAfterShooting_saved;

    public ScenarioEvent_1(LevelModel levelModel, GUIobjectCharacterDialogue characterDialogue){
        this.levelModel = levelModel;
        this.characterDialogue = characterDialogue;
        boardModel = levelModel.getCurrentBoardModel();
        configureEvent();
    }

    @Override
    public void update(float delta){
        if(actualEvent == 0){
            if(characterDialogue.getActualPage() == 2){
                actualEvent = 1;
                executeEvent_1();
            }
        }
        else if(actualEvent == 1){
            if(characterDialogue.getActualPage() > 2){
                actualEvent = 2;
                executeEvent_2();
            }
        }
    }

    private void configureEvent(){
        levelModel.addNewBox(9, 7, "th01_boxes01", GlobalAttributes.COLOR.NONE);
        cannonModel = ((CannonModel) boardModel.getBoardObjectAbove(9, 10));

        // Guardamos los datos originales
        advancesLeft_saved = cannonModel.getAdvancesLeft();
        pauseAfterShooting_saved = cannonModel.getPauseAfterShooting();

        // Configuramos el cañon para la escena
        cannonModel.setAdvancesLeft(0);
        cannonModel.setShootingCell(new CellDTO(cannonModel.getColumn(), cannonModel.getRow()));
        cannonModel.setPauseAfterShooting(999);

        // Hacemos que el painter dibuje los efectos aunque el juego no esté en marcha
        GlobalLevelData.getInstance().setPaintFxAlways(true);
    }

    private void executeEvent_1(){
        GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.NONE);
        cannonModel.setActualizeWhileIsNotPlaying(true);
    }

    private void executeEvent_2(){
        // Burbuja con expresión malvada para que se entienda que lo ha hecho intencionadamente
        EffectManager.getInstance().createSpriteEffectRobotBubble(cannonModel.getPositionXCenter() + 6,
                cannonModel.getPositionYCenter() + 24, GameConstants.ABOVE_ALL_DEPTH, 4, SpriteEffectRobotBubble.ROBOT_BUBBLE_TYPE.EVIL);

        // Restablecemos el estado de la partida
        GlobalLevelData.getInstance().setLevelEvent(GlobalLevelData.LEVEL_EVENT.ENTERING);
        GlobalLevelData.getInstance().setLostBoxes(0);
        GlobalLevelData.getInstance().setPaintFxAlways(false);

        // Volvemos a configurar el cañon con los parametro originales
        cannonModel.setActualizeWhileIsNotPlaying(false);
        cannonModel.setAdvancesLeft(advancesLeft_saved);
        cannonModel.setShootingCell(new CellDTO(-1, -1));
        cannonModel.setPauseAfterShooting(pauseAfterShooting_saved);
        cannonModel.setCannonStatus(CannonModel.CANNON_STATUS.NONE);
    }
}