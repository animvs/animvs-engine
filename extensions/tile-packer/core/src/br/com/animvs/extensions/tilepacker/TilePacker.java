package br.com.animvs.extensions.tilepacker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

public class TilePacker extends ApplicationAdapter {
    private static final int PAD_X = 1;
    private static final int PAD_Y = 1;
    private static int TILE_SIZE = 32;

    private static String DIR_INPUT = "cache/input/input.png";
    private static String DIR_OUTPUT = "cache/output/output.png";

    @Override
    public void create() {
        Texture texture = new Texture(Gdx.files.local(DIR_INPUT), Pixmap.Format.RGBA8888, false);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        Pixmap pixmapOriginal = new Pixmap(Gdx.files.local(DIR_INPUT));

        int totalTilesHorizontal = texture.getWidth() / TILE_SIZE;
        int totalTilesVertical = texture.getHeight() / TILE_SIZE;

        int sizeX = texture.getWidth() + totalTilesHorizontal * PAD_X;
        int sizeY = texture.getHeight() + totalTilesHorizontal * PAD_Y;

        Pixmap pixmapNew = new Pixmap(sizeX, sizeY, Pixmap.Format.RGBA8888);
        pixmapNew.setColor(0f, 1f, 0f, 1f);

        for (int y = 0; y < totalTilesVertical; y++) {
            for (int x = 0; x < totalTilesHorizontal; x++) {
                pixmapNew.drawPixmap(
                        pixmapOriginal,
                        x * TILE_SIZE + (x * PAD_X) + PAD_X,
                        y * TILE_SIZE + (y * PAD_Y) + PAD_Y,

                        x * TILE_SIZE,
                        y * TILE_SIZE,

                        TILE_SIZE,
                        TILE_SIZE
                );

                //left pad:
                pixmapNew.drawPixmap(
                        pixmapOriginal,
                        x * TILE_SIZE + (x * PAD_X),
                        y * TILE_SIZE + (y * PAD_Y),

                        x * TILE_SIZE - PAD_X,
                        y * TILE_SIZE - PAD_Y,

                        PAD_X,
                        PAD_Y
                );
            }
        }

        PixmapIO.writePNG(Gdx.files.local(DIR_OUTPUT), pixmapNew);

        pixmapOriginal.dispose();
        pixmapNew.dispose();

        Gdx.app.exit();
    }

    @Override
    public void render() {
        /*Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();*/
    }
}
