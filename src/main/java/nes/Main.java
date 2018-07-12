/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes;

import name.martingeisse.esdk2.demo.nes.system.Launcher;
import name.martingeisse.esdk2.demo.nes.ui.Screen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Launcher launcher = new Launcher(args);
		launcher.setScreenWidth(256);
		launcher.setScreenWidth(240);
		launcher.startup();

		Screen screen = new Screen();
		screen.setPixel(0, 0, 0xffff0000);

		while (true) {

			// draw
			// TODO draw
			screen.render();

			// OS-related housekeeping
			GL11.glFlush();
			Display.update();
			Display.processMessages();
			Mouse.poll();
			Keyboard.poll();

			// allow to exit by pressing escape
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}

			// game logic
			// TODO game logic

		}

		launcher.shutdown();
		System.exit(0);

	}

}
