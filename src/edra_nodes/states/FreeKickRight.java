package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * tiro de meta do goleiro,
 * posiciono meus jogadores de linha para posicao de ataque
 */
public class FreeKickRight extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.FREE_KICK_RIGHT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}