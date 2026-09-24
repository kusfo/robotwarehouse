package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

/**
 * Created by JordiRM on 11/10/2016.
 */
public class GUIobject {
    private boolean finalized = false;

    public void update(float delta){}
    public void setFinalized(boolean finalized){
        this.finalized = finalized;
    }
    public boolean getFinalized(){
        return finalized;
    }
}
