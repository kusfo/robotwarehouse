package com.teenagemutantninjacoders.robotwarehouse.data.dtos.blocks;

public class RatlienLairDTO {
    private boolean exitUp = false;
    private boolean exitDown = false;
    private boolean exitLeft = false;
    private boolean exitRight = false;
    private int priority;
    private boolean visible = true;

    public boolean isExitUp() {
        return exitUp;
    }

    public void setExitUp(boolean exitUp) {
        this.exitUp = exitUp;
    }

    public boolean isExitDown() {
        return exitDown;
    }

    public void setExitDown(boolean exitDown) {
        this.exitDown = exitDown;
    }

    public boolean isExitLeft() {
        return exitLeft;
    }

    public void setExitLeft(boolean exitLeft) {
        this.exitLeft = exitLeft;
    }

    public boolean isExitRight() {
        return exitRight;
    }

    public void setExitRight(boolean exitRight) {
        this.exitRight = exitRight;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
