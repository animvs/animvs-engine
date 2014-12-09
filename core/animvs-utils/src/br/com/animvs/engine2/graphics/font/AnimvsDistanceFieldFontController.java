package br.com.animvs.engine2.graphics.font;

/**
 * Created by Daldegan on 14/10/2014.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

import br.com.animvs.engine2.internationalization.AnimvsLanguageController;

public final class AnimvsDistanceFieldFontController {
    private String languageDir;

    private BitmapFont currentFont;
    private String currentFontPath;

    private AnimvsLanguageController languageController;
    private AssetManager assetManager;

    private boolean loading;

    private float spread;
    private float paddingAdvanceY;

    private Vector2 resolutionReal;

    private ShaderProgram distanceFieldShader;

    public boolean getLoading() {
        return loading;
    }

    public BitmapFont getFont() {
        return currentFont;
    }

    public ShaderProgram getShader() {
        return distanceFieldShader;
    }

    public float getSpread() {
        return spread;
    }

    public float getPaddingAdvanceY() {
        return paddingAdvanceY;
    }

    public float getRatio(){
        return Gdx.graphics.getHeight() / resolutionReal.y;
    }

    private String getVertexShader() {
        return "uniform mat4 u_projTrans;\n" +
                "\n" +
                "attribute vec4 a_position;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "attribute vec4 a_color;\n" +
                "\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoord;\n" +
                "\n" +
                "void main() {\n" +
                "    gl_Position = u_projTrans * a_position;\n" +
                "    v_texCoord = a_texCoord0;\n" +
                "    v_color = a_color;\n" +
                "}";
    }

    private String getFragmentShader() {
        return "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" +
                "\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float smoothing;\n" +
                "\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoord;\n" +
                "\n" +
                //"const float smoothing = 0.675/16.0;\n" +
                "\n" +
                "void main() {\n" +
                "    float distance = texture2D(u_texture, v_texCoord).a * v_color.a;\n" +
                "    float alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, distance);\n" +
                "    gl_FragColor = vec4(v_color.rgb, alpha);\n" +
                "}";
    }

    public AnimvsDistanceFieldFontController(AnimvsLanguageController languageController, String languageDir,
                                             float spreadUsedDuringFontGeneration, AssetManager assetManager, Vector2 resolutionReal) {

        if (assetManager == null)
            throw new RuntimeException("The parameter 'assetManager' must be != null");

        if (languageController == null)
            throw new RuntimeException("The parameter 'languageController' must be != null");

        if (spreadUsedDuringFontGeneration <= 0)
            throw new RuntimeException("The parameter 'spreadUsedDuringFontGeneration' must be > 0");

        if (resolutionReal == null || resolutionReal.x == 0f || resolutionReal.y == 0f)
            throw new RuntimeException("The parameter 'resolutionReal' (both, x and y) must be > 0");

        this.assetManager = assetManager;
        this.resolutionReal = resolutionReal;
        this.spread = spreadUsedDuringFontGeneration;
        this.paddingAdvanceY = -(spread * 2f);
        distanceFieldShader = new ShaderProgram(getVertexShader(), getFragmentShader());
        if (!distanceFieldShader.isCompiled())
            Gdx.app.error("DISTANCE FIELD FONTS", "Shader compilation failed:\n" + distanceFieldShader.getLog());

        this.languageController = languageController;
        this.languageDir = languageDir;

        languageController.setLangChangedListener(new AnimvsLanguageController.LanguageChangedListener() {
            @Override
            public void eventLanguageChanged(String newLang) {
                loadFonts(newLang);
            }
        });
    }

    public void loadFonts() {
        loadFonts(this.languageController.getCurrentLanguage());
    }

    private void loadFonts(String abbreviation) {
        //TODO: Is necessary to dispose old font texture:
        /*if (currentFont != null)
            assetManager.unload(currentFontPath);*/

        currentFontPath = retrieveFontFilePath(abbreviation);

        BitmapFontLoader.BitmapFontParameter loadParameters = new BitmapFontLoader.BitmapFontParameter();
        loadParameters.minFilter = Texture.TextureFilter.Linear;
        loadParameters.magFilter = Texture.TextureFilter.Linear;
        loadParameters.loadedCallback = new AssetLoaderParameters.LoadedCallback() {
            @Override
            public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
                loading = false;
                currentFont = assetManager.get(currentFontPath, BitmapFont.class);
            }
        };

        assetManager.load(currentFontPath, BitmapFont.class, loadParameters);
    }

    private final String retrieveFontFilePath(String abbreviation) {
        for (int i = 0; i < languageController.getConfig().getLanguages().size; i++) {
            if (languageController.getConfig().getLanguages().get(i).getLanguageAbbreviation().equals(abbreviation))
                return languageDir + languageController.getConfig().getLanguages().get(i).getFontFilePath();
        }

        throw new RuntimeException("Language not found on config file: " + abbreviation);
    }
}

