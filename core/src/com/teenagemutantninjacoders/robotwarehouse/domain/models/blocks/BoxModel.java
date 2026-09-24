package com.teenagemutantninjacoders.robotwarehouse.domain.models.blocks;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalAttributes;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.data.dtos.DestructionDTO;
import com.teenagemutantninjacoders.robotwarehouse.display.AnimatedSprite;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.BoxDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.AudioManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.ChallengeManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.manager.EffectManager;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.BoardObjectModel;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.MovableBoardObjectModel;

/**
 * Created by jordi.montornes on 30/01/2016.
 */
public class BoxModel extends MovableBoardObjectModel implements iColoredObject, iDestroyableBoardObject{
    private static final int LIFE_INITIAL_VALUE = 1000;
    private static final float CHEW_TIME_INITIAL_VALUE = 1.0f;

    private GlobalAttributes.COLOR color = GlobalAttributes.COLOR.NONE;
    private int sameAdjacentBoxesAmount = 0;
    private boolean inGroup = false;
    private BoxModel boxUp, boxDown, boxRight, boxLeft;
    private DestructionDTO destructionDTO;
    private float flashAlpha = 0.f;
    private Sprite flashSprite;
    private Sprite glowSprite;
    private Sprite shinyIcon;
    private AnimatedSprite boxSparkAnimation;
    private AnimatedSprite ratlienAttachedUpAnimation;
    private AnimatedSprite ratlienAttachedDownAnimation;
    private AnimatedSprite ratlienAttachedLeftAnimation;
    private AnimatedSprite ratlienAttachedRightAnimation;

    private boolean ratlienAttachedUp;
    private boolean ratlienAttachedDown;
    private boolean ratlienAttachedLeft;
    private boolean ratlienAttachedRight;
    private float chewTime = CHEW_TIME_INITIAL_VALUE;
    private int lifeMeter;
    private int robotsSmashedOnMovement = 0;
    private int ratliensSmashedOnMovement = 0;

    public BoxModel(int h, int v,  String resourceName, GlobalAttributes.COLOR color) {
        setColumn(h);
        setRow(v);
        setResourceName(resourceName);
        setPosition(new Vector2(getCellPositionX(getColumn()), getCellPositionY(getRow())));
        setHitbox();
        setHitboxImpact();
        setDepth(GameConstants.ABOVE_DEPTH);
        setObstacleLevel(GameConstants.BOX_OBSTACLE_LEVEL);
        setMoveVelocity(500);
        setBoardObjectLayer(BOARD_OBJECT_LAYER.ABOVE);
        setCanAvoidFalls(false);
        setCanRunOver(true);
        setTouchable(true);
        setStopOnLevelEvent(false);
        setActiveObject(true);
        initializeMovableObject(); // Una vez hemos inicializado el objeto, hacemos lo mismo con su boardObject

        setAtlas(Assets.getTextureAtlas(resourceName));

        switch (color){
            case RED: setSprite(new Sprite(getAtlas().findRegion("redBox")));
                shinyIcon = new Sprite(getAtlas().findRegion("redBoxShine"));
                break;
            case BLUE: setSprite(new Sprite(getAtlas().findRegion("blueBox")));
                shinyIcon = new Sprite(getAtlas().findRegion("blueBoxShine"));
                break;
            case YELLOW: setSprite(new Sprite(getAtlas().findRegion("yellowBox")));
                shinyIcon = new Sprite(getAtlas().findRegion("yellowBoxShine"));
                break;
            default: setSprite(new Sprite(getAtlas().findRegion("greyBox")));break;
        }
        setColor(color);

        flashSprite = new Sprite(getAtlas().findRegion("boxFlash"));
        glowSprite = new Sprite(getSprite());
        glowSprite.setAlpha(0);
        boxSparkAnimation = new AnimatedSprite(getAtlas(), "boxSpark", 0.1f, Animation.PlayMode.NORMAL);
        boxSparkAnimation.getSprite().setAlpha(0.9f);
        boxSparkAnimation.setLastFrame();
        ratlienAttachedUp = false;
        ratlienAttachedDown = false;
        ratlienAttachedLeft = false;
        ratlienAttachedRight = false;
        lifeMeter = LIFE_INITIAL_VALUE;
        setGameObjectDraw(new BoxDraw());
    }

