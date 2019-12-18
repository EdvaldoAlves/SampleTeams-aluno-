package edra_nodes.conditions;

import easy_soccer_lib.utils.EFieldSide;
import easy_soccer_lib.utils.Vector2D;
import edra.BTreePlayer;
import edra.behavior_tree.BTNode;
import edra.behavior_tree.BTStatus;

import java.awt.*;

/**
 *CHECA SE O JOGADOR FICA POSICIONADO NO MEIO DO CAMPO
 */
public class IfPlayerIsInMiddle extends BTNode<BTreePlayer> {
    @Override
    public BTStatus tick(BTreePlayer agent) {
        EFieldSide side = agent.getSelfPerc().getSide();
        Rectangle fieldAttack = new Rectangle(-18,-34, 34, 68);
        Vector2D currentPos = agent.getSelfPerc().getPosition();
        if(fieldAttack.contains(currentPos.getX(), currentPos.getY())) {
            return BTStatus.SUCCESS;
        }
        return BTStatus.FAILURE;
    }
}
