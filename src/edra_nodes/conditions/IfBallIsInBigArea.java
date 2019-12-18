package edra_nodes.conditions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.awt.*;

/**
 *
 *
 * Observa se a bola se encontra da grande area
 *
 *
 */
public class IfBallIsInBigArea extends BTNode<BTreePlayer> {

    @Override
    public BTStatus tick(BTreePlayer agent) {
        EFieldSide side = agent.getSelfPerc().getSide();
        Rectangle area = (side == EFieldSide.LEFT) ?
                new Rectangle(-51, -21, 16, 42) :
                new Rectangle(36, -21, 18, 42);
        Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();

        if(area.contains(ballPos.getX(), ballPos.getY())) {
            return BTStatus.SUCCESS;
        }

        return BTStatus.FAILURE;
    }
}
