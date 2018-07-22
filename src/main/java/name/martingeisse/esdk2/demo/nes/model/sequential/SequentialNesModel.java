package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.ui.Screen;

/**
 *
 */
public class SequentialNesModel {

	private final CartridgeFileContents cartridgeFileContents;

	private final CpuBusHandler cpuBusHandler;
	private final Cpu cpu;

	private final Screen screen;
	private final PpuBusHandler ppuBusHandler;
	private final Ppu ppu;

	public SequentialNesModel(CartridgeFileContents cartridgeFileContents) {
		if (cartridgeFileContents == null) {
			throw new IllegalArgumentException("cartridgeFileContents cannot be null");
		}
		this.cartridgeFileContents = cartridgeFileContents;

		this.cpuBusHandler = new CpuBusHandler(cartridgeFileContents) {

			@Override
			protected byte readIo2(int address) {
				switch (address & 7) {

					case 2:
						return (byte) ppu.getStatusRegister();

					case 4:
						return (byte) ppu.readFromSprRam();

					case 7:
						return (byte) ppu.readFromVram();

					default:
						return 0;

				}
			}

			@Override
			protected void writeIo2(int address, byte data) {
				switch (address & 7) {

					case 0:
						ppu.setControlRegister(data & 0xff);
						break;

					case 1:
						ppu.setMaskRegister(data & 0xff);
						break;

					case 3:
						ppu.setSprRamAddressRegister(data & 0xff);
						break;

					case 4:
						ppu.writeToSprRam(data & 0xff);
						break;

					case 5:
						// TODO scroll
						break;

					case 6:
						ppu.writeToVramAddressRegister(data & 0xff);
						break;

					case 7:
						ppu.writeToVram(data & 0xff);
						break;


				}
			}

			@Override
			protected byte readIo4(int address) {
				return 0;
			}

			@Override
			protected void writeIo4(int address, byte data) {

			}

		};
		this.cpu = new Cpu(cpuBusHandler);

		this.screen = new Screen();
		this.ppuBusHandler = new PpuBusHandler(cartridgeFileContents);
		this.ppu = new Ppu(ppuBusHandler, screen, cpu::fireNmi);
	}

	public void frame() {

		// simulate 240 displayed rows and draw them
		for (int y = 0; y < 240; y++) {
			row();
			ppu.drawRow(y);
		}

		// simulate 22 v-blank lines and one pre-render line, drawing nothing
		ppu.setVblank(true);
		rows(22);
		ppu.setVblank(false);
		row();

		// display the rendered screen
		screen.render();

	}

	private void rows(int n) {
		while (n > 0) {
			row();
			n--;
		}
	}

	private void row() {
		for (int i = 0; i < 100; i++) {
			cpu.step();
		}
	}

}
