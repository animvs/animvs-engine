package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 * Created by DALDEGAN on 22/03/2015.
 */
public class AnimvsHeuristic implements Heuristic<AnimvsTileNode> {
    private AnimvsPathFindController controller;

    public AnimvsHeuristic(AnimvsPathFindController controller) {
        if (controller == null)
            throw new RuntimeException("The parameter 'controller' must be != NULL");

        this.controller = controller;
    }

    @Override
    public float estimate(AnimvsTileNode node, AnimvsTileNode endNode) {
        int startX = node.getIndex() % controller.getMapTilesWidth();
        int startY = node.getIndex() / controller.getMapTilesWidth();

        int endX = endNode.getIndex() % controller.getMapTilesWidth();
        int endY = endNode.getIndex() / controller.getMapTilesWidth();

        float distance = Math.abs(startX - endX) + Math.abs(startY - endY);

        return distance;
    }
}
