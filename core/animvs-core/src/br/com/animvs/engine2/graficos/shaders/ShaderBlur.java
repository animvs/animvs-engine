package br.com.animvs.engine2.graficos.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

public final class ShaderBlur extends Shader {
	private float power;
	private float time;

	public final void setPower(float power) {
		this.power = power;
	}

	public ShaderBlur() {
		power = 1f;
	}

	@Override
	protected FileHandle getFragmentShaderFile() {
		return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/frag/blur.frag");
	}

	@Override
	protected FileHandle getVertexShaderFile() {
		return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/vert/default.vert");
	}

	@Override
	public void update() {
		time += Gdx.graphics.getDeltaTime();

		setUniformf("u_power", Math.abs(MathUtils.cos(power * time)));
	}
}
