package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DALDEGAN on 21/03/2015.
 */
public abstract class AnimvsPathFindController {
    private Array<AnimvsTileNode> nodes;
    private TiledMapTileLayer tiles;

    private int levelPixelWidth;
    private int levelPixelHeight;

    private int mapWidth;
    private int mapHeight;

    private AnimvsGraph graph;

    private int usedNodesIndex;

    public final int getMapWidth() {
        return mapWidth;
    }

    public final int getMapHeight() {
        return mapHeight;
    }

    public final AnimvsGraph getGraph() {
        return graph;
    }

    public AnimvsPathFindController() {
    }

    final int createNodeIndex() {
        return usedNodesIndex++;
    }

    public void initialize() {
    }

     public AnimvsTileNode findNode(int x, int y) {
        int modX = x / levelPixelWidth;
        int modY = y / levelPixelHeight;

        return nodes.get(mapWidth * modY + modX);
    }

    public final void computeGraph(TiledMap map) {
        tiles = (TiledMapTileLayer) map.getLayers().get(0);

        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        int tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        int tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        levelPixelWidth = mapWidth * tilePixelWidth;
        levelPixelHeight = mapHeight * tilePixelHeight;

        //Create all Nodes:
        nodes = new Array<AnimvsTileNode>(mapWidth * mapHeight);
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                AnimvsTileNode node = new AnimvsTileNode(this);
                node.setType(AnimvsTileNode.Type.REGULAR);
                nodes.add(node);
            }
        }

        //Create all Nodes Connections:
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                //This is the current tile (node):
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);

                //Find neighbor tiles (nodes):
                TiledMapTileLayer.Cell up = tiles.getCell(x, y + 1);
                TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y - 1);
                TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);

                AnimvsTileNode targetNode = nodes.get(mapWidth * y + x);

                float COST = 1f;

                if (target == null) {
                    if (y != 0 && down == null) {
                        AnimvsTileNode downNode = nodes.get(mapWidth * (y - 1) + x);
                        if (testConnection(x, y, x, y - 1, targetNode, downNode))
                            targetNode.createConnection(downNode, COST);
                    }
                    if (x != 0 && left == null) {
                        AnimvsTileNode leftNode = nodes.get(mapWidth * y + x - 1);
                        if (testConnection(x, y, x - 1, y, targetNode, leftNode))
                            targetNode.createConnection(leftNode, COST);
                    }
                    if (x != mapWidth - 1 && right == null) {
                        AnimvsTileNode rightNode = nodes.get(mapWidth * y + x + 1);
                        if (testConnection(x, y, x + 1, y, targetNode, rightNode))
                            targetNode.createConnection(rightNode, COST);
                    }
                    if (y != mapHeight - 1 && up == null) {
                        AnimvsTileNode upNode = nodes.get(mapWidth * (y + 1) + x);
                        if (testConnection(x, y, x, y + 1, targetNode, upNode))
                            targetNode.createConnection(upNode, COST);
                    }
                }
            }
        }

        graph = new AnimvsGraph(nodes);
    }

    protected abstract boolean testConnection(int fromTileX, int fromTileY, int toTileX, int toTileY, AnimvsTileNode from, AnimvsTileNode to);
}
