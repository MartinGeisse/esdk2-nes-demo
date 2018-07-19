/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;

/**
 *
 */
public abstract class CpuBusHandler implements BusHandler {

	private final CartridgeFileContents cartridgeFileContents;
	private final byte[] ram = new byte[0x800];

	public CpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		this.cartridgeFileContents = cartridgeFileContents;
	}

	@Override
	public byte read(int address) {
		address = address & 0xffff;
		if (address < 0x2000) {
			// RAM
			return ram[address & 0x07ff];
		} else if (address < 0x4000) {
			// I/O registers
			return readIo2(address);
		} else if (address < 0x4020) {
			// I/O registers
			return readIo4(address);
		} else if (address < 0x8000) {
			// unused (could be used for save RAM and expansion ROM)
			return 0;
		} else {
			// PRG ROM
			return cartridgeFileContents.readPrgRom(address & 0x7fff);
		}
	}

	@Override
	public void write(int address, byte data) {
		address = address & 0xffff;
		if (address < 0x2000) {
			// RAM
			ram[address & 0x07ff] = data;
		} else if (address < 0x4000) {
			// I/O registers
			writeIo2(address, data);
		} else if (address < 0x4020) {
			// I/O registers
			writeIo4(address, data);
		} else {
			// Addresses < 0x8000 are unused (could be used for save RAM and expansion ROM); above that is the
			// PRG ROM (cannot write to that)
		}
	}

	protected abstract byte readIo2(int address);
	protected abstract void writeIo2(int address, byte data);
	protected abstract byte readIo4(int address);
	protected abstract void writeIo4(int address, byte data);

}
