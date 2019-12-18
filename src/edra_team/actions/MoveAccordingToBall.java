package edra_team.actions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class MoveAccordingToBall extends BTNode<BTreePlayer> {
	
	@Override
	public BTStatus tick(BTreePlayer agent) {

		Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
		Vector2D playerPos = agent.getSelfPerc().getPosition();

		if(agent.getSelfPerc().getSide() == EFieldSide.LEFT && ballPos.getX() < 0) {
			posGoleiro(agent, ballPos, playerPos,0);
		}
		else if (agent.getSelfPerc().getSide() == EFieldSide.RIGHT && ballPos.getX()>0){
			posGoleiro(agent, ballPos, playerPos,-1);
		}
		return BTStatus.RUNNING;

	}
	
	private boolean posGoleiro(BTreePlayer agent, Vector2D ballPos, Vector2D playerPos, double whereToLook ) {
		if(ballPos.getY() < playerPos.getY()) {
			if(agent.getSelfPerc().getPosition().getY() > -5.75) {
				agent.getCommander().doTurnToDirection(new Vector2D(0, -7));
				agent.getCommander().doDashBlocking(100);
			}
			else {
				agent.getCommander().doTurnToDirection(new Vector2D(whereToLook, 0));
			}
			return true;
		}
		else if(ballPos.getY() > playerPos.getY()+1.3) { //esse 1.3 eh pra ter certeza que ele vai ter o turntodirection completo antes do dash
			if(agent.getSelfPerc().getPosition().getY() < 5.75) {
				agent.getCommander().doTurnToDirection(new Vector2D(0, 7));
				agent.getCommander().doDashBlocking(100);
			}
			else {
				agent.getCommander().doTurnToDirection(new Vector2D(whereToLook, 0));
			}
			return true;
		}
		return false;
	}
}
