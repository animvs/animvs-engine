package br.com.animvs.engine.net.cliente;

import java.io.IOException;
import java.nio.ByteBuffer;

import br.com.animvs.engine.net.log.NetLogger;
import br.com.animvs.engine.net.pacotes.PacoteManager;
import br.com.animvs.engine.net.pacotes.SocketMensagemTO;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Disposable;

/**
 * Cliente socket criado durante o estabelecimento de uma conex�o com o servidor
 * 
 * @author Daldegan
 * 
 * @param <TOwnerInfo>
 *            Tipo que especifica os dados utilizados para identificar as
 *            conexões estabelecidas
 * @param <TPacoteManager>
 *            Tipo que especifica o gerenciador de pacotes utilizado para
 *            processar (receber e enviar) dados
 */
public abstract class SocketCliente<TOwnerInfo extends SocketOwnerInfo> implements Disposable {

	private TOwnerInfo ownerInfo;
	private Socket clienteGDX;

	private byte[] bufferLeitura;

	private NetLogger logger;

	private byte[] recebimentoBuffer;
	private int tamanhoRecebimentoBuffer;

	private boolean debug;

	public final boolean getDebug() {
		return debug;
	}

	public final void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Retorna o cliente socket nativo do libgdx utilizado na conexão
	 */
	protected final Socket getClienteGDX() {
		return clienteGDX;
	}

	/**
	 * Retorna os dados que identificam a conexão estabelecida por este cliente
	 */
	public final TOwnerInfo getOwnerInfo() {
		return ownerInfo;
	}

	/**
	 * Retorna se a conexão deste cliente está ativa e ainda é válida
	 */
	public final boolean getConectado() {
		return clienteGDX.isConnected();
	}

	public SocketCliente(Socket socket, TOwnerInfo ownerInfo, int tamanhoBufferLeitura, int tamanhoBufferProcessamento, NetLogger logger) {
		if (!socket.isConnected())
			throw new RuntimeException("O cliente socket GDX deve estar conectado antes de instanciar o SocketCliente");

		this.clienteGDX = socket;
		this.ownerInfo = ownerInfo;
		this.logger = logger;

		recebimentoBuffer = new byte[tamanhoBufferLeitura];
		bufferLeitura = new byte[tamanhoBufferLeitura];
	}

	/**
	 * Executa atividades necessárias para computar o recebimento de dados do
	 * servidor<BR>
	 * <BR>
	 * <B>Deve ser</B> chamado no loop principal
	 * 
	 * @throws IOException
	 *             caso ocorra algum erro durante a atualização deste cliente
	 */
	public final <TPacoteManager extends PacoteManager<TOwnerInfo>> void update(TPacoteManager pacoteManager) throws IOException {

		if (clienteGDX.getInputStream().available() < 2)
			return;

		int totalBytesLidos = clienteGDX.getInputStream().read(bufferLeitura);

		if (totalBytesLidos == -1)
			return; // Nada recebido.

		for (int i = 0; i < totalBytesLidos; i++) {
			recebimentoBuffer[tamanhoRecebimentoBuffer + i] = bufferLeitura[i];
		}

		tamanhoRecebimentoBuffer += totalBytesLidos;

		while (tamanhoRecebimentoBuffer > 2) {
			short tamanhoPacote = byte2short(recebimentoBuffer[0], recebimentoBuffer[1]);

			if (tamanhoRecebimentoBuffer < tamanhoPacote)
				break;
			
			// logger.loga("NET", "Recebimento de pacote: " + tamanhoPacote + " Total Lido: " + totalBytesLidos + " Tamanho Buffer Recebimento: " + tamanhoRecebimentoBuffer);

			short pacoteID = byte2short(recebimentoBuffer[2], recebimentoBuffer[3]);

			// Tamanho do pacote -4, pois os dados do cabeçalho já foram lidos e
			// não serão mais necessários:
			byte[] pacoteBuffer = new byte[tamanhoPacote - 4];
			for (int i = 4; i < tamanhoPacote; i++)
				pacoteBuffer[i - 4] = recebimentoBuffer[i];

			// Copia o conteudo restante do buffer de recebimento para seu
			// inicio:
			tamanhoRecebimentoBuffer -= tamanhoPacote;
			for (int i = 0; i < tamanhoRecebimentoBuffer; i++)
				recebimentoBuffer[i] = recebimentoBuffer[tamanhoPacote + i];

			// logger.loga("NET",
			// "Preparando construção de pacote - Conteúdo: ");
			// logger.loga("NET", pacoteBuffer);

			pacoteManager.recebe(pacoteManager.getClasseMensagemTOPorPacoteID(pacoteID), pacoteBuffer, this, pacoteID, tamanhoPacote);
		}
	}

	public final <TMensagemTO extends SocketMensagemTO, TPacoteManager extends PacoteManager<TOwnerInfo>> void envia(TMensagemTO mensagemTO, TPacoteManager pacoteManager) {

		short pacoteID = pacoteManager.getPacoteIDPorClasseMensagem(mensagemTO.getClass());

		if (pacoteID == 0)
			throw new RuntimeException("Tipo de pacote desconhecido: " + mensagemTO.getClass());

		pacoteManager.envia(pacoteID, mensagemTO, this);
	}

	public final void enviaBytes(ByteBuffer buffer, int tamanho) {
		try {
			if (debug)
				logger.loga("NET CLIENT", "Enviando " + tamanho + " bytes");

			// int total = buffer.position();
			// buffer.position(0);
			//
			// for (int i = 0; i < total; i++) {
			// logger.loga("XX " + i, String.valueOf(buffer.getChar()));
			// }

			byte[] bufferArray = new byte[tamanho];
			buffer.get(bufferArray, 0, tamanho);

			// String conteudoBufferStr = "";
			// for (int i = 0; i < tamanho; i++) {
			// if (i > 0)
			// conteudoBufferStr += "-";
			// conteudoBufferStr += bufferArray[i];
			// }

			if (debug)
				logger.loga("ENVIO", bufferArray);

			getClienteGDX().getOutputStream().write(bufferArray, 0, tamanho);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao enviar bytes via sockets", e);
		}
	}

	/**
	 * Encerra a conex�o
	 */
	@Override
	public void dispose() {
		clienteGDX.dispose();
	}

	// private final short byte2short(byte byte2, byte byte1) {
	// return (short) ((byte1 << 8) | (byte2));
	// }

	private final short byte2short(byte byte1, byte byte2) {
		short value = (short) ((byte1 & 0xFF) | byte2 << 8);

		return value;
	}

	protected void conexaoEstabelecida() {
		logger.loga("NET CLIENT", "Conexão estabelecida");
	}
}
