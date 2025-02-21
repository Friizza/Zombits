package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;
import java.awt.Rectangle;

public class BulletRefill {
    private GamePanel gp;
    public int worldX, worldY;
    public Texture texture;
    public Rectangle solidArea;
    public boolean isActive = true;

    public BulletRefill(GamePanel gp, int x, int y) {
        this.gp = gp;
        this.worldX = x;
        this.worldY = y;

        // Create collision area
        solidArea = new Rectangle(5, 5, GamePanel.tileSize - 10, GamePanel.tileSize - 10);
    }

    public void update() {
        // Check if player collides with this refill
        if (isActive && checkPlayerCollision()) {
            // Apply refill effect
            refillAmmo();
            isActive = false;
        }
    }

    private boolean checkPlayerCollision() {
        Rectangle playerRect = new Rectangle(
            gp.player.worldX + gp.player.solidArea.x,
            gp.player.worldY + gp.player.solidArea.y,
            gp.player.solidArea.width,
            gp.player.solidArea.height
        );

        Rectangle refillRect = new Rectangle(
            worldX + solidArea.x,
            worldY + solidArea.y,
            solidArea.width,
            solidArea.height
        );

        return playerRect.intersects(refillRect);
    }

    private void refillAmmo() {
        gp.gameSound.playSE(gp.gameSound.ammoPickup);

        int refillAmount = 60;
        gp.player.pistol.reserveAmmo += refillAmount;
        gp.player.rifle.reserveAmmo += refillAmount;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
