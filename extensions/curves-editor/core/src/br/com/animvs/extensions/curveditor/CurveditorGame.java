package br.com.animvs.extensions.curveditor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import br.com.animvs.extensions.curveditor.controller.GameController;

public class CurveditorGame extends ApplicationAdapter {
    private GameController controller;
	
	@Override
	public void create () {
        controller = new GameController();
	}

	@Override
	public void render () {
        controller.render();
	}

    @Override
    public void dispose() {
        controller.dispose();
    }
}
