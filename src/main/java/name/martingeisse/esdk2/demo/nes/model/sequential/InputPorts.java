/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import org.lwjgl.input.Keyboard;

/**
 *
 */
public final class InputPorts {

	private boolean strobe;
	private int latchedController1Data;

	public void write16(byte data) {
		if (strobe) {
			latchController1Data();
		}
		strobe = (data & 1) != 0;
	}

	public byte read16() {
		if (strobe) {
			latchController1Data();
			return (byte) (latchedController1Data & 1);
		} else {
			byte result = (byte) (latchedController1Data & 1);
			latchedController1Data >>>= 1;
			return result;
		}
	}

	private void latchController1Data() {
		latchedController1Data = 0;
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_UP) ? 16 : 0);
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_DOWN) ? 32 : 0);
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_LEFT) ? 64 : 0);
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) ? 128 : 0);
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_X) ? 1 : 0); // A
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_C) ? 2 : 0); // B
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_RETURN) ? 8 : 0); // start
		latchedController1Data |= (Keyboard.isKeyDown(Keyboard.KEY_SPACE) ? 4 : 0); // select
	}

	public byte read17() {
		// no controller 2 attached
		return 0;
	}

}
