package edra_team.actions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

public class RetreatAccordingToHomePosition extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        Vector2D homePosition = agent.getHomePosition();

        Vector2D inicioDoCampo = (agent.getSelfPerc().getSide() == EFieldSide.LEFT) ?
                new Vector2D(52, homePosition.getY()) :
                new Vector2D(-52, homePosition.getY());;

        //Esta no fim do campo, nao pode mais avancar
        if(agent.isCloseTo(inicioDoCampo, 3)) {
            return BTStatus.SUCCESS;
        }

        //Se esta virado para o outro lado do campo, avancar
        if(agent.isAlignedTo(inicioDoCampo)) {
            agent.getCommander().doDashBlocking(100.0d);
        } else {
            agent.getCommander().doTurnToPoint(inicioDoCampo);
        }
        return BTStatus.RUNNING;
    }
}
