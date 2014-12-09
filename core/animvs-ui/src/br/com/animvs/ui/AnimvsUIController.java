package br.com.animvs.ui;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class AnimvsUIController {

    private boolean backPressionadoAnteriormente;

    private AnimvsUI uiBackAcionado;

    private Array<AnimvsUI> uisAbertas;

    private boolean permiteRetornar;

    private String caminhoStyleJSON;

    private boolean visivel;

    private Vector2 resolucaoReal;

    private Rectangle windowBoundsCache;

    protected Array<AnimvsUI> getUisAbertas() {
        return uisAbertas;
    }

    protected final Vector2 getResolucaoReal() {
        return resolucaoReal;
    }

    public final boolean getisivel() {
        return visivel;
    }

    public final void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public String getCaminhoStyleJSON() {
        return caminhoStyleJSON;
    }

    public final boolean getPermiteRetornar() {
        return permiteRetornar;
    }

    public final Vector2 getRatio() {
        Vector2 ratio = new Vector2();
        return getRatio(ratio);
    }

    public Vector2 getRatio(Vector2 ratioToBeSet) {
        if (ratioToBeSet == null)
            throw new RuntimeException("The parameter 'ratioToBeSet' must be != null");

        ratioToBeSet.set(Gdx.graphics.getWidth() / getResolucaoReal().x, Gdx.graphics.getHeight() / getResolucaoReal().y);

        return ratioToBeSet;
    }

    public final void setPermiteRetornar(boolean geraHistoricoDeTelas) {
        this.permiteRetornar = geraHistoricoDeTelas;
    }

    public AnimvsUIController(String caminhoStyleJSON, Vector2 resolucaoReal) {
        this.caminhoStyleJSON = caminhoStyleJSON;
        this.resolucaoReal = resolucaoReal;

        resolucaoReal = new Vector2();

        windowBoundsCache = new Rectangle();

        visivel = true;
        uisAbertas = new Array<AnimvsUI>();
        Gdx.input.setCatchBackKey(true);
    }

    final void setUiBackAcionado(AnimvsUI uiBackAcionado) {
        this.uiBackAcionado = uiBackAcionado;
    }

    public final void renderiza() {
        if (!visivel)
            return;

        handleBack();

        if (uisAbertas.size > 0)
            uisAbertas.get(uisAbertas.size - 1).render();
    }

    public void resize(int width, int height) {
        for (int i = 0; i < uisAbertas.size; i++)
            uisAbertas.get(i).resize(width, height);

        eventoAposDimensiona(uisAbertas);
    }

    public void reconstroiFontes(ArrayMap<String, BitmapFont> fontes) {
        for (int i = 0; i < uisAbertas.size; i++) {
            ArrayMap<String, BitmapFont> fontesAntigas = uisAbertas.get(i).getFontes();

            for (int j = 0; j < fontesAntigas.size; j++)
                uisAbertas.get(i).getUiSkin().remove(fontesAntigas.getKeyAt(j), BitmapFont.class);

            fontesAntigas.clear();

            for (int j = 0; j < fontes.size; j++) {
                fontesAntigas.put(fontes.getKeyAt(j), fontes.getValueAt(j));
                uisAbertas.get(i).getUiSkin().add(fontes.getKeyAt(j), fontes.getValueAt(j), BitmapFont.class);
            }
        }
    }

    public final void dispose() {
        for (int i = 0; i < uisAbertas.size; i++)
            uisAbertas.get(i).dispose();
    }

    public final void setJanelaAtualInvisivel() {
        if (uisAbertas.size > 0)
            uisAbertas.get(uisAbertas.size - 1).setVisivel(false);
    }

    public final void setTodasJanelaInvisivel() {
        while (uisAbertas.size > 0) {
            uisAbertas.get(uisAbertas.size - 1).setVisivel(false);
        }
    }

    protected void eventoAposDimensiona(Array<AnimvsUI> uisAbertas) {
    }

    final void uiDisposed(AnimvsUI ui) {
        uisAbertas.removeValue(ui, true);
    }

    final void uiVisivel(AnimvsUI ui, boolean visivel) {
        uisAbertas.removeValue(ui, true);

        if (visivel) {
            ui.eventoVisivel();

            // Gdx.input.setInputProcessor(ui.getStage());

            // if (!uisAbertas.contains(ui, true)) {
            uisAbertas.add(ui);
            // } else if (uisAbertas.size == 0) {
            // uisAbertas.removeValue(ui, true);
            // }

            ui.setInput();
            // resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            ui.eventoNaoVisivel();
            // boolean algumVisivel = false;

            // for (int i = 0; i < uis.size; i++) {
            // if (uis.get(i).getVisivel()){
            // algumVisivel = true;
            // break;
            // }
            // }
            //
            // if (!algumVisivel)
            // TarotController.redefineInput();
        }
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
        if (Gdx.app.getType() == ApplicationType.WebGL && uisAbertas.size <= 1)
            return;

        boolean voltar = checaKeyBackPressionado() && permiteRetornar;

        if (uiBackAcionado == null && voltar)
            uiBackAcionado = uisAbertas.get(uisAbertas.size - 1);

        if (uiBackAcionado == null && !voltar)
            return;

        uiBackAcionado.eventoBack();
        uiBackAcionado.setVisivel(false);
        // uisAbertas.removeIndex(uisAbertas.size - 1);

        if (uisAbertas.size > 0) {
            uisAbertas.get(uisAbertas.size - 1).setVisivel(true);
            uisAbertas.get(uisAbertas.size - 1).setInput();
        } else
            Gdx.app.exit();

        uiBackAcionado = null;
    }

    private final boolean checaKeyBackPressionado() {
        boolean pressionadoAgora = Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.ESCAPE);

        if (!backPressionadoAnteriormente) {
            if (pressionadoAgora) {
                backPressionadoAnteriormente = true;
                return true;
            }
        } else if (!pressionadoAgora)
            backPressionadoAnteriormente = false;

        return false;
    }

    public static Skin createSkin(AssetManager assetManager, String uiSkinPath, ArrayMap<String, BitmapFont> fontes, String styleJSONPath){
        Skin uiSkin = new Skin();
        uiSkin.addRegions(assetManager.get(uiSkinPath, TextureAtlas.class));

        // Limpa resource utilizado somente para o load:
        uiSkinPath = null;

        for (int i = 0; i < fontes.size; i++)
            uiSkin.add(fontes.getKeyAt(i), fontes.getValueAt(i), BitmapFont.class);

        // uiSkin.load(Gdx.files.internal("data/ui/ui-main.json"));
        uiSkin.load(Gdx.files.internal(styleJSONPath));

        return uiSkin;
    }
}
