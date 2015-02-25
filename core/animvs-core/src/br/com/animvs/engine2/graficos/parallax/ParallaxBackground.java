package br.com.animvs.engine2.graficos.parallax;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by DALDEGAN on 24/02/2015.
 */
public final class ParallaxBackground implements Disposable {
    private ParallaxLayer[] layers;
    private Viewport viewport;
    private SpriteBatch batch;
    private Vector2 speed = new Vector2();

    /**
     * @param layers The  background layers
     * @param width  The screenWith
     * @param height The screenHeight
     * @param speed  A Vector2 attribute to point out the x and y speed
     */
    public ParallaxBackground(ParallaxLayer[] layers, float width, float height, Vector2 speed, Viewport viewport) {
        this.layers = layers;
        this.speed.set(speed);

        if (viewport == null)
            throw new RuntimeException("The parameter 'viewport' must be != NULL");

        this.viewport = viewport;
        batch = new SpriteBatch();
    }

    public void render(float delta) {
        viewport.getCamera().position.add(speed.x * delta, speed.y * delta, 0f);
        for (int i = 0; i < layers.length; i++) {
            batch.setProjectionMatrix(viewport.getCamera().projection);
            batch.begin();
            float currentX = -viewport.getCamera().position.x * layers[i].parallaxRatio.x % (layers[i].region.getRegionWidth() + layers[i].padding.x);

            if (speed.x < 0) currentX += -(layers[i].region.getRegionWidth() + layers[i].padding.x);
            do {
                float currentY = -viewport.getCamera().position.y * layers[i].parallaxRatio.y % (layers[i].region.getRegionHeight() + layers[i].padding.y);

                if (speed.y < 0)
                    currentY += -(layers[i].region.getRegionHeight() + layers[i].padding.y);

                do {
                    batch.draw(layers[i].region,
                            -viewport.getCamera().viewportWidth / 2 + currentX + layers[i].startPosition.x,
                            -viewport.getCamera().viewportHeight / 2 + currentY + layers[i].startPosition.y);

                    currentY += (layers[i].region.getRegionHeight() + layers[i].padding.y);
                } while (currentY < viewport.getCamera().viewportHeight);

                currentX += (layers[i].region.getRegionWidth() + layers[i].padding.x);
            } while (currentX < viewport.getCamera().viewportWidth);
            batch.end();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
