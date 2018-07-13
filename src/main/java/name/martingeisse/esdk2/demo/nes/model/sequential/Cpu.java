package name.martingeisse.esdk2.demo.nes.model.sequential;

/**
 *
 */
public final class Cpu {

	private final BusHandler busHandler;

	private byte a, x, y, pc, sp, p;

	public Cpu(BusHandler busHandler) {
		this.busHandler = busHandler;
		reset();
	}

	public void reset() {
		a = x = y = 0;
		pc = busHandler.read(0xfffc);
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
		switch (fetch()) {
			// TODO
		}
	}

	private byte fetch() {
		byte data = busHandler.read(pc);
		pc++;
		return data;
	}

}
