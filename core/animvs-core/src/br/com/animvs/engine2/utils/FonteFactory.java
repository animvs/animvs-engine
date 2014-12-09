package br.com.animvs.engine2.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class FonteFactory {
	private static int[] TAMANHOS_NATIVOS = new int[] { 4, 8, 12, 16, 20, 24, 28, 32 };
	
	private static final int getMaiorTamanhoPossivel() {
		int maiorTamanhoPossivel = 0;

		for (int i = 0; i < TAMANHOS_NATIVOS.length; i++)
			if (maiorTamanhoPossivel < TAMANHOS_NATIVOS[i])
				maiorTamanhoPossivel = TAMANHOS_NATIVOS[i];

		return maiorTamanhoPossivel;
	}

	public static final String preparaFonteNativa(AssetManager assetManager, int tamanhoFonte, String caminhoPastaFontes) {
		int tamanho = computaTamanhoProximo(tamanhoFonte);

		Gdx.app.debug("FONTE", "Fonte nativa criada com o tamanho: " + tamanho);

		// String caminho = "data/core/fonts/native/font" + tamanho + ".fnt";
		String caminho = caminhoPastaFontes + tamanho + ".fnt";

		// return new Fonte(new BitmapFont(Gdx.files.internal(caminho)));
		assetManager.load(caminho, BitmapFont.class);

		return caminho;
	}

	public static final int getSegundoMaiorTamanho(int tamanho) {
		int indice = getIndiceFonteMaisProxima(tamanho);

		if (indice > 0)
			indice -= 1;

		return TAMANHOS_NATIVOS[indice];
	}

	private static int computaTamanhoProximo(int tamanho) {
		// Checa se existe algum tamanho maior que o necess�rios:
		int maiorTamanhoPossivel = getMaiorTamanhoPossivel();

		if (tamanho > maiorTamanhoPossivel)
			return maiorTamanhoPossivel;

		return TAMANHOS_NATIVOS[getIndiceFonteMaisProxima(tamanho)];
	}

	private static final int getIndiceFonteMaisProxima(int tamanho) {
		int indice = -1;
		int menorDiferenca = Integer.MAX_VALUE;

		for (int i = 0; i < TAMANHOS_NATIVOS.length; i++) {
			int diferencaAtual = Math.abs(TAMANHOS_NATIVOS[i] - tamanho);

			if (diferencaAtual < menorDiferenca) {
				menorDiferenca = diferencaAtual;
				indice = i;
			}
		}

		return indice;
	}
}
