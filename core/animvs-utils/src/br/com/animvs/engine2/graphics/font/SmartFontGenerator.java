package br.com.animvs.engine2.graphics.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.Page;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

class SmartFontGenerator {
    private static final String TAG = "FONT CACHE";
    private boolean forceGeneration;
    private String generatedFontDir;
    private int referenceScreenWidth;

    private String propertyName;

    // TODO figure out optimal page fontSize automatically
    // private int pageSize;

    // private Array<String> arquivosFonte;

    public SmartFontGenerator(int referenceScreenwidth, String propertyName) {
        if (propertyName == null)
            throw new RuntimeException("The parameter 'propertyName' must be != null");

        this.referenceScreenWidth = referenceScreenwidth;
        forceGeneration = false;
        generatedFontDir = "cache/fonts/";
        this.propertyName = propertyName;

        // referenceScreenWidth = 1280;
        // pageSize = 256; // fontSize of atlas pages for font pngs

        // arquivosFonte = new Array<String>();
    }

    public final class AnimvsBitmapFont extends BitmapFont {
        public AnimvsBitmapFont(FileHandle fontFile) {
            super(fontFile);
        }

        public AnimvsBitmapFont(BitmapFontData fontData, TextureRegion[] textureRegions, boolean integer) {
            super(fontData, textureRegions, integer);
        }

        @Override
        public void dispose() {
            for (int i = 0; i < getRegions().length; i++)
                getRegion(i).getTexture().dispose();
        }
    }

    /**
     * Will load font from file. If that fails, font will be generated and saved
     * to file.
     *
     * @param fontFile the actual font (.otf, .ttf)
     * @param fontName the name of the font, i.e. "arial-small", "arial-large",
     *                 "monospace-10" This will be used for creating the font file
     *                 names
     * @param fontSize fontSize of font when screen width equals referenceScreenWidth
     */
    public AnimvsBitmapFont createFont(FileHandle fontFile, String fontName, int fontSize, String characters, String language, int pageSize) {
        if (pageSize < 50)
            throw new RuntimeException("Page fontSize too small during font generation: " + pageSize);

        AnimvsBitmapFont font = null;
        // if fonts are already generated, just load from file
        Preferences fontPrefs = Gdx.app.getPreferences(propertyName);
        int displayWidth = fontPrefs.getInteger("display-width", 0);
        int displayHeight = fontPrefs.getInteger("display-height", 0);
        String lastUsedLanguage = fontPrefs.getString("lang");
        boolean loaded = false;
        if (displayWidth != Gdx.graphics.getWidth() || displayHeight != Gdx.graphics.getHeight()) {
            Gdx.app.debug(TAG, "Screen fontSize change detected, regenerating fonts");
        } else if (!lastUsedLanguage.equals(language))
            Gdx.app.debug(TAG, "Language change detected, regenerating fonts");
        else if (!forceGeneration) {
            try {
                // try to load from file
                Gdx.app.debug(TAG, "Loading generated font from file cache");
                font = new AnimvsBitmapFont(getFontFile(fontName + ".fnt"));
                setDefaultFilter(font);

                loaded = true;

                // arquivosFonte.add(fontFile.path());
            } catch (GdxRuntimeException e) {
                Gdx.app.error(TAG, e.getMessage());
                Gdx.app.debug(TAG, "Couldn't load pre-generated fonts. Will generate fonts.");
            }
        }
        if (!loaded || forceGeneration) {
            forceGeneration = false;
            // float width = Gdx.graphics.getWidth();
            // float ratio = width / referenceScreenWidth; // use 1920x1280 as
            // float baseSize = 28f; // for 28 sized fonts at baseline width
            // above

            Gdx.app.log(TAG, "Generating Font - Name: " + fontName + " Size: " + fontSize + " characters: " + characters);
            font = generateFontWriteFiles(fontName, fontFile, fontSize, pageSize, pageSize, characters);
            // arquivosFonte.add(fontFile.path());
        }
        return font;
    }

    public final void updateFontsProperties(String language) {
        Preferences fontPrefs = Gdx.app.getPreferences(propertyName);

        // store screen width for detecting screen fontSize change
        // on later startups, which will require font regeneration
        fontPrefs.putInteger("display-width", Gdx.graphics.getWidth());
        fontPrefs.putInteger("display-height", Gdx.graphics.getHeight());
        fontPrefs.putString("lang", language);
        fontPrefs.flush();
    }

    /**
     * Convenience method for generating a font, and then writing the fnt and
     * png files. Writing a generated font to files allows the possibility of
     * only generating the fonts when they are missing, otherwise loading from a
     * previously generated file.
     *
     * @param fontFile
     * @param fontSize
     */
    private AnimvsBitmapFont generateFontWriteFiles(String fontName, FileHandle fontFile, int fontSize, int pageWidth, int pageHeight, String characters) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        PixmapPacker packer = new PixmapPacker(pageWidth, pageHeight, Pixmap.Format.RGBA8888, 1, false);

        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.characters = characters;
        fontParameters.size = fontSize;
        fontParameters.minFilter = TextureFilter.Nearest;
        fontParameters.magFilter = TextureFilter.Linear;
        fontParameters.packer = packer;

