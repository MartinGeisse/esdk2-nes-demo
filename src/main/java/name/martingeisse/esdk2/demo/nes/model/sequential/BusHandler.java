package name.martingeisse.esdk2.demo.nes.model.sequential;

/**
 *
 */
public interface BusHandler {

	byte read(int address);

	void write(int address, byte data);

}
