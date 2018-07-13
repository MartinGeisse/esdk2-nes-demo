/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;

/**
 *
 */
public class CpuBusHandler implements BusHandler {

	private final CartridgeFileContents cartridgeFileContents;

	public CpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		this.cartridgeFileContents = cartridgeFileContents;
	}

	@Override
	public byte read(int address) {
		address = address & 0xffff;
		if (address < 0x8000) {
			return 0; // TODO
		} else {
			return cartridgeFileContents.readPrgRom(address & 0x7fff);
		}
	}

	@Override
	public void write(int address, byte data) {
		// TODO
	}

}
