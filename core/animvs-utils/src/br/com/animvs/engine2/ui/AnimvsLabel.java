package br.com.animvs.engine2.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import br.com.animvs.engine2.graphics.font.AnimvsDistanceFieldFontController;

/**
 * Created by MS on 17/10/2014.
 */
public class AnimvsLabel extends Label {

    private AnimvsDistanceFieldFontController controller;

    private float fontSize;

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public AnimvsLabel(CharSequence text, Skin skin, AnimvsDistanceFieldFontController fontsController) {
        super(text, skin);
        initialize(fontsController);
    }

    public AnimvsLabel(CharSequence text, Skin skin, String styleName, AnimvsDistanceFieldFontController fontsController) {
        super(text, skin, styleName);
        initialize(fontsController);
    }

    public AnimvsLabel(CharSequence text, LabelStyle style, AnimvsDistanceFieldFontController fontsController) {
        super(text, style);
        initialize(fontsController);
    }

    private void initialize(AnimvsDistanceFieldFontController controller) {
        this.controller = controller;
        fontSize = 1f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        setFontScale(fontSize * controller.getRatio());

        batch.setShader(controller.getShader());
        controller.getShader().setUniformf("smoothing", 0.25f / (controller.getSpread() * getFontScaleY()));
        super.draw(batch, parentAlpha);
        batch.setShader(null);
    }
}
