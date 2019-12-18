package edra_team.actions;

import easy_soccer_lib.perception.PlayerPerception;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.util.List;

/**
 * tenta passar para o jogador mais proximo e menos marcado
 */
public class PassBall extends BTNode<BTreePlayer> {
//ordeno jogadores aliados de acordo com a distancia
    //checo qual o com menos marcacao e chuto para ele
    @Override
    public BTStatus tick(BTreePlayer agent) {
        Vector2D ballPosition = agent.getFieldPerc().getBall().getPosition();
        List<PlayerPerception> myTeam = agent.getFieldPerc().getTeamPlayers(agent.getSelfPerc().getSide());

        //removendo o this.agent da lista
        PlayerPerception thisPlayer = null;
        for(int i = 0 ; i < myTeam.size() ; i++) {
            if(myTeam.get(i).getUniformNumber() == agent.getSelfPerc().getUniformNumber()) {
                thisPlayer = myTeam.get(i);
                myTeam.remove(myTeam.get(i));
            }
        }

        PlayerPerception closestPlayer = null;
        double closestDistance = Double.MAX_VALUE;

        //buscando jogador mais proximo
        for (PlayerPerception player : myTeam) {
            double playerDistance = player.getPosition().distanceTo(agent.getSelfPerc().getPosition());
            if (playerDistance < closestDistance) {
                closestDistance = playerDistance;
                closestPlayer = player;
            }
        }

        if(!agent.isCloseTo(ballPosition, 1.0d)) {
            return BTStatus.FAILURE;
        }

        agent.getCommander().doKickToPoint(85, closestPlayer.getPosition());
        return BTStatus.SUCCESS;

    }
}
