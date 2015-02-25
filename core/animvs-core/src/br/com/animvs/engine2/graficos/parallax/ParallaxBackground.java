package br.com.animvs.engine2.graficos.parallax;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by DALDEGAN on 24/02/2015.
 */
public final class ParallaxBackground implements Disposable {
    private ParallaxLayer[] layers;
    private Camera camera;
    private SpriteBatch batch;
    private Vector2 speed = new Vector2();

    /**
     * @param layers The  background layers
     * @param width  The screenWith
     * @param height The screenHeight
     * @param speed  A Vector2 attribute to point out the x and y speed
     */
    public ParallaxBackground(ParallaxLayer[] layers, float width, float height, Vector2 speed, Camera camera) {
        this.layers = layers;
        this.speed.set(speed);

        if (camera == null)
            this.camera = new OrthographicCamera(width, height);
        else
            this.camera = camera;

        batch = new SpriteBatch();
    }

    public void render(float delta) {
        this.camera.position.add(speed.x * delta, speed.y * delta, 0);
        for (ParallaxLayer layer : layers) {
            batch.setProjectionMatrix(camera.projection);
            batch.begin();
            float currentX = -camera.position.x * layer.parallaxRatio.x % (layer.region.getRegionWidth() + layer.padding.x);

            if (speed.x < 0) currentX += -(layer.region.getRegionWidth() + layer.padding.x);
            do {
                float currentY = -camera.position.y * layer.parallaxRatio.y % (layer.region.getRegionHeight() + layer.padding.y);
                if (speed.y < 0) currentY += -(layer.region.getRegionHeight() + layer.padding.y);
                do {
                    batch.draw(layer.region,
                            -this.camera.viewportWidth / 2 + currentX + layer.startPosition.x,
                            -this.camera.viewportHeight / 2 + currentY + layer.startPosition.y);
                    currentY += (layer.region.getRegionHeight() + layer.padding.y);
                } while (currentY < camera.viewportHeight);
                currentX += (layer.region.getRegionWidth() + layer.padding.x);
            } while (currentX < camera.viewportWidth);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
