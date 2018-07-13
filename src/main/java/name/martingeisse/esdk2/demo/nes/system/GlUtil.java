package name.martingeisse.esdk2.demo.nes.system;

import org.lwjgl.opengl.GL11;

/**
 *
 */
public final class GlUtil {

	// prevent instantiation
	private GlUtil() {
	}

	public static void checkError() {
		int error = GL11.glGetError();
		if (error != 0) {
			throw new RuntimeException("GL error: " + error + " (0x" + Integer.toHexString(error) + ")");
		}
	}

}
