package edra.behavior_tree;

public class TestMain {

	public static void main(String[] args) throws InterruptedException {
		BTNode<Integer> btree = createTree();

		while (true) {
			BTStatus st = btree.tick(4);
			System.out.println(st);
			
			Thread.sleep(4000);
		}

	}

	private static BTNode<Integer> createTree() {
		Sequence<Integer> seq1 = new Sequence<Integer>("Testa3");
		seq1.add(new SeIgual(3));
		seq1.add(new Print("TREEEEEES"));
		
		Sequence<Integer> seq2 = new Sequence<Integer>("Testa2");
		seq2.add(new SeIgual(2));
		seq2.add(new Print("dois"));
		
		Selector<Integer> root = new Selector<Integer>("RAIZ");
		root.add(seq1);
		root.add(seq2);
		
		return root;
	}

}


class SeIgual extends BTNode<Integer> {
	private int numberToCompare;
	
	SeIgual(int x) {
		this.numberToCompare = x;
	}
	
	@Override
	public BTStatus tick(Integer data) {
		if (data == this.numberToCompare) {
			return BTStatus.SUCCESS;
		}
		return BTStatus.FAILURE;
	}
	
}

class Print extends BTNode<Integer> {
	private String message;
	
	Print(String msg) {
		this.message = msg;
	}
	
	@Override
	public BTStatus tick(Integer data) {
		System.out.println("PRINTING: " + message);
		return BTStatus.SUCCESS;
	}
	
}
