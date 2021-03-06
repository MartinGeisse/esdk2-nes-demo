/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.model;

/**
 *
 */
public final class Constants {

	// prevent instantiation
	private Constants() {
	}

	public static final int NAME_TABLE_WIDTH = 32;
	public static final int NAME_TABLE_HEIGHT = 30;
	public static final int TILE_WIDTH = 8;
	public static final int TILE_HEIGHT = 8;
	public static final int SCREEN_WIDTH = NAME_TABLE_WIDTH * TILE_WIDTH;
	public static final int SCREEN_HEIGHT = NAME_TABLE_HEIGHT * TILE_HEIGHT;
	public static final int SCREEN_ZOOM = 2;

	public static final int NMI_VECTOR_LOCATION = 0xfffa;
	public static final int RESET_VECTOR_LOCATION = 0xfffc;
	public static final int INTERRUPT_VECTOR_LOCATION = 0xfffe;

}
