package br.com.animvs.engine2.graficos.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

public class ShaderLoad extends ShaderFullQuad {

	private float time;
	private Vector2 resolution;

	public ShaderLoad() {
        resolution = new Vector2();
	}

	@Override
	protected FileHandle getFragmentShaderFile() {
		/*return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/frag/loading.frag");*/

        return Gdx.files.internal("data/shaders/frag/loading.frag");
	}

	@Override
	protected void eventUpdateParameters() {
		time += Gdx.graphics.getDeltaTime() * 2f;
		setUniformf("iGlobalTime", time);
	}

	@Override
	protected Vector2 getResolution() {
		resolution.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		return resolution;
	}
}
