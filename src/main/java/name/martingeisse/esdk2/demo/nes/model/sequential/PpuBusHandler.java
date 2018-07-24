/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;

/**
 *
 */
public final class PpuBusHandler {

	private final CartridgeFileContents cartridgeFileContents;
	private final byte[] nametableRam = new byte[0x1000];
	private final byte[] paletteRam = new byte[32];
	private int readDataRegister = 0;

	public PpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;
	}

	public byte read(int address, boolean latched) {
		address = address & 0x3fff;

		int previousReadDataRegister = readDataRegister;
		if (address < 0x2000) {
			readDataRegister = cartridgeFileContents.readChrRom(address);
		} else {
			// TODO mirroring
			readDataRegister = nametableRam[address & 0x03ff];
		}

		if (address < 0x3f00) {
			if (latched) {
				return (byte) previousReadDataRegister;
			} else {
				return (byte) readDataRegister;
			}
		} else {
			return paletteRam[address & 31];
		}

	}

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

	private String toHex(int value, int digits) {
		String s = "00000000" + Integer.toHexString(value);
		return s.substring(s.length() - digits);
	}

}
