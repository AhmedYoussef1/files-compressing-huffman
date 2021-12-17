package bits;

import java.nio.ByteBuffer;

public class Bits {
	private ByteBuffer bBuffer;
	private byte b;
	private int bPos;

	public Bits(int n) {
		bBuffer = ByteBuffer.allocate(n);
		bPos = 0;
	}

	public int bytePos() {
		return bPos;
	}

	// assume bits after the given length are zeroes
	public void append(byte bits, int len) {
		if (bPos + len > 8) {
			b |= bits << bPos;
			bBuffer.put(b);

			b = (byte) ((0xff & bits) >> (8 - bPos));
			bPos += len - 8;
		} else {
			b |= bits << bPos;
			bPos += len;
		}
	}

	public void append(byte[] byteArr, int bitsLen) {
		for (byte b : byteArr) {
			if (bitsLen > 8) {
				append(b, 8);
				bitsLen -= 8;
			} else {
				append(b, bitsLen);
				break;
			}
		}
	}

	public void appendOne() {
		append((byte) 1, 1);
	}

	public void appendZero() {
		append((byte) 0, 1);
	}

	public ByteBuffer get() {
		return bBuffer;
	}

	public void clean() {
		bBuffer.clear();
	}

	public Byte lastByte() {
		if (bPos % 8 > 0)
			return b;
		return null;
	}

	public static String ByteToString(byte b) {
		String str = "";
		for (int i = 0; i < 8; i++) {
			if ((b & (1 << i)) > 0)
				str = "1" + str;
			else
				str = "0" + str;
		}
		return str;
	}
}
