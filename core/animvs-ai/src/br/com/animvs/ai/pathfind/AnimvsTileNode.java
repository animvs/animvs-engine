package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DALDEGAN on 21/03/2015.
 */
public class AnimvsTileNode implements IndexedNode<AnimvsTileNode> {
    private Array<Connection<AnimvsTileNode>> connections;

    private Type type;
    private int index;

    public final void setType(Type type) {
        this.type = type;
    }

    public AnimvsTileNode(AnimvsPathFindController controller) {
        this.connections = new Array<Connection<AnimvsTileNode>>();
        index = controller.createNodeIndex();

        /*this.tileX = tileX;
        this.tileY = tileY;
        this.position = new Vector2(tileX * controller.getMapWidth(), tileY * controller.getMapHeight());*/
    }

    @Override
    public Array<Connection<AnimvsTileNode>> getConnections() {
        return connections;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public void createConnection(AnimvsTileNode node, float cost) {
        connections.add(new AnimvsConnection(this, node, cost));
    }

    public enum Type {
        REGULAR
    }
}
