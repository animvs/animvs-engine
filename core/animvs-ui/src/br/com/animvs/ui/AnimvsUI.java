package br.com.animvs.ui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AnimvsUI implements ApplicationListener {

	private class ClickBotaoBack extends ClickListener {

		private AnimvsUI ui;

		public ClickBotaoBack(AnimvsUI ui) {
			this.ui = ui;
		}

		@Override
		public final void clicked(InputEvent event, float x, float y) {
			ui.controller.setUiBackAcionado(ui);
		}
	}

	private AnimvsUIController controller;

	private boolean debug;

	private Stage stage;
	private Window window;
	private Skin uiSkin;
	private String windowStyleName;
	private Table tbConteudo;

	private boolean efeitoScale;

	// Usados somente até o momento do load:
	private String caminhoUISkin;
	// private String fonteNome;
	// private Fonte fonte;
	private boolean alteraInput;

	private float fadeDuracao;

	private ArrayMap<String, BitmapFont> fontes;

	private AssetManager assetManager;

	private Table tbRetornar;

	private Vector2 ratioCache;

	protected final ArrayMap<String, BitmapFont> getFontes() {
		return fontes;
	}

	protected final AssetManager getAssetManager() {
		return assetManager;
	}

	// private InputProcessor inputAnterior;

	protected final AnimvsUIController getController() {
		return controller;
	}

	public final Stage getStage() {
		return stage;
	}

	public final void setDebug(boolean debug) {
		this.debug = debug;
		window.debug();
		getTbConteudo().debug();
		getTbRetornar().debug();
	}

	public final boolean getVisivel() {
		if (window == null)
			return false;

		return window.isVisible();
	}

	public final void setVisivel(boolean visivel) {
		if (window.isVisible() != visivel) {

			if (visivel) {
				if (fadeDuracao > 0) {
					window.getColor().a = 0;
					window.addAction(Actions.fadeIn(fadeDuracao, Interpolation.fade));
				}
			}
			// else
			// window.addAction(Actions.sequence(Actions.fadeOut(fadeDuracao,
			// Interpolation.fade), Actions.removeActor()));
		}

		if (window != null)
			window.setVisible(visivel);

		controller.uiVisivel(this, visivel);
	}

	protected Table getTbConteudo() {
		return tbConteudo;
	}

	protected Table getTbRetornar() {
		return tbRetornar;
	}

	protected final Skin getUiSkin() {
		return uiSkin;
	}

	public final boolean getEfeitoScale() {
		return efeitoScale;
	}

	public final void setEfeitoScale(boolean efeitoScale) {
		if (this.efeitoScale == efeitoScale)
			return;

		this.efeitoScale = efeitoScale;
	}

	public AnimvsUI(AnimvsUIController controller, AssetManager assetManager, String caminhoUISkin, String fonteNome, BitmapFont fonte) {
		if (controller == null)
			throw new AnimvsUIException("O parâmetro controller não pode ser null");

		this.controller = controller;
		ratioCache = new Vector2();

		ArrayMap<String, BitmapFont> fontes = new ArrayMap<String, BitmapFont>();
		fontes.put(fonteNome, fonte);

		iniciaUI(fontes, assetManager, caminhoUISkin, null, true);
	}

	public AnimvsUI(AnimvsUIController controller, AssetManager assetManager, String caminhoUISkin, ArrayMap<String, BitmapFont> fontes) {
		this(controller, assetManager, caminhoUISkin, fontes, null, true);
	}

	public AnimvsUI(AnimvsUIController controller, AssetManager assetManager, String caminhoUISkin, ArrayMap<String, BitmapFont> fontes, String windowStyleName, boolean alterainput) {
		if (controller == null)
			throw new AnimvsUIException("O parâmetro controller não pode ser null");

		this.controller = controller;
		ratioCache = new Vector2();

		iniciaUI(fontes, assetManager, caminhoUISkin, windowStyleName, alterainput);
	}

	@Override
	public void create() {
		// batch = new SpriteBatch();

		// stage = new Stage();

		debug = false;

		uiSkin = new Skin();
		// uiSkin.addRegions(assetManager.get("data/graphics/ui/ui-main.atlas",
		// TextureAtlas.class));
		uiSkin.addRegions(assetManager.get(caminhoUISkin, TextureAtlas.class));
		// uiSkin.add("default-font",
		// AntsFontController.getFontePequena().getFonteGDX());

		// Limpa resource utilizado somente para o load:
		caminhoUISkin = null;

		for (int i = 0; i < fontes.size; i++)
			uiSkin.add(fontes.getKeyAt(i), fontes.getValueAt(i), BitmapFont.class);

		// uiSkin.load(Gdx.files.internal("data/ui/ui-main.json"));
		uiSkin.load(Gdx.files.internal(getController().getCaminhoStyleJSON()));

		if (windowStyleName == null)
			window = new Window("", uiSkin);
		else
			window = new Window("", uiSkin, windowStyleName);

		// window.setFillParent(true);
		window.setVisible(false);

		// Gdx.input.setInputProcessor(this.stage);

		tbConteudo = new Table();
		tbRetornar = new Table();

		// tbConteudo.setFillParent(true);

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public final void reconstroi() {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void setBoundsByViewport(Viewport viewport) {
		window.setBounds(viewport.getLeftGutterWidth(), viewport.getTopGutterY(), viewport.getScreenWidth(), viewport.getScreenHeight());
	}

	@Override
	public final void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

		stage.clear();
		window.clear();
		// window.setFillParent(true);
		// window.setKeepWithinStage(true);
		// window.setBounds(0f, 0f, width, height);
//		setBoundsByViewport(stage.getViewport());
		
		Rectangle bounds = getController().getBounds();
		window.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
		stage.addActor(window);

		ratioCache = getController().getRatio(ratioCache);

		// Gdx.app.log("ANIMVS UI", "Resize - Resolução: " +
		// Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight() +
		// " RatioX: " + ratioX + " RatioY: " + ratioY);

		tbConteudo.clear();
		tbRetornar.clear();

		// window.setFillParent(false);
		// window.setBounds(0f, 0f, (int)(width * 0.98f), height);
		// window.setFillParent(true);

		eventoDimensiona(width, height, ratioCache.x, ratioCache.y);

		window.add(tbConteudo).fill().expand();
		// tbRetornar.add(tbComandos).height(115f * ratioY).fillX().expand();
		// batch.getProjectionMatrix().setToOrtho2D(0f, 0f, width, height);

		if (criaBotaoRetornar()) {
			TextButton botaoRetornar = new TextButton("Retornar", getUiSkin());
			botaoRetornar.addListener(new ClickBotaoBack(this));

			tbRetornar.add(botaoRetornar).fillX().height(110f * ratioCache.y).expand();

			window.row();
			window.add(tbRetornar).fillX().left().expandX();
		}
		// tbConteudo.debug();
	}

	protected void dimensiona() {
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render() {
		stage.act(Gdx.graphics.getDeltaTime());

		// TODO: Corrigir, fix para UI's que não realizam a progressão de alpha
		// durante fadein por algum motivo:
		if (window.getActions().size == 0 && window.getColor().a != 1f)
			window.getColor().a = 1f;
		// else
		// Gdx.app.debug("ANIMVS UI", "ALPHA COLOR DEBUG: " +
		// window.getColor().a);

		// batch.begin();
		stage.draw();
		// batch.end();

		if (debug) {
			// batch.begin();
			//Table.drawDebug(stage);
			// batch.end();
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
		controller.uiDisposed(this);

		stage.dispose();
		try {
			uiSkin.dispose();
		} catch (GdxRuntimeException e) {
			// Tratamento de dispose duplicado de fontes:
			if (!e.getMessage().equals("Pixmap already disposed!"))
				throw e;
		}
	}

	// public final void dimensiona() {
	// eventoDimensiona(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
	// RenderizacaoController.getRatio().x,
	// RenderizacaoController.getRatio().y);
	//
	// if (botaoVoltar != null) {
	// getWindow().row();
	// getWindow().add(botaoVoltar).height(115f *
	// RenderizacaoController.getRatio().y).fillX();
	// }
	// }

	protected void setInput() {
		Gdx.input.setInputProcessor(stage);
	}

	protected abstract void eventoDimensiona(int width, int height, float ratioX, float ratioY);

	// private final void preparaTamanho(float tamanhoX, float tamanhoY) {
	// this.tamanhoDesejado = new Vector2();
	//
	// tamanhoDesejado.set(tamanhoX, tamanhoY);
	// }

	private final void iniciaUI(ArrayMap<String, BitmapFont> fontes, AssetManager assetManager, String caminhoUISkin, String windowStyleName, boolean alterainput) {
		this.assetManager = assetManager;
		this.caminhoUISkin = caminhoUISkin;
		this.fontes = fontes;
		this.windowStyleName = windowStyleName;
		this.alteraInput = alterainput;

		// this.stage = new Stage(viewport);
		this.stage = new Stage(new ScreenViewport());

		efeitoScale = false;

		fadeDuracao = 0.8f; // Por padrão, 800ms de duração.
	}

	protected final Label criaLabelCentralizado(String texto, Skin uiSkin) {
		return criaLabelCentralizado(texto, uiSkin, null);
	}

	protected final Label criaLabelCentralizado(String texto, Skin uiSkin, String styleName) {
		Label novoLabel;

		if (styleName != null)
			novoLabel = new Label(texto, uiSkin, styleName);
		else
			novoLabel = new Label(texto, uiSkin);

		novoLabel.setWrap(true);
		//novoLabel.setAlignment(Align.center);

		return novoLabel;
	}

	protected boolean criaBotaoRetornar() {
		return true;
	}

	protected abstract void eventoVisivel();

	protected abstract void eventoNaoVisivel();

	protected abstract void eventoBack();
}
