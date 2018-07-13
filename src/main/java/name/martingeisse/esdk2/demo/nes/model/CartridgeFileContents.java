package name.martingeisse.esdk2.demo.nes.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
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
			int numberOChrRomBanks = in.read(); // 8kB per bank
			int controlByte1 = in.read();
			int controlByte2 = in.read();
			int numberOfRamBanks = in.read();
			for (int i = 0; i < 7; i++) {
				if (in.read() != 0) {
					throw new CartridgeFileFormatException("header fill bytes not 0");
				}
			}

			// validate header and check if all required features are supported
			int mapperNumber = ((controlByte1 >> 4) & 0x0f) + (controlByte2 & 0xf0);
			if (mapperNumber != 0) {
				throw new CartridgeFileFormatException("ROM mappers not supported");
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
			this.chrRom = new byte[8 * 1024 * numberOfPrgRomBanks];
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
		if (address < 0 || address >= prgRom.length) {
			return 0;
		}
		return prgRom[address];
	}

	public byte readChrRom(int address) {
		if (address < 0 || address >= chrRom.length) {
			return 0;
		}
		return chrRom[address];
	}

}
