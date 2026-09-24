package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

/**
 * Created by jordi.montornes on 28/02/2018.
 */

public class RatlienLairModel extends AbyssModel {
    private boolean active;
    private int priority;
    private int randomValue;
    private boolean exitUp, exitDown, exitLeft, exitRight;
    private boolean disabled;
    private ParticleEffectModel disableParticleEffect = null;

    public RatlienLairModel(int h, int v, String resourceName, int variation, int priority, boolean exitUp, boolean exitDown, boolean exitLeft, boolean exitRight, boolean visible) {
        super(h, v, resourceName, variation);
        active = false;
        this.priority = priority;
        disabled = false;
        setVisible(visible);
        this.exitUp = exitUp;
        this.exitDown = exitDown;
        this.exitLeft = exitLeft;
        this.exitRight = exitRight;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int getPriority() {
        return priority;
    }

    public void setRandomValue(int randomValue) {
        this.randomValue = randomValue;
    }

    public int getRandomValue() {
        return randomValue;
    }

    public boolean hasExit(EXIT_DIRECTION exitDirection){
        switch(exitDirection){
            case UP: return exitUp;
            case DOWN: return exitDown;
            case LEFT: return exitLeft;
            case RIGHT: return exitRight;
        }
        return false;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setDisableParticleEffect(ParticleEffectModel particleEffectModel){
        disableParticleEffect = particleEffectModel;
    }

    public ParticleEffectModel getDisableParticleEffect(){
        return disableParticleEffect;
    }

    public enum EXIT_DIRECTION {
        UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");
        private String value;

        EXIT_DIRECTION(String newValue) {
            setValue(newValue);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String newValue) {
            value = newValue;
        }

        public static EXIT_DIRECTION fromString(String text) {
            if (text != null) {
                for (EXIT_DIRECTION var : EXIT_DIRECTION.values()) {
                    if (text.equals(var.getValue())) {
                        return var;
                    }
                }
            }
            return UP;
        }
    }
}
