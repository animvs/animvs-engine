package br.com.animvs.engine2.graficos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Created by Daldegan on 18/08/2014.
 */
public class ImageAnimated extends Widget {

    private final float offsetX;
    private final float offsetY;
    private AnimacaoSkeletal graphic;

    public void setAnimation(String animation, boolean loop) {
        graphic.setAnimation(animation, loop);
    }

    public void addAnimation(String animation, boolean loop, float delay) {
        graphic.addAnimation(animation, loop, delay);
    }

    public void setInterpolation(String animationFrom, String animatioTo, float duration) {
        graphic.setInterpolation(animationFrom, animatioTo, duration);
    }

    public void setInterpolationDefault(float duration) {
        graphic.setInterpolationDefault(duration);
    }

    public void setAnimationPosition(float pos){
        graphic.setAnimationPosition(pos);
    }

    public float getDuration(String animation){
        return graphic.getDuration(animation);
    }

    @Override
    public void setColor(Color color) {
        graphic.getColor().set(color);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        graphic.getColor().set(r, g, b, a);
    }

    @Override
    public Color getColor() {
        return graphic.getColor();
    }

    public void setSkin(String skin) {
        graphic.setSkin(skin);
    }

    @Override
    public final float getHeight() {
        return graphic.getHeight();
    }

    public ImageAnimated(AnimacaoSkeletal graphic, String skin, String animation, boolean loop, float offsetX, float offsetY) {
        if (graphic == null)
            throw new RuntimeException("The parameter 'graphic' must be != null");

        this.offsetX = offsetX;
        this.offsetY = offsetY;

        this.graphic = graphic;

        graphic.setSkin(skin);
        graphic.setAnimation(animation, loop);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        graphic.getColor().a = parentAlpha;
        //graphic.getColor().a = 1f;

        //graphic.setPosicao(getX() + (getX() * offsetX), getY() + (getY() * offsetY));
        graphic.setPosicao(getX() + offsetX, getY() + offsetY);

        graphic.setEscala(getScaleX(), getScaleY());
        graphic.setRotacao(getRotation());

        batch.end();

         graphic.render(batch, Gdx.graphics.getDeltaTime());
        batch.begin();

    }
}
