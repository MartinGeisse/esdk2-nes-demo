package name.martingeisse.esdk2.demo.nes.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class only supports the NROM "mapper" (which actually means no mapper at all), using exactly 2 PRG-ROM banks
 * (using the whole PRG ROM address space without any mapping), exactly 1 CHR-ROM bank (using exactly the address space
 * for pattern tables, without any mapping and disabling the internal pattern RAM), and no save-RAM.
 */
public final class CartridgeFileContents {

	private final byte[] prgRom;
	private final byte[] chrRom;

	public CartridgeFileContents(File cartridgeFile) throws IOException, CartridgeFileFormatException {
		try (FileInputStream in = new FileInputStream(cartridgeFile)) {

			// read header
			if (in.read() != 'N' || in.read() != 'E' || in.read() != 'S' || in.read() != 0x1a) {
				throw new CartridgeFileFormatException("invalid magic number");
			}
			int numberOfPrgRomBanks = in.read(); // 16kB per bank
			int numberOfChrRomBanks = in.read(); // 8kB per bank
			int controlByte1 = in.read();
			int controlByte2 = in.read();
			int numberOfRamBanks = in.read();
			for (int i = 0; i < 7; i++) {
				if (in.read() != 0) {
					throw new CartridgeFileFormatException("header fill bytes not 0");
				}
			}

			// debugging output
			System.out.println("number of 16kB PRG-ROM banks: " + numberOfPrgRomBanks);
			System.out.println("number of 8kB CHR-ROM banks: " + numberOfChrRomBanks);
			System.out.println("number of 8kB RAM banks: " + numberOfRamBanks);
			System.out.println("control bytes: " + Integer.toHexString(controlByte1) + ", " + Integer.toHexString(controlByte2));

			// validate header and check if all required features are supported
			int mapperNumber = ((controlByte1 >> 4) & 0x0f) + (controlByte2 & 0xf0);
			if (mapperNumber != 0) {
				throw new CartridgeFileFormatException("ROM mappers not supported");
			}
			if (numberOfPrgRomBanks != 2 || numberOfChrRomBanks != 1 || numberOfRamBanks != 0) {
				throw new CartridgeFileFormatException("this version only supports 2 PRG banks / 1 CHR bank / 0 RAM banks");
			}

			// read trainer if present
			if ((controlByte1 & 2) != 0) {
				for (int i=0; i<512; i++) {
					in.read();
				}
			}

			// read PRG-ROM
			this.prgRom = new byte[16 * 1024 * numberOfPrgRomBanks];
			if (in.read(prgRom) != prgRom.length) {
				throw new CartridgeFileFormatException("unexpected EOF in PRG-ROM contents");
			}

			// read CHR-ROM
			this.chrRom = new byte[8 * 1024 * numberOfChrRomBanks];
			if (in.read(chrRom) != chrRom.length) {
				throw new CartridgeFileFormatException("unexpected EOF in CHR-ROM contents");
			}

			// file should end here
			if (in.read() >= 0) {
				throw new CartridgeFileFormatException("unexpected content at end of file");
			}

		}
	}

	public byte readPrgRom(int address) {
		return prgRom[address & 0x7FFF];
	}

	public byte readChrRom(int address) {
		return chrRom[address & 0x1FFF];
	}

}
