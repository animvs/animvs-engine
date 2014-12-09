package br.com.animvs.engine.net.log;

public final class StdoutLogger extends NetLogger {

	@Override
	protected final void eventoLoga(String titulo, String mensagem) {
		System.out.println(titulo +": " + mensagem);
	}

	@Override
	protected final void eventoLoga(String titulo, Throwable e) {
		System.out.println("ERRO: " + titulo);
		e.printStackTrace();
	}

}
