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
	private final Runnable vblankEdgeCallback;
	private boolean vblank = false;
	private int previousWriteValue = 0;
	private int controlRegister = 0;
	private int maskRegister = 0;
	private int sprRamAddressRegister = 0;
	private int backgroundScrollRegister = 0;
	private int vramAddressRegister = 0;
	private int vramReadData = 0;
	private byte[] sprRam = new byte[256];

	public Ppu(BusHandler busHandler, Screen screen, Runnable vblankEdgeCallback) {
		if (busHandler == null) {
			throw new IllegalArgumentException("busHandler cannot be null");
		}
		if (screen == null) {
			throw new IllegalArgumentException("screen cannot be null");
		}
		this.busHandler = busHandler;
		this.screen = screen;
		this.vblankEdgeCallback = vblankEdgeCallback;
	}

	public void drawRow(int row) {
		int tileY = row >> 3;
		int pixelY = row & 7;
		int patternTableBaseAddress = 0; // TODO should come from a configuration register
		int attributeTableBaseAddress = patternTableBaseAddress + 960;
		for (int tileX = 0; tileX < Constants.NAME_TABLE_WIDTH; tileX++) {

			// read tile code from the name table
			int tileAddress = 0x2000 + (tileY * 32 + tileX) & 0xff;
			int tileCode = busHandler.read(tileAddress);

			// read pattern from the pattern table
			int patternLine1 = busHandler.read(patternTableBaseAddress + (tileCode << 4) + (pixelY << 1));
			int patternLine2 = busHandler.read(patternTableBaseAddress + (tileCode << 4) + (pixelY << 1) + 1);

			// read attributes from the attribute table
			int attributeByte = busHandler.read(attributeTableBaseAddress + ((tileY >> 2) << 3) + tileX >> 2);
			int shiftAmount = ((tileX & 2) == 0 ? 0 : 2) + ((tileY & 2) == 0 ? 0 : 4);
			int upperTwoColorIndexBitsPreshifted = ((attributeByte >> shiftAmount) & 3) << 2;

			for (int pixelX = 0; pixelX < Constants.TILE_WIDTH; pixelX++) {

				int columnMask = (128 >> pixelX);
				int lowerTwoColorIndexBits = ((patternLine1 & columnMask) != 0 ? 2 : 0) +
					((patternLine2 & columnMask) != 0 ? 1 : 0);

				// determine color
				int localColorIndex = lowerTwoColorIndexBits == 0 ? 0 : (upperTwoColorIndexBitsPreshifted | lowerTwoColorIndexBits);
				int globalColorIndex = busHandler.read(0x3f00 + localColorIndex);
				int color = systemPalette[globalColorIndex];

				screen.setPixel(tileX * Constants.TILE_WIDTH + pixelX, tileY * Constants.TILE_HEIGHT + pixelY, color);

			}
		}
	}

	public void setVblank(boolean vblank) {
		if (vblank && !this.vblank) {
			if ((controlRegister & 128) != 0 && vblankEdgeCallback != null) {
				vblankEdgeCallback.run();
			}
		}
		this.vblank = vblank;
	}

	public void setControlRegister(int controlRegister) {
		this.previousWriteValue = this.controlRegister = controlRegister & 0xff;
	}

	public void setMaskRegister(int maskRegister) {
		this.previousWriteValue = this.maskRegister = maskRegister & 0xff;
	}

	public int getStatusRegister() {
		return (previousWriteValue & 31) | (vblank ? 128 : 0);
	}

	public void setSprRamAddressRegister(int sprRamAddressRegister) {
		this.previousWriteValue = this.sprRamAddressRegister = sprRamAddressRegister & 0xff;
	}

	public void writeToSprRam(int data) {
		sprRam[sprRamAddressRegister] = (byte)data;
		sprRamAddressRegister = (sprRamAddressRegister + 1) & 0xff;
	}

	public void setBackgroundScrollRegister(int backgroundScrollRegister) {
		this.previousWriteValue = this.backgroundScrollRegister = backgroundScrollRegister & 0xff;
	}

	public void writeToVramAddressRegister(int value) {
		// my best guess how the VRAM address register handles two writes to build a 16-bit address
		this.previousWriteValue = value & 0xff;
		vramAddressRegister = (vramAddressRegister & 0xff) << 8 | value & 0xff;
	}

	public void writeToVram(int data) {
		busHandler.write(vramAddressRegister, (byte)data);
		int increment = (controlRegister & 4) == 0 ? 1 : 32;
		vramAddressRegister = (vramAddressRegister + increment) & 0xffff;
	}

	public int readFromVram() {
		// my best guess why the first byte read contains garbage
		int result = vramReadData;
		vramReadData = busHandler.read(vramAddressRegister);
		return result;
	}

}
