package table;

import java.util.Collection;
import java.util.HashMap;

import huffman.TreeNode;

public class TableInt implements HuffmanTable {
	private HashMap<Integer, TreeNode> map;

	public TableInt(int n) {
		if(n < 5)
			map = new HashMap<>(1 << 20);
		else
			throw new RuntimeException("n must be less than 5");
	}

	@Override
	public Collection<TreeNode> getValues() {
		return map.values();
	}

	@Override
	public TreeNode get(byte[] word) {
		return map.get(toInt(word));
	}

	@Override
	public void coding(byte[] word, boolean[] code) {
		map.get(toInt(word)).code = code;
	}

	@Override
	public void frequency(byte[] word) {
		int key = toInt(word);

		TreeNode node = map.get(key);
		if (node == null)
			map.put(key, new TreeNode(word, 1));
		else
			node.inc_freq();
	}

	// Assuming hash.length = Files.get_n()
	private static int toInt(byte[] word) {
		int val = 0;
		for (byte b : word) {
			val <<= 8;
			val |= (0xff & b);
		}
		return val;
	}
	
//	public static String IntToBit(long num) {
//	StringBuilder str = new StringBuilder(32);
//	for (long i = 31; i >= 0; i--) {
//		if ((num & (long) 1 << i) != 0)
//			str.append('1');
//		else
//			str.append('0');
//	}
//	return str.toString();
//}
//	
//	public static void main(String[] args) {
//		byte[] word = {(byte)128, (byte)255, (byte)255, (byte)128};
//		System.out.println(toInt(word));
//		System.out.println(IntToBit(toInt(word)));
//	}
}
