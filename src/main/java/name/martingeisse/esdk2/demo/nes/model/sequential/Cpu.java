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

			case 0x00:
				throw new RuntimeException();

			case 0x01:
				throw new RuntimeException();

			case 0x02:
				throw new RuntimeException();

			case 0x03:
				throw new RuntimeException();

			case 0x04:
				throw new RuntimeException();

			case 0x05:
				throw new RuntimeException();

			case 0x06:
				throw new RuntimeException();

			case 0x07:
				throw new RuntimeException();

			case 0x08:
				throw new RuntimeException();

			case 0x09:
				throw new RuntimeException();

			case 0x0a:
				throw new RuntimeException();

			case 0x0b:
				throw new RuntimeException();

			case 0x0c:
				throw new RuntimeException();

			case 0x0d:
				throw new RuntimeException();

			case 0x0e:
				throw new RuntimeException();

			case 0x0f:
				throw new RuntimeException();

			case 0x10:
				throw new RuntimeException();

			case 0x11:
				throw new RuntimeException();

			case 0x12:
				throw new RuntimeException();

			case 0x13:
				throw new RuntimeException();

			case 0x14:
				throw new RuntimeException();

			case 0x15:
				throw new RuntimeException();

			case 0x16:
				throw new RuntimeException();

			case 0x17:
				throw new RuntimeException();

			case 0x18:
				throw new RuntimeException();

			case 0x19:
				throw new RuntimeException();

			case 0x1a:
				throw new RuntimeException();

			case 0x1b:
				throw new RuntimeException();

			case 0x1c:
				throw new RuntimeException();

			case 0x1d:
				throw new RuntimeException();

			case 0x1e:
				throw new RuntimeException();

			case 0x1f:
				throw new RuntimeException();

			case 0x20:
				throw new RuntimeException();

			case 0x21:
				throw new RuntimeException();

			case 0x22:
				throw new RuntimeException();

			case 0x23:
				throw new RuntimeException();

			case 0x24:
				throw new RuntimeException();

			case 0x25:
				throw new RuntimeException();

			case 0x26:
				throw new RuntimeException();

			case 0x27:
				throw new RuntimeException();

			case 0x28:
				throw new RuntimeException();

			case 0x29:
				throw new RuntimeException();

			case 0x2a:
				throw new RuntimeException();

			case 0x2b:
				throw new RuntimeException();

			case 0x2c:
				throw new RuntimeException();

			case 0x2d:
				throw new RuntimeException();

			case 0x2e:
				throw new RuntimeException();

			case 0x2f:
				throw new RuntimeException();

			case 0x30:
				throw new RuntimeException();

			case 0x31:
				throw new RuntimeException();

			case 0x32:
				throw new RuntimeException();

			case 0x33:
				throw new RuntimeException();

			case 0x34:
				throw new RuntimeException();

			case 0x35:
				throw new RuntimeException();

			case 0x36:
				throw new RuntimeException();

			case 0x37:
				throw new RuntimeException();

			case 0x38:
				throw new RuntimeException();

			case 0x39:
				throw new RuntimeException();

			case 0x3a:
				throw new RuntimeException();

			case 0x3b:
				throw new RuntimeException();

			case 0x3c:
				throw new RuntimeException();

			case 0x3d:
				throw new RuntimeException();

			case 0x3e:
				throw new RuntimeException();

			case 0x3f:
				throw new RuntimeException();

			case 0x40:
				throw new RuntimeException();

			case 0x41:
				throw new RuntimeException();

			case 0x42:
				throw new RuntimeException();

			case 0x43:
				throw new RuntimeException();

			case 0x44:
				throw new RuntimeException();

			case 0x45:
				throw new RuntimeException();

			case 0x46:
				throw new RuntimeException();

			case 0x47:
				throw new RuntimeException();

			case 0x48:
				throw new RuntimeException();

			case 0x49:
				throw new RuntimeException();

			case 0x4a:
				throw new RuntimeException();

			case 0x4b:
				throw new RuntimeException();

			case 0x4c:
				throw new RuntimeException();

			case 0x4d:
				throw new RuntimeException();

			case 0x4e:
				throw new RuntimeException();

			case 0x4f:
				throw new RuntimeException();

			case 0x50:
				throw new RuntimeException();

			case 0x51:
				throw new RuntimeException();

			case 0x52:
				throw new RuntimeException();

			case 0x53:
				throw new RuntimeException();

			case 0x54:
				throw new RuntimeException();

			case 0x55:
				throw new RuntimeException();

			case 0x56:
				throw new RuntimeException();

			case 0x57:
				throw new RuntimeException();

			case 0x58:
				throw new RuntimeException();

			case 0x59:
				throw new RuntimeException();

			case 0x5a:
				throw new RuntimeException();

			case 0x5b:
				throw new RuntimeException();

			case 0x5c:
				throw new RuntimeException();

			case 0x5d:
				throw new RuntimeException();

			case 0x5e:
				throw new RuntimeException();

			case 0x5f:
				throw new RuntimeException();

			case 0x60:
				throw new RuntimeException();

			case 0x61:
				throw new RuntimeException();

			case 0x62:
				throw new RuntimeException();

			case 0x63:
				throw new RuntimeException();

			case 0x64:
				throw new RuntimeException();

			case 0x65:
				throw new RuntimeException();

			case 0x66:
				throw new RuntimeException();

			case 0x67:
				throw new RuntimeException();

			case 0x68:
				throw new RuntimeException();

			case 0x69:
				throw new RuntimeException();

			case 0x6a:
				throw new RuntimeException();

			case 0x6b:
				throw new RuntimeException();

			case 0x6c:
				throw new RuntimeException();

			case 0x6d:
				throw new RuntimeException();

			case 0x6e:
				throw new RuntimeException();

			case 0x6f:
				throw new RuntimeException();

			case 0x70:
				throw new RuntimeException();

			case 0x71:
				throw new RuntimeException();

			case 0x72:
				throw new RuntimeException();

			case 0x73:
				throw new RuntimeException();

			case 0x74:
				throw new RuntimeException();

			case 0x75:
				throw new RuntimeException();

			case 0x76:
				throw new RuntimeException();

			case 0x77:
				throw new RuntimeException();

			case 0x78:
				throw new RuntimeException();

			case 0x79:
				throw new RuntimeException();

			case 0x7a:
				throw new RuntimeException();

			case 0x7b:
				throw new RuntimeException();

			case 0x7c:
				throw new RuntimeException();

			case 0x7d:
				throw new RuntimeException();

			case 0x7e:
				throw new RuntimeException();

			case 0x7f:
				throw new RuntimeException();

			case 0x80:
				throw new RuntimeException();

			case 0x81:
				throw new RuntimeException();

			case 0x82:
				throw new RuntimeException();

			case 0x83:
				throw new RuntimeException();

			case 0x84:
				throw new RuntimeException();

			case 0x85:
				throw new RuntimeException();

			case 0x86:
				throw new RuntimeException();

			case 0x87:
				throw new RuntimeException();

			case 0x88:
				throw new RuntimeException();

			case 0x89:
				throw new RuntimeException();

			case 0x8a:
				throw new RuntimeException();

			case 0x8b:
				throw new RuntimeException();

			case 0x8c:
				throw new RuntimeException();

			case 0x8d:
				throw new RuntimeException();

			case 0x8e:
				throw new RuntimeException();

			case 0x8f:
				throw new RuntimeException();

			case 0x90:
				throw new RuntimeException();

			case 0x91:
				throw new RuntimeException();

			case 0x92:
				throw new RuntimeException();

			case 0x93:
				throw new RuntimeException();

			case 0x94:
				throw new RuntimeException();

			case 0x95:
				throw new RuntimeException();

			case 0x96:
				throw new RuntimeException();

			case 0x97:
				throw new RuntimeException();

			case 0x98:
				throw new RuntimeException();

			case 0x99:
				throw new RuntimeException();

			case 0x9a:
				throw new RuntimeException();

			case 0x9b:
				throw new RuntimeException();

			case 0x9c:
				throw new RuntimeException();

			case 0x9d:
				throw new RuntimeException();

			case 0x9e:
				throw new RuntimeException();

			case 0x9f:
				throw new RuntimeException();

			case 0xa0:
				throw new RuntimeException();

			case 0xa1:
				throw new RuntimeException();

			case 0xa2:
				throw new RuntimeException();

			case 0xa3:
				throw new RuntimeException();

			case 0xa4:
				throw new RuntimeException();

			case 0xa5:
				throw new RuntimeException();

			case 0xa6:
				throw new RuntimeException();

			case 0xa7:
				throw new RuntimeException();

			case 0xa8:
				throw new RuntimeException();

			case 0xa9:
				throw new RuntimeException();

			case 0xaa:
				throw new RuntimeException();

			case 0xab:
				throw new RuntimeException();

			case 0xac:
				throw new RuntimeException();

			case 0xad:
				throw new RuntimeException();

			case 0xae:
				throw new RuntimeException();

			case 0xaf:
				throw new RuntimeException();

			case 0xb0:
				throw new RuntimeException();

			case 0xb1:
				throw new RuntimeException();

			case 0xb2:
				throw new RuntimeException();

			case 0xb3:
				throw new RuntimeException();

			case 0xb4:
				throw new RuntimeException();

			case 0xb5:
				throw new RuntimeException();

			case 0xb6:
				throw new RuntimeException();

			case 0xb7:
				throw new RuntimeException();

			case 0xb8:
				throw new RuntimeException();

			case 0xb9:
				throw new RuntimeException();

			case 0xba:
				throw new RuntimeException();

			case 0xbb:
				throw new RuntimeException();

			case 0xbc:
				throw new RuntimeException();

			case 0xbd:
				throw new RuntimeException();

			case 0xbe:
				throw new RuntimeException();

			case 0xbf:
				throw new RuntimeException();

			case 0xc0:
				throw new RuntimeException();

			case 0xc1:
				throw new RuntimeException();

			case 0xc2:
				throw new RuntimeException();

			case 0xc3:
				throw new RuntimeException();

			case 0xc4:
				throw new RuntimeException();

			case 0xc5:
				throw new RuntimeException();

			case 0xc6:
				throw new RuntimeException();

			case 0xc7:
				throw new RuntimeException();

			case 0xc8:
				throw new RuntimeException();

			case 0xc9:
				throw new RuntimeException();

			case 0xca:
				throw new RuntimeException();

			case 0xcb:
				throw new RuntimeException();

			case 0xcc:
				throw new RuntimeException();

			case 0xcd:
				throw new RuntimeException();

			case 0xce:
				throw new RuntimeException();

			case 0xcf:
				throw new RuntimeException();

			case 0xd0:
				throw new RuntimeException();

			case 0xd1:
				throw new RuntimeException();

			case 0xd2:
				throw new RuntimeException();

			case 0xd3:
				throw new RuntimeException();

			case 0xd4:
				throw new RuntimeException();

			case 0xd5:
				throw new RuntimeException();

			case 0xd6:
				throw new RuntimeException();

			case 0xd7:
				throw new RuntimeException();

			case 0xd8:
				throw new RuntimeException();

			case 0xd9:
				throw new RuntimeException();

			case 0xda:
				throw new RuntimeException();

			case 0xdb:
				throw new RuntimeException();

			case 0xdc:
				throw new RuntimeException();

			case 0xdd:
				throw new RuntimeException();

			case 0xde:
				throw new RuntimeException();

			case 0xdf:
				throw new RuntimeException();

			case 0xe0:
				throw new RuntimeException();

			case 0xe1:
				throw new RuntimeException();

			case 0xe2:
				throw new RuntimeException();

			case 0xe3:
				throw new RuntimeException();

			case 0xe4:
				throw new RuntimeException();

			case 0xe5:
				throw new RuntimeException();

			case 0xe6:
				throw new RuntimeException();

			case 0xe7:
				throw new RuntimeException();

			case 0xe8:
				throw new RuntimeException();

			case 0xe9:
				throw new RuntimeException();

			case 0xea:
				throw new RuntimeException();

			case 0xeb:
				throw new RuntimeException();

			case 0xec:
				throw new RuntimeException();

			case 0xed:
				throw new RuntimeException();

			case 0xee:
				throw new RuntimeException();

			case 0xef:
				throw new RuntimeException();

			case 0xf0:
				throw new RuntimeException();

			case 0xf1:
				throw new RuntimeException();

			case 0xf2:
				throw new RuntimeException();

			case 0xf3:
				throw new RuntimeException();

			case 0xf4:
				throw new RuntimeException();

			case 0xf5:
				throw new RuntimeException();

			case 0xf6:
				throw new RuntimeException();

			case 0xf7:
				throw new RuntimeException();

			case 0xf8:
				throw new RuntimeException();

			case 0xf9:
				throw new RuntimeException();

			case 0xfa:
				throw new RuntimeException();

			case 0xfb:
				throw new RuntimeException();

			case 0xfc:
				throw new RuntimeException();

			case 0xfd:
				throw new RuntimeException();

			case 0xfe:
				throw new RuntimeException();

			case 0xff:
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
