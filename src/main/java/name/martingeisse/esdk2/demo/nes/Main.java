/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.model.sequential.SequentialNesModel;
import name.martingeisse.esdk2.demo.nes.system.Launcher;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.File;

/**
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Launcher launcher = new Launcher(args);
		launcher.setScreenWidth(Constants.SCREEN_WIDTH + 100);
		launcher.setScreenHeight(Constants.SCREEN_HEIGHT + 100);
		launcher.startup();

		CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("~/test.nes"));
		SequentialNesModel model = new SequentialNesModel(cartridgeFileContents);

		while (true) {

			// draw
			model.render();

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
			for (int i = 0; i < 10_000; i++) {
				model.step();
			}

		}

		launcher.shutdown();
		System.exit(0);

	}

}
