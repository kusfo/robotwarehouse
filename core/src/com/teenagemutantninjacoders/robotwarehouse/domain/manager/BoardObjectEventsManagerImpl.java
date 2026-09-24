package com.teenagemutantninjacoders.robotwarehouse.domain.manager;

import com.teenagemutantninjacoders.robotwarehouse.domain.helpers.BoardModelHelper;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces.BoxListener;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.EnemyModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;

/**
 * Created by JordiRM on 14/06/2016.
 */
public class BoardObjectEventsManagerImpl {
    private LevelModel levelModel;

    private BoxListener boxListener;

    public BoardObjectEventsManagerImpl(LevelModel levelModel, BoxListener boxListener) {
        this.levelModel = levelModel;
        this.boxListener = boxListener;
    }

    public void updateBoardObject(BoardObjectModel boardObject, float delta) {
        if(!boardObject.isTerminated()){
            if (boardObject instanceof BoxModel) {
                BoxModel boxModel = (BoxModel) boardObject;
                if (BoardModelHelper.getFallingGround(levelModel.getCurrentBoardModel(), boxModel.getColumn(), boxModel.getRow())) {
                    boxListener.setBoxBoardObjectEvent(boxModel, BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);
                }
            } else if (boardObject instanceof EnemyModel) {
                if (BoardModelHelper.getFallingGround(levelModel.getCurrentBoardModel(), boardObject.getColumn(), boardObject.getRow()))
                    boardObject.setEvent(BoardObjectModel.BOARD_OBJECT_EVENT.FALLING, true);
            }
        }
    }
}
