package com.zombits.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyHandler extends Input.Keys {

    GamePanel gp;

    public boolean leftPressed, rightPressed, upPressed, downPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    public void checkInput() {

        float nextX = gp.player.worldX;
        float nextY = gp.player.worldY;

        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            upPressed = true;
            gp.player.direction = "up";
            nextY++;
            if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                gp.player.worldY++;
            }
        } else {
            upPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            downPressed = true;
            gp.player.direction = "down";
            nextY--;
            if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                gp.player.worldY--;
            }
        } else {
            downPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            leftPressed = true;
            gp.player.spriteDirection = "left";
            nextX--;
            gp.player.direction = "left";
            if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                gp.player.worldX--;
            }
        } else {
            leftPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            rightPressed = true;
            gp.player.spriteDirection = "right";
            nextX++;
            gp.player.direction = "right";
            if(!gp.cChecker.checkTileCollision(nextX, nextY)) {
                gp.player.worldX++;
            }
        } else {
            rightPressed = false;
        }
    }

}