    public boolean isFlashing() {
        return flashAlpha > 0.6f;
    }

    public void Flash() {
         flashAlpha = 0.9f;
         boxSparkAnimation.play();
    }
    public void softFlash() {
        flashAlpha = 0.6f;
    }

    public void setGlowAlpha(float glowAlpha) {
        glowSprite.setAlpha(glowAlpha);
    }

    public float getFlashAlpha() {
        return flashAlpha;
    }

    public void setFlashAlpha(float flashAlpha) {
        this.flashAlpha = flashAlpha;
        flashSprite.setAlpha(flashAlpha);
    }

    public AnimatedSprite getBoxSparkAnimation() {
        return boxSparkAnimation;
    }

    public Sprite getGlowSprite() {
        return glowSprite;
    }

    public Sprite getFlashSprite() {
        return flashSprite;
    }
    public Sprite getShinyIcon() {
        return shinyIcon;
    }
    public boolean isReadyForTeleport(){
        if(getEvent() == BoardObjectModel.BOARD_OBJECT_EVENT.IDLE && howManyRatliensAttached() == 0 ) return true;
        return false;
    }
    public int getSameAdjacentBoxesAmount(){
        return sameAdjacentBoxesAmount;
    }
    public void setSameAdjacentBoxesAmount(int amount){
        sameAdjacentBoxesAmount = amount;
    }

    public boolean getInGroup(){
        return inGroup;
    }
    public void setInGroup(boolean inGroup){
        this.inGroup = inGroup;
    }

    public BoxModel getBoxUp(){
        return boxUp;
    }
    public BoxModel getBoxDown(){
        return boxDown;
    }
    public BoxModel getBoxRight(){
        return boxRight;
    }
    public BoxModel getBoxLeft(){
        return boxLeft;
    }
    public void setBoxUp(BoxModel newBoxModel){
        boxUp = newBoxModel;
    }
    public void setBoxDown(BoxModel newBoxModel){
        boxDown = newBoxModel;
    }
    public void setBoxRight(BoxModel newBoxModel){
        boxRight = newBoxModel;
    }
    public void setBoxLeft(BoxModel newBoxModel){
        boxLeft = newBoxModel;
    }

    public void disableBox(){
        setColor(GlobalAttributes.COLOR.NONE);
        sameAdjacentBoxesAmount = 0;
        inGroup = false;
    }
    public void setDisabledSprite(){
        setSprite(new Sprite(getAtlas().findRegion("greyBox")));
    }

    @Override
    public GlobalAttributes.COLOR getColor(){
        return color;
    }
    @Override
    public void setColor(GlobalAttributes.COLOR newColor){
        color = newColor;
    }

    // For Testing
    public String getColorName(){
        switch(color)
        {
            case RED: return "roja";
            case YELLOW: return "amarilla";
            case BLUE: return "azul";
            default: return "gris";
        }
    }

    @Override
    public boolean isTargetable() {
        return getMoveStatus().equals(MOVE_STATUS.IDLE);
    }

    @Override
    public boolean isDestroyableByEnemy() {
        return true;
    }

    @Override
    public void setDestruction(DestructionDTO destructionDTO) {
        this.destructionDTO = destructionDTO;
    }

    @Override
    public void executeDestruction() {
        ParticleEffectModel.EFFECT_TYPE effectType;
        switch( getColor()){
            case RED: effectType = ParticleEffectModel.EFFECT_TYPE.BOX_DESTROY_RED;
                break;
            case BLUE: effectType = ParticleEffectModel.EFFECT_TYPE.BOX_DESTROY_BLUE;
                break;
            case YELLOW: effectType = ParticleEffectModel.EFFECT_TYPE.BOX_DESTROY_YELLOW;
                break;

            default: effectType = ParticleEffectModel.EFFECT_TYPE.BOX_DESTROY_GREY; break;
        }
        EffectManager.getInstance().createEffect(effectType, getPositionX() + 16, getPositionY() + 26, GameConstants.ABOVE_ALL_DEPTH);
        AudioManager.getInstance().playSound(AudioManager.SOUND.BOX_DESTRUCTION);
        GlobalLevelData.getInstance().decreasingRatliensChewing(howManyRatliensAttached());
    }

