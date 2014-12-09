package br.com.animvs.ui;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;

public class AnimvsUIController2 implements Disposable {

    private boolean backPreviouslyPressed;

    private String styleJSONPath;

    private Vector2 resolutionReal;

    private Rectangle windowBoundsCache;

    private AnimvsUI2 ui;

    protected final Vector2 getResolutionReal() {
        return resolutionReal;
    }

    /*public final boolean getisivel() {
        return visivel;
    }

    public final void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }*/

    public final String getStyleJSONPath() {
        return styleJSONPath;
    }

    public boolean getAllowBack() {
        return true;
    }

    public boolean getAllowExitApp() {
        return true;
    }

    public AnimvsUI2 getUI() {
        return ui;
    }

    public void setUI() {
        if (ui != null)
            ui.eventNotVisible();

        this.ui = null;
    }

    public void setUI(AnimvsUI2 uiCurrent) {
        if (uiCurrent == null)
            setUI();
        else {
            if (this.ui != null)
                this.ui.eventNotVisible();

            this.ui = uiCurrent;
            if (this.ui.getUseInput())
                this.ui.setInput();

            /*this.ui.rebuild();*/
            this.ui.eventVisible();
        }
    }

    public final Vector2 getRatio() {
        Vector2 ratio = new Vector2();
        return getRatio(ratio);
    }

    public Vector2 getRatio(Vector2 ratioToBeSet) {
        if (ratioToBeSet == null)
            throw new RuntimeException("The parameter 'ratioToBeSet' must be != null");

        ratioToBeSet.set(Gdx.graphics.getWidth() / getResolutionReal().x, Gdx.graphics.getHeight() / getResolutionReal().y);

        return ratioToBeSet;
    }

    public AnimvsUIController2(String styleJSONPath, Vector2 resolutionReal) {
        this.styleJSONPath = styleJSONPath;
        this.resolutionReal = resolutionReal;

        windowBoundsCache = new Rectangle();

        Gdx.input.setCatchBackKey(true);
    }

    /*final void setUiBackAcionado(AnimvsUI uiBackAcionado) {
        this.uiBackAcionado = uiBackAcionado;
    }*/

    public final void render() {
        handleBack();

        if (ui != null) {
            ui.getStage().getViewport().apply(true);
            ui.render();
        }
    }

    public void resize(int width, int height) {
        if (ui != null) {
            ui.resize(width, height);
            eventAfterRecreate(ui);
        }
    }

    public void recreateFonts(ArrayMap<String, BitmapFont> fontes) {
        if (ui != null) {
            ArrayMap<String, BitmapFont> fontesAntigas = ui.getFonts();

            for (int j = 0; j < fontesAntigas.size; j++)
                ui.getUiSkin().remove(fontesAntigas.getKeyAt(j), BitmapFont.class);

            fontesAntigas.clear();

            for (int j = 0; j < fontes.size; j++) {
                fontesAntigas.put(fontes.getKeyAt(j), fontes.getValueAt(j));
                ui.getUiSkin().add(fontes.getKeyAt(j), fontes.getValueAt(j), BitmapFont.class);
            }
        }
    }

    @Override
    public final void dispose() {
        if (ui != null)
            ui.dispose();
    }

    final void eventUIDisposed(AnimvsUI2 ui) {
        if (ui != null && ui == this.ui)
            this.ui = null;
    }

    protected void eventAfterRecreate(AnimvsUI2 uiCurrent) {
    }

    final Rectangle getBounds() {
        return getBounds(windowBoundsCache);
    }

    protected Rectangle getBounds(Rectangle rectangleToBeSet) {
        rectangleToBeSet.set(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return rectangleToBeSet;
    }

    private final void handleBack() {
        // TODO: Aplicações Html 5 não devem fechar a última janela ?
        if (Gdx.app.getType() == ApplicationType.WebGL && ui == null)
            return;

        boolean back = checkKeyBackPressed() && getAllowBack();

        if (back) {
            if (ui != null)
                ui.eventoBack();
            else if (getAllowExitApp())
                Gdx.app.exit();
        }
    }

    private final boolean checkKeyBackPressed() {
        boolean pressedNow = Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE);

        if (!backPreviouslyPressed) {
            if (pressedNow) {
                backPreviouslyPressed = true;
                return true;
            }
        } else if (!pressedNow)
            backPreviouslyPressed = false;

        return false;
    }
}
