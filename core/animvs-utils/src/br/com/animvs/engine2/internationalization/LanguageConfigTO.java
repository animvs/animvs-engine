package br.com.animvs.engine2.internationalization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public final class LanguageConfigTO {
	private Array<LanguageConfigItemTO> languages;

	public static class LanguageConfigItemTO {
		private String languageAbbreviation;
		private String fontFilePath;

		public LanguageConfigItemTO() {
		}

		public LanguageConfigItemTO(String languageAbbreviation, String fontFilePath) {
			this.languageAbbreviation = languageAbbreviation;
			this.fontFilePath = fontFilePath;
		}

		public String getLanguageAbbreviation() {
			return languageAbbreviation;
		}

		public void setLanguageAbbreviation(String languageAbbreviation) {
			this.languageAbbreviation = languageAbbreviation;
		}

		public String getFontFilePath() {
			return fontFilePath;
		}

		public void setFontFilePath(String fontFilePath) {
			this.fontFilePath = fontFilePath;
		}
	}

	public Array<LanguageConfigItemTO> getLanguages() {
		return languages;
	}

	public void setLanguages(Array<LanguageConfigItemTO> languages) {
		this.languages = languages;
	}

	public static void TEST_serialization(){
		LanguageConfigTO config = new LanguageConfigTO();
		
		config.setLanguages(new Array<LanguageConfigTO.LanguageConfigItemTO>());
		
		config.setLanguages(new Array<LanguageConfigTO.LanguageConfigItemTO>());
		config.getLanguages().add(new LanguageConfigItemTO("vi", "UVNAnhHai_R.TTF"));
		
		config.setLanguages(new Array<LanguageConfigTO.LanguageConfigItemTO>());
		config.getLanguages().add(new LanguageConfigItemTO("en", "Quando-Regular.ttf"));
		
		String content = new Json().prettyPrint(config);
		
		Gdx.files.absolute("C:/animvs/test/assets/data/lang/lang-config.txt").writeString(content, false, "UTF-8");
	}
}
