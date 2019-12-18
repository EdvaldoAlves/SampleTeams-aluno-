package edra_team.actions;

import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class ReturnToHomePosition extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		//se distancia maior que 50, voltar para posicao inicial
		Vector2D homePosition = agent.getHomePosition();

		//Se ja esta proximo da homePosition, retorna sucesso
		if(agent.isCloseTo(homePosition, 1.0d)) {
			return BTStatus.SUCCESS;
		}

		//Se esta virado para a homePosition, entao caminhe ate ela
		//senao, vire na direcao dela
		if (agent.isAlignedTo(homePosition)) {
			agent.getCommander().doDashBlocking(50.0d); //chega mais perto da bola
		} else {
			agent.getCommander().doTurnToPoint(homePosition);
		}

		return BTStatus.RUNNING;
	}

}
