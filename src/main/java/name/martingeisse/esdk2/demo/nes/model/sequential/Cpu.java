package name.martingeisse.esdk2.demo.nes.model.sequential;

import name.martingeisse.esdk2.demo.nes.model.Constants;

/**
 *
 */
public final class Cpu {

	public static final int FLAG_NEGATIVE = 128;
	public static final int FLAG_OVERFLOW = 64;
	public static final int FLAG_BREAK = 16;
	public static final int FLAG_DECIMAL = 8;
	public static final int FLAG_INTERRUPT_DISABLE = 4;
	public static final int FLAG_ZERO = 2;
	public static final int FLAG_CARRY = 1;

	private final BusHandler busHandler;

	private int a, x, y, status, sp;
	private int pc;

	public Cpu(BusHandler busHandler) {
		this.busHandler = busHandler;
		reset();
	}

	//
	// primitive operations
	//

	private int read(int address) {
		return busHandler.read(address) & 0xff;
	}

	private void write(int address, int data) {
		busHandler.write(address, (byte) data);
	}

	private void setFlag(int flag) {
		status |= flag;
	}

	private void clearFlag(int flag) {
		status &= ~flag;
	}

	private void setFlag(int flag, boolean value) {
		if (value) {
			setFlag(flag);
		} else {
			clearFlag(flag);
		}
	}

	private boolean getFlag(int flag) {
		return (status & flag) != 0;
	}

	//
	// composite helper operations
	//

	private int read16(int address) {
		return read(address) + (read(address + 1) << 8);
	}

	private int read16ZeroPage(int address) {
		int low = read(address & 0xff);
		int high = read((address + 1) & 0xff);
		return low + (high << 8);
	}

	private int fetch() {
		int data = read(pc);
		pc++;
		return data;
	}

	private int fetch16() {
		int result = read16(pc);
		pc += 2;
		return result;
	}

	private int fetchOperandAddressAbsolute() {
		return fetch16();
	}

	private int fetchOperandAbsolute() {
		return read(fetchOperandAddressAbsolute());
	}

	private int fetchOperandAddressZeroPage() {
		return fetch();
	}

	private int fetchOperandZeroPage() {
		return read(fetchOperandAddressZeroPage());
	}

	private int fetchOperandAddressIndexed(int index) {
		return fetchOperandAddressAbsolute() + index;
	}

	private int fetchOperandIndexed(int index) {
		return read(fetchOperandAddressIndexed(index));
	}

	private int fetchOperandAddressZeroPageIndexed(int index) {
		// stays in zero page -- wraparound!
		return (fetch() + index) & 0xff;
	}

	private int fetchOperandZeroPageIndexed(int index) {
		return read(fetchOperandAddressZeroPageIndexed(index));
	}

	private int fetchOperandAddressIndirectIndexed(int index) {
		return read16ZeroPage(fetch()) + index;
	}

	private int fetchOperandIndirectIndexed(int index) {
		return read(fetchOperandAddressIndirectIndexed(index));
	}

	private int fetchOperandAddressIndexedIndirect(int index) {
		return read16ZeroPage(fetch() + index);
	}

	private int fetchOperandIndexedIndirect(int index) {
		return read(fetchOperandAddressIndexedIndirect(index));
	}

	private void setNZ(int from) {
		setFlag(FLAG_ZERO, from == 0);
		setFlag(FLAG_NEGATIVE, (from & 128) != 0);
	}

	private void push(int data) {
		write(getStackPointerAddress(), data);
		sp--;
	}

	private int pull() {
		sp++;
		return read(getStackPointerAddress());
	}

	private int getStackPointerAddress() {
		return 0x100 | sp;
	}

	//
	// vector handling
	//

	public void reset() {
		a = x = y = 0;
		pc = read16(Constants.RESET_VECTOR_LOCATION);
		sp = 0xfd;
		status = 0x34;
	}

	public void fireNmi() {
		push(pc >> 8);
		push(pc & 0xff);
		push(status);
		pc = read16(Constants.NMI_VECTOR_LOCATION);
		setFlag(FLAG_INTERRUPT_DISABLE);
	}