    public boolean isBoxDead() {
        if(this.lifeMeter <= 0) {
            return true;
        }
        return false;
    }

    public boolean isTimeToBeChewed(float delta) {
        chewTime = MathUtils.clamp(chewTime - (1 * delta), 0, 999);
        if(chewTime <= 0.0f) {
            return true;
        }
        return false;
    }

    public void makeRatlienChew() {
        int damage = 100 - ((howManyRatliensAttached() - 1) * 20);
        if(ratlienAttachedUp) {
            this.lifeMeter-= damage;
        }
        if(ratlienAttachedDown) {
            this.lifeMeter-= damage;
        }
        if(ratlienAttachedLeft) {
            this.lifeMeter-= damage;
        }
        if(ratlienAttachedRight) {
            this.lifeMeter-= damage;
        }

        chewTime = CHEW_TIME_INITIAL_VALUE;
    }

    public void attachRatlien(DIRECTION targetDirection) {
        //target direction is the direction of the ratlien, so we manage here exactly the opposite:
        switch (targetDirection) {
            case UP:
                ratlienAttachedDown = true;
                ratlienAttachedDownAnimation = new AnimatedSprite(Assets.getTextureAtlas("e_ratlien"), "chewing_front", 0.15f, Animation.PlayMode.NORMAL);
                ratlienAttachedDownAnimation.addSequence("chewing_front_loop", 0.1f, Animation.PlayMode.LOOP);
                break;
            case DOWN:
                ratlienAttachedUp = true;
                ratlienAttachedUpAnimation = new AnimatedSprite(Assets.getTextureAtlas("e_ratlien"), "chewing_back", 0.15f, Animation.PlayMode.NORMAL);
                ratlienAttachedUpAnimation.addSequence("chewing_back_loop", 0.1f, Animation.PlayMode.LOOP);
                break;
            case LEFT:
                ratlienAttachedRight = true;
                ratlienAttachedRightAnimation = new AnimatedSprite(Assets.getTextureAtlas("e_ratlien"), "chewing_right", 0.15f, Animation.PlayMode.NORMAL);
                ratlienAttachedRightAnimation.addSequence("chewing_right_loop", 0.1f, Animation.PlayMode.LOOP);
                break;
            case RIGHT:
                ratlienAttachedLeft = true;
                ratlienAttachedLeftAnimation = new AnimatedSprite(Assets.getTextureAtlas("e_ratlien"), "chewing_left", 0.15f, Animation.PlayMode.NORMAL);
                ratlienAttachedLeftAnimation.addSequence("chewing_left_loop", 0.1f, Animation.PlayMode.LOOP);
                break;
        }
        GlobalLevelData.getInstance().increaseRatliensChewing(1);
    }


    public int howManyRatliensAttached() {
        int numRatliens = 0;
        if(ratlienAttachedLeft)
            numRatliens++;
        if(ratlienAttachedRight)
            numRatliens++;
        if(ratlienAttachedUp)
            numRatliens++;
        if(ratlienAttachedDown)
            numRatliens++;
        return numRatliens;
    }

    public AnimatedSprite getRatlienAttachedUpAnimation() {
        return ratlienAttachedUpAnimation;
    }

    public AnimatedSprite getRatlienAttachedDownAnimation() {
        return ratlienAttachedDownAnimation;
    }

    public AnimatedSprite getRatlienAttachedLeftAnimation() {
        return ratlienAttachedLeftAnimation;
    }

    public AnimatedSprite getRatlienAttachedRightAnimation() {
        return ratlienAttachedRightAnimation;
    }

    public boolean haveRatlienAttachedUp() {
        return ratlienAttachedUp;
    }

    public boolean haveRatlienAttachedDown() {
        return ratlienAttachedDown;
    }

    public boolean haveRatlienAttachedLeft() {
        return ratlienAttachedLeft;
    }

    public boolean haveRatlienAttachedRight() {
        return ratlienAttachedRight;
    }

