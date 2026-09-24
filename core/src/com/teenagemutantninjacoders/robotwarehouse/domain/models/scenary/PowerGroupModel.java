package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary;

import com.badlogic.gdx.math.MathUtils;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks.OverBaseBoxActivableModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JordiM on 04/08/2017.
 */

public class PowerGroupModel {
    private final int groupNumber;
    private List<OverBaseBoxActivableModel> groupMembers;
    private boolean consumed;
    private float touchTime;
    private GlobalAttributes.POWER power;

    public PowerGroupModel(int groupNumber, GlobalAttributes.POWER power) {
        this.power = power;
        this.groupNumber = groupNumber;
        groupMembers = new ArrayList<OverBaseBoxActivableModel>();
        consumed = false;
        touchTime = 0.0f;
    }

    public void addPowerOverBase(OverBaseBoxActivableModel powerOverBase) {
        groupMembers.add(powerOverBase);
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public GlobalAttributes.POWER getPower() {
        return power;
    }
    public boolean areAllPowersInGroupTouched() {
        for(OverBaseBoxActivableModel overBaseBoxActivableModel : groupMembers) {
            if(!overBaseBoxActivableModel.isTouched()) {
                return false;
            }
        }
        return true;
    }

    public boolean areAllPowersInGroupActivated() {
        for(OverBaseBoxActivableModel overBaseBoxActivableModel : groupMembers) {
            if(!overBaseBoxActivableModel.isActivated()) {
                return false;
            }
        }
        return true;
    }

    public void activateAllPowersInGroup() {
        for(OverBaseBoxActivableModel overBaseActivableModel : groupMembers) {
            overBaseActivableModel.activate();
        }
    }

    public void unTouchAllPowersInGroup() {
        for(OverBaseBoxActivableModel overBaseBoxActivableModel : groupMembers) {
            overBaseBoxActivableModel.untouch();
        }
    }

    public void consumeGroup() {
        consumed = true;
        groupMembers = new ArrayList<OverBaseBoxActivableModel>();
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void resetTimer() {
        this.touchTime = 0.0f;
    }
    public void tickTimer(float amountTime) {
        this.touchTime = MathUtils.clamp(touchTime + amountTime, 0, 9999);
        if(this.touchTime > 2.0f) {
            unTouchAllPowersInGroup();
            resetTimer();
        }
    }

}
