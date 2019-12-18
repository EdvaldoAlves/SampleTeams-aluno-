package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * jogador mais próximo a bola chuta para outro jogador de seu time.
 *
 * este estado serve como um if, pois logo em seguida é chamado o pass ball
 */
public class IndirectFreeKickRight extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.INDIRECT_FREE_KICK_RIGHT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
