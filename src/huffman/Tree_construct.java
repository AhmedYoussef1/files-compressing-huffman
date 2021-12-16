package huffman;

import java.util.Arrays;
import java.util.Collection;
import java.util.PriorityQueue;

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

	public static void main(String[] args) {
//		Tree_construct tc = new Tree_construct();
//		
//		Map<String, TreeNode> table = new HashMap<>();
//		
//		table.put("a", new TreeNode("a".getBytes(), 2));
//		table.put("b", new TreeNode("b".getBytes(), 3));
//		table.put("c", new TreeNode("c".getBytes(), 6));
//		
//		TreeNode root = tc.make_tree(table);
//		Map<String, boolean[]> cod_table = new HashMap<>();
//		boolean[] route = new boolean[10];
//		
//		System.out.println(tc.coding_table(root, cod_table, route, 0));

		System.out.println();
	}
}
