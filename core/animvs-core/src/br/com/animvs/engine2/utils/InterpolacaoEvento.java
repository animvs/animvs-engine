package br.com.animvs.engine2.utils;

public abstract class InterpolacaoEvento {
	private Interpolacao owner;
	
	final void setOwner(Interpolacao owner) {
		this.owner = owner;
	}

	public final Interpolacao getOwner() {
		return owner;
	}
	
	public void eventoFinalizado(){
	}
	
	public void eventoCancelado(){
	}

	public abstract void update(float valor);
}
