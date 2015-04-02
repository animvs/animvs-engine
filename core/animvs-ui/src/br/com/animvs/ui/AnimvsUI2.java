package br.com.animvs.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AnimvsUI2 implements ApplicationListener {

    private AnimvsUIController2 controller;

    private boolean debug;

    private Stage stage;
    private Window window;
    private Skin uiSkin;
    private String windowStyleName;

    // Usados somente até o momento do load:
    private String caminhoUISkin;

    private ArrayMap<String, BitmapFont> fontes;

    private AssetManager assetManager;

    private Vector2 ratioCache;

    protected final ArrayMap<String, BitmapFont> getFonts() {
        return fontes;
    }

    protected final AssetManager getAssetManager() {
        return assetManager;
    }

    // private InputProcessor inputAnterior;

    protected final AnimvsUIController2 getController() {
        return controller;
    }

    public final Stage getStage() {
        return stage;
    }

    protected final Window getWindow() {
        return window;
    }

    public final void setDebug(boolean debug) {
        this.debug = debug;
        window.debug();
    }

    protected final Skin getUiSkin() {
        return uiSkin;
    }

    protected boolean getUseInput() {
        return true;
    }


    public AnimvsUI2(AnimvsUIController2 controller, AssetManager assetManager, String caminhoUISkin, String fonteNome, BitmapFont fonte) {
        if (controller == null)
            throw new AnimvsUIException("O parâmetro controller não pode ser null");

        ArrayMap<String, BitmapFont> fontes = new ArrayMap<String, BitmapFont>();
        fontes.put(fonteNome, fonte);

        initialize(fontes, assetManager, caminhoUISkin, null);
    }

    public AnimvsUI2(AnimvsUIController2 controller, AssetManager assetManager, String caminhoUISkin, ArrayMap<String, BitmapFont> fontes) {
        if (controller == null)
            throw new AnimvsUIException("O parâmetro controller não pode ser null");

        this.controller = controller;
        ratioCache = new Vector2();

        initialize(fontes, assetManager, caminhoUISkin, windowStyleName);
    }

    @Override
    public void create() {
        debug = false;

        /*uiSkin = new Skin();
        uiSkin.addRegions(assetManager.get(caminhoUISkin, TextureAtlas.class));

        // Limpa resource utilizado somente para o load:
        caminhoUISkin = null;

        for (int i = 0; i < fontes.size; i++)
            uiSkin.add(fontes.getKeyAt(i), fontes.getValueAt(i), BitmapFont.class);

        // uiSkin.load(Gdx.files.internal("data/ui/ui-main.json"));
        uiSkin.load(Gdx.files.internal(getController().getStyleJSONPath()));*/

        uiSkin = AnimvsUIController.createSkin(assetManager, caminhoUISkin, fontes, getController().getStyleJSONPath());

        if (windowStyleName == null)
            window = new Window("", uiSkin);
        else
            window = new Window("", uiSkin, windowStyleName);
        window.setName("window");

        // window.setFillParent(true);

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Deprecated
    public final void rebuild() {
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setBoundsByViewport(Viewport viewport) {
        //window.setBounds(viewport.getLeftGutterWidth(), viewport.getTopGutterY(), viewport.getScreenWidth(), viewport.getScreenHeight());
        window.setBounds(viewport.getLeftGutterWidth(), viewport.getTopGutterHeight(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    @Override
    public final void resize(int width, int height) {
        stage.getViewport().update(width, height);

        stage.clear();
        window.clear();

        /*Rectangle bounds = getController().getBounds();
        window.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);*/

        stage.addActor(window);

        ratioCache = getController().getRatio(ratioCache);

        eventBuild(width, height, ratioCache.x, ratioCache.y);
    }

    @Override
    public void render() {
        //stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        stage.act(Gdx.graphics.getDeltaTime());

        // TODO: Corrigir, fix para UI's que não realizam a progressão de alpha durante fadein por algum motivo:
        if (window.getActions().size == 0 && window.getColor().a != 1f)
            window.getColor().a = 1f;
        // else
        // Gdx.app.debug("ANIMVS UI", "ALPHA COLOR DEBUG: " +
        // window.getColor().a);

        // batch.begin();
        stage.draw();
        // batch.end();

        window.setDebug(debug);
        /*if (debug) {
            // batch.begin();
			//Table.drawDebug(stage);
			// batch.end();
		}*/
    }

    public void transition(float width) {
        transition(window.getChildren(), width, true);
    }

    private void transition(SnapshotArray<Actor> actors, float width, boolean toLeft) {
        for (int i = 0; i < actors.size; i++) {
            float movementAmount;

            if (actors.get(i) instanceof Table && !(actors.get(i) instanceof Button)) {
                transition(((Table) actors.get(i)).getChildren(), width, toLeft);
            } else {
                if (toLeft)
                    movementAmount = -(actors.get(i).getX() + actors.get(i).getWidth());
                else
                    movementAmount = width + (actors.get(i).getX() + actors.get(i).getWidth());
                toLeft = !toLeft;
                actors.get(i).addAction(Actions.sequence(Actions.moveBy(movementAmount, 0f, 0.75f, Interpolation.exp10), new Action() {
                    @Override
                    public boolean act(float delta) {
                        window.setVisible(false);
                        return true;
                    }
                }));
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        // batch.dispose();
        controller.eventUIDisposed(this);

        stage.dispose();
    }

    protected void setInput() {
        Gdx.input.setInputProcessor(stage);
    }

    protected abstract void eventBuild(int width, int height, float ratioX, float ratioY);

    private final void initialize(ArrayMap<String, BitmapFont> fontes, AssetManager assetManager, String caminhoUISkin, String windowStyleName) {
        this.ratioCache = new Vector2();

        this.assetManager = assetManager;
        this.caminhoUISkin = caminhoUISkin;
        this.fontes = fontes;
        this.windowStyleName = windowStyleName;

        //this.stage = new Stage(new FitViewport(getController().getResolutionReal().x, getController().getResolutionReal().y));
        //this.stage = new Stage(new FitViewport(768, 1280));

        this.stage = new Stage(new ScreenViewport(), new PolygonSpriteBatch());
    }

    protected final Label createLabelCentered(String texto, Skin uiSkin) {
        return createLabelCentered(texto, uiSkin, null);
    }

    protected final Label createLabelCentered(String texto, Skin uiSkin, String styleName) {
        Label novoLabel;

        if (styleName != null)
            novoLabel = new Label(texto, uiSkin, styleName);
        else
            novoLabel = new Label(texto, uiSkin);

        novoLabel.setWrap(true);
        novoLabel.setAlignment(Align.center);

        return novoLabel;
    }

    protected abstract void eventVisible();

    protected abstract void eventNotVisible();

    protected abstract void eventoBack();
}
