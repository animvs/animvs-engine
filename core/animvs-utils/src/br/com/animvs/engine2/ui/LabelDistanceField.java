package br.com.animvs.engine2.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.StringBuilder;

import br.com.animvs.engine2.graphics.font.AnimvsDistanceFieldFontController;

/**
 * Created by Daldegan on 14/10/2014.
 */
public class LabelDistanceField extends Widget {

    /*public LabelDistanceField(AnimvsDistanceFieldFontController distanceFieldController, CharSequence text, Skin skin, float fontSize) {
        super(text, skin);
        initialize(fontSize, distanceFieldController);
    }

    public LabelDistanceField(AnimvsDistanceFieldFontController distanceFieldController, CharSequence text, Skin skin, String styleName, float fontSize) {
        super(text, skin, styleName);
        initialize(fontSize, distanceFieldController);
    }*/

    /*@Override
    public void draw(Batch batch, float parentAlpha) {
        setFontSize(fontSize * distanceFieldController.getRatio());

        float originalY = getY();
        setY(getY() - (getFontScaleY() * distanceFieldController.getPaddingAdvanceY()));

        batch.setShader(distanceFieldController.getShader());
        distanceFieldController.getShader().setUniformf("smoothing", 0.25f / (distanceFieldController.getSpread() * getFontScaleY()));
        super.draw(batch, parentAlpha);
        batch.setShader(null);

        setY(originalY);
    }*/

    static private final Color tempColor = new Color();

    private AnimvsDistanceFieldFontController distanceFieldController;

    private LabelDistanceFieldStyle style;
    private final BitmapFont.TextBounds bounds = new BitmapFont.TextBounds();
    private final com.badlogic.gdx.utils.StringBuilder text = new StringBuilder();
    private StringBuilder tempText;
    private BitmapFontCache cache;
    private int labelAlign = Align.left;
    private BitmapFont.HAlignment lineAlign = BitmapFont.HAlignment.LEFT;
    private boolean wrap;
    private float lastPrefHeight;
    private boolean sizeInvalid;
    private float fontSize;
    private boolean ellipse;

    protected AnimvsDistanceFieldFontController getDistanceFieldController() {
        return distanceFieldController;
    }

    public LabelDistanceField(CharSequence text, Skin skin, AnimvsDistanceFieldFontController controller, float fontSize) {
        this(controller, text, skin.get(LabelDistanceFieldStyle.class), fontSize);
    }

    public LabelDistanceField(CharSequence text, Skin skin, String styleName, AnimvsDistanceFieldFontController controller, float fontSize) {
        this(controller, text, skin.get(styleName, LabelDistanceFieldStyle.class), fontSize);
    }

    /**
     * Creates a label, using a {@link LabelDistanceFieldStyle} that has a BitmapFont with the specified name from the skin and the specified
     * color.
     */
    public LabelDistanceField(AnimvsDistanceFieldFontController controller, CharSequence text, Skin skin, String fontName, Color color, float fontSize) {
        this(controller, text, new LabelDistanceFieldStyle(skin.getFont(fontName), color), fontSize);
    }

    /**
     * Creates a label, using a {@link LabelDistanceFieldStyle} that has a BitmapFont with the specified name and the specified color from the
     * skin.
     */
    public LabelDistanceField(AnimvsDistanceFieldFontController controller, CharSequence text, Skin skin, String fontName, String colorName, float fontSize) {
        this(controller, text, new LabelDistanceFieldStyle(skin.getFont(fontName), skin.getColor(colorName)), fontSize);
    }

    public LabelDistanceField(AnimvsDistanceFieldFontController controller, CharSequence text, LabelDistanceFieldStyle style, float fontSize) {
        if (text != null)
            this.text.append(text);

        setStyle(style);
        initialize(fontSize, controller);
        setSize(getPrefWidth(), getPrefHeight());
    }

    private void initialize(float scale, AnimvsDistanceFieldFontController distanceFieldController) {
        if (scale <= 0)
            throw new RuntimeException("The parameter 'scale' must be > 0");

        if (distanceFieldController == null)
            throw new RuntimeException("The parameter 'distanceFieldController' must be != null");

        this.sizeInvalid = true;
        this.fontSize = scale;
        this.distanceFieldController = distanceFieldController;

        setScale(fontSize * distanceFieldController.getRatio());
    }

