package br.com.animvs.engine2.graphics.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.GdxRuntimeException;

import javax.security.auth.login.Configuration;

import br.com.animvs.engine2.graphics.font.SmartFontGenerator.AnimvsBitmapFont;
import br.com.animvs.engine2.internationalization.AnimvsLanguageController;
import br.com.animvs.engine2.internationalization.AnimvsLanguageController.LanguageChangedListener;
import br.com.animvs.engine2.internationalization.Language;
import br.com.animvs.engine2.internationalization.LanguageItemTO;

public final class AnimvsSmartFontController {
    private Array<AnimvsSmartFontInfo> fontsInfo;

    private String languageDir;

    private SmartFontGenerator generator;
    private ArrayMap<String, AnimvsBitmapFont> fonts;
    private AnimvsLanguageController languageController;
    private String propertyName;
    private String versiontag;

    private int referenceWidthCache;

    private String additionalCharacters;

    private boolean forceGeneration;

    public BitmapFont getFont(String name) {
        if (!fonts.containsKey(name))
            throw new RuntimeException("Font not found: " + name);

        return fonts.get(name);
    }

    public ArrayMap<String, BitmapFont> getFonts() {
        ArrayMap<String, BitmapFont> fonts = new ArrayMap<String, BitmapFont>();

        for (int i = 0; i < this.fonts.size; i++)
            fonts.put(this.fonts.getKeyAt(i), this.fonts.getValueAt(i));

        return fonts;
    }

    public void TEST_addFont(BitmapFont font) {
        AnimvsBitmapFont a = generator.new AnimvsBitmapFont(font.getData(), font.getRegions(), true);
        fonts.put("en", a);
    }

    public AnimvsSmartFontController(AnimvsLanguageController languageController, String propertyName, String languageDir, Array<AnimvsSmartFontInfo> fontsInfo, String additionalCharacters, String versionTag, int referenceWidth) {
        this.propertyName = propertyName;
        this.additionalCharacters = additionalCharacters;
        this.versiontag = versionTag;

        this.referenceWidthCache = referenceWidth;

        if (languageController == null)
            throw new RuntimeException("The parameter 'languageController' must be != null");

        if (fontsInfo == null)
            throw new RuntimeException("The parameter 'fontInfo' must be != null");

        if (fontsInfo.size == 0)
            throw new RuntimeException("The parameter 'fontInfo' cannot be an empty ArrayMap");

        fonts = new ArrayMap<String, SmartFontGenerator.AnimvsBitmapFont>();

        this.languageController = languageController;
        this.languageDir = languageDir;
        this.fontsInfo = fontsInfo;

        languageController.setLangChangedListener(new LanguageChangedListener() {
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
        if (this.fontsInfo.size == 0) {
            Gdx.app.log("FONT", "No font info registered, skipping font creation ...");
            return;
        }

        if (generator == null)
            generator = new SmartFontGenerator(versiontag, referenceWidthCache, propertyName);
        else
            disposeFonts();

        for (int i = 0; i < fontsInfo.size; i++) {
            // int fontSize = (int) ( ((Gdx.graphics.getWidth() +
            // Gdx.graphics.getHeight()) / 2f) * (fontsInfo.get(i).getFontSize()
            // * 0.002f));
            int size = (int) (Gdx.graphics.getHeight() * (fontsInfo.get(i).getFontSize() * 0.002f));

            String path = retrieveFontFilePath(abbreviation);
            String caracteres = getUniqueCharacters(languageController.getLang());

            if (forceGeneration)
                generator.setForceGeneration(true);

            fonts.put(fontsInfo.get(i).getFontName(), generator.createFont(Gdx.files.internal(path), fontsInfo.get(i).getFontName(), size, caracteres, abbreviation, fontsInfo.get(i).getPageSize()));
        }

        if (forceGeneration) {
            forceGeneration = false;
            generator.setForceGeneration(false);
        }

        // JogoController.trataArquivosCache();
        generator.updateFontsProperties(languageController.getLang().getLangAbbreviation());
    }

    public void forceGeneration() {
        this.forceGeneration = true;
    }

    private void disposeFonts() {
        for (int i = 0; i < fonts.size; i++) {
            Gdx.app.debug("FONT", "Disposing font: " + fonts.getKeyAt(i));

            try {
                fonts.getValueAt(i).dispose();
            } catch (GdxRuntimeException e) {
                if (!e.getMessage().equals("Pixmap already disposed!"))
                    throw e;
            }
        }

        fonts.clear();
    }

    private final String retrieveFontFilePath(String abbreviation) {
        for (int i = 0; i < languageController.getConfig().getLanguages().size; i++) {
            if (languageController.getConfig().getLanguages().get(i).getLanguageAbbreviation().equals(abbreviation))
                return languageDir + "/" + languageController.getConfig().getLanguages().get(i).getFontFilePath();
        }

        throw new RuntimeException("Language not found on config file: " + abbreviation);
    }

    private final String getUniqueCharacters(Language lang) {

        StringBuilder caracteres = new StringBuilder();

        // Pega todos os caracteres do Json da lingua selecionada
        for (int i = 0; i < lang.getSessions().size; i++) {
            LanguageItemTO sessao = lang.getSessions().get(i);

            for (int j = 0; j < sessao.getItems().size; j++)
                parseUniqueCharacters(caracteres, sessao.getItems().get(j));
        }

        if (additionalCharacters != null)
            addDifferentCharacters(caracteres, additionalCharacters);

        return caracteres.toString().replace(" ", "").replace("{", "").replace("}", "").replace("\n", "");
    }

    private final void parseUniqueCharacters(StringBuilder charactersList, LanguageItemTO session) {
        if (session.getItems() == null)
            addDifferentCharacters(charactersList, session.getValue());
        else {
            for (int j = 0; j < session.getItems().size; j++) {
                parseUniqueCharacters(charactersList, session.getItems().get(j));
            }
        }
    }

    private final void addDifferentCharacters(StringBuilder listCaracteres, String frase) {
        for (int letra = 0; letra < frase.length(); letra++) {

            boolean existeNaLista = false;
            String palavra = listCaracteres.toString();

            for (int j = 0; j < palavra.length(); j++) {
                if (frase.charAt(letra) == palavra.charAt(j)) {
                    existeNaLista = true;
                    break;
                }
            }
            if (!existeNaLista) {
                listCaracteres.append(frase.charAt(letra));
            }

        }
    }
}
