/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.Controller;

/**
 *
 */
public final class InputPorts {

	private Controller controller;
	private boolean strobe;
	private int latchedController1Data;

	public void setController(Controller controller) {
		this.controller = controller;
	}

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
		if (controller != null) {
			latchedController1Data |= (controller.isUpPressed() ? 16 : 0);
			latchedController1Data |= (controller.isDownPressed() ? 32 : 0);
			latchedController1Data |= (controller.isLeftPressed() ? 64 : 0);
			latchedController1Data |= (controller.isRightPressed() ? 128 : 0);
			latchedController1Data |= (controller.isAPressed() ? 1 : 0);
			latchedController1Data |= (controller.isBPressed() ? 2 : 0);
			latchedController1Data |= (controller.isStartPressed() ? 8 : 0);
			latchedController1Data |= (controller.isSelectPressed() ? 4 : 0);
		}
	}

	public byte read17() {
		// no controller 2 attached
		return 0;
	}

}