    public void setStyle(LabelDistanceFieldStyle style) {
        if (style == null) throw new IllegalArgumentException("style cannot be null.");
        if (style.font == null) throw new IllegalArgumentException("Missing LabelStyle font.");
        this.style = style;
        cache = new BitmapFontCache(style.font, style.font.usesIntegerPositions());
        invalidateHierarchy();
    }

    /**
     * Returns the label's style. Modifying the returned style may not have an effect until {@link #setStyle(LabelDistanceFieldStyle)} is
     * called.
     */
    public LabelDistanceFieldStyle getStyle() {
        return style;
    }

    /**
     * @param newText May be null.
     */
    public void setText(CharSequence newText) {
        if (newText instanceof StringBuilder) {
            if (text.equals(newText)) return;
            text.setLength(0);
            text.append((StringBuilder) newText);
        } else {
            if (newText == null) newText = "";
            if (textEquals(newText)) return;
            text.setLength(0);
            text.append(newText);
        }
        invalidateHierarchy();
    }

    public boolean textEquals(CharSequence other) {
        int length = text.length;
        char[] chars = text.chars;
        if (length != other.length()) return false;
        for (int i = 0; i < length; i++)
            if (chars[i] != other.charAt(i)) return false;
        return true;
    }

    public StringBuilder getText() {
        return text;
    }

    public void invalidate() {
        super.invalidate();
        sizeInvalid = true;
    }

    private void scaleAndComputeSize() {
        BitmapFont font = cache.getFont();
        float oldScaleX = font.getScaleX();
        float oldScaleY = font.getScaleY();
        if (fontSize != 1f)
            font.setScale(fontSize);

        computeSize();

        if (fontSize != 1f)
            font.setScale(oldScaleX, oldScaleY);
    }

    private void computeSize() {
        sizeInvalid = false;
        if (wrap) {
            float width = getWidth();
            if (style.background != null)
                width -= style.background.getLeftWidth() + style.background.getRightWidth();
            bounds.set(cache.getFont().getWrappedBounds(text, width));
        } else
            bounds.set(cache.getFont().getMultiLineBounds(text));
    }

    public void layout() {
        BitmapFont font = cache.getFont();
        float oldScaleX = font.getScaleX();
        float oldScaleY = font.getScaleY();

        font.setScale(fontSize);

        if (sizeInvalid)
            computeSize();

        if (wrap) {
            float prefHeight = getPrefHeight();
            if (prefHeight != lastPrefHeight) {
                lastPrefHeight = prefHeight;
                invalidateHierarchy();
            }
        }

        float width = getWidth(), height = getHeight();
        StringBuilder text;
        if (ellipse && width < bounds.width) {
            float ellipseWidth = font.getBounds("...").width;
            text = tempText != null ? tempText : (tempText = new StringBuilder());
            text.setLength(0);
            if (width > ellipseWidth) {
                text.append(this.text, 0, font.computeVisibleGlyphs(this.text, 0, this.text.length, width - ellipseWidth));
                text.append("...");
            }
        } else
            text = this.text;

        Drawable background = style.background;
        float x = 0, y = 0;
        if (background != null) {
            x = background.getLeftWidth();
            y = background.getBottomHeight();
            width -= background.getLeftWidth() + background.getRightWidth();
            height -= background.getBottomHeight() + background.getTopHeight();
        }
        if ((labelAlign & Align.top) != 0) {
            y += cache.getFont().isFlipped() ? 0 : height - bounds.height;
            y += style.font.getDescent();
        } else if ((labelAlign & Align.bottom) != 0) {
            y += cache.getFont().isFlipped() ? height - bounds.height : 0;
            y -= style.font.getDescent();
        } else
            y += (int) ((height - bounds.height) / 2);
        if (!cache.getFont().isFlipped()) y += bounds.height;

        if ((labelAlign & Align.left) == 0) {
            if ((labelAlign & Align.right) != 0)
                x += width - bounds.width;
            else
                x += (int) ((width - bounds.width) / 2);
        }

        if (wrap)
            cache.setWrappedText(text, x, y, bounds.width, lineAlign);
        else
            cache.setMultiLineText(text, x, y, bounds.width, lineAlign);

        font.setScale(oldScaleX, oldScaleY);
    }

