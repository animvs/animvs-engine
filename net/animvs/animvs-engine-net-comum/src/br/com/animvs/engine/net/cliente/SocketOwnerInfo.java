package br.com.animvs.engine.net.cliente;

public class SocketOwnerInfo {
	private String ip;
	
	public final String getIp() {
		return ip;
	}
	
	public SocketOwnerInfo(String ip){
		this.ip = ip;
	}
}
