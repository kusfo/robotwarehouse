package com.teenagemutantninjacoders.robotwarehouse.domain.manager.interfaces;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.BoxModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;

/**
 * Created by JordiRM on 25/11/2017.
 */
public interface BoxListener {
    void setBoxBoardObjectEvent(BoxModel boxModel, BoardObjectModel.BOARD_OBJECT_EVENT event, boolean isTerminated);
    void updateBoxGroup(BoxModel boxmodel);
    void updateNeighboursBoxesGroup(int h, int v, GlobalAttributes.COLOR color);
}