    public void killRatlien(DIRECTION direction, boolean smashed) {
        switch (direction) {
            case UP:
                ratlienAttachedUp = false;
                break;
            case DOWN:
                ratlienAttachedDown = false;
                break;
            case LEFT:
                ratlienAttachedLeft = false;
                break;
            case RIGHT:
                ratlienAttachedRight = false;
                break;
        }
        if(smashed) {
            AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_SMASHED_1, AudioManager.SOUND.RATLIEN_SMASHED_2);
            ratliensSmashedOnMovement++;
        }
        else AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_DEAD);
        killRatlienEffect(direction);
        GlobalLevelData.getInstance().decreasingRatliensChewing(1);
    }
    public void killAllRatliens(boolean smashed){
        int howManyRatliensAttached = howManyRatliensAttached();
        if(howManyRatliensAttached == 0)
            return;

        GlobalLevelData.getInstance().decreasingRatliensChewing(howManyRatliensAttached);
        if (ratlienAttachedUp){
            killRatlienEffect(DIRECTION.UP);
            if(smashed) {
                ratliensSmashedOnMovement++;
            }
        }
        if (ratlienAttachedDown) {
            killRatlienEffect(DIRECTION.DOWN);
            if(smashed) {
                ratliensSmashedOnMovement++;
            }
        }
        if (ratlienAttachedLeft){
            killRatlienEffect(DIRECTION.LEFT);
            if(smashed) {
                ratliensSmashedOnMovement++;
            }
        }
        if (ratlienAttachedRight){
            killRatlienEffect(DIRECTION.RIGHT);
            if(smashed) {
                ratliensSmashedOnMovement++;
            }
        }
        ratlienAttachedUp = false;
        ratlienAttachedDown = false;
        ratlienAttachedLeft = false;
        ratlienAttachedRight = false;
        if(smashed) AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_SMASHED_1, AudioManager.SOUND.RATLIEN_SMASHED_2);
        else AudioManager.getInstance().playSound(AudioManager.SOUND.RATLIEN_DEAD);
        ChallengeManager.getInstance().notifyChallengeTrigger(ChallengeManager.CHALLENGE_TRIGGER.TRG_ENEMY_DESTROYED, GlobalAttributes.CHALLENGE_OBJECT.RATLIEN);
    }

    private void killRatlienEffect(DIRECTION direction){
        float despX = 0;
        float despY = 0;
        switch (direction){
            case UP: despY += 16; break;
            case DOWN: despY -= 16; break;
            case LEFT: despX -= 16; break;
            case RIGHT: despX += 16; break;
        }

        // Arreglo para que la posicion del impacto se desplace en las cajas en movimiento y el efecto quede mejor
        if(getMoveStatus() == MOVE_STATUS.MOVING){
            if(getDirection() == DIRECTION.UP || getDirection() == DIRECTION.DOWN) despY *= 2;
            if(getDirection() == DIRECTION.LEFT || getDirection() == DIRECTION.RIGHT) despX *= 2;
        }

        EffectManager.getInstance().createSpriteEffect(getPositionXCenter() + despX, getPositionY() + 26 + despY,
                GameConstants.BOARD_EFFECT, Assets.getTextureAtlas("fx_ratlien_dead"), 0.05f, Animation.PlayMode.NORMAL, true);
        EffectManager.getInstance().createEffect(ParticleEffectModel.EFFECT_TYPE.RATLIEN_DEAD, getPositionXCenter() + despX, getPositionY() + 26 + despY,
                GameConstants.ABOVE_ALL_DEPTH);
    }

    public boolean canAttachRatlien(DIRECTION direction) {
        switch (direction) {
            case UP:
                if(ratlienAttachedDown) {
                    return false;
                }
                break;
            case DOWN:
                if(ratlienAttachedUp) {
                    return false;
                }
                break;
            case LEFT:
                if(ratlienAttachedRight) {
                    return false;
                }
                break;
            case RIGHT:
                if(ratlienAttachedLeft) {
                    return false;
                }
                break;
        }
        return true;
    }

    public void resetSmashedObjectCounters() {
        ratliensSmashedOnMovement = 0;
        robotsSmashedOnMovement = 0;
    }


    public void addRobotSmashed() {
        robotsSmashedOnMovement++;
    }

    public void addRatlienSmashed() {
        ratliensSmashedOnMovement++;
    }

    public int getRobotsSmashed() {
        return robotsSmashedOnMovement;
    }

    public int getRatliensSmashed() {
        return ratliensSmashedOnMovement;
    }
}