	// note: does not obey the I flag
	public void fireIrq() {
		push(pc >> 8);
		push(pc & 0xff);
		push(status);
		pc = read16(Constants.INTERRUPT_VECTOR_LOCATION);
		setFlag(FLAG_INTERRUPT_DISABLE);
	}

	//
	// instruction execution
	//

	private String toHex(int value, int digits) {
		String s = "00000000" + Integer.toHexString(value);
		return s.substring(s.length() - digits);
	}

	public void step() {

		boolean debug = false;//(pc > 0x800b) && (pc != 0x8070);
		if (debug) {
			System.out.print("pc=" + toHex(pc, 4) + " a=" + toHex(a, 2) + " x=" + toHex(x, 2) + " y=" + toHex(y, 2));
			System.out.print(" sp=" + toHex(sp, 2) + " status: ");
			String flagNames = "";
			for (int i = 7; i >= 0; i--) {
				if (getFlag(1 << i)) {
					System.out.print("CZIDB-VN".charAt(i));
				} else {
					System.out.print('-');
				}
			}
		}

		int opcode = fetch();
		if (debug) {
			System.out.println(" opcode=" + toHex(opcode, 2) + " next = " + toHex(read(pc), 2) + ", " + toHex(read(pc + 1), 2));
		}

		switch (opcode) {

			case 0x00: // BRK
				throw new UnsupportedOperationException("BRK not yet implemented");

			case 0x01: // ORA - (indirect, X)
				a |= fetchOperandIndexedIndirect(x);
				setNZ(a);
				break;

			case 0x05: // ORA - zero page
				a |= fetchOperandZeroPage();
				setNZ(a);
				break;

			case 0x06: // ASL - zero page
				performAsl(fetchOperandAddressZeroPage());
				break;

			case 0x08: // PHP
				push(status);
				break;

			case 0x09: // ORA - immediate
				a |= fetch();
				setNZ(a);
				break;

			case 0x0a: // ASL - accumulator
				setFlag(FLAG_CARRY, (a & 128) != 0);
				a = (a << 1) & 0xff;
				setNZ(a);
				break;

			case 0x0d: // ORA - absolute
				a |= fetchOperandAbsolute();
				setNZ(a);
				break;

			case 0x0e: // ASL - absolute
				performAsl(fetchOperandAddressAbsolute());
				break;

			case 0x10: // BPL
				fetchAndPerformBranch(!getFlag(FLAG_NEGATIVE));
				break;

			case 0x11: // ORA - (indirect), Y
				a |= fetchOperandIndirectIndexed(y);
				setNZ(a);
				break;

			case 0x15: // ORA - zero page, X
				a |= fetchOperandZeroPageIndexed(x);
				setNZ(a);
				break;

			case 0x16: // ASL - zero page, X
				performAsl(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0x18: // CLC
				clearFlag(FLAG_CARRY);
				break;

			case 0x19: // ORA - absolute, Y
				a |= fetchOperandIndexed(y);
				setNZ(a);
				break;

			case 0x1d: // ORA, absolute, X
				a |= fetchOperandIndexed(x);
				setNZ(a);
				break;

			case 0x1e: // ASL - absolute, X
				performAsl(fetchOperandAddressIndexed(x));
				break;

			case 0x20: // JSR
			{
				int address = fetchOperandAddressAbsolute();
				push((pc - 1) >> 8);
				push((pc - 1) & 0xff);
				pc = address;
				break;
			}

			case 0x21: // AND - (indirect, X)
				a &= fetchOperandIndexedIndirect(x);
				setNZ(a);
				break;

			case 0x24: // BIT - zero page
				performBit(fetchOperandZeroPage());
				break;

			case 0x25: // AND - zero page
				a &= fetchOperandZeroPage();
				setNZ(a);
				break;

			case 0x26: // ROL - zero page
				performRol(fetchOperandAddressZeroPage());
				break;

			case 0x28: // PLP
				status = pull();
				break;

			case 0x29: // AND - immediate
				a &= fetch();
				setNZ(a);
				break;

			case 0x2a: // ROL - accumulator
			{
				boolean oldCarry = getFlag(FLAG_CARRY);
				setFlag(FLAG_CARRY, (a & 128) != 0);
				a = ((a << 1) & 0xff) + (oldCarry ? 1 : 0);
				setNZ(a);
				break;
			}

			case 0x2c: // BIT - absolute
				performBit(fetchOperandAbsolute());
				break;

			case 0x2d: // AND - absolute
				a &= fetchOperandAbsolute();
				setNZ(a);
				break;

			case 0x2e: // ROL - absolute
				performRol(fetchOperandAddressAbsolute());
				break;

			case 0x30: // BMI
				fetchAndPerformBranch(getFlag(FLAG_NEGATIVE));
				break;

			case 0x31: // AND - (indirect), Y
				a &= fetchOperandIndirectIndexed(y);
				setNZ(a);
				break;

			case 0x35: // AND - zero page, X
				a &= fetchOperandZeroPageIndexed(x);
				setNZ(a);
				break;

			case 0x36: // ROL - zero page, X
				performRol(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0x38: // SEC
				setFlag(FLAG_CARRY);
				break;

			case 0x39: // AND - absolute, Y
				a &= fetchOperandIndexed(y);
				setNZ(a);
				break;

			case 0x3d: // AND - absolute, X
				a &= fetchOperandIndexed(x);
				setNZ(a);
				break;

			case 0x3e: // ROL - absolute, X
				performRol(fetchOperandAddressIndexed(x));
				break;

			case 0x40: // RTI
				status = pull();
				pc = pull() + (pull() << 8);
				break;

			case 0x41: // EOR - (indirect, X)
				a ^= fetchOperandIndexedIndirect(x);
				setNZ(a);
				break;

			case 0x45: // EOR - zero page
				a ^= fetchOperandZeroPage();
				setNZ(a);
				break;

			case 0x46: // LSR - zero page
				performLsr(fetchOperandAddressZeroPage());
				break;

			case 0x48: // PHA
				push(a);
				break;

			case 0x49: // EOR - immediate
				a ^= fetch();
				setNZ(a);
				break;

			case 0x4a: // LSR - accumulator
				setFlag(FLAG_CARRY, (a & 1) != 0);
				a >>>= 1;
				setNZ(a);
				break;

			case 0x4c: // JMP - absolute
				pc = fetchOperandAddressAbsolute();
				break;

			case 0x4d: // EOR - absolute
				a ^= fetchOperandAbsolute();
				setNZ(a);
				break;

			case 0x4e: // LSR - absolute
				performLsr(fetchOperandAddressAbsolute());
				break;

			case 0x50: // BVC
				fetchAndPerformBranch(!getFlag(FLAG_OVERFLOW));
				break;

			case 0x51: // EOR - (indirect), Y
				a ^= fetchOperandIndirectIndexed(y);
				setNZ(a);
				break;

			case 0x55: // EOR - zero page, X
				a ^= fetchOperandZeroPageIndexed(x);
				setNZ(a);
				break;

			case 0x56: // LSR - zero page, X
				performLsr(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0x58: // CLI
				clearFlag(FLAG_INTERRUPT_DISABLE);
				break;

			case 0x59: // EOR - absolute, Y
				a ^= fetchOperandIndexed(y);
				setNZ(a);
				break;

			case 0x5d: // EOR - absolute, X
				a ^= fetchOperandIndexed(x);
				setNZ(a);
				break;

			case 0x5e: // LSR - absolute, X
				performLsr(fetchOperandAddressIndexed(x));
				break;

			case 0x60: // RTS
				pc = pull() + (pull() << 8) + 1;
				break;

			case 0x61: // ADC - (indirect, X)
				performAdc(fetchOperandIndexedIndirect(x));
				break;

			case 0x65: // ADC - zero page
				performAdc(fetchOperandZeroPage());
				break;

			case 0x66: // ROR - zero page
				performRor(fetchOperandAddressZeroPage());
				break;

			case 0x68: // PLA
				a = pull();
				setNZ(a);
				break;

			case 0x69: // ADC - immediate
				performAdc(fetch());
				break;

			case 0x6a: // ROR - accumulator
			{
				boolean oldCarry = getFlag(FLAG_CARRY);
				setFlag(FLAG_CARRY, (a & 1) != 0);
				a = (a >>> 1) + (oldCarry ? 128 : 0);
				setNZ(a);
				break;
			}

			case 0x6c: // JMP - indirect
				pc = fetchOperandAddressIndexedIndirect(0);
				break;

			case 0x6d: // ADC - absolute
				performAdc(fetchOperandAbsolute());
				break;

			case 0x6e: // ROR - absolute
				performRor(fetchOperandAddressAbsolute());
				break;

			case 0x70: // BVS
				fetchAndPerformBranch(getFlag(FLAG_OVERFLOW));
				break;

			case 0x71: // ADC - (indirect), Y
				performAdc(fetchOperandIndirectIndexed(y));
				break;

			case 0x75: // ADC - zero page, X
				performAdc(fetchOperandZeroPageIndexed(x));
				break;

			case 0x76: // ROR - zero page, X
				performRor(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0x78: // SEI
				setFlag(FLAG_INTERRUPT_DISABLE);
				break;

			case 0x79: // ADC - absolute, Y
				performAdc(fetchOperandIndexed(y));
				break;

			case 0x7d: // ADC - absolute, X
				performAdc(fetchOperandIndexed(x));
				break;

			case 0x7e: // ROR - absolute, X
				performRor(fetchOperandAddressIndexed(x));
				break;

			case 0x81: // STA - (indirect, X)
				write(fetchOperandAddressIndexedIndirect(x), a);
				break;

			case 0x84: // STY - zero page
				write(fetchOperandAddressZeroPage(), y);
				break;

			case 0x85: // STA - zero page
				write(fetchOperandAddressZeroPage(), a);
				break;

			case 0x86: // STX - zero page
				write(fetchOperandAddressZeroPage(), x);
				break;

			case 0x88: // DEY
				y = (y - 1) & 0xff;
				setNZ(y);
				break;

			case 0x8a: // TXA
				a = x;
				setNZ(a);
				break;

			case 0x8c: // STY - absolute
				write(fetchOperandAddressAbsolute(), y);
				break;

			case 0x8d: // STA - absolute
				write(fetchOperandAddressAbsolute(), a);
				break;

			case 0x8e: // STX - absolute
				write(fetchOperandAddressAbsolute(), x);
				break;

			case 0x90: // BCC
				fetchAndPerformBranch(!getFlag(FLAG_CARRY));
				break;

			case 0x91: // STA - (indirect), Y
				write(fetchOperandAddressIndirectIndexed(y), a);
				break;

			case 0x94: // STY - zero page, X
				write(fetchOperandAddressZeroPageIndexed(x), y);
				break;

			case 0x95: // STA - zero page, X
				write(fetchOperandAddressZeroPageIndexed(x), a);
				break;

			case 0x96: // STX - zero page, Y
				write(fetchOperandAddressZeroPageIndexed(y), x);
				break;

			case 0x98: // TYA
				a = y;
				setNZ(a);
				break;

			case 0x99: // STA - absolute, Y
				write(fetchOperandAddressIndexed(y), a);
				break;

			case 0x9a: // TXS
				sp = x;
				// does not affect flags for some reason
				break;

			case 0x9d: // STA - absolute, X
				write(fetchOperandAddressIndexed(x), a);
				break;

			case 0xa0: // LDY - immediate
				y = fetch();
				setNZ(y);
				break;

			case 0xa1: // LDA - (indirect, X)
				a = fetchOperandIndexedIndirect(x);
				setNZ(a);
				break;

			case 0xa2: // LDX - immediate
				x = fetch();
				setNZ(x);
				break;

			case 0xa4: // LDY - zero page
				y = fetchOperandZeroPage();
				setNZ(y);
				break;

			case 0xa5: // LDA - zero page
				a = fetchOperandZeroPage();
				setNZ(a);
				break;

			case 0xa6: // LDX - zero page
				x = fetchOperandZeroPage();
				setNZ(x);
				break;

			case 0xa8: // TAY
				y = a;
				setNZ(y);
				break;

			case 0xa9: // LDA - immediate
				a = fetch();
				setNZ(a);
				break;

			case 0xaa: // TAX
				x = a;
				setNZ(x);
				break;

			case 0xac: // LDY - absolute
				y = fetchOperandAbsolute();
				setNZ(y);
				break;

			case 0xad: // LDA - absolute
				a = fetchOperandAbsolute();
				setNZ(a);
				break;

			case 0xae: // LDX - absolute
				x = fetchOperandAbsolute();
				setNZ(x);
				break;

			case 0xb0: // BCS
				fetchAndPerformBranch(getFlag(FLAG_CARRY));
				break;

			case 0xb1: // LDA - (indirect), Y
				a = fetchOperandIndirectIndexed(y);
				setNZ(a);
				break;

			case 0xb4: // LDY - zero page, X
				y = fetchOperandZeroPageIndexed(x);
				setNZ(y);
				break;

			case 0xb5: // LDA - zero page, X
				a = fetchOperandZeroPageIndexed(x);
				setNZ(a);
				break;

			case 0xb6: // LDX - zero page, Y
				x = fetchOperandZeroPageIndexed(y);
				setNZ(x);
				break;

			case 0xb8: // CLV
				clearFlag(FLAG_OVERFLOW);
				break;

			case 0xb9: // LDA - absolute, Y
				a = fetchOperandIndexed(y);
				setNZ(a);
				break;

			case 0xba: // TSX
				x = sp;
				setNZ(x);
				break;

			case 0xbc: // LDY - absolute, X
				y = fetchOperandIndexed(x);
				setNZ(y);
				break;

			case 0xbd: // LDA - absolute, X
				a = fetchOperandIndexed(x);
				setNZ(a);
				break;

			case 0xbe: // LDX - absolute, Y
				x = fetchOperandIndexed(y);
				setNZ(x);
				break;

			case 0xc0: // CPY - immediate
				performCmp(y, fetch());
				break;

			case 0xc1: // CMP - (indirect, X)
				performCmp(a, fetchOperandIndexedIndirect(x));
				break;

			case 0xc4: // CPY - zero page
				performCmp(y, fetchOperandZeroPage());
				break;

			case 0xc5: // CMP - zero page
				performCmp(a, fetchOperandZeroPage());
				break;

			case 0xc6: // DEC - zero page
				performDec(fetchOperandAddressZeroPage());
				break;

			case 0xc8: // INY
				y = (y + 1) & 0xff;
				setNZ(y);
				break;

			case 0xc9: // CMP - immediate
				performCmp(a, fetch());
				break;

			case 0xca: // DEX
				x = (x - 1) & 0xff;
				setNZ(x);
				break;

			case 0xcc: // CPY - absolute
				performCmp(y, fetchOperandAbsolute());
				break;

			case 0xcd: // CMP - absolute
				performCmp(a, fetchOperandAbsolute());
				break;

			case 0xce: // DEC - absolute
				performDec(fetchOperandAddressAbsolute());
				break;

			case 0xd0: // BNE
				fetchAndPerformBranch(!getFlag(FLAG_ZERO));
				break;

			case 0xd1: // CMP - (indirect), y
				performCmp(a, fetchOperandIndirectIndexed(y));
				break;

			case 0xd5: // CMP - zero page, X
				performCmp(a, fetchOperandZeroPageIndexed(x));
				break;

			case 0xd6: // DEC - zero page, X
				performDec(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0xd8: // CLD
				clearFlag(FLAG_DECIMAL);
				break;

			case 0xd9: // CMP - absolute, Y
				performCmp(a, fetchOperandIndexed(y));
				break;

			case 0xdd: // CMP - absolute, X
				performCmp(a, fetchOperandIndexed(x));
				break;

			case 0xde: // DEC - absolute, X
				performDec(fetchOperandAddressIndexed(x));
				break;

			case 0xe0: // CPX - immediate
				performCmp(x, fetch());
				break;

			case 0xe1: // SBC - (indirect, X)
				performSbc(fetchOperandIndexedIndirect(x));
				break;

			case 0xe4: // CPX - zero page
				performCmp(x, fetchOperandZeroPage());
				break;

			case 0xe5: // SBC - zero page
				performSbc(fetchOperandZeroPage());
				break;

			case 0xe6: // INC - zero page
				performInc(fetchOperandAddressZeroPage());
				break;

			case 0xe8: // INX
				x = (x + 1) & 0xff;
				setNZ(x);
				break;

			case 0xe9: // SBC - immediate
				performSbc(fetch());
				break;

			case 0xea: // NOP
				break;

			case 0xec: // CPX - absolute
				performCmp(x, fetchOperandAbsolute());
				break;

			case 0xed: // SBC - absolute
				performSbc(fetchOperandAbsolute());
				break;

			case 0xee: // INC - absolute
				performInc(fetchOperandAddressAbsolute());
				break;

			case 0xf0: // BEQ
				fetchAndPerformBranch(getFlag(FLAG_ZERO));
				break;

			case 0xf1: // SBC - (indirect), Y
				performSbc(fetchOperandIndirectIndexed(y));
				break;

			case 0xf5: // SBC - zero page, X
				performSbc(fetchOperandZeroPageIndexed(x));
				break;

			case 0xf6: // INC - zero page, X
				performInc(fetchOperandAddressZeroPageIndexed(x));
				break;

			case 0xf8: // SED
				setFlag(FLAG_DECIMAL);
				break;

			case 0xf9: // SBC - absolute, Y
				performSbc(fetchOperandIndexed(y));
				break;

			case 0xfd: // SBC - absolute, X
				performSbc(fetchOperandIndexed(x));
				break;

			case 0xfe: // INC - absolute, X
				performInc(fetchOperandAddressIndexed(x));
				break;

			default:
				throw new RuntimeException("unknown opcode: " + opcode);
		}
	}

	private void performAdc(int operand) {
		int a2 = a + operand + (getFlag(FLAG_CARRY) ? 1 : 0);
		setFlag(FLAG_CARRY, a2 > 255);
		setFlag(FLAG_OVERFLOW, (a & 128) == (operand & 128) && (a & 128) != (a2 & 128));
		a = a2 & 0xff;
		setNZ(a);
	}

	private void performSbc(int operand) {
		int a2 = a - operand - (getFlag(FLAG_CARRY) ? 1 : 0);
		setFlag(FLAG_CARRY, a2 >= 0); // borrow is carry inverted
		setFlag(FLAG_OVERFLOW, (a & 128) != (operand & 128) && (a & 128) != (a2 & 128));
		a = a2 & 0xff;
		setNZ(a);
	}

	private void performCmp(int leftOperand, int rightOperand) {
		int a2 = leftOperand - rightOperand;
		setFlag(FLAG_CARRY, a2 >= 0); // borrow is carry inverted
		setNZ(a2);
	}

	private void fetchAndPerformBranch(boolean condition) {
		int offset = (byte)fetch();
		if (condition) {
			pc += offset;
		}
	}

	private void performAsl(int address) {
		int oldValue = read(address);
		setFlag(FLAG_CARRY, (oldValue & 128) != 0);
		int newValue = (oldValue << 1) & 0xff;
		setNZ(newValue);
		write(address, newValue);
	}

	private void performInc(int address) {
		int newValue = (read(address) + 1) & 0xff;
		setNZ(newValue);
		write(address, newValue);
	}

	private void performDec(int address) {
		int newValue = (read(address) - 1) & 0xff;
		setNZ(newValue);
		write(address, newValue);
	}

	private void performLsr(int address) {
		int oldValue = read(address);
		setFlag(FLAG_CARRY, (oldValue & 1) != 0);
		int newValue = oldValue >>> 1;
		setNZ(newValue);
		write(address, newValue);
	}

	private void performRol(int address) {
		int oldValue = read(address);
		int newValue = ((oldValue << 1) & 0xff) + (getFlag(FLAG_CARRY) ? 1 : 0);
		setFlag(FLAG_CARRY, (oldValue & 128) != 0);
		setNZ(newValue);
		write(address, newValue);
	}

	private void performRor(int address) {
		int oldValue = read(address);
		int newValue = (oldValue >>> 1) + (getFlag(FLAG_CARRY) ? 128 : 0);
		setFlag(FLAG_CARRY, (oldValue & 1) != 0);
		setNZ(newValue);
		write(address, newValue);
	}

	private void performBit(int operand) {
		setFlag(FLAG_NEGATIVE, (operand & 128) != 0);
		setFlag(FLAG_OVERFLOW, (operand & 64) != 0);
		setFlag(FLAG_ZERO, (a & operand) == 0);
	}

}
