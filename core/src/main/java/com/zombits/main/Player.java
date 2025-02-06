package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;

public class Player {

    GamePanel gp;

    public Texture down, up, left, right;
    public Texture downStill, down1, down2, upStill, up1, up2, leftStill, left1, left2, rightStill, right1, right2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int worldX = 49 * GamePanel.tileSize;
    public int worldY = 31 * GamePanel.tileSize;

    public Player(GamePanel gp) {
        this.gp = gp;
    }

    public void dispose() {
        down1.dispose();
        down2.dispose();
        downStill.dispose();
        up1.dispose();
        up2.dispose();
        upStill.dispose();
        left1.dispose();
        left2.dispose();
        leftStill.dispose();
        right1.dispose();
        right2.dispose();
        rightStill.dispose();
    }
}
