package com.teenagemutantninjacoders.robotwarehouse.data.mappers;

/**
 * Created by jordi.montornes on 22/02/2016.
 */
public interface BaseDTOMapper<d, M> {
    M transform (d dto);
}
