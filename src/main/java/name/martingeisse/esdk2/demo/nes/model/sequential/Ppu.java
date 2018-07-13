/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 *
 */
public class Ppu {

	private final BusHandler busHandler;
	private final Screen screen;

	public Ppu(BusHandler busHandler, Screen screen) {
		this.busHandler = busHandler;
		this.screen = screen;
	}

	public void draw() {
		for (int tileX = 0; tileX < Constants.NAME_TABLE_WIDTH; tileX++) {
			for (int tileY = 0; tileY < Constants.NAME_TABLE_HEIGHT; tileY++) {
				for (int pixelX = 0; pixelX < Constants.TILE_WIDTH; pixelX++) {
					for (int pixelY = 0; pixelY < Constants.TILE_HEIGHT; pixelY++) {
						int color = 0x00ff0000;
						screen.setPixel(tileX * Constants.TILE_WIDTH + pixelX, tileY * Constants.TILE_HEIGHT + pixelY, color);
					}
				}
			}
		}
	}

}
