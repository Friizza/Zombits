package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    GamePanel gp;

    public SpriteBatch playerBatch;
    public Texture down, up, left, right;
    public Texture downStill, down1, down2, upStill, up1, up2, leftStill, left1, left2, rightStill, right1, right2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int worldX = 100;
    public int worldY = 100;

    public Player(GamePanel gp) {
        this.gp = gp;
    }
}
