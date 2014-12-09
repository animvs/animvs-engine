package br.com.animvs.engine.net.cliente.json;

import java.nio.ByteBuffer;

import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.log.NetLogger;
import br.com.animvs.engine.net.pacotes.PacoteManager;
import br.com.animvs.engine.net.pacotes.SocketMensagemTO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public abstract class PacoteManagerJSON<TOwnerInfo extends SocketOwnerInfo> extends PacoteManager<TOwnerInfo> {

	private Json serializador;

	public PacoteManagerJSON(NetLogger logger) {
		super(logger);
		serializador = new Json();
	}

	@Override
	protected final <TMensagemTO extends SocketMensagemTO> TMensagemTO constroiRecebimento(Class<TMensagemTO> classMensagemTO, short pacoteID, int tamanho, byte[] buffer) {
		String json = new String(buffer);
		return serializador.fromJson(classMensagemTO, json);
	}

	@Override
	protected final <TMensagemTO extends SocketMensagemTO> void constroiEnvio(TMensagemTO mensagemTO, ByteBuffer bufferParaPreencher) {
//		mensagemTO.preparaEnvio();
		String json = serializador.toJson(mensagemTO, mensagemTO.getClass());
		
		bufferParaPreencher.put(json.getBytes());
	}
}
