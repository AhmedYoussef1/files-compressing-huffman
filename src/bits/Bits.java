package bits;

import java.util.BitSet;

import huffman.Files;

public class Bits {
	private int size, len;
	private BitSet bits;

	public Bits(int size) {
		this.size = size;
		this.len = 0;
		this.bits = new BitSet(size);
	}

	public int length() {
		return this.len;
	}

	public int empty() {
		return this.size - this.len;
	}

	// return byte array + 1 extra byte
	// BitSet doesn't return last all zero bytes
	public byte[] bytes_plusOne() {
		this.bits.set(len + 8);
		return this.bits.toByteArray();
	}

	public void clear() {
		this.len = 0;
		this.bits.clear();
	}

	public int appendOne() {
		bits.set(len);
		return ++len;
	}

	public int appendZero() {
		return ++len;
	}

	public int append(boolean bitArr[]) {
		for (boolean val : bitArr) {
			if (val)
				bits.set(len);
			len++;
		}
		return len;
	}

	public int append(byte b, int n) {
		for (int i = 0; i < n; i++) {
			if ((b & (1 << i)) > 0)
				this.bits.set(len);
			len++;
		}
		return len;
	}

	public int append(byte[] bytes) {
		for (byte b : bytes)
			append(b, 8);
		return len;
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

	// I will reuse this variable for every function call to bytesToHex
	// This is better for both memory and performance
	private static StringBuilder hexString = new StringBuilder(2 * Files.get_n());

	// Assuming hash.length = Files.get_n()
	public static String hashMe(byte[] word) {
		hexString.delete(0, hexString.length()); // clean
		for (int i = 0; i < word.length; i++) {
			String hex = Integer.toHexString(0xff & word[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static void main(String[] args) {
		byte b = 'c';
		
		System.out.println(Bits.ByteToString(b));
		
		
	}

}
