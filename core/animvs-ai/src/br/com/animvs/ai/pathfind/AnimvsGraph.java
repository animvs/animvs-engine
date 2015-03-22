package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DALDEGAN on 21/03/2015.
 */
public class AnimvsGraph extends DefaultIndexedGraph<AnimvsTileNode> {
    public AnimvsGraph() {
        super();
    }

    public AnimvsGraph(Array<AnimvsTileNode> nodes) {
        super(nodes);
    }

    /*@Override
    public int getNodeCount() {
        return super.getNodeCount();
    }

    @Override
    public Array<Connection<AnimvsNode>> getConnections(AnimvsNode fromNode) {
        return super.getConnections(fromNode);
    }*/
}
