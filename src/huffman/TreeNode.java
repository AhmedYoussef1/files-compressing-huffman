package huffman;

public class TreeNode implements Comparable<TreeNode> {
	public byte[] data;
	public boolean[] code;

	private int freq;
	private TreeNode Zero;
	private TreeNode One;

	public TreeNode() {
	}

	public TreeNode(byte[] data, int freq) {
		this.data = data;
		this.freq = freq;
	}

	public TreeNode(TreeNode zero, TreeNode one) {
		this.Zero = zero;
		this.One = one;
		this.freq = zero.freq + one.freq;
	}

	public int inc_freq() {
		return ++this.freq;
	}

	public int get_freq() {
		return this.freq;
	}

	public boolean isTerminal() {
		return this.data != null;
	}

	public TreeNode zero() {
		return this.Zero;
	}

	public TreeNode one() {
		return this.One;
	}

	public TreeNode zeroForce() {
		if (this.Zero == null)
			this.Zero = new TreeNode();
		return this.Zero;
	}

	public TreeNode oneForce() {
		if (this.One == null)
			this.One = new TreeNode();
		return this.One;
	}

	@Override
	public int compareTo(TreeNode a) {
		return (this.freq > a.freq) ? 1 : ((this.freq == a.freq) ? 0 : -1);
	}

}
