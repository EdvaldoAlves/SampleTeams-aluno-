package edra_nodes.conditions;

import java.util.List;

import easy_soccer_lib.perception.FieldPerception;
import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class IfClosestPlayerToBall extends BTNode<BTreePlayer> {

	@Override
	public BTStatus tick(BTreePlayer agent) {
		PlayerPerception selfPerc = agent.getSelfPerc();
		FieldPerception fieldPerc = agent.getFieldPerc();
		
		Vector2D ballPosition = fieldPerc.getBall().getPosition();
		List<PlayerPerception> myTeam = fieldPerc.getTeamPlayers(selfPerc.getSide());
		
		PlayerPerception closestPlayer = agent.getSelfPerc();
		double closestDistance = Double.MAX_VALUE;
		
		for (PlayerPerception player : myTeam) {
			double playerDistance = player.getPosition().distanceTo(ballPosition);
			if (playerDistance < closestDistance) {
				closestDistance = playerDistance;
				closestPlayer = player;
			}
		}
		
//		print(5000, "No ", selfPerc.getUniformNumber(), ", pos: ",
//				selfPerc.getPosition(),	", TEAM ", selfPerc.getSide(),
//				", CLOSEST? " + (closestPlayer.getUniformNumber() == selfPerc.getUniformNumber()));
		
		if (closestPlayer.getUniformNumber() == selfPerc.getUniformNumber()) {
			return BTStatus.SUCCESS;
		} else {
			return BTStatus.FAILURE;
		}
	}

}
