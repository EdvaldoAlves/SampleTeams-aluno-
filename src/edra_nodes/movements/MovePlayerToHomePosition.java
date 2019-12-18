package edra_nodes.movements;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

//é usada no tratamento do estado BeforeKickOff
//move o jogador para sua home position instantaneamente
public class MovePlayerToHomePosition extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EFieldSide side = agent.getSelfPerc().getSide();
        Vector2D homePosition = agent.getHomePosition();

        if(side == EFieldSide.RIGHT) {
            homePosition.setX(-homePosition.getX());
            homePosition.setY(-homePosition.getY()); 
        }

        //Este if serve para evitar de recolocar o jogador na home position, caso ele
        //ja esteja posicionado nela. Economiza processamento do servidor
        Vector2D currentPos = agent.getSelfPerc().getPosition();
        if(homePosition.getX() == currentPos.getX() && homePosition.getY() == currentPos.getY()) {
            return BTStatus.SUCCESS;
        }

        agent.getCommander().doMoveBlocking(homePosition.getX(), homePosition.getY());
        return BTStatus.SUCCESS;
    }
}
