package br.com.animvs.engine2.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

public final class ConnectionChecker {

	public abstract class ConnectionCheckerCallBack {
		public abstract void checkEnded(ConnectionStatus status);
	}

	public enum ConnectionStatus {
		CHECKING, CONNECTED, DISCONNECTED
	}

	private ConnectionStatus connectionStatus;
	private ConnectionCheckerCallBack callback;

	public ConnectionChecker() {
		connectionStatus = ConnectionStatus.CHECKING;
	}

	public void start() {
		Gdx.net.sendHttpRequest(new HttpRequest("GET"), new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				connectionStatus = ConnectionStatus.CONNECTED;
				checkEnded(connectionStatus);
			}

			@Override
			public void failed(Throwable t) {
				connectionStatus = ConnectionStatus.DISCONNECTED;
				checkEnded(connectionStatus);
			}

			@Override
			public void cancelled() {
				throw new RuntimeException("Connection checker was cancelled");
			}

			private void checkEnded(ConnectionStatus status) {
				if (callback != null)
					callback.checkEnded(connectionStatus);
			}
		});
	}

	public void registerCallBack(ConnectionCheckerCallBack callback) {
		this.callback = callback;
	}
}
