package br.com.animvs.engine2.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import br.com.animvs.engine2.net.Http;
import br.com.animvs.engine2.resolver.AnalyticsResolver;
import br.com.animvs.engine2.saturn.InstallManager;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Array;

@Deprecated
public final class ExceptionSender {

	private static class Error {
		public String mensagem;
		public String stack;

		Error(String mensagem, String stack) {
			this.mensagem = mensagem;
			this.stack = stack;
		}
	}

	private AnalyticsResolver analytics;
	private String gameName;

	public void setAnalytics(AnalyticsResolver analytics) {
		this.analytics = analytics;
	}

	public ExceptionSender(String gameName) {
		if (gameName == null)
			throw new RuntimeException("The parameter 'gameName' must be != null");

		this.gameName = gameName;
	}

	public final void sendException(InstallManager instalacao, Thread thread, Throwable e) {

		// if (getPlatform().contains("DEVELOPER")) {
		// Gdx.app.error("EXCEPTION", e.getMessage(), e);
		// if (e instanceof RuntimeException)
		// throw (RuntimeException)e;
		// else
		// Gdx.app.exit();
		// }

		try {
			Error error = getError(e);

			if (analytics != null)
				analytics.analyticsException(thread, e);

			if (Gdx.app.getType() == ApplicationType.iOS)
				sendIOSError(instalacao, error);
			else
				sendError(instalacao,thread, error);
		} catch (RuntimeException ex) {
			Gdx.app.error("ANIMVS CONNECT", "Stack error send has failed", e);
		}

		// while (JogoController.getEnviandoErro()) {
		// try {
		// // Engine.loga("ANIMVS CONNECT", "Aguardando envio de erro");
		// Thread.sleep(25);
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		// }
		//
		// Engine.loga(e);
		// throw e;
	}

	private final void sendIOSError(InstallManager instalacao, Error erro) {
		String[] parametrosNomes = new String[4];
		parametrosNomes[0] = "saturn";
		parametrosNomes[1] = "message";
		parametrosNomes[2] = "stack";
		parametrosNomes[3] = "platform";

		String[] parametrosConteudo = new String[4];

		if (instalacao == null || instalacao.getInstallTO().getSerial() == null)
			parametrosConteudo[0] = "NULL";
		else
			parametrosConteudo[0] = instalacao.getInstallTO().getSerial();

		parametrosConteudo[1] = erro.mensagem;
		parametrosConteudo[2] = erro.stack.replace("\n", "<BR>").replace(" ", "$");
		parametrosConteudo[3] = getPlatform() + " " + instalacao.getInstallTO().getInstallFileVersion();

		// JogoController.setEnviandoErro(true);

		HttpResponseListener requestListener = new HttpResponseListener() {

			@Override
			public final void handleHttpResponse(HttpResponse httpResponse) {
				String[] resultado = httpResponse.getResultAsString().split(":");

				if (resultado[0].equals("OK")) {
					Gdx.app.log("ANIMVS CONNECT", "Error stack was sent sucessfully");
				} else
					Gdx.app.log("ANIVMS CONNECT", "Error stack send wasn't accepted: " + resultado[0] + " Mensagem: " + resultado[1]);

				// JogoController.setEnviandoErro(false);
			}

			@Override
			public final void failed(Throwable t) {
				Gdx.app.error("ANIMVS CONNECT", "Stack error send has failed", new RuntimeException("Error during stack send", t));

				// JogoController.setEnviandoErro(false);
			}

			@Override
			public final void cancelled() {
				Gdx.app.log("ANIMVS CONNECT", "Stack send post was cancelled");
			}
		};

		// Fix para iOS: enviando dados via GET (post envia
		// parametros NULL ?!)
		Http.enviaRequest("http://www.animvs.com.br/sys/error.php", parametrosNomes, parametrosConteudo, requestListener);
	}

	private final void sendError(InstallManager install, Thread thread, Error error) {
		Array<String> parametrosNomes = new Array<String>();
		parametrosNomes.add("saturn");
		parametrosNomes.add("message");
		parametrosNomes.add("stack");
		parametrosNomes.add("platform");

		Array<String> parametrosConteudo = new Array<String>();

		if (install == null || install.getInstallTO().getSerial() == null)
			parametrosConteudo.add("NULL");
		else
			parametrosConteudo.add(install.getInstallTO().getSerial());

		parametrosConteudo.add(error.mensagem);
		parametrosConteudo.add(error.stack.replace("\n", "<BR>"));
		parametrosConteudo.add(getPlatform() + " " + install.getInstallTO().getInstallFileVersion());

		// JogoController.setEnviandoErro(true);

		HttpResponseListener responseListener = new HttpResponseListener() {

			@Override
			public final void handleHttpResponse(HttpResponse httpResponse) {
				String[] resultado = httpResponse.getResultAsString().split(":");

				if (resultado[0].equals("OK")) {
					Gdx.app.log("ANIMVS CONNECT", "Error stack was sent sucessfully");
				} else
					Gdx.app.log("ANIVMS CONNECT", "Error stack send wasn' accepted: " + resultado[0] + " Mensagem: " + resultado[1]);

				// JogoController.setEnviandoErro(false);
			}

			@Override
			public final void failed(Throwable t) {
				Gdx.app.error("ANIMVS CONNECT", "Stack error send has failed", new RuntimeException("Error during stack send", t));

				// JogoController.setEnviandoErro(false);
			}

			@Override
			public final void cancelled() {
				Gdx.app.log("ANIMVS CONNECT", "Error stack send was called");
			}
		};

		Http.enviaPost("http://www.animvs.com.br/sys/error.php", parametrosNomes, parametrosConteudo, responseListener);
	}

	private final Error getError(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stack = sw.toString(); // stack trace as a string

		try {
			sw.close();
			pw.close();
		} catch (IOException e1) {
		}

		return new Error(e.toString(), stack);
	}

	private final String getPlatform() {
		if (Gdx.app.getType() == ApplicationType.Android)
			return gameName + "_ANDROID";
		else if (Gdx.app.getType() == ApplicationType.iOS)
			return gameName + "_IOS";
		else
			return gameName + "_DEVELOPER";
	}
}
