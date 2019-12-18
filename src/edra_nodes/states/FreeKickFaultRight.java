package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * jogador mais proximo a bola a chuta em direção ao gol
 */
public class FreeKickFaultRight extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.FREE_KICK_FAULT_RIGHT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}