package edra.behavior_tree;


public abstract class BTNode<T> {
	protected final String name;
	
	public BTNode(String name) {
		this.name = name;
	}
	
	public BTNode() {
		this.name = null;
	}
	
	/**
	 * Funcao principal. Pode implementar um teste de um condicao (retornando SUCCESS
	 * ou FAILURE) ou uma acao propriamente dita. 
	 * 
	 * Recebe uma classe do tipo T, por meio da qual, a 
	 * arvore pode ler dados do agent e informar acoes para o agente realizar.
	 */
	public abstract BTStatus tick(T agent);
	
	@Override
	public String toString() {
		if (this.name != null) {
			return this.name;
		}
		return super.toString();
	}

	
	/**
	 * Uma funcao para facilitar debugging. Imprime 1 vez a cada T milissegundos.
	 */
	protected void printTimed(int T, Object ... args) {
		if (System.currentTimeMillis() > lastPrintTime + T) {
			for (Object object : args) {
				System.out.print(object);
			}
			System.out.println();
			this.lastPrintTime = System.currentTimeMillis();
		}
	}
	
	protected void print(Object ... args) {
		for (Object object : args) {
			System.out.print(object);
		}
		System.out.println();
	}

	private long lastPrintTime;

}
