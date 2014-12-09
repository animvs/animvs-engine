package br.com.animvs.engine2.graficos.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public final class ShaderBloom extends Shader {
	private float time;

	public final void setPower(float power) {
	}

	public ShaderBloom() {
	}

	@Override
	protected FileHandle getFragmentShaderFile() {
		return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/frag/bloom.frag");
	}

	@Override
	protected FileHandle getVertexShaderFile() {
		return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/vert/default.vert");
	}

	@Override
	public void update() {
		time += Gdx.graphics.getDeltaTime();

		setUniformf("u_power", MathUtils.cos(time));
	}
}
