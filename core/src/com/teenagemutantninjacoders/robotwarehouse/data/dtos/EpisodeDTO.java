package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import java.util.ArrayList;

/**
 * Created by JordiRM on 12/07/2018.
 */
public class EpisodeDTO {
    private ArrayList<MapLevelDTO> levels;
    private ArrayList<DecorationDTO> decorations;
    private int episodeNumber;
    private int background = 1;
    private int unblockEpisodeWhenCompleting = 0;
    private String hiddenBySpecialEvent = "";
    private int totalLevels;
    private int totalStars;
    private int totalChallenges;
    private int goalLevel;
    private int columns;
    private int rows;

    public EpisodeDTO(){
        levels = new ArrayList<MapLevelDTO>();
        decorations = new ArrayList<DecorationDTO>();
    }

    public ArrayList<MapLevelDTO> getLevels() {
        return levels;
    }

    public void setLevels(ArrayList<MapLevelDTO> levels) {
        this.levels = levels;
    }

    public ArrayList<DecorationDTO> getDecorations() {
        return decorations;
    }

    public void setDecorations(ArrayList<DecorationDTO> decorations) {
        this.decorations = decorations;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getUnblockEpisodeWhenCompleting() {
        return unblockEpisodeWhenCompleting;
    }

    public void setUnblockEpisodeWhenCompleting(int unblockEpisodeWhenCompleting) {
        this.unblockEpisodeWhenCompleting = unblockEpisodeWhenCompleting;
    }

    public String getHiddenBySpecialEvent() {
        return hiddenBySpecialEvent;
    }

    public void setHiddenBySpecialEvent(String hiddenBySpecialEvent) {
        this.hiddenBySpecialEvent = hiddenBySpecialEvent;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
    }

    public int getTotalChallenges() {
        return totalChallenges;
    }

    public void setTotalChallenges(int totalChallenges) {
        this.totalChallenges = totalChallenges;
    }

    public int getGoalLevel() {
        return goalLevel;
    }

    public void setGoalLevel(int goalLevel) {
        this.goalLevel = goalLevel;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
