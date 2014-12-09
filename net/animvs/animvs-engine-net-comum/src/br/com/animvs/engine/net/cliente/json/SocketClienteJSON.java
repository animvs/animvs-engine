package br.com.animvs.engine.net.cliente.json;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.log.GdxLogger;

import com.badlogic.gdx.net.Socket;

public class SocketClienteJSON<TSocketOwnerInfo extends SocketOwnerInfo> extends SocketCliente<TSocketOwnerInfo> {

	public SocketClienteJSON(Socket socket, TSocketOwnerInfo ownerInfo, int tamanhoBufferLeitura, int tamanhoBufferProcessamento) {
		super(socket, ownerInfo, tamanhoBufferLeitura, tamanhoBufferProcessamento, new GdxLogger());
	}
}
