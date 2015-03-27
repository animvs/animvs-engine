package br.com.animvs.extensions.curveditor.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


/**
 * Created by DALDEGAN on 27/03/2015.
 */
public final class GameController implements Disposable {
    private final int SPILINE_DETAIL = 100;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private CatmullRomSpline<Vector2> bezier;
    private Vector2[] points;

    private OrthographicCamera camera;

    public GameController() {
        this.batch = new SpriteBatch();
        //this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        /*this.camera.zoom = 2f;
        this.camera.update();*/

        this.shapeRenderer = new ShapeRenderer();
        //this.shapeRenderer.setProjectionMatrix(camera.combined);
    }

    public final void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        input();

        if (bezier == null || bezier.controlPoints.length <= 1)
            return;

        this.shapeRenderer.setColor(0f, 0f, 1f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < SPILINE_DETAIL - 1; ++i)
            shapeRenderer.line(points[i], points[i + 1]);
        shapeRenderer.end();

        this.shapeRenderer.setColor(1f, 0f, 0f, 1f);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < bezier.controlPoints.length; i++)
            shapeRenderer.circle(bezier.controlPoints[i].x, bezier.controlPoints[i].y, 5f);
        shapeRenderer.end();

        /* ON THE FLY
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.getColor().set(0f, 0f, 1f, 1f);
        for (int i = 0; i < -1; ++i)
            shapeRenderer.line(bezier.valueAt(points[i], ((float) i) / ((float) SPILINE_DETAIL - 1)), bezier.valueAt(points[i + 1], ((float) i) / ((float) SPILINE_DETAIL - 1)));
        shapeRenderer.end();*/
    }

    private void cache() {
        if (bezier.controlPoints.length <= 1)
            return;

        points = new Vector2[SPILINE_DETAIL];

        //Bezier<Vector2> myCatmull = new Bezier<Vector2>(points, 0, 4);
        //Vector2 out = new Vector2();
        for (int i = 0; i < SPILINE_DETAIL; ++i) {
            points[i] = new Vector2();
            //bezier.valueAt(points[i], ((float) i) / ((float) SPILINE_DETAIL - 1));
            bezier.valueAt(points[i], i / (float) SPILINE_DETAIL);
        }
    }

    private void input() {
        if (!Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            return;

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        Array<Vector2> pointArray;

        if (bezier != null)
            pointArray = new Array<Vector2>(bezier.controlPoints);
        else
            pointArray = new Array<Vector2>(new Vector2[0]);

        int nearestIndex = 0;
        for (int i = 0; i < pointArray.size; i++) {
            if (pointArray.get(i).dst(mouseX, mouseY) < 5f) {
                pointArray.removeIndex(i);

                prepareCurve(pointArray.toArray());
                cache();

                return;
            }

            nearestIndex = i;
        }

        pointArray.insert(nearestIndex, new Vector2(mouseX, mouseY));
        prepareCurve(pointArray.toArray());

        cache();
    }

    private void prepareCurve(Vector2[] points) {
        if (bezier == null)
            bezier = new CatmullRomSpline<Vector2>(points, false);
        else
            bezier.set(points, false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
