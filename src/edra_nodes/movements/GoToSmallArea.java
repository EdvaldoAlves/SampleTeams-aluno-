package edra_nodes.movements;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class GoToSmallArea extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
        
        if(agent.getSelfPerc().getSide() == EFieldSide.LEFT) {
        	if(agent.getSelfPerc().getUniformNumber() == 6) {
        		
        		agent.getCommander().doMoveBlocking(ballPos.getX()-15,ballPos.getY()+10);
        	}
        	if(agent.getSelfPerc().getUniformNumber() == 5) {
        		agent.getCommander().doMoveBlocking(ballPos.getX()-30,ballPos.getY()+20);
        	}
        	 return BTStatus.SUCCESS;
        }
        else if(agent.getSelfPerc().getSide() == EFieldSide.RIGHT) {
        	if(agent.getSelfPerc().getUniformNumber() == 6) {
        		
        		agent.getCommander().doMoveBlocking(ballPos.getX()-15,ballPos.getY()+10);
        	}
        	if(agent.getSelfPerc().getUniformNumber() == 5) {
        		agent.getCommander().doMoveBlocking(ballPos.getX()-30,ballPos.getY()+20);
        	}
        	 return BTStatus.SUCCESS;
        }
      
        return BTStatus.RUNNING;
    }
}
