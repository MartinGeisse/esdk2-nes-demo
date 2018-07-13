package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 *
 */
public class SequentialNesModel {

	private final CartridgeFileContents cartridgeFileContents;
	private final Screen screen = new Screen();

	public SequentialNesModel(CartridgeFileContents cartridgeFileContents) {
		this.cartridgeFileContents = cartridgeFileContents;
	}

	public void step() {

	}

	public void render() {
		screen.render();
	}

}
