package br.com.animvs.engine2.graficos.shaders;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ShaderSplatt extends Shader {

    private Texture background;
    private Texture mix1;
    private Texture mix2;
    private Texture alpha;

    private Vector2 ratioFundo;
    private Vector2 ratioMix1;
    private Vector2 ratioMix2;

    private float tempoIluminacao;
    private boolean usaIluminacao;

    private Vector2 resolution;

    @Override
    protected FileHandle getFragmentShaderFile() {
        return Gdx.files.internal("data/graphics/shaders/frag/splatt_TEST.frag");
    }

    @Override
    protected FileHandle getVertexShaderFile() {
        return Gdx.files.internal("data/graphics/shaders/vert/fullquadTextured.vert");
    }

    protected Vector2 getResolution() {
        resolution.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return resolution;
    }

    @Override
    public void update() {
        // Seta texturas utilizadas pelo shader:
        setUniformi("s_background", 1);
        setUniformi("s_alpha", 2);
        setUniformi("s_mix1", 3);
        setUniformi("s_mix2", 4);

        passaParametrosRatio();

        background.bind(1);
        alpha.bind(2);
        mix1.bind(3);
        mix2.bind(4);

        tempoIluminacao += Gdx.graphics.getDeltaTime() * 0.02f;

        if (tempoIluminacao > 1f) {
            tempoIluminacao = 1f - tempoIluminacao;
//			Engine.loga("LIGHT SHADER", "Resetado: " + String.valueOf(tempoIluminacao));
        }

        setUniformf("tempoIluminacao", tempoIluminacao);

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        // batch.draw(background, 0f, 0f, 1280f, 768f);
    }

    public final boolean getUsaIluminacao() {
        return usaIluminacao;
    }

    public final void setUsaIluminacao(boolean usaIluminacao) {
        this.usaIluminacao = usaIluminacao;
    }

    public ShaderSplatt(Texture alpha, Texture background, Texture mix1, Texture mix2) {
        this.alpha = alpha;
        this.background = background;
        this.mix1 = mix1;
        this.mix2 = mix2;

        resolution = new Vector2();

        preparaParametrosRatio();
    }

    private final void passaParametrosRatio() {
        setUniformf("detailScale_background", ratioFundo);
        setUniformf("detailScale_mix1", ratioMix1);
        setUniformf("detailScale_mix2", ratioMix2);

        setUniformi("usaIluminacao", usaIluminacao ? 1 : 0);
    }

    private final void preparaParametrosRatio() {

        ratioFundo = new Vector2(getResolution().x / background.getWidth(), getResolution().y / background.getHeight());
        ratioMix1 = new Vector2(getResolution().x / mix1.getWidth(), (getResolution().y / mix1.getHeight()));
        ratioMix2 = new Vector2(getResolution().x / mix2.getWidth(), getResolution().y / mix2.getHeight());

        /*ratioFundo = ((getResolution().x / background.getWidth()) + (getResolution().y / background.getHeight())) / 2f;
        ratioFundo = limitaRatio(ratioFundo);*/

        /*ratioMix1 = ((getResolution().x / mix1.getWidth()) + (getResolution().y / mix1.getHeight())) / 2f;
        ratioMix1 = limitaRatio(ratioMix1);

        ratioMix2 = ((getResolution().x / mix2.getWidth()) + (getResolution().y / mix2.getHeight())) / 2f;
        ratioMix2 = limitaRatio(ratioMix2);*/
    }

    private final float limitaRatio(float ratio) {
        if (ratio >= 8)
            ratio *= 0.75f;

        return ratio;
    }
}
