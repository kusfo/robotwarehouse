package com.teenagemutantninjacoders.robotwarehouse.data.dtos;

import java.util.ArrayList;

/**
 * Created by JordiRM on 13/09/2018.
 */
public class DialogueDTO {
    private ArrayList<DialoguePageDTO> pages;
    private String side;
    private int bubbleType = 1;
    private int bubbleDespX = 0;
    private int bubbleDespY = 0;
    public DialogueDTO(){
        pages = new ArrayList<DialoguePageDTO>();
    }
    public ArrayList<DialoguePageDTO> getPages() {
        return pages;
    }
    public void setPages(ArrayList<DialoguePageDTO> page) {
        this.pages = page;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getBubbleDespX() {
        return bubbleDespX;
    }

    public void setBubbleDespX(int bubbleDespX) {
        this.bubbleDespX = bubbleDespX;
    }

    public int getBubbleDespY() {
        return bubbleDespY;
    }

    public void setBubbleDespY(int bubbleDespY) {
        this.bubbleDespY = bubbleDespY;
    }

    public int getBubbleType() {
        return bubbleType;
    }

    public void setBubbleType(int bubbleType) {
        this.bubbleType = bubbleType;
    }
}
