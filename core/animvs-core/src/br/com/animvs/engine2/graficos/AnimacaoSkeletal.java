package br.com.animvs.engine2.graficos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

import br.com.animvs.engine2.graficos.loaders.AnimacaoSkeletalData;
import br.com.animvs.engine2.graficos.shaders.Shader;

public class AnimacaoSkeletal {
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;

    private SkeletonData skeletonData;
    private AnimationStateData animationStateData;

    private Skeleton skeleton;
    private AnimationState animationState;
    private Array<Event> events = new Array();

    private SkeletonBounds bounds;

    private Vector2 escala;

    private float rotacao;

    private Shader shader;

    private float animationSpeedScale;

    private float animationPosition;

    private PolygonSpriteBatch polygonBatchCache;

    public float getAnimationSpeedScale() {
        return animationSpeedScale;
    }

    public void setAnimationSpeedScale(float animationSpeedScale) {
        this.animationSpeedScale = animationSpeedScale;
    }

    public final Shader getShader() {
        return shader;
    }

    public final void setShader(Shader shader) {
        this.shader = shader;
    }

    public final Vector2 getEscala() {
        return escala;
    }

    public void setEscalaX(float x) {
        escala.set(x, escala.y);
    }

    public void setEscalaY(float y) {
        escala.set(escala.x, y);
    }

    public void setEscala(float escalaX, float escalaY) {
        this.escala.set(escalaX, escalaY);
    }

    public Color getColor() {
        return skeleton.getColor();
    }

    public final String getSkin() {
        return skeleton.getSkin().getName();
    }

    public final void setAnimation(String nomeAnimacao, boolean loop) {
        animationState.setAnimation(0, nomeAnimacao, loop);
    }

    public final void addAnimation(String nomeAnimacao, boolean loop) {
        addAnimation(nomeAnimacao, loop, 0f);
    }

    public final void addAnimation(String nomeAnimacao, boolean loop, float delay) {
        animationState.addAnimation(0, nomeAnimacao, loop, delay);
    }

    public final void setSkin(String skinNome) {
        skeleton.setSkin(skinNome);
    }

    public void setX(float x) {
        skeleton.setX(x);
    }

    public void setY(float y) {
        skeleton.setY(y);
    }

    public float getX() {
        return skeleton.getX();
    }

    public float getY() {
        return skeleton.getY();
    }

    public float getRotacao() {
        return rotacao;
    }

    public final void setPosicao(float x, float y) {
        skeleton.setPosition(x, y);
    }

    public final SkeletonBounds getBounds(boolean updateAabb) {
        if (bounds == null)
            bounds = new SkeletonBounds();

        bounds.update(skeleton, updateAabb);

        return bounds;
    }

    public float getWidth() {
        return skeletonData.getWidth();
    }

    public float getHeight() {
        return skeletonData.getHeight();
    }

    public AnimacaoSkeletal(AnimacaoSkeletalData animacaoSkeletalData) {
        this.skeletonData = animacaoSkeletalData.skeletonData;
        this.animationStateData = animacaoSkeletalData.animationStateData;
        escala = new Vector2(1f, 1f);
        animationSpeedScale = 1f;
        animationPosition = -1;

        skeleton = new Skeleton(skeletonData);
        skeleton.setToSetupPose();
        skeleton = new Skeleton(skeleton);
        skeleton.updateWorldTransform();

        animationStateData.setDefaultMix(0.25f);
        animationState = new AnimationState(animationStateData);

        renderer = new SkeletonRenderer();
    }

    public void setInterpolation(String animationFrom, String animatioTo, float duration) {
        animationStateData.setMix(animationFrom, animatioTo, duration);
    }

    public void setInterpolationDefault(float duration) {
        animationStateData.setDefaultMix(duration);
    }

    public float getDuration(String animationName) {
        Animation animation = skeletonData.findAnimation(animationName);

        if (animation == null)
            return 0f;

        return animation.getDuration();
    }

    public final void render(Batch batch, float delta) {
        if (polygonBatchCache == null && batch instanceof PolygonSpriteBatch)
            polygonBatchCache = (PolygonSpriteBatch) batch;

        if (animationPosition <= -1)
            animationState.update(delta * animationSpeedScale);
        else
            animationState.getCurrent(0).setTime(animationPosition);

        // skeleton.update(delta);
        /*skeleton.setToSetupPose();*/

        atualizaEscala();

        atualizaRotacao();

        animationState.apply(skeleton);
        skeleton.updateWorldTransform();

        batch.begin();

        if (shader != null) {
            shader.beginRender(batch);
            shader.update();
        }

        if (polygonBatchCache != null)
            renderer.draw(polygonBatchCache, skeleton);
        else
            renderer.draw(batch, skeleton);

        if (shader != null)
            shader.endRender(batch);

        batch.end();
    }

    public final void render(PolygonSpriteBatch batch, float delta) {
        if (animationPosition <= -1)
            animationState.update(delta * animationSpeedScale);
        else
            animationState.update(animationPosition);

        // skeleton.update(delta);
        skeleton.setToSetupPose();

        atualizaEscala();

        atualizaRotacao();

        animationState.apply(skeleton);
        skeleton.updateWorldTransform();

        batch.begin();

        if (shader != null) {
            shader.beginRender(batch);
            shader.update();
        }

        renderer.draw(batch, skeleton);

        if (shader != null)
            shader.endRender(batch);

        batch.end();
    }

    public final void flipX(boolean flip) {
        skeleton.setFlipX(flip);
    }

    public final void flipY(boolean flip) {
        skeleton.setFlipY(flip);
    }

    private final void atualizaEscala() {
        if (escala.x != 1f)
            skeleton.getRootBone().setScaleX(escala.x);

        if (escala.y != 1f)
            skeleton.getRootBone().setScaleY(escala.y);
    }

    public void setRotacao(float rotacao) {
        this.rotacao = rotacao;
    }

    public void setAnimationPosition(float position) {
        this.animationPosition = position;
    }

    private final void atualizaRotacao() {
        skeleton.getRootBone().setRotation(rotacao);
    }
}