    public void draw(Batch batch, float parentAlpha) {
        validate();
        Color color = tempColor.set(getColor());
        color.a *= parentAlpha;
        if (style.background != null) {
            batch.setColor(color.r, color.g, color.b, color.a);
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }
        if (style.fontColor != null)
            color.mul(style.fontColor);

        cache.getFont().setScale(fontSize * distanceFieldController.getRatio());

        //TODO: Why is this necessary ?
        //invalidate();

        cache.tint(color);
        cache.setPosition(getX(), getY() - distanceFieldController.getPaddingAdvanceY() * fontSize);

        batch.setShader(distanceFieldController.getShader());
        distanceFieldController.getShader().setUniformf("smoothing", 0.25f / (distanceFieldController.getSpread() * fontSize * distanceFieldController.getRatio()));
        cache.draw(batch);
        batch.setShader(null);
    }

    public float getPrefWidth() {
        if (wrap) return 0;
        if (sizeInvalid) scaleAndComputeSize();
        float width = bounds.width;
        Drawable background = style.background;
        if (background != null) width += background.getLeftWidth() + background.getRightWidth();
        return width;
    }

    public float getPrefHeight() {
        if (sizeInvalid) scaleAndComputeSize();
        float height = bounds.height - style.font.getDescent() * 2;
        Drawable background = style.background;
        if (background != null) height += background.getTopHeight() + background.getBottomHeight();
        return height;
    }

    public BitmapFont.TextBounds getTextBounds() {
        if (sizeInvalid) scaleAndComputeSize();
        return bounds;
    }

    /**
     * If false, the text will only wrap where it contains newlines (\n). The preferred fontSize of the label will be the text bounds.
     * If true, the text will word wrap using the width of the label. The preferred width of the label will be 0, it is expected
     * that the something external will set the width of the label. Default is false.
     * <p/>
     * When wrap is enabled, the label's preferred height depends on the width of the label. In some cases the parent of the label
     * will need to layout twice: once to set the width of the label and a second time to adjust to the label's new preferred
     * height.
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
        invalidateHierarchy();
    }

    /**
     * @param alignment Aligns each line of text horizontally and all the text vertically.
     * @see Align
     */
    public void setAlignment(int alignment) {
        setAlignment(alignment, alignment);
    }

    /**
     * @param labelAlign Aligns all the text with the label widget.
     * @param lineAlign  Aligns each line of text (left, right, or center).
     * @see Align
     */
    public void setAlignment(int labelAlign, int lineAlign) {
        this.labelAlign = labelAlign;

        if ((lineAlign & Align.left) != 0)
            this.lineAlign = BitmapFont.HAlignment.LEFT;
        else if ((lineAlign & Align.right) != 0)
            this.lineAlign = BitmapFont.HAlignment.RIGHT;
        else
            this.lineAlign = BitmapFont.HAlignment.CENTER;

        invalidate();
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        invalidateHierarchy();
    }

    public float getFontSize() {
        return fontSize;
    }

    /**
     * When true the text will be truncated with an ellipse if it does not fit within the width of the label. Default is false.
     */
    public void setEllipse(boolean ellipse) {
        this.ellipse = ellipse;
    }

    /**
     * Allows subclasses to access the cache in {@link #draw(Batch, float)}.
     */
    protected BitmapFontCache getBitmapFontCache() {
        return cache;
    }

    /**
     * The style for a label, see {@link Label}.
     *
     * @author Nathan Sweet - Modified by Daldegan
     */
    static public class LabelDistanceFieldStyle {
        public BitmapFont font;
        /**
         * Optional.
         */
        public Color fontColor;
        /**
         * Optional.
         */
        public Drawable background;

        public LabelDistanceFieldStyle() {
        }

        public LabelDistanceFieldStyle(BitmapFont font, Color fontColor) {
            this.font = font;
            this.fontColor = fontColor;
        }

        public LabelDistanceFieldStyle(LabelDistanceFieldStyle style) {
            this.font = style.font;
            if (style.fontColor != null)
                fontColor = new Color(style.fontColor);

            background = style.background;
        }
    }
}
