/*
 * Copyright (c) 2018 Martin Geisse
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk2.demo.nes.system;

import java.lang.reflect.Field;

/**
 * Helper class that extracts LWJGL native libraries to a temporary folder
 * because Java is too stupid to load native libraries from inside a
 * JAR file.
 */
public final class LwjglNativeLibraryHelper {

	/**
	 * Extracts LWJGL libraries to a folder and 
	 * @throws Exception on errors
	 */
	public static void prepareNativeLibraries() throws Exception {
		System.setProperty("java.library.path", "native-libs/" + OperatingSystemSelector.getHostOs().getNativeLibraryPath());
		final Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
		fieldSysPath.setAccessible(true);
		fieldSysPath.set(null, null);
	}

}
