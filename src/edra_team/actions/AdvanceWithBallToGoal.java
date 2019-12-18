package edra_team.actions;



import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;



public class AdvanceWithBallToGoal extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
		
		//condicao ruim extrema: longe da bola
		if (!agent.isCloseTo(ballPos, 10.0)) {
			return BTStatus.FAILURE;
		}
		
		//resultado desejado atingido: perto da bola (dist < 3) e perto do goal (dist < 20)
		if (agent.isCloseTo(ballPos, 3.0) && agent.isCloseTo(agent.getGoalPosition(), 20)) {
			return BTStatus.SUCCESS;
		}
		
		//caso intermediario: razoavelmente perto da bola 
		//                    corre atras da bola e da um pequeno toque
		if (agent.isAlignedTo(ballPos)) {
			if (agent.isCloseTo(ballPos, 1.0)) {
				agent.getCommander().doKickToPoint(40.0d, agent.getGoalPosition()); //da um toque adiante (forca baixa)
			} else {
				agent.getCommander().doDashBlocking(100.0d); //chega mais perto da bola
			}
		} else {
			agent.getCommander().doTurnToPoint(ballPos);
		}
		
		return BTStatus.RUNNING;
	}
	
}
