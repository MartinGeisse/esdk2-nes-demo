package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.Constants;

/**
 *
 */
public final class Cpu {

	private final BusHandler busHandler;

	private byte a, x, y, p;
	private short pc, sp;

	public Cpu(BusHandler busHandler) {
		this.busHandler = busHandler;
		reset();
	}

	public void reset() {
		a = x = y = 0;
		pc = read16(Constants.RESET_VECTOR_LOCATION);
		sp = (byte)0xfd;
		p = 0x34;
	}

	public void fireNmi() {
		// TODO
	}

	public void fireIrq() {
		// TODO
	}


	public void step() {
		int opcode = fetch();
		System.out.println(Integer.toHexString(opcode & 0xff));
		switch (opcode) {
			// TODO
		}
	}

	private byte fetch() {
		byte data = busHandler.read(pc);
		pc++;
		return data;
	}

	private short read16(int address) {
		int lowByte = busHandler.read(address);
		int highByte = busHandler.read(address + 1);
		return (short)((lowByte & 0xff) + (highByte & 0xff) << 8);
	}

}
