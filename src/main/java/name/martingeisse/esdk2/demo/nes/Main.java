/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes;

import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.system.Launcher;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

/**
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {

		Launcher launcher = new Launcher();
		launcher.setWidth(Constants.SCREEN_WIDTH);
		launcher.setHeight(Constants.SCREEN_HEIGHT);
		launcher.startup();
		long window = launcher.getWindow();

//		CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("/Users/martin/test.nes"));
//		// CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("resource/roms/nestest.nes"));
//		final SequentialNesModel model = new SequentialNesModel(cartridgeFileContents);
//
//		new Thread(() -> {
//			SequentialNesModel model2 = model;
//			try {
//				Thread.sleep(2_000);
//				System.out.println();
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//		}).start();
//


		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(win, true);
			}
		});
		while (!glfwWindowShouldClose(window)) {

			// OS-related housekeeping
//			Display.update();
//			Display.processMessages();
//			Mouse.poll();
//			Keyboard.poll();

			// allow to exit by pressing escape
//			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//				break;
//			}

			// simulation
//			for (int i = 0; i < 100; i++) {
//				model.frame();
//			}



			glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwSwapBuffers(window);
			glfwPollEvents();
		}

		launcher.shutdown();
//		System.exit(0);

	}

}
