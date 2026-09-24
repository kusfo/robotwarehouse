package com.teenagemutantninjacoders.robotwarehouse.domain.models.scenary.spaceDock;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.teenagemutantninjacoders.robotwarehouse.display.objectDraws.GameObjectDraw;
import com.teenagemutantninjacoders.robotwarehouse.domain.models.GameObject;

/**
 * Created by JordiRM on 24/07/2017.
 */
public class SpaceDockMonitorDraw extends GameObjectDraw{
    @Override
    public void draw(GameObject currentGameObject, Batch batch, float delta){
        SpaceDockMonitor spaceDockMonitor = (SpaceDockMonitor) currentGameObject;

        // Dibujamos la pantalla animada
        spaceDockMonitor.getScreen().setPosition(currentGameObject.getPositionX() + 32, currentGameObject.getPositionY() + 39);
        spaceDockMonitor.getScreen().draw(batch, delta);

        // Dibujamos el marco
        drawSprite(currentGameObject, batch, delta);

        // Dibujamos el marcador numérico con escalado o sin el
        GlyphLayout layout = spaceDockMonitor.getMonitorLayout();
        BitmapFont font = spaceDockMonitor.getMonitorFont();

        if(spaceDockMonitor.getAdquiredBounceCounter() != 1) {
            font.getData().setScale(spaceDockMonitor.getTextScale(), spaceDockMonitor.getTextScale());
            layout.setText(font, Integer.toString(spaceDockMonitor.getRequestedBoxesShowed()));
            font.draw(batch, layout, (spaceDockMonitor.getPositionX() + 64) - (layout.width / 2), (spaceDockMonitor.getPositionY() + 58) + (layout.height / 2));
            font.getData().setScale(1.0f, 1.0f); // Como estamos escalando una fuente general solo la escalamos al dibujar y luego la restauramos
        }else{
            layout.setText(font, Integer.toString(spaceDockMonitor.getRequestedBoxesShowed()));
            font.draw(batch, layout, (spaceDockMonitor.getPositionX() + 64) - (layout.width / 2), (spaceDockMonitor.getPositionY() + 58) + (layout.height / 2));
        }
    }
}