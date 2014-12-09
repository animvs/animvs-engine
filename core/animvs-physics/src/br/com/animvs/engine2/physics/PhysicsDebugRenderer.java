package br.com.animvs.engine2.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

final class PhysicsDebugRenderer {

    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private World world;

    private AnimvsPhysicsController physicsController;

    public PhysicsDebugRenderer(AnimvsPhysicsController physicsController, World world) {
        this.world = world;

        if (physicsController == null)
            throw new RuntimeException("The parameter 'physicsController' must be != null");

        this.physicsController = physicsController;
        // setCamera(camera);

        this.debugMatrix = new Matrix4();
    }

    public void initialize(World world) {
        this.world = world;

        this.debugRenderer = new Box2DDebugRenderer();
        this.debugRenderer.setDrawBodies(true);
    }

    // public void setCamera(OrthographicCamera camera) {
    // debugMatrix = new Matrix4(camera.combined);
    // debugMatrix.scale(FisicaController.BOX_TO_WORLD,
    // FisicaController.BOX_TO_WORLD, 1f);
    // }

    public void render(Matrix4 matrix) {
        if (world == null)
            return;

        // TODO: retirar o calculo do matrix do loop principal.
        debugMatrix.set(matrix);
        debugMatrix.scale(physicsController.getBoxToWorld(), physicsController.getBoxToWorld(), 1f);

        debugRenderer.render(world, debugMatrix);
    }

    public void dispose() {
        if (debugRenderer != null)
            debugRenderer.dispose();
    }
}
