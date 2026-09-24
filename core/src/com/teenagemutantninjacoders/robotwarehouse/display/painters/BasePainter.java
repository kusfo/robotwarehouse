package com.teenagemutantninjacoders.robotwarehouse.display.painters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teenagemutantninjacoders.robotwarehouse.domain.GameConstants;

/**
 * Created by JordiRM on 10/01/2017.
 */
public class BasePainter {
    protected OrthographicCamera camera;
    protected Viewport viewport;
    public BasePainter(){
        camera = new OrthographicCamera(GameConstants.HORIZONTAL_RESOLUTION, GameConstants.VERTICAL_RESOLUTION);
        camera.setToOrtho(false, GameConstants.VIRTUAL_HORIZONTAL_RESOLUTION, GameConstants.VIRTUAL_VERTICAL_RESOLUTION);
        viewport = new FitViewport(GameConstants.VIRTUAL_HORIZONTAL_RESOLUTION, GameConstants.VIRTUAL_VERTICAL_RESOLUTION, camera);
    }
    public void initFrame() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    public Viewport getViewport(){
        return viewport;
    }
}
