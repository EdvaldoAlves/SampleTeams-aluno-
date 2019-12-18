package edra_nodes.conditions;

import easy_soccer_lib.utils.EFieldSide;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class IfRightSide extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        return agent.getSelfPerc().getSide() == EFieldSide.RIGHT ?
                BTStatus.SUCCESS :
                BTStatus.FAILURE;
    }
}
