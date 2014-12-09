/*package br.com.animvs.engine.net.pacotes;

import java.nio.ByteBuffer;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;

/**
 * 
 * @author Daldegan
 * 
 */
/*public abstract class PacoteSocket<TO extends SocketMensagemTO, TOwnerInfo extends SocketOwnerInfo, TPacoteHeader extends PacoteHeader> {

	// private SocketCliente<TOwnerInfo, TPacoteHeader, TPacoteManager>
	// socketCliente;

//	private TPacoteHeader pacoteHeader;

	// public final SocketCliente<TOwnerInfo, TPacoteHeader, TPacoteManager>
	// getCliente() {
	// return socketCliente;
	// }

//	final TPacoteHeader getPacoteHeader() {
//		return pacoteHeader;
//	}
	
	private short pacoteID;
	private ByteBuffer buffer;

	public PacoteSocket(short pacoteID, ByteBuffer buffer) {
		// this.socketCliente = socketCliente;
		this.pacoteID = pacoteID;
		this.buffer = buffer;
	}

	public abstract TO eventoConstroiMensagemTO(short pacoteID, ByteBuffer data);

	protected abstract ByteBuffer[] eventoConstroiEnvio(TO to, SocketCliente<TOwnerInfo> clienteOwner);

	protected abstract void eventoRecebimento(TO to, SocketCliente<TOwnerInfo> clienteOwner);
}*/
