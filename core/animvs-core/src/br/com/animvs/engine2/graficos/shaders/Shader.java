package br.com.animvs.engine2.graficos.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public abstract class Shader implements Disposable {

	private ShaderProgram shaderGDX;

	public final ShaderProgram getGDXShader() {
		return shaderGDX;
	}

	protected abstract FileHandle getFragmentShaderFile();

	protected abstract FileHandle getVertexShaderFile();

	public Shader() {
		// this.assetManager = assetManager;

		ShaderProgram.pedantic = false;

		shaderGDX = new ShaderProgram(getVertexShaderFile(), getFragmentShaderFile());

		if (!shaderGDX.isCompiled()) {
			throw new RuntimeException("Problema ao carregar shader. Vertex: '" + getVertexShaderFile().path() + "' Fragment: '" + getFragmentShaderFile().path() + "'. Erro: " + shaderGDX.getLog());
		}
		
		if (getGDXShader().getLog() != null && !getGDXShader().getLog().equals("No errors.\n") && getGDXShader().getLog().length() > 0)
			Gdx.app.error("SHADER LOG", getGDXShader().getLog());
	}

	protected final void setUniformi(String nomeUniform, int valor) {
		shaderGDX.setUniformi(nomeUniform, valor);
	}

	protected final void setUniformf(String nomeUniform, Vector2 valor) {
		shaderGDX.setUniformf(nomeUniform, valor);
	}

	protected final void setUniformf(String nomeUniform, Color valor) {
		shaderGDX.setUniformf(nomeUniform, valor);
	}

	protected final void setUniformf(String nomeUniform, Vector3 valor) {
		shaderGDX.setUniformf(nomeUniform, valor);
	}

	protected final void setUniformf(String nomeUniform, float valor) {
		shaderGDX.setUniformf(nomeUniform, valor);
	}

	protected final void setUniformf(int uniformLocation, float valor) {
		shaderGDX.setUniformf(uniformLocation, valor);
	}

	public final String getUniformName(int location) {
		return shaderGDX.getUniforms()[location];
	}

	public final int getUniformLocation(String name) {
		return shaderGDX.getUniformLocation(name);
	}

	@Override
	public void dispose() {
		shaderGDX.dispose();
	}

	// default
	public final void beginRender(Batch batch) {
		batch.setShader(shaderGDX);
		shaderGDX.begin();
	}

	// default
	public final void endRender(Batch batch) {
		batch.setShader(null);
		shaderGDX.end();
	}

	public abstract void update();
}
