/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.model.SimpleController;
import name.martingeisse.esdk2.demo.nes.model.sequential.SequentialNesModel;
import name.martingeisse.esdk2.demo.nes.system.Launcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static org.lwjgl.glfw.GLFW.*;

/**
 * TODO the current problem lies in "sprite 0 hit detection" which I remember is an ugly hack that is not yet supported
 */
public class Main {

	public static void main(String[] args) throws Exception {
		try (FileOutputStream fileOutputStream = new FileOutputStream("log.txt")) {
			System.setOut(new PrintStream(fileOutputStream));

			//  startup
			Launcher launcher = new Launcher();
			launcher.startup();
			long window = launcher.getWindow();

			// load game and create simulation model
			// CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("resource/roms/nestest.nes"));
			CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("/home/martin/test.nes"));
			final SequentialNesModel model = new SequentialNesModel(cartridgeFileContents);

			// set up game controller
			SimpleController controller = new SimpleController();
			model.setController(controller);

			// exit on escape key
			glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
				boolean value;
				if (action == GLFW_PRESS) {
					value = true;
				} else if (action == GLFW_RELEASE) {
					value = false;
				} else {
					return;
				}
				switch (key) {

					case GLFW_KEY_ESCAPE:
						glfwSetWindowShouldClose(win, true);
						break;

					case GLFW_KEY_UP:
						controller.setUpPressed(value);
						break;

					case GLFW_KEY_DOWN:
						controller.setDownPressed(value);
						break;

					case GLFW_KEY_LEFT:
						controller.setLeftPressed(value);
						break;

					case GLFW_KEY_RIGHT:
						controller.setRightPressed(value);
						break;

					case GLFW_KEY_C:
						controller.setAPressed(value);
						break;

					case GLFW_KEY_X:
						controller.setBPressed(value);
						break;

					case GLFW_KEY_ENTER:
						controller.setStartPressed(value);
						break;

					case GLFW_KEY_SPACE:
						controller.setSelectPressed(value);
						break;

				}
			});

			// main loop
			while (!glfwWindowShouldClose(window)) {
				for (int i = 0; i < 100; i++) {
					model.frame();
				}
				glfwSwapBuffers(window);
				glfwPollEvents();
			}

			// shutdown
			launcher.shutdown();

		}
	}

}
