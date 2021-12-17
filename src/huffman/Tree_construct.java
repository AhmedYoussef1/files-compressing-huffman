package huffman;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.PriorityQueue;

import bits.Code;
import table.HuffmanTable;

public class Tree_construct {

	public TreeNode make_tree(Collection<TreeNode> nodes) {
		PriorityQueue<TreeNode> a = new PriorityQueue<>(nodes);

		while (a.size() > 1)
			a.add(new TreeNode(a.poll(), a.poll()));

		return a.poll();
	}

	public int coding_table(TreeNode node, HuffmanTable table, boolean[] route, int pos) {
		if (node.isTerminal()) {
			table.coding(node.data, Arrays.copyOfRange(route, 0, pos));
			return pos;
		}

		route[pos] = true;
		int one = coding_table(node.one(), table, route, pos + 1);
		route[pos] = false;
		int zero = coding_table(node.zero(), table, route, pos + 1);

		return (one > zero) ? one : zero;
	}
	
	public void coding_table(TreeNode node, HuffmanTable table, BitSet route, int pos) {
		if (node.isTerminal()) {
			byte des[] = new byte[pos/8 + 1];
			byte src[] = route.toByteArray();
			System.arraycopy(route.toByteArray(), 0, des, 0, Math.min(src.length, des.length));
			
			table.get(node.data).coding = new Code(des.clone(), pos);
			return;
		}
		
		route.set(pos);
		coding_table(node.one(), table, route, pos + 1);
		route.clear(pos);
		coding_table(node.zero(), table, route, pos + 1);
	}

	public static void main(String[] args) {
//		Tree_construct tc = new Tree_construct();
//		
//		HuffmanTable table = new TableArray(1);
//		
//		table.frequency("a".getBytes()); table.frequency("a".getBytes());
//		table.frequency("b".getBytes()); table.frequency("b".getBytes()); table.frequency("b".getBytes());
//		table.frequency("c".getBytes()); table.frequency("c".getBytes()); table.frequency("c".getBytes());
//		table.frequency("c".getBytes()); table.frequency("c".getBytes()); table.frequency("c".getBytes());
//		
//		TreeNode root = tc.make_tree(table.getValues());
//		
//		BitSet bs = new BitSet();
//		
//		tc.coding_table(root, table, bs, 0);
//
//		System.out.println();
	}
}
