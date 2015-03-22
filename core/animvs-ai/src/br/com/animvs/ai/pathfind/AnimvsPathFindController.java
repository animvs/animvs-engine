package br.com.animvs.ai.pathfind;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by DALDEGAN on 21/03/2015.
 */
public abstract class AnimvsPathFindController {
    private Array<AnimvsTileNode> nodes;
    //private TiledMapTileLayer tiles;

    private int levelPixelWidth;
    private int levelPixelHeight;

    private int mapTilesWidth;
    private int mapTilesHeight;
    private int tilePixelWidth;
    private int tilePixelHeight;

    private AnimvsGraph graph;

    private int usedNodesIndex;

    public final int getMapTilesWidth() {
        return mapTilesWidth;
    }

    public final int getMapTilesHeight() {
        return mapTilesHeight;
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

    public AnimvsTileNode findNode(int tileX, int tileY) {
        int modX = tileX / tilePixelWidth;
        int modY = tileY / tilePixelHeight;

        return nodes.get(mapTilesWidth * modY + modX);
    }

    public Vector2 findNodePosition(int index) {
        int y = MathUtils.ceil(index / mapTilesWidth );
        int x = index % mapTilesWidth;

        return new Vector2(x * tilePixelWidth, y * tilePixelHeight);
        //AnimvsTileNode downNode = nodes.get(mapTilesWidth * (y - 1) + x);
    }

    public final void computeGraph(TiledMap map) {
        //tiles = (TiledMapTileLayer) map.getLayers().get("ground");

        mapTilesWidth = map.getProperties().get("width", Integer.class);
        mapTilesHeight = map.getProperties().get("height", Integer.class);
        tilePixelWidth = map.getProperties().get("tilewidth", Integer.class);
        tilePixelHeight = map.getProperties().get("tileheight", Integer.class);

        levelPixelWidth = mapTilesWidth * tilePixelWidth;
        levelPixelHeight = mapTilesHeight * tilePixelHeight;

        //Create all Nodes:
        nodes = new Array<AnimvsTileNode>(mapTilesWidth * mapTilesHeight);
        for (int y = 0; y < mapTilesHeight; y++) {
            for (int x = 0; x < mapTilesWidth; x++) {
                AnimvsTileNode node = new AnimvsTileNode(this, x, y);
                node.setType(AnimvsTileNode.Type.REGULAR);
                nodes.add(node);
            }
        }

        int totalGeneratedConnections = 0;

        //Create all Nodes Connections:
        for (int y = 0; y < mapTilesHeight; y++) {
            for (int x = 0; x < mapTilesWidth; x++) {
                /*//This is the current tile (node):
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);

                //Find neighbor tiles (nodes):
                TiledMapTileLayer.Cell up = tiles.getCell(x, y + 1);
                TiledMapTileLayer.Cell right = tiles.getCell(x + 1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y - 1);
                TiledMapTileLayer.Cell left = tiles.getCell(x - 1, y);*/

                AnimvsTileNode targetNode = nodes.get(mapTilesWidth * y + x);

                float COST = 1f;

                /*if (target == null) {*/
                if (y != 0 /*&& down == null*/) {
                    AnimvsTileNode downNode = nodes.get(mapTilesWidth * (y - 1) + x);
                    if (testConnection(x, y, x, y - 1, targetNode, downNode)) {
                        targetNode.createConnection(downNode, COST);
                        totalGeneratedConnections++;
                    }
                }
                if (x != 0 /*&& left == null*/) {
                    AnimvsTileNode leftNode = nodes.get(mapTilesWidth * y + x - 1);
                    if (testConnection(x, y, x - 1, y, targetNode, leftNode)) {
                        targetNode.createConnection(leftNode, COST);
                        totalGeneratedConnections++;
                    }
                }
                if (x != mapTilesWidth - 1 /*&& right == null*/) {
                    AnimvsTileNode rightNode = nodes.get(mapTilesWidth * y + x + 1);
                    if (testConnection(x, y, x + 1, y, targetNode, rightNode)) {
                        targetNode.createConnection(rightNode, COST);
                        totalGeneratedConnections++;
                    }
                }
                if (y != mapTilesHeight - 1 /*&& up == null*/) {
                    AnimvsTileNode upNode = nodes.get(mapTilesWidth * (y + 1) + x);
                    if (testConnection(x, y, x, y + 1, targetNode, upNode)) {
                        targetNode.createConnection(upNode, COST);
                        totalGeneratedConnections++;
                    }
                }
                /*}*/
            }
        }

        Gdx.app.log("AI", "Pathfinder has generated " + totalGeneratedConnections + " connections out of " + (mapTilesWidth * mapTilesHeight) + " tiles (~" + (int) ((float) totalGeneratedConnections / (mapTilesWidth * mapTilesHeight * 4f) * 100f) + "% of all tiles are passable)");

        graph = new AnimvsGraph(nodes);
    }

    protected abstract boolean testConnection(int fromTileX, int fromTileY, int toTileX, int toTileY, AnimvsTileNode from, AnimvsTileNode to);
}
