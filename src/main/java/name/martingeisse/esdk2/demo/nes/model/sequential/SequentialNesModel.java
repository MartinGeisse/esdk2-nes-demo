package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 *
 */
public class SequentialNesModel {

	private final CartridgeFileContents cartridgeFileContents;
	private final Screen screen;
	private final PpuBusHandler ppuBusHandler;
	private final Ppu ppu;

	public SequentialNesModel(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;
		this.screen = new Screen();
		this.ppuBusHandler = new PpuBusHandler(cartridgeFileContents);
		this.ppu = new Ppu(ppuBusHandler, screen);
	}

	public void step() {
	}

	public void render() {
		ppu.draw();
		screen.render();
	}

}
