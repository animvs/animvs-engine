package br.com.animvs.engine.net.server.json;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.cliente.json.PacoteManagerJSON;
import br.com.animvs.engine.net.cliente.json.SocketClienteJSON;
import br.com.animvs.engine.net.log.NetLogger;
import br.com.animvs.engine.net.server.SocketServer;

import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

public abstract class SocketServerJSON<TSocketOwnerInfo extends SocketOwnerInfo> extends SocketServer<TSocketOwnerInfo, PacoteManagerJSON<TSocketOwnerInfo>, SocketCliente<TSocketOwnerInfo>> {

	public SocketServerJSON(int port, ServerSocketHints hints, PacoteManagerJSON<TSocketOwnerInfo> pacoteManager, NetLogger logger) {
		super(port, hints, pacoteManager, logger);
	}

	@Override
	protected SocketCliente<TSocketOwnerInfo> criaCliente(Socket clienteGDX) {
		// String enderecoRemoto = clienteGDX.getRemoteAddress();

		return new SocketClienteJSON<TSocketOwnerInfo>(clienteGDX, criaOwnerInfo(clienteGDX.getRemoteAddress()), 2048, 4096);
	}
}
