package com.zombits.main.zombie;

import com.badlogic.gdx.graphics.Texture;
import com.zombits.main.GamePanel;

import java.awt.Rectangle;

public class Zombie {
    GamePanel gp;

    public float worldX, worldY;
    public float speed = 1f;
    public Rectangle solidArea;

    // Animation properties
    public Texture left, right;
    public Texture left1, left2, left3;
    public Texture right1, right2, right3;
    public String direction = "left";
    public int spriteCounter = 0;
    public int spriteNum = 1;

    // Status
    public int maxHealth = 100;
    public int currentHealth = maxHealth;
    public boolean isAlive = true;

    public Zombie(GamePanel gp, float x, float y) {
        this.gp = gp;
        this.worldX = x;
        this.worldY = y;

        // Set up collision area
        solidArea = new Rectangle();
        solidArea.x = 15;
        solidArea.y = 12;
        solidArea.width = 18;
        solidArea.height = 27;
    }

    public void loadTextures() {
        // Load zombie walking animations
        left1 = new Texture("Zombie/zombie_left_1.png");
        left2 = new Texture("Zombie/zombie_left_2.png");
        left3 = new Texture("Zombie/zombie_left_3.png");
        right1 = new Texture("Zombie/zombie_right_1.png");
        right2 = new Texture("Zombie/zombie_right_2.png");
        right3 = new Texture("Zombie/zombie_right_3.png");

        // Set initial direction sprite
        left = left1;
        right = right1;
    }

    public void update() {
        if (!isAlive) return;

        // Calculate direction to player
        float dx = gp.player.worldX - worldX;
        float dy = gp.player.worldY - worldY;
        float length = (float)Math.sqrt(dx * dx + dy * dy);

        // Normalize direction and apply speed
        if (length > 0) {
            float moveX = (dx / length) * speed;
            float moveY = (dy / length) * speed;

            // Update direction for sprite selection
            if (moveX < 0) {
                direction = "left";
            } else {
                direction = "right";
            }

            // Check collisions before moving
            if (!gp.cChecker.checkTileCollision(worldX + moveX, worldY)) {
                worldX += moveX;
            }
            if (!gp.cChecker.checkTileCollision(worldX, worldY + moveY)) {
                worldY += moveY;
            }
        }

        // Update animation
        updateAnimation();
    }

    public void updateAnimation() {
        spriteCounter++;
        if (spriteCounter > 20) {
            switch (spriteNum) {
                case 1:
                    left = left1;
                    right = right1;
                    spriteNum = 2;
                    break;
                case 2:
                    left = left2;
                    right = right2;
                    spriteNum = 3;
                    break;
                case 3:
                    left = left3;
                    right = right3;
                    spriteNum = 1;
                    break;
            }
            spriteCounter = 0;
        }
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            isAlive = false;
            currentHealth = 0;
            gp.score++;
        }
    }

    public boolean checkPlayerCollision() {
        Rectangle zombieArea = new Rectangle(
            (int)(worldX + solidArea.x),
            (int)(worldY + solidArea.y),
            solidArea.width,
            solidArea.height
        );

        Rectangle playerArea = new Rectangle(
            (int)(gp.player.worldX + gp.player.solidArea.x),
            (int)(gp.player.worldY + gp.player.solidArea.y),
            gp.player.solidArea.width,
            gp.player.solidArea.height
        );

        return zombieArea.intersects(playerArea);
    }

    public void dispose() {
        left1.dispose();
        left2.dispose();
        left3.dispose();
        right1.dispose();
        right2.dispose();
        right3.dispose();
    }
}
