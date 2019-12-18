package edra_nodes.conditions;

import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.util.List;

/**
 * Checa se a posse de bola está com time oponente
 *
 * se a bola esta mais proxima de um jogador de um jogador oponente
 */
@Deprecated
public class IfBallIsWithAllies extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        List<PlayerPerception> jogadores = agent.getFieldPerc().getAllPlayers();
        Vector2D ballPos = agent.getFieldPerc().getBall().getPosition();

        PlayerPerception closestPlayer = agent.getSelfPerc();
        double closestDistance = Double.MAX_VALUE;

        for (PlayerPerception currentPlayer: jogadores) {
            double playerDistance = currentPlayer.getPosition().distanceTo(ballPos);
            if (playerDistance < closestDistance) {
                closestDistance = playerDistance;
                closestPlayer = currentPlayer;
            }
        }

        if(closestPlayer.getSide() == agent.getSelfPerc().getSide())
            return BTStatus.SUCCESS;
        return BTStatus.FAILURE;
    }
}
