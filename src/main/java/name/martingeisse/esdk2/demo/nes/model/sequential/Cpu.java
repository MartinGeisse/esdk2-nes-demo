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

			case 0x00: // BRK
				throw new RuntimeException();

			case 0x01: // ORA - (indirect, X)
				throw new RuntimeException();

			case 0x05: // ORA - zero page
				throw new RuntimeException();

			case 0x06: // ASL - zero page
				throw new RuntimeException();

			case 0x08: // PHP
				throw new RuntimeException();

			case 0x09: // ORA - immediate
				throw new RuntimeException();

			case 0x0a: // ASL - accumulator
				throw new RuntimeException();

			case 0x0d: // ORA - absolute
				throw new RuntimeException();

			case 0x0e: // ASL - absolute
				throw new RuntimeException();

			case 0x10: // BPL
				throw new RuntimeException();

			case 0x11: // ORA - (indirect), Y
				throw new RuntimeException();

			case 0x15: // ORA - zero page, X
				throw new RuntimeException();

			case 0x16: // ASL - zero page, X
				throw new RuntimeException();

			case 0x18: // CLC
				throw new RuntimeException();

			case 0x19: // ORA - absolute, Y
				throw new RuntimeException();

			case 0x1d: // ORA, absolute, X
				throw new RuntimeException();

			case 0x1e: // ASL - absolute, X
				throw new RuntimeException();

			case 0x20: // JSR
				throw new RuntimeException();

			case 0x21: // AND - (indirect, X)
				throw new RuntimeException();

			case 0x24: // BIT - zero page
				throw new RuntimeException();

			case 0x25: // AND - zero page
				throw new RuntimeException();

			case 0x26: // ROL - zero page
				throw new RuntimeException();

			case 0x28: // PLP
				throw new RuntimeException();

			case 0x29: // AND - immediate
				throw new RuntimeException();

			case 0x2a: // ROL - accumulator
				throw new RuntimeException();

			case 0x2c: // BIT - absolute
				throw new RuntimeException();

			case 0x2d: // AND - absolute
				throw new RuntimeException();

			case 0x2e: // ROL - absolute
				throw new RuntimeException();

			case 0x30: // BMI
				throw new RuntimeException();

			case 0x31: // AND - (indirect), Y
				throw new RuntimeException();

			case 0x35: // AND - zero page, X
				throw new RuntimeException();

			case 0x36: // ROL - zero page, X
				throw new RuntimeException();

			case 0x38: // SEC
				throw new RuntimeException();

			case 0x39: // AND - absolute, Y
				throw new RuntimeException();

			case 0x3d: // AND - absolute, X
				throw new RuntimeException();

			case 0x3e: // ROL - absolute, X
				throw new RuntimeException();

			case 0x40: // RTI
				throw new RuntimeException();

			case 0x41: // EOR - (indirect, X)
				throw new RuntimeException();

			case 0x45: // EOR - zero page
				throw new RuntimeException();

			case 0x46: // LSR - zero page
				throw new RuntimeException();

			case 0x48: // PHA
				throw new RuntimeException();

			case 0x49: // EOR - immediate
				throw new RuntimeException();

			case 0x4a: // LSR - accumulator
				throw new RuntimeException();

			case 0x4c: // JMP - absolute
				throw new RuntimeException();

			case 0x4d: // EOR - absolute
				throw new RuntimeException();

			case 0x4e: // LSR - absolute
				throw new RuntimeException();

			case 0x50: // BVC
				throw new RuntimeException();

			case 0x51: // EOR - (indirect), Y
				throw new RuntimeException();

			case 0x55: // EOR - zero page, X
				throw new RuntimeException();

			case 0x56: // LSR - zero page, X
				throw new RuntimeException();

			case 0x58: // CLI
				throw new RuntimeException();

			case 0x59: // EOR - absolute, Y
				throw new RuntimeException();

			case 0x5d: // EOR - absolute, X
				throw new RuntimeException();

			case 0x5e: // LSR - absolute, X
				throw new RuntimeException();

			case 0x60: // RTS
				throw new RuntimeException();

			case 0x61: // ADC - (indirect, X)
				throw new RuntimeException();

			case 0x65: // ADC - zero page
				throw new RuntimeException();

			case 0x66: // ROR - zero page
				throw new RuntimeException();

			case 0x68: // PLA
				throw new RuntimeException();

			case 0x69: // ADC - immediate
				throw new RuntimeException();

			case 0x6a: // ROR - accumulator
				throw new RuntimeException();

			case 0x6c: // JMP - indirect
				throw new RuntimeException();

			case 0x6d: // ADC - absolute
				throw new RuntimeException();

			case 0x6e: // ROR - absolute
				throw new RuntimeException();

			case 0x70: // BVS
				throw new RuntimeException();

			case 0x71: // ADC - (indirect), Y
				throw new RuntimeException();

			case 0x75: // ADC - zero page, X
				throw new RuntimeException();

			case 0x76: // ROR - zero page, X
				throw new RuntimeException();

			case 0x78: // SEI
				throw new RuntimeException();

			case 0x79: // ADC - absolute, Y
				throw new RuntimeException();

			case 0x7d: // ADC - absolute, X
				throw new RuntimeException();

			case 0x7e: // ROR - absolute, X
				throw new RuntimeException();

			case 0x81: // STA - (indirect, X)
				throw new RuntimeException();

			case 0x84: // STY - zero page
				throw new RuntimeException();

			case 0x85: // STA - zero page
				throw new RuntimeException();

			case 0x86: // STX - zero page
				throw new RuntimeException();

			case 0x88: // DEY
				throw new RuntimeException();

			case 0x8a: // TXA
				throw new RuntimeException();

			case 0x8c: // STY - absolute
				throw new RuntimeException();

			case 0x8d: // STA - absolute
				throw new RuntimeException();

			case 0x8e: // STX - absolute
				throw new RuntimeException();

			case 0x90: // BCC
				throw new RuntimeException();

			case 0x91: // STA - (indirect), Y
				throw new RuntimeException();

			case 0x94: // STY - zero page, X
				throw new RuntimeException();

			case 0x95: // STA - zero page, X
				throw new RuntimeException();

			case 0x96: // STX - zero page, Y
				throw new RuntimeException();

			case 0x98: // TYA
				throw new RuntimeException();

			case 0x99: // STA - absolute, Y
				throw new RuntimeException();

			case 0x9a: // TXS
				throw new RuntimeException();

			case 0x9d: // STA - absolute, X
				throw new RuntimeException();

			case 0xa0: // LDY - immediate
				throw new RuntimeException();

			case 0xa1: // LDA - (indirect, X)
				throw new RuntimeException();

			case 0xa2: // LDX - immediate
				throw new RuntimeException();

			case 0xa4: // LDY - zero page
				throw new RuntimeException();

			case 0xa5: // LDA - zero page
				throw new RuntimeException();

			case 0xa6: // LDX - zero page
				throw new RuntimeException();

			case 0xa8: // TAY
				throw new RuntimeException();

			case 0xa9: // LDA - immediate
				throw new RuntimeException();

			case 0xaa: // TAX
				throw new RuntimeException();

			case 0xac: // LDY - absolute
				throw new RuntimeException();

			case 0xad: // LDA - absolute
				throw new RuntimeException();

			case 0xae: // LDX - absolute
				throw new RuntimeException();

			case 0xb0: // BCS
				throw new RuntimeException();

			case 0xb1: // LDA - (indirect), Y
				throw new RuntimeException();

			case 0xb4: // LDY - zero page, X
				throw new RuntimeException();

			case 0xb5: // LDA - zero page, X
				throw new RuntimeException();

			case 0xb6: // LDX - zero page, Y
				throw new RuntimeException();

			case 0xb8: // CLV
				throw new RuntimeException();

			case 0xb9: // LDA - absolute, Y
				throw new RuntimeException();

			case 0xba: // TSX
				throw new RuntimeException();

			case 0xbc: // LDY - absolute, X
				throw new RuntimeException();

			case 0xbd: // LDA - absolute, X
				throw new RuntimeException();

			case 0xbe: // LDX - absolute, Y
				throw new RuntimeException();

			case 0xc0: // CPY - immediate
				throw new RuntimeException();

			case 0xc1: // CMP - (indirect, X)
				throw new RuntimeException();

			case 0xc4: // CPY - zero page
				throw new RuntimeException();

			case 0xc5: // CMP - zero page
				throw new RuntimeException();

			case 0xc6: // DEC - zero page
				throw new RuntimeException();

			case 0xc8: // INY
				throw new RuntimeException();

			case 0xc9: // CMP - immediate
				throw new RuntimeException();

				// TODO --- check ---

			case 0xca: // DEX
				throw new RuntimeException();

			case 0xcc: // CPY - absolute
				throw new RuntimeException();

			case 0xcd: // CMP - absolute
				throw new RuntimeException();

			case 0xce: // DEC - absolute
				throw new RuntimeException();

			case 0xd0: // BNE
				throw new RuntimeException();

			case 0xd1: // CMP - (indirect), y
				throw new RuntimeException();

			case 0xd5: // CMP - zero page, X
				throw new RuntimeException();

			case 0xd6: // DEC - zero page, X
				throw new RuntimeException();

			case 0xd8: // CLD
				throw new RuntimeException();

			case 0xd9: // CMP - absolute, Y
				throw new RuntimeException();

			case 0xdd: // CMP - absolute, X
				throw new RuntimeException();

			case 0xde: // DEC - absolute, X
				throw new RuntimeException();

			case 0xe0: // CPY - immediate
				throw new RuntimeException();

			case 0xe1: // SBC - (indirect, X)
				throw new RuntimeException();

			case 0xe4: // CPX - zero page
				throw new RuntimeException();

			case 0xe5: // SBC - zero page
				throw new RuntimeException();

			case 0xe6: // INC - zero page
				throw new RuntimeException();

			case 0xe8: // INX
				throw new RuntimeException();

			case 0xe9: // SBC - immediate
				throw new RuntimeException();

			case 0xea: // NOP
				throw new RuntimeException();

			case 0xec: // CPX - absolute
				throw new RuntimeException();

			case 0xed: // SBC - absolute
				throw new RuntimeException();

			case 0xee: // INC - absolute
				throw new RuntimeException();

			case 0xf0: // BEQ
				throw new RuntimeException();

			case 0xf1: // SBC - (indirect), Y
				throw new RuntimeException();

			case 0xf5: // SBC - zero page, X
				throw new RuntimeException();

			case 0xf6: // INC - zero page, X
				throw new RuntimeException();

			case 0xf8: // SED
				throw new RuntimeException();

			case 0xf9: // SBC - absolute, Y
				throw new RuntimeException();

			case 0xfd: // SBC - absolute, X
				throw new RuntimeException();

			case 0xfe: // INC - absolute, X
				throw new RuntimeException();

			default:
				throw new RuntimeException("unknown opcode: " + opcode);
		}
		// TODO
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
