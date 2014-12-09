package br.com.animvs.ui;

/**
 * Exception m�nima para UI's
 * @author Daldegan
 *
 */
public class AnimvsUIException extends RuntimeException {

	public AnimvsUIException() {
		super();
	}

	// public AnimvsUIException(String mensagem, Throwable causa, boolean
	// enableSuppression, boolean writableStackTrace) {
	// super(mensagem, causa, enableSuppression, writableStackTrace);
	// }

	public AnimvsUIException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}

	public AnimvsUIException(String mensagem) {
		super(mensagem);
	}

	public AnimvsUIException(Throwable causa) {
		super(causa);
	}

	

}
