/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import java.nio.IntBuffer;

/**
 *
 */
public class Screen {

	private final IntBuffer buffer = IntBuffer.allocate(256 * 240);

	public void setPixel(int x, int y, int rgb) {
		if (x < 0 || x >= 256 || y < 0 || y >= 240) {
			throw new IllegalArgumentException("position outside screen bounds: " + x + ", " + y);
		}
		buffer.put(y * 256 + x, rgb);
	}

	public void render() {
		GL14.glWindowPos2i(0, 0);
		GL11.glDrawPixels(256, 240, GL11.GL_RGB, GL12.GL_UNSIGNED_INT_8_8_8_8, buffer);
	}

}
