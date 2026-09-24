package com.teenagemutantninjacoders.robotwarehouse.display.painters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalGeneralData;
import com.teenagemutantninjacoders.robotwarehouse.data.GlobalLevelData;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.GameObjectDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.ParticleEffectModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by JordiRM on 04/02/2016.
 */
public class GamePainter extends BasePainter{
    private SpriteBatch batch;
    private BitmapFont testFont;
    private Sprite fadeSprite;
    private GameObjectDraw genericGameObjectDraw;
    private float gameZoneFadeAlpha = 0f;
    private boolean zoneFadeActivated = false;
    private float zoneFadeSpeed = 5;
    private ShapeRenderer touchLine;
    private Vector2 touchLineBegin, touchLineEnd;
    private Texture pointTestTexture = new Texture(Gdx.files.internal("images/test_point.png"));
    private Texture background;
    private TextureRegion shipPlatform;
    private Sprite pointTest = new Sprite(pointTestTexture);
    private boolean drawTouchActivity;
    private boolean paintFxAlways = false;
    private GAME_ZONE_FADE_STATUS gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FULL_OUT;

    private void createTouchLineRenderer() {
        touchLine = new ShapeRenderer();
        touchLine.setProjectionMatrix(camera.combined);
        touchLineBegin = new Vector2(0,0);
        touchLineEnd = new Vector2(0,0);
    }

    private void createSpriteBatch() {
        this.batch = new SpriteBatch();
        this.batch.setProjectionMatrix(camera.combined);
    }

    private void createAuxiliarySprites() {
        fadeSprite = new Sprite(Assets.getTextureAtlas("general_screen_elements").findRegion("pixel"));
        fadeSprite.setPosition(0,0);
        fadeSprite.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        fadeSprite.setColor(Color.BLACK);
    }

    private void createFont() {
        testFont = new BitmapFont();
        testFont.setColor(Color.WHITE);
        testFont.getData().setScale(1.5f, 1.5f);
    }

    public void initializePainter(){
        createSpriteBatch();
        createFont();
        createAuxiliarySprites();
        drawTouchActivity = GlobalGeneralData.getInstance().debug_drawTouchActivity;
        if(drawTouchActivity) createTouchLineRenderer();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        genericGameObjectDraw = new GameObjectDraw();
        background = Assets.getTexture("bg_space_" + Integer.toString(GlobalLevelData.getInstance().getBackground()));
        shipPlatform = Assets.getTextureAtlas("spaceDock").findRegion("shipPlatform");
    }

    public void initBatch() {
        batch.begin();
    }

    public void endBatch() {
        batch.end();

        if(drawTouchActivity) drawTouchLine();
    }

    private void drawTouchLine() {
        touchLine.begin(ShapeRenderer.ShapeType.Line);
        touchLine.line(touchLineBegin, touchLineEnd);
        touchLine.end();
    }

    public void paintGameObjects(ArrayList<GameObject> currentGameObjects, float delta){
        paintFxAlways = GlobalLevelData.getInstance().isPaintingFxAlways();
        paintBackground();
        paintSpaceDock();

        // Ordenamos la lista que nos llega segun el depth de cada objeto
        Collections.sort(currentGameObjects, new Comparator<GameObject>() {
            @Override
            public int compare(GameObject p1, GameObject p2) {
                return new Integer(p2.getDepth()).compareTo(new Integer(p1.getDepth()));
            }
        });

        for (GameObject currentGameObject : currentGameObjects) {
            if(currentGameObject instanceof ParticleEffectModel){
                paintParticleEffect(delta, (ParticleEffectModel) currentGameObject);
            } else {
                paintGameObject(delta, currentGameObject);
            }
        }

        // Oscurecimiento de la zona de juego
        paintGameZoneFX(delta);
        if(drawTouchActivity) pointTest.draw(batch);
    }

    private void paintParticleEffect(float delta, ParticleEffectModel currentGameObject) {
        if(GlobalLevelData.getInstance().getLevelStatus() == GlobalLevelData.LEVEL_STATUS.PLAYING || paintFxAlways) {
            currentGameObject.getParticleEffect().draw(batch, delta);
        }else{
            currentGameObject.getParticleEffect().draw(batch);
        }
    }

    private void paintGameObject(float delta, GameObject currentGameObject) {
        if(currentGameObject.isVisible()) {
            if (currentGameObject.getGameObjectDraw() == null) {
                genericGameObjectDraw.draw(currentGameObject, batch, delta);
            } else {
                currentGameObject.getGameObjectDraw().draw(currentGameObject, batch, delta);
            }
        }
    }

    public void touchDownTest(float x, float y){
        pointTest.setPosition(x - 6, y - 6);
        touchLineBegin.x = x;
        touchLineBegin.y = y;
        touchLineEnd.x = x;
        touchLineEnd.y = y;
    }
    public void touchdragTest(float x, float y){
        touchLineEnd.x = x;
        touchLineEnd.y = y;
    }

    private void paintSpaceDock(){
        batch.draw(shipPlatform, 6, 0);//7
    }
    private void paintBackground(){
        batch.draw(background, 0, 0);
    }

    private void paintGameZoneFX(float delta){

        if(zoneFadeActivated && gameZoneFadeAlpha < 0.4f){
            gameZoneFadeAlpha = MathUtils.clamp(gameZoneFadeAlpha + (delta * zoneFadeSpeed), 0, 0.4f);
            if(gameZoneFadeAlpha == 0.4f) gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FULL_IN;
        }
        else if (!zoneFadeActivated && gameZoneFadeAlpha > 0){
            gameZoneFadeAlpha = MathUtils.clamp(gameZoneFadeAlpha - (delta * zoneFadeSpeed), 0, 0.4f);
            if(gameZoneFadeAlpha == 0) gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FULL_OUT;
        }
        if(gameZoneFadeAlpha >0) {
            fadeSprite.setAlpha(gameZoneFadeAlpha);
            fadeSprite.draw(batch);
        }
    }
    public void gameZoneFade(boolean state){
        zoneFadeActivated = state;
        zoneFadeSpeed = 5;
        if(state) gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FADING_IN;
        else gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FADING_OUT;
    }
    public void gameZoneFade(boolean state, float speed){
        zoneFadeActivated = state;
        zoneFadeSpeed = speed;
        if(state) gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FADING_IN;
        else gameZoneFadeStatus = GAME_ZONE_FADE_STATUS.FADING_OUT;
    }

    public GAME_ZONE_FADE_STATUS getGameZoneFadeStatus(){
        return gameZoneFadeStatus;
    }

    public void paintUIElements(Stage stage, float delta){
        stage.act(delta);
        stage.draw();
    }

    public void dispose(){
        batch.dispose();
        testFont.dispose();

        pointTestTexture.dispose();
    }

    public enum GAME_ZONE_FADE_STATUS {
        FADING_IN, FADING_OUT, FULL_IN, FULL_OUT;
    }
}