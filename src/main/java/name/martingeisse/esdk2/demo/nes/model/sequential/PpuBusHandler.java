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
	private int readDataRegister = 0;

	public PpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;
	}

	@Override
	public byte read(int address) {
		address = address & 0x3fff;

		// TODO the following is not correctly emulated at the momoent:
		// Reading palette data from $3F00-$3FFF works differently. The palette data is placed immediately on the
		// data bus, and hence no dummy read is required. Reading the palettes still updates the internal buffer
		// though, but the data placed in it is the mirrored nametable data that would appear "underneath" the
		// palette. (Checking the PPU memory map should make this clearer.)

		int previousReadDataRegister = readDataRegister;
		if (address < 0x2000) {
			readDataRegister = cartridgeFileContents.readChrRom(address);
		} else {
			// TODO mirroring
			readDataRegister = nametableRam[address & 0x03ff];
		}

		if (address < 0x3f00) {
			return (byte) previousReadDataRegister;
		} else {
			return paletteRam[address & 31];
		}

	}

	@Override
	public void write(int address, byte data) {


		System.out.println("PPU write: address = " + toHex(address, 4) + ", data = " + toHex(data, 2));

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
