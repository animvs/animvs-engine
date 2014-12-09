package br.com.animvs.engine2.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;

import br.com.animvs.engine2.graphics.font.AnimvsDistanceFieldFontController;

/**
 * Created by Daldegan on 14/10/2014.
 */
public class ImageTextButtonDistanceField extends Button {
    private AnimvsDistanceFieldFontController distanceFieldController;

    private Image image;
    private LabelDistanceField label;
    private ImageTextButtonDistanceFieldStyle style;

    public ImageTextButtonDistanceField(String text, Skin skin, AnimvsDistanceFieldFontController distanceFieldController, float fontSize) {
        super(skin.get("default", ImageTextButtonDistanceFieldStyle.class));
        setSkin(skin);
        initialize(text, skin, fontSize, distanceFieldController);
    }

    public ImageTextButtonDistanceField(String text, Skin skin, String styleName, AnimvsDistanceFieldFontController distanceFieldController, float fontSize) {
        super(skin.get(styleName, ImageTextButtonDistanceFieldStyle.class));
        setSkin(skin);
        initialize(text, skin, fontSize, distanceFieldController);
    }

    public void setStyle(Button.ButtonStyle style) {
        if (!(style instanceof ImageTextButtonDistanceFieldStyle))
            throw new IllegalArgumentException("style must be a ImageTextButtonDistanceFieldStyle.");
        super.setStyle(style);
        this.style = (ImageTextButtonDistanceFieldStyle) style;
        if (image != null) updateImage();
        if (label != null) {
            ImageTextButtonDistanceFieldStyle textButtonDistanceFieldStyle = (ImageTextButtonDistanceFieldStyle) style;
            LabelDistanceField.LabelDistanceFieldStyle labelStyle = label.getStyle();
            labelStyle.font = textButtonDistanceFieldStyle.font;
            labelStyle.fontColor = textButtonDistanceFieldStyle.fontColor;
            label.setStyle(labelStyle);
        }
    }

    public ImageTextButtonDistanceFieldStyle getStyle() {
        return style;
    }

    private void updateImage() {
        Drawable drawable = null;
        if (isDisabled() && style.imageDisabled != null)
            drawable = style.imageDisabled;
        else if (isPressed() && style.imageDown != null)
            drawable = style.imageDown;
        else if (isChecked() && style.imageChecked != null)
            drawable = (style.imageCheckedOver != null && isOver()) ? style.imageCheckedOver : style.imageChecked;
        else if (isOver() && style.imageOver != null)
            drawable = style.imageOver;
        else if (style.imageUp != null) //
            drawable = style.imageUp;
        image.setDrawable(drawable);
    }

    public void draw(Batch batch, float parentAlpha) {
        updateImage();
        Color fontColor;
        if (isDisabled() && style.disabledFontColor != null)
            fontColor = style.disabledFontColor;
        else if (isPressed() && style.downFontColor != null)
            fontColor = style.downFontColor;
        else if (isChecked() && style.checkedFontColor != null)
            fontColor = (isOver() && style.checkedOverFontColor != null) ? style.checkedOverFontColor : style.checkedFontColor;
        else if (isOver() && style.overFontColor != null)
            fontColor = style.overFontColor;
        else
            fontColor = style.fontColor;
        if (fontColor != null)
            label.getStyle().fontColor = fontColor;

        super.draw(batch, parentAlpha);

        label.setScale(label.getFontSize() * distanceFieldController.getRatio());
        //label.setScale(5f);
        label.setPosition(getX() + (getWidth() / 2f) - (label.getWidth() / 2f), getY() + (getHeight() / 2f) - (label.getHeight() / 2f) + distanceFieldController.getPaddingAdvanceY() * label.getFontSize() * distanceFieldController.getRatio());

        batch.setShader(distanceFieldController.getShader());
        distanceFieldController.getShader().setUniformf("smoothing", 0.25f / (distanceFieldController.getSpread() * (label.getFontSize() * distanceFieldController.getRatio())));
        label.draw(batch, parentAlpha);
        batch.setShader(null);
    }

    public Image getImage() {
        return image;
    }

    public Cell getImageCell() {
        return getCell(image);
    }

    public LabelDistanceField getLabel() {
        return label;
    }

    public Cell getLabelCell() {
        return getCell(label);
    }

    public void setText(CharSequence text) {
        label.setText(text);
    }

    public CharSequence getText() {
        return label.getText();
    }

    private void initialize(String text, Skin skin, float fontSize, AnimvsDistanceFieldFontController distanceFieldController) {
        if (fontSize <= 0)
            throw new RuntimeException("The parameter 'scale' must be > 0");

        if (distanceFieldController == null)
            throw new RuntimeException("The parameter 'distanceFieldController' must be != null");

        defaults().space(3);

        image = new Image();
        image.setScaling(Scaling.fit);
        add(image);

        this.distanceFieldController = distanceFieldController;
        label = new LabelDistanceField(text, skin, distanceFieldController, fontSize);

        label.setAlignment(Align.center);
        add(label);
        label.setVisible(false);

        setSize(getPrefWidth(), getPrefHeight());
    }

    /**
     * The style for an image text button, see {@link ImageTextButton}.
     *
     * @author Nathan Sweet
     */
    static public class ImageTextButtonDistanceFieldStyle extends TextButton.TextButtonStyle {
        /**
         * Optional.
         */
        public Drawable imageUp, imageDown, imageOver, imageChecked, imageCheckedOver, imageDisabled;
        public ImageTextButtonDistanceFieldStyle() {
        }

        public ImageTextButtonDistanceFieldStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public ImageTextButtonDistanceFieldStyle(ImageTextButtonDistanceFieldStyle style) {
            super(style);
            if (style.imageUp != null) this.imageUp = style.imageUp;
            if (style.imageDown != null) this.imageDown = style.imageDown;
            if (style.imageOver != null) this.imageOver = style.imageOver;
            if (style.imageChecked != null) this.imageChecked = style.imageChecked;
            if (style.imageCheckedOver != null) this.imageCheckedOver = style.imageCheckedOver;
            if (style.imageDisabled != null) this.imageDisabled = style.imageDisabled;
        }

        public ImageTextButtonDistanceFieldStyle(TextButton.TextButtonStyle style) {
            super(style);
        }
    }
}
