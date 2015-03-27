package br.com.animvs.extensions.curveditor.controller;

import com.badlogic.gdx.utils.Disposable;

/**
 * Created by DALDEGAN on 27/03/2015.
 */
public abstract class BaseController implements Disposable {
    private GameController controller;

    protected final GameController getController() {
        return controller;
    }

    public BaseController(GameController controller) {
        this.controller = controller;
    }

    protected void initialize() {

    }

    @Override
    public void dispose() {

    }
}
