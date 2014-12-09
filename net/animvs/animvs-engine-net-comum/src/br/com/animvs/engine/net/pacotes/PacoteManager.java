package br.com.animvs.engine.net.pacotes;

import java.nio.ByteBuffer;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.log.NetLogger;

import com.badlogic.gdx.utils.BufferUtils;

/**
 * Processa dados recebidos via Socket. A implementação desta classe deve prover
 * mecanismos para identificar a complitude de um pacote (identificar em meio
 * aos bytes recebidos o fim de um pacote e o início de outro)
 * 
 * @author Daldegan
 * 
 */
public abstract class PacoteManager<TOwnerInfo extends SocketOwnerInfo> {

	/**
	 * Deve implementar a construução do pacote através dos dados recebidos via
	 * socket
	 * 
	 * @param data
	 *            bytes que compõe o pacote
	 * @return o pacote construído
	 */
	// public abstract <TO extends SocketMensagemTO, TPacote extends
	// PacoteSocket<TO, TOwnerInfo, TPacoteHeader>> TPacote
	// criaPacote(ByteBuffer data);
	
	/**
	 * Define o tamanho (em bytes) do buffer utilizado para a construção de pacotes<BR>
	 * PS: Deve ter pelo menos o tamanho do maior pacote passível de construção 
	 */
	protected int getTamanhoBufferConstrucaoPacotes(){
		return 4096;
	}

	private NetLogger logger;

	protected NetLogger getLogger() {
		return logger;
	}

	public PacoteManager(NetLogger logger) {
		this.logger = logger;
	}

	public final <TMensagemTO extends SocketMensagemTO, TRemetente extends SocketCliente<TOwnerInfo>> void recebe(Class<TMensagemTO> classMensagemTO, byte[] buffer, TRemetente clienteRemetente, short pacoteID, int tamanho) {
		// buffer.getShort(); // descarta pacote de tamanho
		// short pacoteID = buffer.getShort();

		TMensagemTO mensagemTO = constroiRecebimento(classMensagemTO, pacoteID, tamanho, buffer);

		eventoMensagemRecebida(pacoteID, mensagemTO, clienteRemetente);
	}

	public final void envia(short pacoteID, SocketMensagemTO mensagemTO, SocketCliente<TOwnerInfo> cliente) {
		ByteBuffer bufferPacote = BufferUtils.newByteBuffer(getTamanhoBufferConstrucaoPacotes());

		// O pacoteID deve ser lido antes do preparo para envio, onde o mesmo é
		// removido para economia de banda:
		// short pacotedID = mensagemTO.getPacoteID();

		bufferPacote.position(4); // Deixa espa�o para o cabecalho
		constroiEnvio(mensagemTO, bufferPacote);

		// bufferPacote.compact();

		short tamanho = (short) bufferPacote.position();

		// logger.loga("ENVIO DE PACOTE", "PacoteID: " + pacoteID);
		// logger.loga("ENVIO DE PACOTE", "Tamanho Pacote: " + tamanho);

		bufferPacote.putShort(2, pacoteID);
		bufferPacote.putShort(0, tamanho);
		bufferPacote.rewind();

		// 4 bytes adicionados ao tamanho devido ao cabeçalho:
		cliente.enviaBytes(bufferPacote, tamanho);
	}

	public abstract <TMensagemTO extends SocketMensagemTO> short getPacoteIDPorClasseMensagem(Class<TMensagemTO> classeMensagem);

	public abstract <TMensagemTO extends SocketMensagemTO> Class<TMensagemTO> getClasseMensagemTOPorPacoteID(short pacoteID);

	protected abstract <TMensagemTO extends SocketMensagemTO> TMensagemTO constroiRecebimento(Class<TMensagemTO> classMensagemTO, short pacoteID, int tamanho, byte[] buffer);

	protected abstract <TRemetente extends SocketCliente<TOwnerInfo>> void eventoMensagemRecebida(short pacoteID, SocketMensagemTO mensagemTO, TRemetente clienteRemetente);

	protected abstract <TMensagemTO extends SocketMensagemTO> void constroiEnvio(TMensagemTO mensagemTO, ByteBuffer bufferParaPreencher);
}
