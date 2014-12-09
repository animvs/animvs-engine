package br.com.animvs.engine.net.cliente.json;

import java.nio.ByteBuffer;

import br.com.animvs.engine.net.pacotes.PacoteHeader;

public final class PacoteHeaderJSON extends PacoteHeader {

	private String pacoteNome;

	public final String getPacoteNome() {
		return pacoteNome;
	}

	public PacoteHeaderJSON(ByteBuffer data) {
		super(data);
	}

	@Override
	protected final void eventoConstrucao(ByteBuffer data) {
		pacoteNome = new String(data.array());
	}

}
