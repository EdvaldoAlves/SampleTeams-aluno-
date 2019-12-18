package edra_nodes.conditions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.awt.*;


public class IfBallIsInMid extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EFieldSide side = agent.getSelfPerc().getSide();
        Rectangle fieldAttack = new Rectangle(-18,-34, 34, 68);
        Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();
        if(fieldAttack.contains(ballPos.getX(), ballPos.getY())) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
