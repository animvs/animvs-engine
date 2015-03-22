package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by DALDEGAN on 22/03/2015.
 */
public class AnimvsConnection implements Connection<AnimvsTileNode> {
    private AnimvsTileNode toNode;
    private AnimvsTileNode fromNode;
    private float cost;

    public AnimvsConnection(AnimvsTileNode fromNode, AnimvsTileNode toNode, float cost) {
        this.toNode = toNode;
        this.fromNode = fromNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public AnimvsTileNode getFromNode() {
        return fromNode;
    }

    @Override
    public AnimvsTileNode getToNode() {
        return toNode;
    }
}
