package table;

import java.util.Collection;
import java.util.HashMap;

import huffman.Files;
import huffman.TreeNode;

public class TableString implements HuffmanTable {
	private HashMap<String, TreeNode> map;

	public TableString() {
		map = new HashMap<>(1 << 20);
	}

	@Override
	public Collection<TreeNode> getValues() {
		return map.values();
	}

	@Override
	public TreeNode get(byte[] word) {
		return map.get(hash(word));
	}

	@Override
	public void coding(byte[] word, boolean[] code) {
		map.get(hash(word)).code = code;
	}

	@Override
	public void frequency(byte[] word) {
		String key = hash(word);

		TreeNode node = map.get(key);
		if (node == null)
			map.put(key, new TreeNode(word, 1));
		else
			node.inc_freq();
	}

	// I will reuse this variable for every function call to bytesToHex
	// This is better for both memory and performance
	private StringBuilder hexString = new StringBuilder(2 * Files.get_n());

	// Assuming hash.length = Files.get_n()
	private String hash(byte[] word) {
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
}
