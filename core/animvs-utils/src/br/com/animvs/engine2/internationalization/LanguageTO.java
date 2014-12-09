package br.com.animvs.engine2.internationalization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

final class LanguageTO {
	private Array<LanguageItemTO> items;

	public Array<LanguageItemTO> getItems() {
		return items;
	}

	public LanguageTO() {
		items = new Array<LanguageItemTO>();
	}

	public static final void serializationTest() {
		Json json = new Json();

		LanguageTO languageTO = new LanguageTO();

		LanguageItemTO sessionMain = new LanguageItemTO();
		sessionMain.setItems(new Array<LanguageItemTO>());
		sessionMain.setName("main");

		sessionMain.getItems().add(new LanguageItemTO("play", "jogar"));
		sessionMain.getItems().add(new LanguageItemTO("gameOver", "Fim de Jogo"));
		sessionMain.getItems().add(new LanguageItemTO("gameWon", "Zerou"));

		LanguageItemTO sessionUI = new LanguageItemTO();
		sessionUI.setItems(new Array<LanguageItemTO>());
		sessionUI.setName("ui");

		sessionUI.getItems().add(new LanguageItemTO("start", "Iniciar"));
		sessionUI.getItems().add(new LanguageItemTO("pause", "Pausar"));

		sessionMain.getItems().add(sessionUI);

		languageTO.getItems().add(sessionMain);

		Gdx.files.absolute("C:/animvs/test/assets/data/lang/pt-br.txt").writeString(json.prettyPrint(languageTO), false);
	}

}
