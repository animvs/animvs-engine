package br.com.animvs.engine2.utils;

import com.badlogic.gdx.utils.Array;

public final class InterpolacaoController {
	private Array<Interpolacao> interpolacoesAtivas = new Array<Interpolacao>();
	private Array<Interpolacao> interpolacoesRemover = new Array<Interpolacao>();

	public final void update() {
		if (interpolacoesAtivas.size == 0)
			return;

		for (int i = 0; i < interpolacoesAtivas.size; i++) {
			interpolacoesAtivas.get(i).update();

			if (interpolacoesAtivas.get(i).cancelada) {
				interpolacoesRemover.add(interpolacoesAtivas.get(i));
				
				if (interpolacoesAtivas.get(i).getEvento() != null)
					interpolacoesAtivas.get(i).getEvento().eventoCancelado();
			} else if (!interpolacoesAtivas.get(i).getRodando()) {
				interpolacoesRemover.add(interpolacoesAtivas.get(i));
				
				if (interpolacoesAtivas.get(i).getEvento() != null)
					interpolacoesAtivas.get(i).getEvento().eventoFinalizado();
			}
		}

		for (int i = 0; i < interpolacoesRemover.size; i++)
			interpolacoesAtivas.removeValue(interpolacoesRemover.get(i), true);

		interpolacoesRemover.clear();
	}

	final void registraInterpolacao(Interpolacao interpolacao) {
		if (interpolacoesAtivas.contains(interpolacao, true))
			return;

		interpolacoesAtivas.add(interpolacao);
	}
}
