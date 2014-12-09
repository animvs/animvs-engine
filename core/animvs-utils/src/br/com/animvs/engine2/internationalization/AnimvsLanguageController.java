package br.com.animvs.engine2.internationalization;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public final class AnimvsLanguageController {
    private Json json;

    private String languagesDir;

    private String lang;
    private Language currentLang;
    private LanguageChangedListener langChangedListener;

    private LanguageConfigTO config;

    public String getCurrentLanguage() {
        return lang;
    }

    public Language getLang() {
        return currentLang;
    }

    public LanguageConfigTO getConfig() {
        return config;
    }

    public static abstract class LanguageChangedListener {
        public abstract void eventLanguageChanged(String newLang);
    }

    public void setLangChangedListener(LanguageChangedListener languageChangedListener) {
        this.langChangedListener = languageChangedListener;
    }

    public AnimvsLanguageController(String defaultLanguageAbbreviation, String languagesDir) {
        this.languagesDir = languagesDir;
        json = new Json();

        /*if (languagesDir.charAt(languagesDir.length() - 1) == '/')
            languagesDir = languagesDir.substring(0, languagesDir.length() - 1);*/

        String langConfigDir = languagesDir + "/" + "lang-config.txt";

        if (!Gdx.files.internal(langConfigDir).exists())
            throw new RuntimeException("Language config file not found: " + langConfigDir);

        /*if (Gdx.app.getType() == ApplicationType.Desktop) {
            LanguageConfigTO.serializationTest();
            LanguageTO.serializationTest();
        }*/

        config = json.fromJson(LanguageConfigTO.class, Gdx.files.internal(langConfigDir));

        setLanguage(defaultLanguageAbbreviation);
    }

    public void setLanguage(String newLangAbbreviation) {
        if (lang != null && lang.equals(newLangAbbreviation))
            return; // no changes

        loadLanguage(newLangAbbreviation);
        lang = newLangAbbreviation;

        if (langChangedListener != null)
            langChangedListener.eventLanguageChanged(lang);
    }

    private void loadLanguage(String langAbbreviation) {
        for (int i = 0; i < config.getLanguages().size; i++) {
            if (config.getLanguages().get(i).getLanguageAbbreviation().equals(langAbbreviation)) {
                String languageDir = languagesDir + "/" + config.getLanguages().get(i).getLanguageAbbreviation() + ".txt";

                if (!Gdx.files.internal(languageDir).exists())
                    throw new RuntimeException("Language file not found: " + languageDir);

                LanguageTO currentLangTO = json.fromJson(LanguageTO.class, Gdx.files.internal(languageDir));
                currentLang = new Language(currentLangTO, config.getLanguages().get(i).getLanguageAbbreviation());
                return;
            }
        }

        throw new RuntimeException("Language not registered on lang-config.txt: " + langAbbreviation);
    }
}
