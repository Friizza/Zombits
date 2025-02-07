package com.zombits.main;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionChecker {

    GamePanel gp;
    TiledMapTileLayer collisionLayer;
    TiledMapTileLayer.Cell cell;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkTileCollision(float x, float y) {
        // CALCULATE THE TILES THAT PLAYER'S SOLID AREA WILL OCCUPY
        int leftTileX = (int)((x + gp.player.solidArea.x) / GamePanel.tileSize);
        int rightTileX = (int)((x + gp.player.solidArea.x + gp.player.solidArea.width) / GamePanel.tileSize);
        int topTileY = (int)((y + gp.player.solidArea.y) / GamePanel.tileSize);
        int bottomTileY = (int)((y + gp.player.solidArea.y + gp.player.solidArea.height) / GamePanel.tileSize);

        collisionLayer = (TiledMapTileLayer)gp.map.getLayers().get("Collisions");

        // Check all tiles the solid area touches
        for (int tileX = leftTileX; tileX <= rightTileX; tileX++) {
            for (int tileY = topTileY; tileY <= bottomTileY; tileY++) {
                cell = collisionLayer.getCell(tileX, tileY);
                if (cell != null && cell.getTile().getProperties().containsKey("collision")) {
                    return true;
                }
            }
        }
        return false;
    }
}
