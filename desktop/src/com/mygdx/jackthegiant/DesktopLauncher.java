package com.mygdx.jackthegiant;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.jackthegiant.GameMain;

import helpers.GameInfo;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Jack The Giant");
		config.setWindowedMode(GameInfo.WIDTH, GameInfo.HEIGHT);

		new Lwjgl3Application(new GameMain(), config);
	}
}
