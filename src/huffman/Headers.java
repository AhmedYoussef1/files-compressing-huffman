package huffman;

import java.nio.ByteBuffer;
import java.util.Arrays;

import bits.Bits;

public class Headers {
	public void writeHeaders(Bits bits, int n, int unusedBytes, int unusedBits, TreeNode tree) {
		ByteBuffer bb = ByteBuffer.allocate(9);
		bb.putInt(n);
		bb.putInt(unusedBytes);
		bb.put((byte) unusedBits);

		bits.append(bb.array());
		encodeTree(tree, bits);
	}

	public int readHeaders(byte[] buffer, TreeNode tree) {
		int n = ByteBuffer.wrap(buffer, 0, 5).getInt();
		int unusedBytes = ByteBuffer.wrap(buffer, 4, 4).getInt();
		int unusedBits = buffer[8];

		Files.set_n(n);
		Files.unusedBytes = unusedBytes;
		Files.unusedBits = unusedBits;

		return decodeTree(tree, buffer, 9 * 8);
	}

	private void encodeTree(TreeNode node, Bits treeDFS) {
		if (node.data != null) {
			treeDFS.appendZero();
			treeDFS.append(node.data);
			return;
		}
		treeDFS.appendOne();
		encodeTree(node.one(), treeDFS);
		encodeTree(node.zero(), treeDFS);
	}

	private int decodeTree(TreeNode node, byte[] treeDFS, int pos) {
		if ((treeDFS[pos / 8] & (1 << (pos % 8))) > 0) {
			pos = decodeTree(node.oneForce(), treeDFS, pos + 1);
			pos = decodeTree(node.zeroForce(), treeDFS, pos);

			return pos;
		} else {
			node.data = readData(treeDFS, pos + 1);
			return pos + Files.get_n() * 8 + 1;
		}
	}

	private byte[] readData(byte[] treeDFS, int pos) {
		byte data[] = new byte[Files.get_n()];

		for (int i = 0; i < Files.get_n() * 8; i++, pos++) {
			if ((treeDFS[pos / 8] & 1 << (pos % 8)) > 0)
				data[i / 8] = (byte) (data[i / 8] | 1 << (i % 8));
		}

		return data;
	}

	public boolean identicalTrees(TreeNode a, TreeNode b) {
		if (a == null && b == null)
			return true;

		if (a != null && b != null)
			return (Arrays.equals(a.data, b.data) && identicalTrees(a.zero(), b.zero())
					&& identicalTrees(a.one(), b.one()));

		return false;
	}

	public static void main(String[] args) {
//		Headers h = new Headers();
//		Bits header_data = new Bits(1 << 16);
//
//		Tree_construct tc = new Tree_construct();
//
//		Map<String, TreeNode> table = new HashMap<>();
//
//		table.put("a", new TreeNode("a".getBytes(), 45));
//		table.put("b", new TreeNode("b".getBytes(), 13));
//		table.put("c", new TreeNode("c".getBytes(), 12));
//		table.put("d", new TreeNode("a".getBytes(), 16));
//		table.put("e", new TreeNode("b".getBytes(), 9));
//		table.put("f", new TreeNode("c".getBytes(), 5));
//
//		TreeNode root = tc.make_tree(table);
//
//		h.writeHeaders(header_data, 1, 3, 6, root);
//
//		TreeNode root_decoded = new TreeNode();
//
//		byte[] buffer = header_data.bytes_plusOne();
//		buffer = Arrays.copyOfRange(buffer, 0, buffer.length);
//
//		System.out.println(h.readHeaders(buffer, root_decoded));
//		System.out.println(h.identicalTrees(root, root_decoded));
	}
}
