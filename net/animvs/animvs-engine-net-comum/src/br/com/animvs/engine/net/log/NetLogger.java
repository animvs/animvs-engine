package br.com.animvs.engine.net.log;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Grava logs gerados na camada de comunicação
 * @author Daldegan
 *
 */
public abstract class NetLogger {
	public final void loga(String titulo, String mensagem) {
		eventoLoga(titulo, mensagem);
	}
	
	public final void loga(String titulo, byte[] bytes) {
		String bufferStr = "";
		
		for (int i = 0; i < bytes.length; i++) {
			if (i > 0)
				bufferStr += "-";
			bufferStr += bytes[i];
		}
		eventoLoga(titulo + " Bytes", bufferStr);
		eventoLoga(titulo + " String", new String(bytes));
	}
	
	public final void loga(String titulo, ByteBuffer buffer, int tamanho) {
		String bufferStr = "";
		
		byte[] bytes = new byte[tamanho];
		
		for (int i = 0; i < tamanho; i++) {
			if (i > 0)
				bufferStr += "-";
			bytes[i] = buffer.get(i);
			bufferStr += bytes[i];
		}
		
		eventoLoga(titulo + " Bytes", bufferStr);
		eventoLoga(titulo + " String", new String(bytes));
	}

	public final void loga(String titulo, Throwable e) {
		eventoLoga(titulo, e);
	}

	protected abstract void eventoLoga(String titulo, String mensagem);

	protected abstract void eventoLoga(String titulo, Throwable e);
}
