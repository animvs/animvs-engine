package br.com.animvs.engine2.graficos.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector2;

public abstract class ShaderFullQuad extends Shader {

    private Vector2 resolucao;
    private Mesh mesh;

    public ShaderFullQuad() {
        mesh = createFullScreenQuad();
        resolucao = new Vector2();
    }

    @Override
    protected FileHandle getVertexShaderFile() {
        /*return Gdx.files.classpath("br/com/animvs/engine2/graficos/shaders/vert/fullquad.vert");*/
        return Gdx.files.internal("data/shaders/vert/fullquad.vert");
    }

    @Override
    public final void update() {
        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);

        eventBeforeBegin();

        getGDXShader().begin();

        setUniformf("iResolution", getResolution());
        eventUpdateParameters();

        mesh.render(getGDXShader(), GL20.GL_TRIANGLE_FAN);
        getGDXShader().end();

        eventAfterEnd();
        Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
    }

    protected abstract Vector2 getResolution();

    protected void eventBeforeBegin() {
    }

    protected void eventAfterEnd() {
    }

    protected abstract void eventUpdateParameters();

    @Override
    public void dispose() {
        mesh.dispose();
        super.dispose();
    }

    public Mesh createFullScreenQuad() {

        // mesh = new Mesh(VertexDataType.VertexArray, false, size * 4, size *
        // 6, new VertexAttribute(Usage.Position, 2,
        // ShaderProgram.POSITION_ATTRIBUTE), new
        // VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
        // new VertexAttribute(Usage.TextureCoordinates, 2,
        // ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));

        return createMesh();
    }

    protected Mesh createMesh() {
        float[] verts = createVerticesFullQuadStandard();

        Mesh fullscreenQuad = new Mesh(true, 4, 0, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        return fullscreenQuad.setVertices(verts);
    }

    protected float[] createVerticesFullQuadStandard() {
        float[] verts = new float[12];
        int i = 0;

        verts[i++] = -1f;
        verts[i++] = -1f;
        verts[i++] = 0;

        verts[i++] = 1f;
        verts[i++] = -1f;
        verts[i++] = 0;

        verts[i++] = 1f;
        verts[i++] = 1f;
        verts[i++] = 0;

        verts[i++] = -1f;
        verts[i++] = 1f;
        verts[i++] = 0;

        return verts;
    }

	/*
	 * public Mesh createFullScreenQuad() { float[] verts = new float[20]; int i
	 * = 0; verts[i++] = -1.f; // x1 verts[i++] = -1.f; // y1 verts[i++] = 0.f;
	 * // u1 verts[i++] = 0.f; // v1 verts[i++] = 1.f; // x2 verts[i++] = -1.f;
	 * // y2 verts[i++] = 1.f; // u2 verts[i++] = 0.f; // v2 verts[i++] = 1.f;
	 * // x3 verts[i++] = 1.f; // y2 verts[i++] = 1.f; // u3 verts[i++] = 1.f;
	 * // v3 verts[i++] = -1.f; // x4 verts[i++] = 1.f; // y4 verts[i++] = 0.f;
	 * // u4 verts[i++] = 1.f; // v4 Mesh tmpMesh = new Mesh(true, 4, 0, new
	 * VertexAttribute(Usage.Position, 2, "a_position"), new
	 * VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));
	 * tmpMesh.setVertices(verts); return tmpMesh; }
	 */
}
