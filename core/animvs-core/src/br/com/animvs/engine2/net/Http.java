package br.com.animvs.engine2.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Array;

public abstract class Http {

	private Http() {
	}

	public static final void enviaRequest(String url) {
		HttpRequest request = new HttpRequest("GET");
		request.setUrl(url);

		Gdx.net.sendHttpRequest(request, new ResponseListener(request));
	}

	public static final void enviaRequest(String url, String parametro, String valor) {
		enviaRequest(url, parametro, valor, null);
	}

	public static final void enviaRequest(String url, String parametro, String valor, HttpResponseListener responseListener) {
		if (parametro == null || valor == null)
			throw new RuntimeException("Os parâmetros 'parametro' e 'valor' não podem ser NULL");

		HttpRequest request = new HttpRequest("GET");
		request.setUrl(url + "?" + parametro + "=" + valor);

		if (responseListener == null)
			new ResponseListener(request);

		Gdx.net.sendHttpRequest(request, responseListener);
	}

	public static final void enviaRequest(String urlPura, String[] urlParametrosNomes, String[] urlParametrosConteudo) {
		enviaRequest(urlPura, urlParametrosNomes, urlParametrosConteudo, null);
	}

	public static final void enviaRequest(String urlPura, String[] urlParametrosNomes, String[] urlParametrosConteudo, HttpResponseListener responseListener) {
		String urlFinal;

		if (urlParametrosConteudo != null && urlParametrosNomes == null)
			throw new RuntimeException("O parâmetro urlParametrosNomes não pode ser null quando o parâmetro urlParametrosConteudo estiver preenchido");
		if (urlParametrosConteudo == null && urlParametrosNomes != null)
			throw new RuntimeException("O parâmetro urlParametrosConteudo não pode ser null quando o parâmetro urlParametrosNomes estiver preenchido");
		if (urlParametrosNomes.length != urlParametrosConteudo.length)
			throw new RuntimeException("O parâmetro urlParametrosNomes.lengh não pode ser !=  urlParametrosConteudo.lenght");

		urlFinal = urlPura;

		if (urlParametrosNomes != null) {
			for (int i = 0; i < urlParametrosConteudo.length; i++) {
				if (i == 0)
					urlFinal += "?";
				else
					urlFinal += "&";

				urlFinal += urlParametrosNomes[i] + "=" + urlParametrosConteudo[i];
			}
		}

		HttpRequest request = new HttpRequest("GET");
		request.setUrl(urlFinal);

		if (responseListener == null)
			responseListener = new ResponseListener(request);

		Gdx.net.sendHttpRequest(request, responseListener);
	}

	public static void enviaPost(String url, Array<String> headers, Array<String> values) {
		enviaPost(url, headers, values, null);
	}

	public static void enviaPost(String url, Array<String> headers, Array<String> values, HttpResponseListener responseListener) {
		Array<String> headersInterno = new Array<String>(headers);
		Array<String> valuesInterno = new Array<String>(values);

		if (valuesInterno != null && headersInterno == null)
			throw new RuntimeException("O parâmetro urlParametrosNomes não pode ser null quando o parâmetro urlParametrosConteudo estiver preenchido");
		if (valuesInterno == null && headersInterno != null)
			throw new RuntimeException("O parâmetro urlParametrosConteudo não pode ser null quando o parâmetro urlParametrosNomes estiver preenchido");
		if (headersInterno.size != valuesInterno.size)
			throw new RuntimeException("O parâmetro urlParametrosNomes.lengh não pode ser !=  urlParametrosConteudo.lenght");

		HttpRequest request = new HttpRequest("POST");
		request.setUrl(url);

		String content = "";
		boolean primeiro = true;

		for (int i = 0; i < headersInterno.size; i++) {
			// String valueFormatado = values[i].replace(" ", "$").replace("\n",
			// "<BR>");

			Gdx.app.log("HTTP POST", "Preparando envio - Header: '" + headersInterno.get(i) + "' Value: " + valuesInterno.get(i));

			if (!primeiro)
				content += "&";
			else
				primeiro = false;

			content += headersInterno.get(i) + "=" + valuesInterno.get(i);
		}

		Gdx.app.log("HTTP POST", "Enviando header - URL: " + request.getUrl() + " Content: " + content);

		if (responseListener == null)
			responseListener = new ResponseListener(request);

		request.setContent(content);

		Gdx.net.sendHttpRequest(request, responseListener);
	}

	public static void TEST(String url, String message, String stack) {
		HttpRequest request = new HttpRequest("POST");
		request.setUrl(url);

		request.setContent("message=" + message + "&" + "stack=" + stack);

		Gdx.net.sendHttpRequest(request, new ResponseListener(request));
	}
}