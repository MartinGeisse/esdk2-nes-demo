/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 * TODO "reading PPUStatus -- register 2 -- resets the address latch"
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
	private int dynamicallyStoredWriteValue = 0;
	private int controlRegister = 0;
	private int maskRegister = 0;
	private int sprRamAddressRegister = 0;
	private int scrollXRegister = 0;
	private int scrollYRegister = 0;
	private int vramAddressRegister = 0;
	private byte[] sprRam = new byte[256];
	private boolean writeToggle16 = false;

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
		int patternTableBaseAddress = (controlRegister & 16) == 0 ? 0x0000 : 0x1000;
		int attributeTableBaseAddress = patternTableBaseAddress + 960;
		for (int tileX = 0; tileX < Constants.NAME_TABLE_WIDTH; tileX++) {

			// read tile code from the name table
			int tileAddress = 0x2000 + (tileY * 32 + tileX);
			int tileCode = busHandler.read(tileAddress) & 0xff;

			// read pattern from the pattern table TODO patterns are corrupted
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

	public int getDynamicallyStoredWriteValue() {
		return dynamicallyStoredWriteValue;
	}

	public void setDynamicallyStoredWriteValue(int dynamicallyStoredWriteValue) {
		this.dynamicallyStoredWriteValue = dynamicallyStoredWriteValue;
	}

	public void setControlRegister(int controlRegister) {
		// TODO the nametable bits found here are the highest order scroll bits
		this.controlRegister = controlRegister & 0xff;
	}

	public void setMaskRegister(int maskRegister) {
		this.maskRegister = maskRegister & 0xff;
	}

	public int readStatusRegister() {
		writeToggle16 = false;
		int result = (dynamicallyStoredWriteValue & 31) | (vblank ? 128 : 0);
		vblank = false;
		return result;
	}

	public void setSprRamAddressRegister(int sprRamAddressRegister) {
		this.sprRamAddressRegister = sprRamAddressRegister & 0xff;
	}

	public void writeToSprRam(int data) {
		sprRam[sprRamAddressRegister] = (byte)data;
		sprRamAddressRegister = (sprRamAddressRegister + 1) & 0xff;
	}

	public int readFromSprRam() {
		// this does not increment the address
		return sprRam[sprRamAddressRegister] & 0xff;
	}

	public void writeToScrollRegister(int value) {
		if (writeToggle16) {
			scrollYRegister = value & 0xff;
		} else {
			scrollXRegister = value & 0xff;
		}
		writeToggle16 = !writeToggle16;
	}

	public void writeToVramAddressRegister(int value) {
		if (writeToggle16) {
			// write to low byte
			vramAddressRegister = (vramAddressRegister & 0xff00) + (value & 0xff);
		} else {
			// write to high byte
			vramAddressRegister = (vramAddressRegister & 0xff) + ((value & 0xff) << 8);
		}
		writeToggle16 = !writeToggle16;
	}

	public void writeToVram(int data) {
		busHandler.write(vramAddressRegister, (byte)data);
		incrementVramAddress();
	}

	public int readFromVram() {
		int result = busHandler.read(vramAddressRegister);
		incrementVramAddress();
		return result;
	}

	private void incrementVramAddress() {
		int increment = (controlRegister & 4) == 0 ? 1 : 32;
		vramAddressRegister = (vramAddressRegister + increment) & 0xffff;
	}

}
