package edra_nodes.states;

import easy_soccer_lib.utils.EMatchState;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * Escanteio, preparo meu atacante para chutar a bola
 *
 * se for escanteio contra meu time, posiciono meus jogadores na grande area
 */
public class CornerKickLeft extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EMatchState state = agent.getMatchPerc().getState();
        if(state == EMatchState.CORNER_KICK_LEFT) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
