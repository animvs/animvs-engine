package br.com.animvs.engine2.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

final class ResponseListener implements HttpResponseListener {
	private HttpRequest request;

	public ResponseListener(HttpRequest request) {
		this.request = request;
	}

	@Override
	public void handleHttpResponse(HttpResponse httpResponse) {
		Gdx.app.log("HTTP REQUEST", "Request SUCCESS. URL: " + request.getUrl() + " Result:" + httpResponse.getResultAsString());
	}

	@Override
	public void failed(Throwable t) {
		Gdx.app.log("HTTP REQUEST", "Request FAILED. URL: " + request.getUrl() + " Content: " + request.getContent());
	}

	@Override
	public void cancelled() {
		Gdx.app.log("HTTP REQUEST", "Request CANCELED. URL: " + request.getUrl() + " Content: " + request.getContent());
	}
}
