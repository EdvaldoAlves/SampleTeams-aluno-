package edra_team.actions;

import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;
import easy_soccer_lib.utils.Vector2D;


public class KickToScore extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
		
		//condicao ruim extrema: longe demais da bola
		if (!agent.isCloseTo(ballPos, 3.0)) {
			return BTStatus.FAILURE;
		}
		

		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				//da um chute com forca maxima (100)
				agent.getCommander().doKickToPoint(100.0d, agent.getGoalPosition());
				return BTStatus.SUCCESS;
			} else {
				//corre com forca intermediaria (porque esta perto da bola)
				agent.getCommander().doDashBlocking(60.0d);
			}
		} else {
			agent.getCommander().doTurnToPoint(ballPos);
		}
		return BTStatus.RUNNING;
	}

}
