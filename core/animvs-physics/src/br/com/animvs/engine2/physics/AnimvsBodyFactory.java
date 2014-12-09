package br.com.animvs.engine2.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Base physics bodies factory.
 *
 * @author Daldegan
 */
public final class AnimvsBodyFactory {

    private AnimvsBodyFactory() {
        // cannot be instanced
    }

    private static final float FRICTION = 0.01f;

    public static Body createEllipse6(AnimvsPhysicsController physics, Vector2 posicao, BodyDef.BodyType bodyTipo, float altura, float largura, float density, float restituition) {
        Vector2[] vertices = new Vector2[6];

        vertices[0] = new Vector2(largura, altura);
        vertices[1] = new Vector2(largura + (largura / 1.2f), 0f);
        vertices[2] = new Vector2(largura, -altura);
        vertices[3] = new Vector2(-largura, -altura);
        vertices[4] = new Vector2(-largura - (largura / 1.2f), 0f);
        vertices[5] = new Vector2(-largura, altura);

        return AnimvsBodyFactory.createByVertex(physics, posicao, 0f, bodyTipo, vertices, density, restituition, largura, altura);
        // Float massa = fisico.getMassa();
        // Engine.loga("MASSA", massa.toString());
        // fisico.setFriccao(0.3f);

    }

    public static Body createByVertex(AnimvsPhysicsController physics, Vector2 posicao, float angulo, BodyDef.BodyType tipoBody, Vector2[] vertices, float density, float restitution, float largura, float altura) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = tipoBody;
        bodyDef.position.set(physics.toBox(posicao.x), physics.toBox(Gdx.graphics.getHeight() - posicao.y));

        bodyDef.angle = angulo * MathUtils.degreesToRadians;
        Body body = physics.getWorld().createBody(bodyDef);

        PolygonShape bodyShape = new PolygonShape();

        Vector2[] verticesBox = new Vector2[vertices.length];

        // Inverte a lista de vertices, pois a criação de vertices deve ser em
        // sentido antiorario no engine Box2D.
        // for (int i = vertices.length - 1; i >= 0; i--) {
        // verticesBox[vertices.length - (i + 1)] = new
        // Vector2(FisicaController.converteParaBox(vertices[i].x),
        // FisicaController.converteParaBox(vertices[i].y*-1f));
        // }

        for (int i = 0; i < vertices.length; i++) {
            verticesBox[i] = new Vector2(physics.toBox(vertices[i].x), physics.toBox(vertices[i].y * -1f));
        }

        bodyShape.set(verticesBox);
        // body.setGravityScale(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.friction = FRICTION;

        body.createFixture(fixtureDef);
        bodyShape.dispose();

        // return new Fisico(body, largura, altura);
        return body;
    }

    //
    public static Body createRetangle(AnimvsPhysicsController physics, Vector2 position, float rotation, BodyDef.BodyType bodyType, float width, float height, float density, float restitution, boolean sensor) {

        BodyDef bodyDef = new BodyDef();

        // bodyDef.fixedRotation = true;
        // bodyDef.gravityScale = 0f;

        bodyDef.type = bodyType;
        bodyDef.position.set(physics.toBox(position.x + (width / 2f)), physics.toBox(position.y + (height / 2f)));

        bodyDef.angle = rotation;
        Body body = physics.getWorld().createBody(bodyDef);

        PolygonShape bodyShape = new PolygonShape();

        float w = physics.toBox(width / 2f);
        float h = physics.toBox(height / 2f);

        bodyShape.setAsBox(w, h);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.isSensor = sensor;
        fixtureDef.friction = FRICTION;

        body.createFixture(fixtureDef);
        bodyShape.dispose();

        // return new Fisico(body, width, height);
        return body;
    }

    public static Body createSphere(AnimvsPhysicsController physics, Vector2 position, BodyDef.BodyType bodyType, float radius, float density, float restitution) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = bodyType;

        bodyDef.position.set(physics.toBox(position.x), physics.toBox(position.y));

        Body body = physics.getWorld().createBody(bodyDef);

        CircleShape bodyShape = new CircleShape();
        bodyShape.setRadius(physics.toBox(radius));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = bodyShape;
        fixtureDef.friction = FRICTION;

        body.createFixture(fixtureDef);
        bodyShape.dispose();

        // return new Fisico(body, raio * 2f, raio * 2f);
        return body;
    }
}