        // FreeTypeFontGenerator.FreeTypeBitmapFontData fontData =
        // generator.generateData(fontSize, characters, false, packer);

        FreeTypeFontGenerator.FreeTypeBitmapFontData fontData = generator.generateData(fontParameters);
        Array<PixmapPacker.Page> pages = packer.getPages();
        TextureRegion[] texRegions = new TextureRegion[pages.size];
        for (int i = 0; i < pages.size; i++) {
            PixmapPacker.Page p = pages.get(i);
            Texture tex = new Texture(new PixmapTextureData(p.getPixmap(), p.getPixmap().getFormat(), false, false, true)) {
                @Override
                public void dispose() {
                    super.dispose();
                    getTextureData().consumePixmap().dispose();
                }
            };
            tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            texRegions[i] = new TextureRegion(tex);
        }
        AnimvsBitmapFont font = new AnimvsBitmapFont(fontData, texRegions, true);
        setDefaultFilter(font);

        saveFontToFile(font, fontSize, fontName, packer);
        generator.dispose();
        packer.dispose();
        return font;
    }

    private void saveFontToFile(BitmapFont font, int fontSize, String fontName, PixmapPacker packer) {
        FileHandle fontFile = getFontFile(fontName + ".fnt"); // .fnt path
        FileHandle pixmapDir = getFontFile(fontName); // png dir path
        BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);

        String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(), pixmapDir, fontName);
        Gdx.app.debug(TAG, String.format("Saving font [%s]: fontfile: %s, pixmapDir: %s\n", fontName, fontFile, pixmapDir));
        // here we must add the png dir to the page refs
        for (int i = 0; i < pageRefs.length; i++) {
            pageRefs[i] = fontName + "/" + pageRefs[i];
        }
        BitmapFontWriter.writeFont(font.getData(), pageRefs, fontFile, new BitmapFontWriter.FontInfo(fontName, fontSize), 1, 1);
    }

    private FileHandle getFontFile(String filename) {
        return Gdx.files.local(generatedFontDir + filename);
    }

    // GETTERS, SETTERS -----------------------

    public void setForceGeneration(boolean force) {
        forceGeneration = force;
    }

    public boolean getForceGeneration() {
        return forceGeneration;
    }

    /**
     * Set directory for storing generated fonts
     */
    public void setGeneratedFontDir(String dir) {
        generatedFontDir = dir;
    }

    /**
     * @see br.com.animvs.engine2.graphics.font.jrenner.smartfont.SmartFontGenerator#setGeneratedFontDir(String)
     */
    public String getGeneratedFontDir() {
        return generatedFontDir;
    }

    /**
     * Set the reference screen width for computing sizes. If reference width is
     * 1280, and screen width is 1280 Then the fontSize paramater will be
     * unaltered when creating a font. If the screen width is 720, the font fontSize
     * will by scaled down to (720 / 1280) of original fontSize.
     */
    public void setReferenceScreenWidth(int width) {
        referenceScreenWidth = width;
    }

    /**
     * @see br.com.animvs.engine2.graphics.font.jrenner.smartfont.SmartFontGenerator#setReferenceScreenWidth(int)
     */
    public int getReferenceScreenWidth() {
        return referenceScreenWidth;
    }

    // /**
    // * Set the width and height of the png files to which the fonts will be
    // * saved. In the future it would be nice for page fontSize to be automatically
    // * set to the optimal fontSize by the font generator. In the mean time it must
    // * be set manually.
    // */
    // public void setPageSize(int fontSize) {
    // pageSize = fontSize;
    // }

    // /** @see
    // br.com.animvs.engine2.graphics.font.jrenner.smartfont.SmartFontGenerator#setPageSize(int)
    // */
    // public int getPageSize() {
    // return pageSize;
    // }

    public static String[] writePixmapsCompressed(Array<Page> pages, FileHandle outputDir, String fileName) {
        Pixmap[] pix = new Pixmap[pages.size];
        for (int i = 0; i < pages.size; i++) {
            pix[i] = pages.get(i).getPixmap();
        }
        // Pixmap[] pagesArray = writePixmaps(pix, outputDir, fileName);

        if (pages == null || pages.size == 0)
            throw new IllegalArgumentException("no pixmaps supplied to BitmapFontWriter.write");

        String[] pageRefs = new String[pages.size];

        for (int i = 0; i < pages.size; i++) {
            String ref = pages.size == 1 ? (fileName + ".png") : (fileName + "_" + i + ".png");

            // the ref for this image
            pageRefs[i] = ref;

            // write the PNG in that directory
            PixmapIO.writeCIM(outputDir.child(ref), pages.get(i).getPixmap());
        }
        return pageRefs;
    }

    private void setDefaultFilter(AnimvsBitmapFont font){
        font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Linear);
    }

    // public final void disposeTodasFontes(AssetManager assetManager) {
    // for (int i = 0; i < arquivosFonte.fontSize; i++) {
    // if (assetManager.isLoaded(arquivosFonte.get(i)))
    // assetManager.unload(arquivosFonte.get(i));
    // }
    // }
}