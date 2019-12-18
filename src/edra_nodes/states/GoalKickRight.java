package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * se for falta para meu time, posiciono um atacante para realizar o chute
 *
 * se meu time fez falta, posiciono meu goleiro para defender
 * */
public class GoalKickRight extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.GOAL_KICK_RIGHT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
