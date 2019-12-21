package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.CartridgeFileContents;
import name.martingeisse.esdk2.demo.nes.model.Controller;
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

	private final Apu apu;
	private final InputPorts inputPorts;

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
						// TODO Reading the status register will clear D7 mentioned above and also the address latch used by PPUSCROLL and PPUADDR. It does not clear the sprite 0 hit or overflow bit.
						return (byte) ppu.readStatusRegister();

					case 4:
						return (byte) ppu.readFromSprRam();

					case 7:
						return (byte) ppu.readFromVram();

					default:
						return (byte) ppu.getDynamicallyStoredWriteValue();

				}
			}

			@Override
			protected void writeIo2(int address, byte data) {
				ppu.setDynamicallyStoredWriteValue(data);
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
						// note: we may have to ignore writes during rendering since the original NES does
						// something similar
						ppu.writeToSprRam(data & 0xff);
						break;

					case 5:
						// TODO Changes made to the vertical scroll during rendering will only take effect on the next frame.
						ppu.writeToScrollRegister(data & 0xff);
						break;

					case 6:
						ppu.writeToVramAddressRegister(data & 0xff);
						break;

					case 7:
						ppu.writeToVram(data & 0xff);
						break;


				}
			}

			// TODO button order A, B, Select, Start, Up, Down, Left, Right
			// https://wiki.nesdev.com/w/index.php/Input_devices
			// https://wiki.nesdev.com/w/index.php/Standard_controller
			// https://wiki.nesdev.com/w/index.php/Controller_port_registers
			// https://wiki.nesdev.com/w/index.php/Controller_port_pinout
			// https://wiki.nesdev.com/w/index.php/Controller_Reading

			@Override
			protected byte readIo4(int address) {
				address = address & 0x1f;
				if (address == 0x14) {
					// sprite DMA
					return 0;
				} else if (address == 0x16) {
					return inputPorts.read16();
				} else if (address == 0x17) {
					return inputPorts.read17();
				} else {
					return apu.read(address);
				}
			}

			@Override
			protected void writeIo4(int address, byte data) {
				address = address & 0x1f;
				if (address == 0x14) {
					// TODO sprite DMA
				} else if (address == 0x16) {
					inputPorts.write16(data);
				} else {
					apu.write(address, data);
				}
			}

		};
		this.cpu = new Cpu(cpuBusHandler);

		this.screen = new Screen();
		this.ppuBusHandler = new PpuBusHandler(cartridgeFileContents);
		this.ppu = new Ppu(ppuBusHandler, screen, cpu::fireNmi);

		this.apu = new Apu();
		this.inputPorts = new InputPorts();
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

	public void setController(Controller controller) {
		inputPorts.setController(controller);
	}

}
