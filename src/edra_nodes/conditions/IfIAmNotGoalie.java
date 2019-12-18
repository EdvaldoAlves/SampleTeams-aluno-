package edra_nodes.conditions;

import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class IfIAmNotGoalie extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        return agent.getSelfPerc().getUniformNumber() != 1 ? BTStatus.SUCCESS: BTStatus.FAILURE;
    }
}
