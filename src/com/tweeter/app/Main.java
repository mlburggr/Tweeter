package com.tweeter.app;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
	public static void main(String[] args) {
		Game game = new Game("Tweeter");
		try {
			AppGameContainer app = new AppGameContainer(game);
			app.setVSync(false);
			app.setShowFPS(true);
			app.setDisplayMode(500, 500, false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
