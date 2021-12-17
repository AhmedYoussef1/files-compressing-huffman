package table;

import java.util.Collection;
import java.util.HashMap;

import huffman.TreeNode;

public class TableLong implements HuffmanTable {
	private HashMap<Long, TreeNode> map;

	public TableLong(int n) {
		if(n < 9)
			map = new HashMap<>(1 << 20);
		else
			throw new RuntimeException("n must be less than 9");
	}

	@Override
	public Collection<TreeNode> getValues() {
		return map.values();
	}

	@Override
	public TreeNode get(byte[] word) {
		return map.get(toLong(word));
	}

	@Override
	public void coding(byte[] word, boolean[] code) {
		map.get(toLong(word)).code = code;
	}

	@Override
	public void frequency(byte[] word) {
		long key = toLong(word);

		TreeNode node = map.get(key);
		if (node == null)
			map.put(key, new TreeNode(word, 1));
		else
			node.inc_freq();
	}

	private static long toLong(byte[] word) {
		long val = 0L;
		for (byte b : word) {
			val <<= 8;
			val |= (0xff & b);
		}
		return val;
	}
	
//	public static String longToBit(long num) {
//		StringBuilder str = new StringBuilder(64);
//		for (long i = 63; i >= 0; i--) {
//			if ((num & (long) 1 << i) != 0)
//				str.append('1');
//			else
//				str.append('0');
//		}
//		return str.toString();
//	}
//	
//	public static void main(String[] args) {
//		byte[] word = {2, (byte)128, (byte)128, (byte)128, (byte)128, (byte)128, (byte)128, (byte)255};
//		System.out.println(longToBit(toLong(word)));
//	}
}
