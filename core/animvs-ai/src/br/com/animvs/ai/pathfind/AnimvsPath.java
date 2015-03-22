package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by DALDEGAN on 22/03/2015.
 */
public final class AnimvsPath implements GraphPath<AnimvsTileNode> {
    private Array<AnimvsTileNode> nodes;

    public AnimvsPath() {
        this.nodes = new Array<AnimvsTileNode>();
    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public AnimvsTileNode get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(AnimvsTileNode node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<AnimvsTileNode> iterator() {
        return nodes.iterator();
    }
}
