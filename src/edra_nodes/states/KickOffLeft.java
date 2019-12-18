package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * Condicao, que retorna Sucess
 * se o estado da partida e KickOff
 * e Failure se nao eh
 * */
public class KickOffLeft extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.KICK_OFF_LEFT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }


}
