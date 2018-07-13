package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 *
 */
public class SequentialNesModel {

	private final CartridgeFileContents cartridgeFileContents;
	private final Screen screen = new Screen();
	private final Ppu ppu = new Ppu(null, screen);

	public SequentialNesModel(CartridgeFileContents cartridgeFileContents) {
		this.cartridgeFileContents = cartridgeFileContents;
	}

	public void step() {

	}

	public void render() {
		ppu.draw();
		screen.render();
	}

}
