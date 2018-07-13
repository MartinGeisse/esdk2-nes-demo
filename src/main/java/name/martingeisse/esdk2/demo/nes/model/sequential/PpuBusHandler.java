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

	public PpuBusHandler(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;
	}

	@Override
	public byte read(int address) {
		if (address < 0x2000) {
			return cartridgeFileContents.readChrRom(address);
		} else {
			// TODO
			return 0;
		}
	}

	@Override
	public void write(int address, byte data) {
		// TODO
	}

}
