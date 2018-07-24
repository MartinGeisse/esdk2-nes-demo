/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

/**
 *
 */
public final class Apu {

	public void write(int registerIndex, byte data) {
		// TODO
	}

	public byte read(int registerIndex) {
		if (registerIndex != 15) {
			return 0;
		}

		// TODO
		return 0;
	}

}
