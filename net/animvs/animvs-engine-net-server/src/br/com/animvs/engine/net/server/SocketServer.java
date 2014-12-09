package br.com.animvs.engine.net.server;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.log.NetLogger;
import br.com.animvs.engine.net.pacotes.PacoteManager;

import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Array;

/**
 * Servidor sockets utilizado para ouvir e estabelecer conexões
 * 
 * @author Daldegan
 * 
 * @param <TOwnerInfo>
 *            Tipo que especifica os dados utilizados para identificar as
 *            conexões estabelecidas
 * @param <TPacoteManager>
 *            Tipo que especifica o gerenciador de pacotes utilizado para
 *            processar (receber e enviar) dados
 * @param <TSocketCliente>
 *            Tipo que especifica os clientes sockets utilizados
 */
public abstract class SocketServer<TOwnerInfo extends SocketOwnerInfo, TPacoteManager extends PacoteManager<TOwnerInfo>, TSocketCliente extends SocketCliente<TOwnerInfo>> {

	private TPacoteManager pacoteManager;

	private Array<TSocketCliente> clientes;

	private NetLogger logger;
	private SocketServerThread<TOwnerInfo, TPacoteManager, TSocketCliente> thread;

	NetLogger getLogger() {
		return logger;
	}

	public final Array<TSocketCliente> getClientes() {
		return clientes;
	}

	// TPacoteManager getPacoteManager() {
	// return pacoteManager;
	// }

	public SocketServer(int port, ServerSocketHints hints, TPacoteManager pacoteManager, NetLogger logger) {
		this.pacoteManager = pacoteManager;
		this.logger = logger;

		clientes = new Array<TSocketCliente>();

		logger.loga("NET SERVER", "Inicializando servidor ouvindo na porta " + port);

		thread = new SocketServerThread<TOwnerInfo, TPacoteManager, TSocketCliente>((SocketServer<TOwnerInfo, PacoteManager<TOwnerInfo>, TSocketCliente>) this, Protocol.TCP, port, pacoteManager, hints, clientes);
	}

	/**
	 * Atualiza todas as conexões: recebe e envia dados via Sockets
	 */
	public final void update() {
		thread.update();
	}

	/**
	 * Encerra o servidor sockets e fecha todas as conexões
	 */
	public void dispose() {
		thread.dispose();
	}

	/**
	 * Deve implementar a criação de um cliente (conexão estabelecida com
	 * o servidor)
	 * 
	 * @param clienteGDX
	 *            Cliente socket nativo do libgdx
	 * @param pacoteManager
	 *            Gerenciador de pacotes utilizado para processar pacotes
	 *            recebidos
	 * @return
	 */
	protected abstract TSocketCliente criaCliente(Socket clienteGDX);

	/**
	 * Deve implementar o retorno das informações que identificam o cliente.
	 */
	protected abstract TOwnerInfo criaOwnerInfo(String ip);

	protected void conexaoEstabelecida(TSocketCliente cliente) {
		logger.loga("NET SERVER", "Conexão estabelecida");
	}
}
