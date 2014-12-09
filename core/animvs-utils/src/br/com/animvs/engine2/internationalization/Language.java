package br.com.animvs.engine2.internationalization;

import com.badlogic.gdx.utils.Array;

public final class Language {
    private Array<LanguageItemTO> sessions;
    private String langAbbreviation;

    public Array<LanguageItemTO> getSessions() {
        return sessions;
    }

    public String getLangAbbreviation() {
        return langAbbreviation;
    }

    public Language(LanguageTO languageTO, String languageAbbreviation) {
        this.langAbbreviation = languageAbbreviation;

        sessions = languageTO.getItems();
    }

    public final String getValor(String caminho) {
        String[] atributos = caminho.split("\\.");

        Array<LanguageItemTO> items = sessions;

        LanguageItemTO lastItem;

        for (int i = 0; i < atributos.length - 1; i++) {
            lastItem = getItem(items, atributos[i]);

            if (lastItem == null)
                throw new RuntimeException("Value not found when requiring language value: " + caminho);

            items = lastItem.getItems();
        }

        LanguageItemTO itemTO = getItem(items, atributos[atributos.length - 1]);

        if (itemTO == null)
            throw new RuntimeException("Language value not found. LANG: '" + langAbbreviation + "' PATH: '" + caminho + "'");

        return itemTO.getValue();
    }

    private LanguageItemTO getItem(Array<LanguageItemTO> items, String name) {
        if (name == null)
            throw new RuntimeException("The parameter 'name' must be != null");

        for (int i = 0; i < items.size; i++) {
            if (items.get(i).getName().equals(name))
                return items.get(i);
        }

        return null;
    }
}
