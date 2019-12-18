package edra_team.actions;

import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;



public class GoGetBall extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
		
		//condicao desejada: perto da bola (dist < 3) 
		if (agent.isCloseTo(ballPos, 1.0)) {
			print("PERTO!");
			return BTStatus.SUCCESS;
		}

		//corre atras da bola e da um pequeno toque
		if (agent.isAlignedTo(ballPos)) {
			agent.getCommander().doDashBlocking(100.0d);
		} else {
			agent.getCommander().doTurnToPoint(ballPos);
		}
		
		return BTStatus.RUNNING;
	}
	
}
