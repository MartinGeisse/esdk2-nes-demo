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

	private static final int[] systemPalette = {
		0x757575,
		0x271b8f,
		0x0000ab,
		0x47009f,
		0x8f0077,
		0xab0013,
		0xa70000,
		0x7f0b00,
		0x432f00,
		0x004700,
		0x005100,
		0x003f17,
		0x1b3f5f,
		0x000000,
		0x000000,
		0x000000,

		0xbcbcbc,
		0x0073ef,
		0x233bef,
		0x8300f3,
		0xbf00bf,
		0xe7005b,
		0xdb2b00,
		0xcb4f0f,
		0x8b7300,
		0x009700,
		0x00ab00,
		0x00933b,
		0x00838b,
		0x000000,
		0x000000,
		0x000000,

		0xffffff,
		0x3fbfff,
		0x5f97ff,
		0xa78bfd,
		0xf77bff,
		0xff77b7,
		0xff7763,
		0xff9b3b,
		0xf3bf3f,
		0x83d313,
		0x4fdf4b,
		0x58f898,
		0x00ebdb,
		0x000000,
		0x000000,
		0x000000,

		0xffffff,
		0xabe7ff,
		0xc7d7ff,
		0xd7c8ff,
		0xffc7ff,
		0xffc7db,
		0xffbfb3,
		0xffdbab,
		0xffe7a3,
		0xe3ffa3,
		0xabf3bf,
		0xb3ffcf,
		0x9ffff3,
		0x000000,
		0x000000,
		0x000000
	};
	private final BusHandler busHandler;
	private final Screen screen;

	public Ppu(BusHandler busHandler, Screen screen) {
		if (busHandler == null) {
			throw new IllegalArgumentException("busHandler cannot be null");
		}
		if (screen == null) {
			throw new IllegalArgumentException("screen cannot be null");
		}
		this.busHandler = busHandler;
		this.screen = screen;
	}

	public void draw() {
		for (int tileX = 0; tileX < Constants.NAME_TABLE_WIDTH; tileX++) {
			for (int tileY = 0; tileY < Constants.NAME_TABLE_HEIGHT; tileY++) {

				// test -- should actually read from name table here
				int tileCode = (tileY * 32 + tileX) & 0xFF;

				// test -- should come from a configuration register
				int patternBaseAddress = 0x1000 * ((tileY >> 3) & 1);

				for (int pixelX = 0; pixelX < Constants.TILE_WIDTH; pixelX++) {
					for (int pixelY = 0; pixelY < Constants.TILE_HEIGHT; pixelY++) {

						// read from pattern table
						int patternLine1 = busHandler.read(patternBaseAddress + (tileCode << 4) + (pixelY << 1));
						int patternLine2 = busHandler.read(patternBaseAddress + (tileCode << 4) + (pixelY << 1) + 1);
						int columnMask = (128 >> pixelX);
						int lowTwoColorIndexBits = ((patternLine1 & columnMask) != 0 ? 2 : 0) +
							((patternLine2 & columnMask) != 0 ? 1 : 0);
						// TODO use upper two bits from attribute table
						int localColorIndex = lowTwoColorIndexBits;
						// TODO read color from nackground palette
						int globalColorIndex = localColorIndex;
						int color = systemPalette[globalColorIndex];

						screen.setPixel(tileX * Constants.TILE_WIDTH + pixelX, tileY * Constants.TILE_HEIGHT + pixelY, color);
					}
				}
			}
		}
	}

}
