package br.com.animvs.engine2.physics;

import com.badlogic.gdx.physics.box2d.World;

final class FixedTimeStepHandler {

    private final int maxFPS;
    private final int minFPS;

    private final float timeStep;
    private final float maxSteps;
    private final float maxTimePerFrame;

    private final int velocityInterations;
    private final int positionInterations;

    private float physicsTimeLeft;

    public float getPhysicsTimeLeft() {
        return physicsTimeLeft;
    }

    public FixedTimeStepHandler(Integer minFPS, Integer maxFPS, Integer velocityInterations, Integer positionInterations) {
        this.velocityInterations = velocityInterations != null ? velocityInterations : 8;
        this.positionInterations = positionInterations != null ? positionInterations : 3;

        this.minFPS = minFPS != null ? minFPS : 10;
        this.maxFPS = maxFPS != null ? maxFPS : 30;

        this.timeStep = 1f / this.maxFPS;
        this.maxSteps = 1f + this.maxFPS / this.minFPS;
        this.maxTimePerFrame = this.timeStep * this.maxSteps;
    }

    public int update(final World world, float deltaTime) {
        physicsTimeLeft += deltaTime;
        if (physicsTimeLeft > maxTimePerFrame) {
            physicsTimeLeft = maxTimePerFrame;
        }

        int steps = 0;
        while (physicsTimeLeft >= timeStep) {
            world.step(timeStep, velocityInterations, positionInterations);
            physicsTimeLeft -= timeStep;
            steps++;
        }
        return steps;
    }
}