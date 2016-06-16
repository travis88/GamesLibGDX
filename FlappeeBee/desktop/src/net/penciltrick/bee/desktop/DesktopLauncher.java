package net.penciltrick.bee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.penciltrick.bee.FlappeeBeeGame;
import net.penciltrick.bee.GameScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = GameScreen.WORLD_HEIGHT;
		config.width = GameScreen.WORLD_WIDTH;
		config.title = "FlappeBee";
		new LwjglApplication(new FlappeeBeeGame(), config);
	}
}
