package edra_team.actions;

import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;


public class CatchBall extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();

        //corre atras da bola e da um pequeno toque
        if (agent.isAlignedTo(ballPos)) {
            agent.getCommander().doDashBlocking(85.0d);
        } else {
            agent.getCommander().doTurnToPointBlocking(ballPos);
        }

        //condicao desejada: perto da bola (dist < 3)
        if (agent.isCloseTo(ballPos, 1.0)) {
            agent.getCommander().doCatchBlocking(0);
            return BTStatus.SUCCESS;
        }

        return BTStatus.RUNNING;
    }
}
