/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.model.Constants;
import name.martingeisse.esdk2.demo.nes.model.sequential.SequentialNesModel;
import name.martingeisse.esdk2.demo.nes.system.Launcher;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.io.File;

/**
 *
 */
public class TestMain {

	public static void main(String[] args) throws Exception {

		CartridgeFileContents cartridgeFileContents = new CartridgeFileContents(new File("/Users/martin/test.nes"));
		for (int i = 0; i < 32768; i++) {
			if (i % 8 == 0) {
				String s = "0000" + Integer.toHexString(i);
				System.out.println();
				System.out.print(s.substring(s.length() - 4) + ": ");
			}
			{
				String s = "00" + Integer.toHexString(cartridgeFileContents.readPrgRom(i));
				System.out.print(s.substring(s.length() - 2) + " ");
			}
		}
		System.out.println();

	}

}
