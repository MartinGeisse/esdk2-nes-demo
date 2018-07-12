/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.system;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Launches the game system.
 */
public class Launcher {

	/**
	 * the screenWidth
	 */
	private int screenWidth = 800;

	/**
	 * the screenHeight
	 */
	private int screenHeight = 600;

	/**
	 * the fullscreen
	 */
	private boolean fullscreen = false;

	/**
	 * Constructor.
	 *
	 * @param args command-line arguments
	 */
	public Launcher(String[] args) {
	}

	/**
	 * Getter method for the screenWidth.
	 *
	 * @return the screenWidth
	 */
	public final int getScreenWidth() {
		return screenWidth;
	}

	/**
	 * Setter method for the screenWidth.
	 *
	 * @param screenWidth the screenWidth to set
	 */
	public final void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	/**
	 * Getter method for the screenHeight.
	 *
	 * @return the screenHeight
	 */
	public final int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * Setter method for the screenHeight.
	 *
	 * @param screenHeight the screenHeight to set
	 */
	public final void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	/**
	 * Getter method for the fullscreen.
	 *
	 * @return the fullscreen
	 */
	public final boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Setter method for the fullscreen.
	 *
	 * @param fullscreen the fullscreen to set
	 */
	public final void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	/**
	 * Starts the game system.
	 *
	 * @throws Exception on errors during initialization
	 */
	public void startup() throws Exception {

		// include LWJGL native libraries
		LwjglNativeLibraryHelper.prepareNativeLibraries();

		// configure the display
		DisplayMode bestMode = null;
		int bestModeFrequency = -1;
		for (DisplayMode mode : Display.getAvailableDisplayModes()) {
			if (mode.getWidth() == screenWidth && mode.getHeight() == screenHeight && (mode.isFullscreenCapable() || !fullscreen)) {
				if (mode.getFrequency() > bestModeFrequency) {
					bestMode = mode;
					bestModeFrequency = mode.getFrequency();
				}
			}
		}
		if (bestMode == null) {
			bestMode = new DisplayMode(screenWidth, screenHeight);
		}
		Display.setDisplayMode(bestMode);
		if (fullscreen) {
			Display.setFullscreen(true);
		}
		Display.create(new PixelFormat(0, 24, 0));

		// initialize the keyboard
		Keyboard.create();
		Keyboard.poll();

	}

	/**
	 * Shuts down the game system.
	 */
	public void shutdown() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

}
