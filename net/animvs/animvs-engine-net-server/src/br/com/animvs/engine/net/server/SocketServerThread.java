package br.com.animvs.engine.net.server;

import java.io.IOException;
import java.net.SocketTimeoutException;

import br.com.animvs.engine.net.cliente.SocketCliente;
import br.com.animvs.engine.net.cliente.SocketOwnerInfo;
import br.com.animvs.engine.net.pacotes.PacoteManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

final class SocketServerThread<TSocketOwnerInfo extends SocketOwnerInfo, TPacoteManager extends PacoteManager<TSocketOwnerInfo>, TSocketCliente extends SocketCliente<TSocketOwnerInfo>> implements Disposable {

	private ServerSocket serverGDX;

	private SocketServer<TSocketOwnerInfo, PacoteManager<TSocketOwnerInfo>, TSocketCliente> server;

	private TPacoteManager pacoteManager;

	private Array<TSocketCliente> clientes;
	private Array<TSocketCliente> clientesRemover;

	private boolean deveFinalizar;
	private boolean disposed;

	public final Array<TSocketCliente> getClientes() {
		return clientes;
	}

	private class AcceptThread extends Thread {
		private Array<TSocketCliente> clientes;

		public AcceptThread(Array<TSocketCliente> clientes) {
			this.clientes = clientes;

			setName("AnimvsEngine Socket Accept Thread");
		}

		@Override
		public final void run() {

			SocketHints hints = new SocketHints();
			hints.connectTimeout = 3000;
			hints.keepAlive = true;
			hints.tcpNoDelay = true;
			hints.performancePrefLatency = 2;
			hints.performancePrefBandwidth = 1;
			hints.performancePrefConnectionTime = 0;
			hints.receiveBufferSize = 1024;
			hints.sendBufferSize = 1024;

			server.getLogger().loga("NET SERVER", "Aguardando conexões ...");

			while (!deveFinalizar) {
				try {
					Socket novoSocket = serverGDX.accept(hints);
					TSocketCliente novoCliente = server.criaCliente(novoSocket);
					server.conexaoEstabelecida(novoCliente);

					clientes.add(novoCliente);
				} catch (RuntimeException gdxEx) {
					if (gdxEx.getCause() instanceof SocketTimeoutException) {
						// server.getLogger().loga("NET SERVER",
						// "Nenhuma conexão estabelecida");
					} else
						Gdx.app.error("NET", "Erro durante Socket Accept", gdxEx);
					// throw gdxEx;
				}

				Thread.yield();
			}
		}
	}

	/**
	 * Atualiza todas as conexões
	 */
	public final void update() {
		if (disposed)
			throw new RuntimeException("Tentativa de update() em socket server disposed");

		for (int i = 0; i < clientes.size; i++) {
			if (!clientes.get(i).getConectado())
				clientesRemover.add(clientes.get(i));
			else {
				try {
					clientes.get(i).update(pacoteManager);
				} catch (IOException e) {
					server.getLogger().loga("Erro durante update de cliente socket (fechado conex�o)", e);

					clientesRemover.add(clientes.get(i));
				}
			}
		}

		for (int i = 0; i < clientesRemover.size; i++) {
			clientes.removeValue(clientesRemover.get(i), true);
			server.getLogger().loga("NET SERVER", "Conex�o com cliente terminada");
		}

		clientesRemover.clear();
	}

	public SocketServerThread(SocketServer<TSocketOwnerInfo, PacoteManager<TSocketOwnerInfo>, TSocketCliente> server, Protocol protocolo, int porta, TPacoteManager pacoteManager, ServerSocketHints hints, Array<TSocketCliente> clientes) {
		this.server = server;
		this.clientes = clientes;
		this.pacoteManager = pacoteManager;

		clientesRemover = new Array<TSocketCliente>();

		serverGDX = Gdx.net.newServerSocket(protocolo, porta, hints);

		AcceptThread acceptThread = new AcceptThread(clientes);
		acceptThread.start();
	}

	@Override
	public void dispose() {
		deveFinalizar = true;

		if (disposed)
			return;

		server.getLogger().loga("NET SERVER", "Finalizando servidor");

		// Encerra todas as conexões:
		for (int i = 0; i < clientes.size; i++)
			clientes.get(i).dispose();

		serverGDX.dispose();

		disposed = true;
	}
}
