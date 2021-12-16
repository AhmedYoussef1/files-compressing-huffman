package table;

import java.util.Collection;

import huffman.TreeNode;

public interface HuffmanTable {
	public void frequency(byte[] word);

	public void coding(byte[] word, boolean[] code);

	public Collection<TreeNode> getValues();

	public TreeNode get(byte[] word);
}
