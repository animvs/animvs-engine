package br.com.animvs.engine2.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public final class Interpolacao {

	private Interpolation interpolacaoGDX;

	boolean cancelada;

	private float tempoTotalS;
	private float tempoAtualS;

	private float parametroInicial;
	private float parametroFinal;

	private float valor;

	private InterpolacaoEvento evento;

	private boolean rodando;

	final InterpolacaoEvento getEvento() {
		return evento;
	}

	public final void setEvento(InterpolacaoEvento evento) {
		evento.setOwner(this);

		this.evento = evento;
	}

	public final boolean getRodando() {
		// return tempoAtualS > 0f && !getFinalizada();
		return rodando;
	}

	public final float getValor() {
		return valor;
	}

	public final void inicia(Interpolation interpolacaoGDX, float parametroInicial, float parametroFinal, float tempoMS, InterpolacaoController controller) {
		if (interpolacaoGDX == null)
			throw new RuntimeException("O parâmetro interpolacaoGDX não pode ser null");

		// if (parametroInicial >= parametroFinal)
		// throw new
		// RuntimeException("O parâmetro parametroInicial deve ser < ao parâmetro parametroFinal");

		if (tempoMS <= 0)
			throw new RuntimeException("O parâmetro tempoMS deve ser > 0");

		this.interpolacaoGDX = interpolacaoGDX;
		this.tempoTotalS = tempoMS * 0.001f;
		this.parametroInicial = parametroInicial;
		this.parametroFinal = parametroFinal;

		this.cancelada = false;
		this.tempoAtualS = 0f;
		this.rodando = true;

		controller.registraInterpolacao(this);
	}

	public final void cancela() {
		cancelada = true;
		rodando = false;
	}

	final void update() {
		tempoAtualS += Gdx.graphics.getDeltaTime();

		float alpha = MathUtils.clamp(tempoAtualS / tempoTotalS, 0f, 1f);

		valor = interpolacaoGDX.apply(parametroInicial, parametroFinal, alpha);

		if (evento != null)
			evento.update(valor);

		rodando = (tempoAtualS / tempoTotalS) < 1f;
	}
}
