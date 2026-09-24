package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;

/**
 * Created by JordiRM on 14/03/2017.
 */
public interface iDestroyableBoardObject {
    void setDestruction(DestructionDTO destructionDTO);
    void executeDestruction();
}
