package br.com.animvs.engine.net.pacotes;

import java.nio.ByteBuffer;

/**
 * Cabecalho de pacotes<BR>
 * Deve oferecer os dados necess�rios para a constru��o dos pacotes processados
 * pelo servidor / cliente
 * 
 * @author Daldegan
 * 
 */
public abstract class PacoteHeader {

	private int tamanho;

	/**
	 * Retorna o tamanho do pacote (quantos bytes formam o buffer do mesmo)
	 */
	public final int getTamanho() {
		return tamanho;

	}

	public PacoteHeader(ByteBuffer data) {
		eventoConstrucao(data);
	}

	/**
	 * Deve converter o buffer oferecido como par�metros nos dados que comp�e o
	 * cabe�alho dos pacotes processados pelo servidor / cliente
	 * 
	 * @param data
	 *            Buffer com os dados necess�rio para a constru��o do cabe�alho
	 *            do pacote
	 */
	protected abstract void eventoConstrucao(ByteBuffer data);
}
