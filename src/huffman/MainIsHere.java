package huffman;

import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import table.*;

public class MainIsHere {

	private static String compPath(String inPath) {
		return inPath + ".hc";
	}

	private static String decompPath(String inPath) {
		String original_path = inPath.substring(0, inPath.lastIndexOf('.'));
		int dot = original_path.lastIndexOf('.');

		return original_path.substring(0, dot) + "-d" + original_path.substring(dot);
	}

	private static void compressFile(String path, HuffmanTable huffTable) throws IOException {
		Files f = new Files();
		Tree_construct tree_c = new Tree_construct();
		File inFile = new File(path), outFile = new File(compPath(path));

		System.out.println("Analyzing file...");
		f.freq_table(inFile, huffTable);

		System.out.println("Codes generation and more analyzing...");
		TreeNode root = tree_c.make_tree(huffTable.getValues());

//		boolean[] route = new boolean[128 * Files.get_n()]; // edit this later

//		tree_c.coding_table(root, huffTable, route, 0);
		tree_c.coding_table(root, huffTable, new BitSet(), 0);

		// calculate unused bits, unused bytes
		Files.unusedBytes = (int) (Files.get_n() - inFile.length() % Files.get_n()) % Files.get_n();

		long usedBits = 0;
		for (TreeNode node : huffTable.getValues())
			usedBits += node.coding.len * node.get_freq();

		Files.unusedBits = (int) (8 - usedBits % 8) % 8;

		System.out.println("Compressing...");
		f.compress(inFile, outFile, huffTable, root);
		System.out.println("Done!");
	}

	private static void decompressFile(String inPath) throws IOException {
		Files f = new Files();
		File inFile = new File(inPath), outFile = new File(decompPath(inPath));

		System.out.println("Decompressing...");
		f.decompress(inFile, outFile);
		System.out.println("Done!");
	}

	public static void main(String[] args) {
		boolean debug = true;
//		boolean compress = true;
		String path = "files/gbbct10.seq";
		int n = 1;
		
		if(n < 1) {
			System.out.println("Error: n must be positive integer!");
			return;
		}
		
		HuffmanTable huffTable;
		if(n < 3)
			huffTable = new TableArray(n);
		else if (n < 5)
			huffTable = new TableInt(n);
		else if (n < 9)
			huffTable = new TableLong(n);
		else
			huffTable = new TableString();
			
		try {
			if (debug) {
				Files.set_n(n);
				long tik = System.nanoTime();
				compressFile(path, huffTable);
				long tok = System.nanoTime();
				System.out.println("Time: " + (tok - tik) / 1000000 + " ms");
				tik = System.nanoTime();
				decompressFile(compPath(path));
				tok = System.nanoTime();
				System.out.println("Time: " + (tok - tik) / 1000000 + " ms");
			}
//			else { 
//				if(compress) {
//					Files.set_n(n);
//					compressFile(path);
//				}
//				else {
//					decompressFile(compPath(path));
//				}
//			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

/*
 * assumptions: headers are written and read in one buffer
 */