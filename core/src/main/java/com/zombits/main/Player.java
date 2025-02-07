package com.zombits.main;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Player {

    GamePanel gp;

    public Texture left, right;
    public Texture left1, left2, left3, left4, right1, right2, right3, right4;
    public Texture leftStill1, leftStill2, leftStill3, rightStill1, rightStill2, rightStill3;
    public String spriteDirection = "right";
    public String direction = "down";

    public int spriteCounter = 0;
    public int worldX = 14 * GamePanel.tileSize;
    public int worldY = 21 * GamePanel.tileSize;

    public Rectangle solidArea = new Rectangle();

    public Player(GamePanel gp) {
        this.gp = gp;

        solidArea.x = 15;
        solidArea.y = 12;
        solidArea.width = 18;
        solidArea.height = 27;
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
