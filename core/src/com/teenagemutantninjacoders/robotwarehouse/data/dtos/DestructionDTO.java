package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

/**
 * Created by JordiRM on 14/03/2017.
 */
public class DestructionDTO {
    private DESTRUCTION_TYPE destructionType;

    public DestructionDTO(DESTRUCTION_TYPE destructionType){
        this.destructionType = destructionType;
    }
    public DESTRUCTION_TYPE getDestructionType() {
        return destructionType;
    }

    public enum DESTRUCTION_TYPE{
        SMASHED, EXPLOSION, CHEWED
    }
}