package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;

public class Bullet {

    GamePanel gp;

    public float x, y;
    public float dirX, dirY;
    public float speed = 10f;
    public Texture texture;

    public Bullet(GamePanel gp, float startX, float startY, float targetX, float targetY) {
        x = startX;
        y = startY;

        // Calculate direction vector
        float dx = targetX - startX;
        float dy = targetY - startY;
        float length = (float)Math.sqrt(dx * dx + dy * dy);

        // Normalize direction
        dirX = dx / length;
        dirY = dy / length;
    }

    public void update() {
        x += dirX * speed;
        y += dirY * speed;
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
