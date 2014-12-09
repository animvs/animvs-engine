package br.com.animvs.engine.net.log;

import com.badlogic.gdx.Gdx;

public final class GdxLogger extends NetLogger {

	// private static class Erro {
	// public String mensagem;
	// public String stack;
	//
	// Erro(String mensagem, String stack) {
	// this.mensagem = mensagem;
	// this.stack = stack;
	// }
	// }

	@Override
	protected void eventoLoga(String titulo, String mensagem) {
		Gdx.app.log(titulo, mensagem);

	}

	@Override
	protected void eventoLoga(String titulo, Throwable e) {
		// Erro erro = getErro(e);
		//
		// Gdx.app.error("ERRO", erro.mensagem);
		// Gdx.app.error(titulo, erro.stack);
		Gdx.app.error("ERRO", e.getMessage());
	}

	// private final Erro getErro(Throwable e) {
	// StringWriter sw = new StringWriter();
	// PrintWriter pw = new PrintWriter(sw);
	// e.printStackTrace(pw);
	// String stack = sw.toString(); // stack trace as a string
	//
	// try {
	// sw.close();
	// pw.close();
	// } catch (IOException e1) {
	// }
	//
	// return new Erro(e.toString(), stack);
	// }
}
