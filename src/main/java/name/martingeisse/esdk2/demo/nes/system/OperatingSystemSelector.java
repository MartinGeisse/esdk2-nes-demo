/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.system;

/**
 * Helps managing OS-specific code.
 */
public enum OperatingSystemSelector {

	/**
	 * All Windows OSes
	 */
	WINDOWS("windows"),

	/**
	 * All Linux OSes
	 */
	LINUX("linux"),

	/**
	 * Mac OS X
	 */
	MAC("macosx");

	/**
	 * the hostOs
	 */
	private static OperatingSystemSelector hostOs = null;

	/**
	 * static initializer
	 */
	static {
		final String name = System.getProperty("os.name").toLowerCase();
		if (name.indexOf("windows") >= 0) {
			hostOs = WINDOWS;
		} else if (name.indexOf("linux") >= 0) {
			hostOs = LINUX;
		} else if (name.indexOf("mac os x") >= 0) {
			hostOs = MAC;
		} else {
			throw new RuntimeException("operating system not recognized, os.name: \"" + System.getProperty("os.name") + "\"");
		}
	}

	/**
	 * Getter method for the hostOs.
	 * @return the hostOs
	 */
	public static OperatingSystemSelector getHostOs() {
		return hostOs;
	}

	/**
	 * the nativeLibraryPath
	 */
	private final String nativeLibraryPath;

	/**
	 * Constructor.
	 * @param nativeLibraryPath
	 */
	private OperatingSystemSelector(final String nativeLibraryPath) {
		this.nativeLibraryPath = nativeLibraryPath;
	}

	/**
	 * Returns the path to the folder that contains the native libraries
	 * for this OS.
	 * @return the path
	 */
	public String getNativeLibraryPath() {
		return nativeLibraryPath;
	}
	
}
