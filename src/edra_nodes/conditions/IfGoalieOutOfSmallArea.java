package edra_nodes.conditions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.awt.*;

public class IfGoalieOutOfSmallArea extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EFieldSide side = agent.getSelfPerc().getSide();
        Rectangle area = (side == EFieldSide.LEFT) ?
                new Rectangle(-52, -9, 6, 18) :
                new Rectangle(46, -9, 6, 18);
        Vector2D currentPos = agent.getSelfPerc().getPosition();

        if(area.contains(currentPos.getX(), currentPos.getY())) {
            return BTStatus.SUCCESS;
        }

        return BTStatus.FAILURE;
    }
}
