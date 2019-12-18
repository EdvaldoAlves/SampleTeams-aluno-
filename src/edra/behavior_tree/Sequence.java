package edra.behavior_tree;

import java.util.LinkedList;

public class Sequence<T> extends BTNode<T> {
	private LinkedList<BTNode<T>> list;
	
	public Sequence() {
		this.list = new LinkedList<BTNode<T>>();
	}

	public Sequence(String name) {
		super("SEQ-" + name);
		this.list = new LinkedList<BTNode<T>>();
	}
	
	public void add(BTNode<T> node) {
		this.list.add(node);
	}

	@Override
	public BTStatus tick(T agentInterface) {
		BTNode<T> node;
		BTStatus status;
		
		if (this.list.isEmpty()) {
			print("ATENCAO: ", super.name, " vazio!");
		}
		
		for (int i = 0; i < list.size(); i++) {
			node = this.list.get(i);
			
			status = node.tick(agentInterface);
			switch (status) {
			case SUCCESS:
				break;
			case FAILURE:
				print(super.name, " -- FAILURE in ", node);
				return BTStatus.FAILURE;
			case RUNNING:
				print(super.name, " -- RUNNING ", node);
				return BTStatus.RUNNING;
			default:
				throw new IllegalArgumentException("Unexpected value: " + status);
			}
		}
		
		print(super.name, " -- SUCCESS (all)");
		return BTStatus.SUCCESS;
	}

}
