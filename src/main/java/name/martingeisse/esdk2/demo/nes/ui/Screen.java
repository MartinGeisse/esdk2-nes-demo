/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.ui;

import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.system.GlUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 *
 */
public class Screen {

	private final IntBuffer buffer = ByteBuffer.allocateDirect(Constants.SCREEN_WIDTH * Constants.SCREEN_HEIGHT * 4).asIntBuffer();

	public void setPixel(int x, int y, int rgb) {
		if (x < 0 || x >= Constants.SCREEN_WIDTH || y < 0 || y >= Constants.SCREEN_HEIGHT) {
			throw new IllegalArgumentException("position outside screen bounds: " + x + ", " + y);
		}
		buffer.put(y * Constants.SCREEN_WIDTH + x, rgb);
	}

	public void render() {
		GL14.glWindowPos2i(0, 0);
		GL11.glDrawPixels(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8, buffer);
		GlUtil.checkError();
	}

}
