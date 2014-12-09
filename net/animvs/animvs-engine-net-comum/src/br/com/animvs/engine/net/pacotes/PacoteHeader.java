package br.com.animvs.engine.net.pacotes;

import java.nio.ByteBuffer;

/**
 * Cabecalho de pacotes<BR>
 * Deve oferecer os dados necessários para a construção dos pacotes processados
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
	 * Deve converter o buffer oferecido como parâmetros nos dados que compõe o
	 * cabeçalho dos pacotes processados pelo servidor / cliente
	 * 
	 * @param data
	 *            Buffer com os dados necessário para a construção do cabeçalho
	 *            do pacote
	 */
	protected abstract void eventoConstrucao(ByteBuffer data);
}
