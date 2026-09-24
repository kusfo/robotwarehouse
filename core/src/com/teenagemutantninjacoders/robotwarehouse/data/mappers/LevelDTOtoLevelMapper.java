package com.teenagemutantninjacoders.robotwarehouse.data.mappers;

import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.BigDecoDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.BoxDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.AbyssDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorBarrierDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorButtonDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorRotatingDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseIndicatorLightDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.enemies.CannonDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseDecoDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseTrackDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.RobotElevatorDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorTileDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.FloorTrapDoorDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.scenary.LevelDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.OverBaseBoxActivableDTO;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks.WallDTO;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorBarrierModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.FloorTrapDoorModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.enemies.CannonModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.LevelModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseModel;

import java.util.ArrayList;

/**
 * Created by jordi.montornes on 31/01/2016.
 */
public class LevelDTOtoLevelMapper implements BaseDTOMapper<LevelDTO,LevelModel>{

    @Override
    public LevelModel transform(LevelDTO dto) {
        LevelModel levelModel = new LevelModel();

        ArrayList<WallDTO> wallDTOArrayList = dto.getWalls();
        for(WallDTO wallDTO : wallDTOArrayList){
            levelModel.addNewWall(wallDTO.getCellH(),wallDTO.getCellV(), wallDTO.getResourceName(), wallDTO.getVariation());
        }

        ArrayList<FloorTileDTO> floorTileDTOArrayList = dto.getFloorTiles();
        for(FloorTileDTO floorTileDTO: floorTileDTOArrayList){
            levelModel.addNewFloorTile(floorTileDTO.getCellH(), floorTileDTO.getCellV(), floorTileDTO.getResourceName(), floorTileDTO.getVariation());
        }

        ArrayList<FloorTrapDoorDTO> floorTrapDoorDTOArrayList = dto.getFloorTrapDoors();
        for(FloorTrapDoorDTO floorTrapDoorDTO: floorTrapDoorDTOArrayList){
            levelModel.addNewFloorTrapDoor(floorTrapDoorDTO.getCellH(), floorTrapDoorDTO.getCellV(),floorTrapDoorDTO.getResourceName(), floorTrapDoorDTO.getVariation(),
                    FloorTrapDoorModel.FLOOR_STATUS.fromString(floorTrapDoorDTO.getFloorStatus()),floorTrapDoorDTO.getTimeOpen(),floorTrapDoorDTO.getTimeClosed(),
                    floorTrapDoorDTO.getInitialEventTime());
        }

        ArrayList<FloorButtonDTO> floorButtonDTOArrayList = dto.getFloorButtons();
        for(FloorButtonDTO floorButtonDTO : floorButtonDTOArrayList) {
            levelModel.addNewFloorButton(floorButtonDTO.getCellH(), floorButtonDTO.getCellV(),floorButtonDTO.getResourceName(), floorButtonDTO.getVariation(),
                    GlobalAttributes.COLOR.fromString(floorButtonDTO.getColor()), floorButtonDTO.isColorExclusive());
        }

        ArrayList<FloorBarrierDTO> floorBarrierDTOArrayList = dto.getFloorBarriers();
        for(FloorBarrierDTO floorBarrierDTO : floorBarrierDTOArrayList) {
            levelModel.addNewFloorBarrier(floorBarrierDTO.getCellH(), floorBarrierDTO.getCellV(),floorBarrierDTO.getResourceName(), floorBarrierDTO.getVariation(),
                    GlobalAttributes.COLOR.fromString(floorBarrierDTO.getColor()),
                    FloorBarrierModel.BARRIER_TYPE.fromString(floorBarrierDTO.getBarrierType()),
                    FloorBarrierModel.BARRIER_STATUS.fromString(floorBarrierDTO.getBarrierStatus()),
                    FloorBarrierModel.BARRIER_ORIENTATION.fromString(floorBarrierDTO.getBarrierOrientation()),
                    floorBarrierDTO.isAutomatic(), floorBarrierDTO.getTimeBlocked(), floorBarrierDTO.getTimeUnblocked(), floorBarrierDTO.getInitialEventTime());
        }

        ArrayList<FloorRotatingDTO> floorRotatingDTOArrayList = dto.getRotatingFloors();
        for(FloorRotatingDTO floorRotatingDTO: floorRotatingDTOArrayList){
            ArrayList<MovableBoardObjectModel.DIRECTION> directionsList = new ArrayList<MovableBoardObjectModel.DIRECTION>();
            for(int i = 0; i < floorRotatingDTO.getDirectionsList().size(); i++)
                directionsList.add(MovableBoardObjectModel.DIRECTION.fromString(floorRotatingDTO.getDirectionsList().get(i)));

            levelModel.addNewFloorRotating(floorRotatingDTO.getCellH(), floorRotatingDTO.getCellV(),floorRotatingDTO.getResourceName(), floorRotatingDTO.getVariation(),
                    directionsList, floorRotatingDTO.getRotationTime());
        }

        ArrayList<BoxDTO> boxDTOs = dto.getBoxes();
        for(BoxDTO boxDTO : boxDTOs){
            levelModel.addNewBox(boxDTO.getCellH(), boxDTO.getCellV(), boxDTO.getResourceName(), GlobalAttributes.COLOR.fromString(boxDTO.getColor()));
        }

        ArrayList<AbyssDTO> abyssDTOs = dto.getAbyss();
        for(AbyssDTO abyssDTO : abyssDTOs){
            levelModel.addNewAbyss(abyssDTO.getCellH(), abyssDTO.getCellV(), abyssDTO.getResourceName(), abyssDTO.getVariation(), abyssDTO.getRatlienLair());
        }

        ArrayList<OverBaseBoxActivableDTO> overBaseActivableDTOs = dto.getOverBaseBoxActivables();
        for (OverBaseBoxActivableDTO overBaseDTO : overBaseActivableDTOs) {
            levelModel.addNewOverBaseBoxActivable(overBaseDTO.getCellH(), overBaseDTO.getCellV(), overBaseDTO.getResourceName(), overBaseDTO.getVariation(), OverBaseModel.OVERBASE_TYPE.fromString(overBaseDTO.getOverBaseType()), overBaseDTO.getOverBaseGroupNumber(), GlobalAttributes.POWER.fromString(overBaseDTO.getPower()));
        }

        ArrayList<OverBaseDecoDTO> overBaseDecoDTOs = dto.getOverBaseDecos();
        for (OverBaseDecoDTO overBaseDecoDTO : overBaseDecoDTOs) {
            levelModel.addNewOverBaseDeco(overBaseDecoDTO.getCellH(), overBaseDecoDTO.getCellV(), overBaseDecoDTO.getResourceName(), overBaseDecoDTO.getVariation());
        }

        ArrayList<OverBaseTrackDTO> overBaseTrackDTOs = dto.getOverBaseTracks();
        for (OverBaseTrackDTO overBaseTrackDTO : overBaseTrackDTOs) {
            levelModel.addNewOverBaseTrack(overBaseTrackDTO.getCellH(), overBaseTrackDTO.getCellV(), overBaseTrackDTO.getResourceName(), overBaseTrackDTO.getVariation(), overBaseTrackDTO.isCannonStop());
        }

        ArrayList<OverBaseIndicatorLightDTO> overBaseIndicatorLightDTOs = dto.getOverBaseIndicatorLights();
        for (OverBaseIndicatorLightDTO overBaseIndicatorLightDTO : overBaseIndicatorLightDTOs) {
            levelModel.addNewOverBaseIndicatorLight(overBaseIndicatorLightDTO.getCellH(), overBaseIndicatorLightDTO.getCellV());
        }

        ArrayList<BigDecoDTO> bigDecoDTOS = dto.getBigDecos();
        for (BigDecoDTO bigDecoDTO : bigDecoDTOS) {
            levelModel.addNewBigDeco(bigDecoDTO.getCellH(), bigDecoDTO.getCellV(), bigDecoDTO.getResourceName(), bigDecoDTO.getVariation());
        }

        ArrayList<RobotElevatorDTO> robotElevators = dto.getRobotElevators();
        for(RobotElevatorDTO robotElevatorDTO : robotElevators) {
            levelModel.addNewRobotElevator(robotElevatorDTO.getCellH(), robotElevatorDTO.getCellV(),robotElevatorDTO.getResourceName(), robotElevatorDTO.getVariation(), robotElevatorDTO.getPriority());
        }

        ArrayList<CannonDTO> cannons = dto.getCannons();
        for(CannonDTO cannon : cannons) {
            levelModel.addNewCannon(cannon.getCellH(), cannon.getCellV(), cannon.getResourceName(), cannon.getVariation(),
                    MovableBoardObjectModel.DIRECTION.fromString(cannon.getDirection()), CannonModel.CANNON_SHOOT_DIRECTION.fromString(cannon.getShootDirection()),
                    cannon.getMaxAutoAdvances(), cannon.getBaseVelocity(), cannon.getPauseAfterShooting(), cannon.getInitialWaitingTime(),
                    cannon.hasProgramedLoop(), cannon.getProgramedStops());
        }

        levelModel.setLevelTime(dto.getLevelTime());
        levelModel.setRequestBoxes(dto.getRequestedBoxes());
        levelModel.setOptimalTime(dto.getOptimalTime());
        levelModel.setTimeMultiplier(dto.getTimeMultiplier());
        levelModel.setRankPointsMod(dto.getRankPointsMod());
        levelModel.setMaxComboRed(dto.getMaxComboRed());
        levelModel.setMaxComboBlue(dto.getMaxComboBlue());
        levelModel.setMaxComboYellow(dto.getMaxComboYellow());
        levelModel.setMaxNumRobots(dto.getMaxNumRobots());
        levelModel.setRobotSpanTime(dto.getRobotSpanTime());
        levelModel.setRatlienSpanTime(dto.getRatlienSpanTime());
        levelModel.setMaxNumRatliens(dto.getMaxNumRatliens());
        levelModel.setPusherRobot(dto.getPusherRobot());
        levelModel.setExplosiveRobot(dto.getExplosiveRobot());
        levelModel.setChallenge(dto.getChallenge());
        levelModel.setChallengeValue(dto.getChallengeValue());
        levelModel.setChallengeObject(dto.getChallengeObject());
        levelModel.setIntroductionEventId(dto.getIntroductionEventId());
        levelModel.setInitialBoxes();
        return levelModel;
    }
}
