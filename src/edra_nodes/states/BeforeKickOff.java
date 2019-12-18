package edra_nodes.states;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.EMatchState;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

/**
 * Condicao, que retorna Sucess
 * se o estado da partida e PrepareToKick
 * e Failure se nao eh
 * */
public class BeforeKickOff extends BTNode<BTreePlayer>{
	@Override
    public BTStatus tick(BTreePlayer agent) {
		EMatchState state = agent.getMatchPerc().getState();

		if(state == EMatchState.BEFORE_KICK_OFF) {
			return BTStatus.SUCCESS;
		}
		return  BTStatus.FAILURE;
    }
}
