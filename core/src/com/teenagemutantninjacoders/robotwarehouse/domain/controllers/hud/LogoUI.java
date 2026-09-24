package com.teenagemutantninjacoders.robotwarehouse.domain.controllers.hud;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.display.Assets;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;

/**
 * Created by JordiM on 11/03/2017.
 */

public class LogoUI extends BaseUI{
    private TextureAtlas atlas;
    public LogoUI(Viewport viewport){
        super(viewport);
        GUIstage = new Stage();
        GUIstage.setViewport(viewport);
        atlas = Assets.getTextureAtlas("logos");
        showLogo();
        addFadeSystem();
        screenFadeIn();
    }

    private void showLogo(){
        Image background = new Image(atlas.findRegion("mncScreenBackground"));
        Image logo = new Image(atlas.findRegion("mncScreenLogo"));

        background.setSize(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);

        float logoPosX = (GameConstants.HORIZONTAL_RESOLUTION / 2) - (logo.getWidth() / 2);
        float logoPosY = (GameConstants.VERTICAL_RESOLUTION / 2) - (logo.getHeight() / 2);
        logo.setPosition(logoPosX, logoPosY);

        GUIstage.addActor(background);
        GUIstage.addActor(logo);
    }

    public void update(float delta){
        updateScreenFade(delta);
        GUIstage.act(delta);
        GUIstage.draw();
    }

    public Stage getUIStage(){
        return GUIstage;
    }

    public void dispose(){
        GUIstage.dispose();
    }
}
