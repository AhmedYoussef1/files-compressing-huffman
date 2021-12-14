package huffman;

import java.util.Arrays;
import java.util.Map;
import java.util.PriorityQueue;

import bits.Bits;

public class Tree_construct {
	
	public TreeNode make_tree(Map<String, TreeNode> table) {
		PriorityQueue<TreeNode> a = new PriorityQueue<>(table.values());
		
		while(a.size() > 1)
			a.add(new TreeNode(a.poll(), a.poll()));
		
		return a.poll();
	}
	
	public int coding_table(TreeNode node, Map<String, boolean[]> table, boolean[] route, int pos) {
		if (node.isTerminal()) {
			table.put(Bits.hashMe(node.data), Arrays.copyOfRange(route, 0, pos));
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
