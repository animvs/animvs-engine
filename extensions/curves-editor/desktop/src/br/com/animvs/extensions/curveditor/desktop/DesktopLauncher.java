package br.com.animvs.extensions.curveditor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import br.com.animvs.extensions.curveditor.CurveditorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "ANIMVS - curveditor";
        config.width = 1024;
        config.height = 768;

        config.backgroundFPS = 30;
        config.foregroundFPS = 30;

        config.resizable = false;

		new LwjglApplication(new CurveditorGame(), config);
	}
}
