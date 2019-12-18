package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * Condicao, que retorna Sucess
 * se o estado da partida e PlayOn
 * e Failure se nao eh
 * */
public class PlayOn extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        return agent.getMatchPerc().getState() == EMatchState.PLAY_ON ?
                BTStatus.SUCCESS : BTStatus.FAILURE;
    }
}
