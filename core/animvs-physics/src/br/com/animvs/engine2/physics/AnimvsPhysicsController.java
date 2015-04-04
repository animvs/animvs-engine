package br.com.animvs.engine2.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class AnimvsPhysicsController implements Disposable {
    private FixedTimeStepHandler stepHandler;

    private final float worldToBox;
    private final float boxToWorld;

    private World world;
    private Vector2 gravity;
    private Array<Body> bodiesToDestroy;
    private Array<Joint> jointsToDestroy;

    private PhysicsDebugRenderer debug;

    public static final void setSensor(Body body) {
        Array<Fixture> fixtures = body.getFixtureList();

        for (int i = 0; i < fixtures.size; i++)
            fixtures.get(i).setSensor(true);
    }

    public static final void setFilter(Body body, Filter filter) {
        Array<Fixture> fixtures = body.getFixtureList();

        for (int i = 0; i < fixtures.size; i++)
            fixtures.get(i).setFilterData(filter);
    }

    public float getWorldToBox() {
        return worldToBox;
    }

    public float getBoxToWorld() {
        return boxToWorld;
    }

    public final float toBox(float valor) {
        return valor * worldToBox;
    }

    public final float toWorld(float valor) {
        return valor * boxToWorld;
    }

    public final World getWorld() {
        return world;
    }

    public AnimvsPhysicsController(boolean debug, float boxToWorld, float worldToBox, Vector2 gravity) {
        this(debug, boxToWorld, worldToBox, gravity, null, null, null, null);
    }

    public AnimvsPhysicsController(boolean debug, float boxToWorld, float worldToBox, Vector2 gravity, Integer minFPS, Integer maxFPS, Integer velocityInterations, Integer positionInterations) {
        if (gravity == null)
            throw new RuntimeException("The parameter 'gravity' must be != null");

        this.worldToBox = worldToBox;
        this.boxToWorld = boxToWorld;
        this.gravity = gravity;

        if (debug)
            this.debug = new PhysicsDebugRenderer(this, world);

        stepHandler = new FixedTimeStepHandler(minFPS, maxFPS, velocityInterations, positionInterations);
        bodiesToDestroy = new Array<Body>();
        jointsToDestroy = new Array<Joint>();
    }

    public synchronized void initialize() {
        world = new World(gravity, true);

        if (debug != null)
            debug.initialize(world);
    }

    protected synchronized final void setContactListener(ContactListener contactListener) {
        world.setContactListener(contactListener);
    }

    public synchronized final void update(float dt) {
        stepHandler.update(world, dt);

        if (world.isLocked())
            Gdx.app.log("WORLD LOCKED", "WORLD LOCKED -----------------------------------------------------------");
        else
            destroyQueued();

        eventAfterUpdate();
    }

    public final void renderDebug(Matrix4 matrix) {
        if (debug == null)
            throw new RuntimeException("Physics debug must be enabled in order to render it");

        debug.render(matrix);
    }

    public final synchronized void destroyBody(Body body) {
        if (bodiesToDestroy.contains(body, true))
            return;

        bodiesToDestroy.add(body);
    }

    public final synchronized void destroyJoint(Joint joint) {
        if (jointsToDestroy.contains(joint, true))
            return;

        jointsToDestroy.add(joint);
    }

    protected void eventAfterUpdate() {
        //Can be overriden if necessary
    }

    private void destroyQueued() {
        if (world.isLocked()) {
            Gdx.app.log("PHYSICS", "AVOIDING PHYSIC DISPOSAL - WORLD IS LOCKED");
            return;
        }

        for (int i = 0; i < bodiesToDestroy.size; i++) {
            if (bodiesToDestroy.get(i) != null)
                world.destroyBody(bodiesToDestroy.get(i));
        }

        for (int i = 0; i < jointsToDestroy.size; i++) {
            if (jointsToDestroy.get(i) != null)
                world.destroyJoint(jointsToDestroy.get(i));
        }

        bodiesToDestroy.clear();
        jointsToDestroy.clear();
    }

    public void TMP_FORCE_WORLD_UNLOCK() {
        while (world.isLocked())
            update(0.01f);
    }

    @Override
    public void dispose() {
        if (debug != null)
            debug.dispose();

        world.dispose();
    }
}
