package br.com.animvs.engine2.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DALDEGAN on 06/03/2015.
 */
public class RayCasterTMP implements RayCastCallback {

    public final class CollisionInfo {
        public Fixture fixture;
        public Vector2 point;
        public Vector2 normal;
        public float fraction;

        public CollisionInfo(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
            this.fixture = fixture;
            this.point = point;
            this.normal = normal;
            this.fraction = fraction;
        }

        public CollisionInfo() {
        }
    }

    private World world;
    private Array<CollisionInfo> collidedBodies;

    public RayCasterTMP(World physicWorld) {
        this.world = physicWorld;
        collidedBodies = new Array<CollisionInfo>();
    }

    public final void rayCast(Vector2 lineStart, Vector2 lineEnd) {
        collidedBodies.clear();
        world.rayCast(this, lineStart, lineEnd);
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        collidedBodies.add(new CollisionInfo(fixture, point, normal, fraction));

        return 1f;
    }
}
