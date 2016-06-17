package net.penciltrick.bee;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class FlappeeBeeGame extends Game {
	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	private final AssetManager assetManager = new AssetManager();

	public AssetManager getAssetManager() { return assetManager; }
}
