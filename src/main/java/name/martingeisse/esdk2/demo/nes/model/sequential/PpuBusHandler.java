/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;

/**
 *
 */
public final class PpuBusHandler implements BusHandler {

	private final CartridgeFileContents cartridgeFileContents;
	private final byte[] nametableRam = new byte[0x1000];
	private final byte[] paletteRam = new byte[32];

	public PpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;
	}

	@Override
	public byte read(int address) {
		address = address & 0x3fff;
		if (address < 0x2000) {
			return cartridgeFileContents.readChrRom(address);
		} else if (address < 0x3f00) {
			// TODO mirroring
			return nametableRam[address & 0x03ff];
		} else {
			return paletteRam[address & 31];
		}
	}

	@Override
	public void write(int address, byte data) {
		address = address & 0x3fff;
		if (address < 0x2000) {
			// ignore -- it's a ROM
		} else if (address < 0x3f00) {
			// TODO mirroring
			nametableRam[address & 0x03ff] = data;
		} else {
			paletteRam[address & 31] = data;
		}
	}

}
