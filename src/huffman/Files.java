package huffman;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import bits.Bits;
import table.HuffmanTable;

public class Files {
	private static int n = 1; // number of bytes per word

	public static int set_n(int N) {
		return (n = N);
	}

	public static int get_n() {
		return n;
	}

	private int max_memory() {
		// 25
		return 1 << 25;
	}

	public static int unusedBytes, unusedBits;

	public void freq_table(File file, HuffmanTable huffTable) throws IOException {
		int buffSize = Files.n * (max_memory() / Files.n);
		if (buffSize == 0)
			buffSize = Files.n;

		InputStream f = new FileInputStream(file);
		byte[] buffer = new byte[buffSize];

		int read;
		while ((read = f.read(buffer)) > 0) { // read from file
			// clear trash from the buffer
			if(read % Files.n != 0) {
				for(int i = read; i < read + Files.n && i < buffSize; i++)
					buffer[i] = 0;
			}
			// analysis the buffer
			byte[] word = new byte[Files.n];
			int i = 0;
			for (; i < read; i += Files.n) {
				System.arraycopy(buffer, i, word, 0, Files.n);
				huffTable.frequency(word.clone());
			}
		}
		f.close();
	}

	public void compress(File inputFile, File outputFile, HuffmanTable huffTable, TreeNode root) throws IOException {
		int buffSize = Files.n * ((max_memory() << 1) / Files.n); // max_memory / 2
		if (buffSize == 0)
			buffSize = Files.n;

		byte[] buffer = new byte[buffSize];
		Bits compressed = new Bits(buffSize);

		InputStream in = new FileInputStream(inputFile);
		OutputStream out = new FileOutputStream(outputFile);

		int read;

		// headers
		Headers h = new Headers();
		h.writeHeaders(compressed, get_n(), unusedBytes, unusedBits, root);
		while (compressed.bytePos() % 8 != 0)
			compressed.appendZero();

		// data
		while ((read = in.read(buffer)) > 0) {
			for (int i = 0; i < read; i += Files.n) {
				byte[] word = Arrays.copyOfRange(buffer, i, i + Files.n);
				TreeNode node = huffTable.get(word);
				compressed.append(node.coding.data, node.coding.len);
			}
			// write compressed to file (without the last byte)
			ByteBuffer bb = compressed.get();
			out.write(bb.array(), 0, bb.position());
			compressed.clean();
			
			// clear the buffer
			buffer = new byte[buffSize];
		}
		// write last bits
		Byte lastByte = compressed.lastByte();
		if (lastByte != null)
			out.write(lastByte);

		in.close();
		out.close();
	}

	public void decompress(File inputFile, File outputFile) throws IOException {
		int buffSize = max_memory() >> 2; // max_memory / 4
		if (buffSize == 0)
			buffSize = 1;

		byte[] buffer = new byte[buffSize];
		ByteBuffer uncompressed = ByteBuffer.allocate(buffSize << 2);

		InputStream in = new FileInputStream(inputFile);
		OutputStream out = new FileOutputStream(outputFile);

		int read, pos = 0, unreaded = (int) inputFile.length();
		Object[] decoded;
		TreeNode root = new TreeNode(), node = null;

		boolean firstRead = true;
		while ((read = in.read(buffer)) > 0) {
			if (firstRead) { // read headers
				Headers h = new Headers();
				pos = h.readHeaders(buffer, root);
				while (pos % 8 != 0)
					pos++;
				firstRead = false;
			}

			unreaded -= read;
			int len = (unreaded > 0) ? read * 8 : read * 8 - unusedBits;
			int extraBytes = (unreaded > 0) ? 0 : unusedBytes;

			while (pos < len) { // read until end of the buffer
				decoded = decode(root, node, buffer, pos, len, uncompressed);
				node = (TreeNode) decoded[0];
				pos = (int) decoded[1];
				out.write(uncompressed.array(), 0, uncompressed.position() - extraBytes);
				uncompressed.clear();
			}
			pos = 0;
		}

		in.close();
		out.close();
	}

	private Object[] decode(TreeNode root, TreeNode node, byte[] read, int offset, int bitLen, ByteBuffer write) {
		if (node == null)
			node = root;

		int pos = offset;
		while (pos < bitLen && write.remaining() > Files.get_n()) {
			if (node.data != null) {
				write.put(node.data);
				node = root;
			} else {
				if ((read[pos / 8] & (1 << (pos % 8))) > 0)
					node = node.one();
				else
					node = node.zero();
				pos++;
			}
		}
		if (node.data != null) {
			write.put(node.data);
			node = root;
		}
		return new Object[] { node, pos };
	}

	public static void main(String[] args) throws IOException {
//		Files.set_n(1);
//		Files f = new Files();
//		Tree_construct tree_c = new Tree_construct();
//
//		String path = "files/";
//		String name = "lec.pdf";
//
//		// file analysis
//		System.out.println("Analyzing file...");
//		Map<String, TreeNode> freq_table = f.freq_table(new File(path + name));
//
//		// coding tree
//		System.out.println("Making coding tree...");
//		TreeNode root = tree_c.make_tree(freq_table);
//
//		Map<String, boolean[]> coding_table = new HashMap<>();
//		boolean[] route = new boolean[128 * Files.n];
//
//		// coding table
//		System.out.println("Making coding table...");
//		tree_c.coding_table(root, coding_table, route, 0);
//
//		// calculate unused bits, unused bytes
//		File infile = new File(path + name);
//		int usused_bytes = (int) (Files.get_n() - infile.length() % Files.get_n()) % Files.get_n();
//		System.out.println("Unused bytes: " + usused_bytes);
//
//		long usedBits = 0;
//		for (TreeNode node : freq_table.values()) {
//			usedBits += coding_table.get(Bits.hashMe(node.data)).length * node.get_freq();
//		}
//
//		int unused_bits = (int) (8 - usedBits % 8) % 8;
//		System.out.println("Unused bits: " + unused_bits);
////		for (TreeNode node : freq_table.values()) {
////			String binary = "";
////			for (byte b : node.data)
////				binary = binary + Bits.ByteToString(b) + " ";
////			System.out.println(binary + ": " + Arrays.toString(coding_table.get(Arrays.hashCode(node.data))));
////		}
//
////		compress
//		System.out.println("Compressing...");
//		f.compress(new File(path + name), new File(path + name + ".comp"), coding_table);
//
//		System.out.println("Unused bytes true: " + unusedBytes);
//		System.out.println("Unused bits true: " + unusedBits);
//
//		// extract
//		System.out.println("Decompressing...");
//		f.decompress(new File(path + name + ".comp"), new File(path + "ex_" + name), root);
//		System.out.println("Done!");
	}
}
