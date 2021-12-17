package table;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import huffman.TreeNode;

public class TableArray implements HuffmanTable {

	TreeNode[] arr;
	Collection<TreeNode> coll;

	public TableArray(int n) {
		if (n < 3)
			arr = new TreeNode[1 << (n * 8)];
		else
			throw new RuntimeException("n must less than 3");
	}

	@Override
	public void frequency(byte[] word) {
		int key = toInt(word);

		if (arr[key] == null)
			arr[key] = new TreeNode(word, 1);
		else
			arr[key].inc_freq();
	}

	@Override
	public void coding(byte[] word, boolean[] code) {
		arr[toInt(word)].code = code;
	}

	@Override
	public Collection<TreeNode> getValues() {
		if (coll == null) {
			return Arrays.stream(arr).filter((e) -> e != null).collect(Collectors.toList());
		} else
			return coll;
	}

	@Override
	public TreeNode get(byte[] word) {
		return arr[toInt(word)];
	}

	private static int toInt(byte[] word) {
		if (word.length == 1)
			return 0xff & word[0];
		else
			return (0xff & word[0]) + ((0xff & word[1]) << 8);
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
//		byte[] word = {(byte)255, (byte)128};
//		System.out.println(toInt(word));
//		System.out.println(IntToBit(toInt(word)));
//	}

}
