package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Player {

    GamePanel gp;

    public int maxHealth = 100;
    public int health = maxHealth;
    public int speed = 1;

    private float damageCooldown = 0.5f;
    private float timeSinceLastDamage = 0;

    public Texture left, right;
    public Texture left1, left2, left3, left4, right1, right2, right3, right4;
    public Texture leftStill1, leftStill2, leftStill3, rightStill1, rightStill2, rightStill3;
    public String spriteDirection = "right";
    public String direction = "down";

    public Gun currentGun;
    public Gun pistol;
    public Gun rifle;

    public int spriteCounter = 0;
    public int worldX = 14 * GamePanel.tileSize;
    public int worldY = 21 * GamePanel.tileSize;

    public Rectangle solidArea = new Rectangle();

    public Player(GamePanel gp) {
        this.gp = gp;

        // Initialize solid area for collision detection
        solidArea.x = 15;
        solidArea.y = 12;
        solidArea.width = 18;
        solidArea.height = 27;

        // Initialize guns
        rifle = new Gun("Rifle", 25, 0.1f, true);  // 0.1s between shots (automatic)
        pistol = new Gun("Pistol", 40, 0.5f, false); // 0.5s between shots (semi-automatic)
        currentGun = pistol;
    }

    public void update(float deltaTime) {
        timeSinceLastDamage += deltaTime;
        currentGun.lastShotTime += deltaTime;

        if(gp.mouseH.isShooting && currentGun.lastShotTime >= currentGun.fireRate) {
            gp.shoot(worldX, worldY, gp.mouseH.mouseX, gp.mouseH.mouseY);
            currentGun.lastShotTime = 0;

            if(!currentGun.isAutomatic()) {
                gp.mouseH.isShooting = false;
            }
        }
    }

    public boolean canTakeDamage() {
        return timeSinceLastDamage >= damageCooldown;
    }

    public void takeDamage(int damage) {
        if(canTakeDamage()) {
            health -= damage;
            timeSinceLastDamage = 0;
        }
    }

    public void dispose() {
        left1.dispose();
        left2.dispose();
        left3.dispose();
        left4.dispose();
        leftStill1.dispose();
        leftStill2.dispose();
        leftStill3.dispose();
        right1.dispose();
        right2.dispose();
        right3.dispose();
        right4.dispose();
        rightStill1.dispose();
        rightStill2.dispose();
        rightStill3.dispose();
    }
}
